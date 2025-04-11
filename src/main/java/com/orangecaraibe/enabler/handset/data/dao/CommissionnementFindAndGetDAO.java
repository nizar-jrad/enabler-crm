package com.orangecaraibe.enabler.handset.data.dao;

import com.orangecaraibe.enabler.handset.data.bean.Vente;

public interface CommissionnementFindAndGetDAO
{
	/**
	 * Obtenir le code Sim à partir de l'imei
	 */
	Vente getVenteByImei( String imei );

	/**
	 * Obtenir un imei permettant le desimlockage code : identifié date d'achat : plus de trois mois
	 */
	public String getImeiCodeOkDateOk();

}
