/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.service;

import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.InterfaceUnavailableException;

/**
 * @author NCR
 */
public interface TroubleTicketService
{

	public Request createTroubleTicket( Request request, String cuid )
		throws CustomerProblemUpdateException, InterfaceUnavailableException;

	public void updateStatusTicket( Request request );

	public Request updateTroubleTicket( Request request, String cuid )
		throws CustomerProblemUpdateException;

}
