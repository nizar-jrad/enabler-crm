package com.orangecaraibe.enabler.crm.business.service;

import java.util.Date;
import java.util.List;

import com.orangecaraibe.enabler.crm.business.bean.Campagne;

public interface CampagneService
{

	public List<Campagne> findAndGetCampagne( String partyGuid, String contractId, Date fromDate, Date toDate );
}
