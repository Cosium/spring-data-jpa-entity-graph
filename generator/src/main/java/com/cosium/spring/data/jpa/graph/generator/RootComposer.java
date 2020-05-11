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

/** @author Réda Housni Alaoui */
public class RootComposer implements Composer {

  private static final String SIMPLE_NAME = "RootComposer";
  private final ClassName entityGraphClassName;
  private final TypeSpec.Builder typeSpecBuilder;

  public RootComposer(ClassName entityGraphClassName) {
    this.entityGraphClassName = entityGraphClassName;

    FieldSpec entityGraphTypeField =
        FieldSpec.builder(Api.ENTITY_GRAPH_TYPE_CLASS_NAME, "entityGraphType")
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
            .addStatement("this($T.$N)", Api.ENTITY_GRAPH_TYPE_CLASS_NAME, "LOAD")
            .build();

    MethodSpec constructorWithEntityGraphType =
        MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(Api.ENTITY_GRAPH_TYPE_CLASS_NAME, "entityGraphType")
            .addStatement("this.entityGraphType = entityGraphType")
            .addStatement(
                "entityGraphAttributePaths = new $T()",
                ParameterizedTypeName.get(ClassName.get(ArrayList.class), listOfString))
            .build();

    MethodSpec rootMethod =
        MethodSpec.methodBuilder("root")
            .addModifiers(Modifier.PUBLIC)
            .returns(entityGraphClassName.nestedClass(SIMPLE_NAME))
            .addStatement("return this")
            .build();

    MethodSpec buildMethod =
        MethodSpec.methodBuilder("build")
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
            .addMethod(rootMethod)
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
            .addStatement("return new $T(root(), path)", targetNodeComposer)
            .build());
  }
}
