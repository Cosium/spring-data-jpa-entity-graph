package com.cosium.spring.data.jpa.entity.graph.repository;

import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * Created on 18/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphQueryByExampleExecutor<T> extends QueryByExampleExecutor<T> {

	/**
	 * Returns a {@link Page} of entities matching the given {@link Example}. In case no match could be found, an empty
	 * {@link Page} is returned.
	 *
	 * @param example can be {@literal null}.
	 * @param pageable can be {@literal null}.
	 * @param entityGraph can be {@literal null}.
	 * @return a {@link Page} of entities matching the given {@link Example}.
	 */
	<S extends T> Page<S> findAll(Example<S> example, Pageable pageable, EntityGraph entityGraph);

	/**
	 * Returns a single entity matching the given {@link Example} or {@literal null} if none was found.
	 *
	 * @param example can be {@literal null}.
	 * @param entityGraph can be {@literal null}.
	 * @return a single entity matching the given {@link Example} or {@literal null} if none was found.
	 * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if the Example yields more than one result.
	 */
	<S extends T> S findOne(Example<S> example, EntityGraph entityGraph);

	/**
	 * Returns all entities matching the given {@link Example} applying the given {@link Sort}. In case no match could be
	 * found an empty {@link Iterable} is returned.
	 *
	 * @param example can be {@literal null}.
	 * @param sort the {@link Sort} specification to sort the results by, must not be {@literal null}.
	 * @param entityGraph can be {@literal null}.
	 * @return all entities matching the given {@link Example}.
	 */
	<S extends T> List<S> findAll(Example<S> example, Sort sort, EntityGraph entityGraph);

	/**
	 * Returns the number of instances matching the given {@link Example}.
	 *
	 * @param example the {@link Example} to count instances for, can be {@literal null}.
	 * @param entityGraph can be {@literal null}.
	 * @return the number of instances matching the {@link Example}.
	 */
	<S extends T> List<S> findAll(Example<S> example, EntityGraph entityGraph);

}
