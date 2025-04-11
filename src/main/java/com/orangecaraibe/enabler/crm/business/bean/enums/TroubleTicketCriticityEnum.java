package com.orangecaraibe.enabler.crm.business.bean.enums;

public enum TroubleTicketCriticityEnum
{

	MINEUR( "Mineur", 1 );

	String label;

	int code;

	private TroubleTicketCriticityEnum( String label, int code )
	{
		this.label = label;
		this.code = code;
	}
}
