package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Réda Housni Alaoui
 */
public class NoopEntityGraphTest {

  @Test
  public void test() {
    assertThat(EntityGraph.NOOP.buildQueryHint(null, null)).isEmpty();
  }
}
