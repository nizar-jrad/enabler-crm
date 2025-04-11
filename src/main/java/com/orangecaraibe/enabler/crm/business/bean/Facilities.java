/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean;

import java.util.List;

/**
 * Liste des facilités de paiement
 * 
 * @author NCR
 */
public class Facilities
{

	/** Facilité de type delais de paiement */
	public Facility delay;

	/** Liste de echeances pour un paiement en plusieurs fois */
	public List<Facility> schedules;

	public Facility getDelay()
	{
		return delay;
	}

	public void setDelay( Facility delay )
	{
		this.delay = delay;
	}

	public List<Facility> getSchedules()
	{
		return schedules;
	}

	public void setSchedules( List<Facility> schedules )
	{
		this.schedules = schedules;
	}

}
