package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.google.common.base.MoreObjects;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.query.JpaEntityGraph;

import static java.util.Objects.requireNonNull;

/**
 * Wrapper class allowing to hold a {@link JpaEntityGraph} with its associated domain class. Created
 * on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class EntityGraphBean {

  private final JpaEntityGraph jpaEntityGraph;
  private final Class<?> domainClass;
  private final ResolvableType repositoryMethodReturnType;
  private final boolean optional;
  private final boolean primary;
  private final boolean valid;

  public EntityGraphBean(
      JpaEntityGraph jpaEntityGraph,
      Class<?> domainClass,
      ResolvableType repositoryMethodReturnType,
      boolean optional,
      boolean primary) {
    this.jpaEntityGraph = requireNonNull(jpaEntityGraph);
    this.domainClass = requireNonNull(domainClass);
    this.repositoryMethodReturnType = requireNonNull(repositoryMethodReturnType);
    this.optional = optional;
    this.primary = primary;
    this.valid = computeValidity();
  }

  private boolean computeValidity() {
    Class<?> resolvedReturnType = repositoryMethodReturnType.resolve();
    if (Void.TYPE.equals(resolvedReturnType) || domainClass.isAssignableFrom(resolvedReturnType)) {
      return true;
    }
    for (Class genericType : repositoryMethodReturnType.resolveGenerics()) {
      if (domainClass.isAssignableFrom(genericType)) {
        return true;
      }
    }
    return false;
  }

  /** @return The jpa entity graph */
  public JpaEntityGraph getJpaEntityGraph() {
    return jpaEntityGraph;
  }

  /** @return The jpa entity class */
  public Class<?> getDomainClass() {
    return domainClass;
  }

  /** @return True if this entity graph is not mandatory */
  public boolean isOptional() {
    return optional;
  }

  /** @return True if this EntityGraph seems valid */
  public boolean isValid() {
    return valid;
  }

  /**
   * @return True if this EntityGraph is a primary one. Default EntityGraph is an example of non
   *     primary EntityGraph.
   */
  public boolean isPrimary() {
    return primary;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("jpaEntityGraph", jpaEntityGraph)
        .add("domainClass", domainClass)
        .add("repositoryMethodReturnType", repositoryMethodReturnType)
        .add("optional", optional)
        .toString();
  }
}
