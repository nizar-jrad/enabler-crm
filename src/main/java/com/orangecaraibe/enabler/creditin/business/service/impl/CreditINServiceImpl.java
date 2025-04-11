/**
 *
 */
package com.orangecaraibe.enabler.creditin.business.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orangecaraibe.enabler.creditin.business.service.CreditINService;
import com.orangecaraibe.enabler.externalws.services.ExternalWSService;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.CreateUpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException;

/**
 * @author gjospitre Implémentation de la couche business pour le WS Manage Credit IN
 */
@Service( "creditINService" )
public class CreditINServiceImpl
	implements CreditINService
{
	/** The Constant log. */
	private static final Logger LOGGER = LoggerFactory.getLogger( CreditINServiceImpl.class );

	/**
	 * Service vers les WS externes
	 */
	@Autowired
	@Qualifier( "externalWSService" )
	ExternalWSService externalWSService;

	@Override
	public Double getCreditIN( String msisdn )
		throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		FindAndGetException
	{
		LOGGER.info( "Appel WS de récupération du crédit du contract" );
		return externalWSService.getContractCredit( msisdn );
	}

	@Override
	public void setCreditIN( String msisdn, Double amount, int daysActive, int daysInactive )
		throws InterfaceUnavailableException, CreateUpdateException
	{
		LOGGER.info( "Appel WS d'ajout de crédit de communication" );
		externalWSService.setCreditIN( msisdn, amount, daysActive, daysInactive );
	}

	/**
	 * @param externalWSService
	 */
	public void setExternalWSService( ExternalWSService externalWSService )
	{
		this.externalWSService = externalWSService;
	}

}
