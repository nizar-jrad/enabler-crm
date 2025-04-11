package com.orangecaraibe.enabler.crm.soa.consumer.integrationtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;

import com.orangecaraibe.enabler.AbstractBaseConsumerTestCase;
import com.orangecaraibe.enabler.InfoClient;
import com.orangecaraibe.enabler.UseCaseTestFindAndGetDAO;
import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.crm.business.enums.ChannelEnum;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemCreateException;
import com.orangecaraibe.enabler.crm.webservice.criteria.CriteriaConstants;
import com.orangecaraibe.enabler.crm.webservice.criteria.WebservicesConstantes;
import com.orangecaraibe.soa.v2.criteria.CriteriaComposer;
import com.orangecaraibe.soa.v2.criteria.Restrictions;
import com.orangecaraibe.soa.v2.criteria.Variations;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.CreateException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.FindAndGetReasonSpecException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.ManageCustomerProblem;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.UpdateException;
import com.orangecaraibe.soa.v2.model.commonbusiness.agreement.FrameworkAgreement;
import com.orangecaraibe.soa.v2.model.commonbusiness.identity.NationalIdType;
import com.orangecaraibe.soa.v2.model.commonbusiness.identity.PartyIdentification;
import com.orangecaraibe.soa.v2.model.commonbusiness.identity.UserIdentity;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.LocalComment;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.LocalLegalStatusCodeEnum;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Organisation;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.PartyExternalIdentification;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Person;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.PersonName;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Salutation;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.EmailAddress;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.paymentmeans.BankAccount;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.paymentmeans.DirectDebit;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.CustomerInterlocutor;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.CustomerServiceRepresentative;
import com.orangecaraibe.soa.v2.model.commons.UseCase;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.commons.criteria.Restriction;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccount;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.CustomerAccount;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.payment.LocalPaymentFacility;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblem;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblemStatus;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblemStatusCode;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.LocalReasonSpecification;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.Reason;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Sms;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.SaleChannel;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledAtomicOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPublicKey;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionValueSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledFunctionValue;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledProduct;

public class CustomerProblemServiceTestCase
	extends AbstractBaseConsumerTestCase
{
	@Resource
	private ManageCustomerProblem manageCustomerProblemConsumer;

	@Resource
	private UseCaseTestFindAndGetDAO useCaseTestFindAndGetDAO;

	@Test
	public void createCustomerProblemWebcare()
		throws CustomerProblemCreateException, CreateException, InterfaceUnavailableException
	{

		CustomerProblem customerProblem = generateCustomerProblemWebCare();

		customerProblem = manageCustomerProblemConsumer.create( customerProblem );

		assertNotNull( customerProblem );
		assertNotNull( customerProblem.getTroubleTicketID() );
		assertNotNull( customerProblem.getCustomerProblemStatus() );
		assertTrue( !customerProblem.getCustomerProblemStatus().isEmpty() );
		List<CustomerProblemStatus> customerProblemStatus = customerProblem.getCustomerProblemStatus();
		assertTrue( customerProblemStatus.size() == 1 );

	}

	@Test
	public void createCustomerProblemMobiliteBanquaire()
		throws CustomerProblemCreateException, CreateException, InterfaceUnavailableException
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		CustomerProblem customerProblem = new CustomerProblem();

		customerProblem.setLocalTitle( "Titre de la demande, ex : Acte commercial Homologation Pré-identification" );
		customerProblem.setDescription( "Description détaillée de la demande : Pré-identification du contrat 12345678" );

		CustomerProblemStatus status = new CustomerProblemStatus();
		status.setStatusCode( CustomerProblemStatusCode.CLOSED );

		// customerProblem.setDescription( "Exemple de demande DCRM - paiement Webcare" );

		// LocalReasonSpecification reasonSpec = new LocalReasonSpecification();
		// reasonSpec.setReasonSpecificationCode( "1000" );
		//
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
		// assertNotNull( customerProblem.getCustomerProblemStatus() );
		// assertTrue( !customerProblem.getCustomerProblemStatus().isEmpty() );
		// List<CustomerProblemStatus> customerProblemStatus = customerProblem.getCustomerProblemStatus();
		// assertTrue( customerProblemStatus.size() == 1 );
		// assertNotNull( customerProblemStatus.get( 0 ).getStatusCode() );
		// assertTrue( customerProblemStatus.get( 0 ).getStatusCode().equals( CustomerProblemStatusCode.OPENED ) );
	}

	@Test
	public void createCustomerProblemVue360()
		throws CustomerProblemCreateException, CreateException, InterfaceUnavailableException
	{

		CustomerProblem customerProblem = generateCustomerProblemVue360();

		customerProblem = manageCustomerProblemConsumer.create( customerProblem );

		assertNotNull( customerProblem );
		assertNotNull( customerProblem.getTroubleTicketID() );
		assertNotNull( customerProblem.getCustomerProblemStatus() );
		assertTrue( !customerProblem.getCustomerProblemStatus().isEmpty() );
		List<CustomerProblemStatus> customerProblemStatus = customerProblem.getCustomerProblemStatus();
		assertTrue( customerProblemStatus.size() == 1 );

	}

	@Test
	public void testFindAndGetOfferSpec()
		throws FindAndGetReasonSpecException, InterfaceUnavailableException
	{

		Restriction restriction = Restrictions.eq( CriteriaConstants.saleChannel, ChannelEnum.WEBCARE.name() );
		Criteria criteria = CriteriaComposer.getInstance().add( restriction ).getCriteria();

		List<LocalReasonSpecification> listCrmReasonSpec =
			manageCustomerProblemConsumer.findAndGetReasonSpec( criteria );

		assertNotNull( listCrmReasonSpec );

		for ( LocalReasonSpecification localReasonSpec : listCrmReasonSpec )
		{
			assertNotNull( localReasonSpec.getReasonSpecificationCode() );
		}
	}

	private CustomerProblem generateCustomerProblemVue360()
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		CustomerProblem customerProblem = new CustomerProblem();

		CustomerProblemStatus status = new CustomerProblemStatus();
		status.setStatusCode( CustomerProblemStatusCode.INITIALISED );

		InstalledPublicKey publicKey = new InstalledPublicKey();
		publicKey.setInstalledPublicKeyID( infoClient.getMsisdn() );

		Reason reason = new Reason();
		reason.setMainReason( "Gestion client" );
		reason.setReason( "Adresse postale" );
		reason.setSubReason( "Modification" );

		customerProblem.setLocalTitle( "Gestion client,Adresse postale,Modification" );

		customerProblem.getCustomerProblemStatus().add( status );
		customerProblem.getInstalledPublicKey().add( publicKey );
		customerProblem.setReason( reason );

		Sms media = new Sms();
		customerProblem.setMedia( media );

		Person person = new Person();
		PartyExternalIdentification partyExternalIdentification = new PartyExternalIdentification();
		partyExternalIdentification.setExternalOwnerPartyID( "CRM" );
		partyExternalIdentification.setPartyExternalID( infoClient.getIdClientPublic() );
		person.getPartyExternalIdentification().add( partyExternalIdentification );
		customerProblem.getInteractionSubject().add( person );

		InstalledContract contract = new InstalledContract();
		contract.setInstalledOfferID( "COPL_OffresAuCompteur_OBS:" + infoClient.getCo_id() );
		contract.getInstalledPublicKey().add( publicKey );
		customerProblem.getInteractionSubject().add( contract );

		BillingAccount billingaccount = new BillingAccount();
		billingaccount.setBillingAccountID( infoClient.getCustomer_id() );
		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setCustomerAccountID( infoClient.getCustcode() );
		billingaccount.setCustomerAccount( customerAccount );
		customerProblem.getInteractionSubject().add( customerAccount );

		CustomerInterlocutor customerInterlocuteur = new CustomerInterlocutor();
		EmailAddress email = new EmailAddress();
		email.setEMailAddress( infoClient.getEmail() );
		customerInterlocuteur.setParty( person );
		customerInterlocuteur.setMayBeContactedUsing( email );

		SaleChannel webcare = new SaleChannel();
		webcare.setLabel( "CRM" );
		customerProblem.getLocalCommercialClassifications().add( webcare );

		// CustomerInterlocutor customerInterlocutor = new CustomerInterlocutor();
		// customerInterlocutor.setPartyRoleID( "CUSTOMER_INTERLOCUTOR:" + infoClient.getMsisdn() );
		//
		// PhoneNumber phone = new PhoneNumber();
		// phone.setNumber( infoClient.getMsisdn() );
		// customerInterlocutor.setMayBeContactedUsing( phone );
		// customerProblem.getCustomerInterlocutor().add( customerInterlocutor );

		CustomerServiceRepresentative customerServiceRepresentative = new CustomerServiceRepresentative();
		Person personservice = new Person();
		UserIdentity user = new UserIdentity();
		user.setGID( "lderely" );
		PartyIdentification partyIdentification = new PartyIdentification();
		partyIdentification.setNationalIDType( NationalIdType.NATIONALORGANISATIONIDENTIFIER );
		partyIdentification.setNationalID( "HCHP4248" );
		personservice.getPartyIdentification().add( partyIdentification );
		customerServiceRepresentative.setParty( personservice );
		customerServiceRepresentative.setUserIdentity( user );
		customerProblem.getCustomerServiceRepresentative().add( customerServiceRepresentative );
		return customerProblem;
	}

	@Test
	public void testFindAndGetOfferSpecCriteriaEmpty()
		throws FindAndGetReasonSpecException, InterfaceUnavailableException
	{

		Restriction restriction = Restrictions.eq( CriteriaConstants.saleChannel, "test" );
		Criteria criteria = CriteriaComposer.getInstance().add( restriction ).getCriteria();

		List<LocalReasonSpecification> listCrmReasonSpec =
			manageCustomerProblemConsumer.findAndGetReasonSpec( criteria );
		assertTrue( listCrmReasonSpec.isEmpty() );

	}

	@Test
	public void testFindAndGetOfferSpecCriteriaInvalid()
		throws FindAndGetReasonSpecException, InterfaceUnavailableException
	{

		Restriction restriction = Restrictions.eq( "SalCanal", "incorrect" );
		Criteria criteria = CriteriaComposer.getInstance().add( restriction ).getCriteria();
		try
		{
			manageCustomerProblemConsumer.findAndGetReasonSpec( criteria );
			fail( "Criteria valide " );
		}
		catch ( Exception e )
		{
			if ( e instanceof ConstraintViolationException )
			{
				fail( "Erreur insertion JDBC enabler-framework-log" );
			}
			else
			{
				assertTrue( e instanceof FindAndGetReasonSpecException );
				return;
			}

		}

	}

	@Test
	public void testUpdateNotifyDunning()
		throws InterfaceUnavailableException, UpdateException
	{
		CustomerProblem customerProblem = generateCustomerDunningLiquisation();
		customerProblem.setTroubleTicketID( "casts-14256-dsqdqsd-dsqds" );
		List<UseCase> useCases = new ArrayList<UseCase>();
		UseCase useCase = new UseCase();
		useCase.setUseCaseID( "UC_NOTIFY_DUNNING" );
		useCases.add( useCase );

		customerProblem = manageCustomerProblemConsumer.update( customerProblem, useCases );

		assertTrue( true );

	}

	private CustomerProblem generateCustomerDunningLiquisation()
	{
		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		CustomerProblem customerProblem = new CustomerProblem();

		CustomerProblemStatus status = new CustomerProblemStatus();
		status.setStatusCode( CustomerProblemStatusCode.INITIALISED );

		InstalledPublicKey publicKey = new InstalledPublicKey();
		publicKey.setInstalledPublicKeyID( infoClient.getMsisdn() );
		customerProblem.getInstalledPublicKey().add( publicKey );

		Reason reason = new Reason();
		reason.setLocalReasonObject( "Gestion client" );
		reason.setMainReason( "Gestion client" );
		reason.setReason( "Adresse postale" );
		reason.setSubReason( "Modification" );

		customerProblem.getCustomerProblemStatus().add( status );

		customerProblem.setReason( reason );

		Organisation organisation = new Organisation();
		organisation.setLocalLegalStatusRepresentative( "test" );
		organisation.setLegalStatusCode( LocalLegalStatusCodeEnum.LIQUIDATION );

		organisation.setLocalJudgementDate( DateUtils.convertDateToXMLGregorianCalendar( new Date() ) );
		customerProblem.getInteractionSubject().add( organisation );

		InstalledContract contract = new InstalledContract();
		contract.setInstalledOfferID( "offer:" + infoClient.getCo_id() );
		contract.getInstalledPublicKey().add( publicKey );
		customerProblem.getInteractionSubject().add( contract );

		BillingAccount billingaccount = new BillingAccount();
		billingaccount.setBillingAccountID( infoClient.getCustomer_id() );
		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setCustomerAccountID( infoClient.getCustcode() );
		billingaccount.setCustomerAccount( customerAccount );
		customerProblem.getInteractionSubject().add( customerAccount );

		SaleChannel webcare = new SaleChannel();
		webcare.setLabel( ChannelEnum.CRM.name() );
		customerProblem.getLocalCommercialClassifications().add( webcare );
		CustomerInterlocutor customerInterlocutor = new CustomerInterlocutor();
		customerInterlocutor.setPartyRoleID( "CUSTOMER_INTERLOCUTOR:" + infoClient.getEmail() );
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEMailAddress( infoClient.getEmail() );
		customerInterlocutor.setMayBeContactedUsing( emailAddress );

		customerProblem.getCustomerInterlocutor().add( customerInterlocutor );

		return customerProblem;

	}

	@Test
	public void testUpdateNotifyDunningRegisterCard()
		throws InterfaceUnavailableException, UpdateException
	{
		CustomerProblem customerProblem = generateCustomerProblemWebCare();
		customerProblem.setTroubleTicketID( "casts-14256-dsqdqsd-dsqds" );
		List<UseCase> useCases = new ArrayList<UseCase>();
		UseCase useCase = new UseCase();
		useCase.setUseCaseID( "UC_NOTIFY_DUNNING" );
		useCases.add( useCase );
		InstalledAtomicOffer atomic = new InstalledAtomicOffer();
		InstalledProduct installedProduct = new InstalledProduct();

		InstalledFunctionValue installedFonctionValue = new InstalledFunctionValue();

		FunctionSpecification functionSpecification = new FunctionSpecification();

		FunctionValueSpecification fonctionValueSpecificaiton = new FunctionValueSpecification();
		fonctionValueSpecificaiton.setFunctionValue( "VF1000000086" );
		functionSpecification.getFunctionValueSpecifications().add( fonctionValueSpecificaiton );
		installedFonctionValue.setFunctionSpecification( functionSpecification );
		installedProduct.getInstalledFunctionValues().add( installedFonctionValue );
		atomic.getInstalledProducts().add( installedProduct );

		customerProblem.getInteractionSubject().add( atomic );

		customerProblem = manageCustomerProblemConsumer.update( customerProblem, useCases );

		assertTrue( true );

	}

	public void setUseCaseTestFindAndGetDAO( UseCaseTestFindAndGetDAO useCaseTestFindAndGetDAO )
	{
		this.useCaseTestFindAndGetDAO = useCaseTestFindAndGetDAO;
	}

	private CustomerProblem generateCustomerProblemWebCare()
	{

		InfoClient infoClient = useCaseTestFindAndGetDAO.findAndGetCustomer();

		CustomerProblem customerProblem = new CustomerProblem();

		CustomerProblemStatus status = new CustomerProblemStatus();
		status.setStatusCode( CustomerProblemStatusCode.INITIALISED );

		InstalledPublicKey publicKey = new InstalledPublicKey();
		publicKey.setInstalledPublicKeyID( infoClient.getMsisdn() );
		customerProblem.getInstalledPublicKey().add( publicKey );

		Reason reason = new Reason();
		reason.setMainReason( "Réclamation" );
		reason.setReason( "sur contrat" );
		reason.setSubReason( "Autre" );

		customerProblem.getCustomerProblemStatus().add( status );

		customerProblem.setReason( reason );

		Person person = new Person();
		customerProblem.getInteractionSubject().add( person );

		InstalledContract contract = new InstalledContract();
		contract.setInstalledOfferID( "offer:" + infoClient.getCo_id() );
		contract.getInstalledPublicKey().add( publicKey );
		customerProblem.getInteractionSubject().add( contract );

		BillingAccount billingaccount = new BillingAccount();
		billingaccount.setBillingAccountID( infoClient.getCustomer_id() );
		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setCustomerAccountID( infoClient.getCustcode() );
		billingaccount.setCustomerAccount( customerAccount );
		customerProblem.getInteractionSubject().add( customerAccount );

		SaleChannel webcare = new SaleChannel();
		webcare.setLabel( ChannelEnum.WEBCARE.name() );
		customerProblem.getLocalCommercialClassifications().add( webcare );
		CustomerInterlocutor customerInterlocutor = new CustomerInterlocutor();
		customerInterlocutor.setPartyRoleID( "CUSTOMER_INTERLOCUTOR:" + infoClient.getEmail() );
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEMailAddress( infoClient.getEmail() );
		customerInterlocutor.setMayBeContactedUsing( emailAddress );

		customerProblem.getCustomerInterlocutor().add( customerInterlocutor );

		return customerProblem;
	}

	@Test
	public void testUseCaseUpdateAddNote()
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

		if ( CollectionUtils.isNotEmpty( customerProblems ) )
		{
			List<UseCase> useCases = new ArrayList<UseCase>();
			UseCase useCase = new UseCase();
			useCase.setUseCaseID( WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_ADD_NOTE );
			useCases.add( useCase );

			CustomerProblem customerProblem = customerProblems.get( 0 );

			LocalComment localComment = new LocalComment();
			localComment.setLabel( "Test Junit d'ajout de note" );
			localComment.setOperationType( "commentaire" );
			customerProblem.getLocalComment().add( localComment );
			try
			{

				manageCustomerProblemConsumer.update( customerProblem, useCases );
			}
			catch ( InterfaceUnavailableException e )
			{
				LOGGER.error( "InterfaceUnavailableException occurred while updating CustomerProlem", e );

			}
			catch ( UpdateException e )
			{
				LOGGER.error( "UpdateException occurred while updating CustomerProlem", e );
			}
		}

	}

	@Test
	public void testUseCaseUpdateCustomerProblem()
	{

		List<CustomerProblem> customerProblems = new ArrayList<CustomerProblem>();
		Criteria criteria = new Criteria();

		try
		{
			customerProblems = manageCustomerProblemConsumer.findAndGet( criteria );
		}
		catch ( FindAndGetReasonSpecException e1 )
		{
			e1.printStackTrace();
		}
		catch ( InterfaceUnavailableException e1 )
		{
			e1.printStackTrace();
		}
		catch ( FindAndGetException e1 )
		{
			e1.printStackTrace();
		}

		if ( CollectionUtils.isNotEmpty( customerProblems ) )
		{

			CustomerProblem customerProblem = customerProblems.get( 0 );
			// customerProblem.getCustomerProblemStatus().get( 0 ).setEndDate( );
			customerProblem.getCustomerProblemStatus().get( 0 ).setStatusCode( CustomerProblemStatusCode.CLOSED );

			List<UseCase> useCases = new ArrayList<UseCase>();
			UseCase useCase = new UseCase();
			useCase.setUseCaseID( WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_UPDATE_STATUS );
			useCases.add( useCase );

			try
			{
				manageCustomerProblemConsumer.update( customerProblem, useCases );
				assertTrue( "Pas d'erreur", true );

				customerProblem.getCustomerProblemStatus().get( 0 ).setStatusCode( CustomerProblemStatusCode.CLOSED );
				manageCustomerProblemConsumer.update( customerProblem, useCases );
			}
			catch ( InterfaceUnavailableException e )
			{
				LOGGER.error( "InterfaceUnavailableException occurred while updating CustomerProlem", e );
			}
			catch ( UpdateException e )
			{
				LOGGER.error( "UpdateException occurred while updating CustomerProlem", e );
			}
		}

	}

	@Test
	public void createCustomerProblemWtihPaimentFacility()
		throws CustomerProblemCreateException, CreateException, InterfaceUnavailableException
	{

		CustomerProblem customerProblem = generateCustomerProblemVue360();

		LocalPaymentFacility paiementFacility = new LocalPaymentFacility();
		float amount = 30;
		paiementFacility.setAmount( amount );
		paiementFacility.setExclusionDate( DateUtils.convertDateToXMLGregorianCalendar( new Date() ) );

		customerProblem.getInteractionSubject().add( paiementFacility );
		customerProblem = manageCustomerProblemConsumer.create( customerProblem );

		assertNotNull( customerProblem );
		assertNotNull( customerProblem.getTroubleTicketID() );
		assertNotNull( customerProblem.getCustomerProblemStatus() );
		assertTrue( !customerProblem.getCustomerProblemStatus().isEmpty() );
		List<CustomerProblemStatus> customerProblemStatus = customerProblem.getCustomerProblemStatus();
		assertTrue( customerProblemStatus.size() == 1 );

	}
}
