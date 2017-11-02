package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Optional;

/**
 * A {@link org.springframework.data.querydsl.QuerydslPredicateExecutor} that supports {@link
 * EntityGraph} passed through method arguments. Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class EntityGraphQuerydslRepository<T, ID extends Serializable>
    extends EntityGraphSimpleJpaRepository<T, ID>
    implements EntityGraphQuerydslPredicateExecutor<T> {

  private final QuerydslJpaRepository<T, ID> querydslJpaRepositoryDelegate;

  public EntityGraphQuerydslRepository(
      JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.querydslJpaRepositoryDelegate =
        new EntityGraphAwareQuerydslJpaRepository<>(
            (JpaEntityInformation<T, ID>) entityInformation, entityManager);
  }

  public EntityGraphQuerydslRepository(
      JpaEntityInformation<T, ID> entityInformation,
      EntityManager entityManager,
      EntityPathResolver resolver) {
    super(entityInformation, entityManager);
    this.querydslJpaRepositoryDelegate =
        new EntityGraphAwareQuerydslJpaRepository<>(entityInformation, entityManager, resolver);
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
}
