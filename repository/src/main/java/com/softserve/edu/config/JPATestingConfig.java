package com.softserve.edu.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.softserve.edu.repository")
@ComponentScan("com.softserve.edu.entity")
@PropertySource(value = "classpath:database.properties")
public class JPATestingConfig {
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driverClassName"));
        dataSource.setUrl(env.getProperty("db.url.test"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        dataSource.setConnectionProperties(env.getProperty("db.connectionProperties"));
        dataSource.setInitialSize(env.getProperty("db.initialSize", Integer.class));
        dataSource.setMinIdle(env.getProperty("db.minIdle", Integer.class));
        dataSource.setMaxIdle(env.getProperty("db.maxIdle", Integer.class));
        dataSource.setMaxTotal(env.getProperty("db.maxTotal", Integer.class));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(env.getProperty("em.packagesToScan"));
        entityManagerFactoryBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties());
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory,
                                                         DataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    private Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", env.getProperty("hb.dialect"));
        jpaProperties.put("hibernate.show_sql", env.getProperty("hb.showSql"));
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hb.hbm2ddl.auto"));
        jpaProperties.put("hibernate.format_sql", env.getProperty("hb.formatSql"));
        jpaProperties.put("hibernate.use_sql_comments", env.getProperty("hb.sqlComment"));
        jpaProperties.put("hibernate.enable_lazy_load_no_trans", env.getProperty("hb.enableLazyLoadNoTrans"));
        return jpaProperties;
    }
}
