package com.orangecaraibe.enabler.handset.webservice.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.handset.business.api.Mobile;
import com.orangecaraibe.enabler.handset.core.util.IMEIutils;

@Component
public class StringInfoMobileAdapter
{
	private static final String REPONSE_DESIMLOCKAGE_PAR_DEFAUT = "-4||";

	/**
	 * Fonction de formattage de la réponse de demande de code sim: 5 formats sont possibles: - 0 =
	 * "0|01234567890|11/02/2010"; // code trouvé et date d'achat de plus de 3 mois - 1 = "-1||11/02/2010"; // pas de
	 * code trouvé et date d'achat plus de 3 mois - 2 = "-2|1234567890|11/03/2010" // code trouvé mais date de moins de
	 * trois mois - 3 = "-3||11/03/2010" pas de code et moins de 3 mois - 4 = "-4||" pas de date trouvée
	 * (commissionnement non trouvé)
	 * 
	 * @param mobile
	 * @return
	 */
	public String getReponseDesimlockage( Mobile mobile )
	{
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
		Calendar calmoins3mois = Calendar.getInstance();
		Calendar calachat = Calendar.getInstance();
		calmoins3mois.add( Calendar.MONTH, -3 );

		Date dateAchat = null;
		String imei_luhn = ( new IMEIutils() ).completeIMEI( mobile.getImei() );
		result = REPONSE_DESIMLOCKAGE_PAR_DEFAUT + "|" + imei_luhn;
		if ( mobile.getCodeSim() != null )
		{
			if ( mobile.getDateAchat() != null )
			{ // on a trouvé la date
				dateAchat = mobile.getDateAchat();
				calachat.setTime( dateAchat );

				if ( calachat.before( calmoins3mois ) )
					result = "0|" + mobile.getCodeSim() + "|" + sdf.format( dateAchat ) + "|";
				else
					result = "-2|" + mobile.getCodeSim() + "|" + sdf.format( dateAchat ) + "|";

			}
			else
			{
				// pas de date trouvée : code -4 retourné.
				result = "-4|" + mobile.getCodeSim() + "||" + imei_luhn;
			}

		}
		else
		{ // pas de code, on recherche quand même la date

			if ( mobile.getDateAchat() != null )
			{
				dateAchat = mobile.getDateAchat();
				calachat.setTime( dateAchat );

				if ( calachat.before( calmoins3mois ) )
				{
					result = "-1||" + sdf.format( dateAchat ) + "|" + imei_luhn;
				}
				else
					result = "-3||" + sdf.format( dateAchat ) + "|";
			}
			else
			{
				// pas de date trouvée : code -4 par défaut de code et de date
			}
		}
		return result;

	}

}
