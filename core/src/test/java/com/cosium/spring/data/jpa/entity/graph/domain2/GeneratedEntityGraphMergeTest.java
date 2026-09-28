package com.cosium.spring.data.jpa.entity.graph.domain2;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.sample.ProductEntityGraph;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

@DatabaseSetup(BaseTest.DATASET)
class GeneratedEntityGraphMergeTest extends BaseTest {

  @PersistenceContext private EntityManager em;

  @Autowired private ProductRepository productRepository;

  @Test
  @DisplayName("generated graph should include EntityGraphPart")
  @Transactional
  void shouldIncludePart() {
    EntityGraphPart part = EntityGraphPart.of("brand").build();
    ProductEntityGraph g = ProductEntityGraph.____().include(part).maker().country().____.____();
    assertThat(g.buildQueryHint(em, Product.class)).isPresent();
    // Verify actual fetching via repository
    Product product = productRepository.findById(1L, g).orElseThrow();
    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker().getCountry())).isTrue();
  }

  @Test
  @DisplayName("generated graph include should merge with dynamic")
  @Transactional
  void shouldMergeGeneratedWithDynamic() {
    EntityGraphPart brandPart = EntityGraphPart.of("brand").build();
    ProductEntityGraph genGraph = ProductEntityGraph.____().include(brandPart).____();
    DynamicEntityGraph dynGraph = DynamicEntityGraph.loading().addPath("maker").build();
    EntityGraph merged = genGraph.merge(dynGraph);
    assertThat(merged.buildQueryHint(em, Product.class)).isPresent();
    Product p = productRepository.findById(1L, merged).orElseThrow();
    assertThat(Hibernate.isInitialized(p.getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(p.getMaker())).isTrue();
  }

  public interface ProductRepository extends Repository<Product, Long> {
    java.util.Optional<Product> findById(Long id, EntityGraph entityGraph);
  }
}
