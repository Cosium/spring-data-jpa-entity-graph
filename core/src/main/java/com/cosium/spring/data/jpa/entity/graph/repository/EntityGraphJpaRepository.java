package com.cosium.spring.data.jpa.entity.graph.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Réda Housni Alaoui
 */
@NoRepositoryBean
public interface EntityGraphJpaRepository<T, ID>
    extends EntityGraphListCrudRepository<T, ID>,
        EntityGraphListPagingAndSortingRepository<T, ID>,
        EntityGraphQueryByExampleExecutor<T>,
        JpaRepository<T, ID> {

  /**
   * @see JpaRepository#findAll(Example)
   */
  @Override
  <S extends T> List<S> findAll(Example<S> example, @Nullable EntityGraph entityGraph);

  /**
   * @see JpaRepository#findAll(Example, Sort)
   */
  @Override
  <S extends T> List<S> findAll(Example<S> example, Sort sort, @Nullable EntityGraph entityGraph);
}
