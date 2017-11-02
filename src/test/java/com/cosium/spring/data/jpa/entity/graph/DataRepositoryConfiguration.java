package com.cosium.spring.data.jpa.entity.graph;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
public class DataRepositoryConfiguration {}
