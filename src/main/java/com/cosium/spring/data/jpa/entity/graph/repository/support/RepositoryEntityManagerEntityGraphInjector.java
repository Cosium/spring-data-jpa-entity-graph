package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.jpa.repository.query.Jpa21Utils;

/**
 * Injects captured {@link org.springframework.data.jpa.repository.query.JpaEntityGraph} into query hints. <br>
 * Intercepts {@link EntityManager} method calls in order to manipulate query hints map. <br>
 * One interceptor instance is built and used by one unique repository instance. <br>
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class RepositoryEntityManagerEntityGraphInjector implements MethodInterceptor {

	/**
	 * The list of methods that can take a map of query hints as an argument
	 */
	private static final List<String> FIND_METHODS = Collections.singletonList("find");
	/**
	 * The list of methods that can return a {@link Query} object. {@link Query} can then be populated with query hints.
	 */
	private static final List<String> CREATE_QUERY_METHODS = Arrays.asList("createQuery", "createNamedQuery");

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
		EntityGraphBean entityGraphBean = RepositoryMethodEntityGraphExtractor.getCurrentJpaEntityGraph();
		String methodName = invocation.getMethod().getName();
		if (entityGraphBean != null && FIND_METHODS.contains(methodName)) {
			addEntityGraphToFindMethodQueryHints(entityGraphBean, invocation);
		}

		Object result = invocation.proceed();

		if (entityGraphBean != null && CREATE_QUERY_METHODS.contains(methodName)) {
			addEntityGraphToCreatedQuery(entityGraphBean, invocation, (Query) result);
		}
		return result;
	}

	private Map<String, Object> getQueryHints(EntityManager entityManager, EntityGraphBean entityGraphBean) {
		if (entityGraphBean == null) {
			return new HashMap<String, Object>();
		}
		return Jpa21Utils.tryGetFetchGraphHints(
				entityManager,
				entityGraphBean.getJpaEntityGraph(),
				entityGraphBean.getDomainClass()
		);
	}

	/**
	 * Push the current entity graph to the created query
	 *
	 * @param invocation The method invocation
	 * @param query The query to populate
	 */
	private void addEntityGraphToCreatedQuery(EntityGraphBean entityGraphBean, MethodInvocation invocation, Query query) {
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

		if (resultType != null && !resultType.equals(entityGraphBean.getDomainClass())) {
			return;
		}

		Map<String, Object> hints = getQueryHints((EntityManager) invocation.getThis(), entityGraphBean);
		for (Map.Entry<String, Object> hint : hints.entrySet()) {
			query.setHint(hint.getKey(), hint.getValue());
		}
	}

	/**
	 * Push the current entity graph to the find method query hints.
	 *
	 * @param invocation The invocation of the find method
	 */
	private void addEntityGraphToFindMethodQueryHints(EntityGraphBean entityGraphBean, MethodInvocation invocation) {
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
			return;
		}

		queryProperties = new HashMap<String, Object>(queryProperties);
		queryProperties.putAll(getQueryHints((EntityManager) invocation.getThis(), entityGraphBean));
		invocation.getArguments()[index] = queryProperties;
	}
}
