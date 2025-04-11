package com.orangecaraibe.enabler.crm.soa.provider;

import java.util.concurrent.Future;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.CreateAccountPROException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.CreateErrorEmailException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.CreateException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.CreateExistingWebcareAccountException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.CreateParametersException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.CreateUnauthorizedAccountException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.CreateUnknownMCIBAccountException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.DeleteException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.DeleteUnknownWebcareAccountException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.InterfaceUnavailableException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.ManageWebcareAccount;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.CreateResponse;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.CriteriaFindAndGet;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.DeleteResponse;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.FindAndGetResponse;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.UpdateResponse;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.UpdatedFields;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.WebcareCustomer;

/**
 * SOA ManageCustomerProblem Implementation
 */
@Component
public class ManageWebcareAccountProvider
	extends BaseManageProvider
	implements ManageWebcareAccount
{

	private static final Logger LOGGER = LoggerFactory.getLogger( ManageWebcareAccountProvider.class );

	@Autowired
	@Qualifier( "manageWebcareAccountService" )
	private ManageWebcareAccount manageWebcareAccountService;

	/**
	 *
	 */
	public ManageWebcareAccountProvider()
	{
		super();
		LOGGER.info( "Endpoint successfully initialized" );

	}

	@Override
	public Response<UpdateResponse> updateAsync(	com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria,
													UpdatedFields update )
	{
		return manageWebcareAccountService.updateAsync( criteria, update );
	}

	@Override
	public Future<?> updateAsync(	com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria,
									UpdatedFields update, AsyncHandler<UpdateResponse> asyncHandler )
	{
		return manageWebcareAccountService.updateAsync( criteria, update, asyncHandler );
	}

	@Override
	public void update( com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria,
						UpdatedFields update )
	{
		manageWebcareAccountService.updateAsync( criteria, update );
	}

	@Override
	public Response<DeleteResponse> deleteAsync( com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria )
	{
		return manageWebcareAccountService.deleteAsync( criteria );
	}

	@Override
	public Future<?> deleteAsync(	com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria,
									AsyncHandler<DeleteResponse> asyncHandler )
	{
		return manageWebcareAccountService.deleteAsync( criteria, asyncHandler );
	}

	@Override
	public void delete( com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria )
		throws DeleteException,
		com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.InterfaceUnavailableException,
		DeleteUnknownWebcareAccountException
	{
		manageWebcareAccountService.delete( criteria );
	}

	@Override
	public Response<FindAndGetResponse> findAndGetAsync( CriteriaFindAndGet criteria )
	{
		return manageWebcareAccountService.findAndGetAsync( criteria );
	}

	@Override
	public Future<?> findAndGetAsync( CriteriaFindAndGet criteria, AsyncHandler<FindAndGetResponse> asyncHandler )
	{
		return manageWebcareAccountService.findAndGetAsync( criteria, asyncHandler );
	}

	@Override
	public WebcareCustomer findAndGet( CriteriaFindAndGet criteria )
	{
		return manageWebcareAccountService.findAndGet( criteria );
	}

	@Override
	public void create( String msisdn, String email )
		throws CreateAccountPROException, CreateExistingWebcareAccountException, CreateException,
		InterfaceUnavailableException, CreateUnknownMCIBAccountException, CreateParametersException,
		CreateErrorEmailException, CreateUnauthorizedAccountException
	{
		manageWebcareAccountService.create( msisdn, email );
	}

	@Override
	public Response<CreateResponse> createAsync( String msisdn, String email )
	{
		return manageWebcareAccountService.createAsync( msisdn, email );
	}

	@Override
	public Future<?> createAsync( String msisdn, String email, AsyncHandler<CreateResponse> asyncHandler )
	{
		return manageWebcareAccountService.createAsync( msisdn, email, asyncHandler );
	}

}
