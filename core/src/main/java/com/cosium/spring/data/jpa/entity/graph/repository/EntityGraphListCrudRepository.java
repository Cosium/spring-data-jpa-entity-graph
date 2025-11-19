package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.util.List;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Andreas Austing
 */
@NoRepositoryBean
public interface EntityGraphListCrudRepository<T, ID>
    extends ListCrudRepository<T, ID>, EntityGraphCrudRepository<T, ID> {

  /**
   * @see ListCrudRepository#findAllById(Iterable)
   */
  List<T> findAllById(Iterable<ID> ids, EntityGraph entityGraph);

  /**
   * @see ListCrudRepository#findAll()
   */
  List<T> findAll(EntityGraph entityGraph);
}
