package com.cosium.spring.data.jpa.graph.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/** @author Réda Housni Alaoui */
public class EntityGraph {

  private final ClassName className;
  private final TypeSpec typeSpec;

  public EntityGraph(
      ClassName entityGraphClassName, RootComposer rootComposer, NodeComposer nodeComposer) {
    this.className = entityGraphClassName;

    FieldSpec typeField =
        FieldSpec.builder(Constants.ENTITY_GRAPH_TYPE_CLASS_NAME, "type")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            .build();

    FieldSpec attributePathsField =
        FieldSpec.builder(ParameterizedTypeName.get(List.class, String.class), "attributePaths")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            .build();

    ClassName rootComposerClassName = entityGraphClassName.nestedClass(rootComposer.simpleName());

    MethodSpec constructor =
        MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(rootComposerClassName, "rootComposer")
            .addStatement("this.type = rootComposer.entityGraphType")
            .addStatement(
                "this.attributePaths = rootComposer.entityGraphAttributePaths.stream()"
                    + ".map(pathParts -> String.join(\".\", pathParts)).collect($T.toList())",
                Collectors.class)
            .build();

    MethodSpec rootStaticMethod =
        MethodSpec.methodBuilder(Constants.PATH_SEPARATOR)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(rootComposerClassName)
            .addStatement("return new $N()", rootComposer.simpleName())
            .build();

    MethodSpec rootStaticMethodWithEntityGraphType =
        MethodSpec.methodBuilder(Constants.PATH_SEPARATOR)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(Constants.ENTITY_GRAPH_TYPE_CLASS_NAME, "entityGraphType")
            .returns(rootComposerClassName)
            .addStatement("return new $N(entityGraphType)", rootComposer.simpleName())
            .build();

    MethodSpec getEntityGraphTypeMethod =
        MethodSpec.methodBuilder("getEntityGraphType")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(Constants.ENTITY_GRAPH_TYPE_CLASS_NAME)
            .addStatement("return type")
            .build();

    MethodSpec getEntityGraphAttributePathsMethod =
        MethodSpec.methodBuilder("getEntityGraphAttributePaths")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(ParameterizedTypeName.get(List.class, String.class))
            .addStatement("return attributePaths")
            .build();

    MethodSpec getEntityGraphNameMethod =
        MethodSpec.methodBuilder("getEntityGraphName")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(String.class)
            .addStatement("return null")
            .build();

    MethodSpec isOptionalMethod =
        MethodSpec.methodBuilder("isOptional")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(boolean.class)
            .addStatement("return false")
            .build();

    typeSpec =
        TypeSpec.classBuilder(entityGraphClassName)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(Constants.ENTITY_GRAPH_CLASS_NAME)
            .addField(typeField)
            .addField(attributePathsField)
            .addMethod(constructor)
            .addMethod(rootStaticMethod)
            .addMethod(rootStaticMethodWithEntityGraphType)
            .addMethod(getEntityGraphTypeMethod)
            .addMethod(getEntityGraphAttributePathsMethod)
            .addMethod(getEntityGraphNameMethod)
            .addMethod(isOptionalMethod)
            .addType(rootComposer.toTypeSpec())
            .addType(nodeComposer.toTypeSpec())
            .build();
  }

  public void writeTo(Filer filer) throws IOException {
    JavaFile.builder(className.packageName(), typeSpec).build().writeTo(filer);
  }
}
