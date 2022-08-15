package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.MultipleDefaultEntityGraphException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import org.slf4j.LoggerFactory;

/**
 * @author Réda Housni Alaoui
 */
class LegacyDefaultEntityGraphs implements DefaultEntityGraphs {

  private static final AtomicInteger DEPRECATION_LOG_COUNT = new AtomicInteger();
  private static final int MAX_DEPRECATION_LOG_COUNT = 10;

  private final DefaultEntityGraph defaultEntityGraph;

  public LegacyDefaultEntityGraphs(EntityManager entityManager, Class<?> domainType) {
    this.defaultEntityGraph = findOne(entityManager, domainType).orElse(null);
  }

  @Override
  public Optional<DefaultEntityGraph> findOne(Object repository) {
    return Optional.ofNullable(defaultEntityGraph);
  }

  private static Optional<DefaultEntityGraph> findOne(
      EntityManager entityManager, Class<?> domainType) {
    String deprecatedDefaultEntityGraphName =
        findDefaultEntityGraphName(entityManager, domainType).orElse(null);
    if (deprecatedDefaultEntityGraphName != null
        && DEPRECATION_LOG_COUNT.get() < MAX_DEPRECATION_LOG_COUNT) {
      DEPRECATION_LOG_COUNT.incrementAndGet();
      LoggerFactory.getLogger("LegacyDefaultEntityGraphs")
          .warn(
              "Found 'Default EntityGraph' named '{}' for '{}'. "
                  + "'Default EntityGraph by name pattern' feature is deprecated. "
                  + "It will be removed in a future version. "
                  + "Read https://github.com/Cosium/spring-data-jpa-entity-graph/issues/73#issue-1330079585 and https://github.com/Cosium/spring-data-jpa-entity-graph/issues/75 for more information.",
              deprecatedDefaultEntityGraphName,
              domainType);
    }
    return Optional.ofNullable(deprecatedDefaultEntityGraphName)
        .map(NamedEntityGraph::loading)
        .map(DefaultEntityGraph::new);
  }

  /**
   * @return The default entity graph if it exists. Null otherwise.
   */
  private static Optional<String> findDefaultEntityGraphName(
      EntityManager entityManager, Class<?> domainType) {
    String defaultEntityGraphName = null;
    List<EntityGraph<?>> entityGraphs =
        (List<EntityGraph<?>>) entityManager.getEntityGraphs(domainType);
    for (EntityGraph<?> entityGraph : entityGraphs) {
      if (!entityGraph.getName().endsWith(".default")) {
        continue;
      }
      if (defaultEntityGraphName != null) {
        throw new MultipleDefaultEntityGraphException(
            entityGraph.getName(), defaultEntityGraphName);
      }
      defaultEntityGraphName = entityGraph.getName();
    }
    return Optional.ofNullable(defaultEntityGraphName);
  }
}
