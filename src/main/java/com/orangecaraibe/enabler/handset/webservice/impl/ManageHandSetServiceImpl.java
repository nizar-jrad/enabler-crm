package com.orangecaraibe.enabler.handset.webservice.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.orangecaraibe.enabler.handset.business.api.Mobile;
import com.orangecaraibe.enabler.handset.business.service.MobileService;
import com.orangecaraibe.enabler.handset.data.dao.exceptions.DesimlockageDaoException;
import com.orangecaraibe.enabler.handset.webservice.adapter.StringInfoMobileAdapter;
import com.orangecaraibe.soa.v1_2.interfaces.managehandset.ManageHandSet;

/**
 * Base implementation of CustomerProblemService Interface.
 */
@Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
@Service( "manageHandSetService" )
public class ManageHandSetServiceImpl
	implements ManageHandSet
{

	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( ManageHandSetServiceImpl.class );

	private static final String EPOINT_ERROR_RETURN_CODE = "-5|||";

	/** The StringInfoMobile adapter. */
	@Autowired
	private StringInfoMobileAdapter stringInfoMobileAdapter;

	/** the business service */
	@Autowired
	@Qualifier( "mobileService" )
	MobileService mobileService;

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.soa.v1_2.interfaces.managecustomerproblem.ManageHandSet#findAndGetMobileByImei(String)
	 */

	@Override
	public String findAndGetMobileByImei( String imei )
	{
		String result = "";
		if ( LOGGER.isDebugEnabled() )
			LOGGER.debug( "findAndGetMobileByImei" );

		try
		{

			Mobile mobile = mobileService.findAndGetMobileByImei( imei );
			result = stringInfoMobileAdapter.getReponseDesimlockage( mobile );

			return result;

		}
		catch ( DesimlockageDaoException ex )
		{
			if ( LOGGER.isDebugEnabled() )
				LOGGER.error( "", ex );
			return "-5|" + ex.getMessage();
		}
		catch ( RuntimeException e )
		{
			// Epoint de gere pas les exception on renvoi un code spécial
			if ( LOGGER.isErrorEnabled() )
				LOGGER.error( "", e );
			return EPOINT_ERROR_RETURN_CODE;
		}

	}

}
