/**
 *
 */
package com.orangecaraibe.enabler.ussd.soa.consumer.integrationtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Collections;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.orangecaraibe.enabler.AbstractBaseConsumerTestCase;
import com.orangecaraibe.enabler.ussd.core.util.ConstantesUSSD;
import com.orangecaraibe.soa.v1_2.interfaces.manageussd.ManageUSSD;

/**
 * @author sgodard Classe de test pour le WS ManageUSSD destiné à l'USSD
 */
public class USSDServiceTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource
	private ManageUSSD manageUSSDConsumer;

	@Test
	public void testgetUSSDFormatedPortabilityInfoForGPEngageAllOk()
	{
		initMsisdnForGPEngage();
		if ( !msisdnForGPEngage.isEmpty() )
		{
			// On melange la liste pour ne pas avoir toujours les memes cas
			Collections.shuffle( msisdnForGPEngage );
			String msisdn = msisdnForGPEngage.get( 0 ).toString();
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForGPEngageAllOk-- MSISDN=" + msisdn );
			}
			// client particulier engage 596696233639 590690534406
			String message = manageUSSDConsumer.getUSSDFormatedPortabilityInfo( msisdn );

			assertNotNull( "la variable est nulle", message );
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForGPEngageAllOk-- " + "message reçu =" + message );
			}
			assertTrue( "Le message ne correspond pas à un GP engagé", message.contains( "engagement de" ) );
			assertTrue( "Le message ne correspond pas à un GP engagé", message.contains( "prendra fin le" ) );
			assertTrue( "Le message ne correspond pas à un GP engagé", message.contains( "Le RIO est :" ) );
		}
		else
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForGPEngageAllOk-- la requête n'a retourné aucun résultat" );

			}
		}
	}

	@Test
	public void testgetUSSDFormatedPortabilityInfoForGPNonEngageAllOk()
	{
		initMsisdnForGPNonEngage();
		if ( !msisdnForGPNonEngage.isEmpty() )
		{
			// On melange la liste pour ne pas avoir toujours les memes cas
			Collections.shuffle( msisdnForGPNonEngage );
			String msisdn = msisdnForGPNonEngage.get( 0 ).toString();

			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForGPNonEngageAllOk-- MSISDN=" + msisdn );
			}
			// client particulier non engage 590690567292 590690545418 590690509089
			String message = manageUSSDConsumer.getUSSDFormatedPortabilityInfo( msisdn );

			assertNotNull( "la variable est nulle", message );
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForGPNonEngageAllOk-- " + "message reçu =" + message );
			}
			assertTrue( "Le message ne correspond pas à un GP non engagé", message.contains( "est titulaire d" ) );
			assertTrue( "Le message ne correspond pas à un GP non engagé",
						message.contains( "un contrat non soumis" ) );
			assertTrue( "Le message ne correspond pas à un GP non engagé",
						message.contains( "un engagement. Le RIO est :" ) );
		}
		else
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForGPNonEngageAllOk-- la requête n'a retourné aucun résultat" );
			}
		}
	}

	@Test
	public void testgetUSSDFormatedPortabilityInfoForProEngageGestionnaireAllOk()

	{
		initMsisdnForProEngageGestionnaire();
		if ( !msisdnForProEngageGestionnaire.isEmpty() )
		{
			// On melange la liste pour ne pas avoir toujours les memes cas
			Collections.shuffle( msisdnForProEngageGestionnaire );
			String msisdn = msisdnForProEngageGestionnaire.get( 0 ).toString();

			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForProEngageGestionnaireAllOk-- MSISDN=" + msisdn );
			}
			// client professionnel engage 590690434900 594694267822 596696301636 590690379000
			String message = manageUSSDConsumer.getUSSDFormatedPortabilityInfo( msisdn );

			assertNotNull( "la variable est nulle", message );
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForProEngageGestionnaireAllOk-- " + "message reçu ="
					+ message );
			}

			assertTrue( "Le message ne correspond pas à un Pro Engage Gestionnaire",
						message.contains( "est titulaire du n" ) );
			assertTrue( "Le message ne correspond pas à un Pro Engage Gestionnaire",
						message.contains( "dont la date de fin d" ) );
			assertTrue( "Le message ne correspond pas à un Pro Engage Gestionnaire",
						message.contains( "engagement du contrat est le" ) );
			assertTrue( "Le message ne correspond pas à un Pro Engage Gestionnaire",
						message.contains( ". Le RIO est :" ) );
			assertTrue( "Le message ne correspond pas à un Pro Engage Gestionnaire",
						message.contains( "Pour obtenir le RIO" ) );
		}
		else
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForProEngageGestionnaireAllOk-- la requête n'a retourné aucun résultat" );
			}
		}
	}

	@Test
	public void testgetUSSDFormatedPortabilityInfoForProNonEngageNonGestionnaireAllOk()

	{
		initMsisdnForProNonEngageNonGestionnaire();
		if ( !msisdnForProNonEngageNonGestionnaire.isEmpty() )
		{
			// On melange la liste pour ne pas avoir toujours les memes cas
			Collections.shuffle( msisdnForProNonEngageNonGestionnaire );
			String msisdn = msisdnForProNonEngageNonGestionnaire.get( 0 ).toString();

			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForProNonEngageNonGestionnaireAllOk-- MSISDN="
					+ msisdn );
			}
			// client professionnel non engage 590690830911
			String message = manageUSSDConsumer.getUSSDFormatedPortabilityInfo( msisdn );

			assertNotNull( "la variable est nulle", message );
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForProNonEngageNonGestionnaireAllOk-- "
					+ "mesage reçu=" + message );
			}

			assertTrue( "Le message ne correspond pas à un Pro Non Engage Non Gestionnaire",
						message.contains( "est titulaire du n" ) );
			assertTrue( "Le message ne correspond pas à un Pro Non Engage Non Gestionnaire",
						message.contains( "dont le contrat n" ) );
			assertTrue( "Le message ne correspond pas à un Pro Non Engage Non Gestionnaire",
						message.contains( "est pas soumis" ) );
			assertTrue( "Le message ne correspond pas à un Pro Non Engage Non Gestionnaire",
						message.contains( "un engagement. Le RIO est :" ) );
		}
		else
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForProNonEngageNonGestionnaireAllOk-- la requête n'a retourné aucun résultat" );
			}
		}
	}

	@Test
	public void testgetUSSDFormatedPortabilityInfoForProNonEngageGestionnaireAllOk()

	{
		initMsisdnForProNonEngageGestionnaire();
		if ( !msisdnForProNonEngageGestionnaire.isEmpty() )
		{
			// On melange la liste pour ne pas avoir toujours les memes cas
			Collections.shuffle( msisdnForProNonEngageGestionnaire );
			String msisdn = msisdnForProNonEngageGestionnaire.get( 0 ).toString();
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForProNonEngageGestionnaireAllOk-- MSISDN=" + msisdn );
			}
			// client professionnel non engage 590690830911
			String message = manageUSSDConsumer.getUSSDFormatedPortabilityInfo( msisdn );
			assertNotNull( "la variable est nulle", message );
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForProNonEngageGestionnaireAllOk-- " + "message reçu="
					+ message );
			}

			assertTrue( "Le message ne correspond pas à un Pro Non Engage Gestionnaire",
						message.contains( "est titulaire du n" ) );
			assertTrue( "Le message ne correspond pas à un Pro Non Engage Gestionnaire",
						message.contains( "dont le contrat n" ) );
			assertTrue( "Le message ne correspond pas à un Pro Non Engage Gestionnaire",
						message.contains( "est pas soumis" ) );
			assertTrue( "Le message ne correspond pas à un Pro Non Engage Gestionnaire",
						message.contains( "un engagement. Le RIO est :" ) );
			assertTrue( "Le message ne correspond pas à un Pro Non Engage Gestionnaire",
						message.contains( "Pour obtenir le RIO" ) );
		}
		else
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForProNonEngageGestionnaireAllOk-- la requête n'a retourné aucun résultat" );
			}
		}

	}

	@Test
	public void testGetUSSDFormatedPortabilityInfoMsisdnKo()
	{

		String msisdn = "5969049031";
		String response = manageUSSDConsumer.getUSSDFormatedPortabilityInfo( msisdn );
		assertNotNull( response );
		assertFalse( StringUtils.isEmpty( response ) );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testGetUSSDFormatedPortabilityInfoMsisdnKo-- response=" + response );
		}
		assertTrue( response.contains( ConstantesUSSD.ERROR_CODE_UNKNOWN_MSISDN ) );

	}

	@Test
	public void testGetUSSDFormatedPortabilityInfoMsisdnEmpty()
	{

		String msisdn = "";
		String response = manageUSSDConsumer.getUSSDFormatedPortabilityInfo( msisdn );
		assertNotNull( response );
		assertFalse( StringUtils.isEmpty( response ) );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testGetUSSDFormatedPortabilityInfoMsisdnEmpty-- response=" + response );
		}
		assertTrue( response.contains( ConstantesUSSD.ERROR_CODE_UNKNOWN_MSISDN ) );

	}

}
