package com.orangecaraibe.enabler.crm.soa.consumer.integrationtests;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.activation.DataHandler;
import javax.annotation.Resource;

import org.junit.Test;

import com.orangecaraibe.enabler.AbstractBaseConsumerTestCase;
import com.orangecaraibe.enabler.InfoClient;
import com.orangecaraibe.enabler.UseCaseTestFindAndGetDAO;
import com.orangecaraibe.enabler.crm.business.enums.ChannelEnum;
import com.orangecaraibe.enabler.crm.webservice.criteria.CriteriaConstants;
import com.orangecaraibe.soa.v2.criteria.Restrictions;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.CreateException;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.ManageInteraction;
import com.orangecaraibe.soa.v2.model.commonbusiness.identity.UserIdentity;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionAttachmentImage;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionDetail;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionDetailType;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionWay;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.LocalCRMInteractionStatus;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.LocalCRMInteractionStatusCode;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Notification;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Person;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.PersonName;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Salutation;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.EmailAddress;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.PhoneNumber;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.PostalAddress;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.CustomerInterlocutor;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.Distributor;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.PartyRoleSet;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.customerdomain.customer.Holder;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccount;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblem;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblemStatus;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblemStatusCode;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.Reason;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalLetter;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalSelfcare;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Sms;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.SaleChannel;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPublicKey;

public class ManageInteractionServiceTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource( name = "manageInteractionCRMConsumer" )
	private ManageInteraction manageInteractionCRMConsumer;

	@Resource
	private UseCaseTestFindAndGetDAO useCaseTestFindAndGetDAO;

	@Test
	public void testcreateInteractionSmsPrompt()
		throws CreateException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		Interaction interaction = new Interaction();
		interaction.setDescription( "Exemple d'interaction sortante - Envoi SMS depuis Prompt - notification rejet de paiement" );
		Sms media = new Sms();
		interaction.setMedia( media );
		interaction.setInteractionWay( InteractionWay.OUT );

		// LocalCRMInteractionStatus crmInteractionStatus = new LocalCRMInteractionStatus();
		// crmInteractionStatus.setStatusCode( LocalCRMInteractionStatusCode.COMPLETED );
		// interaction.getLocalCRMInteractionStatus().add( crmInteractionStatus );

		Holder holder = new Holder();
		holder.setPartyRoleID( "HOLDER:" + infoClient.getCustomer_id() );
		Person person = new Person();
		PersonName personName = new PersonName();
		personName.setLastName( "MARTIN" );
		personName.setFirstName( "JEANNE" );
		personName.setSalutation( Salutation.MME );
		person.setPartyName( personName );
		holder.setParty( person );

		Distributor distributor = new Distributor();
		distributor.setChannelID( "PROMPT" );
		Person conseiller = new Person();
		conseiller.setPartyID( "FF0347F4-D628-E611-80BA-00505682F3FF" );
		PersonName conseillerName = new PersonName();
		conseillerName.setLastName( "NOMCONSEILLER" );
		conseillerName.setFirstName( "PRENOMCONSEILLER" );
		conseillerName.setSalutation( Salutation.M );
		conseiller.setPartyName( conseillerName );
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setGID( "pnomconseiller" );
		distributor.setUserIdentity( userIdentity );

		distributor.setParty( conseiller );
		PartyRoleSet equipe = new PartyRoleSet();
		equipe.setPartyRoleSetID( "idequipe" );
		equipe.setPartyRoleSetLabel( "nomequipe" );
		distributor.getPartyRoleSet().add( equipe );

		interaction.getFrom().add( distributor );
		interaction.getTo().add( holder );

		InteractionDetail detail = new InteractionDetail();
		detail.setInteractionDetailType( InteractionDetailType.TEXT );
		detail.setInteractionBody( "Orange Caraïbe,prélèvement de 44.00 E rejeté le 07/04/15.RDV sur https://espaceclient.orangecaraibe.com ou 555 avant suspension du service le 09/04/15.Merci" );
		interaction.getDetails().add( detail );

		PhoneNumber phoneNumberTo = new PhoneNumber();
		phoneNumberTo.setNumber( infoClient.getMsisdn() );
		interaction.setToContactMethod( phoneNumberTo );

		PhoneNumber phoneNumberFrom = new PhoneNumber();
		phoneNumberFrom.setNumber( "555" );
		interaction.setFromContactMethod( phoneNumberFrom );

		interaction = manageInteractionCRMConsumer.create( interaction );
		assertNotNull( interaction );
		assertNotNull( interaction.getInteractionID() );

	}

	@Test
	public void testcreateInteractionCourierPrompt()
		throws CreateException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		Interaction interaction = new Interaction();
		interaction.setDescription( "Exemple d'interaction sortante - Envoi courier depuis Prompt - notification " );
		LocalLetter media = new LocalLetter();
		interaction.setMedia( media );
		interaction.setInteractionWay( InteractionWay.OUT );

		LocalCRMInteractionStatus crmInteractionStatus = new LocalCRMInteractionStatus();
		crmInteractionStatus.setStatusCode( LocalCRMInteractionStatusCode.COMPLETED );
		crmInteractionStatus.setLocalStatusReason( "Emis" );
		interaction.getLocalCRMInteractionStatus().add( crmInteractionStatus );

		Holder holder = new Holder();
		holder.setPartyRoleID( "HOLDER:" + infoClient.getCustomer_id() );
		Person person = new Person();
		PersonName personName = new PersonName();
		personName.setLastName( "MARTIN" );
		personName.setFirstName( "JEANNE" );
		personName.setSalutation( Salutation.MME );
		person.setPartyName( personName );
		holder.setParty( person );

		Distributor distributor = new Distributor();
		distributor.setChannelID( "PROMPT" );
		Person conseiller = new Person();
		conseiller.setPartyID( "FF0347F4-D628-E611-80BA-00505682F3FF" );
		PersonName conseillerName = new PersonName();
		conseillerName.setLastName( "NOMCONSEILLER" );
		conseillerName.setFirstName( "PRENOMCONSEILLER" );
		conseillerName.setSalutation( Salutation.M );
		conseiller.setPartyName( conseillerName );
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setGID( "pnomconseiller" );
		distributor.setUserIdentity( userIdentity );

		distributor.setParty( conseiller );
		PartyRoleSet equipe = new PartyRoleSet();
		equipe.setPartyRoleSetID( "idequipe" );
		equipe.setPartyRoleSetLabel( "nomequipe" );
		distributor.getPartyRoleSet().add( equipe );

		interaction.getFrom().add( distributor );
		interaction.getTo().add( holder );

		InteractionDetail detail = new InteractionDetail();
		detail.setInteractionDetailType( InteractionDetailType.TEXT );
		detail.setInteractionBody( "Orange Caraïbe,prélèvement de 44.00 E rejeté le 07/04/15.RDV sur https://espaceclient.orangecaraibe.com ou 555 avant suspension du service le 09/04/15.Merci" );
		interaction.getDetails().add( detail );

		PhoneNumber phoneNumberTo = new PhoneNumber();
		phoneNumberTo.setNumber( infoClient.getCo_id() );
		interaction.setToContactMethod( phoneNumberTo );

		PhoneNumber phoneNumberFrom = new PhoneNumber();
		phoneNumberFrom.setNumber( "555" );
		interaction.setFromContactMethod( phoneNumberFrom );

		PostalAddress postalAdress = new PostalAddress();
		postalAdress.setPostalCode( "97100 " );
		postalAdress.setCityName( "Basse-Terre" );
		postalAdress.setLocalAddressDetails1( "MME MARTIN JEANNE" );
		postalAdress.setLocalAddressDetails2( "83 rue principal" );
		interaction.setToContactMethod( postalAdress );

		interaction = manageInteractionCRMConsumer.create( interaction );

	}

	public void setUseCaseTestFindAndGetDAO( UseCaseTestFindAndGetDAO useCaseTestFindAndGetDAO )
	{
		this.useCaseTestFindAndGetDAO = useCaseTestFindAndGetDAO;
	}

	@Test
	public void testcreateInteraction()
		throws CreateException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		Interaction interaction = new Interaction();
		interaction.setDescription( "Exemple d'interaction entrante - paiement Webcare" );
		LocalSelfcare media = new LocalSelfcare();
		interaction.setMedia( media );
		interaction.setInteractionWay( InteractionWay.IN );

		LocalCRMInteractionStatus crmInteractionStatus = new LocalCRMInteractionStatus();
		crmInteractionStatus.setStatusCode( LocalCRMInteractionStatusCode.COMPLETED );
		interaction.getLocalCRMInteractionStatus().add( crmInteractionStatus );

		Holder holder = new Holder();
		holder.setPartyRoleID( "HOLDER:" + infoClient.getCustomer_id() );
		Person person = new Person();
		PersonName personName = new PersonName();
		personName.setLastName( "MARTIN" );
		personName.setFirstName( "JEANNE" );
		personName.setSalutation( Salutation.MME );
		person.setPartyName( personName );
		holder.setParty( person );

		Distributor distributor = new Distributor();
		distributor.setChannelID( ChannelEnum.WEBCARE.name() );

		interaction.getFrom().add( holder );
		interaction.getTo().add( distributor );

		Notification notification = new Notification();
		interaction.setInteractionRole( notification );

		interaction = manageInteractionCRMConsumer.create( interaction );

	}

	@Test
	public void testcreateInteractionNotificationCourrierPrompt()
		throws CreateException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		Interaction interaction = new Interaction();
		interaction.setDescription( "Exemple d'interaction - Notification d'un envoi de courrier" );
		LocalLetter media = new LocalLetter();
		interaction.setMedia( media );
		interaction.setInteractionWay( InteractionWay.OUT );

		LocalCRMInteractionStatus crmInteractionStatus = new LocalCRMInteractionStatus();
		crmInteractionStatus.setStatusCode( LocalCRMInteractionStatusCode.COMPLETED );
		interaction.getLocalCRMInteractionStatus().add( crmInteractionStatus );

		Holder holder = new Holder();
		holder.setPartyRoleID( "HOLDER:" + infoClient.getCustomer_id() );
		Person person = new Person();
		PersonName personName = new PersonName();
		personName.setLastName( "MARTIN" );
		personName.setFirstName( "JEANNE" );
		personName.setSalutation( Salutation.MME );
		person.setPartyName( personName );
		holder.setParty( person );

		Distributor distributor = new Distributor();
		distributor.setChannelID( "PROMPT" );
		Person conseiller = new Person();
		conseiller.setPartyID( "FF0347F4-D628-E611-80BA-00505682F3FF" );
		PersonName conseillerName = new PersonName();
		conseillerName.setLastName( "NOMCONSEILLER" );
		conseillerName.setFirstName( "PRENOMCONSEILLER" );
		conseillerName.setSalutation( Salutation.M );
		conseiller.setPartyName( conseillerName );
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setGID( "pnomconseiller" );
		distributor.setUserIdentity( userIdentity );

		distributor.setParty( conseiller );
		PartyRoleSet equipe = new PartyRoleSet();
		equipe.setPartyRoleSetID( "idequipe" );
		equipe.setPartyRoleSetLabel( "nomequipe" );
		distributor.getPartyRoleSet().add( equipe );

		interaction.getFrom().add( distributor );
		interaction.getTo().add( holder );

		Notification notification = new Notification();
		interaction.setInteractionRole( notification );

		PostalAddress postal = new PostalAddress();
		postal.setContactMethodID( "123:PostalAddress" );
		postal.setLocalAddressDetails1( "Etage 1" );
		postal.setLocalAddressDetails2( "Entrée A" );
		postal.setLocalAddressDetails3( "Section Belette" );
		postal.setRoadNumber( "129" );
		postal.setRoadName( "New Road" );
		postal.setLocality( "LES ABYMES" );
		postal.setPostalCode( "97139" );
		postal.setCountryName( "GUADELOUPE" );
		interaction.setToContactMethod( postal );

		InteractionAttachmentImage attachement = new InteractionAttachmentImage();
		attachement.setImageName( "Courrier Prompt du " );
		String test = "";
		DataHandler data = new DataHandler( test, "test.fr" );
		attachement.setBinaryData( data );
		attachement.setImageMimeType( "" );

		interaction.getAttachments().add( attachement );

		interaction = manageInteractionCRMConsumer.create( interaction );

		assertNotNull( interaction );
		assertNotNull( interaction.getInteractionID() );

	}

	@Test
	public void findAndGetInteraction()
		throws FindAndGetException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime( new Date() );
		gc.add( Calendar.MONTH, -1 );

		Criteria criteria = new Criteria();

		criteria.getRestrictions().add( Restrictions.eq(	CriteriaConstants.BILLING_ACCOUNT_ID,
															infoClient.getCustomer_id() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.FROM_DATE, gc.getTime() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.MAX_RESULT, "5" ) );

		List<Interaction> interactions = manageInteractionCRMConsumer.findAndGet( criteria );

		assertNotNull( interactions );
	}

	@Test
	public void findAndGetCampagne()
		throws FindAndGetException, InterfaceUnavailableException
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime( new Date() );
		gc.add( Calendar.MONTH, -12 );

		List<Object> list = new ArrayList<Object>( Arrays.asList( "marketingCampaign" ) );

		Criteria criteria = new Criteria();

		criteria.getRestrictions().add( Restrictions.eq(	CriteriaConstants.PARTY_ID,
															"A12B7B31-C5A3-E711-80F6-0050569C055A" ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.FROM_DATE, gc.getTime() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.FROM_DATE, gc.getTime() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.MAX_RESULT, "5" ) );
		criteria.getRestrictions().add( Restrictions.in( CriteriaConstants.INTERACTION_TYPE, list ) );

		List<Interaction> interactions = manageInteractionCRMConsumer.findAndGet( criteria );

		assertNotNull( interactions );
	}

	@Test
	public void testcreateInteractionDemande()
		throws CreateException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		Interaction interaction = new Interaction();
		interaction.setDescription( "Exemple d'interaction sortante - Envoi courier depuis Prompt - notification " );
		LocalLetter media = new LocalLetter();
		interaction.setMedia( media );
		interaction.setInteractionWay( InteractionWay.OUT );

		LocalCRMInteractionStatus crmInteractionStatus = new LocalCRMInteractionStatus();
		crmInteractionStatus.setStatusCode( LocalCRMInteractionStatusCode.COMPLETED );
		crmInteractionStatus.setLocalStatusReason( "Emis" );
		interaction.getLocalCRMInteractionStatus().add( crmInteractionStatus );

		Holder holder = new Holder();
		holder.setPartyRoleID( "HOLDER:" + infoClient.getCustomer_id() );
		Person person = new Person();
		PersonName personName = new PersonName();
		personName.setLastName( "MARTIN" );
		personName.setFirstName( "JEANNE" );
		personName.setSalutation( Salutation.MME );
		person.setPartyName( personName );
		holder.setParty( person );

		Distributor distributor = new Distributor();
		distributor.setChannelID( "PROMPT" );
		Person conseiller = new Person();
		conseiller.setPartyID( "FF0347F4-D628-E611-80BA-00505682F3FF" );
		PersonName conseillerName = new PersonName();
		conseillerName.setLastName( "NOMCONSEILLER" );
		conseillerName.setFirstName( "PRENOMCONSEILLER" );
		conseillerName.setSalutation( Salutation.M );
		conseiller.setPartyName( conseillerName );
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setGID( "pnomconseiller" );
		distributor.setUserIdentity( userIdentity );

		distributor.setParty( conseiller );
		PartyRoleSet equipe = new PartyRoleSet();
		equipe.setPartyRoleSetID( "idequipe" );
		equipe.setPartyRoleSetLabel( "nomequipe" );
		distributor.getPartyRoleSet().add( equipe );

		interaction.getFrom().add( distributor );
		interaction.getTo().add( holder );

		InteractionDetail detail = new InteractionDetail();
		detail.setInteractionDetailType( InteractionDetailType.TEXT );
		detail.setInteractionBody( "Orange Caraïbe,prélèvement de 44.00 E rejeté le 07/04/15.RDV sur https://espaceclient.orangecaraibe.com ou 555 avant suspension du service le 09/04/15.Merci" );
		interaction.getDetails().add( detail );

		PhoneNumber phoneNumberTo = new PhoneNumber();
		phoneNumberTo.setNumber( infoClient.getMsisdn() );
		interaction.setToContactMethod( phoneNumberTo );

		PhoneNumber phoneNumberFrom = new PhoneNumber();
		phoneNumberFrom.setNumber( "555" );
		interaction.setFromContactMethod( phoneNumberFrom );

		PostalAddress postalAdress = new PostalAddress();
		postalAdress.setPostalCode( "97100 " );
		postalAdress.setCityName( "Basse-Terre" );
		postalAdress.setLocalAddressDetails1( "MME MARTIN JEANNE" );
		postalAdress.setLocalAddressDetails2( "83 rue principal" );
		interaction.setToContactMethod( postalAdress );

		interaction.getLocalCustomerProblems().add( createCustomerProblem( infoClient ) );

		interaction = manageInteractionCRMConsumer.create( interaction );
		assertNotNull( interaction );
		assertNotNull( interaction.getInteractionID() );

	}

	private CustomerProblem createCustomerProblem( InfoClient infoClient )
	{
		CustomerProblem customerProblem = new CustomerProblem();

		CustomerProblemStatus status = new CustomerProblemStatus();
		status.setStatusCode( CustomerProblemStatusCode.INITIALISED );

		Reason reason = new Reason();
		reason.setMainReason( "Gestion client" );
		reason.setReason( "Adresse postale" );
		reason.setSubReason( "Modification" );

		customerProblem.getCustomerProblemStatus().add( status );

		customerProblem.setReason( reason );

		Person person = new Person();
		customerProblem.getInteractionSubject().add( person );

		InstalledPublicKey publicKey = new InstalledPublicKey();
		publicKey.setInstalledPublicKeyID( infoClient.getMsisdn() );

		InstalledContract contract = new InstalledContract();
		contract.setInstalledOfferID( "offer" + infoClient.getCo_id() );
		contract.getInstalledPublicKey().add( publicKey );
		customerProblem.getInteractionSubject().add( contract );

		SaleChannel webcare = new SaleChannel();
		webcare.setLabel( ChannelEnum.CRM.name() );
		customerProblem.getLocalCommercialClassifications().add( webcare );
		CustomerInterlocutor customerInterlocutor = new CustomerInterlocutor();
		customerInterlocutor.setPartyRoleID( "CUSTOMER_INTERLOCUTOR:" + infoClient.getEmail() );
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEMailAddress( infoClient.getEmail() );
		customerInterlocutor.setMayBeContactedUsing( emailAddress );

		BillingAccount billingAccount = new BillingAccount();
		billingAccount.setBillingAccountID( infoClient.getCustomer_id() );
		customerProblem.getInteractionSubject().add( billingAccount );

		return customerProblem;
	}

}
