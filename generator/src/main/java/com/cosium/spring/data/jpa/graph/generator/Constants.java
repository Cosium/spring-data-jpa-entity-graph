package com.cosium.spring.data.jpa.graph.generator;

import com.squareup.javapoet.ClassName;

/**
 * @author Réda Housni Alaoui
 */
public class Constants {

  private Constants() {}

  public static final ClassName ENTITY_GRAPH_TYPE_CLASS_NAME =
      ClassName.get("com.cosium.spring.data.jpa.entity.graph.domain2", "EntityGraphType");

  public static final ClassName ENTITY_GRAPH_CLASS_NAME =
      ClassName.get("com.cosium.spring.data.jpa.entity.graph.domain2", "EntityGraph");

  public static final ClassName DYNAMIC_ENTITY_GRAPH_CLASS_NAME =
      ClassName.get("com.cosium.spring.data.jpa.entity.graph.domain2", "DynamicEntityGraph");

  public static final ClassName ENTITY_MANAGER_CLASS_NAME =
      ClassName.get("jakarta.persistence", "EntityManager");
  public static final ClassName ENTITY_GRAPH_QUERY_HINT_CLASS_NAME =
      ClassName.get("com.cosium.spring.data.jpa.entity.graph.domain2", "EntityGraphQueryHint");

  public static final String PATH_SEPARATOR = "____";
}
