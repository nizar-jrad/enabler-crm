/**
 *
 */
package com.orangecaraibe.enabler.externalws.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.externalws.enums.OrderTypeEnum;
import com.orangecaraibe.enabler.externalws.helpers.CustomerOrderCreateHelper;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CustomerOrder;

/**
 * Builder des CustomerOrder pour les commandes d'options
 * 
 * @author sgodard
 */
@Component
public class CustomerOrderOptionBuilder
{

	@Autowired
	private CustomerOrderCreateHelper customerOrderCreateHelper;

	public CustomerOrder createAddOptionCustomerOrderFromCatalogue( String coId, String msisdn, String tmCode,
																	String offerSpecificationCode, String customerId )
	{
		CustomerOrder customerOrder =
			customerOrderCreateHelper.createCustomerOrder( coId, OrderTypeEnum.OPTION.name(), msisdn, customerId );
		int indiceContrat =
			customerOrderCreateHelper.addCustomerOrderContractItemChildChange( customerOrder, coId, tmCode, msisdn );
		int indicePlay = customerOrderCreateHelper.addCustomerOrderPlayMobileChildChange(	customerOrder, coId, tmCode,
																							msisdn, indiceContrat );
		customerOrderCreateHelper.addCustomerOrderAtomicOfferOptionAddFromCatalogue(	customerOrder, coId, tmCode,
																						offerSpecificationCode,
																						indicePlay );
		return customerOrder;
	}

	/**
	 * Création d'un order de suppression d'option
	 * 
	 * @param coId
	 * @param msisdn
	 * @param tmCode
	 * @param snCode
	 * @param spCode
	 * @return un objet CustomerOrder
	 */
	public CustomerOrder createRemoveOptionCustomerOrderFromLegacy( String coId, String msisdn, String tmCode,
																	String snCode, String spCode, String customerId )
	{
		CustomerOrder customerOrder =
			customerOrderCreateHelper.createCustomerOrder( coId, OrderTypeEnum.OPTION.name(), msisdn, customerId );
		int indiceContrat =
			customerOrderCreateHelper.addCustomerOrderContractItemChildChange( customerOrder, coId, tmCode, msisdn );
		int indicePlay = customerOrderCreateHelper.addCustomerOrderPlayMobileChildChange(	customerOrder, coId, tmCode,
																							msisdn, indiceContrat );
		customerOrderCreateHelper.addCustomerOrderAtomicOfferOptionRemoveFromLegacy(	customerOrder, coId, tmCode,
																						spCode, snCode, indicePlay );
		return customerOrder;
	}

	/**
	 * @param customerOrderCreateHelper the customerOrderCreateHelper to set
	 */
	public void setCustomerOrderCreateHelper( CustomerOrderCreateHelper customerOrderCreateHelper )
	{
		this.customerOrderCreateHelper = customerOrderCreateHelper;
	}

}
