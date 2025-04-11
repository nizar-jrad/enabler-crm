/**
 *
 */
package com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO.api;

import org.springframework.stereotype.Component;

/**
 * Classe utilisée pour porter les cas de tests de CreateOrderRemoveService
 * 
 * @author sgodard
 */
@Component
@Deprecated
public class RemoveService
{
	private String customerId;

	private String coId;

	private String msisdn;

	private String tmCode;

	private String snCode;

	private String spCode;

	/**
	 *
	 */
	public RemoveService()
	{

	}

	/**
	 * @return the coId
	 */
	public String getCoId()
	{
		return coId;
	}

	/**
	 * @param coId the coId to set
	 */
	public void setCoId( String coId )
	{
		this.coId = coId;
	}

	/**
	 * @return the msisdn
	 */
	public String getMsisdn()
	{
		return msisdn;
	}

	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn( String msisdn )
	{
		this.msisdn = msisdn;
	}

	/**
	 * @return the tmCode
	 */
	public String getTmCode()
	{
		return tmCode;
	}

	/**
	 * @param tmCode the tmCode to set
	 */
	public void setTmCode( String tmCode )
	{
		this.tmCode = tmCode;
	}

	/**
	 * @return the snCode
	 */
	public String getSnCode()
	{
		return snCode;
	}

	/**
	 * @param snCode the snCode to set
	 */
	public void setSnCode( String snCode )
	{
		this.snCode = snCode;
	}

	/**
	 * @return the spCode
	 */
	public String getSpCode()
	{
		return spCode;
	}

	/**
	 * @param spCode the spCode to set
	 */
	public void setSpCode( String spCode )
	{
		this.spCode = spCode;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId( String customerId )
	{
		this.customerId = customerId;
	}

}
