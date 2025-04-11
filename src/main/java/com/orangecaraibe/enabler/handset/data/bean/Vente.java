package com.orangecaraibe.enabler.handset.data.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "EP_COMMISSIONNEMENT" )
public class Vente
{
	String imei;

	Integer coId;

	Date dateAchat;

	@Id
	@Column( name = "IMEI_ID" )
	public String getImei()
	{
		return imei;
	}

	public void setImei( String imei )
	{
		this.imei = imei;
	}

	@Column( name = "CO_ID" )
	public Integer getCoId()
	{
		return coId;
	}

	public void setCoId( Integer coId )
	{
		this.coId = coId;
	}

	@Column( name = "COMMERCIAL_ACT_DATE" )
	public Date getDateAchat()
	{
		return dateAchat;
	}

	public void setDateAchat( Date date )
	{
		this.dateAchat = date;
	}

}
