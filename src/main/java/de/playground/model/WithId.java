package de.playground.model;

import java.util.Optional;

import org.springframework.data.annotation.Id;

import com.google.common.base.MoreObjects;

public class WithId {

  @Id
  public final String id;

  public final Optional<String> optionalString;

  public WithId(String id, Optional<String> optionalString) {
    this.id = id;
    this.optionalString = optionalString;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("optionalString", optionalString).toString();
  }

}
