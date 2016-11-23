package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.EntityGraphSpecification;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.ProductRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
public class JpaEntityGraphRepositoryTest extends BaseTest {

	@Inject
	private ProductRepository productRepository;

	@Transactional
	@Test
	public void given_null_eg_when_findone_then_then_brand_should_not_be_loaded(){
		Product product = productRepository.findOne(1L, null);
		assertThat(product).isNotNull();
		assertThat(Hibernate.isInitialized(product.getBrand())).isFalse();
	}

	@Transactional
	@Test
	public void given_brand_eg_when_findone_then_brand_should_be_loaded(){
		Product product = productRepository.findOne(1L, EntityGraphUtils.fromName(Product.PRODUCT_BRAND_EG));
		assertThat(product).isNotNull();
		assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
	}

	@Transactional
	@Test
	public void given_brand_eg_when_findByName_then_brand_should_be_loaded(){
		List<Product> products = productRepository.findByName("Product 1", EntityGraphUtils.fromName(Product.PRODUCT_BRAND_EG));
		assertThat(products).hasSize(1);
		for(Product product: products){
			assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
		}
	}

	@Transactional
	@Test
	public void given_brand_eg_when_findbyspecification_implementing_eg_then_brand_should_be_loaded(){
		List<Product> products = productRepository.findAll((Specification<Product>) new EntityGraphSpecification<Product>(Product.PRODUCT_BRAND_EG) {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("name"), "Product 2");
			}
		});
		assertThat(products).hasSize(1);
		for(Product product: products){
			assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
		}
	}


}