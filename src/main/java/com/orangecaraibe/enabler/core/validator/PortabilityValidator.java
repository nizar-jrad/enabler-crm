/**
 *
 */
package com.orangecaraibe.enabler.core.validator;

import org.apache.commons.lang3.StringUtils;

import com.orangecaraibe.enabler.core.enums.TypeClientEnum;
import com.orangecaraibe.enabler.core.utils.Constantes;

/**
 * @author sgodard
 */
public class PortabilityValidator
{

	public boolean checkMsisdn( String msisdn )
	{
		boolean check = true;

		if ( msisdn == null || StringUtils.isEmpty( msisdn ) )
		{
			check = false;
		}

		else if ( !msisdn.matches( Constantes.REGEX_INTERNATIONAL_FORMAT ) || msisdn.length() != 12 )
		{
			check = false;
		}

		return check;
	}

	public boolean check( String obj )
	{
		boolean check = true;

		if ( obj == null )
		{
			check = false;
		}
		else if ( !StringUtils.isEmpty( obj ) )
		{
			try
			{
				if ( Integer.parseInt( obj ) <= 0 )
				{
					check = false;
				}
			}
			catch ( Exception e )
			{
				check = false;
			}

		}
		return check;
	}

	public boolean checkCoIdNoEmpty( String coId )
	{
		boolean check = true;

		if ( coId == null || StringUtils.isEmpty( coId ) )
		{
			check = false;
		}

		try
		{
			if ( Integer.parseInt( coId ) <= 0 )
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

	public boolean checkOperator( String operateur )
	{
		boolean check = true;

		if ( operateur == null || StringUtils.isEmpty( operateur ) )
		{
			check = false;
		}

		else if ( operateur.length() != 2 )
		{
			check = false;
		}
		else
		{
			try
			{
				if ( Integer.parseInt( operateur ) <= 0 )
				{
					check = false;
				}
			}
			catch ( Exception e )
			{
				check = false;
			}

		}
		return check;
	}

	public boolean checkCustomerType( String typeClient )
	{
		boolean check = true;
		if ( typeClient == null || StringUtils.isEmpty( typeClient ) )
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
