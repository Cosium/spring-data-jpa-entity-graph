package com.cosium.spring.data.jpa.entity.graph.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NamedEntityGraphTest {

  @Test
  @DisplayName("Test graphs with same names equal")
  void test1() {
    final EntityGraph graph1 = EntityGraphs.named("graph");
    final EntityGraph graph2 = EntityGraphs.named("graph");
    assertThat(graph1).isEqualTo(graph2).hasSameHashCodeAs(graph2);
  }

  @Test
  @DisplayName("Test graphs with different names not equal")
  void test2() {
    final EntityGraph graph1 = EntityGraphs.named("graph1");
    final EntityGraph graph2 = EntityGraphs.named("graph2");

    assertThat(graph1).isNotEqualTo(graph2).doesNotHaveSameHashCodeAs(graph2);
  }

  @Test
  @DisplayName("Test graphs with different types not equal")
  void test3() {
    final NamedEntityGraph graph1 = new NamedEntityGraph(EntityGraphType.LOAD, "graph");
    final NamedEntityGraph graph2 = new NamedEntityGraph(EntityGraphType.FETCH, "graph");

    assertThat(graph1).isNotEqualTo(graph2).doesNotHaveSameHashCodeAs(graph2);
  }

  @Test
  @DisplayName("Test graphs with different optionality not equal")
  void test4() {
    final EntityGraph optionalGraph = new NamedEntityGraph(EntityGraphType.LOAD, true, "graph");
    final EntityGraph requiredGraph = new NamedEntityGraph(EntityGraphType.LOAD, false, "graph");

    assertThat(optionalGraph).isNotEqualTo(requiredGraph).doesNotHaveSameHashCodeAs(requiredGraph);
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
