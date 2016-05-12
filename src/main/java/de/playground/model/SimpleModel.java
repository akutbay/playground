package de.playground.model;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SimpleModel {

  @Id
  public final String id;

  public final String string;

  @JsonCreator
  public SimpleModel(String id, String string) {
    this.string = string;
    this.id = id;
  }
}
