package com.orangecaraibe.enabler.crm.business.bean.enums;

public enum EnumRequestStatus
{

	ACTIVE( 0, "En cours", "INITIALISED", 1 ), RESOLVED( 1, "Résolue", "CLOSED", 5 ), CANCELLED( 2, "Annulé",
		"CANCELED", 0 );

	private int id;

	private String label;

	private String gdmLabel;

	private int idOceane;

	private EnumRequestStatus( int id, String label, String gdmLabel, int idOceane )
	{
		this.label = label;
		this.id = id;
		this.gdmLabel = gdmLabel;
		this.idOceane = idOceane;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @return the gdmLabel
	 */
	public String getGmdLabel()
	{
		return gdmLabel;
	}

	/**
	 * Revoie le statut correspondant au gdmLabel
	 * 
	 * @param gdmLabel
	 * @return
	 */
	public static EnumRequestStatus getByGdmLabel( String gdmLabel )
	{
		for ( EnumRequestStatus requestStatus : EnumRequestStatus.values() )
		{
			if ( requestStatus.getGmdLabel().equalsIgnoreCase( gdmLabel ) )
				return requestStatus;
		}
		return null;
	}

	public static EnumRequestStatus getById( int id )
	{

		if ( ACTIVE.id == id )
		{
			return ACTIVE;
		}
		else if ( CANCELLED.id == id )
		{
			return CANCELLED;
		}
		else
		{
			return RESOLVED;
		}

	}

	public static EnumRequestStatus getByOceaneID( String oceaneId )
	{
		for ( EnumRequestStatus requestStatus : EnumRequestStatus.values() )
		{
			if ( requestStatus.idOceane == Integer.valueOf( oceaneId ) )
				return requestStatus;
		}
		return null;
	}

	/**
	 * @return the idOceane
	 */
	public int getIdOceane()
	{
		return idOceane;
	}

	/**
	 * @param idOceane the idOceane to set
	 */
	public void setIdOceane( int idOceane )
	{
		this.idOceane = idOceane;
	}
}
