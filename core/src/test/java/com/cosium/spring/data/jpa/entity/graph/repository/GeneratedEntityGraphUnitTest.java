package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.ProductEntityGraph;
import org.junit.Test;

/** @author Réda Housni Alaoui */
public class GeneratedEntityGraphUnitTest {

  @Test
  public void testNoPath() {
    EntityGraph entityGraph = ProductEntityGraph.____().____();
    assertThat(entityGraph.getEntityGraphAttributePaths()).isEmpty();
  }

  @Test
  public void testMultiplePaths() {
    EntityGraph entityGraph =
        ProductEntityGraph.____().brand().____.category().____.maker().country().____.____();

    assertThat(entityGraph.getEntityGraphAttributePaths())
        .hasSize(3)
        .contains("brand")
        .contains("category")
        .contains("maker.country");
  }
}
