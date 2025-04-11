package com.orangecaraibe.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories( basePackages = "com.orangecaraibe.enabler.handset.data.bean.SimAppInfo",
				entityManagerFactoryRef = "simAppEntityManager", transactionManagerRef = "simAppTransactionManager" )
public class SimAppEntityManagerConfig
{

	@Autowired
	private Environment env;

	@Bean
	public DataSource simAppDataSource()
	{
		HikariDataSource dataSource = new HikariDataSource();

		// See: application.properties
		dataSource.setPoolName( "simAppDataSource" );
		dataSource.setDriverClassName( env.getProperty( "spring.datasource.driver-class-name" ) );
		dataSource.setJdbcUrl( env.getProperty( "enabler-crm-simapp.spring.datasource.url" ) );
		dataSource.setUsername( env.getProperty( "enabler-crm-simapp.spring.datasource.username" ) );
		dataSource.setPassword( env.getProperty( "enabler-crm-simapp.spring.datasource.password" ) );
		dataSource.setMaximumPoolSize( Integer.valueOf( env.getProperty( "enabler-crm-simapp.spring.datasource.maximun-pool-size" ) ) );
		dataSource.setMinimumIdle( Integer.valueOf( env.getProperty( "enabler-crm-simapp.spring.datasource.minimun-pool-size" ) ) );
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean simAppEntityManager()
	{
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource( simAppDataSource() );
		em.setPackagesToScan( new String[] { "com.orangecaraibe.enabler.handset.data.bean.SimAppInfo" } );

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter( vendorAdapter );
		em.setJpaProperties( additionalProperties() );

		return em;
	}

	private Properties additionalProperties()
	{

		Properties properties = new Properties();
		// See: application.properties
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
	public PlatformTransactionManager simAppTransactionManager()
	{

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory( simAppEntityManager().getObject() );
		return transactionManager;
	}
}
