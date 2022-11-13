package com.cosium.spring.data.jpa.entity.graph.domain2;

import static java.util.Objects.requireNonNull;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Allows to create on-the-fly generated {@link EntityGraph} by defining its attribute paths.
 *
 * @author Réda Housni Alaoui
 */
public class DynamicEntityGraph implements EntityGraph {

  private final EntityGraphType type;
  private final List<String> attributePaths;

  /**
   * @param attributePaths The paths of attributes of this EntityGraph. You can refer to direct
   *     properties of the entity or nested properties via a {@code property.nestedProperty}. e.g.
   *     for a product entity: {@code ["brand", "supplier.address"]}
   */
  public DynamicEntityGraph(EntityGraphType type, List<String> attributePaths) {
    this.type = requireNonNull(type);
    this.attributePaths =
        Optional.ofNullable(attributePaths)
            .map(ArrayList::new)
            .map(Collections::unmodifiableList)
            .orElse(Collections.emptyList());
  }

  /**
   * When the jakarta.persistence.loadgraph property is used to specify an entity graph, attributes
   * that are specified by attribute nodes of the entity graph are treated as FetchType.EAGER and
   * attributes that are not specified are treated according to their specified or default
   * FetchType.
   *
   * @see <a
   *     href="https://download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf">JPA
   *     2.1 Specification: 3.7.4.2 Load Graph Semantics</a>
   */
  public static Builder loading() {
    return builder(EntityGraphType.LOAD);
  }

  /**
   * When the jakarta.persistence.loadgraph property is used to specify an entity graph, attributes
   * that are specified by attribute nodes of the entity graph are treated as FetchType.EAGER and
   * attributes that are not specified are treated according to their specified or default
   * FetchType.
   *
   * @param attributePaths The paths of attributes of this EntityGraph. You can refer to direct
   *     properties of the entity or nested properties via a {@code property.nestedProperty}. e.g.
   *     for a product entity: {@code ["brand", "supplier.address"]}
   * @see <a
   *     href="https://download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf">JPA
   *     2.1 Specification: 3.7.4.2 Load Graph Semantics</a>
   */
  public static DynamicEntityGraph loading(List<String> attributePaths) {
    return new DynamicEntityGraph(EntityGraphType.LOAD, attributePaths);
  }

  /**
   * When the jakarta.persistence.fetchgraph property is used to specify an entity graph, attributes
   * that are specified by attribute nodes of the entity graph are treated as FetchType.EAGER and
   * attributes that are not specified are treated as FetchType.LAZY
   *
   * @see <a
   *     href="https://download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf">JPA
   *     2.1 Specification: 3.7.4.1 Fetch Graph Semantics</a>
   */
  public static Builder fetching() {
    return builder(EntityGraphType.FETCH);
  }

  /**
   * When the jakarta.persistence.fetchgraph property is used to specify an entity graph, attributes
   * that are specified by attribute nodes of the entity graph are treated as FetchType.EAGER and
   * attributes that are not specified are treated as FetchType.LAZY
   *
   * @param attributePaths The paths of attributes of this EntityGraph. You can refer to direct
   *     properties of the entity or nested properties via a {@code property.nestedProperty}. e.g.
   *     for a product entity: {@code ["brand", "supplier.address"]}
   * @see <a
   *     href="https://download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf">JPA
   *     2.1 Specification: 3.7.4.1 Fetch Graph Semantics</a>
   */
  public static DynamicEntityGraph fetching(List<String> attributePaths) {
    return new DynamicEntityGraph(EntityGraphType.FETCH, attributePaths);
  }

  public static Builder builder(EntityGraphType type) {
    return new Builder(type);
  }

  @Override
  public Optional<EntityGraphQueryHint> buildQueryHint(
      EntityManager entityManager, Class<?> entityType) {
    return Optional.of(
        new EntityGraphQueryHint(
            type, DynamicJpaEntityGraphs.create(entityManager, entityType, attributePaths)));
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
    return type == that.type && attributePaths.equals(that.attributePaths);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, attributePaths);
  }

  public static class Builder {

    private final EntityGraphType type;
    private final List<String> attributePaths = new ArrayList<>();

    private Builder(EntityGraphType type) {
      this.type = requireNonNull(type);
    }

    /**
     * @param pathParts e.g. {@code ["supplier.address"]} or {@code ["supplier", "address"]}
     */
    public Builder addPath(String... pathParts) {
      attributePaths.add(
          Arrays.stream(pathParts)
              .flatMap(pathPart -> Arrays.stream(pathPart.split("\\.")))
              .collect(Collectors.joining(".")));
      return this;
    }

    public DynamicEntityGraph build() {
      return new DynamicEntityGraph(type, attributePaths);
    }
  }
}
