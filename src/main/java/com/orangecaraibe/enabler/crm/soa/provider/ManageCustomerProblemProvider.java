package com.orangecaraibe.enabler.crm.soa.provider;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.CreateException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.FindAndGetReasonSpecException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.ManageCustomerProblem;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.UpdateException;
import com.orangecaraibe.soa.v2.model.commons.UseCase;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblem;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.LocalReasonSpecification;

/**
 * SOA ManageCustomerProblem Implementation
 */
@Component
public class ManageCustomerProblemProvider
	extends BaseManageProvider
	implements ManageCustomerProblem
{

	private static final Logger LOGGER = LoggerFactory.getLogger( ManageCustomerProblemProvider.class );

	@Autowired
	@Qualifier( "manageCustomerProblemService" )
	private ManageCustomerProblem manageCustomerProblemService;

	/**
	 *
	 */
	public ManageCustomerProblemProvider()
	{
		super();
		LOGGER.info( "Endpoint successfully initialized" );

	}

	@Override
	public CustomerProblem create( CustomerProblem customerProblem )
		throws CreateException, InterfaceUnavailableException
	{
		return manageCustomerProblemService.create( customerProblem );
	}

	@Override
	public List<CustomerProblem> findAndGet( Criteria criteria )
		throws FindAndGetException, FindAndGetReasonSpecException, InterfaceUnavailableException
	{
		return manageCustomerProblemService.findAndGet( criteria );
	}

	@Override
	public List<LocalReasonSpecification> findAndGetReasonSpec( Criteria criteria )
		throws FindAndGetReasonSpecException, InterfaceUnavailableException
	{
		return manageCustomerProblemService.findAndGetReasonSpec( criteria );
	}

	@Override
	public CustomerProblem update( CustomerProblem customerProblem, List<UseCase> useCase )
		throws InterfaceUnavailableException, UpdateException
	{
		return manageCustomerProblemService.update( customerProblem, useCase );
	}

}
