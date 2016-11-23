package com.cosium.spring.data.jpa.entity.graph.repository.sample;

import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphType;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public abstract class EntityGraphSpecification<T> implements Specification<T>, EntityGraph {

	private final String entityGraphName;

	public EntityGraphSpecification(String entityGraphName) {
		this.entityGraphName = entityGraphName;
	}

	@Override
	public String getName() {
		return entityGraphName;
	}

	@Override
	public EntityGraphType getType() {
		return EntityGraphType.FETCH;
	}

	@Override
	public List<String> getAttributePaths() {
		return null;
	}
}
