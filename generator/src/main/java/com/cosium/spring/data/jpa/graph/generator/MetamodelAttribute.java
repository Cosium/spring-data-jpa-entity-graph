package com.cosium.spring.data.jpa.graph.generator;

import static javax.lang.model.element.ElementKind.FIELD;

import java.util.Optional;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.persistence.Entity;
import javax.persistence.metamodel.Attribute;

/** @author Réda Housni Alaoui */
public class MetamodelAttribute {

  private final VariableElement variableElement;

  private MetamodelAttribute(VariableElement variableElement) {
    this.variableElement = variableElement;
  }

  public static Optional<MetamodelAttribute> parse(
      Elements elements, Types types, Element element) {
    if (!(element instanceof VariableElement)) {
      return Optional.empty();
    }

    VariableElement variableElement = (VariableElement) element;
    if (variableElement.getKind() != FIELD) {
      return Optional.empty();
    }

    TypeElement attributeTypeElement = elements.getTypeElement(Attribute.class.getCanonicalName());
    if (!types.isSubtype(
        types.erasure(variableElement.asType()), types.erasure(attributeTypeElement.asType()))) {
      return Optional.empty();
    }

    return Optional.of(new MetamodelAttribute(variableElement));
  }

  public Optional<MetamodelAttributeTarget> jpaEntityTarget() {
    DeclaredType attributeType = (DeclaredType) variableElement.asType();
    DeclaredType targetType = (DeclaredType) attributeType.getTypeArguments().get(1);
    TypeElement targetTypeElement = (TypeElement) targetType.asElement();

    if (targetTypeElement.getAnnotation(Entity.class) == null) {
      return Optional.empty();
    }

    return Optional.of(
        new MetamodelAttributeTarget(
            variableElement.getSimpleName().toString(), targetTypeElement));
  }
}
