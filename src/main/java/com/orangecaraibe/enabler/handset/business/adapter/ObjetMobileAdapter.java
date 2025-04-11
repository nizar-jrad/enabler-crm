package com.orangecaraibe.enabler.handset.business.adapter;

import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.handset.business.api.Mobile;
import com.orangecaraibe.enabler.handset.data.bean.SimAppInfo;
import com.orangecaraibe.enabler.handset.data.bean.SimInfo;
import com.orangecaraibe.enabler.handset.data.bean.Vente;

/*
 *   classe de transfert des objets DATA  vers les objets business
 */
@Component
public class ObjetMobileAdapter
{

	public Mobile createMobile( SimInfo simInfo, Vente vente )
	{
		Mobile mobile = new Mobile();
		if ( simInfo != null )
		{
			mobile.setCodeSim( simInfo.getCode() );
			mobile.setImei( simInfo.getImei() );

		}
		if ( vente != null )
		{
			mobile.setImei( vente.getImei() );
			mobile.setDateAchat( vente.getDateAchat() );

		}
		return mobile;
	}

	public Mobile createMobile( SimAppInfo simInfo, Vente vente )
	{
		Mobile mobile = new Mobile();
		if ( simInfo != null )
		{
			mobile.setCodeSim( simInfo.getCode() );
			mobile.setImei( simInfo.getImei() );

		}
		if ( vente != null )
		{
			mobile.setImei( vente.getImei() );
			mobile.setDateAchat( vente.getDateAchat() );

		}
		return mobile;
	}
}
