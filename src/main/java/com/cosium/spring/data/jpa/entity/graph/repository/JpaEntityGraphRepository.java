package com.cosium.spring.data.jpa.entity.graph.repository;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 * @deprecated Use {@link EntityGraphJpaRepository} instead
 */
@Deprecated
@NoRepositoryBean
public interface JpaEntityGraphRepository<T, ID extends Serializable> extends EntityGraphJpaRepository<T, ID>{

}
