package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.JpaEntityGraphRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.JpaEntityGraphSpecificationExecutor;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public interface ProductRepository extends JpaEntityGraphRepository<Product, Long>, JpaEntityGraphSpecificationExecutor<Product> {

	List<Product> findByName(String name, EntityGraph entityGraph);

}
