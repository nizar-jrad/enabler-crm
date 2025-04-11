/**
 *
 */
package com.orangecaraibe.enabler.externalws.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.core.catalogue.CatalogueConstantes;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CommercialOperation;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CommercialOperationTypeEnum;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CustomerOrderItem;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.FunctionalOperationTypeEnum;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.OfferSpecification;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.OfferSpecificationTypeEnum;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.OfferTariffSpecification;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.ParentInstalledOffer;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.ParentInstalledOfferStatusEnum;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.ProductOrderItem;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.TariffActionEnum;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.UnitCode;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.CommercialClassification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPlay;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledTariff;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledProduct;

/**
 * Adapter pour la création des customerOrder à partir d'installedOffer
 * 
 * @author sgodard
 */
@Component
public class CustomerOrderCreateAdapter
{

	/**
	 * Retourne l'offerSpecification à partir de l'installedOffer
	 * 
	 * @param installedOffer
	 * @return offerSpecification
	 */
	public OfferSpecification getOfferSpecification(	InstalledOffer installedOffer,
														CommercialOperationTypeEnum commercialOperationType )
	{

		OfferSpecification offerSpecification = new OfferSpecification();

		offerSpecification.setOfferSpecificationCode( installedOffer.getOfferSpecification().getOfferSpecificationCode() );
		offerSpecification.setOfferSpecificationLabel( installedOffer.getOfferSpecification().getOfferSpecificationLabel() );

		if ( installedOffer instanceof InstalledContract )
		{
			offerSpecification.setOfferSpecificationType( OfferSpecificationTypeEnum.CONTRACT_TYPE );

		}
		else if ( installedOffer instanceof InstalledPlay )
		{
			offerSpecification.setOfferSpecificationType( OfferSpecificationTypeEnum.PLAY );

		}
		else
			offerSpecification.setOfferSpecificationType( OfferSpecificationTypeEnum.ATOMIC_OFFER );

		// Création du tariff spécification
		List<OfferTariffSpecification> tariffs = getTariffSpecification( installedOffer );

		if ( tariffs != null )
		{
			offerSpecification.getTariffSpecification().addAll( tariffs );
		}

		// Classification commerciale
		if ( installedOffer.getOfferSpecification().getCommercialClassifications() != null
			&& installedOffer.getOfferSpecification().getCommercialClassifications().size() > 0 )
		{
			for ( CommercialClassification classification : installedOffer.getOfferSpecification().getCommercialClassifications() )
			{
				com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CommercialClassification commercialClassification =
					new com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CommercialClassification();
				commercialClassification.setCode( classification.getCode() );
				commercialClassification.setLabel( classification.getLabel() );
				commercialClassification.setType( classification.getType() );
				offerSpecification.getCommercialClassification().add( commercialClassification );
			}
		}
		// Création du commercial operation
		CommercialOperation commercialOperation = new CommercialOperation();
		commercialOperation.setCommercialOperationType( commercialOperationType );
		offerSpecification.setCommercialOperation( commercialOperation );

		return offerSpecification;
	}

	/**
	 * @param commercialOperationType
	 * @param currentInstalledOfferID
	 * @param futureInstalledOfferID
	 * @return
	 */
	private List<ParentInstalledOffer> getParentInstalledOffer( CommercialOperationTypeEnum commercialOperationType,
																String currentInstalledOfferID,
																String futureInstalledOfferID )
	{
		List<ParentInstalledOffer> parentInstalledOffers = new ArrayList<ParentInstalledOffer>();
		ParentInstalledOffer futureParentInstalledOffer = null;
		ParentInstalledOffer currentParentInstalledOffer = null;

		switch ( commercialOperationType )
		{
			case ADD:
				futureParentInstalledOffer = new ParentInstalledOffer();
				futureParentInstalledOffer.setParentInstalledOfferStatus( ParentInstalledOfferStatusEnum.FUTURE );

				futureParentInstalledOffer.setInstalledOfferID( futureInstalledOfferID );
				parentInstalledOffers.add( futureParentInstalledOffer );
				break;

			case REMOVE:
				currentParentInstalledOffer = new ParentInstalledOffer();
				currentParentInstalledOffer.setParentInstalledOfferStatus( ParentInstalledOfferStatusEnum.CURRENT );
				currentParentInstalledOffer.setInstalledOfferID( currentInstalledOfferID );
				parentInstalledOffers.add( currentParentInstalledOffer );
				break;

			default:
				currentParentInstalledOffer = new ParentInstalledOffer();
				currentParentInstalledOffer.setParentInstalledOfferStatus( ParentInstalledOfferStatusEnum.CURRENT );
				currentParentInstalledOffer.setInstalledOfferID( currentInstalledOfferID );
				parentInstalledOffers.add( currentParentInstalledOffer );
				futureParentInstalledOffer = new ParentInstalledOffer();
				futureParentInstalledOffer.setParentInstalledOfferStatus( ParentInstalledOfferStatusEnum.FUTURE );
				futureParentInstalledOffer.setInstalledOfferID( futureInstalledOfferID );
				parentInstalledOffers.add( futureParentInstalledOffer );
				break;
		}

		return parentInstalledOffers;
	}

	/**
	 * Methode de création des parentInstalledOffer
	 * 
	 * @param parentItems
	 * @param commercialOperationType
	 * @return
	 */
	public List<ParentInstalledOffer> getParentInstalledOffer(	List<CustomerOrderItem> parentItems,
																CommercialOperationTypeEnum commercialOperationType )
	{
		String futureInstalledOfferID = parentItems.get( 1 ).getInstalledOffer().getInstalledOfferID();

		String currentInstalledOfferID = parentItems.get( 0 ).getInstalledOffer().getInstalledOfferID();

		return getParentInstalledOffer( commercialOperationType, currentInstalledOfferID, futureInstalledOfferID );
	}

	/**
	 * Methode de récupération des tariffs
	 * 
	 * @param installedOffer
	 * @return
	 */
	private List<OfferTariffSpecification> getTariffSpecification( InstalledOffer installedOffer )
	{
		List<OfferTariffSpecification> tariffs = null;

		String tariffCode = "";
		if ( installedOffer.getOfferSpecification() != null )
		{
			if ( installedOffer.getOfferSpecification().getTariffSpecifications() != null
				&& installedOffer.getOfferSpecification().getTariffSpecifications().size() > 0 )
			{
				tariffCode =
					installedOffer.getOfferSpecification().getTariffSpecifications().get( 0 ).getTariffSpecificationCode();
			}
		}

		if ( CollectionUtils.isNotEmpty( installedOffer.getInstalledTariffs() ) )
		{

			tariffs = new ArrayList<OfferTariffSpecification>();

			for ( InstalledTariff tariff : installedOffer.getInstalledTariffs() )
			{
				OfferTariffSpecification tariffSpecification = new OfferTariffSpecification();

				tariffSpecification.setTariffAction( TariffActionEnum.ADD );

				if ( StringUtils.isEmpty( tariffCode ) )
				{
					tariffCode = CatalogueConstantes.TARRIF_NOT_MAPPED;
				}

				tariffSpecification.setTariffSpecificationCode( tariffCode );

				tariffSpecification.setValue( tariff.getValue() );

				if ( tariff.getUnit() != null )
					tariffSpecification.setUnit( UnitCode.valueOf( tariff.getUnit().name() ) );
				else
					tariffSpecification.setUnit( UnitCode.EURO );

				tariffSpecification.setValueIncludingTax( tariff.getValueIncludingTax() );

				tariffs.add( tariffSpecification );
			}
		}

		if ( StringUtils.isNotEmpty( tariffCode ) && ( tariffs == null || tariffs.isEmpty() ) )
		{// il faut le code tarif sur les aopl afin d'envoyer une
			// AOPL avec un TCPl a l'OM
			tariffs = new ArrayList<OfferTariffSpecification>();
			OfferTariffSpecification tariffSpecification = new OfferTariffSpecification();
			tariffSpecification.setTariffAction( TariffActionEnum.ADD );
			tariffSpecification.setTariffSpecificationCode( tariffCode );
			tariffSpecification.setUnit( UnitCode.EURO );

			tariffs.add( tariffSpecification );
		}

		return tariffs;
	}

	public ProductOrderItem getProductOrderItem(	InstalledProduct installedProduct,
													FunctionalOperationTypeEnum operation )
	{
		ProductOrderItem productOrderItem = new ProductOrderItem();
		com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.InstalledProduct serviceProduct =
			new com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.InstalledProduct();
		com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.FunctionalOperation serviceFunctionalOperation =
			new com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.FunctionalOperation();
		com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.ProductSpecification serviceProductSpecification =
			new com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.ProductSpecification();

		serviceProduct.setInstalledProductID( installedProduct.getInstalledProductID() );
		serviceFunctionalOperation.setFunctionalOperationType( operation );
		if ( installedProduct.getProductSpecification() != null )
		{
			serviceProductSpecification.setProductSpecificationCode( installedProduct.getProductSpecification().getProductSpecificationCode() );
		}

		productOrderItem.setInstalledProduct( serviceProduct );
		productOrderItem.setFunctionalOperation( serviceFunctionalOperation );
		productOrderItem.setProductSpecification( serviceProductSpecification );
		return productOrderItem;
	}

}
