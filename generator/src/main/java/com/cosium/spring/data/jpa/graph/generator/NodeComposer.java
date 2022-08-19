package com.cosium.spring.data.jpa.graph.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

/**
 * @author Réda Housni Alaoui
 */
public class NodeComposer implements Composer {

  public static final String SIMPLE_NAME = "NodeComposer";

  private final TypeVariableName rootType;
  private final TypeSpec.Builder typeSpecBuilder;
  private boolean referencesLeafComposer;

  public NodeComposer() {
    rootType = TypeVariableName.get("R");

    FieldSpec rootField =
        FieldSpec.builder(rootType, Constants.PATH_SEPARATOR)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .build();

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
            .addStatement("this.$N = root", Constants.PATH_SEPARATOR)
            .addStatement("this.path = path")
            .build();

    typeSpecBuilder =
        TypeSpec.classBuilder(SIMPLE_NAME)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addTypeVariable(rootType)
            .addField(rootField)
            .addField(pathField)
            .addMethod(constructor);
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
                .nestedClass(SIMPLE_NAME),
            rootType);

    typeSpecBuilder.addMethod(
        MethodSpec.methodBuilder(target.attributeName())
            .addModifiers(Modifier.PUBLIC)
            .returns(targetNodeComposer)
            .addStatement("path.add($S)", target.attributeName())
            .addStatement("return new $T($N, path)", targetNodeComposer, Constants.PATH_SEPARATOR)
            .build());
  }

  private void addPathToLeafComposer(MetamodelAttributeTarget target) {
    ParameterizedTypeName leafComposer =
        ParameterizedTypeName.get(ClassName.get("", LeafComposer.SIMPLE_NAME), rootType);

    typeSpecBuilder.addMethod(
        MethodSpec.methodBuilder(target.attributeName())
            .addModifiers(Modifier.PUBLIC)
            .returns(leafComposer)
            .addStatement("path.add($S)", target.attributeName())
            .addStatement("return new $T($N)", leafComposer, Constants.PATH_SEPARATOR)
            .build());
  }
}
