package com.orangecaraibe.enabler.crm.business.bean.enums;

public enum TroubleTicketSeverityEnum
{

	MINEUR( "Mineur", 1 ), GRAVE( "Grave", 2 ), MAJEUR( "Majeur", 3 ), CRISE_JAUNE( "Crise jaune", 4 );

	private TroubleTicketSeverityEnum( String label, int code )
	{
		this.label = label;
		this.code = code;
	}

	String label;

	int code;
}
