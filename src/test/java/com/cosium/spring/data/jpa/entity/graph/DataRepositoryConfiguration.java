package com.cosium.spring.data.jpa.entity.graph;

import com.cosium.spring.data.jpa.entity.graph.repository.support.JpaEntityGraphRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = JpaEntityGraphRepositoryFactoryBean.class)
public class DataRepositoryConfiguration {

}
