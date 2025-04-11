package com.orangecaraibe.enabler;

import org.springframework.stereotype.Component;

@Component
public class InfoClient
{
	private String msisdn;

	private String customer_id;

	private String co_id;

	private String email;

	private String tmcode;

	private String custcode;

	private String idClientPublic;

	public String getMsisdn()
	{
		return msisdn;
	}

	public void setMsisdn( String msisdn )
	{
		this.msisdn = msisdn;
	}

	public String getCustomer_id()
	{
		return customer_id;
	}

	public void setCustomer_id( String customer_id )
	{
		this.customer_id = customer_id;
	}

	public String getCo_id()
	{
		return co_id;
	}

	public void setCo_id( String co_id )
	{
		this.co_id = co_id;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail( String email )
	{
		this.email = email;
	}

	public String getTmcode()
	{
		return tmcode;
	}

	public void setTmcode( String tmcode )
	{
		this.tmcode = tmcode;
	}

	/**
	 * @return the custcode
	 */
	public String getCustcode()
	{
		return custcode;
	}

	/**
	 * @param custcode the custcode to set
	 */
	public void setCustcode( String custcode )
	{
		this.custcode = custcode;
	}

	/**
	 * @return the idClientPublic
	 */
	public String getIdClientPublic()
	{
		return idClientPublic;
	}

	/**
	 * @param idClientPublic the idClientPublic to set
	 */
	public void setIdClientPublic( String idClientPublic )
	{
		this.idClientPublic = idClientPublic;
	}
}
