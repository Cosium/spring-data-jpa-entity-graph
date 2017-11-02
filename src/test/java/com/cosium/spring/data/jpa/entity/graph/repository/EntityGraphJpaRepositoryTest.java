package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.InapplicableEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.*;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
public class EntityGraphJpaRepositoryTest extends BaseTest {

  @Inject private ProductRepository productRepository;
  @Inject private BrandRepository brandRepository;

  @Transactional
  @Test
  public void given_null_eg_when_findone_then_then_brand_should_not_be_loaded() {
    Optional<Product> product = productRepository.findById(1L, null);
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isFalse();
  }

  @Transactional
  @Test
  public void given_empty_eg_when_findone_then_then_brand_should_not_be_loaded() {
    Optional<Product> product = productRepository.findById(1L, EntityGraphUtils.empty());
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isFalse();
  }

  @Transactional
  @Test
  public void given_brand_eg_when_findone_then_brand_should_be_loaded() {
    Optional<Product> product =
        productRepository.findById(1L, EntityGraphUtils.fromName(Product.BRAND_EG));
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  public void given_optional_brand_eg_when_findone_then_brand_should_be_loaded() {
    Optional<Product> product =
        productRepository.findById(1L, EntityGraphUtils.fromName(Product.BRAND_EG, true));
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  public void given_brand_in_attribute_paths_eg_when_findone_then_brand_should_be_loaded() {
    Optional<Product> product =
        productRepository.findById(
            1L, EntityGraphUtils.fromAttributePaths(Product.BRAND_PROP_NAME));
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  public void
      given_brand_and_maker_in_attribute_paths_eg_when_findone_then_brand_and_maker_should_be_loaded() {
    EntityGraph entityGraph =
        EntityGraphUtils.fromAttributePaths(Product.BRAND_PROP_NAME, Product.MAKER_PROP_NAME);
    Optional<Product> product = productRepository.findById(1L, entityGraph);

    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(product.get().getMaker())).isTrue();
  }

  @Transactional
  @Test
  public void given_brand_eg_when_findByName_then_brand_should_be_loaded() {
    List<Product> products =
        productRepository.findByName("Product 1", EntityGraphUtils.fromName(Product.BRAND_EG));
    assertThat(products).hasSize(1);
    for (Product product : products) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }

  @Transactional
  @Test
  public void given_brand_eg_when_findAll_paginated_then_brand_should_be_loaded() {
    Page<Product> productPage =
        productRepository.findAll(
            new PageRequest(0, 10), EntityGraphUtils.fromName(Product.BRAND_EG));
    assertThat(productPage.getContent()).isNotEmpty();
    for (Product product : productPage.getContent()) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }

  @Transactional
  @Test
  public void given_brand_eg_when_findAll_paginated_with_spec_then_brand_should_be_loaded() {
    Page<Product> productPage =
        productRepository.findAll(
            new EntityGraphSpecification<Product>(Product.BRAND_EG) {
              @Override
              public Predicate toPredicate(
                  Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return null;
              }
            },
            new PageRequest(0, 10));

    assertThat(productPage.getContent()).isNotEmpty();
    for (Product product : productPage.getContent()) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }

  @Transactional
  @Test
  public void given_default_eg_when_findone_without_eg_then_supplier_should_be_loaded() {
    Optional<Product> product = productRepository.findById(1L);
    assertThat(product).isNotEmpty();
    assertThat(Hibernate.isInitialized(product.get().getMaker())).isTrue();
  }

  @Transactional
  @Test
  public void
      given_default_eg_when_findByBarcode_with_eg_annotation_on_brand_eg_then_brand_should_be_loaded() {
    Product product = productRepository.findByBarcode("1111");
    assertThat(product).isNotNull();
    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
  }

  @Transactional
  @Test
  public void given_products_when_countproductsbyname_then_it_should_work() {
    assertThat(productRepository.countByName("Product 1")).isEqualTo(1);
  }

  @Transactional
  @Test
  public void given_products_when_findAllRaw_then_it_should_work() {
    assertThat(productRepository.findAllRaw()).isNotEmpty();
  }

  @Transactional
  @Test
  public void given_entity_without_default_eg_when_findall_then_it_should_work() {
    assertThat(brandRepository.findAll()).isNotEmpty();
  }

  @Transactional
  @Test(expected = InapplicableEntityGraphException.class)
  public void
      given_products_and_ProductName_projection_when_findProductNameByName_with_mandatory_eg_then_it_should_fail() {
    productRepository.findProductNameByName(
        "Product 1", EntityGraphUtils.fromName(Product.BRAND_EG));
  }

  @Transactional
  @Test
  public void
      given_products_and_ProductName_projection_when_findProductNameByName_with_optional_eg_then_it_should_not_fail() {
    productRepository.findProductNameByName(
        "Product 1", EntityGraphUtils.fromName(Product.BRAND_EG, true));
  }

  @Transactional
  @Test
  public void
      given_products_and_ProductName_projection_when_findProductNameByName_without_eg_then_it_should_work() {
    ProductName productName =
        productRepository.findProductNameByName("Product 1", EntityGraphUtils.empty());
    assertThat(productName).isNotNull();
  }
}
