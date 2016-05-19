package de.playground;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import de.playground.model.Jdk8Model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "spring.data.mongodb.host=192.168.99.100"})
@SpringApplicationConfiguration(classes = {Application.class})
public class Jdk8RepositoryTest {

  private final Instant instant = Instant.ofEpochSecond(1000000000L);

  @Autowired
  private Jdk8Repository jdk8Repository;

  @Before
  public void setup() {
    jdk8Repository.deleteAll();
  }

  @Test
  public void get_normal() throws IOException {
    // is optionalstring converted to normal string?
    Jdk8Model expected1 = new Jdk8Model("1", "someString", Optional.empty(), instant);
    Jdk8Model expected2 = new Jdk8Model("2", "someString", null, instant);
    Jdk8Model expected3 = new Jdk8Model("3", "someString", Optional.of("someOptional"), instant);
    jdk8Repository.insert(expected1);
    jdk8Repository.insert(expected2);
    jdk8Repository.insert(expected3);
    Jdk8Model result = jdk8Repository.findByOptionalString("someOptional");
    assertThat(result, equalTo(expected3)); // yey
  }

  @Test
  public void getStrange() throws IOException {
    // how are they deserialised?
    Jdk8Model expected1 = new Jdk8Model("1", "someString1", Optional.empty(), instant);
    Jdk8Model expected2 = new Jdk8Model("2", "someString2", null, instant);
    Jdk8Model expected3 = new Jdk8Model("3", "someString3", Optional.of("someOptional"), instant);
    jdk8Repository.insert(expected1);
    jdk8Repository.insert(expected2);
    jdk8Repository.insert(expected3);
    Jdk8Model result1 = jdk8Repository.findOne("1");
    Jdk8Model result2 = jdk8Repository.findOne("2");
    Jdk8Model result3 = jdk8Repository.findOne("3");
    //assertThat(result1, equalTo(new Jdk8Model("1", "someString1", Optional.empty(), instant)));
    //assertThat(result2, equalTo(new Jdk8Model("2", "someString2", Optional.empty(), instant)));
    assertThat(result3, equalTo(new Jdk8Model("3", "someString3", Optional.of("someOptional"), instant)));
  }

}