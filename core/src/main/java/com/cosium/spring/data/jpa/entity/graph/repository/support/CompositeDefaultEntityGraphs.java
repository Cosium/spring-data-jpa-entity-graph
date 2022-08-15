package com.cosium.spring.data.jpa.entity.graph.repository.support;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Réda Housni Alaoui
 */
class CompositeDefaultEntityGraphs implements DefaultEntityGraphs {

  private final List<DefaultEntityGraphs> list;

  public CompositeDefaultEntityGraphs(DefaultEntityGraphs... defaultEntityGraphs) {
    list = Stream.of(defaultEntityGraphs).collect(Collectors.toList());
  }

  @Override
  public Optional<DefaultEntityGraph> findOne(Object repository) {
    return list.stream()
        .map(entityGraphs -> entityGraphs.findOne(repository))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
  }
}
