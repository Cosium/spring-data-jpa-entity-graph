package com.cosium.spring.data.jpa.graph.generator;

import javax.lang.model.util.Elements;

/**
 * @author Réda Housni Alaoui
 */
public interface Composer {

  void addPath(Elements elements, MetamodelAttributeTarget target);

  boolean referencesLeafComposer();
}
