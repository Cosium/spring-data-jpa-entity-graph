package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
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
  void test() {
    assertThat(myService.list("Product 1")).isNotEmpty();
  }

  interface ProductRepository extends EntityGraphRepository<Product, Long> {

    Stream<Product> findByName(String name);
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
  }
}
