package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.MultipleEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.EmptyEntityGraphSpecification;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.EntityGraphSpecification;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.ProductRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
  @Test
  public void
      given_brand_optional_eg_when_findbyspecification_implementing_eg_then_brand_should_be_loaded() {
    List<Product> products =
        productRepository.findAll(
            (Specification<Product>)
                new EntityGraphSpecification<Product>(Product.BRAND_EG, true) {
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
  public void given_products_when_findAllBySpec_with_two_non_empty_egs_then_it_should_fail() {
    productRepository.findAll(
        new EntityGraphSpecification<Product>(Product.BRAND_EG) {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        },
        EntityGraphUtils.fromName(Product.BRAND_EG));
  }

  @Transactional
  @Test(expected = MultipleEntityGraphException.class)
  public void
      given_products_when_findAllBySpec_with_an_empty_eg_and_a_non_empty_one_then_it_should_fail() {
    productRepository.findAll(
        new EntityGraphSpecification<Product>(Product.BRAND_EG) {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        },
        EntityGraphUtils.empty());
  }

  @Transactional
  @Test(expected = MultipleEntityGraphException.class)
  public void given_products_when_findAllBySpec_with_two_empty_eg_then_it_should_fail() {
    productRepository.findAll(
        new EmptyEntityGraphSpecification<Product>() {
          @Override
          public Predicate toPredicate(
              Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return null;
          }
        },
        EntityGraphUtils.empty());
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
        EntityGraphUtils.fromName(Product.BRAND_EG));
  }
}
