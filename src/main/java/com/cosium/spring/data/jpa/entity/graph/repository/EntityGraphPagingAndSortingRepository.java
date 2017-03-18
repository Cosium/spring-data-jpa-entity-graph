package com.cosium.spring.data.jpa.entity.graph.repository;

import java.io.Serializable;
import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created on 18/03/17.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphPagingAndSortingRepository<T, ID extends Serializable>
		extends PagingAndSortingRepository<T, ID>, EntityGraphCrudRepository<T, ID> {

	/**
	 * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 *
	 * @param pageable
	 * @param entityGraph can be {@literal null}.
	 * @return a page of entities
	 */
	Page<T> findAll(Pageable pageable, EntityGraph entityGraph);

	/**
	 * Returns all entities sorted by the given options.
	 *
	 * @param sort
	 * @param entityGraph can be {@literal null}.
	 * @return all entities sorted by the given options
	 */
	List<T> findAll(Sort sort, EntityGraph entityGraph);

}
