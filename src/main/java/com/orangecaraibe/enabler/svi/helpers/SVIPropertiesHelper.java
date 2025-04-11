package com.orangecaraibe.enabler.svi.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.core.helpers.PropertiesHelper;
import com.orangecaraibe.enabler.core.utils.Constantes;
import com.orangecaraibe.enabler.svi.core.util.ConstantesSVI;

/**
 * @author sgodard Classe permettant de récupérer les message associées à l'USSD
 */
@Component
public class SVIPropertiesHelper
{
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger( SVIPropertiesHelper.class );

	@Autowired
	private PropertiesHelper propertiesHelper;

	@Value( "${ussd.message.additional.number}" )
	private String additionnalNumber;

	/**
	 * Methode qui mutualise le code qui rajoute le message additionnel pour les pro gestionnaires
	 * 
	 * @param sms
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String addAdditionalMessage( String sms )
		throws FileNotFoundException, IOException
	{
		String additional = propertiesHelper.getProperty(	ConstantesSVI.KEY_PRO_GESTIONNAIRE_ADDITIONAL,
															Constantes.SVI_PROPERTIES_FILE );

		if ( additional != null )
		{
			additional.replaceAll( ConstantesSVI.KEYWORD_ADDITIONAL_NUMBER, additionnalNumber );
			sms = sms + additional;
		}
		else
		{
			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}
		return sms;
	}

	/**
	 * Méthode qui récupère le message pour un particulier dont le contrat est engagé à la date d'interrogation du
	 * serveur
	 */
	public String getParticulierEngageMessage( String nom, String prenom, String dateDebut, String dateFin, String rio )
	{
		String sms = "";
		try
		{

			sms =
				propertiesHelper.getProperty( ConstantesSVI.KEY_PARTICULIER_ENGAGE, Constantes.SVI_PROPERTIES_FILE );

			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties avant complétion: " + sms );
			}
			if ( sms == null )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( nom == null || StringUtils.isEmpty( nom ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}

			else if ( dateDebut == null || StringUtils.isEmpty( dateDebut ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( dateFin == null || StringUtils.isEmpty( dateFin ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( rio == null || StringUtils.isEmpty( rio ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else
			{

				if ( prenom == null )
				{
					prenom = "";
				}
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_NOM, nom );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_PRENOM, prenom );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_DEBUT, dateDebut );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_FIN, dateFin );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_RIO, rio );

			}
			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties après complétion: " + sms );
			}

		}

		catch ( FileNotFoundException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Fichier introuvable", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}
		catch ( IOException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Erreur I/O", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}

		return sms;
	}

	/**
	 * Méthode qui récupère le message pour un particulier dont le contrat n'est pas engagé à la date d'interrogation du
	 * serveur
	 */
	public String getParticulierNonEngageMessage( String nom, String prenom, String rio )
	{
		String sms = "";

		try
		{

			sms = propertiesHelper.getProperty( ConstantesSVI.KEY_PARTICULIER_NONENGAGE,
												Constantes.SVI_PROPERTIES_FILE );

			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties avant complétion: " + sms );
			}
			if ( sms == null )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( nom == null || StringUtils.isEmpty( nom ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}

			else if ( rio == null || StringUtils.isEmpty( rio ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else
			{
				if ( prenom == null )
				{
					prenom = "";
				}
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_NOM, nom );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_PRENOM, prenom );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_RIO, rio );
			}

			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties après complétion: " + sms );
			}
		}

		catch ( FileNotFoundException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Fichier introuvable", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}
		catch ( IOException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Erreur I/O", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}
		return sms;
	}

	/**
	 * Méthode qui récupère le message pour un pro dont le contrat est engagé à la date d'interrogation du serveur et
	 * dont le numéro correspond au gestionnaire du compte
	 */
	public String getProEngageGestionnaireMessage( String denomination, String numero, String dateFin, String rio )
	{
		String sms = "";

		try
		{

			sms = propertiesHelper.getProperty( ConstantesSVI.KEY_PRO_ENGAGE_GESTIONNAIRE,
												Constantes.SVI_PROPERTIES_FILE );
			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties avant complétion: " + sms );
			}
			if ( sms == null )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( denomination == null || StringUtils.isEmpty( denomination ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( numero == null || StringUtils.isEmpty( numero ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( dateFin == null || StringUtils.isEmpty( dateFin ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( rio == null || StringUtils.isEmpty( rio ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else
			{
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_DENOMINATION, denomination );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_NUMERO, numero );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_FIN, dateFin );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_RIO, rio );

				sms = addAdditionalMessage( sms );
			}

			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties après complétion: " + sms );
			}
		}

		catch ( FileNotFoundException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Fichier introuvable", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}
		catch ( IOException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Erreur I/O", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}
		return sms;
	}

	/**
	 * Méthode qui récupère le message pour un pro dont le contrat est engagé à la date d'interrogation du serveur et
	 * dont le numéro ne correspond pas au gestionnaire du compte
	 */
	public String getProEngageNonGestionnaireMessage( String denomination, String numero, String dateFin, String rio )
	{
		String sms = "";
		try
		{

			sms = propertiesHelper.getProperty( ConstantesSVI.KEY_PRO_ENGAGE_NONGESTIONNAIRE,
												Constantes.SVI_PROPERTIES_FILE );
			if ( log.isErrorEnabled() )
			{
				log.error( "Message récupéré depuis le fichier de properties avant complétion: " + sms );
			}
			if ( sms == null )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( denomination == null || StringUtils.isEmpty( denomination ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( numero == null || StringUtils.isEmpty( numero ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( dateFin == null || StringUtils.isEmpty( dateFin ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( rio == null || StringUtils.isEmpty( rio ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else
			{
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_DENOMINATION, denomination );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_NUMERO, numero );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_FIN, dateFin );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_RIO, rio );
			}
			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties après complétion: " + sms );
			}
		}

		catch ( FileNotFoundException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Fichier introuvable", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}
		catch ( IOException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Erreur I/O", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}
		return sms;
	}

	/**
	 * Méthode qui récupère le message pour un pro dont le contrat n'est pas engagé à la date d'interrogation du serveur
	 * et dont le numéro correspond au gestionnaire du compte
	 */
	public String getProNonEngageGestionnaireMessage( String denomination, String numero, String rio )
	{
		String sms = "";

		try
		{

			sms = propertiesHelper.getProperty( ConstantesSVI.KEY_PRO_NONENGAGE_GESTIONNAIRE,
												Constantes.SVI_PROPERTIES_FILE );
			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties avant complétion: " + sms );
			}
			if ( sms == null )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( denomination == null || StringUtils.isEmpty( denomination ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( numero == null || StringUtils.isEmpty( numero ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( rio == null || StringUtils.isEmpty( rio ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else
			{
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_DENOMINATION, denomination );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_NUMERO, numero );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_RIO, rio );

				sms = addAdditionalMessage( sms );
			}
			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties après complétion: " + sms );
			}

		}

		catch ( FileNotFoundException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Fichier introuvable", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}
		catch ( IOException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Erreur I/O", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}

		return sms;
	}

	/**
	 * Méthode qui récupère le message pour un pro dont le contrat n'est pas engagé à la date d'interrogation du serveur
	 * et dont le numéro ne correspond pas au gestionnaire du compte
	 */
	public String getProNonEngageNonGestionnaireMessage( String denomination, String numero, String rio )
	{
		String sms = "";

		try
		{

			sms = propertiesHelper.getProperty( ConstantesSVI.KEY_PRO_NONENGAGE_NONGESTIONNAIRE,
												Constantes.SVI_PROPERTIES_FILE );
			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties avant complétion: " + sms );
			}
			if ( sms == null )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( denomination == null || StringUtils.isEmpty( denomination ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( numero == null || StringUtils.isEmpty( numero ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else if ( rio == null || StringUtils.isEmpty( rio ) )
			{
				sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
			}
			else
			{
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_DENOMINATION, denomination );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_NUMERO, numero );
				sms = sms.replaceAll( ConstantesSVI.KEYWORD_RIO, rio );
			}
			if ( log.isDebugEnabled() )
			{
				log.debug( "Message récupéré depuis le fichier de properties après complétion: " + sms );
			}
		}

		catch ( FileNotFoundException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Fichier introuvable", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}
		catch ( IOException e )
		{
			if ( log.isErrorEnabled() )
			{
				log.error( "Erreur I/O", e );
			}

			sms = ConstantesSVI.ERROR_CODE_TECHNICAL_ERROR;
		}

		return sms;
	}

	/**
	 * @return the propertiesHelper
	 */
	public PropertiesHelper getPropertiesHelper()
	{
		return propertiesHelper;
	}

	/**
	 * @param propertiesHelper the propertiesHelper to set
	 */
	public void setPropertiesHelper( PropertiesHelper propertiesHelper )
	{
		this.propertiesHelper = propertiesHelper;
	}

	public void setAdditionnalNumber( String additionnalNumber )
	{
		this.additionnalNumber = additionnalNumber;
	}

}
