package com.orangecaraibe.enabler.crm.business.bean.enums;

import org.apache.commons.lang3.StringUtils;

public enum EnumInteractionSocialMediaType
{
	FACEBOOK( "FacebooK", 100000000 ),
	TWITTER( "Twitter", 100000001 ),
	CHAT_BOL( "Chat boutique en ligne", 100000002 ),
	CHAT_EC( "Chat espace client", 100000003 ),;

	private String label;

	private int code;

	private EnumInteractionSocialMediaType( String label, int code )
	{
		this.label = label;
		this.code = code;
	}

	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @return the label
	 */
	public int getCode()
	{
		return code;
	}

	/**
	 * @param name
	 * @return
	 */
	public static EnumInteractionSocialMediaType getInteractionTypeByCode( int code )
	{
		for ( EnumInteractionSocialMediaType interactionType : EnumInteractionSocialMediaType.values() )
		{
			if ( interactionType.getCode() == code )
				return interactionType;
		}
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	public static EnumInteractionSocialMediaType getInteractionTypeByName( String name )
	{
		for ( EnumInteractionSocialMediaType interactionType : EnumInteractionSocialMediaType.values() )
		{
			if ( StringUtils.equalsIgnoreCase( interactionType.name(), name ) )
				return interactionType;
		}
		return null;
	}

}
