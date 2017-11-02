package com.cosium.spring.data.jpa.entity.graph.repository.support;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

/**
 * This repository factory allows to build {@link EntityGraph} aware repositories. Created on
 * 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class EntityGraphJpaRepositoryFactory extends JpaRepositoryFactory {

  static {
    addEntityGraphToSpecialTypes();
  }

  /**
   * Creates a new {@link JpaRepositoryFactory}.
   *
   * @param entityManager must not be {@literal null}
   */
  public EntityGraphJpaRepositoryFactory(EntityManager entityManager) {
    super(entityManager);
    addRepositoryProxyPostProcessor(new RepositoryMethodEntityGraphExtractor(entityManager));
  }

  /**
   * Add {@link EntityGraph} to the special types.<br>
   * {@link EntityGraph} must be considered as a special type by Spring Data JPA.<br>
   * For this to occur, {@link EntityGraph} must be part of Spring Data JPA arrays storing special
   * types.<br>
   * Once a type is marked as special, Spring Data JPA will not try to bind it to an under
   * construction query.
   */
  private static void addEntityGraphToSpecialTypes() {
    addEntityGraphToSpecialTypes(Parameters.class, "TYPES");
    addEntityGraphToSpecialTypes(Parameter.class, "TYPES");
  }

  private static void addEntityGraphToSpecialTypes(Class<?> clazz, String fieldName) {
    try {
      Field field = ReflectionUtils.findField(clazz, fieldName);
      field.setAccessible(true);
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
      List<Class<?>> specialTypes = new ArrayList<Class<?>>((List<Class<?>>) field.get(null));
      specialTypes.add(EntityGraph.class);
      ReflectionUtils.setField(field, null, specialTypes);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Override
  protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
    if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
      return EntityGraphQuerydslRepository.class;
    } else {
      return EntityGraphSimpleJpaRepository.class;
    }
  }

  private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
    return QUERY_DSL_PRESENT
        && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
  }
}
