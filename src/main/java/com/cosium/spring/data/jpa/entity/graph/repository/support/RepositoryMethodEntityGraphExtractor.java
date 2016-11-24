package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;
import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
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
class RepositoryMethodEntityGraphExtractor implements RepositoryProxyPostProcessor {

	private static final ThreadLocal<EntityGraphBean> CURRENT_ENTITY_GRAPH =
			new NamedThreadLocal<EntityGraphBean>("Thread local holding the current spring data jpa repository entity graph");

	private final EntityManager entityManager;

	RepositoryMethodEntityGraphExtractor(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
		factory.addAdvice(new JpaEntityGraphMethodInterceptor(entityManager, repositoryInformation.getDomainType()));
	}

	static EntityGraphBean getCurrentJpaEntityGraph() {
		return CURRENT_ENTITY_GRAPH.get();
	}

	/**
	 * @param entityGraph
	 * @return True if the provided EntityGraph is empty
	 */
	private static boolean isEmpty(EntityGraph entityGraph) {
		return entityGraph == null ||
				(
						entityGraph.getEntityGraphAttributePaths() == null
								&& entityGraph.getEntityGraphName() == null
								&& entityGraph.getEntityGraphType() == null
				);
	}

	private static class JpaEntityGraphMethodInterceptor implements MethodInterceptor {

		private static final String DEFAULT_ENTITYGRAPH_NAME_SUFFIX = ".default";
		private final Class domainClass;
		private final EntityGraphBean defaultEntityGraph;

		JpaEntityGraphMethodInterceptor(EntityManager entityManager, Class domainClass) {
			this.domainClass = domainClass;
			this.defaultEntityGraph = findDefaultEntityGraph(entityManager);
		}

		/**
		 * @param entityManager
		 * @return The default entity graph if it exists. Null otherwise.
		 */
		private EntityGraphBean findDefaultEntityGraph(EntityManager entityManager){
			EntityGraphBean entityGraphBean = null;
			List<javax.persistence.EntityGraph<?>> entityGraphs = entityManager.getEntityGraphs(domainClass);
			for (javax.persistence.EntityGraph entityGraph : entityGraphs) {
				if (entityGraph.getName().endsWith(DEFAULT_ENTITYGRAPH_NAME_SUFFIX)) {
					if(entityGraphBean != null){
						throw new RuntimeException("Multiple default entity graphs detected : " + entityGraph.getName() + " and " + entityGraphBean.getJpaEntityGraph().getName());
					}
					entityGraphBean = buildEntityGraphBean(EntityGraphUtils.fromName(entityGraph.getName()), true);
				}
			}
			return entityGraphBean;
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
			EntityGraphBean entityGraphBean = isEmpty(entityGraph)? defaultEntityGraph : buildEntityGraphBean(entityGraph, false);
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

		private EntityGraphBean buildEntityGraphBean(EntityGraph entityGraph, boolean optional) {
			Assert.notNull(entityGraph.getEntityGraphType());

			org.springframework.data.jpa.repository.EntityGraph.EntityGraphType type;
			switch (entityGraph.getEntityGraphType()) {
				case FETCH:
					type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;
					break;
				case LOAD:
					type = org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;
					break;
				default:
					throw new RuntimeException("Unknown entity graph type");
			}

			List<String> attributePaths = entityGraph.getEntityGraphAttributePaths();
			JpaEntityGraph jpaEntityGraph = new JpaEntityGraph(
					StringUtils.hasText(entityGraph.getEntityGraphName()) ? entityGraph.getEntityGraphName() : domainClass.getName() + "-_-_-_-_-_-",
					type,
					attributePaths != null ? attributePaths.toArray(new String[attributePaths.size()]) : null
			);

			return new EntityGraphBean(jpaEntityGraph, domainClass, optional);
		}

	}
}
