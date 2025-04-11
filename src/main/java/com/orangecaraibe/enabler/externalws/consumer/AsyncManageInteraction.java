/**
 * 
 */
package com.orangecaraibe.enabler.externalws.consumer;

import java.util.concurrent.Future;

import com.orange.sidom.soa.dcrm.datacontract.FilterCriteriaInteraction;
import com.orange.sidom.soa.dcrm.datacontract.FindAndGetInteractionResponse;
import com.orange.sidom.soa.dcrm.datacontract.InteractionCriteriaType;
import com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteractionFindAndGetServiceFaultFaultFaultMessage;

/**
 * @author ncrtest2
 */
public interface AsyncManageInteraction
{

	public Future<FindAndGetInteractionResponse> findAndGetAsync( InteractionCriteriaType criteria, String value,
																	FilterCriteriaInteraction additionalCriteria )
		throws ManageInteractionFindAndGetServiceFaultFaultFaultMessage;
}
