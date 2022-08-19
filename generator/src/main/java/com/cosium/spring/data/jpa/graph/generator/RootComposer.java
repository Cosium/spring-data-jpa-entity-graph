package com.cosium.spring.data.jpa.graph.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

/**
 * @author Réda Housni Alaoui
 */
public class RootComposer implements Composer {

  private static final String SIMPLE_NAME = "RootComposer";
  private final ClassName entityGraphClassName;
  private final TypeSpec.Builder typeSpecBuilder;
  private boolean referencesLeafComposer;

  public RootComposer(ClassName entityGraphClassName) {
    this.entityGraphClassName = entityGraphClassName;

    FieldSpec entityGraphTypeField =
        FieldSpec.builder(Constants.ENTITY_GRAPH_TYPE_CLASS_NAME, "entityGraphType")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            .build();

    ParameterizedTypeName listOfString = ParameterizedTypeName.get(List.class, String.class);
    ParameterizedTypeName listOfListOfString =
        ParameterizedTypeName.get(ClassName.get(List.class), listOfString);

    FieldSpec entityGraphAttributePathsField =
        FieldSpec.builder(listOfListOfString, "entityGraphAttributePaths")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            .build();

    MethodSpec constructor =
        MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addStatement("this($T.$N)", Constants.ENTITY_GRAPH_TYPE_CLASS_NAME, "LOAD")
            .build();

    MethodSpec constructorWithEntityGraphType =
        MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(Constants.ENTITY_GRAPH_TYPE_CLASS_NAME, "entityGraphType")
            .addStatement("this.entityGraphType = entityGraphType")
            .addStatement(
                "entityGraphAttributePaths = new $T()",
                ParameterizedTypeName.get(ClassName.get(ArrayList.class), listOfString))
            .build();

    MethodSpec buildMethod =
        MethodSpec.methodBuilder(Constants.PATH_SEPARATOR)
            .addModifiers(Modifier.PUBLIC)
            .returns(entityGraphClassName)
            .addStatement("return new $T(this)", entityGraphClassName)
            .build();

    typeSpecBuilder =
        TypeSpec.classBuilder(SIMPLE_NAME)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addField(entityGraphTypeField)
            .addField(entityGraphAttributePathsField)
            .addMethod(constructor)
            .addMethod(constructorWithEntityGraphType)
            .addMethod(buildMethod);
  }

  public String simpleName() {
    return SIMPLE_NAME;
  }

  public TypeSpec toTypeSpec() {
    return typeSpecBuilder.build();
  }

  @Override
  public void addPath(Elements elements, MetamodelAttributeTarget target) {
    if (target.isEntity()) {
      addPathToEntity(elements, target);
    } else {
      referencesLeafComposer = true;
      addPathToLeafComposer(target);
    }
  }

  @Override
  public boolean referencesLeafComposer() {
    return referencesLeafComposer;
  }

  private void addPathToEntity(Elements elements, MetamodelAttributeTarget target) {
    ParameterizedTypeName targetNodeComposer =
        ParameterizedTypeName.get(
            new EntityGraphClassName(elements, target.targetType())
                .toClassName()
                .nestedClass(NodeComposer.SIMPLE_NAME),
            entityGraphClassName.nestedClass(SIMPLE_NAME));

    typeSpecBuilder.addMethod(
        MethodSpec.methodBuilder(target.attributeName())
            .addModifiers(Modifier.PUBLIC)
            .returns(targetNodeComposer)
            .addStatement(
                "$T path = new $T()",
                ParameterizedTypeName.get(List.class, String.class),
                ParameterizedTypeName.get(ArrayList.class, String.class))
            .addStatement("path.add($S)", target.attributeName())
            .addStatement("entityGraphAttributePaths.add(path)")
            .addStatement("return new $T(this, path)", targetNodeComposer)
            .build());
  }

  private void addPathToLeafComposer(MetamodelAttributeTarget target) {
    ParameterizedTypeName leafComposer =
        ParameterizedTypeName.get(
            ClassName.get("", LeafComposer.SIMPLE_NAME), ClassName.get("", SIMPLE_NAME));

    typeSpecBuilder.addMethod(
        MethodSpec.methodBuilder(target.attributeName())
            .addModifiers(Modifier.PUBLIC)
            .returns(leafComposer)
            .addStatement(
                "$T path = new $T()",
                ParameterizedTypeName.get(List.class, String.class),
                ParameterizedTypeName.get(ArrayList.class, String.class))
            .addStatement("path.add($S)", target.attributeName())
            .addStatement("entityGraphAttributePaths.add(path)")
            .addStatement("return new $T(this)", leafComposer)
            .build());
  }
}
