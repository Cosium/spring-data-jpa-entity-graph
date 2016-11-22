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
import org.springframework.util.StringUtils;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
class JpaEntityGraphPostProcessor implements RepositoryProxyPostProcessor {

	private static final ThreadLocal<JpaEntityGraph> CURRENT_ENTITY_GRAPH = new NamedThreadLocal<JpaEntityGraph>("Thread local holding the current spring data jpa repository entity graph");

	@Override
	public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
		factory.addAdvice(JpaEntityGraphMethodInterceptor.INSTANCE);
	}

	static JpaEntityGraph getCurrentJpaEntityGraph(){
		return CURRENT_ENTITY_GRAPH.get();
	}

	private enum JpaEntityGraphMethodInterceptor implements MethodInterceptor{
		INSTANCE;

		private static JpaEntityGraph convert(EntityGraph entityGraph){
			if(entityGraph == null){
				return null;
			}

			org.springframework.data.jpa.repository.EntityGraph.EntityGraphType type;
			switch (entityGraph.getType()){
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
			return new JpaEntityGraph(
					StringUtils.hasText(entityGraph.getName())? entityGraph.getName(): "_-_-_-_-_",
					type,
					attributePaths != null? attributePaths.toArray(new String[attributePaths.size()]) : null
			);
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			Object[] arguments = invocation.getArguments();
			EntityGraph entityGraph = null;
			for(Object argument: arguments){
				if(!(argument instanceof EntityGraph)){
					continue;
				}
				entityGraph = (EntityGraph) argument;
				break;
			}

			CURRENT_ENTITY_GRAPH.set(convert(entityGraph));
			try{
				return invocation.proceed();
			} finally {
				CURRENT_ENTITY_GRAPH.remove();
			}
		}
	}
}
