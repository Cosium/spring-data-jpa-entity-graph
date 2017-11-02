package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.ProductRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 28/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
public class EntityGraphCustomJpaRepositoryTest extends BaseTest {

  @Inject private ProductRepository productRepository;

  @Test
  @Transactional
  public void given_products_when_calling_customvoidmethod_with_eg_then_it_should_work() {
    productRepository.customMethod(EntityGraphUtils.fromName(Product.BRAND_EG));
  }

  @Test
  @Transactional
  public void
      given_products_when_calling_customMethodCallingAnotherRepository_with_eg_then_brand_should_be_loaded() {
    List<Product> products =
        productRepository.customMethodCallingAnotherRepository(
            EntityGraphUtils.fromName(Product.BRAND_EG));
    assertThat(products).isNotEmpty();
    for (Product product : products) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }
}
