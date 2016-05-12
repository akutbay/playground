package de.playground;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import de.playground.model.Jdk8Model;
import de.playground.model.SimpleModel;

public interface Jdk8Repository extends MongoRepository<Jdk8Model, String> {

}


