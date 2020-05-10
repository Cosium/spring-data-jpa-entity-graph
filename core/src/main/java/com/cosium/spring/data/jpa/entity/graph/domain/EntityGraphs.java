package com.cosium.spring.data.jpa.entity.graph.domain;

import java.util.List;

/**
 * Created on 04/08/18.
 *
 * @author Reda.Housni-Alaoui
 */
public final class EntityGraphs {

  private static final EntityGraph EMPTY = new EmptyEntityGraph();

  private EntityGraphs() {}

  /**
   * @param name The name of the targeted EntityGraph
   * @return An EntityGraph referenced by name
   */
  public static EntityGraph named(String name) {
    return new NamedEntityGraph(name);
  }

  /** @return An EntityGraph representing the absence of entity graph */
  public static EntityGraph empty() {
    return EMPTY;
  }

  public static boolean isEmpty(EntityGraph entityGraph) {
    return entityGraph == null
        || (entityGraph.getEntityGraphAttributePaths() == null
            && entityGraph.getEntityGraphName() == null
            && entityGraph.getEntityGraphType() == null);
  }

  private static final class EmptyEntityGraph implements EntityGraph {

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
