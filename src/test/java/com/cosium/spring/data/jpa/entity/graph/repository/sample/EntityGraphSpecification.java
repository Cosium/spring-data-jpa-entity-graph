package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public abstract class EntityGraphSpecification<T> implements Specification<T>, EntityGraph {

  private final String entityGraphName;
  private final boolean optional;

  public EntityGraphSpecification(String entityGraphName, boolean optional) {
    this.entityGraphName = entityGraphName;
    this.optional = optional;
  }

  public EntityGraphSpecification(String entityGraphName) {
    this(entityGraphName, false);
  }

  @Override
  public String getEntityGraphName() {
    return entityGraphName;
  }

  @Override
  public EntityGraphType getEntityGraphType() {
    return EntityGraphType.FETCH;
  }

  @Override
  public List<String> getEntityGraphAttributePaths() {
    return null;
  }

  @Override
  public boolean isOptional() {
    return optional;
  }
}
