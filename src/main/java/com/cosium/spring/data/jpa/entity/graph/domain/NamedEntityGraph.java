package com.cosium.spring.data.jpa.entity.graph.domain;

import com.google.common.base.MoreObjects;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class NamedEntityGraph extends AbstractEntityGraph {

  private final String name;

  public NamedEntityGraph(EntityGraphType type, boolean optional, String name) {
    super(type, optional);
    Assert.hasLength(name);
    this.name = name;
  }

  public NamedEntityGraph(EntityGraphType type, String name) {
    this(type, false, name);
  }

  public NamedEntityGraph(String name) {
    this(DEFAULT_ENTITY_GRAPH_TYPE, name);
  }

  @Override
  public String getEntityGraphName() {
    return name;
  }

  @Override
  public final List<String> getEntityGraphAttributePaths() {
    return null;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", name).toString();
  }
}
