package com.orangecaraibe.enabler.svi.business.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orangecaraibe.enabler.core.enums.TypeClientEnum;
import com.orangecaraibe.enabler.core.utils.FormatUtils;
import com.orangecaraibe.enabler.svi.business.adapters.PortabiliteAdapter;
import com.orangecaraibe.enabler.svi.business.beans.FormattedPortabilite;
import com.orangecaraibe.enabler.svi.business.service.SVIService;
import com.orangecaraibe.enabler.svi.core.exception.TechnicalErrorException;
import com.orangecaraibe.enabler.svi.core.exception.UnknownClientException;
import com.orangecaraibe.enabler.svi.core.exception.UnknownMsisdnException;
import com.orangecaraibe.enabler.svi.helpers.SVIPropertiesHelper;
import com.orangecaraibe.soa.v2.interfaces.manageportability.GetPortabilityInfoException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.ManagePortability;
import com.orangecaraibe.soa.v2.model.customerdomain.manageportability.Portability;

/**
 * @author sgodard Implémentation des services métiers de gestion des informations de portabilité pour l'SVI
 */
@Service( "sviService" )
public class SVIServiceImpl
	implements SVIService
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( SVIServiceImpl.class );

	/** Helper pour récupérer les chaînes depuis le fichier de properties de l'SVI */
	@Autowired
	private SVIPropertiesHelper sviPropertiesHelper;

	/** Business Adapter: transforme un un objet Portability en objet FormatedPortabilite */
	@Autowired
	@Qualifier("sviPortabiliteAdapter")
	private PortabiliteAdapter portabiliteAdapter;

	/** Web Service ManagePortability */
	@Autowired
	private ManagePortability managePortability;

	/**
	 * Reçoie un msisdn , recherche les infos de portabilité et renvoie un message formaté pour l'SVI
	 */
	@Override
	public FormattedPortabilite getSVIFormattedPortabilityInfo( String msisdn )
		throws UnknownClientException, UnknownMsisdnException, TechnicalErrorException
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Begin Business getSVIFormatedPortabilityInfo-- msisdn=" + msisdn );
		}
		String formattedSMS = "";
		String reponse = "";
		FormattedPortabilite portabiliteFormatee = new FormattedPortabilite();
		
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
				portabiliteFormatee  =
					portabiliteAdapter.adaptToFormatedPortabilite( portabilityInfos);
				
				if ( LOGGER.isDebugEnabled() )
				{
						LOGGER.debug( "Business getSVIFormatedPortabilityInfo-- informations formatées="
							+  portabiliteFormatee);
				}

				// Si le client est une entreprise --> E dans SVI
				if ( portabiliteFormatee.getTypeClient() == TypeClientEnum.ENTREPRISE.getUssdValue() )
				{
					String numero = FormatUtils.getPhoneNumberFromMsisdn( msisdn );
					if ( LOGGER.isDebugEnabled() && numero != null )
					{
						LOGGER.debug( "Business getSVIFormatedPortabilityInfo-- msisdn formaté=" + numero );
					}

					// Contrat engagé
					if ( portabilityInfos.isEngage() )
					{
						// Gestionnaire
						if ( BooleanUtils.toBoolean( portabilityInfos.getGestionnaire() ) )
						{
							if ( LOGGER.isDebugEnabled() )
							{
								LOGGER.debug( "Business sviPropertiesHelper -- getProEngageGestionnaireMessage()" );
							}
							formattedSMS =
								sviPropertiesHelper.getProEngageGestionnaireMessage(	portabiliteFormatee.getDenomination(),
																						numero,
																						portabiliteFormatee.getDateFin(),
																						portabiliteFormatee.getRio() );
						}
						// Pas gestionnaire
						else
						{
							if ( LOGGER.isDebugEnabled() )
							{
								LOGGER.debug( "Business sviPropertiesHelper -- getProEngageNonGestionnaireMessage()" );
							}
							formattedSMS =
								sviPropertiesHelper.getProEngageNonGestionnaireMessage(	portabiliteFormatee.getDenomination(),
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
								LOGGER.debug( "Business sviPropertiesHelper -- getProNonEngageGestionnaireMessage()" );
							}
							formattedSMS =
								sviPropertiesHelper.getProNonEngageGestionnaireMessage(	portabiliteFormatee.getDenomination(),
																							numero,
																							portabiliteFormatee.getRio() );
						}
						// Pas gestionnaire
						else
						{
							if ( LOGGER.isDebugEnabled() )
							{
								LOGGER.debug( "Business sviPropertiesHelper -- getProNonEngageNonGestionnaireMessage()" );
							}
							formattedSMS =
								sviPropertiesHelper.getProNonEngageNonGestionnaireMessage( portabiliteFormatee.getDenomination(),
																							numero,
																							portabiliteFormatee.getRio() );
						}
					}

				}
				// si le client est un particulier --> P dans SVI
				else if ( portabiliteFormatee.getTypeClient() == TypeClientEnum.PARTICULIER.getUssdValue() )
				{

					// Contrat engagé
					if ( portabilityInfos.isEngage() )
					{

						if ( LOGGER.isDebugEnabled() )
						{
							LOGGER.debug( "Business sviPropertiesHelper -- getParticulierEngageMessage()" );
						}
						formattedSMS =
							sviPropertiesHelper.getParticulierEngageMessage(	portabiliteFormatee.getNom(),
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
							LOGGER.debug( "Business sviPropertiesHelper -- getParticulierNonEngageMessage()" );
						}
						formattedSMS =
							sviPropertiesHelper.getParticulierNonEngageMessage(	portabiliteFormatee.getNom(),
																					portabiliteFormatee.getPrenom(),
																					portabiliteFormatee.getRio() );
					}

				}

			}
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "End Business getSVIFormatedPortabilityInfo-- message formaté =" + formattedSMS );
			}
			portabiliteFormatee.setSms(formattedSMS);
			
		}
		catch ( InterfaceUnavailableException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué: interface error", e );
			}
			throw new TechnicalErrorException( e.getMessage() );
		}
		catch ( GetPortabilityInfoException | RuntimeException  e )
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
			} else		
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "La récupération des informations de portabilité a échoué: erreur technique", e );
				}
				throw new TechnicalErrorException( e.getMessage() );
			}
		}
		return portabiliteFormatee;
	}

}
