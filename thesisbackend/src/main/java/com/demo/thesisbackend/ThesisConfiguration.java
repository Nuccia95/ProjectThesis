package com.demo.thesisbackend;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.demo.thesisbackend" })
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager", basePackages = { "com.demo.thesisbackend.dao" })
public class ThesisConfiguration implements EnvironmentAware {
    private Environment env;

    @Bean
    public DataSource dataSource() {
	return DataSourceBuilder.create().driverClassName(env.getProperty("spring.datasource.driver-class-name")).url(env.getProperty("spring.datasource.url")).username(env.getProperty("spring.datasource.username"))
		.password(env.getProperty("spring.datasource.password")).build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder) {
	return builder.dataSource(dataSource())//
		.packages("shared.thesiscommon") //$NON-NLS-1$
		.persistenceUnit("thesiscommon") //$NON-NLS-1$
		.build();
    }

    @Override
    public void setEnvironment(final Environment env) {
	this.env = env;

    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(final EntityManagerFactory customerEntityManager) {
	return new JpaTransactionManager(customerEntityManager);
    }
}
