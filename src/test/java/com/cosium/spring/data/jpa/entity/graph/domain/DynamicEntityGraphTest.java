package com.cosium.spring.data.jpa.entity.graph.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Test;

public class DynamicEntityGraphTest {

  @Test
  public void testGraphsWithSamePathsEqual() {
    List<String> paths = Lists.newArrayList("path1", "path2", "path3");
    final DynamicEntityGraph graph1 = new DynamicEntityGraph(paths);
    final DynamicEntityGraph graph2 = new DynamicEntityGraph(paths);
    assertEquals(graph1, graph2);
    assertEquals(graph1.hashCode(), graph2.hashCode());
  }

  @Test
  public void testGraphsWithDifferentTypesNotEqual() {
    List<String> paths = Lists.newArrayList("path1", "path2", "path3");
    final DynamicEntityGraph graph1 = new DynamicEntityGraph(EntityGraphType.FETCH, paths);
    final DynamicEntityGraph graph2 = new DynamicEntityGraph(EntityGraphType.LOAD, paths);
    assertNotEquals(graph1, graph2);
    assertNotEquals(graph1.hashCode(), graph2.hashCode());
  }

  @Test
  public void testGraphsWithDifferentOptionalityNotEqual() {
    List<String> paths = Lists.newArrayList("path1", "path2", "path3");
    final DynamicEntityGraph optionalGraph = new DynamicEntityGraph(EntityGraphType.LOAD, paths);
    optionalGraph.setOptional(true);
    final DynamicEntityGraph requiredGraph = new DynamicEntityGraph(EntityGraphType.LOAD, paths);
    requiredGraph.setOptional(false);

    assertNotEquals(optionalGraph, requiredGraph);
    assertNotEquals(optionalGraph.hashCode(), requiredGraph.hashCode());
  }

  @Test
  public void testGraphsWithDifferentClassNotEqual() {
    final EntityGraph namedEntityGraph = new NamedEntityGraph(EntityGraphType.LOAD, "graph");
    final EntityGraph dynamicEntityGraph =
        new DynamicEntityGraph(EntityGraphType.LOAD, Lists.newArrayList("path"));

    assertNotEquals(dynamicEntityGraph, namedEntityGraph);
    assertNotEquals(dynamicEntityGraph.hashCode(), namedEntityGraph.hashCode());
  }
}
