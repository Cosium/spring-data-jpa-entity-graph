package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import jakarta.persistence.EntityManager;
import java.util.Optional;

/**
 * @author Réda Housni Alaoui
 */
class DefaultEntityGraph implements EntityGraph {

  private final EntityGraph delegate;

  DefaultEntityGraph(EntityGraph delegate) {
    this.delegate = delegate;
  }

  @Override
  public Optional<EntityGraphQueryHint> buildQueryHint(
      EntityManager entityManager, Class<?> entityType) {
    return delegate
        .buildQueryHint(entityManager, entityType)
        .map(hint -> new EntityGraphQueryHint(hint.type(), hint.entityGraph(), false));
  }
}
