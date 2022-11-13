package com.cosium.spring.data.jpa.entity.graph.repository.support;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * Forces the use of {@link RepositoryEntityManagerEntityGraphInjector} while targeting {@link
 * EntityGraphJpaRepositoryFactory}.
 *
 * <p>Created on 18/03/17.
 *
 * @author Reda.Housni-Alaoui
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

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new EntityGraphJpaRepositoryFactory(entityManager);
  }
}
