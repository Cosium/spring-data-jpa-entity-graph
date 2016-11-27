package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.JpaEntityGraphQueryDslPredicateExecutor;
import com.cosium.spring.data.jpa.entity.graph.repository.JpaEntityGraphRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.JpaEntityGraphSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public interface ProductRepository
		extends JpaEntityGraphRepository<Product, Long>,
		JpaEntityGraphSpecificationExecutor<Product>,
		JpaEntityGraphQueryDslPredicateExecutor<Product> {

	List<Product> findByName(String name, EntityGraph entityGraph);

	ProductName findProductNameByName(String name, EntityGraph entityGraph);

	@org.springframework.data.jpa.repository.EntityGraph(value = Product.PRODUCT_BRAND_EG)
	Product findByBarcode(String barcode);

	long countByName(String name);

	@Query("select p.name from Product p")
	List<Object[]> findAllRaw();
}
