package com.cosium.spring.data.jpa.entity.graph.repository.support;

import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.data.jpa.repository.query.JpaEntityGraph;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Captures {@link EntityGraph} on repositories method calls.
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class EntityGraphAwareRepositoryMethodPostProcessor implements RepositoryProxyPostProcessor {

	private static final ThreadLocal<EntityGraphBean> CURRENT_ENTITY_GRAPH =
			new NamedThreadLocal<EntityGraphBean>("Thread local holding the current spring data jpa repository entity graph");

	@Override
	public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
		factory.addAdvice(new JpaEntityGraphMethodInterceptor(repositoryInformation.getDomainType()));
	}

	static EntityGraphBean getCurrentJpaEntityGraph() {
		return CURRENT_ENTITY_GRAPH.get();
	}

	private static class JpaEntityGraphMethodInterceptor implements MethodInterceptor {

		private final Class<?> domainClass;

		JpaEntityGraphMethodInterceptor(Class<?> domainClass) {
			this.domainClass = domainClass;
		}

		private EntityGraphBean buildEntityGraphBean(EntityGraph entityGraph) {
			if (entityGraph == null) {
				return null;
			}

			Assert.notNull(entityGraph.getType());

			org.springframework.data.jpa.repository.EntityGraph.EntityGraphType type;
			switch (entityGraph.getType()) {
				case FETCH:
					type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;
					break;
				case LOAD:
					type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;
					break;
				default:
					throw new RuntimeException("Unknown entity graph type");
			}

			List<String> attributePaths = entityGraph.getAttributePaths();
			JpaEntityGraph jpaEntityGraph = new JpaEntityGraph(
					StringUtils.hasText(entityGraph.getName()) ? entityGraph.getName() : domainClass.getName() + "-_-_-_-_-_-",
					type,
					attributePaths != null ? attributePaths.toArray(new String[attributePaths.size()]) : null
			);

			return new EntityGraphBean(jpaEntityGraph, domainClass);
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			Object[] arguments = invocation.getArguments();
			EntityGraph entityGraph = null;
			for (Object argument : arguments) {
				if (!(argument instanceof EntityGraph)) {
					continue;
				}
				entityGraph = (EntityGraph) argument;
				break;
			}
			EntityGraphBean entityGraphBean = buildEntityGraphBean(entityGraph);
			if (entityGraphBean != null) {
				CURRENT_ENTITY_GRAPH.set(entityGraphBean);
			}
			try {
				return invocation.proceed();
			} finally {
				if (entityGraphBean != null) {
					CURRENT_ENTITY_GRAPH.remove();
				}
			}
		}
	}
}
