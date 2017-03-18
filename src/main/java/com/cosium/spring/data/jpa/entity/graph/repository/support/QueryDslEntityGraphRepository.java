package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;
import java.io.Serializable;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQueryDslPredicateExecutor;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;

/**
 * A {@link org.springframework.data.querydsl.QueryDslPredicateExecutor} that supports {@link EntityGraph} passed through method arguments.
 *
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class QueryDslEntityGraphRepository<T, ID extends Serializable>
	extends EntityGraphSimpleJpaRepository<T, ID> implements EntityGraphQueryDslPredicateExecutor<T> {

	private QueryDslJpaRepository<T, ID> queryDslJpaRepositoryDelegate;

	public QueryDslEntityGraphRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.queryDslJpaRepositoryDelegate = new EntityGraphAwareQueryDslJpaRepository<T, ID>((JpaEntityInformation<T, ID>) entityInformation, entityManager);
	}

	public QueryDslEntityGraphRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager,
										 EntityPathResolver resolver) {
		super(entityInformation, entityManager);
		this.queryDslJpaRepositoryDelegate = new EntityGraphAwareQueryDslJpaRepository<T, ID>(entityInformation, entityManager, resolver);
	}

	@Override
	public T findOne(Predicate predicate, EntityGraph entityGraph) {
		return queryDslJpaRepositoryDelegate.findOne(predicate);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate, EntityGraph entityGraph) {
		return queryDslJpaRepositoryDelegate.findAll(predicate);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate, Sort sort, EntityGraph entityGraph) {
		return queryDslJpaRepositoryDelegate.findAll(predicate, sort);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate, EntityGraph entityGraph, OrderSpecifier<?>... orders) {
		return queryDslJpaRepositoryDelegate.findAll(predicate, orders);
	}

	@Override
	public Iterable<T> findAll(EntityGraph entityGraph, OrderSpecifier<?>... orders) {
		return queryDslJpaRepositoryDelegate.findAll(orders);
	}

	@Override
	public Page<T> findAll(Predicate predicate, Pageable pageable, EntityGraph entityGraph) {
		return queryDslJpaRepositoryDelegate.findAll(predicate, pageable);
	}

	@Override
	public T findOne(Predicate predicate) {
		return queryDslJpaRepositoryDelegate.findOne(predicate);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate) {
		return queryDslJpaRepositoryDelegate.findAll(predicate);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate, Sort sort) {
		return queryDslJpaRepositoryDelegate.findAll(predicate, sort);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
		return queryDslJpaRepositoryDelegate.findAll(predicate, orders);
	}

	@Override
	public Iterable<T> findAll(OrderSpecifier<?>... orders) {
		return queryDslJpaRepositoryDelegate.findAll(orders);
	}

	@Override
	public Page<T> findAll(Predicate predicate, Pageable pageable) {
		return queryDslJpaRepositoryDelegate.findAll(predicate, pageable);
	}

	@Override
	public long count(Predicate predicate) {
		return queryDslJpaRepositoryDelegate.count(predicate);
	}

	@Override
	public boolean exists(Predicate predicate) {
		return queryDslJpaRepositoryDelegate.exists(predicate);
	}
}
