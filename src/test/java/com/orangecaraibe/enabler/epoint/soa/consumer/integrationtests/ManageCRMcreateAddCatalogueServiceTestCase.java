package com.orangecaraibe.enabler.epoint.soa.consumer.integrationtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;

import com.orangecaraibe.enabler.AbstractBaseConsumerTestCase;
import com.orangecaraibe.enabler.InfoClient;
import com.orangecaraibe.enabler.UseCaseTestFindAndGetDAO;
import com.orangecaraibe.enabler.core.utils.Constantes;
import com.orangecaraibe.soa.v1_2.interfaces.managecrm.ManageCRM;

/**
 * @author sgodard
 */
@Deprecated
public class ManageCRMcreateAddCatalogueServiceTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource
	private ManageCRM manageCRMConsumer;

	@Resource
	private UseCaseTestFindAndGetDAO useCaseTestFindAndGetDAO;

	@Test
	public void testCreditIN_30E_PPS()
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomerPPS();

		String coId = infoClient.getCo_id();
		String customerId = infoClient.getCustomer_id();
		String tmCode = infoClient.getTmcode();
		String msisdn = infoClient.getMsisdn();
		String offerSpecificationCode = "AOAA_GesteCommercial30E_PPS";

		String response = manageCRMConsumer.createOrderAddCatalogueService( coId, msisdn, tmCode,
																			offerSpecificationCode, customerId );
		assertNotNull( "null value returned", response );
		assertTrue( response, response.substring( 0, 2 ).equals( Constantes.EPOINT_NO_ERROR_RETURN_CODE ) );

	}

	@Test
	public void testCreditIN_30E_PPD()
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomerPPD();

		String coId = infoClient.getCo_id();
		String customerId = infoClient.getCustomer_id();
		String tmCode = infoClient.getTmcode();
		String msisdn = infoClient.getMsisdn();
		String offerSpecificationCode = "AOAA_GesteCommercial30E_PPS";

		String response = manageCRMConsumer.createOrderAddCatalogueService( coId, msisdn, tmCode,
																			offerSpecificationCode, customerId );
		assertNotNull( "null value returned", response );
		assertTrue( response, response.substring( 0, 2 ).equals( Constantes.EPOINT_NO_ERROR_RETURN_CODE ) );

	}

	@Test
	public void testCreditINinvalidCoId()
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomerPPD();

		String coId = "0";
		String customerId = infoClient.getCustomer_id();
		String tmCode = infoClient.getTmcode();
		String msisdn = infoClient.getMsisdn();
		String offerSpecificationCode = "AOAA_GesteCommercial10E_PPD";
		String response = manageCRMConsumer.createOrderAddCatalogueService( coId, msisdn, tmCode,
																			offerSpecificationCode, customerId );

		assertNotNull( response );
		assertTrue( "code erreur non trouvé", response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );
	}

	// @Test
	public void testCreditINInvalidMsisdn()
	{
		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomerPPD();

		String coId = infoClient.getCo_id();
		String customerId = infoClient.getCustomer_id();
		String tmCode = infoClient.getTmcode();
		String msisdn = "0";
		String offerSpecificationCode = "AOAA_GesteCommercial10E_PPD";

		String response = manageCRMConsumer.createOrderAddCatalogueService( coId, msisdn, tmCode,
																			offerSpecificationCode, customerId );
		assertNotNull( response );
		assertTrue( "code erreur non trouvé", response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );
	}

	@Test
	public void testCreditINInvalidTmCode()
	{
		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomerPPD();

		String coId = infoClient.getCo_id();
		String customerId = infoClient.getCustomer_id();
		String tmCode = "";
		String msisdn = infoClient.getMsisdn();
		String offerSpecificationCode = "AOAA_GesteCommercial10E_PPD";

		String response = manageCRMConsumer.createOrderAddCatalogueService( coId, msisdn, tmCode,
																			offerSpecificationCode, customerId );
		assertNotNull( response );
		assertTrue( "code erreur non trouvé", response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );
	}

	@Test
	public void testCreditINInvalideInvalidOfferSpecificationCode()
	{
		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		String coId = infoClient.getCo_id();
		String customerId = infoClient.getCustomer_id();
		String tmCode = infoClient.getTmcode();
		String msisdn = infoClient.getMsisdn();
		String offerSpecificationCode = "";
		String response = manageCRMConsumer.createOrderAddCatalogueService( coId, msisdn, tmCode,
																			offerSpecificationCode, customerId );
		assertNotNull( response );
		assertTrue( "code erreur non trouvé", response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );
	}

}
