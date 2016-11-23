package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.ProductRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.QProduct;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@DatabaseSetup(BaseTest.DATASET)
@DatabaseTearDown
public class JpaEntityGraphQueryDslPredicateExecutorTest extends BaseTest {

	@Inject
	private ProductRepository productRepository;

	@Transactional
	@Test
	public void given_null_eg_when_findone_then_then_brand_should_not_be_loaded(){
		Product product = productRepository.findOne(QProduct.product.name.eq("Product 1"), (EntityGraph) null);
		assertThat(product).isNotNull();
		assertThat(Hibernate.isInitialized(product.getBrand())).isFalse();
	}

	@Transactional
	@Test
	public void given_brand_eg_when_findone_then_then_brand_should_not_be_loaded(){
		Product product = productRepository.findOne(QProduct.product.name.eq("Product 1"), EntityGraphUtils.fromName(Product.PRODUCT_BRAND_EG));
		assertThat(product).isNotNull();
		assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
	}
}
