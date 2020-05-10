package com.cosium.spring.data.jpa.entity.graph.domain;

import java.util.List;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public interface EntityGraph {

  /** @return The type of the entity graph. May be null. */
  EntityGraphType getEntityGraphType();

  /** @return The name to use to retrieve the EntityGraph. May be null. */
  String getEntityGraphName();

  /** @return The attribute paths. May be null. */
  List<String> getEntityGraphAttributePaths();

  /**
   * @return True if the EntityGraph is optional.<br>
   *     Passing an optional EntityGraph to an unsupported method will not trigger {@link
   *     com.cosium.spring.data.jpa.entity.graph.repository.exception.InapplicableEntityGraphException}.
   */
  boolean isOptional();
}
