package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public interface ProductRepository
    extends EntityGraphJpaRepository<Product, Long>,
        EntityGraphJpaSpecificationExecutor<Product>,
        EntityGraphQuerydslPredicateExecutor<Product>,
        ProductRepositoryCustom {

  List<Product> findByName(String name, EntityGraph entityGraph);

  Page<Product> findPageByBrand(Brand brand, Pageable pageable, EntityGraph entityGraph);

  List<Product> findByBrand(Brand brand);

  ProductName findProductNameByName(String name, EntityGraph entityGraph);

  @org.springframework.data.jpa.repository.EntityGraph(value = Product.BRAND_EG)
  Product findByBarcode(String barcode);

  long countByName(String name);

  @Query("select p.name from Product p")
  List<Object[]> findAllRaw();

  @Query("select p from Product p where p.id = :id")
  Optional<Product> findByIdUsingQueryAnnotation(long id, EntityGraph entityGraph);
}
