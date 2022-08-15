package com.cosium.spring.data.jpa.entity.graph.repository.support;

import java.util.Optional;

/**
 * @author Réda Housni Alaoui
 */
interface DefaultEntityGraphs {

  Optional<DefaultEntityGraph> findOne(Object repository);
}
