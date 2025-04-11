/**
 *
 */
package com.orangecaraibe.enabler.externalws.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.externalws.adapters.CustomerOrderCreateAdapter;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CommercialOperationTypeEnum;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CustomerOrder;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CustomerOrderItem;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.FunctionalOperationTypeEnum;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.OfferSpecification;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.ParentInstalledOffer;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.ProductOrderItem;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledAtomicOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledProduct;

/**
 * Helper pour la création des CustomerOrderItem
 * 
 * @author sgodard
 */
@Component
public class CustomerOrderItemCreateHelper
{
	@Autowired
	private InstalledOfferHelper installedOfferHelper;

	@Autowired
	private CustomerOrderCreateAdapter customerOrderCreateAdapter;

	/**
	 * Créer un CustomerOrderItem pour le contrat
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param msisdn
	 * @param commercialOperationTypeEnum
	 * @return
	 */
	protected CustomerOrderItem createCustomerOrderItemContract(	CustomerOrder customerOrder, String coId,
																	String tmCode, String msisdn,
																	CommercialOperationTypeEnum commercialOperationType )
	{
		// Remonte un seul élément
		List<InstalledOffer> installedOffers = installedOfferHelper.getContractOffer( coId, tmCode, msisdn );
		InstalledOffer installedContract = installedOffers.get( 0 );

		List<CustomerOrderItem> customerOrderItems = customerOrder.getCustomerOrderItem();

		CustomerOrderItem customerOrderItem =
			createCustomerOrderItem( installedContract, commercialOperationType, customerOrderItems, null );

		return customerOrderItem;
	}

	/**
	 * Créer un CustomerOrderItem pour le play mobile
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param msisdn
	 * @param commercialOperationTypeEnum
	 * @return
	 */
	protected CustomerOrderItem createCustomerOrderItemPlay(	CustomerOrder customerOrder, String coId, String tmCode,
																String msisdn,
																CommercialOperationTypeEnum commercialOperationType,
																int indiceParent )
	{
		// Remonte un seul élément
		List<InstalledOffer> installedOffers = installedOfferHelper.getPlayOffer( coId, tmCode, msisdn );
		InstalledOffer installedPlay = installedOffers.get( 0 );

		List<CustomerOrderItem> customerOrderItems = customerOrder.getCustomerOrderItem();

		// Le parent du play est le COPL
		List<CustomerOrderItem> parentItems = new ArrayList<CustomerOrderItem>();

		// On doit renseigner le current et le future dans le cas du plays ce sont les mêmes
		parentItems.add( customerOrder.getCustomerOrderItem().get( indiceParent ) );
		parentItems.add( customerOrder.getCustomerOrderItem().get( indiceParent ) );

		CustomerOrderItem customerOrderItem =
			createCustomerOrderItem( installedPlay, commercialOperationType, customerOrderItems, parentItems );

		return customerOrderItem;
	}

	/**
	 * Créer un CustomerOrderItem pour une option
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param msisdn
	 * @param commercialOperationTypeEnum
	 * @return
	 */
	protected CustomerOrderItem createCustomerOrderItemOptionFromLegacy(	CustomerOrder customerOrder, String coId,
																			String tmCode, String spCode, String snCode,
																			CommercialOperationTypeEnum commercialOperationType,
																			int indiceParent )
	{
		// Remonte un seul élément
		List<InstalledOffer> installedOffers = installedOfferHelper.getIconAtomicOffer( tmCode, spCode, snCode );
		InstalledOffer installedAtomicOffer = installedOffers.get( 0 );

		List<CustomerOrderItem> customerOrderItems = customerOrder.getCustomerOrderItem();

		// Le parent du play est le COPL
		List<CustomerOrderItem> parentItems = new ArrayList<CustomerOrderItem>();

		// On doit renseigner le current et le future dans le cas du plays ce sont les mêmes
		parentItems.add( customerOrder.getCustomerOrderItem().get( indiceParent ) );
		parentItems.add( customerOrder.getCustomerOrderItem().get( indiceParent ) );

		CustomerOrderItem customerOrderItem =
			createCustomerOrderItem( installedAtomicOffer, commercialOperationType, customerOrderItems, parentItems );

		return customerOrderItem;
	}

	/**
	 * Créer un CustomerOrderItem pour une option
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param msisdn
	 * @param commercialOperationTypeEnum
	 * @return
	 */

	protected CustomerOrderItem createCustomerOrderItemOptionFromCatalogue( CustomerOrder customerOrder, String coId,
																			String offerSpecificationCode,
																			CommercialOperationTypeEnum commercialOperationType,
																			int indicePlay )

	{
		// Remonte un seul élément
		InstalledOffer installedAtomicOffer = installedOfferHelper.getIconAtomicOffer( offerSpecificationCode );

		List<CustomerOrderItem> customerOrderItems = customerOrder.getCustomerOrderItem();

		// Le parent du play est le COPL
		List<CustomerOrderItem> parentItems = new ArrayList<CustomerOrderItem>();

		// On doit renseigner le current et le future dans le cas du plays ce sont les mêmes
		parentItems.add( customerOrder.getCustomerOrderItem().get( indicePlay ) );
		parentItems.add( customerOrder.getCustomerOrderItem().get( indicePlay ) );

		CustomerOrderItem customerOrderItem =
			createCustomerOrderItem( installedAtomicOffer, commercialOperationType, customerOrderItems, parentItems );

		return customerOrderItem;
	}

	/**
	 * Créer un customerOrderItem
	 * 
	 * @param installedContract
	 * @param commercialOperationType
	 * @param allItems
	 * @param parentItems
	 * @return un objet customerOrderItem
	 */
	private CustomerOrderItem createCustomerOrderItem(	InstalledOffer installedContract,
														CommercialOperationTypeEnum commercialOperationType,
														List<CustomerOrderItem> allItems,
														List<CustomerOrderItem> parentItems )
	{

		CustomerOrderItem customerOrderItem = new CustomerOrderItem();

		// La numérotation importante car elle permet de définir les liens de
		// parentés
		String itemID = createItemId( allItems.size() );
		customerOrderItem.setCustomerOrderItemID( itemID );

		customerOrderItem.setQuantity( 1 );

		// ////////////////////////////////// Création de l'installed offer
		com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.InstalledOffer itemInstalledOffer =
			new com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.InstalledOffer();
		itemInstalledOffer.setInstalledOfferID( installedContract.getInstalledOfferID() );

		// On ajoute les liens de parenté
		if ( parentItems != null )
		{
			String parentItemID = parentItems.get( 0 ).getCustomerOrderItemID();
			customerOrderItem.setParentCustomerOrderItemID( parentItemID );

			List<ParentInstalledOffer> parentIsntalledOffers =
				customerOrderCreateAdapter.getParentInstalledOffer( parentItems, commercialOperationType );
			itemInstalledOffer.getParentInstalledOffer().addAll( parentIsntalledOffers );
		}

		customerOrderItem.setInstalledOffer( itemInstalledOffer );

		// ////////////////////////////////// Création de l'offer spécification
		OfferSpecification offerSpecification = new OfferSpecification();

		if ( installedContract.getOfferSpecification() != null )
		{
			offerSpecification =
				customerOrderCreateAdapter.getOfferSpecification( installedContract, commercialOperationType );
		}

		customerOrderItem.setOfferSpecification( offerSpecification );

		// On ne va pas jusqu'à la création du produit service et des fonctions
		// car on ne traite actuellement que la suppresion d'option
		if ( commercialOperationType != null && commercialOperationType.equals( CommercialOperationTypeEnum.ADD ) )
		{

			// avant de remonter productOrder, on vérifie la présence de l'installedPrduct dans l'installedOffer
			if ( installedContract instanceof InstalledAtomicOffer
				&& ( CollectionUtils.isNotEmpty( ( (InstalledAtomicOffer) installedContract ).getInstalledProducts() ) ) )
			{
				String productItemID = "";
				ProductOrderItem productOrderItem = null;
				productItemID =
					createItemId( ( (InstalledAtomicOffer) installedContract ).getInstalledProducts().size() );

				InstalledProduct installedProduct =
					( (InstalledAtomicOffer) installedContract ).getInstalledProducts().get( 0 );
				productOrderItem =
					customerOrderCreateAdapter.getProductOrderItem( installedProduct, FunctionalOperationTypeEnum.ADD );
				productOrderItem.setProductOrderItemID( productItemID );
				customerOrderItem.getProductOrderItem().add( productOrderItem );
			}

		}

		return customerOrderItem;
	}

	/**
	 * Methode de génération du customer order item id
	 * 
	 * @param id
	 * @return
	 */
	private String createItemId( int id )
	{
		int nextID = id + 1;
		String textID = String.valueOf( nextID );

		switch ( textID.length() )
		{
			case 2:
				return "000" + textID;
			case 3:
				return "00" + textID;
			case 4:
				return "0" + textID;
			case 5:
				return textID;
			default:
				return "0000" + textID;
		}
	}

	/**
	 * @param installedOfferHelper the installedOfferHelper to set
	 */
	public void setInstalledOfferHelper( InstalledOfferHelper installedOfferHelper )
	{
		this.installedOfferHelper = installedOfferHelper;
	}

	/**
	 * @param customerOrderCreateAdapter the customerOrderCreateAdapter to set
	 */
	public void setCustomerOrderCreateAdapter( CustomerOrderCreateAdapter customerOrderCreateAdapter )
	{
		this.customerOrderCreateAdapter = customerOrderCreateAdapter;
	}

}
