/**
 *
 */
package com.orangecaraibe.enabler.externalws.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.externalws.utils.ExternalWSConstants;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CommercialOperationTypeEnum;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CustomerOrder;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CustomerOrderItem;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.Distributor;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.FunctionSpecification;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.User;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.UserIdentity;

/**
 * Helper pour la création d'un Customer Order
 * 
 * @author sgodard
 */
@Component
public class CustomerOrderCreateHelper
{

	@Value( "${user.epoint.userid}" )
	private String epointUserId;

	@Value( "${user.epoint.dealerid}" )
	private String epointDealerId;

	@Value( "${user.epoint.login}" )
	private String epointUserLogin;

	@Value( "${user.epoint.channel}" )
	private String epointChannelId;

	@Autowired
	private CustomerOrderItemCreateHelper customerOrderItemCreateHelper;

	/**
	 * Création d'un Customer Order sans item Pour l'instant, on n'y met que les infos nécessaires pour la suppresion
	 * d'une option
	 * 
	 * @param coId
	 * @param orderType
	 * @param msisdn
	 * @return
	 */
	public CustomerOrder createCustomerOrder( String coId, String orderType, String msisdn, String customerId )
	{
		CustomerOrder customerOrder = new CustomerOrder();

		customerOrder.setDescription( ExternalWSConstants.EPT_ORDER + " - " + ExternalWSConstants.CONTRACT_ID + " "
			+ coId );

		customerOrder.setLocalOrderType( orderType );
		customerOrder.setLocalBillingAccountID( customerId );
		// TODO gestion des nouvelle list d'installedPublicKey
		com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.InstalledPublicKey ipk =
			new com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.InstalledPublicKey();
		ipk.setInstalledPublicKeyID( msisdn );
		FunctionSpecification functionSpe = new FunctionSpecification();
		functionSpe.setFunctionSpecificationCode( "FUCS_MSISDN" );
		ipk.setFunctionSpecification( functionSpe );
		customerOrder.getLocalInstalledPublicKey().add( ipk );

		// Distributeur
		Distributor distributor = new Distributor();
		distributor.setChannelID( epointChannelId );
		distributor.setDistributionContractID( epointDealerId );
		customerOrder.setDistributor( distributor );

		// Création du creator
		UserIdentity user = new UserIdentity();
		User creator = new User();
		user.setGID( epointUserLogin + ":" + epointUserId );
		creator.setUserIdentity( user );
		customerOrder.setCreator( creator );

		return customerOrder;

	}

	/**
	 * Ajouter un item contrat au customerOrder
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param msisdn
	 * @param commercialOperationType
	 */
	private int addCustomerOrderContractItem(	CustomerOrder customerOrder, String coId, String tmCode, String msisdn,
												CommercialOperationTypeEnum commercialOperationType )
	{
		CustomerOrderItem customerOrderItem =
			customerOrderItemCreateHelper.createCustomerOrderItemContract(	customerOrder, coId, tmCode, msisdn,
																			commercialOperationType );
		customerOrder.getCustomerOrderItem().add( customerOrderItem );
		return customerOrder.getCustomerOrderItem().size() - 1;
	}

	/**
	 * Ajouter un item contrat à CHILDCHANGE au customerOrder
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param msisdn
	 */
	public int addCustomerOrderContractItemChildChange( CustomerOrder customerOrder, String coId, String tmCode,
														String msisdn )
	{
		return addCustomerOrderContractItem(	customerOrder, coId, tmCode, msisdn,
												CommercialOperationTypeEnum.CHILD_CHANGE );
	}

	/**
	 * Ajouter un item play au customerOrder
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param msisdn
	 * @param commercialOperationType
	 * @param indiceParent
	 * @return
	 */
	private int addCustomerOrderPlayMobile( CustomerOrder customerOrder, String coId, String tmCode, String msisdn,
											CommercialOperationTypeEnum commercialOperationType, int indiceParent )
	{
		CustomerOrderItem customerOrderItem =
			customerOrderItemCreateHelper.createCustomerOrderItemPlay(	customerOrder, coId, tmCode, msisdn,
																		commercialOperationType, indiceParent );
		customerOrder.getCustomerOrderItem().add( customerOrderItem );
		return customerOrder.getCustomerOrderItem().size() - 1;
	}

	/**
	 * Ajouter un item play à CHILDCHANGE au customerOrder
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param msisdn
	 * @param indiceParent
	 * @return
	 */
	public int addCustomerOrderPlayMobileChildChange(	CustomerOrder customerOrder, String coId, String tmCode,
														String msisdn, int indiceParent )
	{
		return addCustomerOrderPlayMobile(	customerOrder, coId, tmCode, msisdn,
											CommercialOperationTypeEnum.CHILD_CHANGE, indiceParent );
	}

	/**
	 * Ajouter un item atomic offer au customerOrder
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param spCode
	 * @param snCode
	 * @param commercialOperationType
	 * @param indiceParent
	 */
	private void addCustomerOrderAtomicOfferOptionFromLegacy(	CustomerOrder customerOrder, String coId, String tmCode,
																String spCode, String snCode,
																CommercialOperationTypeEnum commercialOperationType,
																int indiceParent )
	{
		CustomerOrderItem customerOrderItem =
			customerOrderItemCreateHelper.createCustomerOrderItemOptionFromLegacy(	customerOrder, coId, tmCode, spCode,
																					snCode, commercialOperationType,
																					indiceParent );
		customerOrder.getCustomerOrderItem().add( customerOrderItem );
	}

	/**
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param offerSpecificationCode
	 * @param commercialOperationType
	 * @param indicePlay
	 */
	private void addCustomerOrderAtomicOfferOptionFromCatalogue(	CustomerOrder customerOrder, String coId,
																	String tmCode, String offerSpecificationCode,
																	CommercialOperationTypeEnum commercialOperationType,
																	int indicePlay )
	{
		CustomerOrderItem customerOrderItem =
			customerOrderItemCreateHelper.createCustomerOrderItemOptionFromCatalogue(	customerOrder, coId,
																						offerSpecificationCode,
																						commercialOperationType,
																						indicePlay );
		customerOrder.getCustomerOrderItem().add( customerOrderItem );

	}

	/**
	 * Ajouter un item atomic offer à REMOVE au customerOrder
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param spCode
	 * @param snCode
	 * @param indiceParent
	 */
	public void addCustomerOrderAtomicOfferOptionRemoveFromLegacy(	CustomerOrder customerOrder, String coId,
																	String tmCode, String spCode, String snCode,
																	int indiceParent )
	{

		addCustomerOrderAtomicOfferOptionFromLegacy(	customerOrder, coId, tmCode, spCode, snCode,
														CommercialOperationTypeEnum.REMOVE, indiceParent );
	}

	/**
	 * Ajouter un item atomic offer à ADD au customerOrder
	 * 
	 * @param customerOrder
	 * @param coId
	 * @param tmCode
	 * @param spCode
	 * @param snCode
	 * @param indiceParent
	 */
	public void addCustomerOrderAtomicOfferOptionAddFromLegacy( CustomerOrder customerOrder, String coId, String tmCode,
																String spCode, String snCode, int indiceParent )
	{
		addCustomerOrderAtomicOfferOptionFromLegacy(	customerOrder, coId, tmCode, spCode, snCode,
														CommercialOperationTypeEnum.ADD, indiceParent );
	}

	public void addCustomerOrderAtomicOfferOptionAddFromCatalogue(	CustomerOrder customerOrder, String coId,
																	String tmCode, String offerSpecificationCode,
																	int indicePlay )
	{
		addCustomerOrderAtomicOfferOptionFromCatalogue( customerOrder, coId, tmCode, offerSpecificationCode,
														CommercialOperationTypeEnum.ADD, indicePlay );

	}

	public void addCustomerOrderAtomicOfferOptionRemoveFromCatalogue(	CustomerOrder customerOrder, String coId,
																		String tmCode, String offerSpecificationCode,
																		int indicePlay )
	{
		addCustomerOrderAtomicOfferOptionFromCatalogue( customerOrder, coId, tmCode, offerSpecificationCode,
														CommercialOperationTypeEnum.REMOVE, indicePlay );

	}

	/**
	 * @param customerOrderItemCreateHelper the customerOrderItemCreateHelper to set
	 */
	public void setCustomerOrderItemCreateHelper( CustomerOrderItemCreateHelper customerOrderItemCreateHelper )
	{
		this.customerOrderItemCreateHelper = customerOrderItemCreateHelper;
	}

	/**
	 * @param epointUserId the epointUserId to set
	 */
	public void setEpointUserId( String epointUserId )
	{
		this.epointUserId = epointUserId;
	}

	/**
	 * @param epointDealerId the epointDealerId to set
	 */
	public void setEpointDealerId( String epointDealerId )
	{
		this.epointDealerId = epointDealerId;
	}

	/**
	 * @param epointUserLogin the epointUserLogin to set
	 */
	public void setEpointUserLogin( String epointUserLogin )
	{
		this.epointUserLogin = epointUserLogin;
	}

	/**
	 * @param epointChannelId the epointChannelId to set
	 */
	public void setEpointChannelId( String epointChannelId )
	{
		this.epointChannelId = epointChannelId;
	}

}
