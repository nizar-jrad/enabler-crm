/**
 *
 */
package com.orangecaraibe.enabler.ussd.core.util;

/**
 * @author sgodard Constantes relatives à l'USSD
 */
public class ConstantesUSSD
{

	public static final String KEY_PARTICULIER_ENGAGE = "ussd.message.particulier.engage";

	public static final String KEY_PARTICULIER_NONENGAGE = "ussd.message.particulier.nonengage";

	public static final String KEY_PRO_ENGAGE_GESTIONNAIRE = "ussd.message.pro.engage.gestionnaire";

	public static final String KEY_PRO_ENGAGE_NONGESTIONNAIRE = "ussd.message.pro.engage.nongestionnaire";

	public static final String KEY_PRO_NONENGAGE_GESTIONNAIRE = "ussd.message.pro.nonengage.gestionnaire";

	public static final String KEY_PRO_NONENGAGE_NONGESTIONNAIRE = "ussd.message.pro.nonengage.nongestionnaire";

	public static final String KEY_PRO_GESTIONNAIRE_ADDITIONAL = "ussd.message.pro.gestionnaire.additional";

	public static final String KEY_PARTICULIER_NOM_MAXLENGTH = "ussd.message.particulier.nom.maxlength";

	public static final String KEY_PARTICULIER_PRENOM_MAXLENGTH = "ussd.message.particulier.prenom.maxlength";

	public static final String KEY_PRO_DENOMINATION_MAXLENGTH = "ussd.message.pro.denomination.maxlenght";

	public static final String KEYWORD_DENOMINATION = "\\$\\[denomination\\]";

	public static final String KEYWORD_NOM = "\\$\\[nom\\]";

	public static final String KEYWORD_PRENOM = "\\$\\[prenom\\]";

	public static final String KEYWORD_NUMERO = "\\$\\[numero\\]";

	public static final String KEYWORD_ADDITIONAL_NUMBER = "\\$\\[additionalNumero\\]";

	public static final String KEYWORD_RIO = "\\$\\[rio\\]";

	public static final String KEYWORD_DEBUT = "\\$\\[debut\\]";

	public static final String KEYWORD_FIN = "\\$\\[fin\\]";

	public static final String ERROR_CODE_TECHNICAL_ERROR = "Le serveur a rencontre une erreur technique";

	public static final String ERROR_CODE_UNKNOWN_CLIENT = "Aucun compte client n'a ete trouve pour votre numero";

	public static final String ERROR_CODE_UNKNOWN_MSISDN = "Aucun contrat n'a ete trouve pour votre numero";
}
