package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Réda Housni Alaoui
 */
@NoRepositoryBean
public interface EntityGraphJpaSpecificationExecutor<T> extends JpaSpecificationExecutor<T> {

  /**
   * @see JpaSpecificationExecutor#findOne(Specification)
   */
  Optional<T> findOne(Specification<T> spec, @Nullable EntityGraph entityGraph);

  /**
   * @see JpaSpecificationExecutor#findAll(Specification)
   */
  List<T> findAll(Specification<T> spec, @Nullable EntityGraph entityGraph);

  /**
   * @see JpaSpecificationExecutor#findAll(Specification, Pageable)
   */
  Page<T> findAll(Specification<T> spec, Pageable pageable, @Nullable EntityGraph entityGraph);

  /**
   * @see JpaSpecificationExecutor#findAll(Specification, Specification, Pageable)
   */
  Page<T> findAll(
      @Nullable Specification<T> spec,
      @Nullable Specification<T> countSpec,
      Pageable pageable,
      @Nullable EntityGraph entityGraph);

  /**
   * @see JpaSpecificationExecutor#findAll(Specification, Sort)
   */
  List<T> findAll(Specification<T> spec, Sort sort, @Nullable EntityGraph entityGraph);

  /**
   * @see JpaSpecificationExecutor#findBy(PredicateSpecification, Function)
   */
  default <S extends T, R> R findBy(
      PredicateSpecification<T> spec,
      @Nullable EntityGraph entityGraph,
      Function<? super SpecificationFluentQuery<S>, R> queryFunction) {
    return findBy(Specification.where(spec), entityGraph, queryFunction);
  }

  /**
   * @see JpaSpecificationExecutor#findBy(Specification, Function)
   */
  <S extends T, R extends @Nullable Object> R findBy(
      Specification<T> spec,
      @Nullable EntityGraph entityGraph,
      Function<? super SpecificationFluentQuery<S>, R> queryFunction);
}
