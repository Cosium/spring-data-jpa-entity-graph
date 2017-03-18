package com.cosium.spring.data.jpa.entity.graph.repository;

import java.io.Serializable;
import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 18/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphCrudRepository<T, ID extends Serializable> extends CrudRepository<T, ID>{

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
	 * Returns all instances of the type with the given IDs.
	 *
	 * @param ids
	 * @param entityGraph can be {@literal null}.
	 * @return
	 */
	List<T> findAll(Iterable<ID> ids, EntityGraph entityGraph);

	/**
	 * Returns all instances of the type.
	 *
	 * @param entityGraph can be {@literal null}.
	 * @return all entities
	 */
	List<T> findAll(EntityGraph entityGraph);
}
