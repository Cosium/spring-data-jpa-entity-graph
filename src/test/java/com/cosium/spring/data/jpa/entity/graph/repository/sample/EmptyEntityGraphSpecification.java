package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphType;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created on 27/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public abstract class EmptyEntityGraphSpecification<T> implements Specification<T>, EntityGraph {

  private final EntityGraph entityGraph = EntityGraphUtils.empty();

  @Override
  public EntityGraphType getEntityGraphType() {
    return entityGraph.getEntityGraphType();
  }

  @Override
  public String getEntityGraphName() {
    return entityGraph.getEntityGraphName();
  }

  @Override
  public List<String> getEntityGraphAttributePaths() {
    return entityGraph.getEntityGraphAttributePaths();
  }

  @Override
  public boolean isOptional() {
    return false;
  }
}
