/**
 *
 */
package com.orangecaraibe.enabler.ussd.webservice.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orangecaraibe.enabler.core.validator.ArgumentsValidator;
import com.orangecaraibe.enabler.ussd.business.service.USSDService;
import com.orangecaraibe.enabler.ussd.core.exception.TechnicalErrorException;
import com.orangecaraibe.enabler.ussd.core.exception.UnknownClientException;
import com.orangecaraibe.enabler.ussd.core.exception.UnknownMsisdnException;
import com.orangecaraibe.enabler.ussd.core.util.ConstantesUSSD;
import com.orangecaraibe.soa.v1_2.interfaces.manageussd.ManageUSSD;

/**
 * @author sgodard Implémentation du webservice pour ManageUSSD
 */
@Service( "manageUSSDService" )
public class ManageUSSDServiceImpl
	implements ManageUSSD
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageUSSDServiceImpl.class );

	/** Service Business */
	@Autowired
	@Qualifier( "ussdService" )
	private USSDService ussdService;

	@Autowired
	private ArgumentsValidator portabilityValidator;

	@Override
	public String getUSSDFormatedPortabilityInfo( String msisdn )
	{
		String messages = "";
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Begin Webservice getUSSDFormatedPortabilityInfo -- msisdn" + msisdn );
		}

		boolean msisdnCheck = portabilityValidator.checkMsisdn( msisdn );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Webservice getUSSDFormatedPortabilityInfo-- msisdncheck=" + msisdnCheck );
		}
		try
		{

			if ( msisdnCheck )
			{
				messages = ussdService.getUSSDFormatedPortabilityInfo( msisdn );
			}
			else
			{
				messages = ConstantesUSSD.ERROR_CODE_UNKNOWN_MSISDN;

			}

		}
		catch ( TechnicalErrorException e )
		{

			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: erreur technique", e );
			}
			messages = ConstantesUSSD.ERROR_CODE_TECHNICAL_ERROR;
		}

		catch ( UnknownClientException e )
		{

			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: compte inconnu", e );
			}
			messages = ConstantesUSSD.ERROR_CODE_UNKNOWN_CLIENT;
		}
		catch ( UnknownMsisdnException e )
		{

			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: contract inconnu", e );
			}
			messages = ConstantesUSSD.ERROR_CODE_UNKNOWN_MSISDN;
		}
		catch ( RuntimeException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: erreur technique", e );
			}
			messages = ConstantesUSSD.ERROR_CODE_TECHNICAL_ERROR;
		}

		if ( LOGGER.isDebugEnabled() && messages != null )
		{
			LOGGER.debug( "End Webservice getUSSDFormatedPortabilityInfo-- message: " + messages );
		}
		return messages;
	}

}
