package com.cosium.spring.data.jpa.entity.graph.domain;

import java.util.List;

/**
 * Created on 04/08/18.
 *
 * @author Reda.Housni-Alaoui
 */
public final class EntityGraphs {

  private static final EntityGraph NONE = new NoEntityGraph();

  private EntityGraphs() {}

  /**
   * @param name The name of the targeted EntityGraph
   * @return An EntityGraph referenced by name
   */
  public static EntityGraph named(String name) {
    return new NamedEntityGraph(name);
  }

  /** @return An EntityGraph representing the absence of entity graph */
  public static EntityGraph none() {
    return NONE;
  }

  private static final class NoEntityGraph implements EntityGraph {

    @Override
    public EntityGraphType getEntityGraphType() {
      return null;
    }

    @Override
    public String getEntityGraphName() {
      return null;
    }

    @Override
    public List<String> getEntityGraphAttributePaths() {
      return null;
    }

    @Override
    public boolean isOptional() {
      return false;
    }
  }
}
