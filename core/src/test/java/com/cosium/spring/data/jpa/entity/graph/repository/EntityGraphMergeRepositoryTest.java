package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphPart;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.sample.Product_;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.transaction.annotation.Transactional;

@DatabaseSetup(BaseTest.DATASET)
class EntityGraphMergeRepositoryTest extends BaseTest {

  @Autowired private ProductRepository productRepository;

  @Test
  @Transactional
  @DisplayName("should use merged graph via repo findById")
  void shouldUseMergedGraphViaRepo() {
    EntityGraphPart brand = EntityGraphPart.of("brand").build();
    // Actually both are brand, test merging brand + maker
    EntityGraph makerGraph = DynamicEntityGraph.loading().addPath("maker").build();
    EntityGraph merged = DynamicEntityGraph.loading().include(brand).build().merge(makerGraph);
    Product p = productRepository.findById(1L, merged).orElseThrow();
    assertThat(Hibernate.isInitialized(p.getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(p.getMaker())).isTrue();
  }

  @Test
  @Transactional
  @DisplayName("should use merged graph via findBy with spec fluent query")
  void shouldUseMergedGraphViaFluentQuery() {
    EntityGraph g1 = DynamicEntityGraph.loading().addPath("brand").build();
    EntityGraph g2 = DynamicEntityGraph.loading().addPath("maker").build();
    EntityGraph merged = g1.merge(g2);
    Product p =
        productRepository.findBy(
            (Specification<Product>) (root, query, cb) -> cb.equal(root.get(Product_.id), 1L),
            merged,
            FluentQuery.FetchableFluentQuery::oneValue);
    assertThat(Hibernate.isInitialized(p.getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(p.getMaker())).isTrue();
  }

  @Test
  @Transactional
  @DisplayName("should use merged dynamic and generated-like part")
  void shouldUseMergedWithTwoParts() {
    EntityGraphPart brandPart = EntityGraphPart.of("brand").build();
    EntityGraphPart makerPart = EntityGraphPart.of("maker").build();
    EntityGraph g = DynamicEntityGraph.loading().include(brandPart).include(makerPart).build();
    EntityGraph other = DynamicEntityGraph.loading().addPath("category").build();
    EntityGraph merged = g.merge(other);
    Product p = productRepository.findById(1L, merged).orElseThrow();
    assertThat(Hibernate.isInitialized(p.getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(p.getMaker())).isTrue();
    assertThat(Hibernate.isInitialized(p.getCategory())).isTrue();
  }

  public interface ProductRepository
      extends EntityGraphJpaRepository<Product, Long>,
          EntityGraphJpaSpecificationExecutor<Product> {}
}
