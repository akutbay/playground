package de.playground;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.playground.model.Jdk8Model;
import de.playground.model.SimpleModel;

@RestController
public final class Controller {

  private final SimpleRepository simpleRepository;
  private final Jdk8Repository jdk8Repository;

  @Autowired
  public Controller(SimpleRepository simpleRepository, Jdk8Repository jdk8Repository) {
    this.simpleRepository = simpleRepository;
    this.jdk8Repository = jdk8Repository;
  }

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @RequestMapping(path = "/getObject/{id}", method = RequestMethod.GET)
  public Jdk8Model get(@PathVariable("id") String id) {
    return jdk8Repository.findOne(id);
  }

  @RequestMapping(path = "/putObject", method = RequestMethod.PUT)
  public Jdk8Model put(@RequestBody Jdk8Model model) {
    Jdk8Model newmodel = jdk8Repository.insert(model);
    return newmodel;
  }

  @RequestMapping(value = "/putSimpleObject", method = RequestMethod.PUT)
  public SimpleModel putSimple(@RequestBody SimpleModel model) {
    simpleRepository.insert(model);
    return model;
  }

  @RequestMapping(value = "/getSimpleObject/{id}", method = RequestMethod.GET)
  public SimpleModel getSimple(@PathVariable("id") String id) {
    return simpleRepository.findOne(id);
  }
}
