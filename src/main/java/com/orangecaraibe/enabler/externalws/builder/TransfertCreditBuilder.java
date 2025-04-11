package com.orangecaraibe.enabler.externalws.builder;

import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.externalws.utils.ExternalWSConstants;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.AtomicOfferSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.CommercialOperation;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.CommercialOperationType;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.ContractSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledAtomicOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPublicKey;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionalOperation;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionalOperationType;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.ProductSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledFunctionValue;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledProduct;

@Component
public class TransfertCreditBuilder
{

	/**
	 * Méthode de construction du flux Installed contract pour l'enabler vtr
	 * 
	 * @param msisdn
	 * @param amount
	 * @param daysActive
	 * @param daysInactive
	 * @param customerId
	 * @return
	 */
	public InstalledContract createTransertCreditInstalledOffer(	String msisdn, Double amount, int daysActive,
																	int daysInactive )
	{
		InstalledContract installedContract = new InstalledContract();
		ContractSpecification contractSpecification = new ContractSpecification();
		CommercialOperation commercialOperation = new CommercialOperation();
		commercialOperation.setCommercialOperationType( CommercialOperationType.CHILD_CHANGE );
		contractSpecification.getCommercialOperation().add( commercialOperation );
		installedContract.setOfferSpecification( contractSpecification );

		InstalledAtomicOffer installedAtomicOffer = new InstalledAtomicOffer();
		AtomicOfferSpecification atomicOfferSpecification = new AtomicOfferSpecification();
		atomicOfferSpecification.setOfferSpecificationCode( ExternalWSConstants.OFFER_CREDIT_TRANSFERT );
		CommercialOperation offerCommercialOperation = new CommercialOperation();
		offerCommercialOperation.setCommercialOperationType( CommercialOperationType.ADD );
		atomicOfferSpecification.getCommercialOperation().add( offerCommercialOperation );
		atomicOfferSpecification.setCustomerMaxNumberOfOccurences( 0 );
		atomicOfferSpecification.setMaxNumberOfCustomers( 0 );
		atomicOfferSpecification.setDeliveryDuration( 0 );
		installedAtomicOffer.setOfferSpecification( atomicOfferSpecification );

		InstalledProduct installedProduct = new InstalledProduct();
		ProductSpecification productSpecification = new ProductSpecification();
		productSpecification.setProductSpecificationCode( ExternalWSConstants.OFFER_CREDIT_TRANSFERT );
		FunctionalOperation functionalOperation = new FunctionalOperation();
		functionalOperation.setFunctionalOperationType( FunctionalOperationType.ADD );
		productSpecification.setFunctionalOperation( functionalOperation );
		installedProduct.setProductSpecification( productSpecification );
		installedAtomicOffer.getInstalledProducts().add( installedProduct );

		// LE CREDIT DE COMMUNICATION
		InstalledFunctionValue installedFunctionValue = new InstalledFunctionValue();
		installedFunctionValue.setIsInstalledPublicKey( false );
		installedFunctionValue.setFunctionValue( String.valueOf( amount ) );
		FunctionSpecification functionSpecification = new FunctionSpecification();
		functionSpecification.setFunctionSpecificationCode( ExternalWSConstants.FUNCTION_SPECIFICATION_MONTANT_CREDIT );
		functionSpecification.setIsPublicKey( false );
		installedFunctionValue.setFunctionSpecification( functionSpecification );
		installedProduct.getInstalledFunctionValues().add( installedFunctionValue );

		// LE DAY ACTIVE
		InstalledFunctionValue activeInstalledFunctionValue = new InstalledFunctionValue();
		activeInstalledFunctionValue.setIsInstalledPublicKey( false );
		activeInstalledFunctionValue.setFunctionValue( String.valueOf( daysActive ) );
		FunctionSpecification activeFunctionSpecification = new FunctionSpecification();
		activeFunctionSpecification.setFunctionSpecificationCode( ExternalWSConstants.FUNCTION_SPECIFICATION_DAYACTIVE_CREDIT );
		activeFunctionSpecification.setIsPublicKey( false );
		activeInstalledFunctionValue.setFunctionSpecification( activeFunctionSpecification );
		installedProduct.getInstalledFunctionValues().add( activeInstalledFunctionValue );

		// LE DAY INACTIVE
		InstalledFunctionValue inactiveInstalledFunctionValue = new InstalledFunctionValue();
		inactiveInstalledFunctionValue.setIsInstalledPublicKey( false );
		inactiveInstalledFunctionValue.setFunctionValue( String.valueOf( daysInactive ) );
		FunctionSpecification inactiveFunctionSpecification = new FunctionSpecification();
		inactiveFunctionSpecification.setFunctionSpecificationCode( ExternalWSConstants.FUNCTION_SPECIFICATION_DAYINACTIVE_CREDIT );
		inactiveFunctionSpecification.setIsPublicKey( false );
		inactiveInstalledFunctionValue.setFunctionSpecification( inactiveFunctionSpecification );
		installedProduct.getInstalledFunctionValues().add( inactiveInstalledFunctionValue );

		// LE MSISDN
		InstalledPublicKey installedPublicKey = new InstalledPublicKey();
		installedPublicKey.setInstalledPublicKeyID( msisdn );
		// TODO gestion des nouvelle list d'installedPublicKey
		installedContract.getInstalledPublicKey().add( installedPublicKey );
		installedContract.getChildrenInstalledOffers().add( installedAtomicOffer );

		return installedContract;

	}
}
