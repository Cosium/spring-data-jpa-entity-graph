package com.cosium.spring.data.jpa.entity.graph.domain;

import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class EntityGraphUtils {

  /**
   * @return An empty EntityGraph
   * @deprecated Use {@link EntityGraphs#empty()}
   */
  @Deprecated
  public static EntityGraph empty() {
    return EntityGraphs.empty();
  }

  /**
   * @param name The name of the targeted EntityGraph
   * @return An EntityGraph referenced by name
   * @deprecated Use {@link EntityGraphs#named(String)}
   */
  @Deprecated
  public static EntityGraph fromName(String name) {
    return EntityGraphs.named(name);
  }

  /**
   * @param name The name of the targeted EntityGraph
   * @param optional Is the EntityGraph usage optional?
   * @return An EntityGraph referenced by name
   */
  public static EntityGraph fromName(String name, boolean optional) {
    NamedEntityGraph namedEntityGraph = new NamedEntityGraph(name);
    namedEntityGraph.setOptional(optional);
    return namedEntityGraph;
  }

  /**
   * @param attributePaths The attribute paths to be present in the result
   * @return A {@link DynamicEntityGraph} with the path attributes passed in as arguments.
   */
  public static EntityGraph fromAttributePaths(String... attributePaths) {
    Assert.notEmpty(attributePaths, "At least one attribute path is required.");
    return new DynamicEntityGraph(Arrays.asList(attributePaths));
  }
}
