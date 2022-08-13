package com.cosium.spring.data.jpa.entity.graph.repository.query;

import java.lang.reflect.Method;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.JpaParameters;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

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
  protected JpaParameters createParameters(Method method) {
    return new EntityGraphAwareJpaParameters(method);
  }
}
