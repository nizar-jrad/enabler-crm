package com.orangecaraibe.enabler.crm.business.enums;

import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalLetter;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalSelfcare;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalShop;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalSocialMedia;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Media;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Sms;

public enum MediaEnum
{
	Phone( "Phone", com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Phone.class ),
	SMS( "Sms", Sms.class ),
	Email( "Email", com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Email.class ),
	Shop( "LocalShop", LocalShop.class ),
	Selfcare( "LocalSelfcare", LocalSelfcare.class ),
	Letter( "LocalLetter", LocalLetter.class ),
	SocialNetwork( "LocalSocialMedia", LocalSocialMedia.class ),
	Fax( "Fax", com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Fax.class );

	private String mediaName;

	private Class<? extends Media> mediaClass;

	/**
	 * Constructeur avec arguments
	 * 
	 * @param mediaName
	 * @param mediaClass
	 */
	private MediaEnum( String mediaName, Class<? extends Media> mediaClass )
	{
		this.mediaName = mediaName;
		this.mediaClass = mediaClass;

	}

	/**
	 * Renvoie l'enum correspondant au nom de la classe passe
	 * 
	 * @param mediaName
	 * @return MediaEnum
	 */
	public static MediaEnum getMediaEnum( String mediaName )
	{
		if ( mediaName != null )
		{
			for ( MediaEnum mediaEnum : MediaEnum.values() )
			{
				if ( mediaEnum.mediaName.equals( mediaName ) )
				{
					return mediaEnum;
				}
			}
		}
		return null;
	}

}
