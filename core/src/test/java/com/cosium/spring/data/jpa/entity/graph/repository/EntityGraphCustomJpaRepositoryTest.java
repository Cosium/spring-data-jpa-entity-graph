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
  void given_products_when_calling_customvoidmethod_with_eg_then_it_should_work() {
    productRepository.customMethod(NamedEntityGraph.loading(Product.BRAND_EG));
  }

  @Test
  @Transactional
  void
      given_products_when_calling_customMethodCallingAnotherRepository_with_eg_then_brand_should_be_loaded() {
    List<Product> products =
        productRepository.customMethodCallingAnotherRepository(
            NamedEntityGraph.loading(Product.BRAND_EG));
    assertThat(products).isNotEmpty();
    for (Product product : products) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }
}
