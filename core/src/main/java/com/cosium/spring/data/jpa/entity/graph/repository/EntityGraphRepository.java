package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * @author Réda Housni Alaoui
 */
@NoRepositoryBean
public interface EntityGraphRepository<T, ID> extends Repository<T, ID> {

  /**
   * @return The {@link EntityGraph} to use when none is provided or when an equivalent to {@link
   *     EntityGraph#NOOP} is provided. Returning empty means this repository has no default {@link
   *     EntityGraph}.
   */
  default Optional<EntityGraph> defaultEntityGraph() {
    return Optional.empty();
  }
}
