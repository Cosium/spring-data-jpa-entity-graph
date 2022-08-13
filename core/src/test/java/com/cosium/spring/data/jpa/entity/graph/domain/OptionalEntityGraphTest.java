package com.cosium.spring.data.jpa.entity.graph.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Created on 04/08/18.
 *
 * @author Reda.Housni-Alaoui
 */
class OptionalEntityGraphTest {

  @Test
  void testEmpty() {
    EntityGraph emptyGraph = EntityGraphs.empty();
    assertThat(OptionalEntityGraph.of(emptyGraph)).isEmpty();
  }

  @Test
  void testNonEmpty() {
    EntityGraph fooGraph = EntityGraphs.named("foo");
    assertThat(OptionalEntityGraph.of(fooGraph)).contains(fooGraph);
  }
}
