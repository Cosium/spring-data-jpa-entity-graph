package com.cosium.spring.data.jpa.entity.graph.repository;

import java.io.Serializable;
import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface JpaEntityGraphRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

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
	 * Retrieves an entity by its id.
	 *
	 * @param id must not be {@literal null}.
	 * @param entityGraph can be {@literal null}.
	 * @return the entity with the given id or {@literal null} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	T findOne(ID id, EntityGraph entityGraph);

	/**
	 * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 *
	 * @param pageable
	 * @param entityGraph can be {@literal null}.
	 * @return a page of entities
	 */
	Page<T> findAll(Pageable pageable, EntityGraph entityGraph);

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

	/**
	 * Returns all instances of the type with the given IDs.
	 *
	 * @param ids
	 * @param entityGraph can be {@literal null}.
	 * @return
	 */
	List<T> findAll(Iterable<ID> ids, EntityGraph entityGraph);

	/**
	 * Returns all entities sorted by the given options.
	 *
	 * @param sort
	 * @param entityGraph can be {@literal null}.
	 * @return all entities sorted by the given options
	 */
	List<T> findAll(Sort sort, EntityGraph entityGraph);

	/**
	 * Returns all instances of the type.
	 *
	 * @param entityGraph can be {@literal null}.
	 * @return all entities
	 */
	List<T> findAll(EntityGraph entityGraph);
}
