/**
 *
 */
package com.orangecaraibe.enabler.ussd.business.adapters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.core.enums.TypeClientEnum;
import com.orangecaraibe.enabler.core.helpers.PropertiesHelper;
import com.orangecaraibe.enabler.core.utils.Constantes;
import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.core.utils.TextUtils;
import com.orangecaraibe.enabler.ussd.business.beans.FormatedPortabilite;
import com.orangecaraibe.enabler.ussd.core.util.ConstantesUSSD;
import com.orangecaraibe.soa.v2.model.customerdomain.manageportability.Portability;

/**
 * @author sgodard Classe permettant de transformer un objet de la classe Portability en un objet d'une autre couche
 */
@Component("ussdPortabiliteAdapter")
public class PortabiliteAdapter
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( PortabiliteAdapter.class );

	@Autowired
	private PropertiesHelper propertiesHelper;

	/**
	 * Cette méthode retourne un objet contenant les propriétés formatés sous forme de chaînes de caractères
	 * 
	 * @param portabilityInfos
	 * @return
	 */
	public FormatedPortabilite adaptToFormatedPortabilite( Portability portabilityInfos )
	{
		FormatedPortabilite formatedPortabilite = new FormatedPortabilite();

		String nom = null;
		String prenom = null;
		String dateDebut = null;
		String dateFin = null;
		String rio = null;
		String typeClient = null;
		String denomination = null;

		if ( portabilityInfos != null )
		{

			if ( portabilityInfos.getNom() != null && !StringUtils.isEmpty( portabilityInfos.getNom() ) )
			{
				try
				{
					String length = propertiesHelper.getProperty(	ConstantesUSSD.KEY_PARTICULIER_NOM_MAXLENGTH,
																	Constantes.USSD_PROPERTIES_FILE );
					int intLength = Integer.valueOf( length );
					nom = TextUtils.reduceStringLength( portabilityInfos.getNom(), intLength );
				}
				catch ( FileNotFoundException e )
				{

					if ( LOGGER.isErrorEnabled() )
					{
						LOGGER.error( "Fichier introuvable", e );
					}

				}
				catch ( IOException e )
				{
					if ( LOGGER.isErrorEnabled() )
					{
						LOGGER.error( "Erreur I/O", e );
					}

				}

			}

			if ( portabilityInfos.getPrenom() != null && !StringUtils.isEmpty( portabilityInfos.getPrenom() ) )
			{

				try
				{
					String length = propertiesHelper.getProperty(	ConstantesUSSD.KEY_PARTICULIER_PRENOM_MAXLENGTH,
																	Constantes.USSD_PROPERTIES_FILE );
					int intLength = Integer.valueOf( length );
					prenom = TextUtils.reduceStringLength( portabilityInfos.getPrenom(), intLength );
				}
				catch ( FileNotFoundException e )
				{
					if ( LOGGER.isErrorEnabled() )
					{
						LOGGER.error( "Fichier introuvable", e );
					}

				}
				catch ( IOException e )
				{
					if ( LOGGER.isErrorEnabled() )
					{
						LOGGER.error( "Erreur I/O", e );
					}
				}

			}

			if ( portabilityInfos.getDateDebutEngagement() != null )
			{
				Date d1 = DateUtils.getDateFromXmlGregorianCalendar( portabilityInfos.getDateDebutEngagement() );
				dateDebut = DateUtils.getDateFormat( d1 );// jj/mm/aaaa
			}

			if ( portabilityInfos.getDateFinEngagement() != null )
			{
				Date d2 = DateUtils.getDateFromXmlGregorianCalendar( portabilityInfos.getDateFinEngagement() );
				dateFin = DateUtils.getDateFormat( d2 );// jj/mm/aaaa
			}

			if ( portabilityInfos.getRio() != null && !StringUtils.isEmpty( portabilityInfos.getRio() ) )
			{
				rio = portabilityInfos.getRio().toUpperCase();
			}

			if ( portabilityInfos.getTypeClient() != null && !StringUtils.isEmpty( portabilityInfos.getTypeClient() ) )
			{
				// [BSCS] C=customer ---> [USSD] P=particulier
				if ( portabilityInfos.getTypeClient().equalsIgnoreCase( TypeClientEnum.PARTICULIER.getBscsValue() ) )
				{
					typeClient = TypeClientEnum.PARTICULIER.getUssdValue();
				}
				// [BSCS] B=Business ---> [USSD] E=entreprise
				else if ( portabilityInfos.getTypeClient().equalsIgnoreCase( TypeClientEnum.ENTREPRISE.getBscsValue() ) )
				{
					typeClient = TypeClientEnum.ENTREPRISE.getUssdValue();
				}

			}

			if ( portabilityInfos.getDenominationSociale() != null
				&& !StringUtils.isEmpty( portabilityInfos.getDenominationSociale() ) )
			{

				try
				{
					String length = propertiesHelper.getProperty(	ConstantesUSSD.KEY_PRO_DENOMINATION_MAXLENGTH,
																	Constantes.USSD_PROPERTIES_FILE );
					int intLength = Integer.valueOf( length );
					denomination = TextUtils.reduceStringLength( portabilityInfos.getDenominationSociale(), intLength );
				}
				catch ( FileNotFoundException e )
				{
					if ( LOGGER.isErrorEnabled() )
					{
						LOGGER.error( "Fichier introuvable", e );
					}
				}
				catch ( IOException e )
				{
					if ( LOGGER.isErrorEnabled() )
					{
						LOGGER.error( "Erreur I/O", e );
					}
				}
			}

		}

		formatedPortabilite.setNom( nom );
		formatedPortabilite.setPrenom( prenom );
		formatedPortabilite.setDateDebut( dateDebut );
		formatedPortabilite.setDateFin( dateFin );
		formatedPortabilite.setRio( rio );
		formatedPortabilite.setTypeClient( typeClient );
		formatedPortabilite.setDenomination( denomination );

		return formatedPortabilite;
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

}
