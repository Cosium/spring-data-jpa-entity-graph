package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.JpaEntityGraphRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.JpaEntityGraphSpecificationExecutor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.data.jpa.repository.query.JpaEntityGraph;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * A {@link SimpleJpaRepository} that supports {@link EntityGraph} passed through method arguments.
 *
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class SimpleJpaEntityGraphRepository<T, ID extends Serializable>
		extends SimpleJpaRepository<T, ID>
		implements JpaEntityGraphRepository<T, ID>, JpaEntityGraphSpecificationExecutor<T> {

	public SimpleJpaEntityGraphRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	public SimpleJpaEntityGraphRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
	}

	@Override
	public T findOne(Specification<T> spec, EntityGraph entityGraph) {
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
	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable, EntityGraph entityGraph) {
		return findAll(example, pageable);
	}

	@Override
	public <S extends T> S findOne(Example<S> example, EntityGraph entityGraph) {
		return findOne(example);
	}

	@Override
	public T findOne(ID id, EntityGraph entityGraph) {
		return findOne(id);
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
	public List<T> findAll(Iterable<ID> ids, EntityGraph entityGraph) {
		return findAll(ids);
	}

	@Override
	public List<T> findAll(Sort sort, EntityGraph entityGraph) {
		return findAll(sort);
	}

	@Override
	public List<T> findAll(EntityGraph entityGraph) {
		return findAll();
	}
}
