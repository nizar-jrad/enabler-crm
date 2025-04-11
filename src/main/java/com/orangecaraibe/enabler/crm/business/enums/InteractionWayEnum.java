package com.orangecaraibe.enabler.crm.business.enums;

import org.apache.commons.lang3.StringUtils;

import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionWay;

public enum InteractionWayEnum
{
	IN( "in", InteractionWay.IN, "Entrant" ), OUT( "out", InteractionWay.OUT, "Sortant" );

	private String way;

	private InteractionWay gdmInteractionWay;

	private String dcrmDirection;

	private InteractionWayEnum( String way, InteractionWay gdmInteractionWay, String dcrmDirection )
	{
		this.way = way;
		this.dcrmDirection = dcrmDirection;
	}

	/**
	 * Renvoie l'Enum correspondant au sens passe
	 * 
	 * @param way
	 * @return
	 */
	public static InteractionWayEnum getInteractionWayEnumByWay( String way )
	{
		for ( InteractionWayEnum interactionWayEnum : InteractionWayEnum.values() )
		{
			if ( StringUtils.isNotEmpty( way ) && interactionWayEnum.way.equalsIgnoreCase( way ) )
			{
				return interactionWayEnum;
			}
		}
		return null;

	}

	/**
	 * Renvoie l'Enum correspondant a la valeur GDM
	 * 
	 * @param gdmInteractionWay
	 * @return
	 */
	public static InteractionWayEnum getInteractionWayEnumByGdmWay( InteractionWay gdmInteractionWay )
	{
		for ( InteractionWayEnum interactionWayEnum : InteractionWayEnum.values() )
		{
			if ( interactionWayEnum.gdmInteractionWay == gdmInteractionWay )
			{
				return interactionWayEnum;
			}
		}
		return null;

	}

	public String getWay()
	{
		return way;
	}

	public InteractionWay getGdmInteractionWay()
	{
		return gdmInteractionWay;
	}

	public String getDcrmDirection()
	{
		return dcrmDirection;
	}

}
