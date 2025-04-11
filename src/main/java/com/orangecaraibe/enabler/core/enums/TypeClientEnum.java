/**
 *
 */
package com.orangecaraibe.enabler.core.enums;

/**
 * @author sgodard
 */
public enum TypeClientEnum
{
	PARTICULIER( "P", "C" ), ENTREPRISE( "E", "B" );

	private String ussdValue;

	private String bscsValue;

	/**
	 * Constructeur de l'enum TypeClientEnum
	 * 
	 * @param ussdValue
	 * @param bscsValue
	 */
	private TypeClientEnum( String ussdValue, String bscsValue )
	{
		this.ussdValue = ussdValue;
		this.bscsValue = bscsValue;

	}

	/**
	 * @return the ussdValue
	 */
	public String getUssdValue()
	{
		return ussdValue;
	}

	/**
	 * @return the bscsValue
	 */
	public String getBscsValue()
	{
		return bscsValue;
	}

}
