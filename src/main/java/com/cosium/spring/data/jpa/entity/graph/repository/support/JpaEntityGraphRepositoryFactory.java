package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class JpaEntityGraphRepositoryFactory extends JpaRepositoryFactory {

	/**
	 * Creates a new {@link JpaRepositoryFactory}.
	 *
	 * @param entityManager must not be {@literal null}
	 */
	public JpaEntityGraphRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
		addRepositoryProxyPostProcessor(new JpaEntityGraphPostProcessor());
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return SimpleJpaEntityGraphRepository.class;
	}
}
