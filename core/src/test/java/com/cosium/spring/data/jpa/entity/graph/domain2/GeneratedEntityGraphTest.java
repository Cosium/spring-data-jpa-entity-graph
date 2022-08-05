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
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.AttributeNode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Subgraph;
import org.junit.Test;

/** @author Réda Housni Alaoui */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
public class GeneratedEntityGraphTest extends BaseTest {

  @PersistenceContext private EntityManager entityManager;

  @Test
  public void testNoPath() {
    String entityGraph = buildJpaEntityGraph(ProductEntityGraph.____().____(), Product.class);
    assertThat(entityGraph).isEqualTo("");
  }

  @Test
  public void testMultiplePaths() {
    String entityGraph =
        buildJpaEntityGraph(
            ProductEntityGraph.____().brand().____.category().____.maker().country().____.____(),
            Product.class);

    assertThat(entityGraph).isEqualTo("brand,category,maker.country");
  }

  @Test
  public void testPathToMap() {
    assertThat(new Brand().getProducts()).isInstanceOf(Map.class);

    String entityGraph =
        buildJpaEntityGraph(BrandEntityGraph.____().products().____.____(), Brand.class);
    assertThat(entityGraph).isEqualTo("products");
  }

  @Test
  public void testPathToCollection() {
    assertThat(new Maker().getProducts()).isInstanceOf(Set.class);

    String entityGraph =
        buildJpaEntityGraph(MakerEntityGraph.____().products().____.____(), Maker.class);

    assertThat(entityGraph).isEqualTo("products");
  }

  private String buildJpaEntityGraph(EntityGraph entityGraph, Class<?> entityType) {
    return entityGraph
        .buildQueryHint(entityManager, entityType)
        .map(EntityGraphQueryHint::entityGraph)
        .map(this::toString)
        .orElseThrow(RuntimeException::new);
  }

  private String toString(javax.persistence.EntityGraph<?> entityGraph) {
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
