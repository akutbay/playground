package de.playground;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.playground.model.Jdk8Model;
import de.playground.model.SimpleModel;

@SpringApplicationConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class SerializerTest {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

  @Test
  public void deserialise() throws IOException {
    ObjectMapper o = jackson2ObjectMapperBuilder.build();
    String json = "{'string':'the_string','optionalString':'the_optional','instant':'2016-05-12T10:58:59.191Z'}"
        .replaceAll("'", "\"");
    printArgs(Jdk8Model.class);
    Jdk8Model m = o.readValue(json, Jdk8Model.class);
    logger.info(m.toString());

  }

  @Test
  public void simpleDeserialise() throws IOException {
    ObjectMapper o = jackson2ObjectMapperBuilder.build();
    String json = "{'string':'the_string'}".replaceAll("'", "\"");
    logger.info(json);
    printArgs(SimpleModel.class);
    SimpleModel m = o.readValue(json, SimpleModel.class);
    logger.info(m.toString());

  }

  private void printArgs(Class clazz) {
    for (Constructor<?> m : clazz.getDeclaredConstructors()) {
      logger.info(m.getName());
      for (Parameter p : m.getParameters()) {
        logger.info("  " + p.getName());
      }
    }
  }
}