package com.orangecaraibe.enabler.crm.business.bean.enums;

public enum EnumLocalActionType
{
	MEFIN( 1 );

	int localActionValue;

	/**
	 * @param localActionValue
	 */
	private EnumLocalActionType( int localActionValue )
	{
		this.localActionValue = localActionValue;
	}

	/**
	 * @return the localActionValue
	 */
	public int getLocalActionValue()
	{
		return localActionValue;
	}

	/**
	 * @param localActionValue the localActionValue to set
	 */
	public void setLocalActionValue( int localActionValue )
	{
		this.localActionValue = localActionValue;
	}

	public static EnumLocalActionType getEnumType( String type )
	{
		for ( EnumLocalActionType enumLocalActionType : EnumLocalActionType.values() )
		{
			if ( enumLocalActionType.name().equalsIgnoreCase( type ) )
			{
				return enumLocalActionType;
			}
		}
		return null;
	}

	public static EnumLocalActionType getEnumLocalActionType( Integer localActionValue )
	{
		for ( EnumLocalActionType enumLocalActionType : EnumLocalActionType.values() )
		{
			if ( enumLocalActionType.getLocalActionValue() == localActionValue )
			{
				return enumLocalActionType;
			}
		}
		return null;
	}
}
