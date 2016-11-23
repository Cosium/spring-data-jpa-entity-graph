package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.jpa.repository.query.Jpa21Utils;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class RepositoryEntityManager implements MethodInterceptor {

	private static final List<String> FIND_METHODS = Collections.singletonList("find");
	private static final List<String> CREATE_QUERY_METHODS = Arrays.asList("createQuery", "createNamedQuery");

	static EntityManager proxy(EntityManager entityManager) {
		ProxyFactory proxyFactory = new ProxyFactory(entityManager);
		proxyFactory.addAdvice(new RepositoryEntityManager());
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

	private void addEntityGraph(MethodInvocation invocation, Query query){
		EntityGraphBean entityGraphBean = RepositoryMethodPostProcessor.getCurrentJpaEntityGraph();
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

		EntityGraphBean entityGraphBean = RepositoryMethodPostProcessor.getCurrentJpaEntityGraph();
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
