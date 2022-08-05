package com.cosium.spring.data.jpa.entity.graph.repository.support;

import static java.util.Objects.requireNonNull;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;

/**
 * @author Réda Housni Alaoui
 */
class EntityGraphQueryHintCandidate {

	private final EntityGraphQueryHint queryHint;
	private final Class<?> domainClass;
	private final boolean primary;

	public EntityGraphQueryHintCandidate(EntityGraphQueryHint queryHint, Class<?> domainClass, boolean primary) {
		this.queryHint = requireNonNull(queryHint);
		this.domainClass = requireNonNull(domainClass);
		this.primary = primary;
	}

	public EntityGraphQueryHint queryHint() {
		return queryHint;
	}

	public Class<?> domainClass() {
		return domainClass;
	}

	public boolean primary() {
		return primary;
	}
}
