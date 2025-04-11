package com.orangecaraibe.enabler.crm.business.bean.enums;

public enum EnumRetentionStatus
{
	VALIDE( 100000001 );

	int dcrmCode;

	private EnumRetentionStatus( int dcrmCode )
	{
		this.dcrmCode = dcrmCode;
	}

	/**
	 * Renvoie un EnumRetentionStatus en fonction d'un status
	 * 
	 * @param title
	 * @return EnumTitle
	 */
	public static EnumRetentionStatus getEnumStatus( String status )
	{
		for ( EnumRetentionStatus enumStatus : EnumRetentionStatus.values() )
		{
			if ( enumStatus.name().equalsIgnoreCase( status ) )
			{
				return enumStatus;
			}
		}
		return null;
	}

	public int getDcrmCode()
	{
		return dcrmCode;
	}

	public static EnumRetentionStatus getValueFromDCRM( Integer value )
	{
		for ( EnumRetentionStatus enumStatus : EnumRetentionStatus.values() )
		{
			if ( enumStatus.getDcrmCode() == value )
			{
				return enumStatus;
			}
		}
		return null;
	}
}
