package de.playground;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import de.playground.model.SimpleModel;

public interface SimpleRepository extends MongoRepository<SimpleModel, String> {
}


