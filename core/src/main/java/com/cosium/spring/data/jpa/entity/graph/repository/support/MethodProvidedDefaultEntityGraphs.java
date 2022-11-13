package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphRepository;
import java.util.Optional;

/**
 * @author Réda Housni Alaoui
 */
class MethodProvidedDefaultEntityGraphs implements DefaultEntityGraphs {

  @Override
  public Optional<DefaultEntityGraph> findOne(Object repository) {

    if (!(repository instanceof EntityGraphRepository<?, ?> entityGraphRepository)) {
      return Optional.empty();
    }

    return entityGraphRepository.defaultEntityGraph().map(DefaultEntityGraph::new);
  }
}
