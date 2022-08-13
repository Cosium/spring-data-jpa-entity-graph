package com.cosium.spring.data.jpa.graph.generator;

import com.squareup.javapoet.ClassName;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author Réda Housni Alaoui
 */
public class EntityGraphClassName {

  private final Elements elements;
  private final TypeElement entityTypeElement;
  private final String simpleName;

  public EntityGraphClassName(Elements elements, TypeElement entityTypeElement) {
    this.elements = elements;
    this.entityTypeElement = entityTypeElement;
    this.simpleName = entityTypeElement.getSimpleName() + "EntityGraph";
  }

  public String simpleName() {
    return entityTypeElement.getSimpleName() + "EntityGraph";
  }

  public ClassName toClassName() {
    return ClassName.get(
        elements.getPackageOf(entityTypeElement).getQualifiedName().toString(), simpleName);
  }
}
