package de.playground;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;

import de.playground.model.Jdk8Model;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "spring.data.mongodb.host=localhost"})
@SpringApplicationConfiguration(classes = {Application.class})
public class ControllerAsycServiceTest {

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
        new ObjectMapperConfig().jackson2ObjectMapperFactory((aClass, s) -> jackson2ObjectMapperBuilder.build())))
        .setContentType("application/json").addHeader("Accept", ContentType.JSON.getAcceptHeader())
        .setBaseUri("http://localhost").setPort(port);
    jdk8Repository.deleteAll();
    objectMapper = jackson2ObjectMapperBuilder.build();
  }

  @Test
  public void longRunning() throws IOException {
    createModels(IntStream.rangeClosed(1, 3));

    String body = given().
        spec(requestSpecBuilder.build()).
        //pathParam("id", "someId").
            when().
            get("/longRunning").
            then().log().all().
            statusCode(200).extract().body().asString();

  }

  private void createModels(IntStream intStream) {
    intStream.mapToObj(i -> new Jdk8Model("someId" + i, "someString", Optional.of("someOptional"), instant))
        .forEach(model -> jdk8Repository.insert(model));
  }
}