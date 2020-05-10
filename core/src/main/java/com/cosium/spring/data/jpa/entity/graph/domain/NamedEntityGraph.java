package com.cosium.spring.data.jpa.entity.graph.domain;

import com.google.common.base.MoreObjects;
import java.util.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NamedEntityGraph that = (NamedEntityGraph) o;
    return name.equals(that.name)
        && getEntityGraphType() == that.getEntityGraphType()
        && isOptional() == that.isOptional();
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, getEntityGraphType(), isOptional());
  }
}
