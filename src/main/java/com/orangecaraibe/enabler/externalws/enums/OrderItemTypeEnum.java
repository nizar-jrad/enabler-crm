package com.orangecaraibe.enabler.externalws.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumaration des types d'orderItem
 * 
 * @author jmepor
 */
public enum OrderItemTypeEnum
{

	CONTRAT( "COPL_" ),
	PLAY_HANDSET( "PLHA_" ),
	PLAY_CONTRAT( "PLPL_" ),
	PLAN( "AOPL_" ),
	SERVICE( "AOOP_", "AOOI_" ),
	HANDSET( "AOHA_" ),
	EQUIPEMENT_SIM( "AOSI_" ),
	VOUCHER( "AOVO_" ),
	ACCESSORIES( "AOAC_" ),
	CORESERVICES( "AOCS_" ),
	PREPAIEMENT( "AOPP_" ),
	MODELIVRAISON( "AOML_" );

	public String[] castorPrefixes;

	private final static Map<String, OrderItemTypeEnum> map = new HashMap<String, OrderItemTypeEnum>();

	static
	{
		for ( OrderItemTypeEnum atomicOfferType : OrderItemTypeEnum.values() )
		{
			if ( atomicOfferType.castorPrefixes != null )
			{
				for ( String castorPrefix : atomicOfferType.castorPrefixes )
				{
					map.put( castorPrefix, atomicOfferType );
				}
			}
		}
	}

	private OrderItemTypeEnum( String... castorPrefix )
	{
		this.castorPrefixes = castorPrefix;
	}

	public static OrderItemTypeEnum getAtomicOfferTypeForSpecificationCode( String specificationCode )
	{

		String extractedCastorPrefix = specificationCode.substring( 0, 5 );
		return map.get( extractedCastorPrefix );
	}

}
