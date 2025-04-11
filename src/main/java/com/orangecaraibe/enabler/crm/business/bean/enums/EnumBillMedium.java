package com.orangecaraibe.enabler.crm.business.bean.enums;

public enum EnumBillMedium
{

	PAPER( "Papier" ),
	ELECTRONIC( "E-Facture" ),
	PAPER_AND_ELECTRONIC( "Papier et E_Facture" ),
	NONE( "Pas de facture" );

	private final String value;

	EnumBillMedium( String value )
	{
		this.value = value;
	}

	public static EnumBillMedium getEnumBillMedium( String medium )
	{
		for ( EnumBillMedium billMedium : EnumBillMedium.values() )
		{
			if ( billMedium.name().equalsIgnoreCase( medium ) )
				return billMedium;
		}
		return null;
	}

	public String getValue()
	{
		return value;
	}
}
