package com.orangecaraibe.enabler.crm.business.bean;

/**
 * Classe du model contenant les données adressses
 * 
 * @author NCR
 */
public class Address
{

	/** Numero de la rue */
	public String streetNum;

	/** Nom de la rue */
	public String streetName;

	/** Code postal */
	public String zip;

	/** Ville */
	public String city;

	/** Pays */
	public String country;

	/** detail 1 de l'adresse */
	public String details1;

	/** detail 2 de l'adresse */
	public String details2;

	/** detail 3 de l'adresse */
	public String details3;

	/** L'adresse est verifiee */
	public Boolean isNpai;

	public String getStreetNum()
	{
		return streetNum;
	}

	public void setStreetNum( String streetNum )
	{
		this.streetNum = streetNum;
	}

	public String getStreetName()
	{
		return streetName;
	}

	public void setStreetName( String streetName )
	{
		this.streetName = streetName;
	}

	public String getZip()
	{
		return zip;
	}

	public void setZip( String zip )
	{
		this.zip = zip;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity( String city )
	{
		this.city = city;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry( String country )
	{
		this.country = country;
	}

	public String getDetails1()
	{
		return details1;
	}

	public void setDetails1( String details1 )
	{
		this.details1 = details1;
	}

	public String getDetails2()
	{
		return details2;
	}

	public void setDetails2( String details2 )
	{
		this.details2 = details2;
	}

	public String getDetails3()
	{
		return details3;
	}

	public void setDetails3( String details3 )
	{
		this.details3 = details3;
	}

	public Boolean getIsNpai()
	{
		return isNpai;
	}

	public void setIsNpai( Boolean isNpai )
	{
		this.isNpai = isNpai;
	}

}
