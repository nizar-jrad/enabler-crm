/**
 *
 */
package com.orangecaraibe.enabler.ussd.soa.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.soa.v1_2.interfaces.manageussd.ManageUSSD;

/**
 * @author sgodard
 */
@Component
public class ManageUSSDProvider
	implements ManageUSSD
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageUSSDProvider.class );

	@Autowired
	@Qualifier( "manageUSSDService" )
	private ManageUSSD manageUSSDService;

	public ManageUSSDProvider()
	{
		super();
		LOGGER.info( "Endpoint successfully initialized" );

	}

	@Override
	public String getUSSDFormatedPortabilityInfo( String msisdn )
	{
		return manageUSSDService.getUSSDFormatedPortabilityInfo( msisdn );
	}

}
