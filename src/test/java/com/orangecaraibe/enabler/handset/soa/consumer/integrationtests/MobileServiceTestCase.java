package com.orangecaraibe.enabler.handset.soa.consumer.integrationtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;

import com.orangecaraibe.enabler.AbstractBaseConsumerTestCase;
import com.orangecaraibe.soa.v1_2.interfaces.managehandset.ManageHandSet;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.InterfaceUnavailableException;

public class MobileServiceTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource
	private ManageHandSet manageHandSetConsumer;

	@Test
	public void testFindAndGetMobileImeiInconnu()
		throws FindAndGetException, InterfaceUnavailableException
	{
		// teste IMEIsans date de commissionnement ni code desimlockage
		// attendu : -4|||333333333333333

		String imei = "33333333333333";

		String result = manageHandSetConsumer.findAndGetMobileByImei( imei );
		assertNotNull( result );
		System.out.println( result );
		assertTrue( result.startsWith( "-4|||" ) );

	}

	@Test
	public void testFindAndGetMobileCodeOKDateAbsente()
		throws FindAndGetException, InterfaceUnavailableException
	{

		String imei = "869120012452625";
		String result = manageHandSetConsumer.findAndGetMobileByImei( imei );
		assertNotNull( result );
		// la reponse est au format : -4|2685442814||869120012453516
		// avec 15 caractères pour l'imei, donc la chaine de réponse est de taille > 21
		System.out.println( result );
		assertTrue( "code présent, date absente", result.startsWith( "-4" ) && ( result.length() > 21 ) );

	}

	// @Test
	public void testFindAndGetMobileCodeOKDateOK()
		throws FindAndGetException, InterfaceUnavailableException
	{

		//
		// teste un IMEI OK : bien commissionné depuis plus de 3 mois
		// et dont le code de désimlockage est présent
		// attendu : 0|3834155095|dd/MM/yyyy|869120012452625
		//

		String imei = "869120012452625";
		String result = manageHandSetConsumer.findAndGetMobileByImei( imei );
		assertNotNull( result );

		System.out.println( result );
		assertTrue( "date OK et code présent", result.startsWith( "0" ) && ( result.length() > 28 ) );

	}

	// @Test
	public void testFindAndGetMobileCodeKODateOK()
		throws FindAndGetException, InterfaceUnavailableException
	{

		//
		// teste un IMEI KO bien commissionné depuis plus de 3 mois
		// mais sans code de désimlockage attendu :
		// attendu : -1||dd/MM/yyyy|86912001245262
		//

		String imei = "869120012452625";
		String result = manageHandSetConsumer.findAndGetMobileByImei( imei );
		assertNotNull( result );

		System.out.println( result );
		assertTrue( "date OK et code absent", result.startsWith( "-1||" ) && ( result.length() > 25 ) );

	}

	// @Test
	public void testFindAndGetMobileCodeOKDateKO()
		throws FindAndGetException, InterfaceUnavailableException
	{

		//
		// teste un IMEI KO avec code mais commissionné trop récemment
		// depuis moins de trois mois :
		// attendu : -2|3834155095|dd/MM/yyyy|869120012452625
		//

		String imei = "869120012452625";
		String result = manageHandSetConsumer.findAndGetMobileByImei( imei );
		assertNotNull( result );

		System.out.println( result );
		assertTrue( "date KO et code présent", result.startsWith( "-2|" ) && ( result.length() > 28 ) );

	}

	// @Test
	public void testFindAndGetMobileCodeKODateKO()
		throws FindAndGetException, InterfaceUnavailableException
	{

		//
		// teste un IMEI KO commissionné trop récemment depuis moins de 3 mois
		// et sans code de désimlockage connu:
		// attendu : -3||dd/MM/yyyy|869120012452625
		//

		String imei = "869120012452625";
		String result = manageHandSetConsumer.findAndGetMobileByImei( imei );
		assertNotNull( result );

		System.out.println( result );
		assertTrue( "date KO et code absent", result.startsWith( "-3||" ) && ( result.length() > 28 ) );

	}
}
