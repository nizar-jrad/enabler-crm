/**
 *
 */
package com.orangecaraibe.enabler.core.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.core.enums.TypeClientEnum;
import com.orangecaraibe.enabler.core.utils.Constantes;

/**
 * @author sgodard Classe de validateur commune aux différents services
 */
@Component
public class ArgumentsValidator
{

	/**
	 * Vérifie qu'une chaîne est non nulle et non vide
	 * 
	 * @param obj
	 * @return
	 */
	public boolean checkString( String obj )
	{
		boolean check = true;
		if ( obj == null )
		{
			check = false;
		}
		else if ( StringUtils.isEmpty( obj ) )
		{
			check = false;
		}

		return check;
	}

	/**
	 * Vérifie qu'on traite un entier positif
	 * 
	 * @param operateur
	 * @param check
	 * @return
	 */
	private boolean checkNumeric( String numeric )
	{
		boolean check = true;
		try
		{
			if ( Integer.parseInt( numeric ) <= 0 )
			{
				check = false;
			}
		}
		catch ( Exception e )
		{
			check = false;
		}
		return check;
	}

	/**
	 * Vérifie le numéro de téléphone -- Chaîne non nulle et non vide -- Format international
	 * 
	 * @param msisdn
	 * @return
	 */
	public boolean checkMsisdn( String msisdn )
	{
		boolean check = true;
		if ( !checkString( msisdn ) )
		{

			check = false;
		}
		else if ( !msisdn.matches( Constantes.REGEX_INTERNATIONAL_FORMAT ) || msisdn.length() != 12 )
		{
			check = false;
		}

		return check;
	}

	/**
	 * Vérifie un numéro de contrat avec la possibilité de chaîne vide (CheckOutgoingRIO) -- Chaîne non nulle -- On
	 * traite un entier positif
	 * 
	 * @param coId
	 * @return
	 */
	public boolean checkCoIdEmpty( String coId )
	{
		boolean check = true;

		if ( coId == null )
		{
			check = false;
		}

		else if ( !StringUtils.isEmpty( coId ) )
		{
			check = checkNumeric( coId );
		}
		return check;
	}

	/**
	 * Vérifie un numéro de contrat sans possibilité de chaîne vide (getOutgoingRIO) -- Chaîne non nulle ET non vide --
	 * On traite un entier positif
	 * 
	 * @param coId
	 * @return
	 */
	public boolean check( String coId )
	{
		boolean check = true;

		if ( !checkString( coId ) )
		{

			check = false;
		}
		else if ( !checkNumeric( coId ) )
		{
			check = false;
		}
		return check;
	}

	/**
	 * Vérifie l'operateur -- Chaîne non nulle et non vide -- Chaîne sur 2 caractères -- Chaîne représente un entier
	 * 
	 * @param operateur
	 * @return
	 */
	public boolean checkOperator( String operateur )
	{
		boolean check = true;
		if ( !checkString( operateur ) )
		{

			check = false;
		}

		else if ( operateur.length() != 2 )
		{
			check = false;
		}

		else if ( !checkNumeric( operateur ) )
		{
			check = false;
		}

		return check;
	}

	/**
	 * Vérifie le type du client -- Chaîne non nulle et non vide -- la valeur est B ou C
	 * 
	 * @param typeClient
	 * @return
	 */
	public boolean checkCustomerType( String typeClient )
	{
		boolean check = true;
		if ( !checkString( typeClient ) )
		{

			check = false;
		}
		else if ( !typeClient.equals( TypeClientEnum.ENTREPRISE.getBscsValue() )
			&& !typeClient.equals( TypeClientEnum.PARTICULIER.getBscsValue() ) )
		{
			check = false;
		}

		return check;
	}

}
