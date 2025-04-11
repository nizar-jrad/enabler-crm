/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumLegalStatus;

/**
 * @author NCR
 */
public class LegalStatus
{

	EnumLegalStatus legarRedressType;

	Date dateRedress;

	String responsible;

	/**
	 * @return the legarRedressType
	 */
	public EnumLegalStatus getLegarRedressType()
	{
		return legarRedressType;
	}

	/**
	 * @param legarRedressType the legarRedressType to set
	 */
	public void setLegarRedressType( EnumLegalStatus legarRedressType )
	{
		this.legarRedressType = legarRedressType;
	}

	/**
	 * @return the dateRedress
	 */
	public Date getDateRedress()
	{
		return dateRedress;
	}

	/**
	 * @param dateRedress the dateRedress to set
	 */
	public void setDateRedress( Date dateRedress )
	{
		this.dateRedress = dateRedress;
	}

	/**
	 * @return the responsible
	 */
	public String getResponsible()
	{
		return responsible;
	}

	/**
	 * @param responsible the responsible to set
	 */
	public void setResponsible( String responsible )
	{
		this.responsible = responsible;
	}

}
