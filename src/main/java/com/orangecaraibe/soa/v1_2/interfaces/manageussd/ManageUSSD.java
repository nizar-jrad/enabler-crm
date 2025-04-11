/**
 *
 */
package com.orangecaraibe.soa.v1_2.interfaces.manageussd;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

/**
 * @author sgodard Webservice de gestion des informations de portabilité formatées pour l'ussd
 */

@Deprecated
@WebService( targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageUSSD", name = "manageUSSD" )
@SOAPBinding( style = Style.RPC, use = Use.LITERAL )
public interface ManageUSSD
{
	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageUSSD/getUSSDFormatedPortabilityInfo" )
	public String getUSSDFormatedPortabilityInfo(

													@WebParam( name = "msisdn",
																	targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageUSSD/Schemas" )
													String msisdn );

}
