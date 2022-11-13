package com.cosium.spring.data.jpa.graph.generator;

import static java.util.Objects.requireNonNull;

import com.squareup.javapoet.ClassName;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Réda Housni Alaoui
 */
public class MetamodelClass {

  private final Elements elements;
  private final Types types;
  private final TypeElement typeElement;

  public MetamodelClass(Elements elements, Types types, TypeElement typeElement) {
    this.elements = requireNonNull(elements);
    this.types = requireNonNull(types);
    this.typeElement = requireNonNull(typeElement);
  }

  public EntityGraph createEntityGraph() {
    StaticMetamodel staticMetamodel = typeElement.getAnnotation(StaticMetamodel.class);
    TypeElement entityTypeElement;
    try {
      staticMetamodel.value();
      throw new RuntimeException("entityTypeElement.value() didn't throw !");
    } catch (MirroredTypeException e) {
      entityTypeElement = (TypeElement) types.asElement(e.getTypeMirror());
    }

    ClassName entityGraphClassName =
        new EntityGraphClassName(elements, entityTypeElement).toClassName();

    RootComposer rootComposer = new RootComposer(entityGraphClassName);
    NodeComposer nodeComposer = new NodeComposer();

    List<Composer> composers = Arrays.asList(rootComposer, nodeComposer);

    elements.getAllMembers(typeElement).stream()
        .map(member -> MetamodelAttribute.parse(elements, types, member))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(MetamodelAttribute::jpaTarget)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .forEach(attribute -> composers.forEach(composer -> composer.addPath(elements, attribute)));

    return new EntityGraph(entityGraphClassName, rootComposer, nodeComposer);
  }
}
