package com.cosium.spring.data.jpa.entity.graph.domain;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

/**
 * Created on 22/11/16.
 *
 * @deprecated Use {@link com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph} instead.
 * @author Reda.Housni-Alaoui
 */
@Deprecated
public interface EntityGraph extends com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph {

  /**
   * @return The type of the entity graph. May be null.
   */
  EntityGraphType getEntityGraphType();

  /**
   * @return The name to use to retrieve the EntityGraph. May be null.
   */
  String getEntityGraphName();

  /**
   * @return The attribute paths. May be null.
   */
  List<String> getEntityGraphAttributePaths();

  /**
   * @return True if the EntityGraph is optional.<br>
   *     Passing an optional EntityGraph to an unsupported method will not fail.
   */
  boolean isOptional();

  @Override
  default Optional<EntityGraphQueryHint> buildQueryHint(
      EntityManager entityManager, Class<?> entityType) {
    return new EntityGraphQueryHintFactory(entityManager, entityType, this).build();
  }
}
