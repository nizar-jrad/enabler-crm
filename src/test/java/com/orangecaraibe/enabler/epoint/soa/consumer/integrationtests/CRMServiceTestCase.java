/**
 *
 */
package com.orangecaraibe.enabler.epoint.soa.consumer.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Test;

import com.orangecaraibe.enabler.AbstractBaseConsumerTestCase;
import com.orangecaraibe.enabler.core.utils.Constantes;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CheckOutgoingRIOException;
import com.orangecaraibe.soa.v1_2.interfaces.managecrm.ManageCRM;
import com.orangecaraibe.soa.v2.interfaces.manageportability.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.ManagePortability;
import com.orangecaraibe.soa.v2.model.customerdomain.manageportability.Portability;

/**
 * @author sgodard Classe de test pour le WS ManageCRM destiné à Epoint
 */
@Deprecated
public class CRMServiceTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource( name = "manageCRMConsumer" )
	private ManageCRM manageCRMConsumer;

	@Resource( name = "managePortabilityConsumer" )
	private ManagePortability managePortabilityConsumer;

	@Test
	public void testChekOutgoingRIOAllOk()
		throws InterfaceUnavailableException, CheckOutgoingRIOException
	{
		String msisdn = "590690535734";
		String rio = "01P01VRPJEHR";
		String coId = "3161863";
		String typeClient = "C";

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOAllOk-- msisdn=" + msisdn + ", rio=" + rio + ", coId=" + coId
				+ ", type client=" + typeClient );
		}
		String response = manageCRMConsumer.checkOutgoingRIO( msisdn, rio, coId, typeClient );
		assertNotNull( response );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOAllOk-- response=" + response );
		}
		assertEquals( response, "true" );

	}

	@Test
	public void testChekOutgoingRIOMsisdnInvalid()
		throws InterfaceUnavailableException, CheckOutgoingRIOException
	{
		// format français au lieu du format international
		String msisdn = "0690535734";
		String rio = "01P01VRPJEHR";
		String coId = "3161863";
		String typeClient = "C";
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOMsisdnInvalid-- msisdn=" + msisdn + ", rio=" + rio + ", coId=" + coId
				+ ", type client=" + typeClient );
		}
		String response = manageCRMConsumer.checkOutgoingRIO( msisdn, rio, coId, typeClient );
		assertNotNull( response );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOMsisdnInvalid-- response=" + response );
		}
		assertTrue( response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );

	}

	@Test
	public void testChekOutgoingRIOMsisdnKo()
		throws InterfaceUnavailableException, CheckOutgoingRIOException
	{

		String msisdn = "590690535731";
		String rio = "01P01VRPJEHR";
		String coId = "3161863";
		String typeClient = "C";
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOMsisdnKo-- msisdn=" + msisdn + ", rio=" + rio + ", coId=" + coId
				+ ", type client=" + typeClient );
		}
		String response = manageCRMConsumer.checkOutgoingRIO( msisdn, rio, coId, typeClient );
		assertNotNull( response );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOMsisdnKo-- response=" + response );
		}
		assertEquals( response, "false" );

	}

	@Test
	public void testChekOutgoingRIORioKo()
		throws InterfaceUnavailableException, CheckOutgoingRIOException
	{

		// la clé du RIO ne correspond pas aux valeurs fournies
		String msisdn = "590690535734";
		String rio = "02P01VRPJEHR";
		String coId = "3161863";
		String typeClient = "C";
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIORioKo-- msisdn=" + msisdn + ", rio=" + rio + ", coId=" + coId
				+ ", type client=" + typeClient );
		}
		String response = manageCRMConsumer.checkOutgoingRIO( msisdn, rio, coId, typeClient );
		assertNotNull( response );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIORioKo-- response=" + response );
		}
		assertEquals( response, "false" );

	}

	@Test
	public void testChekOutgoingRIOCoIdKo()
		throws InterfaceUnavailableException, CheckOutgoingRIOException
	{
		// coId qui ne correspond pas à la référence client
		String msisdn = "590690535734";
		String rio = "01P01VRPJEHR";
		String coId = "316183";
		String typeClient = "C";

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOCoIdKo-- msisdn=" + msisdn + ", rio=" + rio + ", coId=" + coId
				+ ", type client=" + typeClient );
		}
		String response = manageCRMConsumer.checkOutgoingRIO( msisdn, rio, coId, typeClient );
		assertNotNull( response );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOCoIdKo-- response=" + response );
		}
		assertEquals( response, "false" );

	}

	@Test
	public void testChekOutgoingRIOCoIdInvalid()
		throws InterfaceUnavailableException, CheckOutgoingRIOException
	{
		// coId qui ne correspond pas à la référence client
		String msisdn = "590690535734";
		String rio = "01P01VRPJEHR";
		String coId = "316183TY";
		String typeClient = "C";

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOCoIdInvalid-- msisdn=" + msisdn + ", rio=" + rio + ", coId=" + coId
				+ ", type client=" + typeClient );
		}
		String response = manageCRMConsumer.checkOutgoingRIO( msisdn, rio, coId, typeClient );
		assertNotNull( response );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOCoIdInvalid-- response=" + response );
		}
		assertTrue( response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );

	}

	@Test
	public void testChekOutgoingRIOTypeClientKo()
		throws InterfaceUnavailableException, CheckOutgoingRIOException
	{
		// le type attendu est C
		String msisdn = "590690535734";
		String rio = "01P01VRPJEHR";
		String coId = "3161863";
		String typeClient = "B";
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOTypeClientKo-- msisdn=" + msisdn + ", rio=" + rio + ", coId=" + coId
				+ ", type client=" + typeClient );
		}
		String response = manageCRMConsumer.checkOutgoingRIO( msisdn, rio, coId, typeClient );
		assertNotNull( response );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOTypeClientKo-- response=" + response );
		}
		assertEquals( response, "false" );

	}

	@Test
	public void testChekOutgoingRIOTypeClientInvalid()
		throws InterfaceUnavailableException, CheckOutgoingRIOException
	{
		// le type attendu est C
		String msisdn = "590690535734";
		String rio = "01P01VRPJEHR";
		String coId = "3161863";
		String typeClient = "I";
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOTypeClientInvalid-- msisdn=" + msisdn + ", rio=" + rio + ", coId=" + coId
				+ ", type client=" + typeClient );
		}
		String response = manageCRMConsumer.checkOutgoingRIO( msisdn, rio, coId, typeClient );
		assertNotNull( response );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOTypeClientInvalid-- response=" + response );
		}
		assertTrue( response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );

	}

	@Test
	public void testChekOutgoingRIOAllEmpty()
		throws InterfaceUnavailableException, CheckOutgoingRIOException
	{
		String msisdn = "";
		String rio = "";
		String coId = "";
		String typeClient = "";
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOAllEmpty-- msisdn=" + msisdn + ", rio=" + rio + ", coId=" + coId
				+ ", type client=" + typeClient );
		}
		String response = manageCRMConsumer.checkOutgoingRIO( msisdn, rio, coId, typeClient );
		assertNotNull( response );
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "testChekOutgoingRIOAllEmpty-- response=" + response );
		}
		assertTrue( response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );

	}

	// Cas passant lorsque le coId est vide
	@Test
	public void testChekOutgoingRIOEmptyCoId()
		throws InterfaceUnavailableException, CheckOutgoingRIOException
	{
		String coId = "";

		initMsisdnForGPNonEngage();
		if ( !msisdnForGPNonEngage.isEmpty() )
		{
			// On melange la liste pour ne pas avoir toujours les memes cas
			Collections.shuffle( msisdnForGPNonEngage );
			String msisdn = msisdnForGPNonEngage.get( 0 ).toString();

			String typeClient = "";
			String rio = "";

			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForGPNonEngageAllOk-- MSISDN=" + msisdn );
			}
			try
			{

				Portability p = managePortabilityConsumer.getPortabilityInfo( msisdn );
				typeClient = p.getTypeClient();
				rio = p.getRio();
			}
			catch ( Exception e )
			{
				LOGGER.error( "testChekOutgoingRIOEmptyCoId-- exception appel WS managePortability.getPortabilityInfo("
					+ msisdn + "):" + e.getMessage() );
			}

			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testChekOutgoingRIOEmptyCoId-- msisdn=" + msisdn + ", rio=" + rio + ", coId=" + coId
					+ ", type client=" + typeClient );
			}

			String response = manageCRMConsumer.checkOutgoingRIO( msisdn, rio, coId, typeClient );
			assertNotNull( response );
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testChekOutgoingRIOEmptyCoId-- response=" + response );
			}
			assertEquals( response, "true" );
		}

		else
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "testgetUSSDFormatedPortabilityInfoForGPNonEngageAllOk-- la requête n'a retourné aucun résultat" );
			}
		}
	}

}
