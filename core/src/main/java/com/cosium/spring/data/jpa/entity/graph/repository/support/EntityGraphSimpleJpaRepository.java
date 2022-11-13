package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
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
 * <p>Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class EntityGraphSimpleJpaRepository<T, ID> extends SimpleJpaRepository<T, ID>
    implements EntityGraphJpaRepository<T, ID>, EntityGraphJpaSpecificationExecutor<T> {

  public EntityGraphSimpleJpaRepository(
      JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
  }

  public EntityGraphSimpleJpaRepository(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
  }

  @Override
  public Optional<T> findOne(Specification<T> spec, EntityGraph entityGraph) {
    return findOne(spec);
  }

  @Override
  public List<T> findAll(Specification<T> spec, EntityGraph entityGraph) {
    return findAll(spec);
  }

  @Override
  public Page<T> findAll(Specification<T> spec, Pageable pageable, EntityGraph entityGraph) {
    return findAll(spec, pageable);
  }

  @Override
  public List<T> findAll(Specification<T> spec, Sort sort, EntityGraph entityGraph) {
    return findAll(spec, sort);
  }

  @Override
  public <S extends T> Page<S> findAll(
      Example<S> example, Pageable pageable, EntityGraph entityGraph) {
    return findAll(example, pageable);
  }

  @Override
  public <S extends T> Optional<S> findOne(Example<S> example, EntityGraph entityGraph) {
    return findOne(example);
  }

  @Override
  public Optional<T> findById(ID id, EntityGraph entityGraph) {
    return findById(id);
  }

  @Override
  public Page<T> findAll(Pageable pageable, EntityGraph entityGraph) {
    return findAll(pageable);
  }

  @Override
  public <S extends T> List<S> findAll(Example<S> example, Sort sort, EntityGraph entityGraph) {
    return findAll(example, sort);
  }

  @Override
  public <S extends T> List<S> findAll(Example<S> example, EntityGraph entityGraph) {
    return findAll(example);
  }

  @Override
  public List<T> findAllById(Iterable<ID> ids, EntityGraph entityGraph) {
    return findAllById(ids);
  }

  @Override
  public Iterable<T> findAll(Sort sort, EntityGraph entityGraph) {
    return findAll(sort);
  }

  @Override
  public Iterable<T> findAll(EntityGraph entityGraph) {
    return findAll();
  }
}
