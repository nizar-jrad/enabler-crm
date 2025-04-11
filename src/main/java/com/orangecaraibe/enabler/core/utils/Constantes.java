/**
 *
 */
package com.orangecaraibe.enabler.core.utils;

/**
 * @author sgodard Constantes propres à l'application
 */
public class Constantes
{
	/** Constantes définissant le répertoire contenant les fichiers de properies des messages */
	public static final String CONFIG_FOLDER = "ENABLER_CRM_V1_CONFIG_FOLDER";

	/** Definit le nom du fichier de properties pour la recherche des messages formatés pour l'ussd et svi */
	public static final String USSD_PROPERTIES_FILE = "messages_ussd.properties";

	public static final String SVI_PROPERTIES_FILE = "messages_svi.properties";

	public static final String JJ_MM_AAAA = "dd/MM/yyyy";

	public static final String DATE_FORMAT_DDMMYYYY = "ddMMyyyy";

	public static final String DATE_FORMAT_YYYYMMDDHHMM = "yyyyMMddhhmm";

	public static final String REGEX_INTERNATIONAL_FORMAT = "[1-9]{1,2}[0-9][1-68][0-9]{8}";

	public static final String TO_BE_DEFINED = "TO_BE_DEFINED:";

	// TODO : toutes les constantes EPOINT a renommer ou supprimer
	public static final String EPOINT_NO_ERROR_RETURN_CODE = "1|";

	public static final String EPOINT_ERROR_RETURN_CODE = "-1|";

	public static final String EPOINT_ERROR_RETURN_CODE_2 = "-2|";

	public static final String EPOINT_ERROR_RETURN_CODE_0 = "0|";

	public static final String EPOINT_ERROR_TECHNICAL = "technical error";

	public static final String EPOINT_ERROR_INTERFACE = "interface unavailable";

	public static final String EPOINT_ERROR_MSISDN = "invalid msisdn";

	public static final String EPOINT_ERROR_BILLING_ACCOUNT = "invalid billing account";

	public static final String EPOINT_ERROR_COID = "invalid co id";

	public static final String EPOINT_ERROR_CUSTOMERID = "invalid customer id";

	public static final String EPOINT_ERROR_CLIENT_TYPE = "invalid client type";

	public static final String EPOINT_ERROR_TMCODE = "invalid tmcode";

	public static final String EPOINT_ERROR_SNCODE = "invalid sncode";

	public static final String EPOINT_ERROR_SPCODE = "invalid spcode";

	public static final String EPOINT_ERROR_NO_OFFER = "no offer found";

	public static final String EPOINT_ERROR_CUSTID = "invalid customer id";

	public static final String EPOINT_ERROR_SEPARATOR = ":";

	public static final String EPOINT_RETURN_CODE_OK = "OK";

	public static final String EPOINT_ERROR_EXCEPTION = "EXCEPTION";

	public static final String EPOINT_ERROR_TIMEOUT = "TIMEOUT";

	public static final String EPOINT_ERROR_OFFERCODE = "invalid castor code";

	public static final String EPOINT_ERROR_AMOUNT = "invalid amount";

	public static final String EPOINT_PRESENT_RETURN_CODE = "1";

	public static final String EPOINT_NOT_PRESENT_RETURN_CODE = "0";

	public static final String EPOINT_ERROR_RIO = "Error GetOutGoing RIO";

	public static final String REGEX_GUID_THEME = ":";

	public static final String ODL_PARTY_ID_REGEX = ":";

	/*
	 * OREKA
	 */
	public static final String DCRM_LOGICAL_NAME_SYSTEM_USER = "systemuser";

	public static final String DCRM_LOGICAL_NAME_ACCOUNT = "account";

	public static final String DCRM_LOGICAL_NAME_ADRESSE = "address";

	public static final String WHITE_SPACE = " ";
}
