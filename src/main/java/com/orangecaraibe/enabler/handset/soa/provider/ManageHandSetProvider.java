package com.orangecaraibe.enabler.handset.soa.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.soa.v1_2.interfaces.managehandset.ManageHandSet;

@Component
public class ManageHandSetProvider
	extends BaseManageProvider
	implements ManageHandSet
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageHandSetProvider.class );

	@Autowired
	@Qualifier( "manageHandSetService" )
	private ManageHandSet manageHandSetService;

	public ManageHandSetProvider()
	{
		super();
		LOGGER.info( "Endpoint successfully initialized" );

	}

	@Override
	public String findAndGetMobileByImei( String imei )
	{
		return manageHandSetService.findAndGetMobileByImei( imei );
	}

}
