package com.cosium.spring.data.jpa.entity.graph.domain2;

import static java.util.Objects.requireNonNull;

import jakarta.persistence.EntityManager;
import java.util.Objects;
import java.util.Optional;

/**
 * Refers to a registered {@link EntityGraph} by its name.
 *
 * @author Réda Housni Alaoui
 */
public class NamedEntityGraph implements EntityGraph {

  private final EntityGraphType type;
  private final String name;

  public NamedEntityGraph(String name) {
    this(EntityGraphType.LOAD, name);
  }

  public NamedEntityGraph(EntityGraphType type, String name) {
    this.type = requireNonNull(type);
    this.name = requireNonNull(name);
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
  public static NamedEntityGraph loading(String name) {
    return new NamedEntityGraph(EntityGraphType.LOAD, name);
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
  public static NamedEntityGraph fetching(String name) {
    return new NamedEntityGraph(EntityGraphType.FETCH, name);
  }

  @Override
  public Optional<EntityGraphQueryHint> buildQueryHint(
      EntityManager entityManager, Class<?> entityType) {
    return Optional.of(new EntityGraphQueryHint(type, entityManager.getEntityGraph(name)));
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
    return type == that.type && name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, name);
  }
}
