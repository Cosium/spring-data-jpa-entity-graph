package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Collections;
import org.junit.Test;

public class NamedEntityGraphTest {

  @Test
  public void testGraphsWithSameNamesEqual() {
    final EntityGraph graph1 = NamedEntityGraph.loading("graph");
    final EntityGraph graph2 = NamedEntityGraph.loading("graph");
    assertEquals(graph1, graph2);
    assertEquals(graph1.hashCode(), graph2.hashCode());
  }

  @Test
  public void testGraphsWithDifferentNamesNotEqual() {
    final EntityGraph graph1 = NamedEntityGraph.loading("graph1");
    final EntityGraph graph2 = NamedEntityGraph.loading("graph2");
    assertNotEquals(graph1, graph2);
    assertNotEquals(graph1.hashCode(), graph2.hashCode());
  }

  @Test
  public void testGraphsWithDifferentTypesNotEqual() {

    final NamedEntityGraph graph1 = new NamedEntityGraph(EntityGraphType.LOAD, "graph");
    final NamedEntityGraph graph2 = new NamedEntityGraph(EntityGraphType.FETCH, "graph");
    assertNotEquals(graph1, graph2);
    assertNotEquals(graph1.hashCode(), graph2.hashCode());
  }

  @Test
  public void testGraphsWithDifferentClassNotEqual() {
    final EntityGraph namedEntityGraph = new NamedEntityGraph(EntityGraphType.LOAD, "graph");
    final EntityGraph dynamicEntityGraph =
        new DynamicEntityGraph(EntityGraphType.LOAD, Collections.singletonList("path"));

    assertNotEquals(namedEntityGraph, dynamicEntityGraph);
    assertNotEquals(namedEntityGraph.hashCode(), dynamicEntityGraph.hashCode());
  }
}
