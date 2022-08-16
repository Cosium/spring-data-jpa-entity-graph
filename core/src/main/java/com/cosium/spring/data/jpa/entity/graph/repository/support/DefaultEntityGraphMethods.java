package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphRepository;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AdvisedSupportListener;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

/**
 * @author Réda Housni Alaoui
 */
class DefaultEntityGraphMethods implements MethodInterceptor, RepositoryProxyPostProcessor {

  public static final DefaultEntityGraphMethods INSTANCE = new DefaultEntityGraphMethods();

  private final DefaultMethodInvokingMethodInterceptor defaultMethodInvokingMethodInterceptor =
      new DefaultMethodInvokingMethodInterceptor();

  private DefaultEntityGraphMethods() {}

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    RepositoryMethodInvocation methodInvocation = new RepositoryMethodInvocation(invocation);
    if (!isDefaultEntityGraphMethod(methodInvocation)) {
      return invocation.proceed();
    }

    return defaultMethodInvokingMethodInterceptor.invoke(invocation);
  }

  private boolean isDefaultEntityGraphMethod(RepositoryMethodInvocation invocation) {
    return invocation.repository() instanceof EntityGraphRepository<?, ?>
        && "defaultEntityGraph".equals(invocation.method().getName())
        && invocation.arguments().length == 0;
  }

  @Override
  public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
    // The current advice needs to be positioned before
    // org.springframework.data.repository.core.support.SurroundingTransactionDetectorMethodInterceptor
    factory.addListener(
        new AdvisedSupportListener() {

          @Override
          public void activated(AdvisedSupport advised) {
            factory.addAdvice(0, DefaultEntityGraphMethods.this);
          }

          @Override
          public void adviceChanged(AdvisedSupport advised) {
            // Do nothing
          }
        });
  }
}
