package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.JpaEntityGraphRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class SimpleJpaEntityGraphRepository<T, ID extends Serializable>
		extends SimpleJpaRepository<T, ID>
		implements JpaEntityGraphRepository<T, ID> {

	private final EntityManager entityManager;
	private final Class<T> domainClass;

	public SimpleJpaEntityGraphRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
		this.domainClass = entityInformation.getJavaType();
	}

	public SimpleJpaEntityGraphRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
		this.domainClass = domainClass;
	}

	@Override
	protected Map<String, Object> getQueryHints() {
		Map<String, Object> queryHints = super.getQueryHints();
		queryHints.putAll(Jpa21Utils.tryGetFetchGraphHints(entityManager, JpaEntityGraphPostProcessor.getCurrentJpaEntityGraph(), domainClass));
		return queryHints;
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
