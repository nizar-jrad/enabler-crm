/**
 *
 */
package com.orangecaraibe.enabler.epoint.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orangecaraibe.enabler.core.catalogue.CatalogueHelper;
import com.orangecaraibe.enabler.core.classification.builder.ClassificationBuilder;
import com.orangecaraibe.enabler.crm.webservice.impl.ManageWebcareAccountServiceImpl;
import com.orangecaraibe.enabler.epoint.business.service.CRMService;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.AddOrRemoveAccordInfoCommercialeException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CheckOutgoingRIOException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CreateOrderAddCatalogueServiceException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CreateOrderCreditINException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CreateOrderRemoveServiceException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.FindAndGetAccordInfoCommercialeException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.GetPresenceCompteWebcareException;
import com.orangecaraibe.enabler.externalws.services.ExternalWSService;
import com.orangecaraibe.enabler.externalws.utils.ExternalWSConstants;
import com.orangecaraibe.soa.v2.criteria.CriteriaComposer;
import com.orangecaraibe.soa.v2.criteria.Restrictions;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.FindAndGetUnknownBillingAccountException;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.ManageBillingAccount;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.UpdateException;
import com.orangecaraibe.soa.v2.interfaces.managebillingaccount.UpdateUnknownBillingAccountException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.CreateCustomerOrderException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.GetOutgoingRIOException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.GetPortabilityInfoException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.ManagePortability;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Party;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.ContactMethod;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.EmailAddress;
import com.orangecaraibe.soa.v2.model.commons.UseCase;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccount;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CustomerOrder;
import com.orangecaraibe.soa.v2.model.customerdomain.manageportability.Portability;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOffer;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.CriteriaFindAndGet;
import com.orangecaraibe.webcare.v1_0.interfaces.managewebcareaccount.schemas.WebcareCustomer;

/**
 * @author sgodard Implémentation de la couche business pour le WS ManageCRM
 */
@Deprecated
@Service( "crmService" )
public class CRMServiceImpl
	implements CRMService
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( CRMServiceImpl.class );

	private static final String NUMERO_KDO = "KDO";

	private static final String MARKETING_CAMPAIGN_CONTACT = "pattern";

	private static final String USE_CASE_CREATE_UPDATE_MARKETINGCAMPAIGN = "UC_Create_Update_MarketingCampaign";

	/** Web Service ManagePortability */
	@Autowired
	private ManagePortability managePortability;

	/** Web Service ManageWebcareAccount */
	@Autowired
	@Qualifier( "manageWebcareAccountService" )
	private ManageWebcareAccountServiceImpl manageWebcareAccount;

	/** Web Service ManageBillingAccount */
	@Autowired
	@Qualifier( "manageBillingAccountConsumer" )
	private ManageBillingAccount manageBillingAccount;

	/**	 */
	@Autowired
	@Qualifier( "crm.catalogueHelper" )
	private CatalogueHelper catalogueHelper;

	/**
	 * Service vers les WS externes
	 */
	@Autowired
	@Qualifier( "externalWSService" )
	ExternalWSService externalWSService;

	/**
	 *
	 */
	@Autowired
	private ClassificationBuilder classificationBuilder;

	@Override
	public String getOutgoingRIO( String msisdn, String coId, String typeClient )
		throws InterfaceUnavailableException, GetOutgoingRIOException
	{
		String rio = null;
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug(	"Begin Business getOutgoingRIO-- msisdn=" + msisdn + "",
							"coId=" + coId + ", type client=" + typeClient );
		}

		try
		{
			rio = managePortability.getOutgoingRIO( msisdn, coId, typeClient );

			if ( rio != null )
			{
				if ( LOGGER.isDebugEnabled() )
				{
					LOGGER.debug( "rio calcule: " + rio );
				}
				return rio;
			}
			else
			{
				GetOutgoingRIOException e =
					new GetOutgoingRIOException( "La récupération des informations de portabilité a échoué: rio calculated null" );
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "La récupération des informations de portabilité a échoué : rio calculated null", e );
				}
				throw e;
			}
		}
		catch ( RuntimeException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué", e );
			}
			throw new InterfaceUnavailableException( "La récupération des informations de portabilité a échoué" );
		}

	}

	@Override
	public boolean checkOutgoingRIO( String msisdn, String rio, String coId, String typeClient )
		throws InterfaceUnavailableException, CheckOutgoingRIOException

	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Begin Business checkOutgoingRIO-- msisdn=" + msisdn + ", rio=" + rio + ", co id=" + coId
				+ ", type client=" + typeClient );
		}
		Boolean response = false;
		String rioCalcule = null;
		Portability portabilityInfos = null;
		try
		{
			/**
			 * 25/05/2012 Ajout de la possibilité d'avoir un coId vide Dans ce cas, on fait appel au même WS que l'USSD
			 * pour la récupération du RIO
			 */
			if ( StringUtils.isEmpty( coId ) )
			{
				portabilityInfos = managePortability.getPortabilityInfo( msisdn );

				if ( portabilityInfos != null && portabilityInfos.getRio() != null )
				{
					rioCalcule = portabilityInfos.getRio();
				}
			}
			else
			{
				rioCalcule = managePortability.getOutgoingRIO( msisdn, coId, typeClient );
			}

			if ( rioCalcule != null )
			{
				if ( LOGGER.isDebugEnabled() )
				{
					LOGGER.debug( "rio calcule: " + rioCalcule );
				}
				if ( rio.equals( rioCalcule ) )
				{
					response = true;
				}
			}
			else
			{
				CheckOutgoingRIOException e =
					new CheckOutgoingRIOException( "La récupération des informations de portabilité a échoué: rio calculated null" );
				if ( LOGGER.isErrorEnabled() )
				{
					LOGGER.error( "La récupération des informations de portabilité a échoué : rio calculated null", e );
				}
				throw e;
			}
		}
		catch ( GetOutgoingRIOException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué", e );
			}
			throw new CheckOutgoingRIOException( "La récupération des informations de portabilité a échoué" );
		}
		catch ( GetPortabilityInfoException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué", e );
			}
			throw new CheckOutgoingRIOException( "La récupération des informations de portabilité a échoué" );
		}
		catch ( InterfaceUnavailableException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué", e );
			}
			throw e;
		}

		catch ( RuntimeException e )
		{
			if ( LOGGER.isErrorEnabled() )
			{
				LOGGER.error( "La récupération des informations de portabilité a échoué", e );
			}
			throw new InterfaceUnavailableException( "La récupération des informations de portabilité a échoué" );
		}

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "End Business checkOutgoingRIO--response=" + response );
		}
		return response;
	}

	@Override
	public void createOrderRemoveService(	String coId, String msisdn, String tmCode, String snCode, String spCode,
											String customerId )
		throws com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException,
		CreateOrderRemoveServiceException
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Business createOrderRemoveService() - Begin" );
		}

		try
		{
			CustomerOrder createdCustomerOrder =
				externalWSService.createOrderRemoveService( coId, msisdn, tmCode, snCode, spCode, customerId );

			if ( createdCustomerOrder == null || createdCustomerOrder.getCustomerOrderID() == null )
			{
				throw new CreateOrderRemoveServiceException( "Order non créé" );
			}
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "Nouvel Order créé: " + createdCustomerOrder.getCustomerOrderID() );
			}

		}
		catch ( CreateCustomerOrderException e )
		{
			throw new CreateOrderRemoveServiceException( e.getMessage(), e );
		}

		catch ( RuntimeException e )
		{
			throw new CreateOrderRemoveServiceException( e.getMessage(), e );
		}
		catch ( Exception e )
		{
			throw new com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException( e.getMessage(),
																													e );
		}
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Business createOrderRemoveService() - End" );
		}
	}

	@Override
	public void createOrderAddCatalogueService( String coId, String msisdn, String tmCode,
												String offerSpecificationCode, String customerId )
		throws CreateOrderAddCatalogueServiceException,
		com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Business createOrderAddCatalogueService() - Begin" );
		}

		try
		{
			CustomerOrder createdCustomerOrder =
				externalWSService.createOrderAddCatalogueService(	coId, msisdn, tmCode, offerSpecificationCode,
																	customerId );

			if ( createdCustomerOrder == null || createdCustomerOrder.getCustomerOrderID() == null )
			{
				throw new CreateOrderAddCatalogueServiceException( "Order non créé" );
			}
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "Nouvel Order créé: " + createdCustomerOrder.getCustomerOrderID() );
			}

		}
		catch ( CreateCustomerOrderException e )
		{
			throw new CreateOrderAddCatalogueServiceException( e.getMessage(), e );
		}

		catch ( RuntimeException e )
		{
			throw new CreateOrderAddCatalogueServiceException( e.getMessage(), e );
		}
		catch ( Exception e )
		{
			throw new com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException( e.getMessage(),
																													e );
		}
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Business createOrderAddCatalogueService() - End" );
		}

	}

	/**
	 * @param tmCode
	 * @param snCode
	 * @param spCode
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getClasification( String tmCode, String snCode, String spCode, String libeller )
	{
		String reponse = "";
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Business getClasification() - Begin | tmCode=" + tmCode + " | snCode:" + snCode
				+ " | spCode:" + spCode );
		}
		if ( libeller.equals( NUMERO_KDO ) )
		{
			reponse = classificationBuilder.getClasificationKDOs( tmCode, snCode, spCode );
		}
		else
		{
			reponse = classificationBuilder.getClassification( tmCode, snCode, spCode, libeller );
		}

		return reponse;
	}

	/**
	 * @param tmCode
	 * @param snCode
	 * @param spCode
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getClasifications( String tmCode, String snCode, String spCode )
	{
		String reponse = "";
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Business getClasification() - Begin | tmCode=" + tmCode + " | snCode:" + snCode
				+ " | spCode:" + spCode );
		}
		if ( snCode != null && snCode.length() > 0 )
		{
			reponse = classificationBuilder.getClasificationKDOs( tmCode, snCode, spCode );
		}
		else
		{

			reponse = classificationBuilder.getClassifications( tmCode, snCode, spCode );
		}
		return reponse;
	}

	@Override
	public void creditIN( String coId, String msisdn, String tmCode, String offerSpecificationCode, String customerId )
		throws CreateOrderCreditINException,
		com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Business creditIN() - Begin" );
		}

		try
		{
			CustomerOrder createdCustomerOrder =
				externalWSService.creditIN( coId, msisdn, tmCode, offerSpecificationCode, customerId );

			if ( createdCustomerOrder == null || createdCustomerOrder.getCustomerOrderID() == null )
			{
				throw new CreateOrderRemoveServiceException( "Order non créé" );
			}
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "Nouvel Order créé: " + createdCustomerOrder.getCustomerOrderID() );
			}

		}
		catch ( CreateCustomerOrderException e )
		{
			throw new CreateOrderCreditINException( e.getMessage(), e );
		}

		catch ( RuntimeException e )
		{
			throw new CreateOrderCreditINException( e.getMessage(), e );
		}
		catch ( Exception e )
		{
			throw new com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException( e.getMessage(),
																													e );
		}
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Business creditIN() - End" );
		}

	}

	@Override
	public String getOfferCodeSpecficationCode( String coId, String msisdn, String tmCode, String montant,
												String customerId )
		throws CreateOrderCreditINException,
		com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Business creditIN() - Begin" );
		}

		try
		{
			return catalogueHelper.getOfferSpecificationCode( coId, msisdn, montant, tmCode );
		}
		catch ( RuntimeException e )
		{
			throw new CreateOrderCreditINException( e.getMessage(), e );
		}
		catch ( Exception e )
		{
			throw new com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException( e.getMessage(),
																													e );
		}

	}

	/**
	 * Vérifier la présence d'un compte webcare
	 * 
	 * @param coId
	 * @param msisdn
	 * @return
	 * @throws GetPresenceCompteWebcareException
	 */
	@Override
	public boolean getPresenceCompteWebcare( String coId, String msisdn )
		throws GetPresenceCompteWebcareException
	{
		CriteriaFindAndGet criteriaFindAndGet = new CriteriaFindAndGet();
		// Récupération du billingAccountId à partir du coid
		Criteria criteria = CriteriaComposer.getInstance().getCriteria();

		BillingAccount billingAccount = null;
		String billingAccountId = null;
		if ( coId != null )
		{
			// Critère sur le coId
			criteria.getRestrictions().add( Restrictions.eq( ExternalWSConstants.INSTALLED_OFFER_ID, coId ) );
			try
			{
				List<InstalledOffer> offers = externalWSService.findAndGetSuperMCIB( criteria );
				for ( InstalledOffer offer : offers )
				{
					billingAccount = offer.getBillingAccount();
					if ( billingAccount != null )
					{
						billingAccountId = billingAccount.getBillingAccountID();
						if ( billingAccountId == null )
						{
							throw new GetPresenceCompteWebcareException( "Billing account id found is null for coid "
								+ coId );
						}
					}
					else
					{
						throw new GetPresenceCompteWebcareException( "No billing account found for coid " + coId );
					}
				}
			}
			catch ( Exception e )
			{
				throw new GetPresenceCompteWebcareException( e );
			}

		}
		else
		{
			throw new GetPresenceCompteWebcareException( "coid is null" );
		}

		criteriaFindAndGet.setBillingAccountId( billingAccountId );
		criteriaFindAndGet.setMsisdn( msisdn );

		try
		{
			WebcareCustomer webcareCustomer = manageWebcareAccount.findAndGet( criteriaFindAndGet );

			if ( webcareCustomer != null )
			{
				if ( ( webcareCustomer.getCreationDate() != null ) )
				{
					return true;
				}
			}
		}
		catch ( Exception e )
		{
			throw new GetPresenceCompteWebcareException( e );
		}

		return false;
	}

	/**
	 * Rechercher l'accord sur l'envoi d'informations commerciales
	 * 
	 * @param coId
	 * @param customerId
	 * @return
	 * @throws FindAndGetAccordInfoCommercialeException
	 */
	@Override
	public boolean findAndGetAccordInfoCommerciale( String coId, String customerId )
		throws FindAndGetAccordInfoCommercialeException
	{

		BillingAccount billingAccount = null;

		try
		{
			LOGGER.debug( "Appel au ManageBillingAccount findAndGet billingAccountId: " + customerId );
			Criteria criteria = CriteriaComposer.getInstance().getCriteria();

			if ( customerId != null )
			{
				criteria.getRestrictions().add( Restrictions.eq( "billingAccountId", customerId ) );
			}

			List<BillingAccount> billingAccountList = manageBillingAccount.findAndGet( criteria );

			if ( CollectionUtils.isNotEmpty( billingAccountList ) )
			{
				billingAccount = billingAccountList.get( 0 );
			}
			LOGGER.debug( "Appel au ManageBillingAccount findAndGet billingAccountId: " + customerId + " - done" );

		}
		catch ( com.orangecaraibe.soa.v2.interfaces.managebillingaccount.InterfaceUnavailableException e )
		{
			throw new FindAndGetAccordInfoCommercialeException( e );
		}
		catch ( FindAndGetException e )
		{
			throw new FindAndGetAccordInfoCommercialeException( e );
		}
		catch ( FindAndGetUnknownBillingAccountException e )
		{
			throw new FindAndGetAccordInfoCommercialeException( e );
		}

		if ( billingAccount != null && billingAccount.getParties() != null && !billingAccount.getParties().isEmpty() )
		{
			Party party = billingAccount.getParties().get( 0 );

			for ( ContactMethod contactMethod : party.getContactMethods() )
			{
				if ( contactMethod instanceof EmailAddress )
				{
					EmailAddress emailAddress = (EmailAddress) contactMethod;
					if ( emailAddress.getEMailAddress() != null && !emailAddress.getEMailAddress().equals( "" ) )
					{
						BillingAccount billingAccountMarketing = null;

						try
						{
							LOGGER.debug( "Appel au ManageBillingAccount findAndGet marketingCampaign : "
								+ emailAddress.getEMailAddress() );
							Criteria criteria = CriteriaComposer.getInstance().getCriteria();

							criteria.getRestrictions().add( Restrictions.eq(	MARKETING_CAMPAIGN_CONTACT,
																				emailAddress.getEMailAddress() ) );

							List<BillingAccount> billingAccountList = manageBillingAccount.findAndGet( criteria );

							if ( CollectionUtils.isNotEmpty( billingAccountList ) )
							{
								billingAccountMarketing = billingAccountList.get( 0 );
							}

							LOGGER.debug( "Appel au ManageBillingAccount findAndGet marketingCampaign : "
								+ emailAddress.getEMailAddress() + " - done" );

						}
						catch ( com.orangecaraibe.soa.v2.interfaces.managebillingaccount.InterfaceUnavailableException e )
						{
							throw new FindAndGetAccordInfoCommercialeException( e );
						}
						catch ( FindAndGetException e )
						{
							throw new FindAndGetAccordInfoCommercialeException( e );
						}
						catch ( FindAndGetUnknownBillingAccountException e )
						{
							throw new FindAndGetAccordInfoCommercialeException( e );
						}

						if ( billingAccountMarketing != null )
						{
							if ( CollectionUtils.isNotEmpty( billingAccountMarketing.getParties() ) )
							{
								Party partyMarketing = billingAccountMarketing.getParties().get( 0 );
								if ( partyMarketing.getLocalMarketingAgreement() != null )
									return partyMarketing.getLocalMarketingAgreement();
							}
						}
					}
					break;
				}
			}
		}
		return false;
	}

	/**
	 * Rechercher l'accord sur l'envoi d'informations commerciales
	 * 
	 * @param coId
	 * @param customerId
	 * @return
	 * @throws FindAndGetAccordInfoCommercialeException
	 */
	@Override
	public boolean addOrRemoveAccordInfoCommerciale( boolean accord, String coId, String customerId, String channel )
		throws AddOrRemoveAccordInfoCommercialeException
	{

		BillingAccount billingAccount = null;

		try
		{
			LOGGER.debug( "Appel au ManageBillingAccount findAndGet billingAccountId: " + customerId );
			Criteria criteria = CriteriaComposer.getInstance().getCriteria();

			if ( customerId != null )
			{
				criteria.getRestrictions().add( Restrictions.eq( "billingAccountId", customerId ) );
			}

			List<BillingAccount> billingAccountList = manageBillingAccount.findAndGet( criteria );

			if ( billingAccountList != null && billingAccountList.size() > 0 )
			{
				billingAccount = billingAccountList.get( 0 );
			}
			LOGGER.debug( "Appel au ManageBillingAccount findAndGet billingAccountId: " + customerId + " - done" );

		}
		catch ( com.orangecaraibe.soa.v2.interfaces.managebillingaccount.InterfaceUnavailableException e )
		{
			throw new AddOrRemoveAccordInfoCommercialeException( e );
		}
		catch ( FindAndGetException e )
		{
			throw new AddOrRemoveAccordInfoCommercialeException( e );
		}
		catch ( FindAndGetUnknownBillingAccountException e )
		{
			throw new AddOrRemoveAccordInfoCommercialeException( e );
		}

		if ( billingAccount != null && billingAccount.getParties() != null && !billingAccount.getParties().isEmpty() )
		{
			Party party = billingAccount.getParties().get( 0 );

			for ( ContactMethod contactMethod : party.getContactMethods() )
			{
				if ( contactMethod instanceof EmailAddress )
				{
					EmailAddress emailAddress = (EmailAddress) contactMethod;
					if ( emailAddress.getEMailAddress() != null && !emailAddress.getEMailAddress().equals( "" ) )
					{
						party.getContactMethods().clear();
						party.getContactMethods().add( emailAddress );
						try
						{

							LOGGER.debug( "Appel au ManageBillingAccount update marketingCampaign : " + accord );

							party.setLocalMarketingAgreement( accord );

							List<UseCase> useCaseList = new ArrayList<UseCase>();
							UseCase useCase = new UseCase();
							useCase.setUseCaseID( USE_CASE_CREATE_UPDATE_MARKETINGCAMPAIGN );
							useCaseList.add( useCase );

							// Canal de mise a jour
							billingAccount.setType( channel );

							LOGGER.info( "Appel ws manageBillingAccount.update" );
							billingAccount = manageBillingAccount.update( billingAccount, useCaseList );

							LOGGER.debug( "Appel au ManageBillingAccount update marketingCampaign : " + accord
								+ " - done" );

						}
						catch ( com.orangecaraibe.soa.v2.interfaces.managebillingaccount.InterfaceUnavailableException e )
						{
							throw new AddOrRemoveAccordInfoCommercialeException( e );
						}

						catch ( UpdateUnknownBillingAccountException e )
						{
							throw new AddOrRemoveAccordInfoCommercialeException( e );
						}
						catch ( UpdateException e )
						{
							throw new AddOrRemoveAccordInfoCommercialeException( e );
						}

						return true;
					}
				}
			}
		}
		return false;
	}
}
