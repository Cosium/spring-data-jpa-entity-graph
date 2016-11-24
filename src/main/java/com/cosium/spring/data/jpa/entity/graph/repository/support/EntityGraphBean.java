package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.google.common.base.MoreObjects;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.query.JpaEntityGraph;
import org.springframework.util.Assert;

/**
 * Wrapper class allowing to hold a {@link JpaEntityGraph} with its associated domain class.
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class EntityGraphBean {

	private final JpaEntityGraph jpaEntityGraph;
	private final Class<?> domainClass;
	private final ResolvableType returnType;
	private final boolean optional;

	public EntityGraphBean(JpaEntityGraph jpaEntityGraph, Class<?> domainClass, ResolvableType returnType, boolean optional) {
		Assert.notNull(jpaEntityGraph);
		Assert.notNull(domainClass);
		Assert.notNull(returnType);

		this.jpaEntityGraph = jpaEntityGraph;
		this.domainClass = domainClass;
		this.returnType = returnType;
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

	/**
	 * @return True if this EntityGraph seems valid
	 */
	public boolean isValid() {
		if (domainClass.isAssignableFrom(returnType.resolve())) {
			return true;
		}
		for (Class genericType : returnType.resolveGenerics()) {
			if (domainClass.isAssignableFrom(genericType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("jpaEntityGraph", jpaEntityGraph)
				.add("domainClass", domainClass)
				.add("returnType", returnType)
				.add("optional", optional)
				.toString();
	}
}
