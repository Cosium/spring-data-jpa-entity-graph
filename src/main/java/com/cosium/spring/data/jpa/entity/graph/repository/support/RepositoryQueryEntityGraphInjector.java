package com.cosium.spring.data.jpa.entity.graph.repository.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created on 24/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class RepositoryQueryEntityGraphInjector implements MethodInterceptor {

  private static final Logger LOG =
      LoggerFactory.getLogger(RepositoryQueryEntityGraphInjector.class);

  private static final List<String> EXECUTE_QUERY_METHODS =
      Arrays.asList("getResultList", "getSingleResult");

  private final EntityManager entityManager;
  private final EntityGraphBean entityGraphCandidate;

  private RepositoryQueryEntityGraphInjector(
      EntityManager entityManager, EntityGraphBean entityGraphCandidate) {
    Assert.notNull(entityManager);
    Assert.notNull(entityGraphCandidate);
    this.entityManager = entityManager;
    this.entityGraphCandidate = entityGraphCandidate;
  }

  static Query proxy(
      Query query, EntityManager entityManager, EntityGraphBean entityGraphCandidate) {
    ProxyFactory proxyFactory = new ProxyFactory(query);
    proxyFactory.addAdvice(
        new RepositoryQueryEntityGraphInjector(entityManager, entityGraphCandidate));
    return (Query) proxyFactory.getProxy();
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    if (EXECUTE_QUERY_METHODS.contains(invocation.getMethod().getName())) {
      addEntityGraphToQuery((Query) invocation.getThis());
    }
    return invocation.proceed();
  }

  private void addEntityGraphToQuery(Query query) {
    if (CountQueryDetector.isCountQuery()) {
      LOG.trace("CountQuery detected.");
      return;
    }
    if (!entityGraphCandidate.isPrimary()
        && QueryHintsUtils.containsEntityGraph(query.getHints())) {
      LOG.trace(
          "The query hints passed with the find method already hold an entity graph. Overriding aborted because the candidate EntityGraph is optional.");
      return;
    }

    QueryHintsUtils.removeEntityGraphs(query.getHints());
    Map<String, Object> hints =
        QueryHintsUtils.buildQueryHints(entityManager, entityGraphCandidate);
    for (Map.Entry<String, Object> hint : hints.entrySet()) {
      query.setHint(hint.getKey(), hint.getValue());
    }
  }
}
