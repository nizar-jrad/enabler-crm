package com.orangecaraibe.enabler.crm.data.dao;

public interface LinkCrmBscsDAOSoa
{
	/**
	 * Renvoie le GUID CRM du titulaire a partir du customerId
	 * 
	 * @param customerId
	 * @return
	 */
	public String getPaymentResponsible( final String customerID );

}
