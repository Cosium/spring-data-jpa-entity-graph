package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.Brand;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.BrandEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.Maker;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.MakerEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.ProductEntityGraph;
import java.util.Map;
import java.util.Set;
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

  @Test
  public void testPathToMap() {
    assertThat(new Brand().getProducts()).isInstanceOf(Map.class);

    EntityGraph entityGraph = BrandEntityGraph.____().products().____.____();
    assertThat(entityGraph.getEntityGraphAttributePaths()).hasSize(1).contains("products");
  }

  @Test
  public void testPathToCollection() {
    assertThat(new Maker().getProducts()).isInstanceOf(Set.class);

    EntityGraph entityGraph = MakerEntityGraph.____().products().____.____();
    assertThat(entityGraph.getEntityGraphAttributePaths()).hasSize(1).contains("products");
  }
}
