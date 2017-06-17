package com.cosium.spring.data.jpa.entity.graph.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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
	 * @see CrudRepository#findById(Object)
	 */
	Optional<T> findById(ID id, EntityGraph entityGraph);

	/**
	 * @see CrudRepository#findAllById(Iterable)
	 */
	Iterable<T> findAllById(Iterable<ID> ids, EntityGraph entityGraph);

	/**
	 * @see CrudRepository#findAll()
	 */
	Iterable<T> findAll(EntityGraph entityGraph);
}
