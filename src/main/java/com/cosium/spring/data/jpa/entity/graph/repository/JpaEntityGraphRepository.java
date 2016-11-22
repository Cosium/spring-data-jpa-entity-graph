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

	<S extends T> Page<S> findAll(Example<S> example, Pageable pageable, EntityGraph entityGraph);

	<S extends T> S findOne(Example<S> example, EntityGraph entityGraph);

	T findOne(ID id, EntityGraph entityGraph);

	Page<T> findAll(Pageable pageable, EntityGraph entityGraph);

	<S extends T> List<S> findAll(Example<S> example, Sort sort, EntityGraph entityGraph);

	<S extends T> List<S> findAll(Example<S> example, EntityGraph entityGraph);

	List<T> findAll(Iterable<ID> ids, EntityGraph entityGraph);

	List<T> findAll(Sort sort, EntityGraph entityGraph);

	List<T> findAll(EntityGraph entityGraph);
}
