package com.cosium.spring.data.jpa.entity.graph.repository.query;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import java.lang.reflect.Method;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.repository.query.JpaParameters;

/** @author Réda Housni Alaoui */
class EntityGraphAwareJpaParameters extends JpaParameters {

  public EntityGraphAwareJpaParameters(Method method) {
    super(method);
  }

  @Override
  protected JpaParameter createParameter(MethodParameter parameter) {
    return new EntityGraphAwareJpaParameter(parameter);
  }

  private static class EntityGraphAwareJpaParameter extends JpaParameters.JpaParameter {

    private final boolean entityGraph;

    protected EntityGraphAwareJpaParameter(MethodParameter parameter) {
      super(parameter);
      this.entityGraph = EntityGraph.class.equals(parameter.getParameterType());
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
