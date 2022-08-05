package com.cosium.spring.data.jpa.entity.graph.domain;

import static java.util.Objects.requireNonNull;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 * @deprecated Use {@link com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType} instead
 */
@Deprecated
public enum EntityGraphType {
  LOAD(com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType.LOAD),
  FETCH(com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType.FETCH);

  private final com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType newType;

  EntityGraphType(com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType newType) {
    this.newType = requireNonNull(newType);
  }

  public com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType newType() {
    return newType;
  }
}
