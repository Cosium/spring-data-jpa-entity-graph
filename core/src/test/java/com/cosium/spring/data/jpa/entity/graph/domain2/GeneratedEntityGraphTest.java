package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.sample.Brand;
import com.cosium.spring.data.jpa.entity.graph.sample.BrandEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Maker;
import com.cosium.spring.data.jpa.entity.graph.sample.MakerEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.sample.ProductEntityGraph;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.persistence.AttributeNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Subgraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Réda Housni Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
class GeneratedEntityGraphTest extends BaseTest {

  @PersistenceContext private EntityManager entityManager;

  @Test
  @DisplayName("Test no path")
  void test1() {
    String entityGraph = buildJpaEntityGraph(ProductEntityGraph.____().____(), Product.class);
    assertThat(entityGraph).isEqualTo("");
  }

  @Test
  @DisplayName("Test multiple paths")
  void test2() {
    String entityGraph =
        buildJpaEntityGraph(
            ProductEntityGraph.____().brand().____.category().____.maker().country().____.____(),
            Product.class);

    assertThat(entityGraph).isEqualTo("brand,category,maker.country");
  }

  @Test
  @DisplayName("testPathToMap")
  void test3() {
    assertThat(new Brand().getProducts()).isInstanceOf(Map.class);

    String entityGraph =
        buildJpaEntityGraph(BrandEntityGraph.____().products().____.____(), Brand.class);
    assertThat(entityGraph).isEqualTo("products");
  }

  @Test
  @DisplayName("testPathToCollection")
  void test4() {
    assertThat(new Maker().getProducts()).isInstanceOf(Set.class);

    String entityGraph =
        buildJpaEntityGraph(MakerEntityGraph.____().products().____.____(), Maker.class);

    assertThat(entityGraph).isEqualTo("products");
  }

  @Test
  @DisplayName("Path to collection table")
  void test5() {
    assertThat(new Product().getProperties()).isInstanceOf(Map.class);

    String entityGraph =
        buildJpaEntityGraph(
            ProductEntityGraph.____().properties().____.brand().____.____(), Product.class);
    assertThat(entityGraph).isEqualTo("brand,properties");
  }

  private String buildJpaEntityGraph(EntityGraph entityGraph, Class<?> entityType) {
    return entityGraph
        .buildQueryHint(entityManager, entityType)
        .map(EntityGraphQueryHint::entityGraph)
        .map(this::toString)
        .orElseThrow(RuntimeException::new);
  }

  private String toString(jakarta.persistence.EntityGraph<?> entityGraph) {
    // This algorithm is probably incorrect for more complex cases but good enough
    // since the tests pass and I have to go swim !
    List<String> paths = new ArrayList<>();
    for (AttributeNode<?> node : entityGraph.getAttributeNodes()) {
      List<String> nodePath = new ArrayList<>();
      addPath(node, nodePath);
      paths.add(String.join(".", nodePath));
    }
    Collections.sort(paths);
    return String.join(",", paths);
  }

  private void addPath(AttributeNode<?> node, List<String> path) {
    path.add(node.getAttributeName());
    for (Subgraph<?> subgraph : node.getSubgraphs().values()) {
      for (AttributeNode<?> attributeNode : subgraph.getAttributeNodes()) {
        addPath(attributeNode, path);
      }
    }
  }
}
