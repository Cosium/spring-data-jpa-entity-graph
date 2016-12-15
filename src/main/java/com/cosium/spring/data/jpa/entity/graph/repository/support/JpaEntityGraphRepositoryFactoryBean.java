package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;
import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * Forces the use of {@link RepositoryEntityManagerEntityGraphInjector} while targeting {@link JpaEntityGraphRepositoryFactory}.
 *
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class JpaEntityGraphRepositoryFactoryBean<R extends Repository<T, I>, T, I extends Serializable>
	extends JpaRepositoryFactoryBean<R, T, I>{

	/**
	 * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
	 *
	 * @param repositoryInterface must not be {@literal null}.
	 */
	public JpaEntityGraphRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		/* Make sure to use the EntityManager able to inject captured EntityGraphs */
		super.setEntityManager(RepositoryEntityManagerEntityGraphInjector.proxy(entityManager));
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new JpaEntityGraphRepositoryFactory(entityManager);
	}
}
