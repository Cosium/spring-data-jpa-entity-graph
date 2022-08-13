package com.cosium.spring.data.jpa.entity.graph.sample;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

/**
 * Created on 28/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@Component
public class ProductRepositoryImpl implements ProductRepositoryCustom {

  @Inject private BrandRepository brandRepository;
  @Inject private ProductRepository productRepository;
  @PersistenceContext private EntityManager entityManager;

  @Override
  public void customMethod(EntityGraph entityGraph) {}

  @Override
  public List<Product> customMethodCallingAnotherRepository(EntityGraph entityGraph) {
    Optional<Brand> brand = brandRepository.findById(1L, NamedEntityGraph.loading(Brand.EMPTY_EG));
    entityManager.flush();
    entityManager.clear();
    return productRepository.findByBrand(
        brand.orElseThrow(() -> new RuntimeException("Brand not found")));
  }
}
