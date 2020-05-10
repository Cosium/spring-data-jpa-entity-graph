package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphs;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

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
    Optional<Brand> brand = brandRepository.findById(1L, EntityGraphs.named(Brand.EMPTY_EG));
    entityManager.flush();
    entityManager.clear();
    return productRepository.findByBrand(
        brand.orElseThrow(() -> new RuntimeException("Brand not found")));
  }
}
