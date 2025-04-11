package com.orangecaraibe.enabler.crm.webservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.orangecaraibe.enabler.crm.business.adapter.InteractionAdapter;
import com.orangecaraibe.enabler.crm.business.bean.Campagne;
import com.orangecaraibe.enabler.crm.business.enums.InteractionTypeEnum;
import com.orangecaraibe.enabler.crm.business.exception.InteractionFindAndGetException;
import com.orangecaraibe.enabler.crm.business.service.CampagneService;
import com.orangecaraibe.enabler.crm.business.service.InteractionService;
import com.orangecaraibe.enabler.crm.webservice.criteria.InteractionCriteria;
import com.orangecaraibe.enabler.crm.webservice.criteria.ManageInteractionUseCaseManager;
import com.orangecaraibe.enabler.crm.webservice.validator.InteractionValidator;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.CreateException;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.ManageInteraction;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.UpdateException;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.schemas.FindAndGetFault;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.schemas.InterfaceUnavailableFault;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction;
import com.orangecaraibe.soa.v2.model.commons.UseCase;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.commons.exception.FaultDetail;

@Transactional( propagation = Propagation.SUPPORTS, readOnly = false )
@Service( "manageInteractionService" )
public class ManageInteractionServiceImpl
	implements ManageInteraction
{

	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageInteractionServiceImpl.class );

	@Autowired
	@Qualifier( "interactionService" )
	private InteractionService interactionService;

	@Autowired
	private InteractionAdapter interactionAdapter;

	@Value( "${lnk_crm_bscs.partyid.enabled}" )
	private boolean linkEnabled;

	@Autowired
	private InteractionValidator interactionValidator;

	@Autowired
	@Qualifier( "campagneService" )
	private CampagneService campagneService;

	@Transactional( propagation = Propagation.REQUIRED, readOnly = false )
	@Override
	public Interaction create( Interaction interaction )
		throws CreateException, InterfaceUnavailableException
	{
		if ( linkEnabled )
		{

			try
			{
				boolean isValid = interactionValidator.createInteractionValidator( interaction );
				if ( isValid )
				{
					com.orangecaraibe.enabler.crm.business.bean.Interaction businessInteraction =
						interactionAdapter.adaptGdmInteraction( interaction );
					businessInteraction = interactionService.createInteraction( businessInteraction );
					interaction.setInteractionID( businessInteraction.getId() );
				}
			}
			catch ( RuntimeException e )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( StringUtils.EMPTY, e );
				}

				InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
				fault.setLabel( "Create Interaction error" );
				fault.setCode( "INT001" );
				InterfaceUnavailableException exception =
					new InterfaceUnavailableException( fault.getLabel(), fault, e );
				throw exception;
			}
			// TODO - exception fonctionnelle
			catch ( Exception e )
			{
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( StringUtils.EMPTY, e );
				}

				InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
				fault.setLabel( "Create Interaction error" );
				fault.setCode( "INT001" );
				InterfaceUnavailableException exception =
					new InterfaceUnavailableException( fault.getLabel(), fault, e );

				if ( StringUtils.isNotEmpty( e.getMessage() ) )
				{
					FaultDetail faultDetail = new FaultDetail();
					faultDetail.setFieldName( "message" );
					faultDetail.setFieldValue( e.getMessage() );
					fault.getFaultDetail().add( faultDetail );
				}

				throw exception;
			}

		}
		return interaction;
	}

	@Override
	public List<Interaction> findAndGet( Criteria criteria )
		throws FindAndGetException, InterfaceUnavailableException
	{

		List<Interaction> interactions = new ArrayList<Interaction>();
		if ( linkEnabled )
		{
			InteractionCriteria interactionCriteria;
			try
			{
				interactionCriteria = ManageInteractionUseCaseManager.adaptToInteractionCriteria( criteria );
			}
			catch ( Exception e )
			{
				String errorMessage = "error occurred while adapting input criteria";
				LOGGER.error( errorMessage, e );
				FindAndGetFault fault = new FindAndGetFault();
				fault.setCode( "INT002" );
				fault.setLabel( errorMessage );

				if ( StringUtils.isNotEmpty( e.getMessage() ) )
				{
					FaultDetail faultDetail = new FaultDetail();
					faultDetail.setFieldName( "message" );
					faultDetail.setFieldValue( e.getMessage() );
					fault.getFaultDetail().add( faultDetail );
				}

				FindAndGetException exception = new FindAndGetException( errorMessage, fault );
				throw exception;
			}

			try
			{
				List<com.orangecaraibe.enabler.crm.business.bean.Interaction> businessInteractions = null;

				if ( CollectionUtils.isNotEmpty( interactionCriteria.getInteractionTypes() )
					&& interactionCriteria.getInteractionTypes().contains( InteractionTypeEnum.MARKETING_CAMPAIGN ) )
				{
					Date fromDate = interactionCriteria.getFromDate();
					Date toDate = interactionCriteria.getToDate();

					List<Campagne> campagnes =
						campagneService.findAndGetCampagne( interactionCriteria.getPartyID(),
															interactionCriteria.getInstalledContractID(), fromDate,
															toDate );
					interactions = interactionAdapter.adapteMarketinCampaign( campagnes );
				}
				else
				{
					try
					{
						businessInteractions = interactionService.findAndGetInteraction( interactionCriteria );
					}
					catch ( InteractionFindAndGetException e )
					{
						LOGGER.error(	"InteractionFindAndGetException occurred while calling ManageInteraction.findAndGet EBS",
										e );
						FindAndGetFault fault = new FindAndGetFault();
						fault.setLabel( "FindAndGet Interaction error" );
						fault.setCode( "INT003" );
						FindAndGetException exception = new FindAndGetException( fault.getLabel(), fault, e );
						throw exception;
					}

					if ( businessInteractions != null )
					{
						interactions = interactionAdapter.adaptBusinessInteractions( businessInteractions );
					}

				}

			}
			catch ( RuntimeException e )
			{
				if ( LOGGER.isErrorEnabled() )
					LOGGER.error( StringUtils.EMPTY, e );

				InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
				fault.setLabel( "FindAndGet Interaction error" );
				fault.setCode( "INT003" );
				InterfaceUnavailableException exception =
					new InterfaceUnavailableException( fault.getLabel(), fault, e );
				throw exception;
			}
		}
		return interactions;
	}

	public void setInteractionService( InteractionService interactionService )
	{
		this.interactionService = interactionService;
	}

	@Override
	public Interaction update( Interaction interaction, List<UseCase> useCase )
		throws UpdateException, InterfaceUnavailableException
	{
		throw new UpdateException( "unimplemented operation", new UpdateException() );
	}

	/**
	 * @return the linkEnabled
	 */
	public boolean isLinkEnabled()
	{
		return linkEnabled;
	}

	/**
	 * @param linkEnabled the linkEnabled to set
	 */
	public void setLinkEnabled( boolean linkEnabled )
	{
		this.linkEnabled = linkEnabled;
	}

	/**
	 * @param interactionValidator the interactionValidator to set
	 */
	public void setInteractionValidator( InteractionValidator interactionValidator )
	{
		this.interactionValidator = interactionValidator;
	}

	public void setCampagneService( CampagneService campagneService )
	{
		this.campagneService = campagneService;
	}

	public void setInteractionAdapter( InteractionAdapter interactionAdapter )
	{
		this.interactionAdapter = interactionAdapter;
	}

}
