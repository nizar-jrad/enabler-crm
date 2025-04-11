/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;

/**
 * @author ncrtest2
 */
public class Refund
{
	public float amount;

	public Date dateRefund;

	private String famillieId;

	private String natureId;

	private String siteId;

	private String type;

	private String glCode;

	private String costCenter;

	/**
	 * @return the amount
	 */
	public float getAmount()
	{
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount( float amount )
	{
		this.amount = amount;
	}

	/**
	 * @return the dateRefund
	 */
	public Date getDateRefund()
	{
		return dateRefund;
	}

	/**
	 * @param dateRefund the dateRefund to set
	 */
	public void setDateRefund( Date dateRefund )
	{
		this.dateRefund = dateRefund;
	}

	/**
	 * @return the famillieId
	 */
	public String getFamillieId()
	{
		return famillieId;
	}

	/**
	 * @param famillieId the famillieId to set
	 */
	public void setFamillieId( String famillieId )
	{
		this.famillieId = famillieId;
	}

	/**
	 * @return the natureId
	 */
	public String getNatureId()
	{
		return natureId;
	}

	/**
	 * @param natureId the natureId to set
	 */
	public void setNatureId( String natureId )
	{
		this.natureId = natureId;
	}

	/**
	 * @return the siteId
	 */
	public String getSiteId()
	{
		return siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId( String siteId )
	{
		this.siteId = siteId;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType( String type )
	{
		this.type = type;
	}

	/**
	 * @return the glCode
	 */
	public String getGlCode()
	{
		return glCode;
	}

	/**
	 * @param glCode the glCode to set
	 */
	public void setGlCode( String glCode )
	{
		this.glCode = glCode;
	}

	/**
	 * @return the costCenter
	 */
	public String getCostCenter()
	{
		return costCenter;
	}

	/**
	 * @param costCenter the costCenter to set
	 */
	public void setCostCenter( String costCenter )
	{
		this.costCenter = costCenter;
	}
}
