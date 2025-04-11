/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean.enums;

/**
 * @author ncrtest2
 */
public enum TroubleTicketUrgenceEnum
{

	IMMEDIATE_INTERVENTION( "Intervention immediate", 1 ),
	DEFERRED_INTERVENTION( "Intervention différée", 2 ),
	EMERGENCY( "Etat de crise", 3 ),
	NO_INTERVENTION( "Sans intervention", 4 ),
	ONE( "1", 6 ),
	TWO( "2", 7 ),
	THREE( "3", 8 ),
	FOUR( "4", 9 ),
	CLASS_1( "classe 1", 10 ),
	CLASS_2( "classe 2", 11 ),
	CLASS_3( "classe 3", 12 )

	;

	private TroubleTicketUrgenceEnum( String label, int code )
	{
		this.label = label;
		this.code = code;
	}

	String label;

	int code;
}
