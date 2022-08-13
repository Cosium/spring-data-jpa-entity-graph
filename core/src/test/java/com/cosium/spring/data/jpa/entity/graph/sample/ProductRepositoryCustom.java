package com.cosium.spring.data.jpa.entity.graph.sample;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
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
