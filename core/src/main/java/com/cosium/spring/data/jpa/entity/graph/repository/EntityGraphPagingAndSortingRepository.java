package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created on 18/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphPagingAndSortingRepository<T, ID>
    extends PagingAndSortingRepository<T, ID>, EntityGraphCrudRepository<T, ID> {

  /**
   * @see PagingAndSortingRepository#findAll(Pageable)
   */
  Page<T> findAll(Pageable pageable, EntityGraph entityGraph);

  /**
   * @see PagingAndSortingRepository#findAll(Sort)
   */
  Iterable<T> findAll(Sort sort, EntityGraph entityGraph);
}
