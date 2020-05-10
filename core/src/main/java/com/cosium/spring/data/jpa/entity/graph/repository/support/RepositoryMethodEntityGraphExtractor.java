package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphType;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphs;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.InapplicableEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.MultipleDefaultEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.MultipleEntityGraphException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.query.JpaEntityGraph;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Captures {@link EntityGraph} on repositories method calls. Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class RepositoryMethodEntityGraphExtractor implements RepositoryProxyPostProcessor {

  private static final Logger LOG =
      LoggerFactory.getLogger(RepositoryMethodEntityGraphExtractor.class);

  private static final ThreadLocal<JpaEntityGraphMethodInterceptor> CURRENT_REPOSITORY =
      new NamedThreadLocal<JpaEntityGraphMethodInterceptor>(
          "Thread local holding the current repository");

  private final EntityManager entityManager;

  RepositoryMethodEntityGraphExtractor(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  static EntityGraphBean getCurrentJpaEntityGraph() {
    JpaEntityGraphMethodInterceptor currentRepository = CURRENT_REPOSITORY.get();
    if (currentRepository == null) {
      return null;
    }
    return currentRepository.getCurrentJpaEntityGraph();
  }

  @Override
  public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
    factory.addAdvice(
        new JpaEntityGraphMethodInterceptor(entityManager, repositoryInformation.getDomainType()));
  }

  private static class JpaEntityGraphMethodInterceptor implements MethodInterceptor {

    private static final String DEFAULT_ENTITYGRAPH_NAME_SUFFIX = ".default";
    private final Class<?> domainClass;
    private final EntityGraph defaultEntityGraph;
    private final ThreadLocal<EntityGraphBean> currentEntityGraph =
        new NamedThreadLocal<EntityGraphBean>(
            "Thread local holding the current spring data jpa repository entity graph");

    JpaEntityGraphMethodInterceptor(EntityManager entityManager, Class domainClass) {
      this.domainClass = domainClass;
      this.defaultEntityGraph = findDefaultEntityGraph(entityManager);
    }

    /**
     * @param entityManager
     * @return The default entity graph if it exists. Null otherwise.
     */
    private EntityGraph findDefaultEntityGraph(EntityManager entityManager) {
      EntityGraph defaultEntityGraph = null;
      List<javax.persistence.EntityGraph<?>> entityGraphs =
          (List<javax.persistence.EntityGraph<?>>) entityManager.getEntityGraphs(domainClass);
      for (javax.persistence.EntityGraph entityGraph : entityGraphs) {
        if (entityGraph.getName().endsWith(DEFAULT_ENTITYGRAPH_NAME_SUFFIX)) {
          if (defaultEntityGraph != null) {
            throw new MultipleDefaultEntityGraphException(
                entityGraph.getName(), defaultEntityGraph.getEntityGraphName());
          }
          defaultEntityGraph = EntityGraphUtils.fromName(entityGraph.getName(), true);
        }
      }
      return defaultEntityGraph;
    }

    EntityGraphBean getCurrentJpaEntityGraph() {
      return currentEntityGraph.get();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      JpaEntityGraphMethodInterceptor oldRepo = CURRENT_REPOSITORY.get();
      CURRENT_REPOSITORY.set(this);
      try {
        return doInvoke(invocation);
      } finally {
        CURRENT_REPOSITORY.set(oldRepo);
      }
    }

    private Object doInvoke(MethodInvocation invocation) throws Throwable {
      Object[] arguments = invocation.getArguments();
      EntityGraph providedEntityGraph = null;
      for (Object argument : arguments) {
        if (!(argument instanceof EntityGraph)) {
          continue;
        }
        EntityGraph newEntityGraph = (EntityGraph) argument;
        if (providedEntityGraph != null) {
          throw new MultipleEntityGraphException(
              "Duplicate EntityGraphs detected. '"
                  + providedEntityGraph
                  + "' and '"
                  + newEntityGraph
                  + "' were passed to method "
                  + invocation.getMethod());
        }
        providedEntityGraph = newEntityGraph;
      }

      Class<?> implementationClass;
      if (invocation instanceof ReflectiveMethodInvocation) {
        implementationClass = ((ReflectiveMethodInvocation) invocation).getProxy().getClass();
      } else {
        implementationClass = invocation.getThis().getClass();
      }

      EntityGraphBean entityGraphCandidate =
          buildEntityGraphCandidate(
              providedEntityGraph,
              ResolvableType.forMethodReturnType(invocation.getMethod(), implementationClass));

      if (entityGraphCandidate != null && !entityGraphCandidate.isValid()) {
        if (entityGraphCandidate.isOptional()) {
          LOG.trace("Cannot apply EntityGraph {}", entityGraphCandidate);
          entityGraphCandidate = null;
        } else {
          throw new InapplicableEntityGraphException(
              "Cannot apply EntityGraph " + entityGraphCandidate + " to the the current query");
        }
      }

      EntityGraphBean oldEntityGraphCandidate = currentEntityGraph.get();
      boolean newEntityGraphCandidatePreValidated =
          entityGraphCandidate != null
              && (oldEntityGraphCandidate == null || !oldEntityGraphCandidate.isPrimary());
      if (newEntityGraphCandidatePreValidated) {
        currentEntityGraph.set(entityGraphCandidate);
      }
      try {
        return invocation.proceed();
      } finally {
        if (newEntityGraphCandidatePreValidated) {
          currentEntityGraph.set(oldEntityGraphCandidate);
        }
      }
    }

    private EntityGraphBean buildEntityGraphCandidate(
        EntityGraph providedEntityGraph, ResolvableType returnType) {
      boolean isPrimary = true;
      if (EntityGraphs.isEmpty(providedEntityGraph)) {
        providedEntityGraph = defaultEntityGraph;
        isPrimary = false;
      }
      if (providedEntityGraph == null) {
        return null;
      }

      EntityGraphType entityGraphType = requireNonNull(providedEntityGraph.getEntityGraphType());

      org.springframework.data.jpa.repository.EntityGraph.EntityGraphType type;
      switch (entityGraphType) {
        case FETCH:
          type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;
          break;
        case LOAD:
          type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;
          break;
        default:
          throw new RuntimeException("Unexpected entity graph type '" + entityGraphType + "'");
      }

      List<String> attributePaths = providedEntityGraph.getEntityGraphAttributePaths();
      JpaEntityGraph jpaEntityGraph =
          new JpaEntityGraph(
              StringUtils.hasText(providedEntityGraph.getEntityGraphName())
                  ? providedEntityGraph.getEntityGraphName()
                  : domainClass.getName() + "-_-_-_-_-_-",
              type,
              attributePaths != null
                  ? attributePaths.toArray(new String[attributePaths.size()])
                  : null);

      return new EntityGraphBean(
          jpaEntityGraph, domainClass, returnType, providedEntityGraph.isOptional(), isPrimary);
    }
  }
}
