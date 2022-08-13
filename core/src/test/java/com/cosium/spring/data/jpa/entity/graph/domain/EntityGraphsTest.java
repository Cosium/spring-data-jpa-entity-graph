package com.cosium.spring.data.jpa.entity.graph.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Created on 04/08/18.
 *
 * @author Reda.Housni-Alaoui
 */
class EntityGraphsTest {

  @Test
  @DisplayName("Test empty")
  void test1() {
    assertThat(EntityGraphs.isEmpty(EntityGraphs.empty())).isTrue();
  }
}
