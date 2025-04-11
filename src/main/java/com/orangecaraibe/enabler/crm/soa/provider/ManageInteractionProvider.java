package com.orangecaraibe.enabler.crm.soa.provider;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.soa.v2.interfaces.manageinteraction.CreateException;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.ManageInteraction;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.UpdateException;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction;
import com.orangecaraibe.soa.v2.model.commons.UseCase;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;

/**
 * SOA ManageInteraction Implementation
 */
@Component
public class ManageInteractionProvider
	extends BaseManageProvider
	implements ManageInteraction
{

	private static final Logger LOGGER = LoggerFactory.getLogger( ManageInteractionProvider.class );

	@Autowired
	@Qualifier( "manageInteractionService" )
	private ManageInteraction manageInteractionService;

	/**
	 *
	 */
	public ManageInteractionProvider()
	{
		super();
		LOGGER.info( "Endpoint successfully initialized" );

	}

	@Override
	public Interaction create( Interaction interaction )
		throws CreateException, InterfaceUnavailableException
	{
		return manageInteractionService.create( interaction );
	}

	@Override
	public List<Interaction> findAndGet( Criteria criteria )
		throws FindAndGetException, InterfaceUnavailableException
	{
		return manageInteractionService.findAndGet( criteria );
	}

	@Override
	public Interaction update( Interaction interaction, List<UseCase> useCase )
		throws UpdateException, InterfaceUnavailableException
	{
		return manageInteractionService.update( interaction, useCase );
	}

}
