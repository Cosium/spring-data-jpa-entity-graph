package com.cosium.spring.data.jpa.entity.graph.repository.support;

import static java.util.Objects.requireNonNull;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.Nullable;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Réda Housni Alaoui
 */
class RepositoryMethodInvocation {

  private final MethodInvocation invocation;

  public RepositoryMethodInvocation(MethodInvocation invocation) {
    this.invocation = invocation;
  }

  public @Nullable Object proceed() throws Throwable {
    return invocation.proceed();
  }

  public Object repository() {
    if (invocation instanceof ProxyMethodInvocation) {
      return ((ProxyMethodInvocation) invocation).getProxy();
    } else {
      Object repository = invocation.getThis();
      return requireNonNull(repository, "No qualifier found for invocation " + repository);
    }
  }

  public Method method() {
    return invocation.getMethod();
  }

  public @Nullable Object[] arguments() {
    return invocation.getArguments();
  }

  public @Nullable EntityGraph findEntityGraphArgument() {
    EntityGraph providedEntityGraph = null;
    for (Object argument : invocation.getArguments()) {
      if (!(argument instanceof EntityGraph newEntityGraph)) {
        continue;
      }
      if (providedEntityGraph != null) {
        throw new MultipleEntityGraphException(
            "Duplicate EntityGraphs detected. '"
                + providedEntityGraph
                + "' and '"
                + newEntityGraph
                + "' were passed to method "
                + invocation.getMethod());
      }
      providedEntityGraph = newEntityGraph;
    }
    return providedEntityGraph;
  }

  public boolean isSpecificationExecutorFindByMethod() {
    if (!(repository() instanceof JpaSpecificationExecutor<?>)) {
      return false;
    }
    if (!"findBy".equals(method().getName())) {
      return false;
    }
    @Nullable Object[] arguments = arguments();
    return arguments.length == 2 || arguments.length == 3;
  }
}
