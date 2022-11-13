/*
 * Copyright 2014-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cosium.spring.data.jpa.entity.graph.domain2;

import jakarta.persistence.AttributeNode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Copied and pasted from <a
 * href="https://github.com/spring-projects/spring-data-jpa/blob/05bfccadac7a3c04e6fc569542a5111df365d503/src/main/java/org/springframework/data/jpa/repository/query/Jpa21Utils.java">https://github.com/spring-projects/spring-data-jpa/blob/05bfccadac7a3c04e6fc569542a5111df365d503/src/main/java/org/springframework/data/jpa/repository/query/Jpa21Utils.java</a>
 *
 * @author Thomas Darimont
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author Jens Schauder
 * @author Réda Housni Alaoui
 */
class DynamicJpaEntityGraphs {

  private DynamicJpaEntityGraphs() {}

  public static EntityGraph<?> create(
      EntityManager entityManager, Class<?> entityType, List<String> attributePaths) {
    jakarta.persistence.EntityGraph<?> jpaEntityGraph = entityManager.createEntityGraph(entityType);
    List<String> modifiableAttributePaths = new ArrayList<>(attributePaths);

    // Sort to ensure that the intermediate entity subgraphs are created accordingly.
    Collections.sort(modifiableAttributePaths);

    for (String path : modifiableAttributePaths) {
      String[] pathComponents = StringUtils.delimitedListToStringArray(path, ".");
      createGraph(pathComponents, 0, jpaEntityGraph, null);
    }

    return jpaEntityGraph;
  }

  private static void createGraph(
      String[] pathComponents,
      int offset,
      jakarta.persistence.EntityGraph<?> root,
      Subgraph<?> parent) {

    String attributeName = pathComponents[offset];

    // we found our leaf property, now let's see if it already exists and add it if not
    if (pathComponents.length - 1 == offset) {

      if (parent == null && !exists(attributeName, root.getAttributeNodes())) {
        root.addAttributeNodes(attributeName);
      } else if (parent != null && !exists(attributeName, parent.getAttributeNodes())) {
        parent.addAttributeNodes(attributeName);
      }

      return;
    }

    AttributeNode<?> node = findAttributeNode(attributeName, root, parent);

    if (node != null) {

      Subgraph<?> subgraph = getSubgraph(node);

      if (subgraph == null) {
        subgraph =
            parent != null ? parent.addSubgraph(attributeName) : root.addSubgraph(attributeName);
      }

      createGraph(pathComponents, offset + 1, root, subgraph);

      return;
    }

    if (parent == null) {
      createGraph(pathComponents, offset + 1, root, root.addSubgraph(attributeName));
    } else {
      createGraph(pathComponents, offset + 1, root, parent.addSubgraph(attributeName));
    }
  }

  /**
   * Checks the given {@link List} of {@link AttributeNode}s for the existence of an {@link
   * AttributeNode} matching the given {@literal attributeNodeName}.
   */
  private static boolean exists(String attributeNodeName, List<AttributeNode<?>> nodes) {
    return findAttributeNode(attributeNodeName, nodes) != null;
  }

  /**
   * Find the {@link AttributeNode} matching the given {@literal attributeNodeName} in given {@link
   * Subgraph} or {@link jakarta.persistence.EntityGraph} favoring matches {@link Subgraph} over
   * {@link jakarta.persistence.EntityGraph}.
   *
   * @return {@literal null} if not found.
   */
  private static AttributeNode<?> findAttributeNode(
      String attributeNodeName,
      jakarta.persistence.EntityGraph<?> entityGraph,
      Subgraph<?> parent) {
    return findAttributeNode(
        attributeNodeName,
        parent != null ? parent.getAttributeNodes() : entityGraph.getAttributeNodes());
  }

  /**
   * Find the {@link AttributeNode} matching the given {@literal attributeNodeName} in given {@link
   * List} of {@link AttributeNode}s.
   *
   * @return {@literal null} if not found.
   */
  private static AttributeNode<?> findAttributeNode(
      String attributeNodeName, List<AttributeNode<?>> nodes) {

    for (AttributeNode<?> node : nodes) {
      if (ObjectUtils.nullSafeEquals(node.getAttributeName(), attributeNodeName)) {
        return node;
      }
    }

    return null;
  }

  /**
   * Extracts the first {@link Subgraph} from the given {@link AttributeNode}. Ignores any potential
   * different {@link Subgraph}s registered for more concrete {@link Class}es as the dynamically
   * created graph does not distinguish between those.
   */
  private static Subgraph<?> getSubgraph(AttributeNode<?> node) {
    return node.getSubgraphs().isEmpty() ? null : node.getSubgraphs().values().iterator().next();
  }
}
