package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.querydsl.core.types.Predicate;
import org.junit.Ignore;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import org.springframework.data.querydsl.EntityPathResolver;

/**
 * Created on 22/10/18.
 *
 * @author Reda.Housni-Alaoui
 */
@Ignore
public class EntityGraphQuerydslRepositoryTest {

  private static class EntityGraphQuerydslRepositoryChild
      extends EntityGraphQuerydslRepository<Object, Integer> {

    public EntityGraphQuerydslRepositoryChild(
        JpaEntityInformation<Object, ?> entityInformation,
        EntityManager entityManager,
        EntityPathResolver resolver,
        CrudMethodMetadata metadata) {
      super(entityInformation, entityManager, resolver, metadata);
    }

    @Override
    public boolean exists(Predicate predicate) {
      // #20 Feat. Customizing EntityGraphQuerydslRepository (making delegate protected)
      // This only makes sure that the delegate stays accessible to children repos for possible
      // customizations
      return querydslJpaRepositoryDelegate.exists(predicate);
    }
  }
}
