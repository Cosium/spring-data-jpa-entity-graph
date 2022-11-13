package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraphQueryHint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Selection;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;

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
    EntityGraphQueryHintCandidate entityGraphCandidate = EntityGraphQueryHintCandidates.current();
    String methodName = invocation.getMethod().getName();
    boolean hasEntityGraphCandidate = entityGraphCandidate != null;
    if (hasEntityGraphCandidate && FIND_METHODS.contains(methodName)) {
      addEntityGraphToFindMethodQueryHints(entityGraphCandidate, invocation);
    }

    Object result = invocation.proceed();

    if (hasEntityGraphCandidate
        && CREATE_QUERY_METHODS.contains(methodName)
        && isQueryCreationEligible(entityGraphCandidate, invocation)) {
      result = RepositoryQueryEntityGraphInjector.proxy((Query) result, entityGraphCandidate);
    }
    return result;
  }

  private boolean isQueryCreationEligible(
      EntityGraphQueryHintCandidate entityGraphCandidate, MethodInvocation invocation) {
    Class<?> resultType = null;
    for (Object argument : invocation.getArguments()) {
      if (argument instanceof Class<?>) {
        resultType = (Class<?>) argument;
        break;
      }
      if (!(argument instanceof CriteriaQuery<?> criteriaQuery)) {
        continue;
      }
      Selection<?> selection = criteriaQuery.getSelection();
      if (selection == null) {
        continue;
      }
      resultType = selection.getJavaType();
      break;
    }
    return resultType == null || resultType.equals(entityGraphCandidate.domainClass());
  }

  /**
   * Push the current entity graph to the find method query hints.
   *
   * @param entityGraphCandidate The EntityGraph to set
   * @param invocation The invocation of the find method
   */
  private void addEntityGraphToFindMethodQueryHints(
      EntityGraphQueryHintCandidate entityGraphCandidate, MethodInvocation invocation) {
    LOG.trace("Trying to push the EntityGraph candidate to the query hints find method");

    Map<String, Object> queryProperties = null;
    int index = 0;
    for (Object argument : invocation.getArguments()) {
      if (argument instanceof Map<?, ?>) {
        queryProperties = (Map<String, Object>) argument;
        break;
      }
      index++;
    }
    if (queryProperties == null) {
      LOG.trace("No query hints passed to the find method.");
      return;
    }
    if (!entityGraphCandidate.primary() && QueryHintsUtils.containsEntityGraph(queryProperties)) {
      LOG.trace(
          "The query hints passed with the find method already holds an entity graph. Overriding aborted because the candidate EntityGraph is not primary.");
      return;
    }

    queryProperties = new HashMap<>(queryProperties);
    QueryHintsUtils.removeEntityGraphs(queryProperties);

    EntityGraphQueryHint entityGraphQueryHint = entityGraphCandidate.queryHint();
    queryProperties.put(entityGraphQueryHint.type().key(), entityGraphQueryHint.entityGraph());

    invocation.getArguments()[index] = queryProperties;
  }
}
