package com.cosium.spring.data.jpa.entity.graph.domain;

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import java.util.Optional;
import javax.persistence.EntityManager;

/**
 * @author Réda Housni Alaoui
 * @deprecated Without replacement
 */
@Deprecated
public class EntityGraphQueryHintFactory {

  private final EntityManager entityManager;
  private final Class<?> entityType;
  private final EntityGraph entityGraph;

  public EntityGraphQueryHintFactory(
      EntityManager entityManager, Class<?> entityType, EntityGraph entityGraph) {
    this.entityManager = entityManager;
    this.entityType = entityType;
    this.entityGraph = entityGraph;
  }

  public Optional<EntityGraphQueryHint> build() {
    if (EntityGraphs.isEmpty(entityGraph)) {
      return Optional.empty();
    }

    EntityGraphQueryHint queryHint =
        Optional.ofNullable(entityGraph.getEntityGraphName())
            .map(name -> new NamedEntityGraph(entityGraph.getEntityGraphType().newType(), name))
            .flatMap(this::build)
            .orElse(null);
    if (queryHint != null) {
      return Optional.of(queryHint);
    }

    return Optional.ofNullable(entityGraph.getEntityGraphAttributePaths())
        .map(paths -> new DynamicEntityGraph(entityGraph.getEntityGraphType().newType(), paths))
        .flatMap(this::build);
  }

  private Optional<EntityGraphQueryHint> build(
      com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph newEntityGraph) {
    return newEntityGraph
        .buildQueryHint(entityManager, entityType)
        .map(
            hint ->
                new EntityGraphQueryHint(
                    hint.type(), hint.entityGraph(), !entityGraph.isOptional()));
  }
}
