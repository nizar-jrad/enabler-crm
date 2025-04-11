package com.orangecaraibe.enabler.handset.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orangecaraibe.enabler.handset.business.adapter.ObjetMobileAdapter;
import com.orangecaraibe.enabler.handset.business.api.Mobile;
import com.orangecaraibe.enabler.handset.business.service.MobileService;
import com.orangecaraibe.enabler.handset.data.bean.SimAppInfo;
import com.orangecaraibe.enabler.handset.data.bean.Vente;
import com.orangecaraibe.enabler.handset.data.dao.CommissionnementFindAndGetDAO;
import com.orangecaraibe.enabler.handset.data.dao.exceptions.DesimlockageDaoException;
import com.orangecaraibe.enabler.handset.data.dao.simapp.SimAppFindAndGetDAO;

@Service( "mobileService" )
public class SimAppMobileServiceImpl
	implements MobileService
{
	/* dao mobile */
	@Autowired
	@Qualifier( "simAppFindAndGetDAO" )
	protected SimAppFindAndGetDAO simAppFindAndGetDAO;

	@Autowired
	@Qualifier( "commissionnementFindAndGetDAO" )
	protected CommissionnementFindAndGetDAO commissionnementFindAndGetDAO;

	/*
	 * business adapter
	 */
	@Autowired
	protected ObjetMobileAdapter objetMobileAdapter;

	@Override
	public Mobile findAndGetMobileByImei( String imei )
		throws DesimlockageDaoException
	{

		Mobile mobile = null;
		try
		{
			SimAppInfo simAppInfo = simAppFindAndGetDAO.getSimAppInfoByImei( imei );
			Vente vente = commissionnementFindAndGetDAO.getVenteByImei( imei );
			mobile = objetMobileAdapter.createMobile( simAppInfo, vente );
			mobile.setImei( imei );

			return mobile;
		}
		catch ( Exception e )
		{
			throw new DesimlockageDaoException( e );
		}

	}

}
