package com.cosium.spring.data.jpa.entity.graph.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 23/11/16.
 *
 * @author Reda.Housni-Alaoui
 * @deprecated Use {@link EntityGraphJpaSpecificationExecutor} instead
 */
@Deprecated
@NoRepositoryBean
public interface JpaEntityGraphSpecificationExecutor<T> extends EntityGraphJpaSpecificationExecutor<T> {

}
