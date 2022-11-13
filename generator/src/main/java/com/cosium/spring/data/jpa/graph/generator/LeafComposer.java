package com.cosium.spring.data.jpa.graph.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import javax.lang.model.element.Modifier;

/**
 * @author Réda Housni Alaoui
 */
public class LeafComposer {

  public static final String SIMPLE_NAME = "LeafComposer";

  private final TypeSpec.Builder typeSpecBuilder;

  public LeafComposer() {
    TypeVariableName rootType = TypeVariableName.get("R");

    FieldSpec rootField =
        FieldSpec.builder(rootType, Constants.PATH_SEPARATOR)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .build();

    MethodSpec constructor =
        MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(rootType, "root")
            .addStatement("this.$N = root", Constants.PATH_SEPARATOR)
            .build();

    typeSpecBuilder =
        TypeSpec.classBuilder(SIMPLE_NAME)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addTypeVariable(rootType)
            .addField(rootField)
            .addMethod(constructor);
  }

  public TypeSpec toTypeSpec() {
    return typeSpecBuilder.build();
  }
}
