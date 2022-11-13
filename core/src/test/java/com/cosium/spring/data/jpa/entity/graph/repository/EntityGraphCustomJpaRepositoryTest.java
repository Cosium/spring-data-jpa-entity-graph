package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.sample.Brand;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 28/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
class EntityGraphCustomJpaRepositoryTest extends BaseTest {

  @Inject private EntityGraphCustomJpaRepositoryTestProductRepository productRepository;

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

  public interface EntityGraphCustomJpaRepositoryTestProductRepository
      extends Repository<Product, Long>, ProductRepositoryCustom {

    List<Product> findByBrand(Brand brand);
  }

  public interface ProductRepositoryCustom {

    void customMethod(EntityGraph entityGraph);

    List<Product> customMethodCallingAnotherRepository(EntityGraph entityGraph);
  }

  @Component
  public static class EntityGraphCustomJpaRepositoryTestProductRepositoryImpl
      implements ProductRepositoryCustom {

    @Inject private BrandRepository brandRepository;
    @Inject private EntityGraphCustomJpaRepositoryTestProductRepository productRepository;
    @PersistenceContext private EntityManager entityManager;

    @Override
    public void customMethod(EntityGraph entityGraph) {}

    @Override
    public List<Product> customMethodCallingAnotherRepository(EntityGraph entityGraph) {
      Optional<Brand> brand =
          brandRepository.findById(1L, NamedEntityGraph.loading(Brand.EMPTY_EG));
      entityManager.flush();
      entityManager.clear();
      return productRepository.findByBrand(
          brand.orElseThrow(() -> new RuntimeException("Brand not found")));
    }
  }

  public interface BrandRepository extends EntityGraphJpaRepository<Brand, Long> {}
}
