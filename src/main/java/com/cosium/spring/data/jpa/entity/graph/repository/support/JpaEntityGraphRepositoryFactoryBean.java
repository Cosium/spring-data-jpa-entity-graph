package com.cosium.spring.data.jpa.entity.graph.repository.support;

import java.io.Serializable;

import org.springframework.data.repository.Repository;

/**
 * Forces the use of {@link RepositoryEntityManagerEntityGraphInjector} while targeting {@link EntityGraphJpaRepositoryFactory}.
 *
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 * @deprecated Use {@link EntityGraphJpaRepositoryFactoryBean} instead
 */
@Deprecated
public class JpaEntityGraphRepositoryFactoryBean<R extends Repository<T, I>, T, I extends Serializable>
	extends EntityGraphJpaRepositoryFactoryBean<R, T, I>{

	public JpaEntityGraphRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

}
