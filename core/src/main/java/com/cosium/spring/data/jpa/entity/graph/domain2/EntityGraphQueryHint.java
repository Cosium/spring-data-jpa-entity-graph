package com.cosium.spring.data.jpa.entity.graph.domain2;

import static java.util.Objects.requireNonNull;

import jakarta.persistence.EntityGraph;

/**
 * @author Réda Housni Alaoui
 */
public class EntityGraphQueryHint {

  private final EntityGraphType type;
  private final jakarta.persistence.EntityGraph<?> entityGraph;
  private final boolean failIfInapplicable;

  public EntityGraphQueryHint(EntityGraphType type, EntityGraph<?> entityGraph) {
    this(type, entityGraph, true);
  }

  /**
   * @param failIfInapplicable true if an {@link
   *     com.cosium.spring.data.jpa.entity.graph.repository.exception.InapplicableEntityGraphException}
   *     must be thrown if this entity graph cannot be applied.
   */
  public EntityGraphQueryHint(
      EntityGraphType type, EntityGraph<?> entityGraph, boolean failIfInapplicable) {
    this.type = requireNonNull(type);
    this.entityGraph = requireNonNull(entityGraph);
    this.failIfInapplicable = failIfInapplicable;
  }

  public EntityGraphType type() {
    return type;
  }

  public EntityGraph<?> entityGraph() {
    return entityGraph;
  }

  /**
   * @return true if an {@link
   *     com.cosium.spring.data.jpa.entity.graph.repository.exception.InapplicableEntityGraphException}
   *     must be thrown if this entity graph cannot be applied.
   */
  public boolean failIfInapplicable() {
    return failIfInapplicable;
  }
}
