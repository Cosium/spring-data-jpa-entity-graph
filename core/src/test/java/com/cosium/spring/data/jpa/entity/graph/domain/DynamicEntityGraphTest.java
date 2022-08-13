package com.cosium.spring.data.jpa.entity.graph.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DynamicEntityGraphTest {

  @Test
  @DisplayName("Test graphs with same paths equal")
  void test1() {
    List<String> paths = new ArrayList<>();
    paths.add("path1");
    paths.add("path2");
    paths.add("path3");
    final DynamicEntityGraph graph1 = new DynamicEntityGraph(paths);
    final DynamicEntityGraph graph2 = new DynamicEntityGraph(paths);

    assertThat(graph1).isEqualTo(graph2).hasSameHashCodeAs(graph2);
  }

  @Test
  @DisplayName("Test graphs with different types not equal")
  void test2() {
    List<String> paths = new ArrayList<>();
    paths.add("path1");
    paths.add("path2");
    paths.add("path3");
    final DynamicEntityGraph graph1 = new DynamicEntityGraph(EntityGraphType.FETCH, paths);
    final DynamicEntityGraph graph2 = new DynamicEntityGraph(EntityGraphType.LOAD, paths);

    assertThat(graph1).isNotEqualTo(graph2).doesNotHaveSameHashCodeAs(graph2);
  }

  @Test
  @DisplayName("Test graphs with different optionality not equal")
  void test3() {
    List<String> paths = new ArrayList<>();
    paths.add("path1");
    paths.add("path2");
    paths.add("path3");
    final DynamicEntityGraph optionalGraph = new DynamicEntityGraph(EntityGraphType.LOAD, paths);
    optionalGraph.setOptional(true);
    final DynamicEntityGraph requiredGraph = new DynamicEntityGraph(EntityGraphType.LOAD, paths);
    requiredGraph.setOptional(false);

    assertThat(optionalGraph).isNotEqualTo(requiredGraph).doesNotHaveSameHashCodeAs(requiredGraph);
  }

  @Test
  @DisplayName("Test graphs with different class not equal")
  void test4() {
    final EntityGraph namedEntityGraph = new NamedEntityGraph(EntityGraphType.LOAD, "graph");
    final EntityGraph dynamicEntityGraph =
        new DynamicEntityGraph(EntityGraphType.LOAD, Collections.singletonList("path"));

    assertThat(dynamicEntityGraph)
        .isNotEqualTo(namedEntityGraph)
        .doesNotHaveSameHashCodeAs(namedEntityGraph);
  }
}
