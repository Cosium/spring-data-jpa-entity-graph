package com.cosium.spring.data.jpa.entity.graph.repository.support;

import org.springframework.data.jpa.repository.query.JpaEntityGraph;
import org.springframework.util.Assert;

/**
 * Wrapper class allowing to hold a {@link JpaEntityGraph} with its associated domain class.
 *
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class EntityGraphBean {
	private final JpaEntityGraph jpaEntityGraph;
	private final Class<?> domainClass;
	private final boolean optional;

	public EntityGraphBean(JpaEntityGraph jpaEntityGraph, Class<?> domainClass, boolean optional) {
		Assert.notNull(jpaEntityGraph);
		Assert.notNull(domainClass);

		this.jpaEntityGraph = jpaEntityGraph;
		this.domainClass = domainClass;
		this.optional = optional;
	}

	/**
	 * @return The jpa entity graph
	 */
	public JpaEntityGraph getJpaEntityGraph() {
		return jpaEntityGraph;
	}

	/**
	 * @return The jpa entity class
	 */
	public Class<?> getDomainClass() {
		return domainClass;
	}

	/**
	 * @return True if this entity graph is not mandatory
	 */
	public boolean isOptional() {
		return optional;
	}
}
