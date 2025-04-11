package com.orangecaraibe.enabler.handset.data.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "EP_SIM2" )
public class SimInfo
{

	String imei;

	String code;

	@Id
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
}
