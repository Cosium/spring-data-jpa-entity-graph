package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class DynamicEntityGraphTest {

  @Test
  public void testGraphsWithSamePathsEqual() {
    List<String> paths = new ArrayList<>();
    paths.add("path1");
    paths.add("path2");
    paths.add("path3");
    final DynamicEntityGraph graph1 = DynamicEntityGraph.loading(paths);
    final DynamicEntityGraph graph2 = DynamicEntityGraph.loading(paths);
    assertEquals(graph1, graph2);
    assertEquals(graph1.hashCode(), graph2.hashCode());
  }

  @Test
  public void testGraphsWithDifferentTypesNotEqual() {
    List<String> paths = new ArrayList<>();
    paths.add("path1");
    paths.add("path2");
    paths.add("path3");
    final DynamicEntityGraph graph1 = new DynamicEntityGraph(EntityGraphType.FETCH, paths);
    final DynamicEntityGraph graph2 = new DynamicEntityGraph(EntityGraphType.LOAD, paths);
    assertNotEquals(graph1, graph2);
    assertNotEquals(graph1.hashCode(), graph2.hashCode());
  }

  @Test
  public void testGraphsWithDifferentClassNotEqual() {
    final EntityGraph namedEntityGraph = new NamedEntityGraph(EntityGraphType.LOAD, "graph");
    final EntityGraph dynamicEntityGraph =
        new DynamicEntityGraph(EntityGraphType.LOAD, Collections.singletonList("path"));

    assertNotEquals(dynamicEntityGraph, namedEntityGraph);
    assertNotEquals(dynamicEntityGraph.hashCode(), namedEntityGraph.hashCode());
  }
}
