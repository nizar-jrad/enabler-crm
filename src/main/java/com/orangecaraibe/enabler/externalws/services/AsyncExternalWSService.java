/**
 * 
 */
package com.orangecaraibe.enabler.externalws.services;

import java.util.List;
import java.util.concurrent.Future;

import com.orange.sidom.soa.dcrm.datacontract.InteractionCriteriaType;
import com.orangecaraibe.enabler.crm.business.bean.Interaction;
import com.orangecaraibe.enabler.crm.business.exception.InteractionFindAndGetException;
import com.orangecaraibe.enabler.crm.webservice.criteria.InteractionCriteria;

/**
 * @author ncrtest2
 */
public interface AsyncExternalWSService
{

	public Future<List<Interaction>> findAndGetInteractionAsync( InteractionCriteriaType dcrmCriteria,
																	String dcrmCriteriaValue,
																	InteractionCriteria interactionCriteria )
		throws InteractionFindAndGetException;

}
