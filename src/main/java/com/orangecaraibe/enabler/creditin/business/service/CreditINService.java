/**
 *
 */
package com.orangecaraibe.enabler.creditin.business.service;

import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.CreateUpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException;

/**
 * @author gjospitre Interface pour le service métier de Manage credit IN
 */
public interface CreditINService
{

	/**
	 * Méthode de récupération du crédit de communication pour un contrat
	 * 
	 * @param msisdn
	 * @return
	 * @throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException
	 * @throws FindAndGetException
	 */
	public Double getCreditIN( String msisdn )
		throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		FindAndGetException;

	/**
	 * Méthode d'attribution de crédit de communication
	 * 
	 * @param msisdn
	 * @param amount
	 * @param daysActive
	 * @param daysInactive
	 * @throws CreateUpdateException
	 * @throws InterfaceUnavailableException
	 */
	public void setCreditIN( String msisdn, Double amount, int daysActive, int daysInactive )
		throws InterfaceUnavailableException, CreateUpdateException;

}
