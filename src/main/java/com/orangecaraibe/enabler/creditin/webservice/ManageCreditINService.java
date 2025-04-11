/**
 *
 */
package com.orangecaraibe.enabler.creditin.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orangecaraibe.enabler.core.validator.ArgumentsValidator;
import com.orangecaraibe.enabler.creditin.business.service.CreditINService;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.CreateUpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException;

/**
 * @author gjospitre Implémentation du webservice pour Manage credit IN
 */
@Service( "manageCreditINService" )
public class ManageCreditINService
	implements com.orangecaraibe.soa.v1_2.interfaces.managecreditin.ManageCreditIN
{
	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageCreditINService.class );

	/** Service Business */
	@Autowired
	@Qualifier( "creditINService" )
	private CreditINService creditINService;

	@Autowired
	private ArgumentsValidator argumentsValidator;

	@Override
	public void setCreditIN( String msisdn, double amount, int daysActive, int daysInactive )
		throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		CreateUpdateException
	{

		if ( argumentsValidator.checkMsisdn( msisdn ) )
		{
			creditINService.setCreditIN( msisdn, amount, daysActive, daysInactive );
		}
		else
		{
			LOGGER.error( "Failed on setCreditIN for {} not validate", msisdn );
		}

	}

	@Override
	public Double getCreditIN( String msisdn )
		throws InterfaceUnavailableException, FindAndGetException
	{
		if ( argumentsValidator.checkMsisdn( msisdn ) )
		{
			return creditINService.getCreditIN( msisdn );
		}
		else
		{
			LOGGER.error( "Failed on getCreditIN for {} not validate", msisdn );
			return null;
		}
	}

}
