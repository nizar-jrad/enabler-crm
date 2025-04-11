package com.orangecaraibe.enabler.crm.business.enums;

public enum CommunicationCanalEnum
{

	SMS( "SMS", 100000000 ), EMAIL( "EMAIL", 100000001 ), COURRIER( "COURRIER", 100000002 );

	private CommunicationCanalEnum( String communicationCanal, int communicationCanalValue )
	{
		this.communicationCanal = communicationCanal;
		this.communicationCanalValue = communicationCanalValue;
	}

	private String communicationCanal;

	private int communicationCanalValue;

	/**
	 * @return the communicationCanal
	 */
	public String getCommunicationCanal()
	{
		return communicationCanal;
	}

	/**
	 * @param communicationCanal the communicationCanal to set
	 */
	public void setCommunicationCanal( String communicationCanal )
	{
		this.communicationCanal = communicationCanal;
	}

	/**
	 * @return the communicationCanlaValue
	 */
	public int getCommunicationCanalValue()
	{
		return communicationCanalValue;
	}

	/**
	 * @param communicationCanalValue the communicationCanalValue to set
	 */
	public void setCommunicationCanalValue( int communicationCanalValue )
	{
		this.communicationCanalValue = communicationCanalValue;
	}
}
