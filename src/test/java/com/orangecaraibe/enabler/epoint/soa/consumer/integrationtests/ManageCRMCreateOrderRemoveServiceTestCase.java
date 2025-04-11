/**
 *
 */
package com.orangecaraibe.enabler.epoint.soa.consumer.integrationtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Test;

import com.orangecaraibe.enabler.AbstractBaseConsumerTestCase;
import com.orangecaraibe.enabler.InfoClient;
import com.orangecaraibe.enabler.UseCaseTestFindAndGetDAO;
import com.orangecaraibe.enabler.core.utils.Constantes;
import com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO.api.RemoveService;
import com.orangecaraibe.soa.v1_2.interfaces.managecrm.ManageCRM;

/**
 * @author sgodard
 */
@Deprecated
public class ManageCRMCreateOrderRemoveServiceTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource
	private ManageCRM manageCRMConsumer;

	@Resource
	private UseCaseTestFindAndGetDAO useCaseTestFindAndGetDAO;

	@Test
	public void testCreateOrderRemoveServiceXtrem()
	{
		// Renvoi sur messagerie
		initRemoveServiceForXtrem();
		if ( !removeServiceXtrem.isEmpty() )
		{
			// On melange la liste pour ne pas avoir toujours les memes cas
			Collections.shuffle( removeServiceXtrem );

			RemoveService removeService = removeServiceXtrem.get( 0 );

			String response =
				manageCRMConsumer.createOrderRemoveService( removeService.getCoId(), removeService.getMsisdn(),
															removeService.getTmCode(), removeService.getSnCode(),
															removeService.getSpCode(), removeService.getCustomerId() );
			assertNotNull( "null value returned", response );
			assertTrue( response, response.substring( 0, 2 ).equals( Constantes.EPOINT_NO_ERROR_RETURN_CODE ) );
		}
		else
		{
			fail( "la requête n'a retourné aucun résultat" );
		}
	}

	@Test
	public void testCreateOrderRemoveServicePref()
	{
		// Renvoi sur messagerie
		initRemoveServiceForPref();
		if ( !removeServicePref.isEmpty() )
		{
			// On melange la liste pour ne pas avoir toujours les memes cas
			Collections.shuffle( removeServicePref );

			RemoveService removeService = removeServicePref.get( 0 );

			String response =
				manageCRMConsumer.createOrderRemoveService( removeService.getCoId(), removeService.getMsisdn(),
															removeService.getTmCode(), removeService.getSnCode(),
															removeService.getSpCode(), removeService.getCustomerId() );
			assertNotNull( "null value returned", response );
			assertTrue( response, response.substring( 0, 2 ).equals( Constantes.EPOINT_NO_ERROR_RETURN_CODE ) );
		}
		else
		{
			fail( "la requête n'a retourné aucun résultat" );
		}
	}

	@Test
	public void testCreateOrderRemoveServiceFlotte()
	{
		// Renvoi sur messagerie
		initRemoveServiceFlotte();
		if ( !removeServiceFlotte.isEmpty() )
		{
			// On melange la liste pour ne pas avoir toujours les memes cas
			Collections.shuffle( removeServiceFlotte );

			RemoveService removeService = removeServiceFlotte.get( 0 );

			String response =
				manageCRMConsumer.createOrderRemoveService( removeService.getCoId(), removeService.getMsisdn(),
															removeService.getTmCode(), removeService.getSnCode(),
															removeService.getSpCode(), removeService.getCustomerId() );
			assertNotNull( "null value returned", response );
			assertTrue( response, response.substring( 0, 2 ).equals( Constantes.EPOINT_NO_ERROR_RETURN_CODE ) );
		}
		else
		{
			fail( "la requête n'a retourné aucun résultat" );
		}
	}

	@Test
	public void testCreateOrderRemoveServicePro()
	{
		// Renvoi sur messagerie
		initRemoveServicePro();
		if ( !removeServicePro.isEmpty() )
		{
			// On melange la liste pour ne pas avoir toujours les memes cas
			Collections.shuffle( removeServicePro );

			RemoveService removeService = removeServicePro.get( 0 );

			String response =
				manageCRMConsumer.createOrderRemoveService( removeService.getCoId(), removeService.getMsisdn(),
															removeService.getTmCode(), removeService.getSnCode(),
															removeService.getSpCode(), removeService.getCustomerId() );
			assertNotNull( "null value returned", response );
			assertTrue( response, response.substring( 0, 2 ).equals( Constantes.EPOINT_NO_ERROR_RETURN_CODE ) );
		}
		else
		{
			fail( "la requête n'a retourné aucun résultat" );
		}
	}

	@Test
	public void testCreateOrderRemoveServiceInvalidCoId()
	{
		RemoveService removeService = new RemoveService();
		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		String coId = infoClient.getCo_id();
		String customerId = infoClient.getCustomer_id();
		String tmCode = infoClient.getTmcode();
		String msisdn = infoClient.getMsisdn();

		removeService.setCoId( "0" );
		removeService.setMsisdn( "590690322666" );
		removeService.setTmCode( "267" );
		removeService.setSnCode( "23" );
		removeService.setSpCode( "6" );
		removeService.setCustomerId( "1874569" );

		String response =
			manageCRMConsumer.createOrderRemoveService( removeService.getCoId(), removeService.getMsisdn(),
														removeService.getTmCode(), removeService.getSnCode(),
														removeService.getSpCode(), removeService.getCustomerId() );
		assertNotNull( response );
		assertTrue( "code erreur non trouvé", response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );
	}

	@Test
	public void testCreateOrderRemoveServiceInvalidMsisdn()
	{
		RemoveService removeService = new RemoveService();
		removeService.setCoId( "1954541" );
		removeService.setMsisdn( "90322666" );
		removeService.setTmCode( "267" );
		removeService.setSnCode( "23" );
		removeService.setSpCode( "6" );
		removeService.setCustomerId( "1874569" );

		String response =
			manageCRMConsumer.createOrderRemoveService( removeService.getCoId(), removeService.getMsisdn(),
														removeService.getTmCode(), removeService.getSnCode(),
														removeService.getSpCode(), removeService.getCustomerId() );
		assertNotNull( response );
		assertTrue( "code erreur non trouvé", response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );
	}

	@Test
	public void testCreateOrderRemoveServiceInvalidTmCode()
	{
		RemoveService removeService = new RemoveService();
		removeService.setCoId( "1954541" );
		removeService.setMsisdn( "590690322666" );
		removeService.setTmCode( "-2" );
		removeService.setSnCode( "23" );
		removeService.setSpCode( "6" );
		removeService.setCustomerId( "1874569" );

		String response =
			manageCRMConsumer.createOrderRemoveService( removeService.getCoId(), removeService.getMsisdn(),
														removeService.getTmCode(), removeService.getSnCode(),
														removeService.getSpCode(), removeService.getCustomerId() );
		assertNotNull( response );
		assertTrue( "code erreur non trouvé", response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );
	}

	@Test
	public void testCreateOrderRemoveServiceInvalidSnCode()
	{
		RemoveService removeService = new RemoveService();
		removeService.setCoId( "1954541" );
		removeService.setMsisdn( "590690322666" );
		removeService.setTmCode( "267" );
		removeService.setSnCode( "0" );
		removeService.setSpCode( "6" );
		removeService.setCustomerId( "1874569" );

		String response =
			manageCRMConsumer.createOrderRemoveService( removeService.getCoId(), removeService.getMsisdn(),
														removeService.getTmCode(), removeService.getSnCode(),
														removeService.getSpCode(), removeService.getCustomerId() );
		assertNotNull( response );
		assertTrue( "code erreur non trouvé", response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );
	}

	@Test
	public void testCreateOrderRemoveServiceInvalidSpCode()
	{
		RemoveService removeService = new RemoveService();
		removeService.setCoId( "1954541" );
		removeService.setMsisdn( "590690322666" );
		removeService.setTmCode( "267" );
		removeService.setSnCode( "23" );
		removeService.setSpCode( "0" );
		removeService.setCustomerId( "1874569" );

		String response =
			manageCRMConsumer.createOrderRemoveService( removeService.getCoId(), removeService.getMsisdn(),
														removeService.getTmCode(), removeService.getSnCode(),
														removeService.getSpCode(), removeService.getCustomerId() );
		assertNotNull( response );
		assertTrue( "code erreur non trouvé", response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );
	}
}
