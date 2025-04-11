package com.orangecaraibe.enabler.handset.data.dao.simapp;

import com.orangecaraibe.enabler.handset.data.bean.SimAppInfo;

public interface SimAppFindAndGetDAO
{
	/**
	 * Obtenir le code Sim à partir de l'imei
	 */
	SimAppInfo getSimAppInfoByImei( String imei );

}
