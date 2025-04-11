package com.orangecaraibe.enabler.crm.business.bean.enums;

/**
 * Types de civilité
 * 
 * @author ncrtest2
 */
public enum EnumTitle
{
	MR( 100000000 ), MME( 100000001 ), MLLE( 100000002 );

	int dcrmCode;

	private EnumTitle( int dcrmCode )
	{
		this.dcrmCode = dcrmCode;
	}

	/**
	 * Renvoie un EnumTitle en fonction d'un tritre
	 * 
	 * @param title
	 * @return EnumTitle
	 */
	public static EnumTitle getEnumTitle( String title )
	{
		for ( EnumTitle enumTitle : EnumTitle.values() )
		{
			if ( enumTitle.name().equalsIgnoreCase( title ) )
			{
				return enumTitle;
			}
		}
		return null;
	}

	public int getDcrmCode()
	{
		return dcrmCode;
	}

	public static EnumTitle getValueFromDCRM( Integer value )
	{
		for ( EnumTitle enumTitle : EnumTitle.values() )
		{
			if ( enumTitle.getDcrmCode() == value )
			{
				return enumTitle;
			}
		}
		return null;
	}
}
