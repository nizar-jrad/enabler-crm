package com.orangecaraibe.enabler.crm.business.bean.enums;

/**
 * Statut du contract
 * 
 * @author NCR
 */
public enum EnumContractStatus
{
	INITIALISED( "INITIALISED", "En cours" ),
	INORDER( "INORDER", "Actif" ),
	SUSPENDED( "SUSPENDED", "Suspendu" ),
	REMOVED( "REMOVED", "Désactivé" );

	private String gdmValue;

	private String oceaneValue;

	EnumContractStatus( String gdmValue, String oceaneValue )
	{
		this.gdmValue = gdmValue;
		this.oceaneValue = oceaneValue;
	}

	public static EnumContractStatus getEnumContractStatus( String gdmCode )
	{
		for ( EnumContractStatus contractStatus : EnumContractStatus.values() )
		{
			if ( contractStatus.gdmValue.equals( gdmCode ) )
				return contractStatus;
		}
		return null;
	}

	/**
	 * @return the oceaneValue
	 */
	public String getOceaneValue()
	{
		return oceaneValue;
	}
}
