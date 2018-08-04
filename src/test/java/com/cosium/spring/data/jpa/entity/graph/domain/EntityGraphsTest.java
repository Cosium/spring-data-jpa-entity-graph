package com.cosium.spring.data.jpa.entity.graph.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
