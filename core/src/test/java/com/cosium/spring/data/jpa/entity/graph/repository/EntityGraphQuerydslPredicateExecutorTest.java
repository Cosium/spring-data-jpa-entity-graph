package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.*;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.sample.QProduct;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.util.Optional;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
class EntityGraphQuerydslPredicateExecutorTest extends BaseTest {

  @Inject private ProductRepository productRepository;

  @Transactional
  @Test
  @DisplayName("Given null eg when findone then brand should not be loaded")
  void test1() {
    Optional<Product> product =
        productRepository.findOne(QProduct.product.name.eq("Product 1"), null);
    if (product.isEmpty()) {
      fail("Product must be present");
      return;
    }
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isFalse();
  }

  @Transactional
  @Test
  @DisplayName("Given brand eg when findone then brand should be loaded")
  void test2() {
    Optional<Product> product =
        productRepository.findOne(
            QProduct.product.name.eq("Product 1"), NamedEntityGraph.loading(Product.BRAND_EG));
    if (product.isEmpty()) {
      fail("Product must be present");
      return;
    }
    assertThat(Hibernate.isInitialized(product.get().getBrand())).isTrue();
  }

  @Transactional
  @Test
  @DisplayName("Given brand eg when findpage then brand should be loaded")
  void test3() {
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

  public interface ProductRepository
      extends Repository<Product, Long>, EntityGraphQuerydslPredicateExecutor<Product> {}
}
