package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.ListQueryByExampleExecutor;

/**
 * Created on 18/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphQueryByExampleExecutor<T> extends ListQueryByExampleExecutor<T> {

  /**
   * @see ListQueryByExampleExecutor#findAll(Example, Pageable)
   */
  <S extends T> Page<S> findAll(Example<S> example, Pageable pageable, EntityGraph entityGraph);

  /**
   * @see ListQueryByExampleExecutor#findOne(Example)
   */
  <S extends T> Optional<S> findOne(Example<S> example, EntityGraph entityGraph);

  /**
   * @see ListQueryByExampleExecutor#findAll(Example, Sort)
   */
  <S extends T> List<S> findAll(Example<S> example, Sort sort, EntityGraph entityGraph);

  /**
   * @see ListQueryByExampleExecutor#findAll(Example)
   */
  <S extends T> List<S> findAll(Example<S> example, EntityGraph entityGraph);
}
