package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Réda Housni Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
class NoopEntityGraphTest extends BaseTest {

  @Autowired private EntityManager entityManager;

  @Test
  void test() {
    assertThat(EntityGraph.NOOP.buildQueryHint(entityManager, Object.class)).isEmpty();
  }
}
