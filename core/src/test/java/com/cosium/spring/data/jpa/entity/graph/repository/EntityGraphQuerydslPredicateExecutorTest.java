package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphs;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.ProductRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.QProduct;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.querydsl.core.types.Predicate;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
public class EntityGraphQuerydslPredicateExecutorTest extends BaseTest {

  @Inject private ProductRepository productRepository;

  @Transactional
  @Test
  public void given_null_eg_when_findone_then_brand_should_not_be_loaded() {
    Optional<Product> product =
        productRepository.findOne(QProduct.product.name.eq("Product 1"), null);
    if (!product.isPresent()) {
      fail("Product must be present");
    }
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isFalse();
  }

  @Transactional
  @Test
  public void given_brand_eg_when_findone_then_brand_should_be_loaded() {
    Optional<Product> product =
        productRepository.findOne(
            QProduct.product.name.eq("Product 1"), EntityGraphs.named(Product.BRAND_EG));
    if (!product.isPresent()) {
      fail("Product must be present");
    }
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  public void given_brand_eg_when_findpage_then_brand_should_be_loaded() {
    Page<Product> productPage =
        productRepository.findAll(
            (Predicate) null, PageRequest.of(0, 10), EntityGraphs.named(Product.BRAND_EG));
    assertThat(productPage.getContent()).isNotEmpty();
    for (Product product : productPage.getContent()) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }
}
