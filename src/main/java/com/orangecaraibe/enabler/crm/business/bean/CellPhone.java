package com.orangecaraibe.enabler.crm.business.bean;

/**
 * Classe de données sur le mobile
 * 
 * @author NCR
 */
public class CellPhone
{

	/** Numero IMEI du mobile */
	public String imei;

	/** Tag pour le mobile en cours */
	public Boolean IsCurrent;

	/** Canal d'achat du mobile */
	public String canal;

	// /**Moyen d'acaht du mobile*/
	// public EnumPurchaseType PurchaseType;

	/*** Prix du mobile */
	public Float amount;

	// /**Devise du prix*/
	// public EnumUnit unit;

	/** Age du mobile */
	public int age;

	/** Model du mobile */
	public String model;

	/** Marque du mobile */
	public String brand;

	/** Tag pour un IPhone */
	public Boolean isIPhone;

	/** Code de desimlockage */
	public String simLockCode;

	/** Image du mobile */
	public String url;

	/** Tag pour un mobile orange */
	public Boolean isOrange;

	/** Code du model du mobile */
	public String code;

	public String getImei()
	{
		return imei;
	}

	public void setImei( String imei )
	{
		this.imei = imei;
	}

	public Boolean getIsCurrent()
	{
		return IsCurrent;
	}

	public void setIsCurrent( Boolean isCurrent )
	{
		IsCurrent = isCurrent;
	}

	public String getCanal()
	{
		return canal;
	}

	public void setCanal( String canal )
	{
		this.canal = canal;
	}

	public Float getAmount()
	{
		return amount;
	}

	public void setAmount( Float amount )
	{
		this.amount = amount;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge( int age )
	{
		this.age = age;
	}

	public String getModel()
	{
		return model;
	}

	public void setModel( String model )
	{
		this.model = model;
	}

	public String getBrand()
	{
		return brand;
	}

	public void setBrand( String brand )
	{
		this.brand = brand;
	}

	public Boolean getIsIPhone()
	{
		return isIPhone;
	}

	public void setIsIPhone( Boolean isIPhone )
	{
		this.isIPhone = isIPhone;
	}

	public String getSimLockCode()
	{
		return simLockCode;
	}

	public void setSimLockCode( String simLockCode )
	{
		this.simLockCode = simLockCode;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public Boolean getIsOrange()
	{
		return isOrange;
	}

	public void setIsOrange( Boolean isOrange )
	{
		this.isOrange = isOrange;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode( String code )
	{
		this.code = code;
	}

}
