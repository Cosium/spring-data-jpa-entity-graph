package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 18/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphCrudRepository<T, ID>
    extends ListCrudRepository<T, ID>, EntityGraphRepository<T, ID> {

  /**
   * @see ListCrudRepository#findById(Object)
   */
  Optional<T> findById(ID id, EntityGraph entityGraph);

  /**
   * @see ListCrudRepository#findAllById(Iterable)
   */
  List<T> findAllById(Iterable<ID> ids, EntityGraph entityGraph);

  /**
   * @see ListCrudRepository#findAll()
   */
  List<T> findAll(EntityGraph entityGraph);
}
