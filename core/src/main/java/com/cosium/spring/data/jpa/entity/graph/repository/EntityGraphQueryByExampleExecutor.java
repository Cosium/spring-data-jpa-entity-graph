package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * @author Réda Housni Alaoui
 */
@NoRepositoryBean
public interface EntityGraphQueryByExampleExecutor<T> extends QueryByExampleExecutor<T> {

  /**
   * @see QueryByExampleExecutor#findOne(Example)
   */
  <S extends T> Optional<S> findOne(Example<S> example, @Nullable EntityGraph entityGraph);

  /**
   * @see QueryByExampleExecutor#findAll(Example)
   */
  <S extends T> Iterable<S> findAll(Example<S> example, @Nullable EntityGraph entityGraph);

  /**
   * @see QueryByExampleExecutor#findAll(Example, Sort)
   */
  <S extends T> Iterable<S> findAll(
      Example<S> example, Sort sort, @Nullable EntityGraph entityGraph);

  /**
   * @see QueryByExampleExecutor#findAll(Example, Pageable)
   */
  <S extends T> Page<S> findAll(
      Example<S> example, Pageable pageable, @Nullable EntityGraph entityGraph);
}
