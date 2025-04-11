/**
 * 
 */
package com.orangecaraibe.enabler.externalws.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfint;
import com.orange.sidom.soa.dcrm.datacontract.ArrayOfTypeInteraction;
import com.orange.sidom.soa.dcrm.datacontract.FilterCriteriaInteraction;
import com.orange.sidom.soa.dcrm.datacontract.FilterCriteriaNotes;
import com.orange.sidom.soa.dcrm.datacontract.FindAndGetInteractionResponse;
import com.orange.sidom.soa.dcrm.datacontract.InteractionCriteriaType;
import com.orange.sidom.soa.dcrm.datacontract.ObjectFactory;
import com.orange.sidom.soa.dcrm.datacontract.TypeInteraction;
import com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteraction;
import com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteractionFindAndGetServiceFaultFaultFaultMessage;
import com.orangecaraibe.enabler.core.helpers.AsyncExecutionHelper;
import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.crm.business.bean.Interaction;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionStatus;
import com.orangecaraibe.enabler.crm.business.enums.InteractionTypeEnum;
import com.orangecaraibe.enabler.crm.business.enums.InteractionWayEnum;
import com.orangecaraibe.enabler.crm.business.enums.MediaEnum;
import com.orangecaraibe.enabler.crm.business.exception.InteractionFindAndGetException;
import com.orangecaraibe.enabler.crm.webservice.criteria.InteractionCriteria;
import com.orangecaraibe.enabler.externalws.adapters.WSCrmAdapter;
import com.orangecaraibe.enabler.externalws.services.AsyncExternalWSService;
import com.orangecaraibe.enabler.utils.logging.EnabledCallable;

@Service( "asyncExternalWSService" )
public class AsyncExternalWSServiceImpl
	implements AsyncExternalWSService
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( AsyncExternalWSServiceImpl.class );

	private final static Map<MediaEnum, TypeInteraction> mediaMap = new HashMap<MediaEnum, TypeInteraction>();
	static
	{
		mediaMap.put( MediaEnum.Phone, TypeInteraction.APPEL_TELEPHONIQUE );
		mediaMap.put( MediaEnum.Letter, TypeInteraction.COURRIER );
		mediaMap.put( MediaEnum.Email, TypeInteraction.EMAIL );
		mediaMap.put( MediaEnum.Fax, TypeInteraction.FAX );
		mediaMap.put( MediaEnum.Shop, TypeInteraction.PDV_PHYSIQUE );
		mediaMap.put( MediaEnum.SocialNetwork, TypeInteraction.RESEAU_SOCIAL );
		mediaMap.put( MediaEnum.Selfcare, TypeInteraction.SELFCARE );
		mediaMap.put( MediaEnum.SMS, TypeInteraction.SMS );
	}

	/**
	 * Client WS vers le service de gestion des interactions DCRM
	 */
	@Autowired
	@Qualifier( "manageDCRMInteractionConsumer" )
	private ManageInteraction manageInteractionConsumer;

	@Autowired
	@Qualifier( "core.helper.asyncExecutionHelper" )
	private AsyncExecutionHelper asyncExecutionHelper;

	/** adapter pour le modele objet DCRM */
	@Autowired
	@Qualifier( "wsCrmAdapter" )
	private WSCrmAdapter wsCrmAdapter;

	/**
	 * ObjectFactory pour la creation des criteria
	 */
	@Autowired
	@Qualifier( "objectFactoryDCRM" )
	private ObjectFactory objectFactory;

	@Override
	public Future<List<Interaction>> findAndGetInteractionAsync(	InteractionCriteriaType dcrmCriteria,
																	String dcrmCriteriaValue,
																	InteractionCriteria interactionCriteria )
		throws InteractionFindAndGetException
	{

		Callable<List<Interaction>> c = new FindAndGetCallable( dcrmCriteria, dcrmCriteriaValue, interactionCriteria );
		EnabledCallable<List<Interaction>> ec = new EnabledCallable<List<Interaction>>( c );
		return asyncExecutionHelper.executeTask( ec );
	}

	private class FindAndGetCallable
		implements Callable<List<Interaction>>
	{
		private final InteractionCriteriaType dcrmCriteria;

		private final String dcrmCriteriaValue;

		private final InteractionCriteria interactionCriteria;

		public FindAndGetCallable(	InteractionCriteriaType dcrmCriteria, String dcrmCriteriaValue,
									InteractionCriteria interactionCriteria )
		{
			this.dcrmCriteria = dcrmCriteria;
			this.dcrmCriteriaValue = dcrmCriteriaValue;
			this.interactionCriteria = interactionCriteria;
		}

		@Override
		public List<Interaction> call()
			throws Exception
		{
			return findAndGetInteraction( dcrmCriteria, dcrmCriteriaValue, interactionCriteria );
		}

		private List<Interaction> findAndGetInteraction(	InteractionCriteriaType dcrmCriteria, String dcrmCriteriaValue,
															InteractionCriteria interactionCriteria )
			throws InteractionFindAndGetException
		{

			LOGGER.debug( "recherche des interaction asynchrone" );
			List<Interaction> interactions = new ArrayList<Interaction>();

			/*
			 * filtres additionnels
			 */
			FilterCriteriaInteraction dcrmAdditionalCriteria = new FilterCriteriaInteraction();

			// filtre sur le sens de l'interaction
			if ( interactionCriteria.getInteractionWay() == InteractionWayEnum.IN )
			{
				dcrmAdditionalCriteria.setDirection( objectFactory.createFilterCriteriaInteractionDirection( false ) );
			}
			else if ( interactionCriteria.getInteractionWay() == InteractionWayEnum.OUT )
			{
				dcrmAdditionalCriteria.setDirection( objectFactory.createFilterCriteriaInteractionDirection( true ) );
			}

			// filtre sur le statut de l'interaction
			if ( CollectionUtils.isNotEmpty( interactionCriteria.getInteractionStatusList() ) )
			{
				ArrayOfint arrayOfStatus = new ArrayOfint();
				for ( EnumInteractionStatus status : interactionCriteria.getInteractionStatusList() )
				{
					Integer dcrmStatus = MapUtils.invertMap( WSCrmAdapter.interactionStatusMap ).get( status );
					arrayOfStatus.getInt().add( dcrmStatus );
				}
				JAXBElement<ArrayOfint> jaxbElementStatus =
					objectFactory.createFilterCriteriaInteractionStatuts( arrayOfStatus );
				dcrmAdditionalCriteria.setStatuts( jaxbElementStatus );
			}

			// filtre sur les medias de l'interaction
			if ( CollectionUtils.isNotEmpty( interactionCriteria.getMedias() ) )
			{
				ArrayOfTypeInteraction arrayOfTypeInteraction = new ArrayOfTypeInteraction();
				for ( MediaEnum media : interactionCriteria.getMedias() )
				{
					TypeInteraction dcrmInteractionType = mediaMap.get( media );
					arrayOfTypeInteraction.getTypeInteraction().add( dcrmInteractionType );
				}
				JAXBElement<ArrayOfTypeInteraction> filterCriteriaInteractionMedia =
					objectFactory.createFilterCriteriaInteractionMedia( arrayOfTypeInteraction );

				dcrmAdditionalCriteria.setMedia( filterCriteriaInteractionMedia );
			}

			// periode de recherche - fromDate
			if ( interactionCriteria.getFromDate() != null )
			{
				dcrmAdditionalCriteria.setFromDate( objectFactory.createFilterCriteriaInteractionFromDate( DateUtils.convertDateToXMLGregorianCalendar( interactionCriteria.getFromDate() ) ) );
			}
			// periode de recherche - toDate
			if ( interactionCriteria.getToDate() != null )
			{
				dcrmAdditionalCriteria.setToDate( objectFactory.createFilterCriteriaInteractionToDate( DateUtils.convertDateToXMLGregorianCalendar( interactionCriteria.getToDate() ) ) );
			}

			// limitation du nombre max d'enregistrements
			if ( interactionCriteria.getMaxResults() != null && ( interactionCriteria.getMaxResults() > 0 ) )
			{
				dcrmAdditionalCriteria.setNombreMaxEnregistrements( objectFactory.createFilterCriteriaInteractionNombreMaxEnregistrements( interactionCriteria.getMaxResults() ) );
			}

			// inclusion des notes et attachements
			if ( interactionCriteria.isIncludeNotes() || interactionCriteria.isIncludeAttachments() )
			{
				FilterCriteriaNotes filterNote = new FilterCriteriaNotes();
				filterNote.setGetNote( objectFactory.createFilterCriteriaNotesGetNote( interactionCriteria.isIncludeNotes() ) );
				if ( interactionCriteria.isIncludeAttachments() )
				{
					filterNote.setGetNote( objectFactory.createFilterCriteriaNotesGetNote( true ) );
				}

				filterNote.setGetPieceJointe( objectFactory.createFilterCriteriaNotesGetPieceJointe( interactionCriteria.isIncludeAttachments() ) );
				dcrmAdditionalCriteria.setFilterNotes( objectFactory.createFilterCriteriaInteractionFilterNotes( filterNote ) );
			}

			// inclusion des demandes liees
			if ( interactionCriteria.isIncludeCustomerProblems() )
			{
				dcrmAdditionalCriteria.setGetDemandes( objectFactory.createFilterCriteriaInteractionGetDemandes( interactionCriteria.isIncludeCustomerProblems() ) );
			}

			// inclusion des campagnes liees
			if ( CollectionUtils.isNotEmpty( interactionCriteria.getInteractionTypes() ) )
			{
				for ( InteractionTypeEnum interactionType : interactionCriteria.getInteractionTypes() )
				{
					if ( interactionType == InteractionTypeEnum.MARKETING_CAMPAIGN )
					{
						dcrmAdditionalCriteria.setGetAvecCampagnesLiees( objectFactory.createFilterCriteriaInteractionGetAvecCampagnesLiees( interactionType == InteractionTypeEnum.MARKETING_CAMPAIGN ) );
					}
				}
			}

			// inclusion des mesures financières
			if ( interactionCriteria.isOnlyResolutionAction() )
			{
				dcrmAdditionalCriteria.setGetOnlyMesFinDemande( true );
			}

			/*
			 * Appel au WS DCRM et adaptation de la reponse
			 */
			try
			{
				// appel au WS DCRM
				FindAndGetInteractionResponse findAndGetInteractionResponse =
					manageInteractionConsumer.findAndGet( dcrmCriteria, dcrmCriteriaValue, dcrmAdditionalCriteria );
				// adaptation de la reponse renvoyee par le WS DCRM
				interactions = wsCrmAdapter.adaptFindAndGetInteractionResponse( findAndGetInteractionResponse );
			}
			catch ( ManageInteractionFindAndGetServiceFaultFaultFaultMessage e )
			{
				LOGGER.error( "erreur lors de la recherche des interactions dans le DCRM", e );
				String message = e.getMessage();
				if ( e.getFaultInfo() != null && e.getFaultInfo().getMessage() != null
					&& StringUtils.isNotEmpty( e.getFaultInfo().getMessage().getValue() ) )
				{
					message += " message:" + e.getFaultInfo().getMessage().getValue();
				}
				InteractionFindAndGetException interactionFindAndGetException =
					new InteractionFindAndGetException( "INT012 Erreur lors de l'appel au web service DCRM : "
						+ message, "INT013" );
				throw interactionFindAndGetException;
			}

			return interactions;
		}
	}
}
