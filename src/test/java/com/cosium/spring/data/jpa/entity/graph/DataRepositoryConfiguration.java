package com.cosium.spring.data.jpa.entity.graph;

import com.cosium.spring.data.jpa.entity.graph.repository.support.JpaEntityGraphRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = JpaEntityGraphRepositoryFactoryBean.class)
@EnableAutoConfiguration
public class DataRepositoryConfiguration {

}
