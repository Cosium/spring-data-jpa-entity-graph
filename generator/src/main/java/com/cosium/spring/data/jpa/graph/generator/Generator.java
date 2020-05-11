package com.cosium.spring.data.jpa.graph.generator;

import com.cosium.logging.annotation_processor.AbstractLoggingProcessor;
import com.google.auto.service.AutoService;
import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.persistence.metamodel.StaticMetamodel;

/** @author Réda Housni Alaoui */
@AutoService(Processor.class)
public class Generator extends AbstractLoggingProcessor {

  private static final Boolean ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS = Boolean.FALSE;

  private Types types;
  private Elements elements;
  private Filer filer;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.types = processingEnv.getTypeUtils();
    this.elements = processingEnv.getElementUtils();
    this.filer = processingEnv.getFiler();
  }

  @Override
  protected boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver() || annotations.isEmpty()) {
      return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
    }

    roundEnv
        .getElementsAnnotatedWith(StaticMetamodel.class)
        .stream()
        .map(TypeElement.class::cast)
        .map(typeElement -> new MetamodelClass(elements, types, typeElement))
        .forEach(metamodelClass -> metamodelClass.writeEntityGraphTo(filer));

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
