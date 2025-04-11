/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;

/**
 * Classe des données de recouvrements
 * 
 * @author NCR
 */
public class Recovery
{

	/** Tag de possibilité de facilité de paiement */
	public Boolean isAvalaible;

	/**/
	public Boolean isDunnable;

	public int nbFacilitiesDone;

	public int nbFacilitiesLeftovers;

	public Date suspensionDate;

	public Date resiliationDate;

	public Facilities facilities;

	/**
	 * @return the isAvalaible
	 */
	public Boolean getIsAvalaible()
	{
		return isAvalaible;
	}

	/**
	 * @param isAvalaible the isAvalaible to set
	 */
	public void setIsAvalaible( Boolean isAvalaible )
	{
		this.isAvalaible = isAvalaible;
	}

	/**
	 * @return the isDunnable
	 */
	public Boolean getIsDunnable()
	{
		return isDunnable;
	}

	/**
	 * @param isDunnable the isDunnable to set
	 */
	public void setIsDunnable( Boolean isDunnable )
	{
		this.isDunnable = isDunnable;
	}

	/**
	 * @return the nbFacilitiesDone
	 */
	public int getNbFacilitiesDone()
	{
		return nbFacilitiesDone;
	}

	/**
	 * @param nbFacilitiesDone the nbFacilitiesDone to set
	 */
	public void setNbFacilitiesDone( int nbFacilitiesDone )
	{
		this.nbFacilitiesDone = nbFacilitiesDone;
	}

	/**
	 * @return the nbFacilitiesLeftovers
	 */
	public int getNbFacilitiesLeftovers()
	{
		return nbFacilitiesLeftovers;
	}

	/**
	 * @param nbFacilitiesLeftovers the nbFacilitiesLeftovers to set
	 */
	public void setNbFacilitiesLeftovers( int nbFacilitiesLeftovers )
	{
		this.nbFacilitiesLeftovers = nbFacilitiesLeftovers;
	}

	/**
	 * @return the suspensionDate
	 */
	public Date getSuspensionDate()
	{
		return suspensionDate;
	}

	/**
	 * @param suspensionDate the suspensionDate to set
	 */
	public void setSuspensionDate( Date suspensionDate )
	{
		this.suspensionDate = suspensionDate;
	}

	/**
	 * @return the resiliationDate
	 */
	public Date getResiliationDate()
	{
		return resiliationDate;
	}

	/**
	 * @param resiliationDate the resiliationDate to set
	 */
	public void setResiliationDate( Date resiliationDate )
	{
		this.resiliationDate = resiliationDate;
	}

	/**
	 * @return the facilities
	 */
	public Facilities getFacilities()
	{
		return facilities;
	}

	/**
	 * @param facilities the facilities to set
	 */
	public void setFacilities( Facilities facilities )
	{
		this.facilities = facilities;
	}
}