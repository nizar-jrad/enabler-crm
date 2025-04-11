package com.orangecaraibe.soa.v1_2.interfaces.managesvi;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;


/**
 * @author sgodard Webservice de gestion des informations de portabilité formatées pour l'ussd
 */

@WebService( targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageSVI", name = "manageSVI" )
@SOAPBinding( style = Style.RPC, use = Use.LITERAL  )
public interface ManageSVI
{
	@WebMethod( action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageSVI/getSVIFormatedPortabilityInfo" )
	public String getSVIFormattedPortabilityInfo(

													@WebParam( name = "msisdn",
																	targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageSVI/Schemas" )
													String msisdn );

}
