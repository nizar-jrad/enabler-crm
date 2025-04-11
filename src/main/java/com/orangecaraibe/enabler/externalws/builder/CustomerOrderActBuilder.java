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
 * Builder des CustomerOrder pour les commandes d'achat à l'acte
 * 
 * @author sgodard
 */
@Component
public class CustomerOrderActBuilder
{
	@Autowired
	private CustomerOrderCreateHelper customerOrderCreateHelper;

	private int completeOrder(	CustomerOrder customerOrder, String coId, String msisdn, String tmCode,
								String customerId )
	{
		int indiceContrat =
			customerOrderCreateHelper.addCustomerOrderContractItemChildChange( customerOrder, coId, tmCode, msisdn );
		int indicePlay = customerOrderCreateHelper.addCustomerOrderPlayMobileChildChange(	customerOrder, coId, tmCode,
																							msisdn, indiceContrat );
		return indicePlay;
	}

	public CustomerOrder createAddActCustomerOrderFromCatalogue(	String coId, String msisdn, String tmCode,
																	String offerSpecificationCode, String customerId )
	{
		CustomerOrder customerOrder =
			customerOrderCreateHelper.createCustomerOrder( coId, OrderTypeEnum.ACT.name(), msisdn, customerId );

		int indicePlay = completeOrder( customerOrder, coId, msisdn, tmCode, customerId );

		customerOrderCreateHelper.addCustomerOrderAtomicOfferOptionAddFromCatalogue(	customerOrder, coId, tmCode,
																						offerSpecificationCode,
																						indicePlay );
		return customerOrder;
	}

	public CustomerOrder createAddActCustomerOrderwithAmount(	String coId, String msisdn, String tmCode,
																String offerSpecificationCode, String customerId )
	{
		CustomerOrder customerOrder =
			customerOrderCreateHelper.createCustomerOrder( coId, OrderTypeEnum.ACT.name(), msisdn, customerId );

		completeOrder( customerOrder, coId, msisdn, tmCode, customerId );

		return customerOrder;
	}

	public void setCustomerOrderCreateHelper( CustomerOrderCreateHelper customerOrderCreateHelper )
	{
		this.customerOrderCreateHelper = customerOrderCreateHelper;
	}

}
