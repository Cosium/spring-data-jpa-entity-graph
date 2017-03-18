package com.cosium.spring.data.jpa.entity.graph.repository;

import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphJpaSpecificationExecutor<T> extends JpaSpecificationExecutor<T> {

	/**
	 * Returns a single entity matching the given {@link Specification}.
	 *
	 * @param spec
	 * @param  entityGraph
	 * @return
	 */
	T findOne(Specification<T> spec, EntityGraph entityGraph);

	/**
	 * Returns all entities matching the given {@link Specification}.
	 *
	 * @param spec
	 * @param  entityGraph
	 * @return
	 */
	List<T> findAll(Specification<T> spec, EntityGraph entityGraph);

	/**
	 * Returns a {@link Page} of entities matching the given {@link Specification}.
	 *
	 * @param spec
	 * @param pageable
	 * @param  entityGraph
	 * @return
	 */
	Page<T> findAll(Specification<T> spec, Pageable pageable, EntityGraph entityGraph);

	/**
	 * Returns all entities matching the given {@link Specification} and {@link Sort}.
	 *
	 * @param spec
	 * @param sort
	 * @param  entityGraph
	 * @return
	 */
	List<T> findAll(Specification<T> spec, Sort sort, EntityGraph entityGraph);

}
