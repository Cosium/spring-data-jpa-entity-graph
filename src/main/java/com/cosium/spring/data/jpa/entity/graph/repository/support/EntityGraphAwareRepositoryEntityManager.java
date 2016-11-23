package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.jpa.repository.query.Jpa21Utils;

/**
 * Intercepts {@link EntityManager} method calls in order to manipulate query hints map. <br>
 * One interceptor intstance is built and used by one unique repository instance.
 *
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class EntityGraphAwareRepositoryEntityManager implements MethodInterceptor {

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
	 * @param entityManager The entity manager to proxy
	 * @return The proxied entity manager
	 */
	static EntityManager proxy(EntityManager entityManager) {
		ProxyFactory proxyFactory = new ProxyFactory(entityManager);
		proxyFactory.addAdvice(new EntityGraphAwareRepositoryEntityManager());
		return (EntityManager) proxyFactory.getProxy();
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String methodName = invocation.getMethod().getName();
		if (FIND_METHODS.contains(methodName)) {
			addEntityGraph(invocation);
		}
		Object result = invocation.proceed();
		if(CREATE_QUERY_METHODS.contains(methodName)){
			addEntityGraph(invocation, (Query) result);
		}
		return result;
	}

	/**
	 * Push the current entity graph to the created query
	 * @param invocation The method invocation
	 * @param query The query to populate
	 */
	private void addEntityGraph(MethodInvocation invocation, Query query){
		EntityGraphBean entityGraphBean = EntityGraphAwareRepositoryMethodPostProcessor.getCurrentJpaEntityGraph();
		if (entityGraphBean == null) {
			return;
		}
		Map<String, Object> hints = Jpa21Utils.tryGetFetchGraphHints(
				(EntityManager) invocation.getThis(),
				entityGraphBean.getJpaEntityGraph(),
				entityGraphBean.getDomainClass()
		);
		for(Map.Entry<String, Object> hint: hints.entrySet()){
			query.setHint(hint.getKey(), hint.getValue());
		}
	}

	/**
	 * Push the current entity graph to the find method query hints.
	 * @param invocation The invocation of the find method
	 */
	private void addEntityGraph(MethodInvocation invocation) {
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

		EntityGraphBean entityGraphBean = EntityGraphAwareRepositoryMethodPostProcessor.getCurrentJpaEntityGraph();
		if (entityGraphBean == null) {
			return;
		}

		queryProperties = new HashMap<String, Object>(queryProperties);

		queryProperties.putAll(
				Jpa21Utils.tryGetFetchGraphHints(
						(EntityManager) invocation.getThis(),
						entityGraphBean.getJpaEntityGraph(),
						entityGraphBean.getDomainClass()
				)
		);

		invocation.getArguments()[index] = queryProperties;
	}
}
