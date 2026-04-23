package com.cosium.spring.data.jpa.entity.graph.domain2;

import static java.util.Objects.requireNonNull;

import jakarta.persistence.AttributeNode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor.SpecificationFluentQuery;

/**
 * @author Réda Housni Alaoui
 */
public class EntityGraphQueryHint {

  private final EntityGraphType type;
  private final jakarta.persistence.EntityGraph<?> entityGraph;
  private final boolean failIfInapplicable;

  public EntityGraphQueryHint(EntityGraphType type, EntityGraph<?> entityGraph) {
    this(type, entityGraph, true);
  }

  /**
   * @param failIfInapplicable true if an {@link InapplicableEntityGraphException} must be thrown if
   *     this entity graph cannot be applied. This parameter is ignored in the context of a {@link
   *     org.springframework.data.repository.query.FluentQuery}.
   */
  public EntityGraphQueryHint(
      EntityGraphType type, EntityGraph<?> entityGraph, boolean failIfInapplicable) {
    this.type = requireNonNull(type);
    this.entityGraph = requireNonNull(entityGraph);
    this.failIfInapplicable = failIfInapplicable;
  }

  public EntityGraphType type() {
    return type;
  }

  public EntityGraph<?> entityGraph() {
    return entityGraph;
  }

  public boolean failIfInapplicable() {
    return failIfInapplicable;
  }

  public final <T> SpecificationFluentQuery<T> applyTo(SpecificationFluentQuery<T> query) {
    return query.project(toProperties());
  }

  private Collection<String> toProperties() {
    List<String> paths = new ArrayList<>();
    for (AttributeNode<?> node : entityGraph.getAttributeNodes()) {
      visitNode(node, null, paths);
    }
    Collections.sort(paths);
    return paths;
  }

  private void visitNode(
      AttributeNode<?> node, @Nullable String parentPath, List<String> collectedPaths) {
    String visitedPath =
        Optional.ofNullable(parentPath)
            .map(path -> path + "." + node.getAttributeName())
            .orElseGet(node::getAttributeName);

    Map<Class, Subgraph> subgraphs = node.getSubgraphs();
    if (subgraphs.isEmpty()) {
      collectedPaths.add(visitedPath);
      return;
    }

    for (Subgraph<?> subgraph : subgraphs.values()) {
      for (AttributeNode<?> childNode : subgraph.getAttributeNodes()) {
        visitNode(childNode, visitedPath, collectedPaths);
      }
    }
  }
}
