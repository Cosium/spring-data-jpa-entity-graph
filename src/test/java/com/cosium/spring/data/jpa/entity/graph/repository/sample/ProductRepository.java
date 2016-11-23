package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.JpaEntityGraphRepository;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public interface ProductRepository extends JpaEntityGraphRepository<Product, Long> {

	List<Product> findByName(String name, EntityGraph entityGraph);

}
