package com.cosium.spring.data.jpa.entity.graph.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.BaseTest;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.Product;
import com.cosium.spring.data.jpa.entity.graph.repository.sample.ProductRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.hibernate.Hibernate;
import org.junit.Test;
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
		assertThat(products.isEmpty()).isFalse();
		for(Product product: products){
			assertThat(Hibernate.isInitialized(product.getBrand())).isTrue();
		}
	}

}