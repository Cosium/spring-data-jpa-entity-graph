package com.cosium.spring.data.jpa.entity.graph;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created on 22/11/16.
 *
 * @author Reda.Housni-Alaoui
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.cosium.spring.data.jpa.entity.graph",
    repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class,
    considerNestedRepositories = true)
@EnableTransactionManagement
@ComponentScan
public class DataRepositoryConfiguration {

  @Bean
  public PlatformTransactionManager transactionManager() {
    return new JpaTransactionManager();
  }

  @Bean
  public DataSource dataSource() {
    return new SingleConnectionDataSource("jdbc:hsqldb:mem:.", "sa", "", true);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setDataSource(dataSource());
    factory.setMappingResources("META-INF/orm.xml");
    factory.setPackagesToScan("com.cosium.spring.data.jpa.entity.graph");

    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setDatabase(Database.H2);
    jpaVendorAdapter.setGenerateDdl(true);
    factory.setJpaVendorAdapter(jpaVendorAdapter);

    return factory;
  }
}
