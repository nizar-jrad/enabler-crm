package com.orangecaraibe.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@EnableTransactionManagement
@Configuration
@EnableJpaRepositories(
				basePackages = { "com.orangecaraibe.enabler.handset.data.bean",
					"com.orangecaraibe.enabler.crm.dao.bean" },
				entityManagerFactoryRef = "crmEntityManager", transactionManagerRef = "crmTransactionManager" )
public class CrmEntityManagerConfig
{

	@Autowired
	private Environment env;

	@Bean
	@Primary
	public DataSource crmDataSource()
	{
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setPoolName( "crmDataSource" );
		dataSource.setDriverClassName( env.getProperty( "spring.datasource.driver-class-name" ) );
		dataSource.setJdbcUrl( env.getProperty( "enabler-crm.spring.datasource.url" ) );
		dataSource.setUsername( env.getProperty( "enabler-crm.spring.datasource.username" ) );
		dataSource.setPassword( env.getProperty( "enabler-crm.spring.datasource.password" ) );
		dataSource.setMaximumPoolSize( Integer.valueOf( env.getProperty( "enabler-crm.spring.datasource.maximun-pool-size" ) ) );
		dataSource.setMinimumIdle( Integer.valueOf( env.getProperty( "enabler-crm.spring.datasource.minimun-pool-size" ) ) );
		return dataSource;
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean crmEntityManager()
	{
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource( crmDataSource() );
		em.setPackagesToScan( new String[] { "com.orangecaraibe.enabler.handset.data.bean",
			"com.orangecaraibe.enabler.crm.dao.bean" } );
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter( vendorAdapter );
		em.setJpaProperties( additionalProperties() );

		return em;
	}

	private Properties additionalProperties()
	{
		Properties properties = new Properties();
		properties.put( "hibernate.dialect", env.getProperty( "enabler-crm.spring.jpa.properties.hibernate.dialect" ) );
		properties.put( "hibernate.show_sql",
						env.getProperty( "enabler-crm.spring.jpa.properties.hibernate.show_sql" ) );
		properties.put( "current_session_context_class",
						env.getProperty( "spring.jpa.properties.hibernate.current_session_context_class" ) );
		properties.put( "hibernate.default_schema",
						env.getProperty( "enabler-crm.spring.jpa.properties.hibernate.default_schema" ) );
		return properties;
	}

	@Bean
	@Primary
	public PlatformTransactionManager crmTransactionManager()
	{
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory( crmEntityManager().getObject() );
		return transactionManager;
	}
}
