package com.cosium.spring.data.jpa.graph.generator;

import static java.util.Objects.requireNonNull;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import javax.lang.model.element.TypeElement;
import org.jspecify.annotations.Nullable;

/**
 * @author Réda Housni Alaoui
 */
public class MetamodelAttributeTarget {

  private final String attributeName;
  private final TypeElement targetType;
  private final @Nullable Entity entityAnnotation;
  private final @Nullable Embeddable embeddableAnnotation;

  public MetamodelAttributeTarget(String attributeName, TypeElement targetType) {
    this.attributeName = requireNonNull(attributeName);
    this.targetType = requireNonNull(targetType);
    this.entityAnnotation = targetType.getAnnotation(Entity.class);
    this.embeddableAnnotation = targetType.getAnnotation(Embeddable.class);
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

  public boolean isEmbeddable() {
    return embeddableAnnotation != null;
  }
}
