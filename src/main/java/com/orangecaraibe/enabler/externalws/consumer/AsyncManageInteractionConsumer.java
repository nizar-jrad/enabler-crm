/**
 * 
 */
package com.orangecaraibe.enabler.externalws.consumer;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orange.sidom.soa.dcrm.datacontract.FilterCriteriaInteraction;
import com.orange.sidom.soa.dcrm.datacontract.FindAndGetInteractionResponse;
import com.orange.sidom.soa.dcrm.datacontract.InteractionCriteriaType;
import com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteraction;
import com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteractionFindAndGetServiceFaultFaultFaultMessage;
import com.orangecaraibe.enabler.core.helpers.AsyncExecutionHelper;
import com.orangecaraibe.enabler.utils.logging.EnabledCallable;

/**
 * @author ncrtest2
 */
@Component( "asyncManageInteractionConsumer" )
public class AsyncManageInteractionConsumer
	implements AsyncManageInteraction
{

	@Autowired
	private ManageInteraction manageInteraction;

	@Autowired
	@Qualifier( "core.helper.asyncExecutionHelper" )
	private AsyncExecutionHelper asyncExecutionHelper;

	@Override
	public Future<FindAndGetInteractionResponse> findAndGetAsync(	InteractionCriteriaType criteria, String value,
																	FilterCriteriaInteraction additionalCriteria )
		throws ManageInteractionFindAndGetServiceFaultFaultFaultMessage
	{
		Callable<FindAndGetInteractionResponse> c = new FindAndGetCallable( criteria, value, additionalCriteria );
		EnabledCallable<FindAndGetInteractionResponse> ec = new EnabledCallable<FindAndGetInteractionResponse>( c );
		return asyncExecutionHelper.executeTask( ec );
	}

	private class FindAndGetCallable
		implements Callable<FindAndGetInteractionResponse>
	{
		private final InteractionCriteriaType criteria;

		private final String value;

		private final FilterCriteriaInteraction additionalCriteria;

		public FindAndGetCallable(	InteractionCriteriaType criteria, String value,
									FilterCriteriaInteraction additionalCriteria )
		{
			this.criteria = criteria;
			this.value = value;
			this.additionalCriteria = additionalCriteria;
		}

		@Override
		public FindAndGetInteractionResponse call()
			throws Exception
		{
			return findAndGet( criteria, value, additionalCriteria );
		}
	}

	private FindAndGetInteractionResponse findAndGet(	InteractionCriteriaType criteria, String value,
														FilterCriteriaInteraction additionalCriteria )
		throws ManageInteractionFindAndGetServiceFaultFaultFaultMessage
	{
		return manageInteraction.findAndGet( criteria, value, additionalCriteria );
	}

}
