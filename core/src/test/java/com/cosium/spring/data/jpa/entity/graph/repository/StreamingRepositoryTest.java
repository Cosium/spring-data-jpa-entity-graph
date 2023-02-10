package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Réda Housni Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
class StreamingRepositoryTest extends BaseTest {

  @Inject private MyService myService;

  @Test
  void test1() {
    assertThat(myService.list("Product 1")).isNotEmpty();
  }

  @Test
  @DisplayName("Given noop eg when finding products then brand should not be initialized")
  void test2() {
    List<Product> products = myService.list("Product 1", EntityGraph.NOOP);
    assertThat(products).isNotEmpty();
    for (Product product : products) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isFalse();
    }
  }

  @Test
  @DisplayName("Given brand eg when finding products then brand should be initialize")
  void test3() {
    List<Product> products =
        myService.list("Product 1", NamedEntityGraph.loading(Product.BRAND_EG));
    assertThat(products).isNotEmpty();
    for (Product product : products) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }

  interface ProductRepository extends EntityGraphRepository<Product, Long> {

    Stream<Product> findByName(String name);

    Stream<Product> findByName(String name, EntityGraph entityGraph);
  }

  @Transactional
  @Component
  static class MyService {
    private final ProductRepository productRepository;

    MyService(ProductRepository productRepository) {
      this.productRepository = productRepository;
    }

    public List<Product> list(String name) {
      return productRepository.findByName(name).collect(Collectors.toList());
    }

    public List<Product> list(String name, EntityGraph entityGraph) {
      return productRepository.findByName(name, entityGraph).collect(Collectors.toList());
    }
  }
}
