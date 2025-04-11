/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;

/**
 * Classe des données pour une facilité de paiement
 * 
 * @author NCR
 */
public class Facility
{

	/** Date du paiement */
	public Date date;

	/** Montant du paiement */
	public Float amount;

	/** delai accorde */
	public int delay;

	/** Date du delais de recouvrement */
	public Date dateDelais;

	public Date getDate()
	{
		return date;
	}

	public void setDate( Date date )
	{
		this.date = date;
	}

	public Float getAmount()
	{
		return amount;
	}

	public void setAmount( Float amount )
	{
		this.amount = amount;
	}

	/**
	 * @return the delay
	 */
	public int getDelay()
	{
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay( int delay )
	{
		this.delay = delay;
	}

	/**
	 * @return the dateDelais
	 */
	public Date getDateDelais()
	{
		return dateDelais;
	}

	/**
	 * @param dateDelais the dateDelais to set
	 */
	public void setDateDelais( Date dateDelais )
	{
		this.dateDelais = dateDelais;
	}

}