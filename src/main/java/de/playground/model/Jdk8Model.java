package de.playground.model;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class Jdk8Model {

  @Id
  public final String id;
  public final String string;
  public final Optional<String> optionalString;
  public final Instant instant;

  @JsonCreator
  public Jdk8Model(String id, String string, Optional<String> optionalString, Instant instant) {
    this.id = id;
    this.string = string;
    this.optionalString = optionalString;
    this.instant = instant;
  }

  @Override
  public String toString() {
    return "Jdk8Model{" +
        "string='" + string + '\'' +
        ", optionalString=" + optionalString +
        ", instant=" + instant +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Jdk8Model jdk8Model = (Jdk8Model) o;
    return Objects.equals(id, jdk8Model.id) &&
        Objects.equals(string, jdk8Model.string) &&
        Objects.equals(optionalString, jdk8Model.optionalString) &&
        Objects.equals(instant, jdk8Model.instant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, string, optionalString, instant);
  }
}
