package com.orangecaraibe.enabler.crm.business.bean;

/**
 * Classe des données des methodes de contact
 * 
 * @author NCR
 */
public class ContactMethods
{

	/** Telephone mobile */
	public String mobilePhone;

	/** Tag de contact par tel mobile */
	public Boolean IsmobilePhoneOptIn;

	/** telephone fixe */
	public String fixePhone;

	/** telephone fixe 2 */
	public String fixePhone2;

	/** FAx */
	public String faxPhone;

	/** adresse mail */
	public String email;

	/** Tag de contact par mail */
	public Boolean IsemailOptIn;

	/** Tag de validation de l'adrese mail */
	public Boolean IsEmailNpai;

	/** Contact twitter */
	public String twitter;

	/** Contract facebook */
	public String facebook;

	public Address adress;

	/**
	 * @return the fixePhone2
	 */
	public String getFixePhone2()
	{
		return fixePhone2;
	}

	/**
	 * @param fixePhone2 the fixePhone2 to set
	 */
	public void setFixePhone2( String fixePhone2 )
	{
		this.fixePhone2 = fixePhone2;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public void setMobilePhone( String mobilePhone )
	{
		this.mobilePhone = mobilePhone;
	}

	public Boolean getIsmobilePhoneOptIn()
	{
		return IsmobilePhoneOptIn;
	}

	public void setIsmobilePhoneOptIn( Boolean ismobilePhoneOptIn )
	{
		IsmobilePhoneOptIn = ismobilePhoneOptIn;
	}

	public String getFixePhone()
	{
		return fixePhone;
	}

	public void setFixePhone( String fixePhone )
	{
		this.fixePhone = fixePhone;
	}

	public String getFaxPhone()
	{
		return faxPhone;
	}

	public void setFaxPhone( String faxPhone )
	{
		this.faxPhone = faxPhone;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail( String email )
	{
		this.email = email;
	}

	public Boolean getIsemailOptIn()
	{
		return IsemailOptIn;
	}

	public void setIsemailOptIn( Boolean isemailOptIn )
	{
		IsemailOptIn = isemailOptIn;
	}

	public Boolean getIsEmailNpai()
	{
		return IsEmailNpai;
	}

	public void setIsEmailNpai( Boolean isEmailNpai )
	{
		IsEmailNpai = isEmailNpai;
	}

	public String getTwitter()
	{
		return twitter;
	}

	public void setTwitter( String twitter )
	{
		this.twitter = twitter;
	}

	public String getFacebook()
	{
		return facebook;
	}

	public void setFacebook( String facebook )
	{
		this.facebook = facebook;
	}

	/**
	 * @return the adress
	 */
	public Address getAdress()
	{
		return adress;
	}

	/**
	 * @param adress the adress to set
	 */
	public void setAdress( Address adress )
	{
		this.adress = adress;
	}

}
