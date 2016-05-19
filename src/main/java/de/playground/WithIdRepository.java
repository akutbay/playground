package de.playground;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.playground.model.WithId;

public interface WithIdRepository extends MongoRepository<WithId, String> {

}
