package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.InapplicableEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.MultipleDefaultEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.MultipleEntityGraphException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.EntityManager;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.ResolvableType;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

/**
 * Captures {@link EntityGraph} on repositories method calls. Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class RepositoryMethodEntityGraphExtractor implements RepositoryProxyPostProcessor {

  private static final Logger LOG =
      LoggerFactory.getLogger(RepositoryMethodEntityGraphExtractor.class);

  private static final ThreadLocal<JpaEntityGraphMethodInterceptor> CURRENT_REPOSITORY =
      new NamedThreadLocal<>("Thread local holding the current repository");

  private final EntityManager entityManager;

  public RepositoryMethodEntityGraphExtractor(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public static EntityGraphQueryHintCandidate getCurrentJpaEntityGraph() {
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

    private static final Logger DEFAULT_ENTITY_GRAPH_LOGGER =
        LoggerFactory.getLogger(
            JpaEntityGraphMethodInterceptor.class.getCanonicalName() + ".DefaultEntityGraph");

    private static final String DEFAULT_ENTITY_GRAPH_NAME_SUFFIX = ".default";
    private static final AtomicInteger DEFAULT_ENTITY_GRAPH_DEPRECATION_LOG_COUNT =
        new AtomicInteger();
    private static final int MAX_DEFAULT_ENTITY_GRAPH_DEPRECATION_LOG_COUNT = 10;

    private final Class<?> domainClass;
    private final EntityManager entityManager;
    private final DefaultEntityGraph defaultEntityGraph;
    private final ThreadLocal<EntityGraphQueryHintCandidate> currentEntityGraph =
        new NamedThreadLocal<>(
            "Thread local holding the current spring data jpa repository entity graph");

    JpaEntityGraphMethodInterceptor(EntityManager entityManager, Class<?> domainClass) {
      this.domainClass = domainClass;
      this.entityManager = entityManager;
      String defaultEntityGraphName =
          findDefaultEntityGraphName(entityManager, domainClass).orElse(null);
      if (defaultEntityGraphName != null
          && DEFAULT_ENTITY_GRAPH_DEPRECATION_LOG_COUNT.get()
              < MAX_DEFAULT_ENTITY_GRAPH_DEPRECATION_LOG_COUNT) {
        DEFAULT_ENTITY_GRAPH_DEPRECATION_LOG_COUNT.incrementAndGet();
        DEFAULT_ENTITY_GRAPH_LOGGER.warn(
            "Found 'Default EntityGraph' {} for {}. "
                + "'Default EntityGraph' feature is deprecated. "
                + "It will be removed in a future version. "
                + "Read https://github.com/Cosium/spring-data-jpa-entity-graph/issues/73#issue-1330079585 for more information.",
            defaultEntityGraphName,
            domainClass);
      }
      this.defaultEntityGraph =
          Optional.ofNullable(defaultEntityGraphName)
              .map(NamedEntityGraph::loading)
              .map(DefaultEntityGraph::new)
              .orElse(null);
    }

    /** @return The default entity graph if it exists. Null otherwise. */
    private static <T> Optional<String> findDefaultEntityGraphName(
        EntityManager entityManager, Class<T> domainClass) {
      String defaultEntityGraphName = null;
      List<javax.persistence.EntityGraph<? super T>> entityGraphs =
          entityManager.getEntityGraphs(domainClass);
      for (javax.persistence.EntityGraph<? super T> entityGraph : entityGraphs) {
        if (!entityGraph.getName().endsWith(DEFAULT_ENTITY_GRAPH_NAME_SUFFIX)) {
          continue;
        }
        if (defaultEntityGraphName != null) {
          throw new MultipleDefaultEntityGraphException(
              entityGraph.getName(), defaultEntityGraphName);
        }
        defaultEntityGraphName = entityGraph.getName();
      }
      return Optional.ofNullable(defaultEntityGraphName);
    }

    public EntityGraphQueryHintCandidate getCurrentJpaEntityGraph() {
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
        Object invocationQualifier = invocation.getThis();
        Objects.requireNonNull(
            invocationQualifier, "No qualifier found for invocation " + invocationQualifier);
        implementationClass = invocationQualifier.getClass();
      }

      ResolvableType returnType =
          ResolvableType.forMethodReturnType(invocation.getMethod(), implementationClass);

      EntityGraphQueryHintCandidate candidate = buildEntityGraphCandidate(providedEntityGraph);

      if (candidate != null && !canApplyEntityGraph(returnType)) {
        if (!candidate.queryHint().failIfInapplicable()) {
          LOG.trace("Cannot apply EntityGraph {}", candidate);
          candidate = null;
        } else {
          throw new InapplicableEntityGraphException(
              "Cannot apply EntityGraph " + candidate + " to the the current query");
        }
      }

      EntityGraphQueryHintCandidate genuineCandidate = currentEntityGraph.get();
      boolean newEntityGraphCandidatePreValidated =
          candidate != null && (genuineCandidate == null || !genuineCandidate.primary());
      if (newEntityGraphCandidatePreValidated) {
        currentEntityGraph.set(candidate);
      }
      try {
        return invocation.proceed();
      } finally {
        if (newEntityGraphCandidatePreValidated) {
          currentEntityGraph.set(genuineCandidate);
        }
      }
    }

    private EntityGraphQueryHintCandidate buildEntityGraphCandidate(
        EntityGraph providedEntityGraph) {

      EntityGraphQueryHint queryHint =
          Optional.ofNullable(providedEntityGraph)
              .flatMap(entityGraph -> entityGraph.buildQueryHint(entityManager, domainClass))
              .orElse(null);

      boolean isPrimary = true;
      if (queryHint == null) {
        queryHint =
            Optional.ofNullable(defaultEntityGraph)
                .flatMap(entityGraph -> entityGraph.buildQueryHint(entityManager, domainClass))
                .orElse(null);
        isPrimary = false;
      }
      if (queryHint == null) {
        return null;
      }
      return new EntityGraphQueryHintCandidate(queryHint, domainClass, isPrimary);
    }

    private boolean canApplyEntityGraph(ResolvableType repositoryMethodReturnType) {
      Class<?> resolvedReturnType = repositoryMethodReturnType.resolve();
      if (resolvedReturnType != null
          && (Void.TYPE.equals(resolvedReturnType)
              || domainClass.isAssignableFrom(resolvedReturnType))) {
        return true;
      }
      for (Class<?> genericType : repositoryMethodReturnType.resolveGenerics()) {
        if (domainClass.isAssignableFrom(genericType)) {
          return true;
        }
      }
      return false;
    }
  }
}
