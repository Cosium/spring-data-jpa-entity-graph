package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;
import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.InapplicableEntityGraphException;
import com.cosium.spring.data.jpa.entity.graph.repository.exception.MultipleDefaultEntityGraphException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.ResolvableType;
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

	private static final Logger LOG = LoggerFactory.getLogger(RepositoryMethodEntityGraphExtractor.class);

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
		private final Class<?> domainClass;
		private final EntityGraph defaultEntityGraph;

		JpaEntityGraphMethodInterceptor(EntityManager entityManager, Class domainClass) {
			this.domainClass = domainClass;
			this.defaultEntityGraph = findDefaultEntityGraph(entityManager);
		}

		/**
		 * @param entityManager
		 * @return The default entity graph if it exists. Null otherwise.
		 */
		private EntityGraph findDefaultEntityGraph(EntityManager entityManager) {
			EntityGraph defaultEntityGraph = null;
			List<javax.persistence.EntityGraph<?>> entityGraphs = (List<javax.persistence.EntityGraph<?>>) entityManager.getEntityGraphs(domainClass);
			for (javax.persistence.EntityGraph entityGraph : entityGraphs) {
				if (entityGraph.getName().endsWith(DEFAULT_ENTITYGRAPH_NAME_SUFFIX)) {
					if (defaultEntityGraph != null) {
						throw new MultipleDefaultEntityGraphException(entityGraph.getName(), defaultEntityGraph.getEntityGraphName());
					}
					defaultEntityGraph = EntityGraphUtils.fromName(entityGraph.getName());
				}
			}
			return defaultEntityGraph;
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

			boolean emptyEntityGraph = isEmpty(entityGraph);

			Class<?> implementationClass;
			if (invocation instanceof ReflectiveMethodInvocation) {
				implementationClass = ((ReflectiveMethodInvocation) invocation).getProxy().getClass();
			} else {
				implementationClass = invocation.getThis().getClass();
			}

			EntityGraphBean entityGraphCandidate = buildEntityGraphCandidate(
					emptyEntityGraph ? defaultEntityGraph : entityGraph,
					ResolvableType.forMethodReturnType(invocation.getMethod(), implementationClass),
					emptyEntityGraph
			);

			if (entityGraphCandidate != null && !entityGraphCandidate.isValid()) {
				if (entityGraphCandidate.isOptional()) {
					LOG.trace("Cannot apply EntityGraph {}", entityGraphCandidate);
					entityGraphCandidate = null;
				} else {
					throw new InapplicableEntityGraphException("Cannot apply EntityGraph " + entityGraphCandidate + " to the the current query");
				}
			}

			if (entityGraphCandidate != null) {
				CURRENT_ENTITY_GRAPH.set(entityGraphCandidate);
			}
			try {
				return invocation.proceed();
			} finally {
				if (entityGraphCandidate != null) {
					CURRENT_ENTITY_GRAPH.remove();
				}
			}
		}

		private EntityGraphBean buildEntityGraphCandidate(EntityGraph entityGraph, ResolvableType returnType, boolean optional) {
			if(entityGraph == null){
				return null;
			}

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
					throw new RuntimeException("Unexpected entity graph type '" + entityGraph.getEntityGraphType() + "'");
			}

			List<String> attributePaths = entityGraph.getEntityGraphAttributePaths();
			JpaEntityGraph jpaEntityGraph = new JpaEntityGraph(
					StringUtils.hasText(entityGraph.getEntityGraphName()) ? entityGraph.getEntityGraphName() : domainClass.getName() + "-_-_-_-_-_-",
					type,
					attributePaths != null ? attributePaths.toArray(new String[attributePaths.size()]) : null
			);

			return new EntityGraphBean(jpaEntityGraph, domainClass, returnType, optional);
		}
	}
}
