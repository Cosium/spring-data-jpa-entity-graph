package com.cosium.spring.data.jpa.entity.graph.repository.support;

import org.springframework.data.jpa.repository.query.JpaEntityGraph;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class EntityGraphBean {
	private final JpaEntityGraph jpaEntityGraph;
	private final Class<?> domainClass;

	public EntityGraphBean(JpaEntityGraph jpaEntityGraph, Class<?> domainClass) {
		this.jpaEntityGraph = jpaEntityGraph;
		this.domainClass = domainClass;
	}

	public JpaEntityGraph getJpaEntityGraph() {
		return jpaEntityGraph;
	}

	public Class<?> getDomainClass() {
		return domainClass;
	}
}
