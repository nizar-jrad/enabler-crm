package com.orangecaraibe.enabler.crm.business.enums;

import org.apache.commons.lang3.StringUtils;

public enum InteractionTypeEnum
{
	MARKETING_CAMPAIGN( "marketingCampaign" );

	private String type;

	private InteractionTypeEnum( String type )
	{
		this.type = type;
	}

	/**
	 * Renvoie l'Enum correspondant au type passe
	 * 
	 * @param type
	 * @return
	 */
	public static InteractionTypeEnum getInteractionTypeEnumByType( String type )
	{
		for ( InteractionTypeEnum interactionTypeEnum : InteractionTypeEnum.values() )
		{
			if ( StringUtils.isNotEmpty( type ) && interactionTypeEnum.type.equalsIgnoreCase( type ) )
			{
				return interactionTypeEnum;
			}
		}
		return null;

	}

}
