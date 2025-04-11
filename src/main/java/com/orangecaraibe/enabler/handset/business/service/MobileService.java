package com.orangecaraibe.enabler.handset.business.service;

import com.orangecaraibe.enabler.handset.business.api.Mobile;

/*
 *  recherche du mobile par son code IMEI
 */
public interface MobileService
{

	public Mobile findAndGetMobileByImei( String imei );

}
