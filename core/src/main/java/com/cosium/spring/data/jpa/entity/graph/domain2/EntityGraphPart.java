package com.cosium.spring.data.jpa.entity.graph.domain2;

import java.util.*;

/** Reusable fragment of an entity graph defined by a set of attribute paths. */
public final class EntityGraphPart {

  private final List<String> attributePaths;

  private EntityGraphPart(List<String> paths) {
    this.attributePaths = List.copyOf(new LinkedHashSet<>(paths));
  }

  public static Builder of(String... paths) {
    return new Builder(List.of(paths));
  }

  public static Builder of(List<String> paths) {
    return new Builder(paths);
  }

  public List<String> attributePaths() {
    return attributePaths;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntityGraphPart that = (EntityGraphPart) o;
    return attributePaths.equals(that.attributePaths);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributePaths);
  }

  @Override
  public String toString() {
    return "EntityGraphPart{attributePaths=%s}".formatted(attributePaths);
  }

  public static class Builder {

    private final List<String> paths = new ArrayList<>();

    private Builder(List<String> initialPaths) {
      this.paths.addAll(initialPaths);
    }

    public Builder include(EntityGraphPart other) {
      paths.addAll(other.attributePaths());
      return this;
    }

    public Builder addPath(String path) {
      paths.add(path);
      return this;
    }

    public Builder addPaths(String... extraPaths) {
      Collections.addAll(paths, extraPaths);
      return this;
    }

    public EntityGraphPart build() {
      return new EntityGraphPart(paths);
    }
  }
}
