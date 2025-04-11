package com.orangecaraibe.enabler.crm.soa.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orange.drm.interfaces.webserviceoreka.v1.FaultExceptionReturn;
import com.orange.drm.interfaces.webserviceoreka.v1.Troubleticket;
import com.orange.drm.interfaces.webserviceoreka.v1.TroubleticketResponse;
import com.orange.drm.interfaces.webserviceoreka.v1.WSTroubleTicket;

/**
 * SOA WSTroubleTicket Implementation
 */
@Component
public class WSTroubleTicketProvider
	extends BaseManageProvider
	implements WSTroubleTicket
{

	private static final Logger LOGGER = LoggerFactory.getLogger( WSTroubleTicketProvider.class );

	@Autowired
	@Qualifier( "wsTroubleTicketService" )
	private WSTroubleTicket wsTroubleTicketService;

	/**
	 *
	 */
	public WSTroubleTicketProvider()
	{
		super();
		LOGGER.info( "Endpoint successfully initialized" );

	}

	@Override
	public TroubleticketResponse troubleticket( Troubleticket troubleTicket )
		throws FaultExceptionReturn
	{

		return wsTroubleTicketService.troubleticket( troubleTicket );
	}

}
