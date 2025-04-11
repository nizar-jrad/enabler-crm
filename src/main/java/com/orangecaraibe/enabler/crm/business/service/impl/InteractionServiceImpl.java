package com.orangecaraibe.enabler.crm.business.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orange.sidom.soa.dcrm.datacontract.InteractionCriteriaType;
import com.orangecaraibe.enabler.crm.business.bean.Interaction;
import com.orangecaraibe.enabler.crm.business.exception.InteractionCreateException;
import com.orangecaraibe.enabler.crm.business.exception.InteractionFindAndGetException;
import com.orangecaraibe.enabler.crm.business.helper.FilterInteractionHelper;
import com.orangecaraibe.enabler.crm.business.service.InteractionService;
import com.orangecaraibe.enabler.crm.webservice.criteria.InteractionCriteria;
import com.orangecaraibe.enabler.externalws.services.AsyncExternalWSService;
import com.orangecaraibe.enabler.externalws.services.ExternalWSService;
import com.orangecaraibe.enabler.externalws.services.impl.AsyncExternalWSServiceImpl;

@Service( "interactionService" )
public class InteractionServiceImpl
	implements InteractionService
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( AsyncExternalWSServiceImpl.class );

	/**
	 * Service vers les WS externes
	 */
	@Autowired
	@Qualifier( "externalWSService" )
	private ExternalWSService externalWSService;

	@Autowired
	@Qualifier( "asyncExternalWSService" )
	private AsyncExternalWSService asyncExternalWSService;

	@Autowired
	private FilterInteractionHelper filterInteractionHelper;

	/*
	 * (non-Javadoc)
	 * @see
	 * com.orangecaraibe.enabler.crm.business.service.InteractionService#findAndGetInteraction(com.orangecaraibe.enabler
	 * .crm.webservice.criteria.InteractionCriteria)
	 */
	@Override
	public List<Interaction> findAndGetInteraction( InteractionCriteria interactionCriteria )
		throws InteractionFindAndGetException
	{

		List<Interaction> interactions = new ArrayList<Interaction>();

		/*
		 * Criteres de recherche exclusifs
		 */
		// recherche par partyID
		if ( interactionCriteria.getPartyID() != null )
		{
			InteractionCriteriaType dcrmCriteria = InteractionCriteriaType.GUID_PARTY;
			String dcrmCriteriaValue = interactionCriteria.getPartyID();
			interactions =
				externalWSService.findAndGetInteraction( dcrmCriteria, dcrmCriteriaValue, interactionCriteria );
		}
		else if ( interactionCriteria.getInteractionID() != null )
		{
			// recherche par interactionID

			InteractionCriteriaType dcrmCriteria = InteractionCriteriaType.GUID_INTERACTION;
			String dcrmCriteriaValue = interactionCriteria.getInteractionID();
			interactions =
				externalWSService.findAndGetInteraction( dcrmCriteria, dcrmCriteriaValue, interactionCriteria );
		}
		else if ( interactionCriteria.getBillingAccountID() != null )
		{
			// recherche par billingAccountID
			InteractionCriteriaType dcrmCriteria = InteractionCriteriaType.CUSTOMER_ID;
			String dcrmCriteriaValue = interactionCriteria.getBillingAccountID();
			interactions =
				externalWSService.findAndGetInteraction( dcrmCriteria, dcrmCriteriaValue, interactionCriteria );

		}
		else if ( interactionCriteria.getInstalledContractID() != null )
		{
			// recherche par installedContractID
			InteractionCriteriaType dcrmCriteria = InteractionCriteriaType.CO_ID;
			String dcrmCriteriaValue = interactionCriteria.getInstalledContractID();
			interactions =
				externalWSService.findAndGetInteraction( dcrmCriteria, dcrmCriteriaValue, interactionCriteria );

		}

		Collections.sort( interactions );
		List<Interaction> interactionsResult = filterResult( interactions, interactionCriteria );

		Collections.sort( interactions );
		if ( interactionCriteria.getMaxResults() != null && interactionCriteria.getMaxResults() > 0
			&& interactionsResult.size() > interactionCriteria.getMaxResults() - 1 )
		{
			return interactionsResult.subList( 0, interactionCriteria.getMaxResults() );
		}
		return interactionsResult;
	}

	private List<Interaction> filterResult( List<Interaction> interactions, InteractionCriteria interactionCriteria )
	{
		List<Interaction> result = new ArrayList<Interaction>();
		for ( Interaction interaction : interactions )
		{
			if ( interactionCriteria.getBillingAccountID() == null
				&& interactionCriteria.getInstalledContractID() == null )
			{
				result.add( interaction );
			}
			else
			{
				if ( CollectionUtils.isNotEmpty( interaction.getRequests() ) )
				{
					Interaction inte =
						filterInteractionHelper.filterInteractionRequest( interaction, interactionCriteria );
					if ( inte != null )
					{
						result.add( inte );
					}

				}
				else
				{
					result.add( interaction );
				}
			}
		}
		return result;
	}

	@Override
	public Interaction createInteraction( com.orangecaraibe.enabler.crm.business.bean.Interaction interaction )
		throws InteractionCreateException
	{

		interaction = externalWSService.createInteraction( interaction );
		return interaction;

	}

	/**
	 * @param externalWSService
	 */
	public void setExternalWSService( ExternalWSService externalWSService )
	{
		this.externalWSService = externalWSService;
	}

	/**
	 * @param asyncExternalWSService the asyncExternalWSService to set
	 */
	public void setAsyncExternalWSService( AsyncExternalWSService asyncExternalWSService )
	{
		this.asyncExternalWSService = asyncExternalWSService;
	}

}
