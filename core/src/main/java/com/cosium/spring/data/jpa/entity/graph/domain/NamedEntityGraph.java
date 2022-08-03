package com.cosium.spring.data.jpa.entity.graph.domain;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import org.springframework.util.Assert;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class NamedEntityGraph extends AbstractEntityGraph {

  private final String name;

  public NamedEntityGraph(EntityGraphType type, boolean optional, String name) {
    super(type, optional);
    Assert.hasLength(name, "The name must not be empty");
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
    return new StringJoiner(", ", NamedEntityGraph.class.getSimpleName() + "[", "]")
        .add("name='" + name + "'")
        .toString();
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
