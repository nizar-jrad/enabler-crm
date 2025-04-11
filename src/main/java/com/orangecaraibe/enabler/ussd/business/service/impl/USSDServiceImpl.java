/**
 *
 */
package com.orangecaraibe.enabler.ussd.business.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orangecaraibe.enabler.core.enums.TypeClientEnum;
import com.orangecaraibe.enabler.core.utils.FormatUtils;
import com.orangecaraibe.enabler.ussd.business.adapters.PortabiliteAdapter;
import com.orangecaraibe.enabler.ussd.business.beans.FormatedPortabilite;
import com.orangecaraibe.enabler.ussd.business.service.USSDService;
import com.orangecaraibe.enabler.ussd.core.exception.TechnicalErrorException;
import com.orangecaraibe.enabler.ussd.core.exception.UnknownClientException;
import com.orangecaraibe.enabler.ussd.core.exception.UnknownMsisdnException;
import com.orangecaraibe.enabler.ussd.helpers.USSDPropertiesHelper;
import com.orangecaraibe.soa.v2.interfaces.manageportability.GetPortabilityInfoException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.ManagePortability;
import com.orangecaraibe.soa.v2.model.customerdomain.manageportability.Portability;

/**
 * @author sgodard Implémentation des services métiers de gestion des informations de portabilité pour l'USSD
 */
@Service( "ussdService" )
public class USSDServiceImpl
	implements USSDService
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( USSDServiceImpl.class );

	/** Helper pour récupérer les chaînes depuis le fichier de properties de l'USSD */
	@Autowired
	@Qualifier( "ussdPropertiesHelper" )
	private USSDPropertiesHelper ussdPropertiesHelper;

	/** Business Adapter: transforme un un objet Portability en objet FormatedPortabilite */
	@Autowired
	@Qualifier("ussdPortabiliteAdapter")
	private PortabiliteAdapter portabiliteAdapter;

	/** Web Service ManagePortability */
	@Autowired
	private ManagePortability managePortability;

	/**
	 * Reçoie un msisdn , recherche les infos de portabilité et renvoie un message formaté pour l'USSD
	 */
	@Override
	public String getUSSDFormatedPortabilityInfo( String msisdn )
		throws UnknownClientException, UnknownMsisdnException, TechnicalErrorException
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Begin Business getUSSDFormatedPortabilityInfo-- msisdn=" + msisdn );
		}
		String formatedInfos = "";

		try
		{

			// Appel au WS ManagePortability.getPortabilityInfo
			Portability portabilityInfos = managePortability.getPortabilityInfo( msisdn );

			if ( portabilityInfos != null )
			{
				if ( LOGGER.isDebugEnabled() )
				{
					LOGGER.debug( "Appel WS ManagePortability réussi -- informations recupérées" );
				}
				FormatedPortabilite portabiliteFormatee =
					portabiliteAdapter.adaptToFormatedPortabilite( portabilityInfos );

				if ( LOGGER.isDebugEnabled() )
				{
					LOGGER.debug( "Business getUSSDFormatedPortabilityInfo-- informations formatées="
						+ portabiliteFormatee );
				}

				// Si le client est une entreprise --> E dans USSD
				if ( portabiliteFormatee.getTypeClient() == TypeClientEnum.ENTREPRISE.getUssdValue() )
				{
					String numero = FormatUtils.getPhoneNumberFromMsisdn( msisdn );

					if ( LOGGER.isDebugEnabled() && numero != null )
					{
						LOGGER.debug( "Business getUSSDFormatedPortabilityInfo-- msisdn formaté=" + numero );
					}

					// Contrat engagé
					if ( portabilityInfos.isEngage() )
					{
						// Gestionnaire
						if ( BooleanUtils.toBoolean( portabilityInfos.getGestionnaire() ) )
						{
							if ( LOGGER.isDebugEnabled() )
							{
								LOGGER.debug( "Business ussdPropertiesHelper -- getProEngageGestionnaireMessage()" );
							}
							formatedInfos =
								ussdPropertiesHelper.getProEngageGestionnaireMessage(	portabiliteFormatee.getDenomination(),
																						numero,
																						portabiliteFormatee.getDateFin(),
																						portabiliteFormatee.getRio() );
						}
						// Pas gestionnaire
						else
						{
							if ( LOGGER.isDebugEnabled() )
							{
								LOGGER.debug( "Business ussdPropertiesHelper -- getProEngageNonGestionnaireMessage()" );
							}
							formatedInfos =
								ussdPropertiesHelper.getProEngageNonGestionnaireMessage(	portabiliteFormatee.getDenomination(),
																							numero,
																							portabiliteFormatee.getDateFin(),
																							portabiliteFormatee.getRio() );
						}

					}
					// Contrat non engagé
					else
					{
						// Gestionnaire
						if ( BooleanUtils.toBoolean( portabilityInfos.getGestionnaire() ) )
						{
							if ( LOGGER.isDebugEnabled() )
							{
								LOGGER.debug( "Business ussdPropertiesHelper -- getProNonEngageGestionnaireMessage()" );
							}
							formatedInfos =
								ussdPropertiesHelper.getProNonEngageGestionnaireMessage(	portabiliteFormatee.getDenomination(),
																							numero,
																							portabiliteFormatee.getRio() );
						}
						// Pas gestionnaire
						else
						{
							if ( LOGGER.isDebugEnabled() )
							{
								LOGGER.debug( "Business ussdPropertiesHelper -- getProNonEngageNonGestionnaireMessage()" );
							}
							formatedInfos =
								ussdPropertiesHelper.getProNonEngageNonGestionnaireMessage( portabiliteFormatee.getDenomination(),
																							numero,
																							portabiliteFormatee.getRio() );
						}
					}

				}
				// si le client est un particulier --> P dans USSD
				else if ( portabiliteFormatee.getTypeClient() == TypeClientEnum.PARTICULIER.getUssdValue() )
				{

					// Contrat engagé
					if ( portabilityInfos.isEngage() )
					{

						if ( LOGGER.isDebugEnabled() )
						{
							LOGGER.debug( "Business ussdPropertiesHelper -- getParticulierEngageMessage()" );
						}
						formatedInfos =
							ussdPropertiesHelper.getParticulierEngageMessage(	portabiliteFormatee.getNom(),
																				portabiliteFormatee.getPrenom(),
																				portabiliteFormatee.getDateDebut(),
																				portabiliteFormatee.getDateFin(),
																				portabiliteFormatee.getRio() );

					}
					// Contrat non engagé
					else
					{
						if ( LOGGER.isDebugEnabled() )
						{
							LOGGER.debug( "Business ussdPropertiesHelper -- getParticulierNonEngageMessage()" );
						}
						formatedInfos =
							ussdPropertiesHelper.getParticulierNonEngageMessage(	portabiliteFormatee.getNom(),
																					portabiliteFormatee.getPrenom(),
																					portabiliteFormatee.getRio() );
					}

				}

			}
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "End Business getUSSDFormatedPortabilityInfo-- message formaté =" + formatedInfos );
			}
			return formatedInfos;
		}
		catch ( GetPortabilityInfoException e )
		{
			if ( e.getMessage().contains( "CONTRACT|" ) )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "La récupération des informations de portabilité a échoué: contrat inconnu", e );
				}
				throw new UnknownMsisdnException( e.getMessage() );
			}
			else if ( e.getMessage().contains( "CUSTOMER|" ) )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "La récupération des informations de portabilité a échoué: compte inconnu", e );
				}
				throw new UnknownClientException( e.getMessage() );
			}
			else if ( e.getMessage().contains( "EBS|" ) )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error(	"La récupération des informations de portabilité a échoué: erreur d'un EBS sous-jacent",
									e );
				}
				throw new TechnicalErrorException( e.getMessage() );
			}
		}
		catch ( InterfaceUnavailableException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: interface error", e );
			}
			throw new TechnicalErrorException( e.getMessage() );
		}

		catch ( RuntimeException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: erreur technique", e );
			}
			throw new TechnicalErrorException( e.getMessage() );
		}
		return null;

	}

}
