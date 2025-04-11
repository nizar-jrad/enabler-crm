/**
 *
 */
package com.orangecaraibe.enabler.core.utils;

import java.util.regex.Pattern;

/**
 * @author sgodard Classe utilitaire de formatage
 */
public class FormatUtils
{

	/**
	 * Récupère un numéro de téléphone à partir d'un msisdn au format international 594690545412 ou 590690254585
	 */
	public static String getPhoneNumberFromMsisdn( String msisdn )
	{
		String number = null;
		if ( msisdn != null && msisdn.length() == 12 )
		{

			number = msisdn.substring( 3, 12 );
			number = "0" + number;

		}
		return number;
	}

	/**
	 * Récupère un numéro de téléphone à partir d'un msisdn au format international 594690545412 ou 590690254585
	 */
	public static String getMsisdnFromPhoneNumber( String msisdn )
	{

		msisdn = formatting( msisdn );
		// if international form return.
		if ( msisdn.length() > 10 )
		{
			return msisdn;
		}
		return (String) msisdn.subSequence( 0, 3 ) + msisdn;
	}

	/**
	 * Mise en forme du nds (Suppression des espaces vides, et du premier zéro des dix chiffres du numéro
	 * 
	 * @param nds national number in form: <code>05 90 06 33 39</code>
	 * @return the international number: <code>+590  590 063339</code>
	 */
	private static String formatting( String msisdn )
	{
		// Suppression des espaces inutiles et autres characteres tels que . -
		msisdn = msisdn.replaceAll( "\\D*", "" );

		// On test si au format 10 chiffres commencant par un 0
		Pattern natinalNumberPattern = Pattern.compile( "0[0-9]{9}" );

		if ( natinalNumberPattern.matcher( msisdn ).matches() )
		{
			// Suppression du 0 en début pour le traitement de l'ajout du préfix en international
			msisdn = msisdn.substring( 1 );
		}

		return msisdn;
	}
}
