package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 17/06/17.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphQuerydslPredicateExecutor<T> extends QuerydslPredicateExecutor<T> {

  /**
   * Returns a single entity matching the given {@link Predicate} or {@literal null} if none was
   * found.
   *
   * @param predicate can be {@literal null}.
   * @param entityGraph can be {@literal null}.
   * @return a single entity matching the given {@link Predicate} or {@literal null} if none was
   *     found.
   * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if the predicate yields
   *     more than one result.
   */
  Optional<T> findOne(Predicate predicate, EntityGraph entityGraph);

  /**
   * Returns all entities matching the given {@link Predicate}. In case no match could be found an
   * empty {@link Iterable} is returned.
   *
   * @param predicate can be {@literal null}.
   * @param entityGraph can be {@literal null}.
   * @return all entities matching the given {@link Predicate}.
   */
  Iterable<T> findAll(Predicate predicate, EntityGraph entityGraph);

  /**
   * Returns all entities matching the given {@link Predicate} applying the given {@link Sort}. In
   * case no match could be found an empty {@link Iterable} is returned.
   *
   * @param predicate can be {@literal null}.
   * @param sort the {@link Sort} specification to sort the results by, must not be {@literal null}.
   * @param entityGraph can be {@literal null}.
   * @return all entities matching the given {@link Predicate}.
   * @since 1.10
   */
  Iterable<T> findAll(Predicate predicate, Sort sort, EntityGraph entityGraph);

  /**
   * Returns all entities matching the given {@link Predicate} applying the given {@link
   * OrderSpecifier}s. In case no match could be found an empty {@link Iterable} is returned.
   *
   * @param predicate can be {@literal null}.
   * @param entityGraph can be {@literal null}.
   * @param orders the {@link OrderSpecifier}s to sort the results by
   * @return all entities matching the given {@link Predicate} applying the given {@link
   *     OrderSpecifier}s.
   */
  Iterable<T> findAll(Predicate predicate, EntityGraph entityGraph, OrderSpecifier<?>... orders);

  /**
   * Returns all entities ordered by the given {@link OrderSpecifier}s.
   *
   * @param orders the {@link OrderSpecifier}s to sort the results by.
   * @param entityGraph can be {@literal null}.
   * @return all entities ordered by the given {@link OrderSpecifier}s.
   */
  Iterable<T> findAll(EntityGraph entityGraph, OrderSpecifier<?>... orders);

  /**
   * Returns a {@link Page} of entities matching the given {@link Predicate}. In case no match could
   * be found, an empty {@link Page} is returned.
   *
   * @param predicate can be {@literal null}.
   * @param pageable can be {@literal null}.
   * @param entityGraph can be {@literal null}.
   * @return a {@link Page} of entities matching the given {@link Predicate}.
   */
  Page<T> findAll(Predicate predicate, Pageable pageable, EntityGraph entityGraph);
}
