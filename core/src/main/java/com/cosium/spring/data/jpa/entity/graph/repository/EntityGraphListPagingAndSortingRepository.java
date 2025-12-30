package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Réda Housni Alaoui
 */
@NoRepositoryBean
public interface EntityGraphListPagingAndSortingRepository<T, ID>
    extends EntityGraphPagingAndSortingRepository<T, ID>, ListPagingAndSortingRepository<T, ID> {

  /**
   * @see ListPagingAndSortingRepository#findAll(Sort)
   */
  List<T> findAll(Sort sort, @Nullable EntityGraph entityGraph);
}
