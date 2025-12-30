package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Réda Housni Alaoui
 */
@NoRepositoryBean
public interface EntityGraphPagingAndSortingRepository<T, ID>
    extends EntityGraphRepository<T, ID>, PagingAndSortingRepository<T, ID> {

  /**
   * @see PagingAndSortingRepository#findAll(Sort)
   */
  Iterable<T> findAll(Sort sort, @Nullable EntityGraph entityGraph);

  /**
   * @see PagingAndSortingRepository#findAll(Pageable)
   */
  Page<T> findAll(Pageable pageable, @Nullable EntityGraph entityGraph);
}
