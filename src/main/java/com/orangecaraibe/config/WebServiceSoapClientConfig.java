package com.orangecaraibe.config;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.orange.interfaces.managetroubleticket.v2.Noname;
import com.orange.sidom.soa.dcrm.servicecontract.campagne._1_0.ManageCampagne;
import com.orange.sidom.soa.dcrm.servicecontract.demande._1_0.ManageDemande;
import com.orange.sidom.soa.dcrm.servicecontract.party._1_0.ManageParty;
import com.orange.sidom.soa.dcrm.servicecontract.theme._1_0.ManageTheme;
import com.orangecaraibe.enabler.SoapEnabler;
import com.orangecaraibe.soa.v1_2.interfaces.managecreditin.ManageCreditIN;
import com.orangecaraibe.soa.v1_2.interfaces.managecrm.ManageCRM;
import com.orangecaraibe.soa.v1_2.interfaces.managehandset.ManageHandSet;
import com.orangecaraibe.soa.v1_2.interfaces.managesvi.ManageSVI;
import com.orangecaraibe.soa.v1_2.interfaces.manageussd.ManageUSSD;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.ManageBillingAccount;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.ManageCommercialInstalledBase;
import com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.ManageCustomerOrderCapture;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.ManageCustomerProblem;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.ManageInteraction;
import com.orangecaraibe.soa.v2.interfaces.manageportability.ManagePortability;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.ManageWebcareAccount;

import fcm.gp.oc.schemas.dunning.data.dunningregistercard.WbsDunningRegistercardSoap;
import fcm.gp.oc.schemas.dunning.data.dunningworkitem.WbsDunningWorkitemSoap;

@SuppressWarnings( { "rawtypes" } )
@Configuration
public class WebServiceSoapClientConfig
{

	@Autowired
	private SoapEnabler soapEnabler;

	@Value( "${soa.username}" )
	private String userName;

	@Value( "${soa.password}" )
	private String password;

	@Value( "${receiveTimeout}" )
	private String receiveTimeOut;

	@Value( "${connectionTimeout}" )
	private String connectionTimeout;

	private JaxWsProxyFactoryBean getConfiguredJaxWsProxyFactoryBean(	boolean hasHandler, boolean mtomEnabled,
																		Class serviceClass, String url )
	{
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setUsername( userName );
		factory.setPassword( password );
		factory.setServiceClass( serviceClass );
		factory.setAddress( url );
		factory.setBus( SpringBusFactory.getDefaultBus( true ) );

		if ( hasHandler )
		{
			factory.getHandlers().add( soapEnabler );
		}
		factory.getHandlers().add( soapEnabler );
		factory.setPassword( password );
		factory.setUsername( userName );
		Map<String, Object> properties = new HashMap<>();
		properties.put( "javax.xml.ws.client.receiveTimeout", receiveTimeOut );
		properties.put( "javax.xml.ws.client.connectionTimeout", connectionTimeout );
		if ( mtomEnabled )
		{

			properties.put( "mtom-enabled", Boolean.TRUE.toString() );
			factory.setProperties( properties );
		}

		return factory;
	}

	@Bean( name = { "manageCustomerOrderConsumer" } )
	@Lazy( true )
	public ManageCustomerOrderCapture getManageCustomerOrderCaptureClient( @Value( "${soa.ordermanager.url}" )
	String soaOrderManagerUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, true, ManageCustomerOrderCapture.class, soaOrderManagerUrl );
		return (ManageCustomerOrderCapture) factory.create();
	}

	@Bean( name = { "superMCIBConsumer" } )
	@Lazy( true )
	public ManageCommercialInstalledBase getManageSuperMCIBClient( @Value( "${soa.supermcib.url}" )
	String soaSuperMCIBUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, true, ManageCommercialInstalledBase.class, soaSuperMCIBUrl );
		return (ManageCommercialInstalledBase) factory.create();
	}

	@Bean( name = { "enablerVTRConsumer" } )
	@Lazy( true )
	public ManageCommercialInstalledBase getManageVTRClient( @Value( "${soa.enablervtr.url}" )
	String soaEnablerVTRUurl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, true, ManageCommercialInstalledBase.class, soaEnablerVTRUurl );
		return (ManageCommercialInstalledBase) factory.create();
	}

	@Bean( name = { "manageCustomerProblemConsumer" } )
	@Lazy( true )
	public ManageCustomerProblem getManageCustomerProblemClient( @Value( "${soa.managecustomerproblem.url}" )
	String soaManageCustomerProblemUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( false, true, ManageCustomerProblem.class, soaManageCustomerProblemUrl );
		return (ManageCustomerProblem) factory.create();
	}

	@Bean( name = { "manageInteractionConsumer" } )
	@Lazy( true )
	public ManageInteraction getManageInteractionClient( @Value( "${soa.manageinteraction.url}" )
	String soaManageInteractionUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false, ManageInteraction.class, soaManageInteractionUrl );
		return (ManageInteraction) factory.create();
	}

	@Bean( name = { "managePortabilityConsumer" } )
	@Lazy( true )
	public ManagePortability getManagePortabilityClient( @Value( "${soa.manageportability.url}" )
	String soaManagePortabilityUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false, ManagePortability.class, soaManagePortabilityUrl );
		return (ManagePortability) factory.create();
	}

	@Bean( name = { "manageHandSetConsumer" } )
	@Lazy( true )
	public ManageHandSet getManageHandSetClient( @Value( "${soa.managehandset.url}" )
	String soaManageHandsetUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( false, true, ManageHandSet.class, soaManageHandsetUrl );
		return (ManageHandSet) factory.create();
	}

	@Bean( name = { "manageUSSDConsumer" } )
	@Lazy( true )
	public ManageUSSD getManageUSSDClient( @Value( "${soa.manageussd.url}" )
	String soaManageUSSDUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( false, true, ManageUSSD.class, soaManageUSSDUrl );
		return (ManageUSSD) factory.create();
	}

	@Bean( name = { "manageSVIConsumer" } )
	@Lazy( true )
	public ManageSVI getManageSVIClient( @Value( "${soa.managesvi.url}" )
	String soaManageSVIUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( false, true, ManageSVI.class, soaManageSVIUrl );
		return (ManageSVI) factory.create();
	}

	@Bean( name = { "manageCRMConsumer" } )
	@Lazy( true )
	public ManageCRM getManageCRMClient( @Value( "${soa.managecrm.url}" )
	String soaManageCrmUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( false, true, ManageCRM.class, soaManageCrmUrl );
		return (ManageCRM) factory.create();
	}

	@Bean( name = { "manageCreditINConsumer" } )
	@Lazy( true )
	public ManageCreditIN getManageCreditINClient( @Value( "${soa.managecreditin.url}" )
	String soaManageCreditINUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( false, true, ManageCreditIN.class, soaManageCreditINUrl );
		return (ManageCreditIN) factory.create();
	}

	@Bean( name = { "manageWebcareAccountConsumer" } )
	@Lazy( true )
	public ManageWebcareAccount getManageWebcareAccountClient( @Value( "${soa.managewebcareaccount.url}" )
	String soaManageWebcareAccountUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( false, true, ManageWebcareAccount.class, soaManageWebcareAccountUrl );
		return (ManageWebcareAccount) factory.create();
	}

	@Bean( name = { "manageBillingAccountConsumer" } )
	@Lazy( true )
	public ManageBillingAccount getManageBillingAccountClient( @Value( "${soa.managebillingaccount.url}" )
	String soaManageBillingAccountUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( false, true, ManageBillingAccount.class, soaManageBillingAccountUrl );
		return (ManageBillingAccount) factory.create();
	}

	@Bean( name = { "manageInteractionCRMConsumer" } )
	@Lazy( true )
	public ManageInteraction getManageInteractionCRMClient( @Value( "${soa.manageinteraction.siclient.url}" )
	String soaManageInteractionUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false, ManageInteraction.class, soaManageInteractionUrl );
		return (ManageInteraction) factory.create();
	}

	@Bean( name = { "wbsDunningWorkitemConsumer" } )
	@Lazy( true )
	public WbsDunningWorkitemSoap getWbsDunningWorkitemClient( @Value( "${soa.wbsdunningworkitem.url}" )
	String soaWbsDunningWorkitemUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false, WbsDunningWorkitemSoap.class, soaWbsDunningWorkitemUrl );
		factory.setBindingId( SOAPBinding.SOAP11HTTP_BINDING );
		return (WbsDunningWorkitemSoap) factory.create();
	}

	@Bean( name = { "manageDemandeConsumer" } )
	@Lazy( true )
	public ManageDemande getManageDemandeClient( @Value( "${soa.webservice.dcrm.demande}" )
	String soaWebserviceDCRMDemandeUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false, ManageDemande.class, soaWebserviceDCRMDemandeUrl );
		factory.setBindingId( SOAPBinding.SOAP11HTTP_BINDING );
		return (ManageDemande) factory.create();
	}

	@Bean( name = { "manageDCRMInteractionConsumer" } )
	@Lazy( true )
	public com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteraction getManageDCRMInteractionClient( @Autowired
	SoapEnabler soapEnabler, @Value( "${soa.webservice.dcrm.interaction}" )
	String soaWebserviceDCRMInteractionUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false,
												com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteraction.class,
												soaWebserviceDCRMInteractionUrl );
		factory.setBindingId( SOAPBinding.SOAP11HTTP_BINDING );
		return (com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteraction) factory.create();
	}

	@Bean( name = { "manageWbsDunningRegisterCardConsumer" } )
	@Lazy( true )
	public WbsDunningRegistercardSoap getManageWbsDunningRegisterCardClient( @Value( "${soa.webservice.dunning.registercard}" )
	String soaWebserviceDunningRegisterCardUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false, WbsDunningRegistercardSoap.class,
												soaWebserviceDunningRegisterCardUrl );
		factory.setBindingId( SOAPBinding.SOAP11HTTP_BINDING );
		return (WbsDunningRegistercardSoap) factory.create();
	}

	@Bean( name = { "manageThemeConsumer" } )
	@Lazy( true )
	public ManageTheme getManageThemeClient( @Value( "${soa.webservice.dcrm.theme}" )
	String soaWebserviceDCRMThemeUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false, ManageTheme.class, soaWebserviceDCRMThemeUrl );
		factory.setBindingId( SOAPBinding.SOAP11HTTP_BINDING );
		return (ManageTheme) factory.create();
	}

	@Bean( name = { "managetroubleTicketConsumer" } )
	@Lazy( true )
	public Noname getManageTroubleTicketClient( @Value( "${soa.webservice.oceane}" )
	String soaWebserviceOceaneUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false, Noname.class, soaWebserviceOceaneUrl );
		factory.setBindingId( SOAPBinding.SOAP11HTTP_BINDING );
		return (Noname) factory.create();
	}

	@Bean( name = { "managePartyConsumer" } )
	@Lazy( true )
	public ManageParty getManagePartyClient( @Value( "${soa.webservice.dcrm.party}" )
	String soaWebserviceDCRMPartyUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false, ManageParty.class, soaWebserviceDCRMPartyUrl );
		factory.setBindingId( SOAPBinding.SOAP11HTTP_BINDING );
		return (ManageParty) factory.create();
	}

	@Bean( name = { "manageCampagneConsumer" } )
	@Lazy( true )
	public ManageCampagne getManageCampagneClient( @Value( "${soa.webservice.dcrm.campagne}" )
	String soaWebserviceDCRMCampagneUrl )
	{
		JaxWsProxyFactoryBean factory =
			getConfiguredJaxWsProxyFactoryBean( true, false, ManageCampagne.class, soaWebserviceDCRMCampagneUrl );
		factory.setBindingId( SOAPBinding.SOAP11HTTP_BINDING );
		return (ManageCampagne) factory.create();
	}

}
