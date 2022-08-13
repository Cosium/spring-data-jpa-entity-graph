package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType;
import java.util.Map;

/**
 * Created on 24/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class QueryHintsUtils {

  private QueryHintsUtils() {}

  /**
   * @return True if the QueryHints already hold an EntityGraph
   */
  public static boolean containsEntityGraph(Map<String, Object> queryHints) {
    return queryHints != null
        && (queryHints.containsKey(EntityGraphType.FETCH.key())
            || queryHints.containsKey(EntityGraphType.LOAD.key()));
  }

  /** Remove all EntityGraph pre-existing in the QueryHints */
  public static void removeEntityGraphs(Map<String, Object> queryHints) {
    if (queryHints == null) {
      return;
    }
    queryHints.remove(EntityGraphType.FETCH.key());
    queryHints.remove(EntityGraphType.LOAD.key());
  }
}
