package com.cosium.spring.data.jpa.entity.graph.repository.support;

import javax.persistence.EntityManager;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;
import org.springframework.util.ReflectionUtils;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
public class JpaEntityGraphRepositoryFactory extends JpaRepositoryFactory {

	static {
		addEntityGraphToSpecialTypes();
	}

	/**
	 * Adds EntityGraph as a special type, to be able to use on method named queries
	 */
	private static void addEntityGraphToSpecialTypes(){
		addEntityGraphToSpecialTypes(Parameters.class, "TYPES");
		addEntityGraphToSpecialTypes(Parameter.class, "TYPES");
	}

	private static void addEntityGraphToSpecialTypes(Class<?> clazz, String fieldName){
		try{
			Field field = ReflectionUtils.findField(clazz, fieldName);
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			List<Class<?>> specialTypes = new ArrayList<Class<?>>((List<Class<?>>) field.get(null));
			specialTypes.add(EntityGraph.class);
			ReflectionUtils.setField(field, null, specialTypes);
		} catch (Exception e){
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Creates a new {@link JpaRepositoryFactory}.
	 *
	 * @param entityManager must not be {@literal null}
	 */
	public JpaEntityGraphRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
		addRepositoryProxyPostProcessor(new RepositoryMethodPostProcessor());
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return SimpleJpaEntityGraphRepository.class;
	}
}
