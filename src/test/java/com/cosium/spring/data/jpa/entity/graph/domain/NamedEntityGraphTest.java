package com.cosium.spring.data.jpa.entity.graph.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.google.common.collect.Lists;
import org.junit.Test;

public class NamedEntityGraphTest {

  @Test
  public void testGraphsWithSameNamesEqual() {
    final EntityGraph graph1 = EntityGraphs.named("graph");
    final EntityGraph graph2 = EntityGraphs.named("graph");
    assertEquals(graph1, graph2);
    assertEquals(graph1.hashCode(), graph2.hashCode());
  }

  @Test
  public void testGraphsWithDifferentNamesNotEqual() {
    final EntityGraph graph1 = EntityGraphs.named("graph1");
    final EntityGraph graph2 = EntityGraphs.named("graph2");
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
  public void testGraphsWithDifferentOptionalityNotEqual() {
    final EntityGraph optionalGraph = new NamedEntityGraph(EntityGraphType.LOAD, true, "graph");
    final EntityGraph requiredGraph = new NamedEntityGraph(EntityGraphType.LOAD, false, "graph");

    assertNotEquals(optionalGraph, requiredGraph);
    assertNotEquals(optionalGraph.hashCode(), requiredGraph.hashCode());
  }

  @Test
  public void testGraphsWithDifferentClassNotEqual() {
    final EntityGraph namedEntityGraph = new NamedEntityGraph(EntityGraphType.LOAD, "graph");
    final EntityGraph dynamicEntityGraph =
        new DynamicEntityGraph(EntityGraphType.LOAD, Lists.newArrayList("path"));

    assertNotEquals(namedEntityGraph, dynamicEntityGraph);
    assertNotEquals(namedEntityGraph.hashCode(), dynamicEntityGraph.hashCode());
  }
}
