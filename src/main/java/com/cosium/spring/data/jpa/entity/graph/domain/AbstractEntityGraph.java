package com.cosium.spring.data.jpa.entity.graph.domain;

import org.springframework.util.Assert;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public abstract class AbstractEntityGraph implements EntityGraph{

	private EntityGraphType entityGraphType = EntityGraphType.FETCH;

	public AbstractEntityGraph(){}

	public AbstractEntityGraph(EntityGraphType entityGraphType){
		Assert.notNull(entityGraphType);
		this.entityGraphType = entityGraphType;
	}

	@Override
	public EntityGraphType getEntityGraphType() {
		return entityGraphType;
	}

	public void setEntityGraphType(EntityGraphType entityGraphType) {
		Assert.notNull(entityGraphType);
		this.entityGraphType = entityGraphType;
	}
}
