package com.orangecaraibe.enabler.crm.business.bean;

/**
 * Un acteur est un individu ou une organisation au sens large. Ce peut etre un party client ou un customer service
 * representative
 */
public class Actor
{

	// GUID de l'acteur (GUID du party ou du conseiller)
	private String guid;

	private String name;

	private ContactMethods contactMethods;

	public String getGuid()
	{
		return guid;
	}

	public void setGuid( String guid )
	{
		this.guid = guid;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public ContactMethods getContactMethods()
	{
		return contactMethods;
	}

	public void setContactMethods( ContactMethods contactMethods )
	{
		this.contactMethods = contactMethods;
	}

}
