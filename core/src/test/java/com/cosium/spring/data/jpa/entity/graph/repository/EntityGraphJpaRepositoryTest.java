package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphType;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.InapplicableEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.sample.Brand;
import com.cosium.spring.data.jpa.entity.graph.sample.BrandRepository;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.sample.ProductEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.ProductName;
import com.cosium.spring.data.jpa.entity.graph.sample.ProductRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
class EntityGraphJpaRepositoryTest extends BaseTest {

  @PersistenceContext private EntityManager entityManager;

  @Inject private ProductRepository productRepository;
  @Inject private BrandRepository brandRepository;

  @Transactional
  @Test
  @DisplayName("Given null eg when findone then then brand should not be loaded")
  void test1() {
    Optional<Product> product = productRepository.findById(1L, null);
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName("Given empty eg when findone then then brand should not be loaded")
  void test2() {
    Optional<Product> product = productRepository.findById(1L, EntityGraph.NOOP);
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName("Given brand eg when findone then brand should be loaded")
  void test3() {
    Optional<Product> product =
        productRepository.findById(1L, NamedEntityGraph.loading(Product.BRAND_EG));
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName("Given brand eg when execute findone then brand should be loaded")
  void test4() {
    Product product =
        NamedEntityGraph.loading(Product.BRAND_EG)
            .execute(entityGraph -> productRepository.findById(1L, entityGraph))
            .orElseThrow(RuntimeException::new);

    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName("Given brand in attribute paths eg when findone then brand should be loaded")
  void test5() {
    Optional<Product> product =
        productRepository.findById(
            1L, DynamicEntityGraph.loading().addPath(Product.BRAND_PROP_NAME).build());
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName(
      "Given brand in attribute paths eg when findone then brand should be loaded with fetch type")
  void test6() {
    Optional<Product> product =
        productRepository.findById(
            1L, DynamicEntityGraph.fetching().addPath(Product.BRAND_PROP_NAME).build());
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName("Querying with an empty fetch graph turn eager fetchtype into lazy")
  void test7() {
    Product product =
        productRepository
            .findById(1L, DynamicEntityGraph.fetching().build())
            .orElseThrow(RuntimeException::new);
    assertThat(Hibernate.isInitialized(product.getBrand())).isFalse();
    assertThat(Hibernate.isInitialized(product.getMaker())).isFalse();
    assertThat(Hibernate.isInitialized(product.getCategory())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName("Querying with an empty generated fetch graph turn eager fetchtype into lazy")
  void test8() {
    Product product =
        productRepository
            .findById(1L, ProductEntityGraph.____(EntityGraphType.FETCH).____())
            .orElseThrow(RuntimeException::new);
    assertThat(Hibernate.isInitialized(product.getBrand())).isFalse();
    assertThat(Hibernate.isInitialized(product.getMaker())).isFalse();
    assertThat(Hibernate.isInitialized(product.getCategory())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName(
      "Given brand and maker in attribute paths eg when findone then brand and maker should be loaded")
  void test10() {
    EntityGraph entityGraph =
        DynamicEntityGraph.loading()
            .addPath(Product.BRAND_PROP_NAME)
            .addPath(Product.MAKER_PROP_NAME)
            .build();
    Optional<Product> product = productRepository.findById(1L, entityGraph);

    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(product.get().getMaker())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName("Non NOOP EntityGraph has priority over @EntityGraph")
  void test110() {
    List<Product> products =
        productRepository.findByName(
            "Product 1", DynamicEntityGraph.loading().addPath("maker").build());
    assertThat(products).hasSize(1);
    for (Product product : products) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isFalse();
      assertThat(Hibernate.isInitialized(product.getMaker())).isTrue();
    }
  }

  @Transactional
  @Test
  @DisplayName("@EntityGraph has priority over EntityGraph#NOOP")
  void test111() {
    List<Product> products = productRepository.findByName("Product 1", EntityGraph.NOOP);
    assertThat(products).hasSize(1);
    for (Product product : products) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }

  @Transactional
  @Test
  @DisplayName("Given brand eg when find all paginated then brand should be loaded")
  void test12() {
    Page<Product> productPage =
        productRepository.findAll(
            PageRequest.of(0, 10), NamedEntityGraph.loading(Product.BRAND_EG));
    assertThat(productPage.getContent()).isNotEmpty();
    for (Product product : productPage.getContent()) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }

  @Transactional
  @Test
  @DisplayName("Given brand eg when find all paginated with spec then brand should be loaded")
  void test13() {
    Page<Product> productPage =
        productRepository.findAll(
            new EntityGraphSpecification<Product>(Product.BRAND_EG) {
              @Override
              public Predicate toPredicate(
                  Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return null;
              }
            },
            PageRequest.of(0, 10));

    assertThat(productPage.getContent()).isNotEmpty();
    for (Product product : productPage.getContent()) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }

  @Transactional
  @Test
  @DisplayName("Given brand eg when performing custom paged query then brand should be loaded")
  void test14() {
    Brand brand = brandRepository.findById(1L).orElseThrow(RuntimeException::new);
    entityManager.flush();
    entityManager.clear();

    Page<Product> productPage =
        productRepository.findPageByBrand(
            brand, PageRequest.of(0, 1), NamedEntityGraph.loading(Product.BRAND_EG));
    assertThat(productPage.getContent()).isNotEmpty();
    for (Product product : productPage.getContent()) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }

  @Transactional
  @Test
  @DisplayName("Given brand eg when performing custom paged query then category should be loaded")
  void test15() {
    Brand brand = brandRepository.findById(1L).orElseThrow(RuntimeException::new);
    entityManager.flush();
    entityManager.clear();

    Page<Product> productPage =
        productRepository.findPageByBrand(
            brand, PageRequest.of(0, 1), NamedEntityGraph.loading(Product.BRAND_EG));
    assertThat(productPage.getContent()).isNotEmpty();
    for (Product product : productPage.getContent()) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
      assertThat(Hibernate.isInitialized(product.getMaker())).isFalse();
      assertThat(Hibernate.isInitialized(product.getCategory())).isTrue();
    }
  }

  @Transactional
  @Test
  @DisplayName(
      "Given default eg when find by barcode with eg annotation on brand eg then brand should be loaded")
  void test17() {
    Product product = productRepository.findByBarcode("1111");
    assertThat(product).isNotNull();
    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName("Given products when countproductsbyname then it should work")
  void test18() {
    assertThat(productRepository.countByName("Product 1")).isEqualTo(1);
  }

  @Transactional
  @Test
  @DisplayName("Given products when find all raw then it should work")
  void test19() {
    assertThat(productRepository.findAllRaw()).isNotEmpty();
  }

  @Transactional
  @Test
  @DisplayName(
      "Given products when find by id using query annotation using brand eg then brand should be eagerly loaded")
  void test20() {
    Product product =
        productRepository
            .findByIdUsingQueryAnnotation(1L, NamedEntityGraph.loading(Product.BRAND_EG))
            .orElseThrow(RuntimeException::new);
    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName("Given entity without default eg when findall then it should work")
  void test21() {
    assertThat(brandRepository.findAll()).isNotEmpty();
  }

  @Transactional
  @Test
  @DisplayName(
      "Given products and product name projection when find product name by name with mandatory eg then it should fail")
  void test22() {
    NamedEntityGraph entityGraph = NamedEntityGraph.loading(Product.BRAND_EG);
    assertThatThrownBy(() -> productRepository.findProductNameByName("Product 1", entityGraph))
        .isInstanceOf(InapplicableEntityGraphException.class);
  }

  @Transactional
  @Test
  @DisplayName(
      "Given products and product name projection when find product name by name without eg then it should work")
  void test23() {
    ProductName productName =
        productRepository.findProductNameByName("Product 1", EntityGraph.NOOP);
    assertThat(productName).isNotNull();
  }

  private abstract static class EntityGraphSpecification<T>
      implements Specification<T>, EntityGraph {

    private final NamedEntityGraph entityGraphDelegate;

    public EntityGraphSpecification(String entityGraphName) {
      this.entityGraphDelegate = NamedEntityGraph.loading(entityGraphName);
    }

    @Override
    public Optional<EntityGraphQueryHint> buildQueryHint(
        EntityManager entityManager, Class<?> entityType) {
      return entityGraphDelegate.buildQueryHint(entityManager, entityType);
    }
  }
}
