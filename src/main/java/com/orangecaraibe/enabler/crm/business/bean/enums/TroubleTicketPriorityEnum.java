/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean.enums;

/**
 * @author ncrtest2
 */
public enum TroubleTicketPriorityEnum
{

	P0( "P0", 0 ), P1( "P1", 1 ), P2( "P2", 2 ), P3( "P3", 3 ), P4( "P4", 4 ), P6( "P6", 6 );

	private TroubleTicketPriorityEnum( String label, int code )
	{
		this.label = label;
		Code = code;
	}

	String label;

	int Code;
}
