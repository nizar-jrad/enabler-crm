package com.orangecaraibe.enabler.crm.business.bean;

/**
 * Classe des données bancaires
 * 
 * @author NCR
 */
public class BankDetails
{

	/** IBAN de a banque */
	private String iban;

	/** BIC de la banque */
	private String bic;

	/** Nom de la banque */
	private String name;

	/** Numero rum de la bank */
	private String rum;

	/** Pays de la banque */
	private String country;

	public String getIban()
	{
		return iban;
	}

	public void setIban( String iban )
	{
		this.iban = iban;
	}

	public String getBic()
	{
		return bic;
	}

	public void setBic( String bic )
	{
		this.bic = bic;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry( String country )
	{
		this.country = country;
	}

	public String getRum()
	{
		return rum;
	}

	public void setRum( String rum )
	{
		this.rum = rum;
	}

}
