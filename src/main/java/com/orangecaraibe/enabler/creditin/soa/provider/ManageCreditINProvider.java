/**
 *
 */
package com.orangecaraibe.enabler.creditin.soa.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.soa.v1_2.interfaces.managecreditin.ManageCreditIN;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.CreateUpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException;

/**
 * @author gjospitre EndPoint pour le WS ManageCReditIN
 */
@Component
public class ManageCreditINProvider
	implements ManageCreditIN
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageCreditINProvider.class );

	@Autowired
	@Qualifier( "manageCreditINService" )
	private ManageCreditIN manageCreditINService;

	public ManageCreditINProvider()
	{
		super();
		LOGGER.info( "Endpoint successfully initialized" );
	}

	@Override
	public void setCreditIN( String msisdn, double amount, int daysActive, int daysInactive )
		throws InterfaceUnavailableException, CreateUpdateException
	{
		manageCreditINService.setCreditIN( msisdn, amount, daysActive, daysInactive );
	}

	@Override
	public Double getCreditIN( String msisdn )
		throws InterfaceUnavailableException, FindAndGetException
	{
		return manageCreditINService.getCreditIN( msisdn );
	}

	public void setManageCreditINService( ManageCreditIN manageCreditINService )
	{
		this.manageCreditINService = manageCreditINService;
	}

}
