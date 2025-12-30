package com.cosium.spring.data.jpa.graph.generator;

import static java.util.Objects.*;

import com.google.auto.service.AutoService;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import org.jspecify.annotations.Nullable;

/**
 * @author Réda Housni Alaoui
 */
@AutoService(Processor.class)
public class Generator extends AbstractProcessor {

  private static final Boolean ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS = Boolean.FALSE;

  private final Set<EntityGraph> writtenEntityGraphs = new HashSet<>();

  @Nullable private Types types;
  @Nullable private Elements elements;
  @Nullable private Filer filer;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.types = processingEnv.getTypeUtils();
    this.elements = processingEnv.getElementUtils();
    this.filer = processingEnv.getFiler();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver() || annotations.isEmpty()) {
      return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
    }

    roundEnv.getElementsAnnotatedWith(StaticMetamodel.class).stream()
        .map(TypeElement.class::cast)
        .map(
            typeElement ->
                new MetamodelClass(requireNonNull(elements), requireNonNull(types), typeElement))
        .map(MetamodelClass::createEntityGraph)
        .forEach(
            entityGraph -> {
              if (!writtenEntityGraphs.add(entityGraph)) {
                return;
              }
              entityGraph.writeTo(requireNonNull(filer));
            });

    return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(StaticMetamodel.class.getCanonicalName());
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }
}
