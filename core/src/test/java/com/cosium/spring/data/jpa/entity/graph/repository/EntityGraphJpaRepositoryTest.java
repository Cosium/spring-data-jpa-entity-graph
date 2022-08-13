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
  void given_null_eg_when_findone_then_then_brand_should_not_be_loaded() {
    Optional<Product> product = productRepository.findById(1L, null);
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isFalse();
  }

  @Transactional
  @Test
  void given_empty_eg_when_findone_then_then_brand_should_not_be_loaded() {
    Optional<Product> product = productRepository.findById(1L, EntityGraph.NOOP);
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isFalse();
  }

  @Transactional
  @Test
  void given_brand_eg_when_findone_then_brand_should_be_loaded() {
    Optional<Product> product =
        productRepository.findById(1L, NamedEntityGraph.loading(Product.BRAND_EG));
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  void given_brand_eg_when_execute_findone_then_brand_should_be_loaded() {
    Product product =
        NamedEntityGraph.loading(Product.BRAND_EG)
            .execute(entityGraph -> productRepository.findById(1L, entityGraph))
            .orElseThrow(RuntimeException::new);

    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
  }

  @Transactional
  @Test
  void given_brand_in_attribute_paths_eg_when_findone_then_brand_should_be_loaded() {
    Optional<Product> product =
        productRepository.findById(
            1L, DynamicEntityGraph.loading().addPath(Product.BRAND_PROP_NAME).build());
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  void given_brand_in_attribute_paths_eg_when_findone_then_brand_should_be_loaded_with_FetchType() {
    Optional<Product> product =
        productRepository.findById(
            1L, DynamicEntityGraph.fetching().addPath(Product.BRAND_PROP_NAME).build());
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  void querying_with_an_empty_fetch_graph_turn_eager_fetchtype_into_lazy() {
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
  void querying_with_an_empty_generated_fetch_graph_turn_eager_fetchtype_into_lazy() {
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
  void empty_legacy_fetch_graph_has_no_impact() {
    Product product =
        productRepository
            .findById(
                1L,
                new com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph() {

                  @Override
                  public com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphType
                      getEntityGraphType() {
                    return com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphType.FETCH;
                  }

                  @Override
                  public String getEntityGraphName() {
                    return null;
                  }

                  @Override
                  public List<String> getEntityGraphAttributePaths() {
                    return null;
                  }

                  @Override
                  public boolean isOptional() {
                    return false;
                  }
                })
            .orElseThrow(RuntimeException::new);
    assertThat(Hibernate.isInitialized(product.getBrand())).isFalse();
    assertThat(Hibernate.isInitialized(product.getMaker())).isTrue();
    assertThat(Hibernate.isInitialized(product.getCategory())).isTrue();
  }

  @Transactional
  @Test
  void
      given_brand_and_maker_in_attribute_paths_eg_when_findone_then_brand_and_maker_should_be_loaded() {
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
  void given_brand_eg_when_findByName_then_brand_should_be_loaded() {
    List<Product> products =
        productRepository.findByName("Product 1", NamedEntityGraph.loading(Product.BRAND_EG));
    assertThat(products).hasSize(1);
    for (Product product : products) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }

  @Transactional
  @Test
  void given_brand_eg_when_findAll_paginated_then_brand_should_be_loaded() {
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
  void given_brand_eg_when_findAll_paginated_with_spec_then_brand_should_be_loaded() {
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
  void given_brand_eg_when_performing_custom_paged_query_then_brand_should_be_loaded() {
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
  void given_brand_eg_when_performing_custom_paged_query_then_category_should_be_loaded() {
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
  void given_default_eg_when_findone_without_eg_then_supplier_should_be_loaded() {
    Optional<Product> product = productRepository.findById(1L);
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getMaker())).isTrue();
  }

  @Transactional
  @Test
  void
      given_default_eg_when_findByBarcode_with_eg_annotation_on_brand_eg_then_brand_should_be_loaded() {
    Product product = productRepository.findByBarcode("1111");
    assertThat(product).isNotNull();
    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
  }

  @Transactional
  @Test
  void given_products_when_countproductsbyname_then_it_should_work() {
    assertThat(productRepository.countByName("Product 1")).isEqualTo(1);
  }

  @Transactional
  @Test
  void given_products_when_findAllRaw_then_it_should_work() {
    assertThat(productRepository.findAllRaw()).isNotEmpty();
  }

  @Transactional
  @Test
  void
      given_products_when_findByIdUsingQueryAnnotation_using_brand_eg_then_brand_should_be_eagerly_loaded() {
    Product product =
        productRepository
            .findByIdUsingQueryAnnotation(1L, NamedEntityGraph.loading(Product.BRAND_EG))
            .orElseThrow(RuntimeException::new);
    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
  }

  @Transactional
  @Test
  void given_entity_without_default_eg_when_findall_then_it_should_work() {
    assertThat(brandRepository.findAll()).isNotEmpty();
  }

  @Transactional
  @Test
  void
      given_products_and_ProductName_projection_when_findProductNameByName_with_mandatory_eg_then_it_should_fail() {
    NamedEntityGraph entityGraph = NamedEntityGraph.loading(Product.BRAND_EG);
    assertThatThrownBy(() -> productRepository.findProductNameByName("Product 1", entityGraph))
        .isInstanceOf(InapplicableEntityGraphException.class);
  }

  @Transactional
  @Test
  void
      given_products_and_ProductName_projection_when_findProductNameByName_without_eg_then_it_should_work() {
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
