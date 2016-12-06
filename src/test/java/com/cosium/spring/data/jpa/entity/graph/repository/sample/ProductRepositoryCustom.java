package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;

/**
 * Created on 28/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public interface ProductRepositoryCustom {

	void customMethod(EntityGraph entityGraph);

	List<Product> customMethodCallingAnotherRepository(EntityGraph entityGraph);

}
