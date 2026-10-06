package com.cosium.spring.data.jpa.entity.graph.repository.support;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * Forces the use of {@link RepositoryEntityManagerEntityGraphInjector} while targeting {@link
 * EntityGraphJpaRepositoryFactory}.
 *
 * @author Réda Housni Alaoui
 */
public class EntityGraphJpaRepositoryFactoryBean<R extends Repository<T, I>, T, I>
    extends JpaRepositoryFactoryBean<R, T, I> {

  /**
   * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
   *
   * @param repositoryInterface must not be {@literal null}.
   */
  public EntityGraphJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  public void setEntityManager(EntityManager entityManager) {
    /* Make sure to use the EntityManager able to inject captured EntityGraphs */
    super.setEntityManager(RepositoryEntityManagerEntityGraphInjector.proxy(entityManager));
  }

  /**
   * Force EntityGraphSimpleJpaRepository as repository base class to ensure EntityGraph-aware
   * methods are available. Spring Boot 4 auto-config injects SimpleJpaRepository.class via property
   * binding.
   *
   * @param repositoryBaseClass the repositoryBaseClass to set, can be {@literal null}.
   */
  @Override
  public void setRepositoryBaseClass(Class<?> repositoryBaseClass) {
    super.setRepositoryBaseClass(EntityGraphSimpleJpaRepository.class);
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new EntityGraphJpaRepositoryFactory(entityManager);
  }
}
