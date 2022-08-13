package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.Test;

class NamedEntityGraphTest {

  @Test
  void testGraphsWithSameNamesEqual() {
    final EntityGraph graph1 = NamedEntityGraph.loading("graph");
    final EntityGraph graph2 = NamedEntityGraph.loading("graph");

    assertThat(graph1).isEqualTo(graph2).hasSameHashCodeAs(graph2);
  }

  @Test
  void testGraphsWithDifferentNamesNotEqual() {
    final EntityGraph graph1 = NamedEntityGraph.loading("graph1");
    final EntityGraph graph2 = NamedEntityGraph.loading("graph2");

    assertThat(graph1).isNotEqualTo(graph2).doesNotHaveSameHashCodeAs(graph2);
  }

  @Test
  void testGraphsWithDifferentTypesNotEqual() {
    final NamedEntityGraph graph1 = new NamedEntityGraph(EntityGraphType.LOAD, "graph");
    final NamedEntityGraph graph2 = new NamedEntityGraph(EntityGraphType.FETCH, "graph");

    assertThat(graph1).isNotEqualTo(graph2).doesNotHaveSameHashCodeAs(graph2);
  }

  @Test
  void testGraphsWithDifferentClassNotEqual() {
    final EntityGraph namedEntityGraph = new NamedEntityGraph(EntityGraphType.LOAD, "graph");
    final EntityGraph dynamicEntityGraph =
        new DynamicEntityGraph(EntityGraphType.LOAD, Collections.singletonList("path"));

    assertThat(namedEntityGraph)
        .isNotEqualTo(dynamicEntityGraph)
        .doesNotHaveSameHashCodeAs(dynamicEntityGraph);
  }
}
