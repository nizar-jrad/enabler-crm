/**
 * 
 */
package com.orangecaraibe.enabler.core.catalogue.adapter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.orangecaraibe.api.catalogue.ws.model.Classification;
import com.orangecaraibe.api.catalogue.ws.model.ExternalIdentifier;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.AtomicOfferSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.CommercialClassification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.DiscountSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.TariffSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionValueSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.ProductSpecification;

/**
 * @author ncrtest2
 */
@Component
public class CastorApiAdapter
{

	/**
	 * Adapte une liste de TariffSpecification de l'API Catalogue WS en liste de TariffSpecification de l'Enabler SOA
	 * V2. Les tariffs sont ajoutés à la liste soaTariffSpecifications passée en paramètre.
	 * 
	 * @param soaTariffSpecifications
	 * @param tariffs
	 */
	private void adaptToSoaTariffSpecifications(	List<TariffSpecification> soaTariffSpecifications,
													List<com.orangecaraibe.api.catalogue.ws.model.TariffSpecification> tariffs )
	{

		if ( soaTariffSpecifications != null && CollectionUtils.isNotEmpty( tariffs ) )
		{
			for ( com.orangecaraibe.api.catalogue.ws.model.TariffSpecification tariff : tariffs )
			{
				if ( tariff != null )
				{
					TariffSpecification soaTarrifSpecification = new TariffSpecification();
					soaTarrifSpecification.setTariffSpecificationCode( tariff.getCode() );
					soaTarrifSpecification.setValueIncludingTax( Double.valueOf( tariff.getAmountIncludingTax() ) );
					soaTarrifSpecification.setValue( Double.valueOf( tariff.getAmountWithoutTax() ) );
					adaptToSoaDiscountSpecifications(	soaTarrifSpecification.getDiscountSpecifications(),
														tariff.getDiscount() );
					soaTariffSpecifications.add( soaTarrifSpecification );
				}
			}
		}
	}

	/**
	 * Adapte une liste de DiscountSpecification de l'API Catalogue WS en liste de DiscountSpecification de l'Enabler
	 * SOA V2. Les discounts sont ajoutés à la liste soaDiscountSpecifications passée en paramètre.
	 * 
	 * @param soaDiscountSpecifications
	 * @param discount
	 */
	private void adaptToSoaDiscountSpecifications(	List<DiscountSpecification> soaDiscountSpecifications,
													List<com.orangecaraibe.api.catalogue.ws.model.DiscountSpecification> discounts )
	{

		if ( soaDiscountSpecifications != null && CollectionUtils.isNotEmpty( discounts ) )
		{
			for ( com.orangecaraibe.api.catalogue.ws.model.DiscountSpecification discount : discounts )
			{
				if ( discount != null )
				{
					DiscountSpecification soaDiscountSpecification = new DiscountSpecification();
					soaDiscountSpecification.setDiscountSpecificationCode( discount.getCode() );
					soaDiscountSpecification.setDiscountSpecificationLabel( discount.getLabel() );
					soaDiscountSpecification.setTailorMadeAmount( Double.valueOf( discount.getAmountWithoutTax() ) );
					soaDiscountSpecification.setTailorMadeAmountIncludingTax( Double.valueOf( discount.getAmountIncludingTax() ) );
					// adaptDiscounts
					soaDiscountSpecifications.add( soaDiscountSpecification );
				}
			}
		}
	}

	/**
	 * Adapte une liste de Classification de l'API Catalogue WS en liste de CommercialClassification de l'Enabler SOA
	 * V2. Les classifications sont ajoutées à la liste commercialOperations passée en paramètre.
	 * 
	 * @param commercialClassifications
	 * @param classifications
	 */
	public void adaptToSoaCommercialClassifications(	List<CommercialClassification> commercialClassifications,
														List<Classification> classifications )
	{
		if ( commercialClassifications != null && CollectionUtils.isNotEmpty( classifications ) )
		{
			for ( Classification classification : classifications )
			{
				if ( classification != null )
				{
					CommercialClassification commercialClassif = new CommercialClassification();
					commercialClassif.setCode( classification.getCode() );
					commercialClassif.setLabel( classification.getLabel() );
					commercialClassif.setType( classification.getType() );
					commercialClassifications.add( commercialClassif );
				}
			}
		}
	}

	/**
	 * Adapte une AtomicOfferSpecification de l'API Catalogue WS en AtomicOfferSpecification de l'Enabler SOA V2.
	 * 
	 * @param atomicOfferSpecification
	 * @return AtomicOfferSpecification soaAtomicOfferSpecification
	 */
	public AtomicOfferSpecification adaptToSoaAtomicOfferSpecification( com.orangecaraibe.api.catalogue.ws.model.AtomicOfferSpecification atomicOfferSpecification )
	{

		AtomicOfferSpecification soaAtomicOfferSpecification = new AtomicOfferSpecification();

		if ( atomicOfferSpecification != null )
		{
			soaAtomicOfferSpecification.setOfferSpecificationCode( atomicOfferSpecification.getCode() );
			soaAtomicOfferSpecification.setOfferSpecificationLabel( atomicOfferSpecification.getLabel() );
			soaAtomicOfferSpecification.setLocalLegacyCode( getLegacyCode( atomicOfferSpecification.getExternalIdentifier() ) );
			adaptToSoaCommercialClassifications(	soaAtomicOfferSpecification.getCommercialClassifications(),
													atomicOfferSpecification.getClassification() );
			adaptToSoaTariffSpecifications( soaAtomicOfferSpecification.getTariffSpecifications(),
											atomicOfferSpecification.getTariffs() );
			adaptToSoaProductSpecification( soaAtomicOfferSpecification.getProductSpecifications(),
											atomicOfferSpecification.getProductSpecification() );
		}

		return soaAtomicOfferSpecification;
	}

	/**
	 * Retourne le code legacy à partir d'une liste d'externalIdentifier de l'API Catalogue WS.
	 * 
	 * @param externalIdentifiers
	 * @return String legacyCode
	 */
	private String getLegacyCode( List<ExternalIdentifier> externalIdentifiers )
	{

		String legacyCode = null;

		if ( CollectionUtils.isNotEmpty( externalIdentifiers ) )
		{
			ExternalIdentifier externalIdentifier = externalIdentifiers.stream().filter( ei -> ei != null
				&& ei.getReferential() != null && ei.getReferential().contains( "BSCS" ) ).findFirst().orElse( null );
			legacyCode = externalIdentifier != null ? externalIdentifier.getIdentifier() : null;
		}

		return legacyCode;

	}

	/**
	 * Adapte une ProductSpecification de l'API Catalogue WS en ProductSpecification de l'Enabler SOA V2. La
	 * productSpecification est ajoutée à la liste soaProductSpecifications passée en paramètre.
	 * 
	 * @param soaProductSpecifications
	 * @param productSpecification
	 */
	public void adaptToSoaProductSpecification( List<ProductSpecification> soaProductSpecifications,
												com.orangecaraibe.api.catalogue.ws.model.ProductSpecification productSpecification )
	{
		if ( productSpecification != null )
		{
			ProductSpecification soaProductSpecification = new ProductSpecification();
			soaProductSpecification.setProductSpecificationCode( productSpecification.getCode() );
			soaProductSpecification.setProductSpecificationLabel( productSpecification.getLabel() );
			soaProductSpecifications.add( soaProductSpecification );
		}
	}

	/**
	 * Adapte une liste de FunctionSpecification de l'API Catalogue WS en liste de FunctionSpecification de l'Enabler
	 * SOA V2. Les functionSpecifications sont ajoutées à la liste soaFunctionSpecifications passée en paramètre.
	 * 
	 * @param soaFunctionSpecifications
	 * @param functionSpecifications
	 */
	public void adaptToSoaFunctionSpecification(	List<FunctionSpecification> soaFunctionSpecifications,
													List<com.orangecaraibe.api.catalogue.ws.model.FunctionSpecification> functionSpecifications )
	{
		if ( soaFunctionSpecifications != null && CollectionUtils.isNotEmpty( functionSpecifications ) )
		{
			for ( com.orangecaraibe.api.catalogue.ws.model.FunctionSpecification functionSpecification : functionSpecifications )
			{
				if ( functionSpecification != null )
				{
					FunctionSpecification soaFunctionSpecification = new FunctionSpecification();
					soaFunctionSpecification.setFunctionSpecificationCode( functionSpecification.getCode() );
					soaFunctionSpecification.setFunctionSpecificationLabel( functionSpecification.getLabel() );
					soaFunctionSpecifications.add( soaFunctionSpecification );
				}
			}
		}
	}

	/**
	 * TODO : Comment récupérer une valeur de fonction via l'API Catalogue WS ? <br />
	 * <br />
	 * Adapteurr Catalogue WS en FunctionValueSpecification de l'Enabler SOA V2.
	 * 
	 * @param functionSpecification
	 * @return FunctionValueSpecification
	 */
	public FunctionValueSpecification adaptToSoaFunctionValueSpecification( com.orangecaraibe.api.catalogue.ws.model.FunctionSpecification functionSpecification )
	{
		if ( functionSpecification != null )
		{
			FunctionValueSpecification functionValueSpecification = new FunctionValueSpecification();
			functionValueSpecification.setFunctionValue( null ); // TODO
			functionValueSpecification.setFunctionValueLabel( null ); // TODO
			return functionValueSpecification;
		}
		return null;
	}

}
