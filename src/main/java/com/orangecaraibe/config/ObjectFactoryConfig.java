package com.orangecaraibe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ObjectFactoryConfig
{

	@Bean( name = { "objectFactoryDCRM" } )
	@Lazy( true )
	public com.orange.sidom.soa.dcrm.datacontract.ObjectFactory objectFactoryDCRM()
	{
		return new com.orange.sidom.soa.dcrm.datacontract.ObjectFactory();
	}

	@Bean( name = { "objectFactoryRegisterCard" } )
	@Lazy( true )
	public fcm.gp.oc.schemas.dunning.data.dunningregistercard.ObjectFactory objectFactoryRegisterCard()
	{
		return new fcm.gp.oc.schemas.dunning.data.dunningregistercard.ObjectFactory();
	}

	@Bean( name = { "objectFactoryWorkLoad" } )
	@Lazy( true )
	public fcm.gp.oc.schemas.dunning.data.dunningworkitem.ObjectFactory objectFactoryWorkLoad()
	{
		return new fcm.gp.oc.schemas.dunning.data.dunningworkitem.ObjectFactory();
	}

	@Bean( name = { "objectFactoryMicrosoft" } )
	@Lazy( true )
	public com.microsoft.schemas._2003._10.serialization.ObjectFactory objectFactoryMicrosoft()
	{
		return new com.microsoft.schemas._2003._10.serialization.ObjectFactory();
	}

	@Bean( name = { "objectfactoryWSTroubleTicket" } )
	@Lazy( true )
	public com.orange.drm.interfaces.webserviceoreka.v1.ObjectFactory objectfactoryWSTroubleTicket()
	{
		return new com.orange.drm.interfaces.webserviceoreka.v1.ObjectFactory();
	}

	@Bean( name = { "objectFactoryOceane" } )
	@Lazy( true )
	public com.orange.interfaces.managetroubleticket.v2.ObjectFactory objectFactoryOceane()
	{
		return new com.orange.interfaces.managetroubleticket.v2.ObjectFactory();
	}

	@Bean( name = { "objectFactoryOceaneWrapper" } )
	@Lazy( true )
	public com.orange.interfaces.managetroubleticket.v2.root.ObjectFactory objectFactoryOceaneWrapper()
	{
		return new com.orange.interfaces.managetroubleticket.v2.root.ObjectFactory();
	}

}
