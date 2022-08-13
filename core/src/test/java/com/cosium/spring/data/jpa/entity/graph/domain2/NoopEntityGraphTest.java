package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Réda Housni Alaoui
 */
class NoopEntityGraphTest {

  @Test
  void test() {
    assertThat(EntityGraph.NOOP.buildQueryHint(null, null)).isEmpty();
  }
}
