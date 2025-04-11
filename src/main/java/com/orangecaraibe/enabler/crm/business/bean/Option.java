package com.orangecaraibe.enabler.crm.business.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe des données option
 * 
 * @author NCR
 */
public class Option
{

	private String offerId;

	/** Nom de l'option */
	private String label;

	/** Famille de l'option */
	private String family;

	/** Cout de l'option */
	private Double amount;

	/** Date de prise de l'option */
	private Date startDate;

	/** Date de fin de l'option */
	private Date endDate;

	/** Statut de l'option */
	// private EnumOptionStatus status;

	/** Tag Option inclue */
	private Boolean isIncluded;

	/** Liste des parametre de l'option */
	private List<Parameter> parameters;

	public String getLabel()
	{
		return label;
	}

	public void setLabel( String label )
	{
		this.label = label;
	}

	public String getFamily()
	{
		return family;
	}

	public void setFamily( String family )
	{
		this.family = family;
	}

	public Double getAmount()
	{
		return amount;
	}

	public void setAmount( Double amount )
	{
		this.amount = amount;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate( Date startDate )
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate( Date endDate )
	{
		this.endDate = endDate;
	}

	public Boolean getIsIncluded()
	{
		return isIncluded;
	}

	public void setIsIncluded( Boolean isIncluded )
	{
		this.isIncluded = isIncluded;
	}

	public String getOfferId()
	{
		return offerId;
	}

	public void setOfferId( String offerId )
	{
		this.offerId = offerId;
	}

	/**
	 * @return the parameters
	 */
	public List<Parameter> getParameters()
	{
		if ( parameters == null )
		{
			parameters = new ArrayList<Parameter>();
		}
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters( List<Parameter> parameters )
	{
		this.parameters = parameters;
	}

}
