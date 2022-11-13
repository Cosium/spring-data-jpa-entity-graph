package com.cosium.spring.data.jpa.graph.generator;

import static java.util.Objects.requireNonNull;

import jakarta.persistence.Entity;
import javax.lang.model.element.TypeElement;

/**
 * @author Réda Housni Alaoui
 */
public class MetamodelAttributeTarget {

  private final String attributeName;
  private final TypeElement targetType;
  private final Entity entityAnnotation;

  public MetamodelAttributeTarget(String attributeName, TypeElement targetType) {
    this.attributeName = requireNonNull(attributeName);
    this.targetType = requireNonNull(targetType);
    this.entityAnnotation = targetType.getAnnotation(Entity.class);
  }

  public String attributeName() {
    return attributeName;
  }

  public TypeElement targetType() {
    return targetType;
  }

  public boolean isEntity() {
    return entityAnnotation != null;
  }
}
