package com.cosium.spring.data.jpa.entity.graph.sample;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Access(value = AccessType.PROPERTY)
  private long id;

  private String login;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
