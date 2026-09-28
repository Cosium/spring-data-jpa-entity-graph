package com.cosium.spring.data.jpa.entity.graph.domain2;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Merges multiple {@link EntityGraph}s into a single hint. Used by {@link
 * EntityGraph#merge(EntityGraph)}.
 */
class CompositeEntityGraph implements EntityGraph {

  private final List<EntityGraph> delegates;
  private final HibernateEntityGraphMerger hibernateMerger = new HibernateEntityGraphMerger();

  CompositeEntityGraph(List<EntityGraph> delegates) {
    this.delegates = List.copyOf(delegates);
  }

  @Override
  public Optional<EntityGraphQueryHint> buildQueryHint(
      EntityManager entityManager, Class<?> entityType) {
    List<EntityGraphQueryHint> hints =
        delegates.stream()
            .map(d -> d.buildQueryHint(entityManager, entityType))
            .flatMap(Optional::stream)
            .toList();

    if (hints.isEmpty()) {
      return Optional.empty();
    }

    if (hints.size() == 1) {
      return Optional.of(hints.get(0));
    }

    EntityGraphType type = hints.get(0).type();
    boolean homogeneous = hints.stream().allMatch(h -> h.type() == type);
    if (!homogeneous) {
      throw new IllegalArgumentException(
          "Cannot merge entity graphs with different types: LOAD and FETCH");
    }

    // Collect jakarta.persistence.EntityGraph instances
    List<jakarta.persistence.EntityGraph<?>> jpaGraphs =
        hints.stream().map(EntityGraphQueryHint::entityGraph).collect(Collectors.toList());

    // Try Hibernate merge if available
    Optional<jakarta.persistence.EntityGraph<?>> merged =
        hibernateMerger.merge(entityManager, entityType, jpaGraphs);
    if (merged.isPresent()) {
      boolean failIfInapplicable =
          hints.stream().anyMatch(EntityGraphQueryHint::failIfInapplicable);
      return Optional.of(new EntityGraphQueryHint(type, merged.get(), failIfInapplicable));
    }

    // Fallback: union of attribute paths
    LinkedHashSet<String> unionPaths = new LinkedHashSet<>();
    for (EntityGraphQueryHint hint : hints) {
      Collection<String> props = hint.toProperties();
      unionPaths.addAll(props);
    }
    List<String> sortedUnion = new ArrayList<>(unionPaths);
    // DynamicJpaEntityGraphs handles sorting internally
    jakarta.persistence.EntityGraph<?> fallbackGraph =
        DynamicJpaEntityGraphs.create(entityManager, entityType, sortedUnion);
    return Optional.of(new EntityGraphQueryHint(type, fallbackGraph));
  }
}
