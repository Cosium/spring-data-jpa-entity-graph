package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 18/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphPagingAndSortingRepository<T, ID>
    extends ListPagingAndSortingRepository<T, ID>, EntityGraphRepository<T, ID> {

  /**
   * @see ListPagingAndSortingRepository#findAll(Pageable)
   */
  Page<T> findAll(Pageable pageable, EntityGraph entityGraph);

  /**
   * @see ListPagingAndSortingRepository#findAll(Sort)
   */
  List<T> findAll(Sort sort, EntityGraph entityGraph);
}
