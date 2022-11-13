package com.cosium.spring.data.jpa.entity.graph.domain2;

/**
 * @author Réda Housni Alaoui
 */
public enum EntityGraphType {
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
  LOAD("jakarta.persistence.loadgraph"),
  /**
   * When the jakarta.persistence.fetchgraph property is used to specify an entity graph, attributes
   * that are specified by attribute nodes of the entity graph are treated as FetchType.EAGER and
   * attributes that are not specified are treated as FetchType.LAZY
   *
   * @see <a
   *     href="https://download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf">JPA
   *     2.1 Specification: 3.7.4.1 Fetch Graph Semantics</a>
   */
  FETCH("jakarta.persistence.fetchgraph");

  private final String key;

  EntityGraphType(String value) {
    this.key = value;
  }

  public String key() {
    return key;
  }
}
