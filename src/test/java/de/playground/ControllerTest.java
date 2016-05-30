package de.playground;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;

import de.playground.model.Jdk8Model;
import de.playground.model.SimpleModel;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "spring.data.mongodb.host=localhost"})
@SpringApplicationConfiguration(classes = {Application.class})
public class ControllerTest {
  private final Instant instant = Instant.ofEpochSecond(1000000000L);

  @Autowired
  private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;
  @Autowired
  private SimpleRepository simpleRepository;

  @Autowired
  private Jdk8Repository jdk8Repository;

  @Value("${local.server.port}")
  private int port;

  private RequestSpecBuilder requestSpecBuilder;

  private ObjectMapper objectMapper;

  @Before
  public void setup() {
    this.requestSpecBuilder = new RequestSpecBuilder().setConfig(RestAssuredConfig.config().objectMapperConfig(
        new ObjectMapperConfig().jackson2ObjectMapperFactory((aClass, s) -> jackson2ObjectMapperBuilder.build())));
    requestSpecBuilder.setContentType("application/json");
    requestSpecBuilder.addHeader("Accept", ContentType.JSON.getAcceptHeader());
    requestSpecBuilder.setBaseUri("http://localhost");
    requestSpecBuilder.setPort(port);
    simpleRepository.deleteAll();
    jdk8Repository.deleteAll();
    objectMapper = jackson2ObjectMapperBuilder.build();
  }

  @Test
  public void get_normal() throws IOException {
    Jdk8Model expected = new Jdk8Model("someId", "someString", Optional.of("someOptional"), instant);
    jdk8Repository.insert(expected);

    String body = given().
        spec(requestSpecBuilder.build()).
        //pathParam("id", "someId").
            when().
            get("/getObject/someId").
            then().
            statusCode(200).extract().body().asString();

    Jdk8Model result = objectMapper.readValue(body, Jdk8Model.class);
    assertThat(expected, equalTo(result));
  }
  @Test
  public void get_with_null() throws IOException {
    // when inserting null into repository, this value is missing in mongo
   //  fetching this from mongo into optional results in optional.empty

    Jdk8Model toInsert = new Jdk8Model("someId", "someString", null, instant);
    jdk8Repository.insert(toInsert);

    String body = given().
        spec(requestSpecBuilder.build()).
        //pathParam("id", "someId").
            when().
            get("/getObject/someId").
            then().
            statusCode(200).extract().body().asString();

    Jdk8Model result = objectMapper.readValue(body, Jdk8Model.class);

    Jdk8Model expected = new Jdk8Model("someId", "someString", Optional.empty(), instant);
    assertThat(expected, equalTo(result));
  }


  @Test
  public void get_with_empty_optional() throws IOException {
    // when inserting optional.empty into repository, this value is optionalString" : {  },
    //  fetching this from mongo into optional results in optional.empty

    Jdk8Model toInsert = new Jdk8Model("someId", "someString", Optional.empty(), instant);
    jdk8Repository.insert(toInsert);

    String body = given().
        spec(requestSpecBuilder.build()).
        //pathParam("id", "someId").
            when().
            get("/getObject/someId").
            then().
            statusCode(200).extract().body().asString();

    Jdk8Model result = objectMapper.readValue(body, Jdk8Model.class);

    Jdk8Model expected = new Jdk8Model("someId", "someString", Optional.empty(), instant);
    assertThat(expected, equalTo(result));
  }

  @Test
  public void put_normal() throws JsonProcessingException {
    // not delivering id by rest call is ok, it serializes to null. putting this in mongo leads to id creation
    String json = "{'string':'the_string','optionalString':'the_optional','instant':'2001-09-09T01:46:40Z'}"
        .replaceAll("'", "\"");

    given().
        spec(requestSpecBuilder.build()).
        body(json).
        when().
        put("/putObject").
        then().
        statusCode(200);

    List<Jdk8Model> all = jdk8Repository.findAll();
    Jdk8Model first = all.get(0);
    assertThat(first.optionalString,equalTo(Optional.of("the_optional")));
    assertThat(first.string,equalTo("the_string"));
    assertThat(first.id, is(not(nullValue())));
    assertThat(first.instant, equalTo(instant));

    System.out.println(first.id);
  }

  @Test
  public void put_with_null() throws JsonProcessingException {

    String json =
        "{'string':'the_string','optionalString':null,'instant':'2016-05-12T10:58:59.191Z'}".replaceAll("'", "\"");
    given().
        spec(requestSpecBuilder.build()).
        body(json).
        when().
        put("/putObject").
        then().
        statusCode(200);
  }

  @Test
  public void put_without_optional() throws JsonProcessingException {
    String json = "{'string':'the_string','instant':'2016-05-12T10:58:59.191Z'}".replaceAll("'", "\"");
    given().
        spec(requestSpecBuilder.build()).
        body(json).
        when().
        put("/putObject").
        then().
        statusCode(200);
  }

  @Test
  public void put_with_invalid_keys() throws JsonProcessingException {
    String json =
        "{'string':'the_string','optionalString':'the_optional','instant':'2016-05-12T10:58:59.191Z', 'invalid':'invalid'}"
            .replaceAll("'", "\"");
    given().
        spec(requestSpecBuilder.build()).
        body(json).
        when().
        put("/putObject").
        then().
        statusCode(200);
  }

  @Test
  public void put_simple_() throws JsonProcessingException {
    String json = "{'id':'someId', 'string':'the_string'}".replaceAll("'", "\"");
    given().
        spec(requestSpecBuilder.build()).
        body(json).
        when().
        put("/putSimpleObject").
        then().
        statusCode(200);
  }

  @Test
  public void get_simple() {
    simpleRepository.insert(new SimpleModel("someId", "someString"));
    given().
        spec(requestSpecBuilder.build()).
        when().
        get("/getSimpleObject/someId").
        then().
        statusCode(200);
  }

}