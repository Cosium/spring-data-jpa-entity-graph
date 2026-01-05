package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import java.util.Optional;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.FluentQuery;

/**
 * @author Réda Housni Alaoui
 */
@NoRepositoryBean
public interface EntityGraphQuerydslPredicateExecutor<T> extends QuerydslPredicateExecutor<T> {

  /**
   * @see QuerydslPredicateExecutor#findOne(Predicate)
   */
  Optional<T> findOne(Predicate predicate, @Nullable EntityGraph entityGraph);

  /**
   * @see QuerydslPredicateExecutor#findAll(Predicate)
   */
  Iterable<T> findAll(Predicate predicate, @Nullable EntityGraph entityGraph);

  /**
   * @see QuerydslPredicateExecutor#findAll(Predicate, Sort)
   */
  Iterable<T> findAll(Predicate predicate, Sort sort, @Nullable EntityGraph entityGraph);

  /**
   * @see QuerydslPredicateExecutor#findAll(Predicate, OrderSpecifier[])
   */
  Iterable<T> findAll(
      Predicate predicate, @Nullable EntityGraph entityGraph, OrderSpecifier<?>... orders);

  /**
   * @see QuerydslPredicateExecutor#findAll(OrderSpecifier[])
   */
  Iterable<T> findAll(@Nullable EntityGraph entityGraph, OrderSpecifier<?>... orders);

  /**
   * @see QuerydslPredicateExecutor#findAll(Predicate, Pageable)
   */
  Page<T> findAll(Predicate predicate, Pageable pageable, @Nullable EntityGraph entityGraph);

  /**
   * @see QuerydslPredicateExecutor#findBy(Predicate, Function)
   */
  <S extends T, R extends @Nullable Object> R findBy(
      Predicate predicate,
      @Nullable EntityGraph entityGraph,
      Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}
