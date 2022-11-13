package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.MultipleEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
class EntityGraphJpaSpecificationExecutorTest extends BaseTest {

  @Inject private ProductRepository productRepository;

  @Transactional
  @Test
  @DisplayName(
      "Given brand eg when findbyspecification implementing eg then brand should be loaded")
  void test1() {
    List<Product> products =
        productRepository.findAll(
            (Specification<Product>)
                new EntityGraphSpecification<Product>(Product.BRAND_EG) {
                  @Override
                  public Predicate toPredicate(
                      Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(root.get("name"), "Product 2");
                  }
                });
    assertThat(products).hasSize(1);
    for (Product product : products) {
      assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
    }
  }

  @Transactional
  @Test
  @DisplayName("Given products when find all by spec with two egs then it should fail")
  void test2() {
    EntityGraphSpecification<Product> entityGraph1 =
        new EntityGraphSpecification<>(Product.BRAND_EG) {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        };
    NamedEntityGraph entityGraph2 = NamedEntityGraph.loading(Product.BRAND_EG);
    assertThatThrownBy(() -> productRepository.findAll(entityGraph1, entityGraph2))
        .isInstanceOf(MultipleEntityGraphException.class);
  }

  @Transactional
  @Test
  @DisplayName(
      "Given products when find all by spec with an noop eg and a non empty one then it should fail")
  void test3() {
    EntityGraphSpecification<Product> entityGraphSpecification =
        new EntityGraphSpecification<>(Product.BRAND_EG) {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        };

    assertThatThrownBy(() -> productRepository.findAll(entityGraphSpecification, EntityGraph.NOOP))
        .isInstanceOf(MultipleEntityGraphException.class);
  }

  @Transactional
  @Test
  @DisplayName("Given products when find all by spec with two noop eg then it should fail")
  void test4() {
    EmptyEntityGraphSpecification<Product> entityGraphSpecification =
        new EmptyEntityGraphSpecification<>() {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        };

    assertThatThrownBy(() -> productRepository.findAll(entityGraphSpecification, EntityGraph.NOOP))
        .isInstanceOf(MultipleEntityGraphException.class);
  }

  @Transactional
  @Test
  @DisplayName(
      "Given products when find all by spec with a non empty eg and an empty one then it should fail")
  void test5() {
    EmptyEntityGraphSpecification<Product> entityGraph1 =
        new EmptyEntityGraphSpecification<>() {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        };

    NamedEntityGraph entityGraph2 = NamedEntityGraph.loading(Product.BRAND_EG);

    assertThatThrownBy(() -> productRepository.findAll(entityGraph1, entityGraph2))
        .isInstanceOf(MultipleEntityGraphException.class);
  }

  /**
   * Created on 27/11/16.
   *
   * @author Reda.Housni-Alaoui
   */
  private abstract static class EmptyEntityGraphSpecification<T>
      implements Specification<T>, EntityGraph {

    @Override
    public Optional<EntityGraphQueryHint> buildQueryHint(
        EntityManager entityManager, Class<?> entityType) {
      return Optional.empty();
    }
  }

  private abstract static class EntityGraphSpecification<T>
      implements Specification<T>, EntityGraph {

    private final NamedEntityGraph entityGraphDelegate;

    public EntityGraphSpecification(String entityGraphName) {
      this.entityGraphDelegate = NamedEntityGraph.loading(entityGraphName);
    }

    @Override
    public Optional<EntityGraphQueryHint> buildQueryHint(
        EntityManager entityManager, Class<?> entityType) {
      return entityGraphDelegate.buildQueryHint(entityManager, entityType);
    }
  }

  public interface ProductRepository
      extends EntityGraphJpaRepository<Product, Long>,
          EntityGraphJpaSpecificationExecutor<Product> {}
}
