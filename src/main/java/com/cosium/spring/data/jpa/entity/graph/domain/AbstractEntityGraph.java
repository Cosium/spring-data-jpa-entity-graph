package com.cosium.spring.data.jpa.entity.graph.domain;

import org.springframework.util.Assert;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public abstract class AbstractEntityGraph implements EntityGraph{

	private EntityGraphType type = EntityGraphType.FETCH;

	public AbstractEntityGraph(){}

	public AbstractEntityGraph(EntityGraphType type){
		Assert.notNull(type);
		this.type = type;
	}

	@Override
	public EntityGraphType getEntityGraphType() {
		return type;
	}

	public void setType(EntityGraphType type) {
		Assert.notNull(type);
		this.type = type;
	}
}
