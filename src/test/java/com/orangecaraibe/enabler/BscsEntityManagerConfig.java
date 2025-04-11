package com.orangecaraibe.enabler;

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
@EnableJpaRepositories(
				basePackages = { "com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO",
					"com.orangecaraibe.enabler.ussd.soa.consumer.integrationtestsDAO",
					"com.orangecaraibe.enabler.UseCaseTestFindAndGetDAO", "com.orangecaraibe.enabler.InfoClient" },
				entityManagerFactoryRef = "bscsEntityManager", transactionManagerRef = "bscsTransactionManager" )
public class BscsEntityManagerConfig
{

	@Autowired
	private Environment env;

	@Bean
	public DataSource bscsDataSource()
	{
		HikariDataSource dataSource = new HikariDataSource();

		// See: application.properties
		dataSource.setDriverClassName( env.getProperty( "spring.datasource.driver-class-name" ) );
		dataSource.setJdbcUrl( env.getProperty( "bscs.spring.datasource.url" ) );
		dataSource.setUsername( env.getProperty( "bscs.spring.datasource.username" ) );
		dataSource.setPassword( env.getProperty( "bscs.spring.datasource.password" ) );

		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean bscsEntityManager()
	{
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource( bscsDataSource() );
		em.setPackagesToScan( new String[] { "com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO",
			"com.orangecaraibe.enabler.ussd.soa.consumer.integrationtestsDAO",
			"com.orangecaraibe.enabler.UseCaseTestFindAndGetDAO", "com.orangecaraibe.enabler.InfoClient" } );
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter( vendorAdapter );
		em.setJpaProperties( additionalProperties() );

		return em;
	}

	private Properties additionalProperties()
	{

		Properties properties = new Properties();
		// See: application.properties
		properties.put( "hibernate.dialect", env.getProperty( "bscs.spring.jpa.properties.hibernate.dialect" ) );
		properties.put( "hibernate.show_sql", env.getProperty( "bscs.spring.jpa.properties.hibernate.show_sql" ) );
		properties.put( "current_session_context_class",
						env.getProperty( "spring.jpa.properties.hibernate.current_session_context_class" ) );
		properties.put( "hibernate.default_schema",
						env.getProperty( "bscs.spring.jpa.properties.hibernate.default_schema" ) );
		return properties;
	}

	@Bean
	public PlatformTransactionManager bscsTransactionManager()
	{

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory( bscsEntityManager().getObject() );
		return transactionManager;
	}
}
