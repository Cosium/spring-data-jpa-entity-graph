package com.cosium.spring.data.jpa.graph.generator;

import com.squareup.javapoet.ClassName;

/** @author Réda Housni Alaoui */
public class Api {

  private Api() {}

  public static final ClassName ENTITY_GRAPH_TYPE_CLASS_NAME =
      ClassName.get("com.cosium.spring.data.jpa.entity.graph.domain", "EntityGraphType");

  public static final ClassName ENTITY_GRAPH_CLASS_NAME =
      ClassName.get("com.cosium.spring.data.jpa.entity.graph.domain", "EntityGraph");
}
