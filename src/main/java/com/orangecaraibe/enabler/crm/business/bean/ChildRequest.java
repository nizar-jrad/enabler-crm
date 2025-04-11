package com.orangecaraibe.enabler.crm.business.bean;

public class ChildRequest
	extends Request
{

	private Request demandeMere;

	public Request getDemandeMere()
	{
		return demandeMere;
	}

	public void setDemandeMere( Request demandeMere )
	{
		this.demandeMere = demandeMere;
	}

}
