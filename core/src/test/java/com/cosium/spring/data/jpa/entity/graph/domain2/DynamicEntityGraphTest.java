package com.cosium.spring.data.jpa.entity.graph.domain2;

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
    final DynamicEntityGraph graph1 = DynamicEntityGraph.loading(paths);
    final DynamicEntityGraph graph2 = DynamicEntityGraph.loading(paths);

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
  @DisplayName("Test graphs with different class not equal")
  void test3() {
    final EntityGraph namedEntityGraph = new NamedEntityGraph(EntityGraphType.LOAD, "graph");
    final EntityGraph dynamicEntityGraph =
        new DynamicEntityGraph(EntityGraphType.LOAD, Collections.singletonList("path"));

    assertThat(dynamicEntityGraph)
        .isNotEqualTo(namedEntityGraph)
        .doesNotHaveSameHashCodeAs(namedEntityGraph);
  }
}
