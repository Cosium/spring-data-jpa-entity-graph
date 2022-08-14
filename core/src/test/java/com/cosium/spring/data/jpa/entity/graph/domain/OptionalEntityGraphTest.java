package com.cosium.spring.data.jpa.entity.graph.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Created on 04/08/18.
 *
 * @author Reda.Housni-Alaoui
 */
class OptionalEntityGraphTest {

  @Test
  @DisplayName("Test empty")
  void test1() {
    EntityGraph emptyGraph = EntityGraphs.empty();
    assertThat(OptionalEntityGraph.of(emptyGraph)).isEmpty();
  }

  @Test
  @DisplayName("Test non empty")
  void test2() {
    EntityGraph fooGraph = EntityGraphs.named("foo");
    assertThat(OptionalEntityGraph.of(fooGraph)).contains(fooGraph);
  }
}
