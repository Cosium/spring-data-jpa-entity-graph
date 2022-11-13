package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.lang.Nullable;

/**
 * A {@link org.springframework.data.querydsl.QuerydslPredicateExecutor} that supports {@link
 * EntityGraph} passed through method arguments.
 *
 * @author Reda.Housni-Alaoui
 */
public class EntityGraphQuerydslRepository<T, ID> extends EntityGraphSimpleJpaRepository<T, ID>
    implements EntityGraphQuerydslPredicateExecutor<T> {

  protected final QuerydslPredicateExecutor<T> querydslJpaRepositoryDelegate;

  public EntityGraphQuerydslRepository(
      JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    this(entityInformation, entityManager, SimpleEntityPathResolver.INSTANCE, null);
  }

  public EntityGraphQuerydslRepository(
      JpaEntityInformation<T, ?> entityInformation,
      EntityManager entityManager,
      EntityPathResolver resolver,
      @Nullable CrudMethodMetadata metadata) {
    super(entityInformation, entityManager);
    this.querydslJpaRepositoryDelegate =
        new EntityGraphAwareQuerydslJpaRepository<>(
            entityInformation, entityManager, resolver, metadata);
  }

  @Override
  public Optional<T> findOne(Predicate predicate, EntityGraph entityGraph) {
    return querydslJpaRepositoryDelegate.findOne(predicate);
  }

  @Override
  public Iterable<T> findAll(Predicate predicate, EntityGraph entityGraph) {
    return querydslJpaRepositoryDelegate.findAll(predicate);
  }

  @Override
  public Iterable<T> findAll(Predicate predicate, Sort sort, EntityGraph entityGraph) {
    return querydslJpaRepositoryDelegate.findAll(predicate, sort);
  }

  @Override
  public Iterable<T> findAll(
      Predicate predicate, EntityGraph entityGraph, OrderSpecifier<?>... orders) {
    return querydslJpaRepositoryDelegate.findAll(predicate, orders);
  }

  @Override
  public Iterable<T> findAll(EntityGraph entityGraph, OrderSpecifier<?>... orders) {
    return querydslJpaRepositoryDelegate.findAll(orders);
  }

  @Override
  public Page<T> findAll(Predicate predicate, Pageable pageable, EntityGraph entityGraph) {
    return querydslJpaRepositoryDelegate.findAll(predicate, pageable);
  }

  @Override
  public Optional<T> findOne(Predicate predicate) {
    return querydslJpaRepositoryDelegate.findOne(predicate);
  }

  @Override
  public Iterable<T> findAll(Predicate predicate) {
    return querydslJpaRepositoryDelegate.findAll(predicate);
  }

  @Override
  public Iterable<T> findAll(Predicate predicate, Sort sort) {
    return querydslJpaRepositoryDelegate.findAll(predicate, sort);
  }

  @Override
  public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
    return querydslJpaRepositoryDelegate.findAll(predicate, orders);
  }

  @Override
  public Iterable<T> findAll(OrderSpecifier<?>... orders) {
    return querydslJpaRepositoryDelegate.findAll(orders);
  }

  @Override
  public Page<T> findAll(Predicate predicate, Pageable pageable) {
    return querydslJpaRepositoryDelegate.findAll(predicate, pageable);
  }

  @Override
  public long count(Predicate predicate) {
    return querydslJpaRepositoryDelegate.count(predicate);
  }

  @Override
  public boolean exists(Predicate predicate) {
    return querydslJpaRepositoryDelegate.exists(predicate);
  }

  @Override
  public <S extends T, R> R findBy(
      Predicate predicate, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return querydslJpaRepositoryDelegate.findBy(predicate, queryFunction);
  }
}
