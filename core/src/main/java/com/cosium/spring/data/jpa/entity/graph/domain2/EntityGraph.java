package com.cosium.spring.data.jpa.entity.graph.domain2;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Réda Housni Alaoui
 */
public interface EntityGraph {

  /**
   * @return A query hint to apply to a query. If empty, the entity graph will be ignored and
   *     therefore not applied to the query.
   */
  Optional<EntityGraphQueryHint> buildQueryHint(EntityManager entityManager, Class<?> entityType);

  default <T> T execute(Function<EntityGraph, T> function) {
    return function.apply(this);
  }

  default EntityGraph merge(EntityGraph other) {
    if (other == null || other == NOOP) {
      return this;
    }
    if (this == NOOP) {
      return other;
    }
    return new CompositeEntityGraph(java.util.List.of(this, other));
  }

  default EntityGraph and(EntityGraph other) {
    return merge(other);
  }

  /** An {@link EntityGraph} that will have zero effect on queries. */
  EntityGraph NOOP = (entityManager, entityType) -> Optional.empty();
}
