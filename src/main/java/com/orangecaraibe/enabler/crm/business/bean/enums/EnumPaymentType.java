package com.orangecaraibe.enabler.crm.business.bean.enums;

/**
 * Enum décrivant les différents types de paiement diponibles
 * 
 * @author NCR
 */
public enum EnumPaymentType
{
	/** ESPECES */
	ESPECES( -1L, "ESPECES" ),

	/** VIREMENT BANCAIRE */
	VIREMENT_BANCAIRE( -2L, "VIREMENT BANCAIRE" ),

	/** PRELEVEMENT AUTOMATIQUE */
	PRELEVEMENT_AUTOMATIQUE( -3L, "PRELEVEMENT AUTOMATIQUE" ),

	/** CARTE PREPAYEE */
	CARTE_PREPAYEE( -4L, "CARTE PREPAYEE" ),

	/** CHEQUE */
	CHEQUE( -5L, "CHEQUE" ),

	/** CARTE BANCAIRE */
	CARTE_BANCAIRE( -6L, "CARTE BANCAIRE" ),

	/** AUTRE MODE DE PAIEMENT */
	AUTRE( -7L, "AUTRE MODE DE PAIEMENT" );

	/** The description. */
	private String description;

	/** Code dans CMS. */
	private long code;

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Instantiates a new payment type enum.
	 * 
	 * @param description the description
	 * @param code the code
	 */
	private EnumPaymentType( long code, String description )
	{
		this.code = code;
		this.description = description;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public long getCode()
	{
		return code;
	}

	/**
	 * Renvoie l'Enum correspondant au code (dans la couche CMS) passé en paramètre
	 * 
	 * @param code Code du paymentType dans la couche CMS
	 * @return Enum correspondant au code (dans la couche CMS) passé en paramètre
	 */
	public static EnumPaymentType getPaymentTypeByCode( long code )
	{
		for ( EnumPaymentType paymentTypeEnum : EnumPaymentType.values() )
		{
			if ( paymentTypeEnum.getCode() == code )
			{
				return paymentTypeEnum;
			}
		}
		return null;

	}

}
