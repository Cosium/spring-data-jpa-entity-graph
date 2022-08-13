package com.cosium.spring.data.jpa.entity.graph.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Created on 04/08/18.
 *
 * @author Reda.Housni-Alaoui
 */
public class EntityGraphsTest {

  @Test
  public void testEmpty() {
    assertThat(EntityGraphs.isEmpty(EntityGraphs.empty())).isTrue();
  }
}
