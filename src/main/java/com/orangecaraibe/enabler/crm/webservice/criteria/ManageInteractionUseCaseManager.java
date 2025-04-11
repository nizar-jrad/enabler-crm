package com.orangecaraibe.enabler.crm.webservice.criteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionStatus;
import com.orangecaraibe.enabler.crm.business.enums.InteractionTypeEnum;
import com.orangecaraibe.enabler.crm.business.enums.InteractionWayEnum;
import com.orangecaraibe.enabler.crm.business.enums.MediaEnum;
import com.orangecaraibe.soa.v2.criteria.CriteriaComposer;
import com.orangecaraibe.soa.v2.criteria.Variations;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.LocalCRMInteractionStatusCode;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;

/**
 * Cette classe permet d'adapter les criteria selon les use cases d'appel a l'EBS ManageInteraction.findANdGet
 */
@Component
public class ManageInteractionUseCaseManager
{

	private final static Logger LOGGER = LoggerFactory.getLogger( ManageInteractionUseCaseManager.class );

	private final static Map<String, EnumInteractionStatus> interactionStatusMap =
		new HashMap<String, EnumInteractionStatus>();
	static
	{
		interactionStatusMap.put(	LocalCRMInteractionStatusCode.IN_PROGRESS.value(),
									EnumInteractionStatus.IN_PROGRESS );
		interactionStatusMap.put( LocalCRMInteractionStatusCode.COMPLETED.value(), EnumInteractionStatus.COMPLETED );
		interactionStatusMap.put( LocalCRMInteractionStatusCode.CANCELLED.value(), EnumInteractionStatus.CANCELLED );
	}

	/**
	 * Controle et adapte les criteres dans le cadre d'une recherche d'interaction
	 * 
	 * @param criteria
	 * @return the InteractionCriteria
	 * @throws Exception
	 */
	public static InteractionCriteria adaptToInteractionCriteria( Criteria criteria )
		throws Exception
	{
		Map<String, Object> properties = CriteriaComposer.extract( criteria );
		InteractionCriteria interactionCriteria = new InteractionCriteria();

		/** identifiant d'interaction : */
		String interactionID = (String) properties.get( CriteriaConstants.INTERACTION_ID );
		LOGGER.debug( CriteriaConstants.LOG_CRITERIA_LABEL + CriteriaConstants.INTERACTION_ID + ":" + interactionID );
		interactionCriteria.setInteractionID( interactionID );

		/** identifiant du Party */
		String partyID = (String) properties.get( CriteriaConstants.PARTY_ID );
		LOGGER.debug( CriteriaConstants.LOG_CRITERIA_LABEL + CriteriaConstants.PARTY_ID + ":" + partyID );
		interactionCriteria.setPartyID( partyID );

		/** identifiant de contrat */
		String installedContractID = (String) properties.get( CriteriaConstants.INSTALLED_CONTRACT_ID );
		LOGGER.debug( CriteriaConstants.LOG_CRITERIA_LABEL + CriteriaConstants.INSTALLED_CONTRACT_ID + ":"
			+ installedContractID );
		interactionCriteria.setInstalledContractID( installedContractID );

		/** identifiant du compte de facturation */
		String billingAccountID = (String) properties.get( CriteriaConstants.BILLING_ACCOUNT_ID );
		LOGGER.debug( CriteriaConstants.LOG_CRITERIA_LABEL + CriteriaConstants.BILLING_ACCOUNT_ID + ":"
			+ billingAccountID );
		interactionCriteria.setBillingAccountID( billingAccountID );

		/** Filtre sur la periode de recherche */
		// date from
		XMLGregorianCalendar fromDateXml = (XMLGregorianCalendar) properties.get( CriteriaConstants.FROM_DATE );
		if ( fromDateXml != null )
		{
			try
			{
				fromDateXml = (XMLGregorianCalendar) properties.get( CriteriaConstants.FROM_DATE );
				LOGGER.debug( CriteriaConstants.LOG_CRITERIA_LABEL + CriteriaConstants.FROM_DATE + ":" + fromDateXml );
				Date fromDate = DateUtils.getDateFromXmlGregorianCalendar( fromDateXml );
				interactionCriteria.setFromDate( fromDate );
			}
			catch ( Exception e )
			{
				throw new Exception( "type ou valeur du critere fromDate invalide" );
			}
		}
		// date to
		XMLGregorianCalendar toDateXml = (XMLGregorianCalendar) properties.get( CriteriaConstants.TO_DATE );
		if ( toDateXml != null )
		{
			try
			{
				toDateXml = (XMLGregorianCalendar) properties.get( CriteriaConstants.TO_DATE );
				LOGGER.debug( CriteriaConstants.LOG_CRITERIA_LABEL + CriteriaConstants.TO_DATE + ":" + toDateXml );
				Date toDate = DateUtils.getDateFromXmlGregorianCalendar( toDateXml );
				interactionCriteria.setToDate( toDate );
			}
			catch ( Exception e )
			{
				throw new Exception( "type ou valeur du critere toDate invalide" );
			}
		}

		/** Inclusion des demandes rattachees aux interactions */
		Criteria includeCustomerProblemsVariation =
			CriteriaComposer.getInstance().add( Variations.include( CriteriaConstants.CUSTOMER_PROBLEMS ) ).getCriteria();

		boolean includeCustomerProblems = CriteriaComposer.isUseCase( criteria, includeCustomerProblemsVariation );
		interactionCriteria.setIncludeCustomerProblems( includeCustomerProblems );

		/** Inclusion des resolutionAction rattachees aux interactions */

		Criteria includeResolutionActionVariation =
			CriteriaComposer.getInstance().add( Variations.include( CriteriaConstants.RESOLUTION_ACTION ) ).getCriteria();
		boolean isOnlyResolutionAction = CriteriaComposer.isUseCase( criteria, includeResolutionActionVariation );
		interactionCriteria.setIsOnlyResolutionAction( isOnlyResolutionAction );

		/** Inclusion des notes rattachees aux interactions */
		Criteria includeNotesVariation =
			CriteriaComposer.getInstance().add( Variations.include( CriteriaConstants.NOTES ) ).getCriteria();

		boolean includeNotes = CriteriaComposer.isUseCase( criteria, includeNotesVariation );
		interactionCriteria.setIncludeNotes( includeNotes );

		/** Inclusion des pieces jointes aux interactions */
		Criteria includeAttachmentsVariation =
			CriteriaComposer.getInstance().add( Variations.include( CriteriaConstants.ATTACHMENTS ) ).getCriteria();

		boolean includeAttachments = CriteriaComposer.isUseCase( criteria, includeAttachmentsVariation );
		interactionCriteria.setIncludeAttachments( includeAttachments );

		/** Filtre sur le media de l'interaction */
		List<MediaEnum> medias = null;
		if ( properties.get( CriteriaConstants.MEDIA ) != null
			&& ( properties.get( CriteriaConstants.MEDIA ) instanceof ArrayList ) )
		{
			List<String> mediaList = (List<String>) properties.get( CriteriaConstants.MEDIA );
			for ( String media : mediaList )
			{
				MediaEnum mediaEnum = MediaEnum.getMediaEnum( media );
				if ( mediaEnum != null )
				{
					if ( medias == null )
					{
						medias = new ArrayList<MediaEnum>();
					}
					medias.add( mediaEnum );
				}
				else
				{
					LOGGER.warn( "le media passe en parametre n'a pu etre mappe :" + media );
				}
			}

			interactionCriteria.setMedias( medias );

		}

		/** Filtre sur le sens de l'interaction */
		String interactionWay = (String) properties.get( CriteriaConstants.INTERACTION_WAY );
		InteractionWayEnum wayEnum = InteractionWayEnum.getInteractionWayEnumByWay( interactionWay );
		if ( wayEnum != null )
		{
			interactionCriteria.setInteractionWay( wayEnum );
		}

		/** Filtre sur le statut de l'interaction */
		if ( properties.get( CriteriaConstants.INTERACTION_STATUS ) != null
			&& ( properties.get( CriteriaConstants.INTERACTION_STATUS ) instanceof ArrayList ) )
		{
			List<EnumInteractionStatus> interactionStatusList = new ArrayList<EnumInteractionStatus>();
			List<String> interactionStatusCriteriaList =
				(List<String>) properties.get( CriteriaConstants.INTERACTION_STATUS );
			for ( String interactionStatusCriteria : interactionStatusCriteriaList )
			{
				if ( interactionStatusMap.containsKey( interactionStatusCriteria ) )
				{
					interactionStatusList.add( interactionStatusMap.get( interactionStatusCriteria ) );
				}
				else
				{
					LOGGER.warn( "le status passe en parametre n'a pu etre mappe :" + interactionStatusCriteria );
				}
			}

			interactionCriteria.setInteractionStatusList( interactionStatusList );

		}

		/** Filtre sur le type de l'interaction */
		List<InteractionTypeEnum> interactionTypeEnumList = null;
		if ( properties.get( CriteriaConstants.INTERACTION_TYPE ) != null
			&& ( properties.get( CriteriaConstants.INTERACTION_TYPE ) instanceof ArrayList ) )
		{
			List<String> interactionTypeList = (List<String>) properties.get( CriteriaConstants.INTERACTION_TYPE );
			for ( String interactionType : interactionTypeList )
			{
				InteractionTypeEnum interactionTypeEnum =
					InteractionTypeEnum.getInteractionTypeEnumByType( interactionType );
				if ( interactionTypeEnum != null )
				{
					if ( interactionTypeEnumList == null )
					{
						interactionTypeEnumList = new ArrayList<InteractionTypeEnum>();
					}
					interactionTypeEnumList.add( interactionTypeEnum );
				}
				else
				{
					LOGGER.warn( "le type passe en parametre n'a pu etre mappe :" + interactionType );
				}
			}

			interactionCriteria.setInteractionTypes( interactionTypeEnumList );

		}

		/** Limitation du nombre de resultats */
		String maxResults = (String) properties.get( CriteriaConstants.MAX_RESULT );
		LOGGER.debug( CriteriaConstants.LOG_CRITERIA_LABEL + CriteriaConstants.MAX_RESULT + ":" + maxResults );
		if ( StringUtils.isNotEmpty( maxResults ) )
		{
			if ( StringUtils.isNumeric( maxResults ) )
			{
				interactionCriteria.setMaxResults( Integer.parseInt( maxResults ) );
			}
			else
			{
				throw new Exception( "valeur du critere maxResults invalide" );
			}
		}

		return interactionCriteria;

	}
}
