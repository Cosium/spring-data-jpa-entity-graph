package com.cosium.spring.data.jpa.entity.graph.domain;

import java.util.List;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public interface EntityGraph {

	/**
	 * @return The type of the entity graph. Can't be null.
	 */
	EntityGraphType getEntityGraphType();

	/**
	 * @return The name to use to retrieve the EntityGraph. May be null or empty
	 */
	String getEntityGraphName();

	/**
	 * @return The attribute paths. May be null.
	 */
	List<String> getEntityGraphAttributePaths();
}
