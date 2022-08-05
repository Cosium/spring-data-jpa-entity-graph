package com.cosium.spring.data.jpa.entity.graph.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 * @deprecated Use {@link com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph}
 *     instead.
 */
@Deprecated
public class DynamicEntityGraph extends AbstractEntityGraph {

  private final List<String> attributePaths;

  public DynamicEntityGraph(List<String> attributePaths) {
    this.attributePaths = Collections.unmodifiableList(attributePaths);
  }

  public DynamicEntityGraph(EntityGraphType type, List<String> attributePaths) {
    super(type);
    this.attributePaths = Collections.unmodifiableList(attributePaths);
  }

  @Override
  public List<String> getEntityGraphAttributePaths() {
    return attributePaths;
  }

  @Override
  public final String getEntityGraphName() {
    return null;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DynamicEntityGraph.class.getSimpleName() + "[", "]")
        .add("attributePaths=" + attributePaths)
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
    DynamicEntityGraph that = (DynamicEntityGraph) o;
    return attributePaths.equals(that.attributePaths)
        && getEntityGraphType() == that.getEntityGraphType()
        && isOptional() == that.isOptional();
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributePaths, getEntityGraphType(), isOptional());
  }
}
