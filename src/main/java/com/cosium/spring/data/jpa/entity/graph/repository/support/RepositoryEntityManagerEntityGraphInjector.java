package com.cosium.spring.data.jpa.entity.graph.repository.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;

/**
 * Injects captured {@link org.springframework.data.jpa.repository.query.JpaEntityGraph} into query
 * hints. <br>
 * Intercepts {@link EntityManager} method calls in order to manipulate query hints map. <br>
 * One interceptor instance is built and used by one unique repository instance. <br>
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class RepositoryEntityManagerEntityGraphInjector implements MethodInterceptor {

  private static final Logger LOG =
      LoggerFactory.getLogger(RepositoryEntityManagerEntityGraphInjector.class);

  /** The list of methods that can take a map of query hints as an argument */
  private static final List<String> FIND_METHODS = Collections.singletonList("find");
  /**
   * The list of methods that can return a {@link Query} object. {@link Query} can then be populated
   * with query hints.
   */
  private static final List<String> CREATE_QUERY_METHODS =
      Arrays.asList("createQuery", "createNamedQuery");

  private RepositoryEntityManagerEntityGraphInjector() {}

  /**
   * Builds a proxy on entity manager which is aware of methods that can make use of query hints.
   *
   * @param entityManager The entity manager to proxy
   * @return The proxied entity manager
   */
  static EntityManager proxy(EntityManager entityManager) {
    ProxyFactory proxyFactory = new ProxyFactory(entityManager);
    proxyFactory.addAdvice(new RepositoryEntityManagerEntityGraphInjector());
    return (EntityManager) proxyFactory.getProxy();
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    EntityGraphBean entityGraphCandidate =
        RepositoryMethodEntityGraphExtractor.getCurrentJpaEntityGraph();
    String methodName = invocation.getMethod().getName();
    boolean hasEntityGraphCandidate = entityGraphCandidate != null;
    if (hasEntityGraphCandidate && FIND_METHODS.contains(methodName)) {
      addEntityGraphToFindMethodQueryHints(entityGraphCandidate, invocation);
    }

    Object result = invocation.proceed();

    if (hasEntityGraphCandidate
        && CREATE_QUERY_METHODS.contains(methodName)
        && isQueryCreationEligible(entityGraphCandidate, invocation)) {
      result =
          RepositoryQueryEntityGraphInjector.proxy(
              (Query) result, (EntityManager) invocation.getThis(), entityGraphCandidate);
    }
    return result;
  }

  private boolean isQueryCreationEligible(
      EntityGraphBean entityGraphCandidate, MethodInvocation invocation) {
    Class<?> resultType = null;
    for (Object argument : invocation.getArguments()) {
      if (argument instanceof Class) {
        resultType = (Class<?>) argument;
        break;
      } else if (argument instanceof CriteriaQuery) {
        resultType = ((CriteriaQuery) argument).getResultType();
        break;
      }
    }
    return resultType == null || resultType.equals(entityGraphCandidate.getDomainClass());
  }

  /**
   * Push the current entity graph to the find method query hints.
   *
   * @param entityGraphCandidate The EntityGraph to set
   * @param invocation The invocation of the find method
   */
  private void addEntityGraphToFindMethodQueryHints(
      EntityGraphBean entityGraphCandidate, MethodInvocation invocation) {
    LOG.trace("Trying to push the EntityGraph candidate to the query hints find method");

    Map<String, Object> queryProperties = null;
    int index = 0;
    for (Object argument : invocation.getArguments()) {
      if (argument instanceof Map) {
        queryProperties = (Map) argument;
        break;
      }
      index++;
    }
    if (queryProperties == null) {
      LOG.trace("No query hints passed to the find method.");
      return;
    }
    if (!entityGraphCandidate.isPrimary() && QueryHintsUtils.containsEntityGraph(queryProperties)) {
      LOG.trace(
          "The query hints passed with the find method already hold an entity graph. Overriding aborted because the candidate EntityGraph is optional.");
      return;
    }

    queryProperties = new HashMap<String, Object>(queryProperties);
    QueryHintsUtils.removeEntityGraphs(queryProperties);
    queryProperties.putAll(
        QueryHintsUtils.buildQueryHints(
            (EntityManager) invocation.getThis(), entityGraphCandidate));
    invocation.getArguments()[index] = queryProperties;
  }
}
