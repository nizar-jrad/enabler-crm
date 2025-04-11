package com.orangecaraibe;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan
@EntityScan
@EnableAsync
public class EnablerCrmConfiguration
{

	public static void main( String[] args )
	{
		SpringApplication.run( EnablerCrmConfiguration.class, args );
	}

	@Bean( name = Bus.DEFAULT_BUS_ID )
	public SpringBus springBus( @Autowired
	ApplicationContext applicationContext )
	{
		SpringBus bus = new SpringBus();
		bus.setId( "cxf" );
		bus.setApplicationContext( applicationContext );
		return bus;
	}
}
