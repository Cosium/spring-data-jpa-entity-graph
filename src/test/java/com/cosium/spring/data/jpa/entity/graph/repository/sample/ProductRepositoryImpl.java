package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import org.springframework.stereotype.Component;

/**
 * Created on 28/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@Component
public class ProductRepositoryImpl implements ProductRepositoryCustom {

	@Inject
	private BrandRepository brandRepository;
	@Inject
	private ProductRepository productRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void customMethod(EntityGraph entityGraph) {

	}

	@Override
	public List<Product> customMethodCallingAnotherRepository(EntityGraph entityGraph) {
		Brand brand = brandRepository.findOne(1L, EntityGraphUtils.fromName(Brand.EMPTY_EG));
		entityManager.flush();
		entityManager.clear();
		return productRepository.findByBrand(brand);
	}
}
