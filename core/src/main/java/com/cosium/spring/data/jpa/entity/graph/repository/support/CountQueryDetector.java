package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.querydsl.jpa.JPQLQuery;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.NamedThreadLocal;

/**
 * Created on 05/12/16.
 *
 * @author Reda.Housni-Alaoui
 */
class CountQueryDetector implements MethodInterceptor {

  private static final CountQueryDetector INSTANCE = new CountQueryDetector();
  private static final String FETCH_METHOD_NAME_PREFIX = "fetch";

  private static final NamedThreadLocal<Boolean> IS_COUNT_QUERY =
      new NamedThreadLocal<>(
          "A thread local holding a boolean describing "
              + "the fact that the current query is count query");

  private CountQueryDetector() {}

  static JPQLQuery<?> proxy(JPQLQuery<?> countQuery) {
    ProxyFactory proxyFactory = new ProxyFactory(countQuery);
    proxyFactory.addAdvice(INSTANCE);
    return (JPQLQuery<?>) proxyFactory.getProxy();
  }

  static boolean isCountQuery() {
    return IS_COUNT_QUERY.get() != null && IS_COUNT_QUERY.get();
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    if (invocation.getMethod().getName().startsWith(FETCH_METHOD_NAME_PREFIX)) {
      IS_COUNT_QUERY.set(true);
    }
    try {
      return invocation.proceed();
    } finally {
      IS_COUNT_QUERY.remove();
    }
  }
}
