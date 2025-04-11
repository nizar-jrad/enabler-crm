package com.orangecaraibe.enabler.crm.webservice.impl;

import java.util.concurrent.Future;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.UpdateUnknownWebcareAccountException;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.CreateResponse;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.CriteriaFindAndGet;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.DeleteResponse;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.FindAndGetResponse;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.UpdateResponse;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.UpdatedFields;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.WebcareCustomer;

/**
 * Base implementation of ManageWebcareAccount Interface.
 */
@Service( "manageWebcareAccountService" )
public class ManageWebcareAccountServiceImpl
	implements ManageWebcareAccount
{

	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageWebcareAccountServiceImpl.class );

	@Autowired
	@Qualifier( "manageWebcareAccountConsumer" )
	private ManageWebcareAccount manageWebcareAccount;

	@Override
	public void delete( com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria )
		throws DeleteException,
		com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.InterfaceUnavailableException,
		DeleteUnknownWebcareAccountException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Future<?> deleteAsync(	com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria,
									AsyncHandler<DeleteResponse> asyncHandler )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<DeleteResponse> deleteAsync( com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebcareCustomer findAndGet( CriteriaFindAndGet criteria )
	{
		return manageWebcareAccount.findAndGet( criteria );
	}

	@Override
	public Future<?> findAndGetAsync( CriteriaFindAndGet criteria, AsyncHandler<FindAndGetResponse> asyncHandler )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<FindAndGetResponse> findAndGetAsync( CriteriaFindAndGet criteria )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update( com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria,
						UpdatedFields update )
		throws com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.UpdateException,
		com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.InterfaceUnavailableException,
		UpdateUnknownWebcareAccountException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Future<?> updateAsync(	com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria,
									UpdatedFields update, AsyncHandler<UpdateResponse> asyncHandler )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<UpdateResponse> updateAsync(	com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.Criteria criteria,
													UpdatedFields update )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create( String msisdn, String email )
		throws CreateAccountPROException, CreateExistingWebcareAccountException, CreateException,
		InterfaceUnavailableException, CreateUnknownMCIBAccountException, CreateParametersException,
		CreateErrorEmailException, CreateUnauthorizedAccountException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Response<CreateResponse> createAsync( String msisdn, String email )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> createAsync( String msisdn, String email, AsyncHandler<CreateResponse> asyncHandler )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Sets the manageWebcareAccount
	 * 
	 * @param manageWebcareAccount the new manageWebcareAccount
	 */
	public void setManageWebcareAccount( ManageWebcareAccount manageWebcareAccount )
	{
		this.manageWebcareAccount = manageWebcareAccount;
	}

}
