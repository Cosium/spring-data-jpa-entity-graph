package com.cosium.spring.data.jpa.entity.graph.repository.support;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.query.EntityGraphAwareJpaQueryMethodFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;

/**
 * This repository factory allows to build {@link EntityGraph} aware repositories. Created on
 * 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class EntityGraphJpaRepositoryFactory extends JpaRepositoryFactory {

  /**
   * Creates a new {@link JpaRepositoryFactory}.
   *
   * @param entityManager must not be {@literal null}
   */
  public EntityGraphJpaRepositoryFactory(EntityManager entityManager) {
    super(entityManager);
    addRepositoryProxyPostProcessor(DefaultEntityGraphMethods.INSTANCE);
    addRepositoryProxyPostProcessor(
        EntityGraphQueryHintCandidates.createPostProcessor(entityManager));
    setQueryMethodFactory(
        new EntityGraphAwareJpaQueryMethodFactory(
            PersistenceProvider.fromEntityManager(entityManager)));
  }

  @Override
  protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
    if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
      return EntityGraphQuerydslRepository.class;
    } else {
      return EntityGraphSimpleJpaRepository.class;
    }
  }

  private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
    return QUERY_DSL_PRESENT
        && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
  }
}
