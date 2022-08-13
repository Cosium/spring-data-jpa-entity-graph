package com.cosium.spring.data.jpa.graph.generator;

import javax.lang.model.element.TypeElement;

/**
 * @author Réda Housni Alaoui
 */
public class MetamodelAttributeTarget {

  private final String attributeName;
  private final TypeElement targetType;

  public MetamodelAttributeTarget(String attributeName, TypeElement targetType) {
    this.attributeName = attributeName;
    this.targetType = targetType;
  }

  public String attributeName() {
    return attributeName;
  }

  public TypeElement targetType() {
    return targetType;
  }
}
