package com.cosium.spring.data.jpa.graph.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

/** @author Réda Housni Alaoui */
public class NodeComposer implements Composer {

  public static final String SIMPLE_NAME = "NodeComposer";

  private final TypeVariableName rootType;
  private final TypeSpec.Builder typeSpecBuilder;

  public NodeComposer() {
    rootType = TypeVariableName.get("R");

    FieldSpec rootField =
        FieldSpec.builder(rootType, "root").addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();

    ParameterizedTypeName listOfString = ParameterizedTypeName.get(List.class, String.class);

    FieldSpec pathField =
        FieldSpec.builder(listOfString, "path")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            .build();

    MethodSpec constructor =
        MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(rootType, "root")
            .addParameter(listOfString, "path")
            .addStatement("this.root = root")
            .addStatement("this.path = path")
            .build();

    MethodSpec rootMethod =
        MethodSpec.methodBuilder("root")
            .addModifiers(Modifier.PUBLIC)
            .returns(rootType)
            .addStatement("return root")
            .build();

    typeSpecBuilder =
        TypeSpec.classBuilder(SIMPLE_NAME)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addTypeVariable(rootType)
            .addField(rootField)
            .addField(pathField)
            .addMethod(constructor)
            .addMethod(rootMethod);
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
                .nestedClass(SIMPLE_NAME),
            rootType);

    typeSpecBuilder.addMethod(
        MethodSpec.methodBuilder(target.attributeName())
            .addModifiers(Modifier.PUBLIC)
            .returns(targetNodeComposer)
            .addStatement("path.add($S)", target.attributeName())
            .addStatement("return new $T(root(), path)", targetNodeComposer)
            .build());
  }
}
