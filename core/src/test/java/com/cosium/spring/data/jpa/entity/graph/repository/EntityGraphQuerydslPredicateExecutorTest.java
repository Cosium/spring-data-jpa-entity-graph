package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.sample.ProductRepository;
import com.cosium.spring.data.jpa.entity.graph.sample.QProduct;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import java.util.Optional;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
class EntityGraphQuerydslPredicateExecutorTest extends BaseTest {

  @Inject private ProductRepository productRepository;

  @Transactional
  @Test
  void given_null_eg_when_findone_then_brand_should_not_be_loaded() {
    Optional<Product> product =
        productRepository.findOne(QProduct.product.name.eq("Product 1"), null);
    if (!product.isPresent()) {
      fail("Product must be present");
      return;
    }
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isFalse();
  }

  @Transactional
  @Test
  void given_brand_eg_when_findone_then_brand_should_be_loaded() {
    Optional<Product> product =
        productRepository.findOne(
            QProduct.product.name.eq("Product 1"), NamedEntityGraph.loading(Product.BRAND_EG));
    if (!product.isPresent()) {
      fail("Product must be present");
      return;
    }
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  void given_brand_eg_when_findpage_then_brand_should_be_loaded() {
    Page<Product> productPage =
        productRepository.findAll(
            QProduct.product.id.isNotNull(),
            PageRequest.of(0, 10),
            NamedEntityGraph.loading(Product.BRAND_EG));
    assertThat(productPage.getContent()).isNotEmpty();
    for (Product product : productPage.getContent()) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }
}
