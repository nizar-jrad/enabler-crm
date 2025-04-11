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
public class ManageCRMpresenceWebcareTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource
	private ManageCRM manageCRMConsumer;

	@Resource
	private UseCaseTestFindAndGetDAO useCaseTestFindAndGetDAO;

	@Test
	public void testPresenceCompte()
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		String coId = infoClient.getCo_id();
		String msisdn = infoClient.getMsisdn();

		String response = manageCRMConsumer.getPresenceCompteWebcare( coId, msisdn );
		assertNotNull( "null value returned", response );
		assertTrue( response, response.substring( 0, 2 ).equals( Constantes.EPOINT_NO_ERROR_RETURN_CODE ) );

	}

}
