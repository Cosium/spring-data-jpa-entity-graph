package com.cosium.spring.data.jpa.entity.graph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@NoRepositoryBean
public interface EntityGraphJpaRepository<T, ID>
    extends JpaRepository<T, ID>,
        EntityGraphListCrudRepository<T, ID>,
        EntityGraphListPagingAndSortingRepository<T, ID>,
        EntityGraphQueryByExampleExecutor<T> {}
