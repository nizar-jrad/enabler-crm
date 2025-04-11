/**
 *
 */
package com.orangecaraibe.soa.v1_2.interfaces.managecrm;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.orangecaraibe.soa.v2.interfaces.manageportability.InterfaceUnavailableException;

/**
 * @author sgodard Interface du webservice ManageCRM de gestion des informations de portabilité pour Epoint
 */
@Deprecated
@WebService( targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM", name = "manageCRM" )
@SOAPBinding( style = Style.RPC, use = Use.LITERAL )
public interface ManageCRM
{
	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/checkOutgoingRIO" )
	public String checkOutgoingRIO( @WebParam( name = "msisdn",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String msisdn, @WebParam( name = "rio",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String rio, @WebParam( name = "coId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String coId, @WebParam( name = "typeClient",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String typeClient );

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/getOutgoingRIO" )
	public String getOutgoingRIO( @WebParam( name = "msisdn",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String msisdn, @WebParam( name = "coId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String coId, @WebParam( name = "typeClient",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String typeClient );

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/createOrderRemoveService" )
	public String createOrderRemoveService( @WebParam( name = "coId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String coId, @WebParam( name = "msisdn",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String msisdn, @WebParam( name = "tmCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String tmCode, @WebParam( name = "snCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String snCode, @WebParam( name = "spCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String spCode, @WebParam( name = "customerId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String customerId );

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/getClasification" )
	public String getClassifications( @WebParam( name = "tmCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String tmCode, @WebParam( name = "snCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String snCode, @WebParam( name = "spCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String spCode )
		throws InterfaceUnavailableException, Exception;

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/getClasification" )
	public String getClassification( @WebParam( name = "tmCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String tmCode, @WebParam( name = "snCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String snCode, @WebParam( name = "spCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String spCode, @WebParam( name = "libeller",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String libeller )
		throws InterfaceUnavailableException, Exception;

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/creditIN" )
	public String creditIN( @WebParam( name = "coId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String coId, @WebParam( name = "msisdn",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String msisdn, @WebParam( name = "tmCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String tmCode, @WebParam( name = "offerSpecificationCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String offerSpecificationCode, @WebParam( name = "customerId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String customerId );

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/ajoutCreditIn" )
	public String gestofferCodeByAMount( @WebParam( name = "coId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String coId, @WebParam( name = "msisdn",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String msisdn, @WebParam( name = "montant",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String montant, @WebParam( name = "tmCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String tmCode, @WebParam( name = "customerId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String customerId );

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/createOrderAddCatalogueService" )
	public String createOrderAddCatalogueService( @WebParam( name = "coId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String coId, @WebParam( name = "msisdn",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String msisdn, @WebParam( name = "tmCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String tmCode, @WebParam( name = "offerSpecificationCode",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String offerSpecificationCode, @WebParam( name = "customerId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String customerId );

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/getPresenceCompteWebcare" )
	public String getPresenceCompteWebcare( @WebParam( name = "coId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String coId, @WebParam( name = "msisdn",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String msisdn );

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/findAndGetAccordInfoCommerciale" )
	public String findAndGetAccordInfoCommerciale( @WebParam( name = "coId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String coId, @WebParam( name = "customerId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String customerId );

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/addOrRemoveAccordInfoCommerciale" )
	public String addOrRemoveAccordInfoCommerciale( @WebParam( name = "accord",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String accord, @WebParam( name = "coId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String coId, @WebParam( name = "customerId",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String customerId );
}
