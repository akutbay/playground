package de.playground;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.playground.model.Jdk8Model;

public class ApplicationTest {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  public void java8parameters() {
    Class<Jdk8Model> clz = Jdk8Model.class;
    for (Constructor<?> m : clz.getDeclaredConstructors()) {
      logger.info(m.getName());
      for (Parameter p : m.getParameters()) {
        logger.info("  " + p.getName());
      }
    }
  }
}
