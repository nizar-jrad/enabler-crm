package com.orangecaraibe.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orangecaraibe.enabler.SoapEnabler;
import com.orangecaraibe.enabler.creditin.soa.provider.ManageCreditINProvider;
import com.orangecaraibe.enabler.crm.soa.provider.ManageBillingAccountProvider;
import com.orangecaraibe.enabler.crm.soa.provider.ManageCustomerProblemProvider;
import com.orangecaraibe.enabler.crm.soa.provider.ManageInteractionProvider;
import com.orangecaraibe.enabler.crm.soa.provider.ManageWebcareAccountProvider;
import com.orangecaraibe.enabler.crm.soa.provider.WSTroubleTicketProvider;
import com.orangecaraibe.enabler.epoint.soa.provider.ManageCRMProvider;
import com.orangecaraibe.enabler.handset.soa.provider.ManageHandSetProvider;
import com.orangecaraibe.enabler.ussd.soa.provider.ManageUSSDProvider;
import com.orangecaraibe.enabler.svi.soa.provider.ManageSVIProvider;

@SuppressWarnings( { "unchecked", "rawtypes" } )
@Configuration
public class WebServiceSoapProviderConfig
{

	@Autowired
	@Qualifier( "soapEnabler" )
	private SoapEnabler soapEnabler;

	@Autowired
	@Qualifier( Bus.DEFAULT_BUS_ID )
	private SpringBus bus;

	@Autowired
	private ApplicationContext applicationContext;

	private EndpointImpl getConfiguredEndpointBean( boolean hasHandler, boolean mtomEnabled, String address,
													Class implementorClass )
	{
		Object implementor = applicationContext.getBean( implementorClass );
		EndpointImpl endpoint = new EndpointImpl( bus, implementor );
		if ( hasHandler )
		{
			endpoint.getHandlers().add( soapEnabler );
		}

		if ( mtomEnabled )
		{
			Map<String, Object> properties = new HashMap<>();
			properties.put( "mtom-enabled", Boolean.TRUE.toString() );
			endpoint.setProperties( properties );
		}
		endpoint.publish( address );
		return endpoint;
	}

	@Bean( name = { "manageCustomerProblemEndPoint" } )
	public EndpointImpl manageCustomerProblemEndPoint()
	{
		return getConfiguredEndpointBean( true, true, "/manageCustomerProblem", ManageCustomerProblemProvider.class );
	}

	@Bean( name = { "manageHandSetEndPoint" } )
	public EndpointImpl manageHandSetEndPoint()
	{
		return getConfiguredEndpointBean( true, true, "/manageHandSet", ManageHandSetProvider.class );
	}

	@Bean( name = { "manageUSSDEndPoint" } )
	public EndpointImpl manageUSSDEndPoint()
	{
		return getConfiguredEndpointBean( true, false, "/manageUSSD", ManageUSSDProvider.class );
	}

	@Bean( name = { "manageSVIEndPoint" } )
	public EndpointImpl manageSVIEndPoint()
	{
		return getConfiguredEndpointBean( true, false, "/manageSVI", ManageSVIProvider.class );
	}

	@Bean( name = { "manageCRMEndPoint" } )
	public EndpointImpl manageCRMEndPoint()
	{
		return getConfiguredEndpointBean( true, false, "/manageCRM", ManageCRMProvider.class );
	}

	@Bean( name = { "manageCreditINEndPoint" } )
	public EndpointImpl manageCreditINEndPoint()
	{
		return getConfiguredEndpointBean( true, false, "/manageCreditIN", ManageCreditINProvider.class );
	}

	@Bean( name = { "manageWebcareAccountEndPoint" } )
	public EndpointImpl manageWebcareAccountEndPoint()
	{
		return getConfiguredEndpointBean( true, false, "/manageWebcareAccount", ManageWebcareAccountProvider.class );
	}

	@Bean( name = { "manageBillingAccountEndPoint" } )
	public EndpointImpl manageBillingAccountEndPoint()
	{
		return getConfiguredEndpointBean( true, false, "/manageBillingAccount", ManageBillingAccountProvider.class );
	}

	@Bean( name = { "manageInteractionEndPoint" } )
	public EndpointImpl manageInteractionEndPoint()
	{
		return getConfiguredEndpointBean( true, true, "/manageInteraction", ManageInteractionProvider.class );
	}

	@Bean( name = { "wsTroubleTicketEndPoint" } )
	public EndpointImpl wsTroubleTicketEndPoint()
	{
		return getConfiguredEndpointBean( true, true, "/wsTroubleTicket", WSTroubleTicketProvider.class );
	}

}