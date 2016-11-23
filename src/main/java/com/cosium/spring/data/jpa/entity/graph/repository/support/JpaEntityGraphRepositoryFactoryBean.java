package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;
import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * Forces the use of {@link EntityGraphAwareRepositoryEntityManager} while targeting {@link JpaEntityGraphRepositoryFactory}.
 *
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class JpaEntityGraphRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
	extends JpaRepositoryFactoryBean<R, T, I>{

	@Override
	public void setEntityManager(EntityManager entityManager) {
		/* Make sure to use the EntityGraph aware EntityManager */
		super.setEntityManager(EntityGraphAwareRepositoryEntityManager.proxy(entityManager));
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new JpaEntityGraphRepositoryFactory(entityManager);
	}
}
