package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.sample.ProductEntityGraph;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Réda Housni Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
class GeneratedEntityGraphTest extends BaseTest {

  @Inject private ProductRepository productRepository;

  @Transactional
  @Test
  void test() {
    Product product =
        productRepository
            .findById(
                1L,
                ProductEntityGraph.____()
                    .brand()
                    .____
                    .category()
                    .____
                    .maker()
                    .country()
                    .____
                    .____())
            .orElseThrow(RuntimeException::new);

    assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    assertThat(Hibernate.isInitialized(product.getCategory())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker())).isTrue();
    assertThat(Hibernate.isInitialized(product.getMaker().getCountry())).isTrue();
  }

  @Test
  @DisplayName("EntityGraph with embedded part is well generated")
  void test2() {
    ProductEntityGraph.____().tracking().modifier().____.____();
  }

  public interface ProductRepository extends EntityGraphCrudRepository<Product, Long> {}
}
