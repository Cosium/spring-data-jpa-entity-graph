package com.cosium.spring.data.jpa.entity.graph.domain;

import java.util.List;

import org.springframework.util.Assert;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class NamedEntityGraph extends AbstractEntityGraph {

	private final String name;

	public NamedEntityGraph(EntityGraphType type, String name) {
		super(type);
		Assert.hasLength(name);
		this.name = name;
	}

	public NamedEntityGraph(String name){
		Assert.hasLength(name);
		this.name = name;
	}

	@Override
	public String getEntityGraphName() {
		return name;
	}

	@Override
	public final List<String> getEntityGraphAttributePaths() {
		return null;
	}

}
