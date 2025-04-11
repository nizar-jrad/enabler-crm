/**
 *
 */
package com.orangecaraibe.enabler.externalws.utils;

/**
 * Classe de constantes pour les appels WS externes
 * 
 * @author sgodard
 */
public class ExternalWSConstants
{
	// TODO : anciennes adherences EPOINT a revoir ou a supprimer
	public static final String EPT_ORDER = "Commande EPOINT WS";

	public static final String CONTRACT_ID = "Contrat:";

	public static final String CREDIT_INCUDING = "creditIncluding";

	/** Credit communication */
	public static final String ATOMIC_OFFER_CREDIT = "AOOT_CreditCom";

	public static String INSTALLED_PUBLIC_KEY_ID = "installedPublicKeyID";

	public static String OFFER_CREDIT_TRANSFERT = "IN_BUNDLE:TRANSFERT_CREDIT";

	public static String FUNCTION_SPECIFICATION_MONTANT_CREDIT = "IN_BUNDLE_PARAM_ID:MONTANT_TRANSFERT_CREDIT";

	public static String FUNCTION_SPECIFICATION_DAYACTIVE_CREDIT = "IN_BUNDLE_PARAM_ID:DAYSACTIVE_TRANSFERT_CREDIT";

	public static String FUNCTION_SPECIFICATION_DAYINACTIVE_CREDIT = "IN_BUNDLE_PARAM_ID:DAYSINACTIVE_TRANSFERT_CREDIT";

	public static String FUNCTION_SPECIFICATION_SOURCE_CREDIT =
		"IN_BUNDLE_PARAM_ID:CUSTOMER_ID_SOURCE_TRANSFERT_CREDIT";

	public static String FUNCTION_SPECIFICATION_LOYALTY_CREDIT =
		"IN_BUNDLE_PARAM_ID:BURNT_LOYALTY_POINTS_TRANSFERT_CREDIT";

	public static String INSTALLED_OFFER_ID = "installedOfferId";

}
