/**
 *
 */
package com.orangecaraibe.enabler.epoint.soa.consumer.integrationtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.annotation.Resource;

import org.junit.Test;

import com.orangecaraibe.enabler.AbstractBaseConsumerTestCase;
import com.orangecaraibe.enabler.core.utils.Constantes;
import com.orangecaraibe.soa.v1_2.interfaces.managecrm.ManageCRM;

/**
 * @author sgodard
 */
@Deprecated
public class ManageCRMGetOutgoingRIOTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource
	private ManageCRM manageCRMConsumer;

	@Test
	public void testGetOutgoingRIOAllOk()

	{

		String msisdn = "590690535734";
		String rio = "01P01VRPJEHR";
		String coId = "3161863";
		String typeClient = "C";

		String response = manageCRMConsumer.getOutgoingRIO( msisdn, coId, typeClient );

		assertNotNull( response );
		assertEquals( response, rio );
	}

	@Test
	public void testGetOutgoingRIOAllEmpty()

	{

		String msisdn = "";
		String coId = "";
		String typeClient = "";

		String response = manageCRMConsumer.getOutgoingRIO( msisdn, coId, typeClient );
		assertNotNull( response );
		assertTrue( response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );

	}

	@Test
	public void testGetOutgoingRIOMsisdnKo()

	{

		String msisdn = "069049501";
		String coId = "123546";
		String typeClient = "C";

		String response = manageCRMConsumer.getOutgoingRIO( msisdn, coId, typeClient );
		assertNotNull( response );
		assertTrue( response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );

	}

	@Test
	public void testGetOutgoingRIOCoidKo()
	{

		String msisdn = "590690535734";
		String coId = "316186";
		String rio = "01P01VRPJEHR";
		String typeClient = "C";

		String response = manageCRMConsumer.getOutgoingRIO( msisdn, coId, typeClient );
		assertNotNull( response );
		assertFalse( response.equals( rio ) );

	}

	@Test
	public void testGetOutgoingRIOCoidInvalid()
	{

		String msisdn = "590690495031";
		String coId = "kl";
		String typeClient = "C";

		String response = manageCRMConsumer.getOutgoingRIO( msisdn, coId, typeClient );
		assertNotNull( response );
		assertTrue( response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );
	}

	@Test
	public void testGetOutgoingRIOTypeClientInvalid()
	{

		String msisdn = "590690495031";
		String coId = "123546";
		String typeClient = "I";

		String response = manageCRMConsumer.getOutgoingRIO( msisdn, coId, typeClient );
		assertNotNull( response );
		assertTrue( response.contains( Constantes.EPOINT_ERROR_RETURN_CODE ) );

	}
}
