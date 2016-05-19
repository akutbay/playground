package de.playground;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.playground.model.Jdk8Model;

public interface Jdk8Repository extends MongoRepository<Jdk8Model, String> {

  Jdk8Model findByOptionalString(String string);
}


