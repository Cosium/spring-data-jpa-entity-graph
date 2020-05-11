package com.cosium.spring.data.jpa.entity.graph.domain;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public enum EntityGraphType {
  LOAD("javax.persistence.loadgraph"),
  FETCH("javax.persistence.fetchgraph");

  private final String key;

  private EntityGraphType(String value) {
    this.key = value;
  }

  public String getKey() {
    return key;
  }
}
