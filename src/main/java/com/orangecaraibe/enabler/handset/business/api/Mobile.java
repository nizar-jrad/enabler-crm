package com.orangecaraibe.enabler.handset.business.api;

import java.io.Serializable;
import java.util.Date;

public class Mobile
	implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getCodeSim()
	{
		return codeSim;
	}

	public void setCodeSim( String codeSim )
	{
		this.codeSim = codeSim;
	}

	public String getReponseInfoDesimlockage()
	{
		return reponseInfoDesimlockage;
	}

	public void setReponseInfoDesimlockage( String reponseInfoDesimlockage )
	{
		this.reponseInfoDesimlockage = reponseInfoDesimlockage;
	}

	public Date getDateAchat()
	{
		return dateAchat;
	}

	public void setDateAchat( Date dateAchat )
	{
		this.dateAchat = dateAchat;
	}

	public boolean isIsIphone()
	{
		return isIphone;
	}

	public void setIsIphone( boolean isIphone )
	{
		this.isIphone = isIphone;
	}

	public String getImei()
	{
		return imei;
	}

	public void setImei( String imei )
	{
		this.imei = imei;
	}

	private String codeSim;

	private String reponseInfoDesimlockage;

	private Date dateAchat;

	private boolean isIphone;

	private String imei;

}
