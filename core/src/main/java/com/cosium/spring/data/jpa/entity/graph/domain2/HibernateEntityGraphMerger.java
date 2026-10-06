package com.cosium.spring.data.jpa.entity.graph.domain2;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.hibernate.graph.EntityGraphs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bridge to {@code org.hibernate.graph.EntityGraphs#merge}.
 *
 * <ul>
 *   <li>{@code NoClassDefFoundError} (Hibernate absent at runtime): {@link Optional#empty()} with
 *       {@code DEBUG} log (normal when Hibernate is not the provider).
 *   <li>Any merge failure: {@link Optional#empty()} with {@code WARN} log, caller falls back to
 *       path union.
 * </ul>
 */
final class HibernateEntityGraphMerger {

  private static final Logger LOGGER = LoggerFactory.getLogger(HibernateEntityGraphMerger.class);

  @SuppressWarnings({"rawtypes", "unchecked"})
  Optional<jakarta.persistence.EntityGraph<?>> merge(
      EntityManager entityManager,
      Class<?> entityType,
      List<jakarta.persistence.EntityGraph<?>> graphs) {
    try {
      // EntityGraphs.merge(EntityManager, Class<T>, List<? extends Graph<T>>)
      jakarta.persistence.EntityGraph<?> merged =
          EntityGraphs.merge(entityManager, (Class) entityType, (List) graphs);
      return Optional.of(merged);
    } catch (NoClassDefFoundError | ExceptionInInitializerError e) {
      // Hibernate EntityGraphs class not available at runtime: nothing to merge
      LOGGER.debug("Hibernate EntityGraphs not available", e);
      return Optional.empty();
    } catch (Exception e) {
      LOGGER.warn("Hibernate merge failed, fallback to path union", e);
      return Optional.empty();
    }
  }
}
