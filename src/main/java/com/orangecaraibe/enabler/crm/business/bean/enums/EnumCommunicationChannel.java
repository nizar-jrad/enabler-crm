package com.orangecaraibe.enabler.crm.business.bean.enums;

/**
 * Enum pour al gestion des canal de communication au client
 * 
 * @author NCR
 */
public enum EnumCommunicationChannel
{
	SMS( "SMS" ), EMAIL( "EMAIL" ), COURRIER( "COURRIER" );

	private String communicationCanal;

	/**
	 * @param communicationCanal
	 */
	private EnumCommunicationChannel( String communicationCanal )
	{
		this.communicationCanal = communicationCanal;
	}

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
}
