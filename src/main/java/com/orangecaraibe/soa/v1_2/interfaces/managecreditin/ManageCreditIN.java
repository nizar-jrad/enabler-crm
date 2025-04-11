/**
 *
 */
package com.orangecaraibe.soa.v1_2.interfaces.managecreditin;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.CreateUpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException;

/**
 * @author gjospitre Interface du webservice ManageCreditIn de gestion du credit de communication
 */
@Deprecated
@WebService( targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCreditIN",
				name = "manageCreditIN" )
@SOAPBinding( style = Style.RPC, use = Use.LITERAL )
public interface ManageCreditIN
{

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCreditIN/setCreditIn" )
	public void setCreditIN( @WebParam( name = "msisdn",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String msisdn, @WebParam( name = "ammount",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	double amount, @WebParam( name = "daysActive",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	int daysActive, @WebParam( name = "daysInactive",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	int daysInactive )
		throws InterfaceUnavailableException, CreateUpdateException;

	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCreditIN/getCreditIn" )
	public Double getCreditIN( @WebParam( name = "msisdn",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageCRM/Schemas" )
	String msisdn )
		throws InterfaceUnavailableException, FindAndGetException;
}
