package com.cosium.spring.data.jpa.entity.graph.repository.query;

import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.ParametersSource;

import java.lang.reflect.Method;

/**
 * @author Réda Housni Alaoui
 */
class EntityGraphAwareJpaQueryMethod extends JpaQueryMethod {

    protected EntityGraphAwareJpaQueryMethod(
            Method method,
            RepositoryMetadata metadata,
            ProjectionFactory factory,
            QueryExtractor extractor) {
        super(method, metadata, factory, extractor);
    }

    @Override
    protected Parameters<?, ?> createParameters(ParametersSource parametersSource) {
        return new EntityGraphAwareJpaParameters(parametersSource);
    }
}
