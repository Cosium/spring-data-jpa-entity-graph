package com.cosium.spring.data.jpa.graph.generator;

import static javax.lang.model.element.ElementKind.*;

import jakarta.persistence.Entity;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.PluralAttribute;
import java.util.List;
import java.util.Optional;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Réda Housni Alaoui
 */
public class MetamodelAttribute {

  private final VariableElement variableElement;
  private final boolean pluralAttribute;

  private MetamodelAttribute(VariableElement variableElement, boolean pluralAttribute) {
    this.variableElement = variableElement;
    this.pluralAttribute = pluralAttribute;
  }

  public static Optional<MetamodelAttribute> parse(
      Elements elements, Types types, Element element) {
    if (!(element instanceof VariableElement variableElement)) {
      return Optional.empty();
    }

    if (variableElement.getKind() != FIELD) {
      return Optional.empty();
    }

    TypeElement attributeTypeElement = elements.getTypeElement(Attribute.class.getCanonicalName());
    if (!types.isSubtype(
        types.erasure(variableElement.asType()), types.erasure(attributeTypeElement.asType()))) {
      return Optional.empty();
    }

    TypeElement pluralAttributeTypeElement =
        elements.getTypeElement(PluralAttribute.class.getCanonicalName());
    boolean pluralAttribute =
        types.isSubtype(
            types.erasure(variableElement.asType()),
            types.erasure(pluralAttributeTypeElement.asType()));

    return Optional.of(new MetamodelAttribute(variableElement, pluralAttribute));
  }

  public Optional<MetamodelAttributeTarget> jpaTarget() {
    TypeMirror rawAttributeType = variableElement.asType();
    if (!(rawAttributeType instanceof DeclaredType attributeType)) {
      return Optional.empty();
    }
    List<? extends TypeMirror> typeArguments = attributeType.getTypeArguments();
    // This is the lazy way of doing this. We consider that any sub interfaces of
    // jakarta.persistence.metamodel.Attribute
    // will declare the target type as the last type arguments.
    // e.g. MapAttribute<Brand, Long, Product> where Product is the target
    // TODO do this cleanly by browsing the class hierarchy
    TypeMirror rawTargetType = typeArguments.get(typeArguments.size() - 1);
    if (!(rawTargetType instanceof DeclaredType targetType)) {
      return Optional.empty();
    }
    Element rawTargetTypeElement = targetType.asElement();
    if (!(rawTargetTypeElement instanceof TypeElement targetTypeElement)) {
      return Optional.empty();
    }

    if (targetTypeElement.getAnnotation(Entity.class) == null && !pluralAttribute) {
      return Optional.empty();
    }

    return Optional.of(
        new MetamodelAttributeTarget(
            variableElement.getSimpleName().toString(), targetTypeElement));
  }
}
