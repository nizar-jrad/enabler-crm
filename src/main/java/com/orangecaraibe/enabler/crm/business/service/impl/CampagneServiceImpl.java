package com.orangecaraibe.enabler.crm.business.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orangecaraibe.enabler.crm.business.bean.Campagne;
import com.orangecaraibe.enabler.crm.business.service.CampagneService;
import com.orangecaraibe.enabler.externalws.services.ExternalWSService;

@Service( "campagneService" )
public class CampagneServiceImpl
	implements CampagneService
{

	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( CampagneServiceImpl.class );

	@Autowired
	@Qualifier( "externalWSService" )
	private ExternalWSService externalWSService;

	@Override
	public List<Campagne> findAndGetCampagne( String partyGuid, String contractId, Date fromDate, Date toDate )
	{
		LOGGER.info( "Call findAndGetCampagne for {} and {}", partyGuid, contractId );
		return externalWSService.findAndGetCampagne( partyGuid, contractId, fromDate, toDate );
	}

}
