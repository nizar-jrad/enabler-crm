package com.orangecaraibe.enabler.crm.soa.consumer.integrationtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.orangecaraibe.enabler.AbstractBaseConsumerTestCase;
import com.orangecaraibe.enabler.InfoClient;
import com.orangecaraibe.enabler.UseCaseTestFindAndGetDAO;
import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.crm.business.enums.ChannelEnum;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemCreateException;
import com.orangecaraibe.enabler.crm.webservice.criteria.CriteriaConstants;
import com.orangecaraibe.soa.v2.criteria.Restrictions;
import com.orangecaraibe.soa.v2.criteria.Variations;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.CreateException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.FindAndGetReasonSpecException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.ManageCustomerProblem;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.UpdateException;
import com.orangecaraibe.soa.v2.model.commonbusiness.agreement.FrameworkAgreement;
import com.orangecaraibe.soa.v2.model.commonbusiness.identity.UserIdentity;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.LocalCRMInteractionStatus;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.LocalCRMInteractionStatusCode;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Person;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.PersonName;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Salutation;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.paymentmeans.BankAccount;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.paymentmeans.DirectDebit;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.CustomerInterlocutor;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.CustomerServiceRepresentative;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.Distributor;
import com.orangecaraibe.soa.v2.model.commons.UseCase;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.customerdomain.customer.Holder;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccount;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.payment.LocalPaymentFacility;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.payment.PaymentFacilityTypeEnum;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblem;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblemStatus;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblemStatusCode;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.LocalReasonSpecification;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.Reason;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalShop;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Media;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.SaleChannel;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPublicKey;

public class ManageCustomerProblemCRMServiceTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource
	private ManageCustomerProblem manageCustomerProblemConsumer;

	@Resource
	private UseCaseTestFindAndGetDAO useCaseTestFindAndGetDAO;

	@Test
	public void createCustomerProblemCRM()
		throws CustomerProblemCreateException, CreateException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		CustomerProblem customerProblem = new CustomerProblem();

		customerProblem.setLocalTitle( "Titre de la demande, ex : Paiement CB Accepté" );
		customerProblem.setDescription( "Description détaillée de la demande : Paiement de la facture 12345678" );

		CustomerProblemStatus status = new CustomerProblemStatus();
		status.setStatusCode( CustomerProblemStatusCode.CLOSED );

		// customerProblem.setDescription( "Exemple de demande DCRM - paiement Webcare" );

		InstalledPublicKey publicKey = new InstalledPublicKey();
		publicKey.setInstalledPublicKeyID( infoClient.getMsisdn() );

		Reason reason = new Reason();
		reason.setMainReason( "Paiement" );
		reason.setReason( "CB" );
		reason.setSubReason( "Accepté" );

		customerProblem.getCustomerProblemStatus().add( status );
		customerProblem.getInstalledPublicKey().add( publicKey );
		customerProblem.setReason( reason );

		SaleChannel webcare = new SaleChannel();
		webcare.setLabel( ChannelEnum.WEBCARE.name() );

		customerProblem.getLocalCommercialClassifications().add( webcare );
		CustomerInterlocutor customerInterlocutor = new CustomerInterlocutor();
		// customerInterlocutor.setPartyRoleID( "CUSTOMER_INTERLOCUTOR:" + infoClient.getEmail() );

		customerInterlocutor.setPartyRoleID( "HOLDER:" + infoClient.getCustomer_id() );
		Person person = new Person();
		person.setPartyID( "123" );
		PersonName personName = new PersonName();
		personName.setLastName( "MARTIN" );
		personName.setFirstName( "JEANNE" );
		personName.setSalutation( Salutation.MME );
		person.setPartyName( personName );
		customerInterlocutor.setParty( person );

		// EmailAddress emailAddress = new EmailAddress();
		// emailAddress.setEMailAddress( infoClient.getEmail() );
		// customerInterlocutor.setMayBeContactedUsing( emailAddress );

		customerProblem.getCustomerInterlocutor().add( customerInterlocutor );

		LocalPaymentFacility facility = new LocalPaymentFacility();
		facility.setAmount( (float) 30 );
		Date date = new Date();
		facility.setPaymentDate( DateUtils.convertDateToXMLGregorianCalendar( date ) );
		facility.setType( PaymentFacilityTypeEnum.PAYMENT_EXTENSION );
		customerProblem.getInteractionSubject().add( facility );

		BillingAccount billingAccount = new BillingAccount();
		billingAccount.setBillingAccountID( infoClient.getCustomer_id() );
		billingAccount.setLocalBillingAccountCode( infoClient.getCustcode() );

		customerProblem.getInteractionSubject().add( billingAccount );

		CustomerServiceRepresentative customerServiceRepresentative = new CustomerServiceRepresentative();
		UserIdentity userIdentity = new UserIdentity();
		userIdentity.setGID( "123" );
		customerServiceRepresentative.setUserIdentity( userIdentity );
		customerServiceRepresentative.setParty( person );
		customerProblem.getCustomerServiceRepresentative().add( customerServiceRepresentative );

		customerProblem = manageCustomerProblemConsumer.create( customerProblem );

		assertNotNull( customerProblem );
		assertNotNull( customerProblem.getTroubleTicketID() );
		assertNotNull( customerProblem.getCustomerProblemStatus() );
		// assertTrue( !customerProblem.getCustomerProblemStatus().isEmpty() );
		// List<CustomerProblemStatus> customerProblemStatus = customerProblem.getCustomerProblemStatus();
		// assertTrue( customerProblemStatus.size() == 1 );
		// assertNotNull( customerProblemStatus.get( 0 ).getStatusCode() );
		// assertTrue( customerProblemStatus.get( 0 ).getStatusCode().equals( CustomerProblemStatusCode.OPENED ) );
	}

	@Test
	public void createCustomerProblemCRMWithInteraction()
		throws CustomerProblemCreateException, CreateException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		CustomerProblem customerProblem = new CustomerProblem();

		customerProblem.setLocalTitle( "Titre de la demande, ex : Paiement CB Accepté" );
		customerProblem.setDescription( "Description détaillée de la demande : Paiement de la facture 12345678" );

		CustomerProblemStatus status = new CustomerProblemStatus();
		status.setStatusCode( CustomerProblemStatusCode.CLOSED );

		// customerProblem.setDescription( "Exemple de demande DCRM - paiement Webcare" );

		InstalledPublicKey publicKey = new InstalledPublicKey();
		publicKey.setInstalledPublicKeyID( infoClient.getMsisdn() );

		Reason reason = new Reason();
		reason.setMainReason( "Paiement" );
		reason.setReason( "CB" );
		reason.setSubReason( "Accepté" );

		customerProblem.getCustomerProblemStatus().add( status );
		customerProblem.getInstalledPublicKey().add( publicKey );
		customerProblem.setReason( reason );

		SaleChannel webcare = new SaleChannel();
		webcare.setLabel( ChannelEnum.CLEMENTINE.name() );

		customerProblem.getLocalCommercialClassifications().add( webcare );
		CustomerInterlocutor customerInterlocutor = new CustomerInterlocutor();
		// customerInterlocutor.setPartyRoleID( "CUSTOMER_INTERLOCUTOR:" + infoClient.getEmail() );

		customerInterlocutor.setPartyRoleID( "HOLDER:" + infoClient.getCustomer_id() );
		Person person = new Person();
		PersonName personName = new PersonName();
		personName.setLastName( "MARTIN" );
		personName.setFirstName( "JEANNE" );
		personName.setSalutation( Salutation.MME );
		person.setPartyName( personName );
		customerInterlocutor.setParty( person );

		// EmailAddress emailAddress = new EmailAddress();
		// emailAddress.setEMailAddress( infoClient.getEmail() );
		// customerInterlocutor.setMayBeContactedUsing( emailAddress );

		customerProblem.getCustomerInterlocutor().add( customerInterlocutor );

		LocalPaymentFacility facility = new LocalPaymentFacility();
		facility.setAmount( (float) 30 );
		Date date = new Date();
		facility.setPaymentDate( DateUtils.convertDateToXMLGregorianCalendar( date ) );
		facility.setType( PaymentFacilityTypeEnum.PAYMENT_EXTENSION );
		customerProblem.getInteractionSubject().add( facility );

		BillingAccount billingAccount = new BillingAccount();
		billingAccount.setBillingAccountID( infoClient.getCustomer_id() );
		billingAccount.setLocalBillingAccountCode( infoClient.getCustcode() );

		customerProblem.getInteractionSubject().add( billingAccount );

		Interaction interaction = new Interaction();
		Media media = new LocalShop();
		interaction.setMedia( media );

		Holder holder = new Holder();
		holder.setPartyRoleID( "HOLDER:" + infoClient.getCustomer_id() );
		holder.setParty( person );
		interaction.getTo().add( holder );
		customerProblem.getInteraction().add( interaction );

		Distributor distributor = new Distributor();
		distributor.setChannelID( ChannelEnum.CLEMENTINE.name() );
		LocalCRMInteractionStatus crmInteractionStatus = new LocalCRMInteractionStatus();
		crmInteractionStatus.setStatusCode( LocalCRMInteractionStatusCode.COMPLETED );
		interaction.getLocalCRMInteractionStatus().add( crmInteractionStatus );
		interaction.getFrom().add( distributor );
		customerProblem = manageCustomerProblemConsumer.create( customerProblem );

		assertNotNull( customerProblem );
		assertNotNull( customerProblem.getTroubleTicketID() );
		assertNotNull( customerProblem.getCustomerProblemStatus() );
		// assertTrue( !customerProblem.getCustomerProblemStatus().isEmpty() );
		// List<CustomerProblemStatus> customerProblemStatus = customerProblem.getCustomerProblemStatus();
		// assertTrue( customerProblemStatus.size() == 1 );
		// assertNotNull( customerProblemStatus.get( 0 ).getStatusCode() );
		// assertTrue( customerProblemStatus.get( 0 ).getStatusCode().equals( CustomerProblemStatusCode.OPENED ) );
	}

	public void setUseCaseTestFindAndGetDAO( UseCaseTestFindAndGetDAO useCaseTestFindAndGetDAO )
	{
		this.useCaseTestFindAndGetDAO = useCaseTestFindAndGetDAO;
	}

	@Test
	public void createCustomerProblemCRMHomologation()
		throws CustomerProblemCreateException, CreateException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		CustomerProblem customerProblem = new CustomerProblem();

		customerProblem.setLocalTitle( "Titre de la demande, ex : Acte commercial Homologation Pré-identification" );
		customerProblem.setDescription( "Description détaillée de la demande : Pré-identification du contrat 12345678" );

		CustomerProblemStatus status = new CustomerProblemStatus();
		status.setStatusCode( CustomerProblemStatusCode.CLOSED );

		InstalledPublicKey publicKey = new InstalledPublicKey();
		publicKey.setInstalledPublicKeyID( infoClient.getMsisdn() );

		Reason reason = new Reason();
		reason.setMainReason( "Gestion client" );
		reason.setReason( "Coordonnées bancaires" );
		reason.setSubReason( "Mobilité bancaire" );

		customerProblem.getCustomerProblemStatus().add( status );
		customerProblem.getInstalledPublicKey().add( publicKey );
		customerProblem.setReason( reason );

		SaleChannel channel = new SaleChannel();
		channel.setLabel( "CRM360" );

		customerProblem.getLocalCommercialClassifications().add( channel );
		CustomerInterlocutor customerInterlocutor = new CustomerInterlocutor();
		// customerInterlocutor.setPartyRoleID( "CUSTOMER_INTERLOCUTOR:" + infoClient.getEmail() );

		customerInterlocutor.setPartyRoleID( "HOLDER:" + infoClient.getCustomer_id() );
		Person person = new Person();
		person.setPartyID( "123" );
		PersonName personName = new PersonName();
		personName.setLastName( "MARTIN" );
		personName.setFirstName( "JEANNE" );
		personName.setSalutation( Salutation.MME );
		person.setPartyName( personName );
		customerInterlocutor.setParty( person );

		// EmailAddress emailAddress = new EmailAddress();
		// emailAddress.setEMailAddress( infoClient.getEmail() );
		// customerInterlocutor.setMayBeContactedUsing( emailAddress );

		customerProblem.getCustomerInterlocutor().add( customerInterlocutor );

		BillingAccount billingAccount = new BillingAccount();
		billingAccount.setBillingAccountID( infoClient.getCustomer_id() );
		billingAccount.setLocalBillingAccountCode( infoClient.getCustcode() );

		DirectDebit paymentMean = new DirectDebit();
		BankAccount banckAccount = new BankAccount();
		banckAccount.setBIC( "AGRIFRPP882" );
		banckAccount.setIBAN( "FR7618206002105487266700217" );

		paymentMean.setBankAccount( banckAccount );
		billingAccount.getPaymentMeans().add( paymentMean );

		customerProblem.getInteractionSubject().add( billingAccount );

		FrameworkAgreement fa = new FrameworkAgreement();
		fa.setInteractionSubjectLabel( "FrameworkAgreement" );
		fa.setAgreementDocumentNumber( "123456789" );
		fa.setAgreementPeriod( "201703050830" );

		customerProblem.getInteractionSubject().add( fa );

		customerProblem = manageCustomerProblemConsumer.create( customerProblem );

		assertNotNull( customerProblem );
		assertNotNull( customerProblem.getTroubleTicketID() );
		assertNotNull( customerProblem.getCustomerProblemStatus() );
		assertTrue( !customerProblem.getCustomerProblemStatus().isEmpty() );
		// List<CustomerProblemStatus> customerProblemStatus = customerProblem.getCustomerProblemStatus();
		// assertTrue( customerProblemStatus.size() == 1 );
		// assertNotNull( customerProblemStatus.get( 0 ).getStatusCode() );
		// assertTrue( customerProblemStatus.get( 0 ).getStatusCode().equals( CustomerProblemStatusCode.OPENED ) );
	}

	@Test
	public void updateCustomerProblemCRM()
		throws CustomerProblemCreateException, CreateException, InterfaceUnavailableException, UpdateException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		List<UseCase> useCases = new ArrayList<UseCase>();
		UseCase useCase = new UseCase();
		useCase.setUseCaseID( "UC_UPD_STATUS" );
		useCases.add( useCase );

		CustomerProblem customerProblem = new CustomerProblem();

		customerProblem.setLocalTitle( "Titre de la demande, ex : Acte commercial Homologation Pré-identification" );
		customerProblem.setDescription( "Description détaillée de la demande : Pré-identification du contrat 12345678" );

		customerProblem.setTroubleTicketID( "GUID de la demande, ex :123-8547s-6d54-ds25" );
		CustomerProblemStatus status = new CustomerProblemStatus();
		status.setStatusCode( CustomerProblemStatusCode.CLOSED );
		status.setLocalStatusReason( "Validé" );

		InstalledPublicKey publicKey = new InstalledPublicKey();
		publicKey.setInstalledPublicKeyID( infoClient.getMsisdn() );

		// Reason reason = new Reason();
		// reason.setMainReason( "Acte commercial" );
		// reason.setReason( "Homologation" );
		// reason.setSubReason( "Pré-identification" );

		// LocalReasonSpecification localReasonSPecification = new LocalReasonSpecification();
		// localReasonSPecification.setReasonSpecificationCode( "idtheme1_idtheme2_idtheme3" );
		// reason.setLocalReasonSpecification( localReasonSPecification );

		customerProblem.getCustomerProblemStatus().add( status );
		customerProblem.getInstalledPublicKey().add( publicKey );
		// customerProblem.setReason( reason );

		SaleChannel channel = new SaleChannel();
		channel.setLabel( "PROMPT" );

		customerProblem.getLocalCommercialClassifications().add( channel );
		CustomerServiceRepresentative customerRepresentative = new CustomerServiceRepresentative();
		// customerInterlocutor.setPartyRoleID( "CUSTOMER_INTERLOCUTOR:" + infoClient.getEmail() );
		UserIdentity user = new UserIdentity();
		user.setGID( "identifiant user, ex : lderely" );
		customerRepresentative.setUserIdentity( user );
		Person person = new Person();

		customerRepresentative.setParty( person );

		customerProblem.getCustomerServiceRepresentative().add( customerRepresentative );

		BillingAccount billingAccount = new BillingAccount();
		billingAccount.setBillingAccountID( infoClient.getCustomer_id() );
		billingAccount.setLocalBillingAccountCode( infoClient.getCustcode() );

		customerProblem.getInteractionSubject().add( billingAccount );

		customerProblem = manageCustomerProblemConsumer.update( customerProblem, useCases );

		// assertNotNull( customerProblem );
		// assertNotNull( customerProblem.getTroubleTicketID() );
		// assertNotNull( customerProblem.getCustomerProblemStatus() );
		// assertTrue( !customerProblem.getCustomerProblemStatus().isEmpty() );
		// List<CustomerProblemStatus> customerProblemStatus = customerProblem.getCustomerProblemStatus();
		// assertTrue( customerProblemStatus.size() == 1 );
		// assertNotNull( customerProblemStatus.get( 0 ).getStatusCode() );
		// assertTrue( customerProblemStatus.get( 0 ).getStatusCode().equals( CustomerProblemStatusCode.OPENED ) );
	}

	@Test
	public void findAndGetCustomerProblemCRMByBillingAccount()
		throws FindAndGetReasonSpecException, InterfaceUnavailableException, FindAndGetException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();
		Criteria criteria = new Criteria();

		criteria.getRestrictions().add( Restrictions.eq(	CriteriaConstants.BILLING_ACCOUNT_ID,
															infoClient.getCustomer_id() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.FROM_DATE, new Date() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.MAX_RESULT, 5 ) );
		criteria.getVariations().add( Variations.include( CriteriaConstants.ATTACHMENTS ) );
		criteria.getVariations().add( Variations.include( CriteriaConstants.NOTES ) );

		List<CustomerProblem> customerProblems = manageCustomerProblemConsumer.findAndGet( criteria );

		assertNotNull( customerProblems );
	}

	@Test
	public void findAndGetCustomerProblemCRMByPartyIdWithAttachmentAndNote()
		throws FindAndGetReasonSpecException, InterfaceUnavailableException, FindAndGetException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();
		Criteria criteria = new Criteria();

		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.PARTY_ID, "123" ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.FROM_DATE, new Date() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.MAX_RESULT, 5 ) );
		criteria.getVariations().add( Variations.include( CriteriaConstants.ATTACHMENTS ) );
		criteria.getVariations().add( Variations.include( CriteriaConstants.NOTES ) );
		criteria.getRestrictions().add( Restrictions.in(	CriteriaConstants.MAIN_REASON,
															Arrays.asList( (Object) "Gestion client" ) ) );

		List<CustomerProblem> customerProblems = manageCustomerProblemConsumer.findAndGet( criteria );

		assertNotNull( customerProblems );
	}

	@Test
	public void findAndGetCustomerProblemCRMByCoId()
		throws FindAndGetReasonSpecException, InterfaceUnavailableException, FindAndGetException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();
		Criteria criteria = new Criteria();

		criteria.getRestrictions().add( Restrictions.eq(	CriteriaConstants.INSTALLED_CONTRACT_ID,
															infoClient.getCo_id() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.FROM_DATE, new Date() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.MAX_RESULT, 5 ) );

		List<CustomerProblem> customerProblems = manageCustomerProblemConsumer.findAndGet( criteria );

		assertNotNull( customerProblems );
	}

	@Test
	public void findAndGetCustomerProblemCRMByMSISDN()
		throws FindAndGetReasonSpecException, InterfaceUnavailableException, FindAndGetException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();
		Criteria criteria = new Criteria();
		GregorianCalendar date = new GregorianCalendar();
		date.setTime( new Date() );
		date.add( GregorianCalendar.MONTH, -6 );

		criteria.getRestrictions().add( Restrictions.eq(	CriteriaConstants.INSTALLED_PUBLIC_KEY_ID,
															infoClient.getMsisdn() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.FROM_DATE, date.getTime() ) );
		criteria.getRestrictions().add( Restrictions.eq( CriteriaConstants.MAX_RESULT, 5 ) );

		List<CustomerProblem> customerProblems = manageCustomerProblemConsumer.findAndGet( criteria );

		assertNotNull( customerProblems );
	}

	@Test
	public void createCustomerProblemFacilityCRM()
		throws CustomerProblemCreateException, CreateException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		CustomerProblem customerProblem = new CustomerProblem();

		customerProblem.setLocalTitle( "Titre de la demande, ex : Paiement CB Accepté" );
		customerProblem.setDescription( "Description détaillée de la demande : Paiement de la facture 12345678" );

		CustomerProblemStatus status = new CustomerProblemStatus();
		status.setStatusCode( CustomerProblemStatusCode.CLOSED );

		// customerProblem.setDescription( "Exemple de demande DCRM - paiement Webcare" );

		// LocalReasonSpecification reasonSpec = new LocalReasonSpecification();
		// reasonSpec.setReasonSpecificationCode( "1000" );
		//
		InstalledPublicKey publicKey = new InstalledPublicKey();
		publicKey.setInstalledPublicKeyID( infoClient.getMsisdn() );

		Reason reason = new Reason();
		reason.setMainReason( "Paiement" );
		reason.setReason( "CB" );
		reason.setSubReason( "Accepté" );

		LocalReasonSpecification localReasonSPecification = new LocalReasonSpecification();
		localReasonSPecification.setReasonSpecificationCode( "idtheme1_idtheme2_idtheme3" );
		reason.setLocalReasonSpecification( localReasonSPecification );

		customerProblem.getCustomerProblemStatus().add( status );
		customerProblem.getInstalledPublicKey().add( publicKey );
		customerProblem.setReason( reason );

		SaleChannel webcare = new SaleChannel();
		webcare.setLabel( ChannelEnum.WEBCARE.name() );

		customerProblem.getLocalCommercialClassifications().add( webcare );
		CustomerInterlocutor customerInterlocutor = new CustomerInterlocutor();
		// customerInterlocutor.setPartyRoleID( "CUSTOMER_INTERLOCUTOR:" + infoClient.getEmail() );

		customerInterlocutor.setPartyRoleID( "HOLDER:1234" );
		Person person = new Person();
		person.setPartyID( "123" );
		PersonName personName = new PersonName();
		personName.setLastName( "MARTIN" );
		personName.setFirstName( "JEANNE" );
		personName.setSalutation( Salutation.MME );
		person.setPartyName( personName );
		customerInterlocutor.setParty( person );

		// EmailAddress emailAddress = new EmailAddress();
		// emailAddress.setEMailAddress( infoClient.getEmail() );
		// customerInterlocutor.setMayBeContactedUsing( emailAddress );

		customerProblem.getCustomerInterlocutor().add( customerInterlocutor );

		Interaction interaction = new Interaction();
		interaction.setInteractionID( "123456" );
		customerProblem.getInteraction().add( interaction );

		BillingAccount billingAccount = new BillingAccount();
		billingAccount.setBillingAccountID( "1234" );
		billingAccount.setLocalBillingAccountCode( "1.1234" );

		LocalPaymentFacility facility = new LocalPaymentFacility();
		// facility.setAmount( 30 );
		// facility.setPaymentDate( value );

		customerProblem.getInteractionSubject().add( billingAccount );

		customerProblem = manageCustomerProblemConsumer.create( customerProblem );

		// assertNotNull( customerProblem );
		// assertNotNull( customerProblem.getTroubleTicketID() );
		// assertNotNull( customerProblem.getCustomerProblemStatus() );
		// assertTrue( !customerProblem.getCustomerProblemStatus().isEmpty() );
		// List<CustomerProblemStatus> customerProblemStatus = customerProblem.getCustomerProblemStatus();
		// assertTrue( customerProblemStatus.size() == 1 );
		// assertNotNull( customerProblemStatus.get( 0 ).getStatusCode() );
		// assertTrue( customerProblemStatus.get( 0 ).getStatusCode().equals( CustomerProblemStatusCode.OPENED ) );
	}
}
