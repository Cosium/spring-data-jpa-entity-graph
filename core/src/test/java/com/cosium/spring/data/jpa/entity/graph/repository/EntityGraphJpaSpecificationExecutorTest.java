package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.MultipleEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.sample.ProductRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
public class EntityGraphJpaSpecificationExecutorTest extends BaseTest {

  @Inject private ProductRepository productRepository;

  @Transactional
  @Test
  public void
      given_brand_eg_when_findbyspecification_implementing_eg_then_brand_should_be_loaded() {
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
  @Test(expected = MultipleEntityGraphException.class)
  public void given_products_when_findAllBySpec_with_two_egs_then_it_should_fail() {
    productRepository.findAll(
        new EntityGraphSpecification<Product>(Product.BRAND_EG) {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        },
        NamedEntityGraph.loading(Product.BRAND_EG));
  }

  @Transactional
  @Test(expected = MultipleEntityGraphException.class)
  public void
      given_products_when_findAllBySpec_with_an_noop_eg_and_a_non_empty_one_then_it_should_fail() {
    productRepository.findAll(
        new EntityGraphSpecification<Product>(Product.BRAND_EG) {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        },
        EntityGraph.NOOP);
  }

  @Transactional
  @Test(expected = MultipleEntityGraphException.class)
  public void given_products_when_findAllBySpec_with_two_noop_eg_then_it_should_fail() {
    productRepository.findAll(
        new EmptyEntityGraphSpecification<Product>() {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        },
        EntityGraph.NOOP);
  }

  @Transactional
  @Test(expected = MultipleEntityGraphException.class)
  public void
      given_products_when_findAllBySpec_with_a_non_empty_eg_and_an_empty_one_then_it_should_fail() {
    productRepository.findAll(
        new EmptyEntityGraphSpecification<Product>() {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        },
        NamedEntityGraph.loading(Product.BRAND_EG));
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
}
