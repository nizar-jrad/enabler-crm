/**
 *
 */
package com.orangecaraibe.enabler.epoint.soa.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.soa.v1_2.interfaces.managecrm.ManageCRM;
import com.orangecaraibe.soa.v2.interfaces.manageportability.InterfaceUnavailableException;

/**
 * @author sgodard EndPoint pour le WS ManageCRM
 */
@Deprecated
@Component
public class ManageCRMProvider
	implements ManageCRM
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageCRMProvider.class );

	@Autowired
	@Qualifier( "manageCRMService" )
	private ManageCRM manageCRMService;

	public ManageCRMProvider()
	{
		super();
		LOGGER.info( "Endpoint successfully initialized" );
	}

	@Override
	public String checkOutgoingRIO( String msisdn, String rio, String coId, String typeClient )
	{
		return manageCRMService.checkOutgoingRIO( msisdn, rio, coId, typeClient );
	}

	@Override
	public String getOutgoingRIO( String msisdn, String coId, String typeClient )
	{

		return manageCRMService.getOutgoingRIO( msisdn, coId, typeClient );
	}

	@Override
	public String createOrderRemoveService( String coId, String msisdn, String tmCode, String snCode, String spCode,
											String customerId )
	{
		return manageCRMService.createOrderRemoveService( coId, msisdn, tmCode, snCode, spCode, customerId );
	}

	@Override
	public String getClassifications( String tmCode, String snCode, String spCode )
		throws InterfaceUnavailableException, Exception
	{

		return manageCRMService.getClassifications( tmCode, snCode, spCode );
	}

	@Override
	public String getClassification( String tmCode, String snCode, String spCode, String libeller )
		throws InterfaceUnavailableException, Exception
	{

		return manageCRMService.getClassification( tmCode, snCode, spCode, libeller );
	}

	@Override
	public String creditIN( String coId, String msisdn, String tmCode, String offerSpecificationCode,
							String customerId )
	{

		return manageCRMService.creditIN( coId, msisdn, tmCode, offerSpecificationCode, customerId );

	}

	@Override
	public String gestofferCodeByAMount( String coId, String msisdn, String montant, String tmCode, String customerId )
	{

		return manageCRMService.gestofferCodeByAMount( coId, msisdn, montant, tmCode, customerId );
	}

	@Override
	public String createOrderAddCatalogueService(	String coId, String msisdn, String tmCode,
													String offerSpecificationCode, String customerId )
	{
		return manageCRMService.createOrderAddCatalogueService( coId, msisdn, tmCode, offerSpecificationCode,
																customerId );
	}

	@Override
	public String getPresenceCompteWebcare( String coId, String msisdn )
	{
		return manageCRMService.getPresenceCompteWebcare( coId, msisdn );
	}

	@Override
	public String findAndGetAccordInfoCommerciale( String coId, String customerId )
	{
		return manageCRMService.findAndGetAccordInfoCommerciale( coId, customerId );
	}

	@Override
	public String addOrRemoveAccordInfoCommerciale( String accord, String coId, String customerId )
	{
		return manageCRMService.addOrRemoveAccordInfoCommerciale( accord, coId, customerId );
	}
}
