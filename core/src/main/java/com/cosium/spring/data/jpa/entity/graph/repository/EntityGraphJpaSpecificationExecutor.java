package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphJpaSpecificationExecutor<T> extends JpaSpecificationExecutor<T> {

  /**
   * @see JpaSpecificationExecutor#findOne(Specification)
   */
  Optional<T> findOne(Specification<T> spec, EntityGraph entityGraph);

  /**
   * @see JpaSpecificationExecutor#findAll(Specification)
   */
  List<T> findAll(Specification<T> spec, EntityGraph entityGraph);

  /**
   * @see JpaSpecificationExecutor#findAll(Specification, Pageable)
   */
  Page<T> findAll(Specification<T> spec, Pageable pageable, EntityGraph entityGraph);

  /**
   * @see JpaSpecificationExecutor#findAll(Specification, Sort)
   */
  List<T> findAll(Specification<T> spec, Sort sort, EntityGraph entityGraph);
}
