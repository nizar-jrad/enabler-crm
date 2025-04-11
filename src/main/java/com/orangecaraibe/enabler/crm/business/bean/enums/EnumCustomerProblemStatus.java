/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean.enums;

/**
 * @author ncrtest2
 */
public enum EnumCustomerProblemStatus
{
	IN_PROGRESS( "Initialised", EnumRequestStatus.ACTIVE ),
	OPENED( "Opened", EnumRequestStatus.ACTIVE ),
	COMPLETED( "Closed", EnumRequestStatus.RESOLVED ),
	CANCELLED( "Canceled", EnumRequestStatus.CANCELLED );

	private final String gdmLabel;

	private final EnumRequestStatus enumRequestStatus;

	private EnumCustomerProblemStatus( String gdmLabel, EnumRequestStatus enumRequestStatus )
	{
		this.gdmLabel = gdmLabel;
		this.enumRequestStatus = enumRequestStatus;
	}

	public static EnumCustomerProblemStatus valueOfGDM( String status )
	{
		for ( EnumCustomerProblemStatus customerProblemStatus : EnumCustomerProblemStatus.values() )
		{
			if ( customerProblemStatus.getGdmLabel().equalsIgnoreCase( status ) )
				return customerProblemStatus;
		}
		return null;
	}

	public String getGdmLabel()
	{
		return gdmLabel;
	}

	public EnumRequestStatus getEnumRequestStatus()
	{
		return enumRequestStatus;
	}

}
