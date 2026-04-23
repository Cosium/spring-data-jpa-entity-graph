package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.sample.ProductEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Product_;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Réda Housni Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
class FluentQueryTest extends BaseTest {

  @Autowired private ProductRepository productRepository;

  @Transactional
  @Test
  @DisplayName("It supports named entity graphs")
  void test1() {
    NamedEntityGraph entityGraph = NamedEntityGraph.loading(Product.BRAND_EG);

    Product product =
        productRepository.findBy(
            idEquals(1L), entityGraph, FluentQuery.FetchableFluentQuery::oneValue);

    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName("It supports dynamic entity graphs")
  void test2() {
    Product product =
        productRepository.findBy(
            idEquals(1L),
            ProductEntityGraph.____().brand().____.maker().country().____.____(),
            FluentQuery.FetchableFluentQuery::oneValue);

    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker().getCountry())).isTrue();

    assertThat(Hibernate.isInitialized(product.getCategory())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName("It uses default entity graphs when null entity graph is provided")
  void test3() {
    Product product =
        productRepository.findBy(idEquals(1L), null, FluentQuery.FetchableFluentQuery::oneValue);

    assertThat(Hibernate.isInitialized(product.getCategory())).isTrue();

    assertThat(Hibernate.isInitialized(product.getBrand())).isFalse();
    assertThat(Hibernate.isInitialized(product.getMaker())).isFalse();
    assertThat(Hibernate.isInitialized(product.getMaker().getCountry())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName("It uses default entity graphs when noop entity graph is provided")
  void test4() {
    Product product =
        productRepository.findBy(
            idEquals(1L), EntityGraph.NOOP, FluentQuery.FetchableFluentQuery::oneValue);

    assertThat(Hibernate.isInitialized(product.getCategory())).isTrue();

    assertThat(Hibernate.isInitialized(product.getBrand())).isFalse();
    assertThat(Hibernate.isInitialized(product.getMaker())).isFalse();
    assertThat(Hibernate.isInitialized(product.getMaker().getCountry())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName("It supports pagination")
  void test5() {
    Page<Product> products =
        productRepository.findBy(
            idEquals(1L),
            ProductEntityGraph.____().brand().____.maker().country().____.____(),
            query -> query.page(Pageable.ofSize(1)));

    assertThat(products).hasSize(1);

    Product product = products.stream().findFirst().orElseThrow();
    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker().getCountry())).isTrue();

    assertThat(Hibernate.isInitialized(product.getCategory())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName("It supports slicing")
  void test6() {
    Slice<Product> products =
        productRepository.findBy(
            idEquals(1L),
            ProductEntityGraph.____().brand().____.maker().country().____.____(),
            query -> query.slice(Pageable.ofSize(1)));

    assertThat(products).hasSize(1);

    Product product = products.stream().findFirst().orElseThrow();
    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker().getCountry())).isTrue();

    assertThat(Hibernate.isInitialized(product.getCategory())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName("It supports counting")
  void test7() {
    long count =
        productRepository.findBy(
            idEquals(1L),
            ProductEntityGraph.____().brand().____.maker().country().____.____(),
            FluentQuery.FetchableFluentQuery::count);

    assertThat(count).isEqualTo(1);
  }

  @Transactional
  @Test
  @DisplayName("It uses default entity graphs when the entity graph less method is used")
  void test8() {
    Product product =
        productRepository.findBy(idEquals(1L), FluentQuery.FetchableFluentQuery::oneValue);

    assertThat(Hibernate.isInitialized(product.getCategory())).isTrue();

    assertThat(Hibernate.isInitialized(product.getBrand())).isFalse();
    assertThat(Hibernate.isInitialized(product.getMaker())).isFalse();
    assertThat(Hibernate.isInitialized(product.getMaker().getCountry())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName("It supports entity graph having a child node with at least 2 children itself")
  void test9() {
    Slice<Product> products =
        productRepository.findBy(
            idEquals(1L),
            ProductEntityGraph.____().brand().____.maker().country().____.maker().ceo().____.____(),
            query -> query.slice(Pageable.ofSize(1)));

    assertThat(products).hasSize(1);

    Product product = products.stream().findFirst().orElseThrow();
    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker().getCountry())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker().getCeo())).isTrue();

    assertThat(Hibernate.isInitialized(product.getCategory())).isFalse();
  }

  private static Specification<Product> idEquals(long id) {
    return (root, query, cb) -> cb.equal(root.get(Product_.id), id);
  }

  public interface ProductRepository
      extends EntityGraphJpaRepository<Product, Long>,
          EntityGraphJpaSpecificationExecutor<Product> {

    @Override
    default Optional<EntityGraph> defaultEntityGraph() {
      return Optional.of(ProductEntityGraph.____().category().____.____());
    }
  }
}
