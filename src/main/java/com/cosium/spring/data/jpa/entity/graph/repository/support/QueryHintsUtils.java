package com.cosium.spring.data.jpa.entity.graph.repository.support;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.query.Jpa21Utils;

import javax.persistence.EntityManager;
import java.util.Map;

/**
 * Created on 24/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class QueryHintsUtils {
  /**
   * @param queryHints
   * @return True if the QueryHints already hold an EntityGraph
   */
  static boolean containsEntityGraph(Map<String, Object> queryHints) {
    return queryHints != null
        && (queryHints.containsKey(EntityGraph.EntityGraphType.FETCH.getKey())
            || queryHints.containsKey(EntityGraph.EntityGraphType.LOAD.getKey()));
  }

  /**
   * Remove all EntityGraph pre existing in the QueryHints
   *
   * @param queryHints
   */
  static void removeEntityGraphs(Map<String, Object> queryHints) {
    if (queryHints == null) {
      return;
    }
    queryHints.remove(EntityGraph.EntityGraphType.FETCH.getKey());
    queryHints.remove(EntityGraph.EntityGraphType.LOAD.getKey());
  }

  static Map<String, Object> buildQueryHints(
      EntityManager entityManager, EntityGraphBean entityGraph) {
    return Jpa21Utils.tryGetFetchGraphHints(
        entityManager, entityGraph.getJpaEntityGraph(), entityGraph.getDomainClass());
  }
}
