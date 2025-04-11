package com.orangecaraibe.enabler.crm.data.dao;

import com.orangecaraibe.enabler.crm.dao.bean.LinkCrmBscsBean;

public interface LinkCrmBscsDAO
{
	/**
	 * Renvoie le GUID CRM du titulaire a partir du customerId
	 * 
	 * @param customerId
	 * @return
	 */
	public LinkCrmBscsBean getCrmGuidByCustomerId( Long customerId );

}
