package com.cosium.spring.data.jpa.entity.graph.domain;

import java.util.Collections;
import java.util.List;

import com.google.common.base.MoreObjects;

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
	public List<String> getEntityGraphAttributePaths() {
		return attributePaths;
	}

	@Override
	public final String getEntityGraphName() {
		return null;
	}


	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("attributePaths", attributePaths)
				.toString();
	}
}
