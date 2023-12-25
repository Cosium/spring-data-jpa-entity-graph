package com.cosium.spring.data.jpa.entity.graph.repository.query;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.repository.query.JpaParameters;
import org.springframework.data.repository.query.ParametersSource;
import org.springframework.data.util.TypeInformation;

/**
 * @author Réda Housni Alaoui
 */
class EntityGraphAwareJpaParameters extends JpaParameters {

    public EntityGraphAwareJpaParameters(ParametersSource parametersSource) {
        super(parametersSource, methodParameter ->
                new EntityGraphAwareJpaParameter(methodParameter, parametersSource.getDomainTypeInformation())
        );
    }

    private static class EntityGraphAwareJpaParameter extends JpaParameters.JpaParameter {

        private final boolean entityGraph;

        private EntityGraphAwareJpaParameter(MethodParameter parameter, TypeInformation<?> domainType) {
            super(parameter, domainType);
            this.entityGraph = EntityGraph.class.isAssignableFrom(parameter.getParameterType());
        }

        @Override
        public boolean isBindable() {
            return !entityGraph && super.isBindable();
        }

        @Override
        public boolean isSpecialParameter() {
            return entityGraph || super.isSpecialParameter();
        }
    }
}
