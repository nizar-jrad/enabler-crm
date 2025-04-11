package com.orangecaraibe.enabler.svi.webservice.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.orangecaraibe.enabler.core.utils.Tools;
import com.orangecaraibe.enabler.core.validator.ArgumentsValidator;
import com.orangecaraibe.enabler.svi.business.beans.FormattedPortabilite;
import com.orangecaraibe.enabler.svi.business.service.SVIService;
import com.orangecaraibe.enabler.svi.core.exception.TechnicalErrorException;
import com.orangecaraibe.enabler.svi.core.exception.UnknownClientException;
import com.orangecaraibe.enabler.svi.core.exception.UnknownMsisdnException;
import com.orangecaraibe.enabler.svi.core.util.ConstantesSVI;
import com.orangecaraibe.soa.v1_2.interfaces.managesvi.ManageSVI;

/**
 * @author sgodard Implémentation du webservice pour ManageSVI
 */
@Service( "manageSVIService" )
public class ManageSVIServiceImpl
	implements ManageSVI
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageSVIServiceImpl.class );

	/** Service Business */
	@Autowired
	@Qualifier( "sviService" )
	private SVIService sviService;

	@Autowired
	private ArgumentsValidator portabilityValidator;

	@Override
	public String getSVIFormattedPortabilityInfo( String msisdn )
	{
		FormattedPortabilite portabilityInfo = new FormattedPortabilite();
		String messages = "";
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Begin Webservice getSVIFormatedPortabilityInfo -- msisdn" + msisdn );
		}

		boolean msisdnCheck = portabilityValidator.checkMsisdn( msisdn );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Webservice getSVIFormatedPortabilityInfo-- msisdncheck=" + msisdnCheck );
		}
		try
		{

			if ( msisdnCheck )
			{
				portabilityInfo = sviService.getSVIFormattedPortabilityInfo( msisdn );
				portabilityInfo.setStatusCode(ConstantesSVI.ERROR_CODE_OK);
				
			}
			else
			{
				portabilityInfo.setStatusCode(ConstantesSVI.ERROR_CODE_UNKNOWN_MSISDN);
				portabilityInfo.setSms(ConstantesSVI.ERROR_MSG_UNKNOWN_MSISDN);
			}

		}
		catch ( TechnicalErrorException e )
		{

			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: erreur technique", e );
			}
			portabilityInfo.setStatusCode(ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR);
			portabilityInfo.setSms(ConstantesSVI.ERROR_MSG_TECHNICAL_ERROR);
		}

		catch ( UnknownClientException e )
		{

			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: compte inconnu", e );
			}
			portabilityInfo.setStatusCode(ConstantesSVI.ERROR_CODE_UNKNOWN_CLIENT);
			portabilityInfo.setSms(ConstantesSVI.ERROR_MSG_UNKNOWN_CLIENT);
		}
		catch ( UnknownMsisdnException e )
		{

			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: contract inconnu", e );
			}
			portabilityInfo.setStatusCode(ConstantesSVI.ERROR_CODE_UNKNOWN_MSISDN);
			portabilityInfo.setSms(ConstantesSVI.ERROR_MSG_UNKNOWN_MSISDN);
		}
		catch ( RuntimeException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: erreur technique", e );
			}
			portabilityInfo.setStatusCode(ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR);
			portabilityInfo.setSms(ConstantesSVI.ERROR_MSG_TECHNICAL_ERROR);
		}

		if ( null == portabilityInfo.getStatusCode())
		{
			portabilityInfo.setStatusCode("200");
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End Webservice getSVIFormatedPortabilityInfo-- message: " + messages );
			}
		}
		String reponse;
		try {
			reponse = Tools.jsonMappingWrite(LOGGER, portabilityInfo);
		} catch (JsonProcessingException e) {
			reponse = String.format("{\"SMS\":\"%s\",\"StatusCode\":\"%s\"}",ConstantesSVI.ERROR_MSG_TECHNICAL_ERROR,ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR);
		}
		return reponse;
	}
}
