/**
 *
 */
package com.orangecaraibe.enabler.externalws.helpers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.core.catalogue.CatalogueHelper;
import com.orangecaraibe.enabler.externalws.enums.OrderItemTypeEnum;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPlay;

/**
 * Helper permettant de récupérer les InstalledOffer(Contract, Play, AO)
 * 
 * @author sgodard
 */
@Component
public class InstalledOfferHelper
{
	@Autowired
	@Qualifier( "crm.catalogueHelper" )
	private CatalogueHelper catalogueHelper;

	/**
	 * Renvoie l' installedContract correspondant à l'offre
	 * 
	 * @param coId
	 * @param tmCode
	 * @param msisdn
	 * @return installedContract
	 */
	private InstalledContract getIconOffer( String coId, String tmCode, String msisdn )
	{
		InstalledContract contract = catalogueHelper.getCastorInstalledContract( coId, tmCode, msisdn );
		return contract;
	}

	/**
	 * Renvoie l'objet castor corespondant au contrat
	 * 
	 * @param coId
	 * @param tmCode
	 * @param msisdn
	 * @return liste contenant l'installed offer du contrat
	 */
	protected List<InstalledOffer> getContractOffer( String coId, String tmCode, String msisdn )
	{
		InstalledContract installedContract = getIconOffer( coId, tmCode, msisdn );
		List<InstalledOffer> installedOffers = new ArrayList<InstalledOffer>();
		// On ne veut que l'objet contrat on supprime donc les enfant afin que la methode récursive ne les remonte pas
		installedContract.getChildrenInstalledOffers().clear();
		installedOffers.add( installedContract );
		return installedOffers;
	}

	/**
	 * Renvoie l'objet castor correspondant au play contrat
	 * 
	 * @param contract
	 * @param customer
	 * @return liste contenant l'installed offer du Play mobile
	 */
	protected List<InstalledOffer> getPlayOffer( String coId, String tmCode, String msisdn )
	{
		InstalledContract installedContract = getIconOffer( coId, tmCode, msisdn );
		List<InstalledOffer> installedOffers = new ArrayList<InstalledOffer>();

		for ( InstalledOffer installedOffer : installedContract.getChildrenInstalledOffers() )
		{
			OrderItemTypeEnum orderItemTypeEnum =
				OrderItemTypeEnum.getAtomicOfferTypeForSpecificationCode( installedOffer.getInstalledOfferID() );

			if ( OrderItemTypeEnum.PLAY_CONTRAT.equals( orderItemTypeEnum ) )
			{
				// On ne veut que le play on supprime donc les enfant afin que la methode récursive ne les remonte pas
				( (InstalledPlay) installedOffer ).getChildrenInstalledOffers().clear();
				installedOffers.add( installedOffer );
				break;
			}
		}
		return installedOffers;
	}

	/**
	 * Renvoie l'objet castor correspondant à l'offre atomique à partir de son code CASTOR
	 * 
	 * @param offerSpecificationCode
	 * @return
	 */
	protected InstalledOffer getIconAtomicOffer( String offerSpecificationCode )
	{
		InstalledOffer installedAtomicOffer = catalogueHelper.getIconAtomicOffer( offerSpecificationCode );

		return installedAtomicOffer;
	}

	/**
	 * Renvoie l'objet castor correspondant à l'offre atomique à partir de ses identifiants externes
	 * 
	 * @param tmCode
	 * @param spCode
	 * @param snCode
	 * @return liste contenant l'installed offer de l'offre atomique
	 */
	protected List<InstalledOffer> getIconAtomicOffer( String tmCode, String spCode, String snCode )
	{
		List<InstalledOffer> installedAtomicOffers = catalogueHelper.getIconAtomicOffer( tmCode, spCode, snCode );

		return installedAtomicOffers;
	}

	/**
	 * @param catalogueHelper the catalogueHelper to set
	 */
	public void setCatalogueHelper( CatalogueHelper catalogueHelper )
	{
		this.catalogueHelper = catalogueHelper;
	}

}
