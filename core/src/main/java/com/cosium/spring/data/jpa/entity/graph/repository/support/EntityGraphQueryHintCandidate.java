package com.cosium.spring.data.jpa.entity.graph.repository.support;

import static java.util.Objects.requireNonNull;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;

/**
 * @author Réda Housni Alaoui
 */
record EntityGraphQueryHintCandidate(
    EntityGraphQueryHint queryHint, Class<?> domainClass, boolean primary) {

  EntityGraphQueryHintCandidate(
      EntityGraphQueryHint queryHint, Class<?> domainClass, boolean primary) {
    this.queryHint = requireNonNull(queryHint);
    this.domainClass = requireNonNull(domainClass);
    this.primary = primary;
  }
}
