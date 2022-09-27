package com.cosium.spring.data.jpa.graph.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import java.io.IOException;
import java.util.Objects;
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

    FieldSpec noopField =
        FieldSpec.builder(entityGraphClassName, "NOOP")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("new $T(EntityGraph.NOOP)", entityGraphClassName)
            .build();

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
                "this(new $T(rootComposer.entityGraphType, rootComposer.entityGraphAttributePaths.stream()"
                    + ".map(pathParts -> String.join(\".\", pathParts)).collect($T.toList())))",
                Constants.DYNAMIC_ENTITY_GRAPH_CLASS_NAME,
                Collectors.class)
            .build();

    MethodSpec emptyConstructor =
        MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(Constants.ENTITY_GRAPH_CLASS_NAME, "delegate")
            .addStatement("this.delegate = delegate")
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
            .addField(noopField)
            .addField(delegateField)
            .addMethod(emptyConstructor)
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

  public void writeTo(Filer filer) {
    try {
      JavaFile.builder(className.packageName(), typeSpec).build().writeTo(filer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntityGraph that = (EntityGraph) o;
    return className.equals(that.className);
  }

  @Override
  public int hashCode() {
    return Objects.hash(className);
  }
}
