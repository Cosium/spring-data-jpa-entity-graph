package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.querydsl.EntityPathResolver;

/**
 * @author Réda Housni Alaoui
 */
class EntityGraphAwareQuerydslJpaRepository<T> extends QuerydslJpaPredicateExecutor<T> {

  public EntityGraphAwareQuerydslJpaRepository(
      JpaEntityInformation<T, ?> entityInformation,
      EntityManager entityManager,
      EntityPathResolver resolver,
      @Nullable CrudMethodMetadata metadata) {
    super(entityInformation, entityManager, resolver, metadata);
  }

  @Override
  protected JPQLQuery<?> createCountQuery(@Nullable Predicate... predicate) {
    return CountQueryDetector.proxy(super.createCountQuery(predicate));
  }
}
