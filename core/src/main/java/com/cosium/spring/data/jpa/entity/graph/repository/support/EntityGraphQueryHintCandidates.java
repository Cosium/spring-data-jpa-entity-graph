package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.InapplicableEntityGraphException;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
class EntityGraphQueryHintCandidates implements MethodInterceptor {

  private static final Logger LOG = LoggerFactory.getLogger(EntityGraphQueryHintCandidates.class);

  private static final ThreadLocal<EntityGraphQueryHintCandidates> CURRENT_CANDIDATES =
      new NamedThreadLocal<>("Thread local holding the current candidates");

  private final Class<?> domainClass;
  private final EntityManager entityManager;
  private final DefaultEntityGraphs defaultEntityGraphs;
  private final ThreadLocal<EntityGraphQueryHintCandidate> currentCandidate =
      new NamedThreadLocal<>("Thread local holding the current entity graph query hint candidate");

  public EntityGraphQueryHintCandidates(
      EntityManager entityManager, RepositoryInformation repositoryInformation) {
    this.domainClass = repositoryInformation.getDomainType();
    this.entityManager = entityManager;
    this.defaultEntityGraphs = new MethodProvidedDefaultEntityGraphs();
  }

  public static RepositoryProxyPostProcessor createPostProcessor(EntityManager entityManager) {
    return new PostProcessor(entityManager);
  }

  public static EntityGraphQueryHintCandidate current() {
    EntityGraphQueryHintCandidates currentRepository = CURRENT_CANDIDATES.get();
    if (currentRepository == null) {
      return null;
    }
    return currentRepository.currentCandidate.get();
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    EntityGraphQueryHintCandidates oldRepo = CURRENT_CANDIDATES.get();
    CURRENT_CANDIDATES.set(this);
    try {
      return doInvoke(invocation);
    } finally {
      if (oldRepo == null) {
        CURRENT_CANDIDATES.remove();
      } else {
        CURRENT_CANDIDATES.set(oldRepo);
      }
    }
  }

  private Object doInvoke(MethodInvocation invocation) throws Throwable {
    RepositoryMethodInvocation methodInvocation = new RepositoryMethodInvocation(invocation);
    EntityGraph providedEntityGraph = methodInvocation.findEntityGraphArgument();
    Object repository = methodInvocation.repository();
    ResolvableType returnType =
        ResolvableType.forMethodReturnType(methodInvocation.method(), repository.getClass());
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

    EntityGraphQueryHintCandidate genuineCandidate = currentCandidate.get();
    boolean newEntityGraphCandidatePreValidated =
        candidate != null && (genuineCandidate == null || !genuineCandidate.primary());
    if (newEntityGraphCandidatePreValidated) {
      currentCandidate.set(candidate);
    }
    try {
      return methodInvocation.proceed();
    } finally {
      if (newEntityGraphCandidatePreValidated) {
        if (genuineCandidate == null) {
          currentCandidate.remove();
        } else {
          currentCandidate.set(genuineCandidate);
        }
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

  private record PostProcessor(EntityManager entityManager)
      implements RepositoryProxyPostProcessor {

    @Override
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
      factory.addAdvice(new EntityGraphQueryHintCandidates(entityManager, repositoryInformation));
    }
  }
}
