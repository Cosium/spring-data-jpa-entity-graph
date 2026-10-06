package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

@DatabaseSetup(BaseTest.DATASET)
class EntityGraphMergeTest extends BaseTest {

  @PersistenceContext private EntityManager em;

  @Autowired private ProductRepository productRepository;

  @Test
  @Transactional
  @DisplayName("should merge two dynamic graphs")
  void shouldMergeTwoDynamicGraphs() {
    DynamicEntityGraph g1 = DynamicEntityGraph.loading().addPath("brand").build();
    DynamicEntityGraph g2 = DynamicEntityGraph.loading().addPath("maker").build();
    EntityGraph merged = g1.merge(g2);

    Optional<EntityGraphQueryHint> hint = merged.buildQueryHint(em, Product.class);
    assertThat(hint).isPresent();
    Collection<String> props = hint.get().toProperties();
    assertThat(props).containsExactlyInAnyOrder("brand", "maker");

    List<Product> ps = productRepository.findByName("Product 1", merged);
    assertThat(ps).hasSize(1);
    assertThat(Hibernate.isInitialized(ps.get(0).getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(ps.get(0).getMaker())).isTrue();
  }

  @Test
  @Transactional
  @DisplayName("should merge dynamic and named")
  void shouldMergeDynamicAndNamed() {
    DynamicEntityGraph g1 = DynamicEntityGraph.loading().addPath("brand").build();
    NamedEntityGraph g2 = NamedEntityGraph.loading(Product.BRAND_EG);
    EntityGraph merged = g1.merge(g2);
    Optional<EntityGraphQueryHint> hint = merged.buildQueryHint(em, Product.class);
    assertThat(hint).isPresent();
    // union without duplicate if both contain brand
    Collection<String> props = hint.get().toProperties();
    assertThat(props).contains("brand");
  }

  @Test
  @Transactional
  @DisplayName("should merge with deduplication")
  void shouldMergeWithDeduplication() {
    DynamicEntityGraph g1 = DynamicEntityGraph.loading().addPath("brand").build();
    DynamicEntityGraph g2 = DynamicEntityGraph.loading().addPath("brand").build();
    EntityGraph merged = g1.merge(g2);
    Collection<String> props =
        merged.buildQueryHint(em, Product.class).orElseThrow().toProperties();
    assertThat(props).containsExactly("brand");
  }

  @Test
  @DisplayName("should fail on different types")
  void shouldFailOnDifferentTypes() {
    DynamicEntityGraph g1 = DynamicEntityGraph.loading().addPath("brand").build();
    DynamicEntityGraph g2 = DynamicEntityGraph.fetching().addPath("brand").build();
    EntityGraph merged = g1.merge(g2);
    assertThatThrownBy(() -> merged.buildQueryHint(em, Product.class))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("should merge via and alias")
  void shouldMergeViaAnd() {
    DynamicEntityGraph g1 = DynamicEntityGraph.loading().addPath("brand").build();
    DynamicEntityGraph g2 = DynamicEntityGraph.loading().addPath("maker").build();
    EntityGraph merged = g1.and(g2);
    assertThat(merged.buildQueryHint(em, Product.class)).isPresent();
  }

  @Test
  @DisplayName("should handle NOOP in merge")
  void shouldHandleNoop() {
    DynamicEntityGraph g1 = DynamicEntityGraph.loading().addPath("brand").build();
    EntityGraph merged1 = g1.merge(EntityGraph.NOOP);
    assertThat(merged1).isSameAs(g1);

    EntityGraph merged2 = EntityGraph.NOOP.merge(g1);
    assertThat(merged2).isSameAs(g1);

    EntityGraph mergedNoop = EntityGraph.NOOP.merge(EntityGraph.NOOP);
    assertThat(mergedNoop.buildQueryHint(em, Product.class)).isEmpty();
    assertThat(mergedNoop).isEqualTo(EntityGraph.NOOP);
  }

  @Test
  @Transactional
  @DisplayName("should merge three graphs")
  void shouldMergeThreeGraphs() {
    DynamicEntityGraph g1 = DynamicEntityGraph.loading().addPath("brand").build();
    DynamicEntityGraph g2 = DynamicEntityGraph.loading().addPath("maker").build();
    DynamicEntityGraph g3 = DynamicEntityGraph.loading().addPath("category").build();
    EntityGraph merged = g1.merge(g2).merge(g3);
    Collection<String> props =
        merged.buildQueryHint(em, Product.class).orElseThrow().toProperties();
    assertThat(props).containsExactlyInAnyOrder("brand", "category", "maker");
  }

  public interface ProductRepository extends Repository<Product, Long> {
    List<Product> findByName(String name, EntityGraph entityGraph);
  }
}
