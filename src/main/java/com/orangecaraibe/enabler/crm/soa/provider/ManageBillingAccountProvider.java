package com.orangecaraibe.enabler.crm.soa.provider;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.CreateException;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.CreditException;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.FindAndGetUnknownBillingAccountException;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.ManageBillingAccount;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.PublishException;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.UpdateException;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.UpdateUnknownBillingAccountException;
import com.orangecaraibe.soa.v2.model.commons.UseCase;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.AppliedDiscount;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccount;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccountStatus;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.Currency;

/**
 * SOA ManageCustomerProblem Implementation
 */
@Component
public class ManageBillingAccountProvider
	extends BaseManageProvider
	implements ManageBillingAccount
{

	private static final Logger LOGGER = LoggerFactory.getLogger( ManageBillingAccountProvider.class );

	@Autowired
	@Qualifier( "manageBillingAccountService" )
	private ManageBillingAccount manageBillingAccountService;

	/**
	 *
	 */
	public ManageBillingAccountProvider()
	{
		super();
		LOGGER.info( "Endpoint successfully initialized" );

	}

	@Override
	public BillingAccount create( BillingAccount billingAccount )
		throws InterfaceUnavailableException, CreateException
	{
		return manageBillingAccountService.create( billingAccount );
	}

	@Override
	public AppliedDiscount credit( String billingAccountID, Currency currency, AppliedDiscount adjustment )
		throws CreditException, InterfaceUnavailableException
	{
		return manageBillingAccountService.credit( billingAccountID, currency, adjustment );
	}

	@Override
	public List<BillingAccount> findAndGet( Criteria criteria )
		throws FindAndGetException, InterfaceUnavailableException, FindAndGetUnknownBillingAccountException
	{
		return manageBillingAccountService.findAndGet( criteria );
	}

	@Override
	public void publish( String billingAccountID, BillingAccountStatus billingAccountStatus )
		throws InterfaceUnavailableException, PublishException
	{
		manageBillingAccountService.publish( billingAccountID, billingAccountStatus );
	}

	@Override
	public BillingAccount update( BillingAccount billingAccount, List<UseCase> useCase )
		throws InterfaceUnavailableException, UpdateUnknownBillingAccountException, UpdateException
	{
		return manageBillingAccountService.update( billingAccount, useCase );
	}

}
