package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.sample.ProductRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import java.util.List;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 28/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
class EntityGraphCustomJpaRepositoryTest extends BaseTest {

  @Inject private ProductRepository productRepository;

  @Test
  @Transactional
  @DisplayName("Given products when calling customvoidmethod with eg then it should work")
  void test1() {
    productRepository.customMethod(NamedEntityGraph.loading(Product.BRAND_EG));
  }

  @Test
  @Transactional
  @DisplayName(
      "Given products when calling custom method calling another repository with eg then brand should be loaded")
  void test2() {
    List<Product> products =
        productRepository.customMethodCallingAnotherRepository(
            NamedEntityGraph.loading(Product.BRAND_EG));
    assertThat(products).isNotEmpty();
    for (Product product : products) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }
}
