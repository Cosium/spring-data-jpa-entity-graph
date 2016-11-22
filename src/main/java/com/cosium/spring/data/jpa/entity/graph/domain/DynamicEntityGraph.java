package com.cosium.spring.data.jpa.entity.graph.domain;

import java.util.Collections;
import java.util.List;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class DynamicEntityGraph extends AbstractEntityGraph {

	private final List<String> attributePaths;

	public DynamicEntityGraph(List<String> attributePaths){
		this.attributePaths = Collections.unmodifiableList(attributePaths);
	}

	public DynamicEntityGraph(EntityGraphType type, List<String> attributePaths){
		super(type);
		this.attributePaths = Collections.unmodifiableList(attributePaths);
	}

	@Override
	public List<String> getAttributePaths() {
		return attributePaths;
	}

	@Override
	public final String getName() {
		return null;
	}
}
