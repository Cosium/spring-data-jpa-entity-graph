package com.cosium.spring.data.jpa.entity.graph.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 * @deprecated Use {@link EntityGraphQueryDslPredicateExecutor} instead
 */
@Deprecated
@NoRepositoryBean
public interface JpaEntityGraphQueryDslPredicateExecutor<T> extends EntityGraphQueryDslPredicateExecutor<T>{

}
