package com.orangecaraibe.enabler.svi.soa.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.orangecaraibe.soa.v1_2.interfaces.managesvi.ManageSVI;

/**
 * @author sgodard
 */
@Component
public class ManageSVIProvider
	implements ManageSVI
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageSVIProvider.class );

	@Autowired
	@Qualifier( "manageSVIService" )
	private ManageSVI manageSVIService;

	public ManageSVIProvider()
	{
		super();
		LOGGER.info( "Endpoint successfully initialized" );

	}

	@Override
	public String getSVIFormattedPortabilityInfo( String msisdn )
	{
		return manageSVIService.getSVIFormattedPortabilityInfo( msisdn );
	}
}
