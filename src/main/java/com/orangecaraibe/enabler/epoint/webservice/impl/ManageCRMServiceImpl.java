/**
 *
 */
package com.orangecaraibe.enabler.epoint.webservice.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orangecaraibe.enabler.core.catalogue.CatalogueHelper;
import com.orangecaraibe.enabler.core.utils.Constantes;
import com.orangecaraibe.enabler.core.validator.ArgumentsValidator;
import com.orangecaraibe.enabler.epoint.business.service.CRMService;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.AddOrRemoveAccordInfoCommercialeException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CheckOutgoingRIOException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CreateOrderAddCatalogueServiceException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CreateOrderCreditINException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CreateOrderRemoveServiceException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.FindAndGetAccordInfoCommercialeException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.GetPresenceCompteWebcareException;
import com.orangecaraibe.soa.v1_2.interfaces.managecrm.ManageCRM;
import com.orangecaraibe.soa.v2.interfaces.manageportability.GetOutgoingRIOException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.InterfaceUnavailableException;

/**
 * @author sgodard Implémentation du webservice pour ManageCRM
 */
@Deprecated
@Service( "manageCRMService" )
public class ManageCRMServiceImpl
	implements ManageCRM
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageCRMServiceImpl.class );

	/** Service Business */
	@Autowired
	@Qualifier( "crmService" )
	private CRMService crmService;

	@Autowired
	private ArgumentsValidator argumentsValidator;

	/**	 */
	@Autowired
	@Qualifier( "crm.catalogueHelper" )
	private CatalogueHelper catalogueHelper;

	@Override
	public String getOutgoingRIO( String msisdn, String coId, String typeClient )
	{
		String rio = null;

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Begin Webservice getOutgoingRIO-- msisdn=" + msisdn + ",  coId=" + coId + ", type client="
				+ typeClient );
		}

		boolean msisdnCheck = argumentsValidator.checkMsisdn( msisdn );
		boolean coIdCheck = argumentsValidator.check( coId );
		boolean typeClientCheck = argumentsValidator.checkCustomerType( typeClient );

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "msisdnChek:" + msisdnCheck + "- coIdCheck:" + coIdCheck + "- typeClientCheck:"
				+ typeClientCheck );
		}

		try
		{
			if ( !msisdnCheck )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "Invalid msisdn" );
				}
				return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_MSISDN;
			}
			else if ( !coIdCheck )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "Invalid co id" );
				}
				return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_COID;
			}
			else if ( !typeClientCheck )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "Invalid customer type" );
				}
				return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_CLIENT_TYPE;
			}
			else
			{
				rio = crmService.getOutgoingRIO( msisdn, coId, typeClient );
				if ( LOGGER.isDebugEnabled() )
				{
					LOGGER.debug( "End Webservice getOutgoingRIO--RIO=" + rio );
				}
				return rio;
			}
		}
		catch ( GetOutgoingRIOException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: get out going RIO error", e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_RIO;
		}
		catch ( InterfaceUnavailableException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: interface error", e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_INTERFACE;
		}
		catch ( RuntimeException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué", e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_TECHNICAL;
		}

	}

	@Override
	public String checkOutgoingRIO( String msisdn, String rio, String coId, String typeClient )
	{
		boolean response = false;

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Begin Webservice checkOutgoingRIO" );
		}

		boolean msisdnCheck = argumentsValidator.checkMsisdn( msisdn );
		boolean coIdCheck = argumentsValidator.checkCoIdEmpty( coId );
		boolean typeClientCheck = argumentsValidator.checkCustomerType( typeClient );

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "msisdnChek:" + msisdnCheck + "- coIdCheck:" + coIdCheck + "- typeClientCheck:"
				+ typeClientCheck );
		}

		try
		{
			if ( !msisdnCheck )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "Invalid msisdn" );
				}
				return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_MSISDN;
			}
			else if ( !coIdCheck )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "Invalid co id" );
				}
				return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_COID;
			}
			else if ( !typeClientCheck )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "Invalid customer type" );
				}
				return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_CLIENT_TYPE;
			}
			else
			{
				response = crmService.checkOutgoingRIO( msisdn, rio, coId, typeClient );
				if ( LOGGER.isDebugEnabled() )
				{
					LOGGER.debug( "End Webservice checkOutgoingRIO--response=" + response );
				}

				return Boolean.toString( response );
			}
		}
		catch ( InterfaceUnavailableException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: interface error", e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_INTERFACE;
		}
		catch ( CheckOutgoingRIOException e )
		{

			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: checkoutgoing error", e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_TECHNICAL;
		}
		catch ( RuntimeException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué", e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_TECHNICAL;
		}

	}

	@Override
	public String createOrderAddCatalogueService(	String coId, String msisdn, String tmCode,
													String offerSpecificationCode, String customerId )
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - createOrderAddCatalogueService() - Begin" );
		}
		boolean coIdCheck = argumentsValidator.check( coId );
		boolean msisdnCheck = argumentsValidator.checkMsisdn( msisdn );
		boolean tmCodeCheck = argumentsValidator.check( tmCode );
		boolean offerCodeCheck = argumentsValidator.checkString( offerSpecificationCode );
		boolean custIdCheck = argumentsValidator.check( customerId );

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Input Validation: coIdCheck:" + coIdCheck + " - msisdnChek:" + msisdnCheck
				+ " - tmCodeCheck:" + tmCodeCheck + " - offerCodeCheck:" + offerCodeCheck + " - custIdCheck:"
				+ custIdCheck );
		}

		if ( !coIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_COID;
		}
		else if ( !msisdnCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_MSISDN;
		}
		else if ( !tmCodeCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_TMCODE;
		}
		else if ( !offerCodeCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_OFFERCODE;
		}
		else if ( !custIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_CUSTID;
		}
		try
		{

			crmService.createOrderAddCatalogueService( coId, msisdn, tmCode, offerSpecificationCode, customerId );

		}
		catch ( CreateOrderAddCatalogueServiceException e )
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( e.getMessage(), e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_TECHNICAL + ":" + e.getMessage();
		}
		catch ( com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException e )
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( e.getMessage(), e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE_2 + Constantes.EPOINT_ERROR_TIMEOUT + ":"
				+ Constantes.EPOINT_ERROR_INTERFACE + ":" + e.getMessage();
		}

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - createOrderAddCatalogueService() - End" );
		}

		return Constantes.EPOINT_NO_ERROR_RETURN_CODE + Constantes.EPOINT_RETURN_CODE_OK;

	}

	/**
	 * Supprimer un service sur un contrat donné La suppression se traduit par la création et l'envoi d'un order de
	 * suppression d'option
	 */
	@Override
	public String createOrderRemoveService( String coId, String msisdn, String tmCode, String snCode, String spCode,
											String customerId )
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - createOrderRemoveService() - Begin" );
		}

		boolean coIdCheck = argumentsValidator.check( coId );
		boolean msisdnCheck = argumentsValidator.checkMsisdn( msisdn );
		boolean tmCodeCheck = argumentsValidator.check( tmCode );
		boolean snCodeCheck = argumentsValidator.check( snCode );
		boolean spCodeCheck = argumentsValidator.check( spCode );
		boolean custIdCheck = argumentsValidator.check( customerId );

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Input Validation: coIdCheck:" + coIdCheck + " - msisdnChek:" + msisdnCheck
				+ " - tmCodeCheck:" + tmCodeCheck + " - snCodeCheck:" + snCodeCheck + " - spCodeCheck" + spCodeCheck );
		}

		if ( !coIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_COID;
		}
		else if ( !msisdnCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_MSISDN;
		}
		else if ( !tmCodeCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_TMCODE;
		}
		else if ( !snCodeCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_SNCODE;
		}
		else if ( !spCodeCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_SPCODE;
		}
		else if ( !custIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_CUSTID;
		}
		try
		{

			crmService.createOrderRemoveService( coId, msisdn, tmCode, snCode, spCode, customerId );

		}
		catch ( CreateOrderRemoveServiceException e )
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( e.getMessage(), e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_TECHNICAL + ":" + e.getMessage();
		}
		catch ( com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException e )
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( e.getMessage(), e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE_2 + Constantes.EPOINT_ERROR_TIMEOUT + ":"
				+ Constantes.EPOINT_ERROR_INTERFACE + ":" + e.getMessage();
		}

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - createOrderRemoveService() - End" );
		}

		return Constantes.EPOINT_NO_ERROR_RETURN_CODE + Constantes.EPOINT_RETURN_CODE_OK;
	}

	/**
	 * Récupere la liste des classification pour un tmcode, un snCode ou un spCode
	 */
	@Override
	public String getClassifications( String tmCode, String snCode, String spCode )
		throws InterfaceUnavailableException, Exception
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Input getClassification: tmCode:" + tmCode + " - snCode:" + snCode + " - spCode:" + spCode );
		}
		String reponse = crmService.getClasifications( tmCode, snCode, spCode );
		return reponse;
	}

	/**
	 * Récupere la classification "libeller" pour un tmcode, un snCode ou un spCode
	 */
	@Override
	public String getClassification( String tmCode, String snCode, String spCode, String libeller )
		throws InterfaceUnavailableException, Exception
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Input getClassification: tmCode:" + tmCode + " - snCode:" + snCode + " - spCode:" + spCode );
		}
		String reponse = crmService.getClasification( tmCode, snCode, spCode, libeller );
		return reponse;
	}

	/**
	 * Method epour lajout de credit par offerspecificaitonCode
	 * 
	 * @param coId le co id pour le quel il faut ajouter le credit
	 * @param msisdn le msisdn pour le quel il faut ajouter le credit
	 * @param tmCode le tmcode de l'offre
	 * @param offerSpecificationCode l'offer specification associer au tmcode
	 * @param customerId le customerId
	 */
	@Override
	public String creditIN( String coId, String msisdn, String tmCode, String offerSpecificationCode,
							String customerId )

	{

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - creditINByOfferSpecificationCode() - Begin" );
		}
		boolean coIdCheck = argumentsValidator.check( coId );
		boolean msisdnCheck = argumentsValidator.checkMsisdn( msisdn );
		boolean tmCodeCheck = argumentsValidator.check( tmCode );
		boolean offerCodeCheck = argumentsValidator.checkString( offerSpecificationCode );
		boolean custIdCheck = argumentsValidator.check( customerId );

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Input Validation: coIdCheck:" + coIdCheck + " - msisdnChek:" + msisdnCheck
				+ " - tmCodeCheck:" + tmCodeCheck + " - offerCodeCheck:" + offerCodeCheck + " - custIdCheck:"
				+ custIdCheck );
		}

		if ( !coIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_COID;
		}
		else if ( !msisdnCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_MSISDN;
		}
		else if ( !tmCodeCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_TMCODE;
		}
		else if ( !offerCodeCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_OFFERCODE;
		}
		else if ( !custIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_CUSTID;
		}
		try
		{

			crmService.creditIN( coId, msisdn, tmCode, offerSpecificationCode, customerId );

		}
		catch ( CreateOrderCreditINException e )
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( e.getMessage(), e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_TECHNICAL + ":" + e.getMessage();
		}
		catch ( com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException e )
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( e.getMessage(), e );
			}
			return Constantes.EPOINT_ERROR_RETURN_CODE_2 + Constantes.EPOINT_ERROR_TIMEOUT + ":"
				+ Constantes.EPOINT_ERROR_INTERFACE + ":" + e.getMessage();
		}

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - creditIN() - End" );
		}

		return Constantes.EPOINT_NO_ERROR_RETURN_CODE + Constantes.EPOINT_RETURN_CODE_OK;

	}

	/**
	 * Method epour lajout de credit par montant de credit
	 * 
	 * @param coId le co id pour le quel il faut ajouter le credit
	 * @param msisdn le msisdn pour le quel il faut ajouter le credit
	 * @param tmCode le tmcode de l'offre
	 * @param montant le montant a credite
	 * @param customerId le customerId
	 */
	@Override
	public String gestofferCodeByAMount( String coId, String msisdn, String montant, String tmCode, String customerId )
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - creditINByOfferSpecificationCode() - Begin" );
		}
		boolean coIdCheck = argumentsValidator.check( coId );
		boolean msisdnCheck = argumentsValidator.checkMsisdn( msisdn );
		boolean tmCodeCheck = argumentsValidator.check( tmCode );
		boolean amountCheck = argumentsValidator.checkString( montant );
		boolean custIdCheck = argumentsValidator.check( customerId );

		String offerSpecificationCode = "";

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Input Validation: coIdCheck:" + coIdCheck + " - msisdnChek:" + msisdnCheck
				+ " - tmCodeCheck:" + tmCodeCheck + " - offerCodeCheck:" + amountCheck + " - custIdCheck:"
				+ custIdCheck );
		}

		if ( !coIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_COID;
		}
		else if ( !msisdnCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_MSISDN;
		}
		else if ( !tmCodeCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_TMCODE;
		}
		else if ( !amountCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_AMOUNT;
		}
		else if ( !custIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_CUSTID;
		}
		try
		{

			String OfferSpecificationCode =
				crmService.getOfferCodeSpecficationCode( coId, msisdn, montant, tmCode, customerId );

		}
		catch ( Exception e )
		{

		}

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Business createOrderRemoveService() - End" );
		}

		return offerSpecificationCode;
	}

	/**
	 * Vérifier la présence d'un compte webcare
	 */
	@Override
	public String getPresenceCompteWebcare( String coId, String msisdn )
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - getPresenceCompteWebcare() - Begin" );
		}
		boolean coIdCheck = argumentsValidator.check( coId );
		boolean msisdnCheck = argumentsValidator.checkMsisdn( msisdn );

		if ( !coIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE_0 + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_COID;
		}
		else if ( !msisdnCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE_0 + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_MSISDN;
		}

		boolean exists = false;

		try
		{
			exists = crmService.getPresenceCompteWebcare( coId, msisdn );
		}
		catch ( GetPresenceCompteWebcareException e )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE_0 + Constantes.EPOINT_ERROR_EXCEPTION + ":" + e.getMessage();
		}

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - getPresenceCompteWebcare() - End" );
		}

		if ( exists )
		{
			return Constantes.EPOINT_NO_ERROR_RETURN_CODE + Constantes.EPOINT_PRESENT_RETURN_CODE;
		}
		else
		{
			return Constantes.EPOINT_NO_ERROR_RETURN_CODE + Constantes.EPOINT_NOT_PRESENT_RETURN_CODE;
		}
	}

	/**
	 * Rechercher l'accord sur l'envoi d'informations commerciales
	 */
	@Override
	public String findAndGetAccordInfoCommerciale( String coId, String customerId )
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - findAndGetAccordInfoCommerciale() - Begin" );
		}
		boolean coIdCheck = argumentsValidator.check( coId );
		boolean customerIdCheck = argumentsValidator.check( customerId );

		if ( !coIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE_0 + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_COID;
		}
		else if ( !customerIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE_0 + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_CUSTOMERID;
		}

		boolean exists = false;

		try
		{
			exists = crmService.findAndGetAccordInfoCommerciale( coId, customerId );
		}
		catch ( FindAndGetAccordInfoCommercialeException e )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE_0 + Constantes.EPOINT_ERROR_EXCEPTION + ":" + e.getMessage();
		}

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - findAndGetAccordInfoCommerciale() - End" );
		}

		if ( exists )
		{
			return Constantes.EPOINT_NO_ERROR_RETURN_CODE + Constantes.EPOINT_PRESENT_RETURN_CODE;
		}
		else
		{
			return Constantes.EPOINT_NO_ERROR_RETURN_CODE + Constantes.EPOINT_NOT_PRESENT_RETURN_CODE;
		}
	}

	/**
	 * Mise à jour de l'accord sur l'envoi d'informations commerciales
	 */
	@Override
	public String addOrRemoveAccordInfoCommerciale( String accord, String coId, String customerId )
	{
		String channel = "EPOINT";

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - addOrRemoveAccordInfoCommerciale() - Begin" );
		}
		boolean coIdCheck = argumentsValidator.check( coId );
		boolean customerIdCheck = argumentsValidator.check( customerId );

		if ( !coIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE_0 + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_COID;
		}
		else if ( !customerIdCheck )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE_0 + Constantes.EPOINT_ERROR_EXCEPTION + ":"
				+ Constantes.EPOINT_ERROR_CUSTOMERID;
		}

		boolean update = false;

		try
		{
			update = crmService.addOrRemoveAccordInfoCommerciale( accord.equals( "1" ), coId, customerId, channel );
		}
		catch ( AddOrRemoveAccordInfoCommercialeException e )
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE_0 + Constantes.EPOINT_ERROR_EXCEPTION + ":" + e.getMessage();
		}

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "WS ManageCRMService - addOrRemoveAccordInfoCommerciale() - End" );
		}

		if ( update )
		{
			return Constantes.EPOINT_NO_ERROR_RETURN_CODE + Constantes.EPOINT_RETURN_CODE_OK;
		}
		else
		{
			return Constantes.EPOINT_ERROR_RETURN_CODE_0 + Constantes.EPOINT_ERROR_BILLING_ACCOUNT;
		}
	}

}
