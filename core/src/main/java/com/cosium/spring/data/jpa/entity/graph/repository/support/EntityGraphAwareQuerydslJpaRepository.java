package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created on 05/12/16.
 *
 * @author Reda.Housni-Alaoui
 */
class EntityGraphAwareQuerydslJpaRepository<T, ID extends Serializable>
    extends QuerydslJpaRepository<T, ID> {

  public EntityGraphAwareQuerydslJpaRepository(
      JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  public EntityGraphAwareQuerydslJpaRepository(
      JpaEntityInformation<T, ID> entityInformation,
      EntityManager entityManager,
      EntityPathResolver resolver) {
    super(entityInformation, entityManager, resolver);
  }

  @Override
  protected JPQLQuery<?> createCountQuery(Predicate... predicate) {
    return CountQueryDetector.proxy(super.createCountQuery(predicate));
  }
}
