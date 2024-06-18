package com.codegen.camel.onorm.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DBConfig {

  @Bean
  public DataSource dataSource() {
    var dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl("jdbc:postgresql://localhost/postgres");
    dataSource.setUsername("guest");
    dataSource.setPassword("welcome1");
    return dataSource;
  }

  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
    //
    return new JpaTransactionManager();
  }

  // Camel will look up for params bean id as Map to manipulate all NamedQueries with params
  @Bean
  Map params() {
    return new HashMap<String, Object>();
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    var jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setDatabase(Database.POSTGRESQL);
    jpaVendorAdapter.setShowSql(true);
    return jpaVendorAdapter;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    var localContainerEntityManagerFactoryBean =
            new LocalContainerEntityManagerFactoryBean();
    localContainerEntityManagerFactoryBean.setDataSource(this.dataSource());
    var properties = new Properties();
    properties.put("hibernate.hbm2ddl.auto", "none");
    //properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    properties.put("hibernate.show_sql", "true");
    properties.put("hibernate.format_sql", "true");
    //properties.put("hibernate.transaction.jta.platform", "");
    localContainerEntityManagerFactoryBean.setJpaProperties(properties);
    localContainerEntityManagerFactoryBean.setJpaVendorAdapter(this.jpaVendorAdapter());
    localContainerEntityManagerFactoryBean.setPackagesToScan("com.codegen.camel.onorm.model");
    return localContainerEntityManagerFactoryBean;
  }
}
