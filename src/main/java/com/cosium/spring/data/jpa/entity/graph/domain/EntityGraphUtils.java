package com.cosium.spring.data.jpa.entity.graph.domain;

import java.util.List;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class EntityGraphUtils {

	private static final EntityGraph EMPTY_ENTITY_GRAPH = new EmptyEntityGraph();

	/**
	 * @param entityGraph
	 * @return True if the provided EntityGraph is empty
	 */
	public static boolean isEmpty(EntityGraph entityGraph) {
		return entityGraph == null ||
				(
						entityGraph.getEntityGraphAttributePaths() == null
								&& entityGraph.getEntityGraphName() == null
								&& entityGraph.getEntityGraphType() == null
				);
	}

	/**
	 * @return An empty EntityGraph
	 */
	public static EntityGraph empty() {
		return EMPTY_ENTITY_GRAPH;
	}

	/**
	 * @param name The name of the targeted EntityGraph
	 * @return A EntityGraph referenced by name
	 */
	public static EntityGraph fromName(String name) {
		return new NamedEntityGraph(name);
	}

	private static final class EmptyEntityGraph implements EntityGraph {

		@Override
		public EntityGraphType getEntityGraphType() {
			return null;
		}

		@Override
		public String getEntityGraphName() {
			return null;
		}

		@Override
		public List<String> getEntityGraphAttributePaths() {
			return null;
		}
	}
}
