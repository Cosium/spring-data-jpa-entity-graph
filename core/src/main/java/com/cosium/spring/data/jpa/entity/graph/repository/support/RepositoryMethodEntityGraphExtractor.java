package com.cosium.spring.data.jpa.entity.graph.repository.support;

import static java.util.Objects.requireNonNull;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.InapplicableEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.MultipleEntityGraphException;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
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
    factory.addAdvice(new JpaEntityGraphMethodInterceptor(entityManager, repositoryInformation));
  }

  private static class JpaEntityGraphMethodInterceptor implements MethodInterceptor {

    private final Class<?> domainClass;
    private final EntityManager entityManager;
    private final DefaultEntityGraphs defaultEntityGraphs;
    private final ThreadLocal<EntityGraphQueryHintCandidate> currentEntityGraph =
        new NamedThreadLocal<>(
            "Thread local holding the current spring data jpa repository entity graph");

    JpaEntityGraphMethodInterceptor(
        EntityManager entityManager, RepositoryInformation repositoryInformation) {
      this.domainClass = repositoryInformation.getDomainType();
      this.entityManager = entityManager;
      this.defaultEntityGraphs =
          new CompositeDefaultEntityGraphs(
              new MethodProvidedDefaultEntityGraphs(),
              new LegacyDefaultEntityGraphs(entityManager, repositoryInformation.getDomainType()));
    }

    public EntityGraphQueryHintCandidate getCurrentJpaEntityGraph() {
      return currentEntityGraph.get();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      if (CURRENT_REPOSITORY.get() == this) {
        return invocation.proceed();
      }
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

      Object repository;
      if (invocation instanceof ProxyMethodInvocation) {
        repository = ((ProxyMethodInvocation) invocation).getProxy();
      } else {
        repository = invocation.getThis();
        requireNonNull(repository, "No qualifier found for invocation " + repository);
      }

      ResolvableType returnType =
          ResolvableType.forMethodReturnType(invocation.getMethod(), repository.getClass());

      EntityGraphQueryHintCandidate candidate =
          buildEntityGraphCandidate(providedEntityGraph, repository);

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
        EntityGraph providedEntityGraph, Object repository) {

      EntityGraphQueryHint queryHint =
          Optional.ofNullable(providedEntityGraph)
              .flatMap(entityGraph -> entityGraph.buildQueryHint(entityManager, domainClass))
              .orElse(null);

      boolean isPrimary = true;
      if (queryHint == null) {
        queryHint =
            defaultEntityGraphs
                .findOne(repository)
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
