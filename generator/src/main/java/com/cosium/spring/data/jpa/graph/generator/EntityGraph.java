package com.cosium.spring.data.jpa.graph.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * @author Réda Housni Alaoui
 */
public class EntityGraph {

  private final ClassName className;
  private final TypeSpec typeSpec;

  public EntityGraph(
      ClassName entityGraphClassName, RootComposer rootComposer, NodeComposer nodeComposer) {
    this.className = entityGraphClassName;

    FieldSpec delegateField =
        FieldSpec.builder(Constants.ENTITY_GRAPH_CLASS_NAME, "delegate")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            .build();

    ClassName rootComposerClassName = entityGraphClassName.nestedClass(rootComposer.simpleName());

    MethodSpec constructor =
        MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(rootComposerClassName, "rootComposer")
            .addStatement(
                "$T type = rootComposer.entityGraphType", Constants.ENTITY_GRAPH_TYPE_CLASS_NAME)
            .addStatement(
                "$T attributePaths = rootComposer.entityGraphAttributePaths.stream()"
                    + ".map(pathParts -> String.join(\".\", pathParts)).collect($T.toList())",
                ParameterizedTypeName.get(List.class, String.class),
                Collectors.class)
            .addStatement(
                "this.delegate = new $T(type, attributePaths)",
                Constants.DYNAMIC_ENTITY_GRAPH_CLASS_NAME)
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
        MethodSpec.methodBuilder("buildQueryHint")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(Constants.ENTITY_MANAGER_CLASS_NAME, "entityManager")
            .addParameter(
                ParameterizedTypeName.get(
                    ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class)),
                "entityType")
            .returns(
                ParameterizedTypeName.get(
                    ClassName.get(Optional.class), Constants.ENTITY_GRAPH_QUERY_HINT_CLASS_NAME))
            .addStatement("return delegate.buildQueryHint(entityManager, entityType)")
            .build();

    TypeSpec.Builder typeSpecBuilder =
        TypeSpec.classBuilder(entityGraphClassName)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(Constants.ENTITY_GRAPH_CLASS_NAME)
            .addField(delegateField)
            .addMethod(constructor)
            .addMethod(rootStaticMethod)
            .addMethod(rootStaticMethodWithEntityGraphType)
            .addMethod(getEntityGraphTypeMethod)
            .addType(rootComposer.toTypeSpec())
            .addType(nodeComposer.toTypeSpec());

    if (rootComposer.referencesLeafComposer() || nodeComposer.referencesLeafComposer()) {
      typeSpecBuilder.addType(new LeafComposer().toTypeSpec());
    }

    typeSpec = typeSpecBuilder.build();
  }

  public void writeTo(Filer filer) throws IOException {
    JavaFile.builder(className.packageName(), typeSpec).build().writeTo(filer);
  }
}
