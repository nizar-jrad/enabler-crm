/**
 *
 */
package com.orangecaraibe.enabler.creditin.soa.consumer.integrationtests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;

import com.orangecaraibe.enabler.AbstractBaseConsumerTestCase;
import com.orangecaraibe.enabler.InfoClient;
import com.orangecaraibe.enabler.UseCaseTestFindAndGetDAO;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CheckOutgoingRIOException;
import com.orangecaraibe.soa.v1_2.interfaces.managecreditin.ManageCreditIN;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.CreateUpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.InterfaceUnavailableException;

/**
 * @author sgodard Classe de test pour le WS ManageCRM destiné à Epoint
 */
public class CreditINServiceTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource
	private ManageCreditIN manageCreditINConsumer;

	private static Double creditPrec;

	@Resource
	private UseCaseTestFindAndGetDAO useCaseTestFindAndGetDAO;

	@Test
	public void testGetCreditINOk()
		throws InterfaceUnavailableException, CheckOutgoingRIOException,
		com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		FindAndGetException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();
		String msisdn = infoClient.getMsisdn();
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testgetCreditINOk msisdn=" + msisdn );
		}
		Double credit = manageCreditINConsumer.getCreditIN( msisdn );
		assertNotNull( credit );
		System.out.println( "Le credit de communication est de :" + credit );
		creditPrec = credit;
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testgetCreditINOk credit=" + credit );
		}

	}

	@Test
	public void testSetCreditINOk()
		throws InterfaceUnavailableException, CheckOutgoingRIOException,
		com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		CreateUpdateException, FindAndGetException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();
		String msisdn = infoClient.getMsisdn();
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testgetCreditINOk msisdn=" + msisdn );
		}
		manageCreditINConsumer.setCreditIN( msisdn, 20.0, 30, 0 );
		Double credit = manageCreditINConsumer.getCreditIN( msisdn );
		System.out.println( "Le nouveau credit de communication est de :" + credit );
		assertTrue( credit > creditPrec );

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testgetCreditINOk credit=" + credit );
		}

	}

}
