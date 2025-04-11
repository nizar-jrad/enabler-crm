package com.orangecaraibe.soa.v1_2.interfaces.managehandset;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@Deprecated
@WebService( targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageHandSet",
				name = "manageHandSet" )
@SOAPBinding( style = Style.RPC, use = Use.LITERAL )
public interface ManageHandSet
{
	@WebMethod( exclude = false,
					action = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageHandSet/findAndGetMobileByImei" )
	public String findAndGetMobileByImei( @WebParam( name = "imei",
					targetNamespace = "http://www.orangecaraibe.com/soa/v1.2/Interfaces/ManageHandSet/Schemas" )
	String imei );
}
