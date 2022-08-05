package com.cosium.spring.data.jpa.entity.graph.domain2;

import java.util.Optional;
import javax.persistence.EntityManager;

/** @author Réda Housni Alaoui */
public interface EntityGraph {

  /**
   * @return A query hint to apply to a query. If empty, the entity graph will be ignored and
   *     therefore not applied to the query.
   */
  Optional<EntityGraphQueryHint> buildQueryHint(EntityManager entityManager, Class<?> entityType);

  /** An {@link EntityGraph} that will have zero effect on queries. */
  EntityGraph NOOP = (entityManager, entityType) -> Optional.empty();
}
