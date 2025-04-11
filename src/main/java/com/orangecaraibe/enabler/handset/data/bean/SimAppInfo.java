package com.orangecaraibe.enabler.handset.data.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "SIMS2", schema = "SIMAPP" )
public class SimAppInfo
{
	Long id;

	String imei;

	String code;

	Long mobile;

	Date dateCreation;

	@Id
	public Long getId()
	{
		return id;
	}

	public void setId( Long id )
	{
		this.id = id;
	}

	@Column( name = "IMEI" )
	public String getImei()
	{
		return imei;
	}

	public void setImei( String imei )
	{
		this.imei = imei;
	}

	@Column( name = "CODE" )
	public String getCode()
	{
		return code;
	}

	public void setCode( String code )
	{
		this.code = code;
	}

	public Long getMobile()
	{
		return mobile;
	}

	public void setMobile( Long mobile )
	{
		this.mobile = mobile;
	}

	@Column( name = "CREATION" )
	public Date getDateCreation()
	{
		return dateCreation;
	}

	public void setDateCreation( Date dateCreation )
	{
		this.dateCreation = dateCreation;
	}

}
