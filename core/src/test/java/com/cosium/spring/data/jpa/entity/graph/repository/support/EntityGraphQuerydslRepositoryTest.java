package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.querydsl.core.types.Predicate;
import org.junit.Ignore;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;

/**
 * Created on 22/10/18.
 *
 * @author Reda.Housni-Alaoui
 */
@Ignore
public class EntityGraphQuerydslRepositoryTest {

  private class EntityGraphQuerydslRepositoryChild
      extends EntityGraphQuerydslRepository<Object, Integer> {

    public EntityGraphQuerydslRepositoryChild(
        JpaEntityInformation<Object, ?> entityInformation, EntityManager entityManager) {
      super(entityInformation, entityManager);
    }

    @Override
    public boolean exists(Predicate predicate) {
      // #20 Feat. Customizing EntityGraphQuerydslRepository (making delegate protected)
      // This only makes sure that the delegate stays acessible to children repos for possible
      // customizations
      return querydslJpaRepositoryDelegate.exists(predicate);
    }
  }
}
