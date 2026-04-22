package com.cosium.spring.data.jpa.entity.graph.repository.support;

import static java.util.Objects.requireNonNull;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * A {@link SimpleJpaRepository} that supports {@link EntityGraph} passed through method arguments.
 *
 * @author Réda Housni Alaoui
 */
public class EntityGraphSimpleJpaRepository<T, ID> extends SimpleJpaRepository<T, ID>
    implements EntityGraphJpaRepository<T, ID>, EntityGraphJpaSpecificationExecutor<T> {

  private final EntityManager entityManager;

  public EntityGraphSimpleJpaRepository(
      JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = requireNonNull(entityManager);
  }

  public EntityGraphSimpleJpaRepository(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
    this.entityManager = requireNonNull(entityManager);
  }

  @Override
  public Optional<T> findOne(Specification<T> spec, @Nullable EntityGraph entityGraph) {
    return findOne(spec);
  }

  @Override
  public List<T> findAll(@Nullable Specification<T> spec, @Nullable EntityGraph entityGraph) {
    return findAll(spec);
  }

  @Override
  public Page<T> findAll(
      @Nullable Specification<T> spec, Pageable pageable, @Nullable EntityGraph entityGraph) {
    return findAll(spec, pageable);
  }

  @Override
  public Page<T> findAll(
      @Nullable Specification<T> spec,
      @Nullable Specification<T> countSpec,
      Pageable pageable,
      @Nullable EntityGraph entityGraph) {
    return findAll(spec, countSpec, pageable);
  }

  @Override
  public List<T> findAll(
      @Nullable Specification<T> spec, Sort sort, @Nullable EntityGraph entityGraph) {
    return findAll(spec, sort);
  }

  @Override
  public <S extends T> Page<S> findAll(
      Example<S> example, Pageable pageable, @Nullable EntityGraph entityGraph) {
    return findAll(example, pageable);
  }

  @Override
  public <S extends T> Optional<S> findOne(Example<S> example, @Nullable EntityGraph entityGraph) {
    return findOne(example);
  }

  @Override
  public Optional<T> findById(ID id, @Nullable EntityGraph entityGraph) {
    return findById(id);
  }

  @Override
  public Page<T> findAll(Pageable pageable, @Nullable EntityGraph entityGraph) {
    return findAll(pageable);
  }

  @Override
  public <S extends T> List<S> findAll(
      Example<S> example, Sort sort, @Nullable EntityGraph entityGraph) {
    return findAll(example, sort);
  }

  @Override
  public <S extends T> List<S> findAll(Example<S> example, @Nullable EntityGraph entityGraph) {
    return findAll(example);
  }

  @Override
  public List<T> findAllById(Iterable<ID> ids, @Nullable EntityGraph entityGraph) {
    return findAllById(ids);
  }

  @Override
  public List<T> findAll(Sort sort, @Nullable EntityGraph entityGraph) {
    return findAll(sort);
  }

  @Override
  public List<T> findAll(@Nullable EntityGraph entityGraph) {
    return findAll();
  }

  @Override
  public <S extends T, R extends @Nullable Object> R findBy(
      Specification<T> spec, Function<? super SpecificationFluentQuery<S>, R> queryFunction) {
    return findBy(spec, null, queryFunction);
  }

  @Override
  public <S extends T, R extends @Nullable Object> R findBy(
      Specification<T> spec,
      @Nullable EntityGraph entityGraph,
      Function<? super SpecificationFluentQuery<S>, @Nullable R> queryFunction) {

    QueryHint queryHint =
        Optional.ofNullable(entityGraph)
            .or(this::defaultEntityGraph)
            .flatMap(it -> it.buildQueryHint(entityManager, getDomainClass()))
            .map(QueryHint::new)
            .orElseGet(QueryHint::noop);

    return super.findBy(
        spec, query -> queryFunction.apply(queryHint.applyTo((SpecificationFluentQuery<S>) query)));
  }

  private record QueryHint(@Nullable EntityGraphQueryHint entityGraphQueryHint) {

    static final QueryHint NOOP = new QueryHint(null);

    static QueryHint noop() {
      return NOOP;
    }

    public <T> SpecificationFluentQuery<T> applyTo(SpecificationFluentQuery<T> query) {
      return Optional.ofNullable(entityGraphQueryHint)
          .map(hint -> hint.applyTo(query))
          .orElse(query);
    }
  }
}
