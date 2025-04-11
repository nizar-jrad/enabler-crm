package com.orangecaraibe.enabler.crm.business.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumTypeCampagne;

public class Campagne
{

	private String name;

	private String description;

	private String offerCode;

	private Date startDate;

	private Date endDate;

	private Date campangeDate;

	private EnumTypeCampagne type;

	private List<Interaction> interactions;

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription( String description )
	{
		this.description = description;
	}

	/**
	 * @return the offerCode
	 */
	public String getOfferCode()
	{
		return offerCode;
	}

	/**
	 * @param offerCode the offerCode to set
	 */
	public void setOfferCode( String offerCode )
	{
		this.offerCode = offerCode;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate( Date startDate )
	{
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate( Date endDate )
	{
		this.endDate = endDate;
	}

	/**
	 * @return the campangeDate
	 */
	public Date getCampangeDate()
	{
		return campangeDate;
	}

	/**
	 * @param campangeDate the campangeDate to set
	 */
	public void setCampangeDate( Date campangeDate )
	{
		this.campangeDate = campangeDate;
	}

	/**
	 * @return the type
	 */
	public EnumTypeCampagne getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType( EnumTypeCampagne type )
	{
		this.type = type;
	}

	/**
	 * @return the interactions
	 */
	public List<Interaction> getInteractions()
	{
		return interactions;
	}

	/**
	 * @param interactions the interactions to set
	 */
	public void setInteractions( List<Interaction> interactions )
	{
		this.interactions = interactions;
	}

	public void addinteraction( Interaction interaction )
	{
		if ( interactions == null )
			interactions = new ArrayList<Interaction>();

		interactions.add( interaction );
	}

}
