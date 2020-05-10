package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;

import java.util.List;

/**
 * Created on 28/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public interface ProductRepositoryCustom {

  void customMethod(EntityGraph entityGraph);

  List<Product> customMethodCallingAnotherRepository(EntityGraph entityGraph);
}
