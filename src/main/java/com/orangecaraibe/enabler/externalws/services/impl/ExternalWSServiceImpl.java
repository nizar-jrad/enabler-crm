package com.orangecaraibe.enabler.externalws.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfint;
import com.orange.interfaces.managetroubleticket.v2.CreateTroubleTicketFault;
import com.orange.interfaces.managetroubleticket.v2.Noname;
import com.orange.interfaces.managetroubleticket.v2.UpdateTroubleTicketFault;
import com.orange.interfaces.managetroubleticket.v2.root.CreateTroubleTicketMessage;
import com.orange.interfaces.managetroubleticket.v2.root.CreateTroubleTicketResponseMessage;
import com.orange.interfaces.managetroubleticket.v2.root.UpdateTroubleTicketMessage;
import com.orange.interfaces.managetroubleticket.v2.root.UpdateTroubleTicketResponseMessage;
import com.orange.sidom.soa.dcrm.datacontract.ArrayOfEntityReference;
import com.orange.sidom.soa.dcrm.datacontract.ArrayOfTypeInteraction;
import com.orange.sidom.soa.dcrm.datacontract.CampagneCriteriaType;
import com.orange.sidom.soa.dcrm.datacontract.CreateDemandeResponse;
import com.orange.sidom.soa.dcrm.datacontract.CreateInteractionResponse;
import com.orange.sidom.soa.dcrm.datacontract.Demande;
import com.orange.sidom.soa.dcrm.datacontract.DemandeCriteriaType;
import com.orange.sidom.soa.dcrm.datacontract.EntityReference;
import com.orange.sidom.soa.dcrm.datacontract.FilterCriteriaCampagne;
import com.orange.sidom.soa.dcrm.datacontract.FilterCriteriaDemande;
import com.orange.sidom.soa.dcrm.datacontract.FilterCriteriaInteraction;
import com.orange.sidom.soa.dcrm.datacontract.FilterCriteriaNotes;
import com.orange.sidom.soa.dcrm.datacontract.FilterCriteriaParty;
import com.orange.sidom.soa.dcrm.datacontract.FilterCriteriaTheme;
import com.orange.sidom.soa.dcrm.datacontract.FindAndGetCampagneResponse;
import com.orange.sidom.soa.dcrm.datacontract.FindAndGetDemandeResponse;
import com.orange.sidom.soa.dcrm.datacontract.FindAndGetInteractionResponse;
import com.orange.sidom.soa.dcrm.datacontract.FindAndGetPartyResponse;
import com.orange.sidom.soa.dcrm.datacontract.FindAndGetThemeResponse;
import com.orange.sidom.soa.dcrm.datacontract.Interaction;
import com.orange.sidom.soa.dcrm.datacontract.InteractionCriteriaType;
import com.orange.sidom.soa.dcrm.datacontract.ObjectFactory;
import com.orange.sidom.soa.dcrm.datacontract.PartyCriteriaType;
import com.orange.sidom.soa.dcrm.datacontract.ThemesNiveau1Filter;
import com.orange.sidom.soa.dcrm.datacontract.TypeInteraction;
import com.orange.sidom.soa.dcrm.servicecontract.campagne._1_0.ManageCampagne;
import com.orange.sidom.soa.dcrm.servicecontract.campagne._1_0.ManageCampagneFindAndGetServiceFaultFaultFaultMessage;
import com.orange.sidom.soa.dcrm.servicecontract.demande._1_0.ManageDemande;
import com.orange.sidom.soa.dcrm.servicecontract.demande._1_0.ManageDemandeCreateServiceFaultFaultFaultMessage;
import com.orange.sidom.soa.dcrm.servicecontract.demande._1_0.ManageDemandeFindAndGetServiceFaultFaultFaultMessage;
import com.orange.sidom.soa.dcrm.servicecontract.demande._1_0.ManageDemandeUpdateServiceFaultFaultFaultMessage;
import com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteraction;
import com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteractionCreateServiceFaultFaultFaultMessage;
import com.orange.sidom.soa.dcrm.servicecontract.interaction._1_0.ManageInteractionFindAndGetServiceFaultFaultFaultMessage;
import com.orange.sidom.soa.dcrm.servicecontract.party._1_0.ManageParty;
import com.orange.sidom.soa.dcrm.servicecontract.party._1_0.ManagePartyFindAndGetServiceFaultFaultFaultMessage;
import com.orange.sidom.soa.dcrm.servicecontract.theme._1_0.ManageTheme;
import com.orange.sidom.soa.dcrm.servicecontract.theme._1_0.ManageThemeFindAndGetServiceFaultFaultFaultMessage;
import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.crm.business.bean.Campagne;
import com.orangecaraibe.enabler.crm.business.bean.ChildRequest;
import com.orangecaraibe.enabler.crm.business.bean.CustomerProblemReason;
import com.orangecaraibe.enabler.crm.business.bean.Holder;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.bean.Theme;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumCustomerProblemStatus;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionStatus;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumThemeType;
import com.orangecaraibe.enabler.crm.business.enums.InteractionTypeEnum;
import com.orangecaraibe.enabler.crm.business.enums.InteractionWayEnum;
import com.orangecaraibe.enabler.crm.business.enums.MediaEnum;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemCreateException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindAndGetException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindReasonException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;
import com.orangecaraibe.enabler.crm.business.exception.InteractionCreateException;
import com.orangecaraibe.enabler.crm.business.exception.InteractionFindAndGetException;
import com.orangecaraibe.enabler.crm.webservice.criteria.InteractionCriteria;
import com.orangecaraibe.enabler.externalws.adapters.TroubleTicketAdapter;
import com.orangecaraibe.enabler.externalws.adapters.WSCrmAdapter;
import com.orangecaraibe.enabler.externalws.builder.CustomerOrderActBuilder;
import com.orangecaraibe.enabler.externalws.builder.CustomerOrderOptionBuilder;
import com.orangecaraibe.enabler.externalws.builder.TransfertCreditBuilder;
import com.orangecaraibe.enabler.externalws.consumer.AsyncManageInteraction;
import com.orangecaraibe.enabler.externalws.services.ExternalWSService;
import com.orangecaraibe.enabler.externalws.utils.ExternalWSConstants;
import com.orangecaraibe.soa.v2.criteria.CriteriaComposer;
import com.orangecaraibe.soa.v2.criteria.Restrictions;
import com.orangecaraibe.soa.v2.criteria.Variations;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.CreateUpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.ManageCommercialInstalledBase;
import com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.CreateCustomerOrderException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.ManageCustomerOrderCapture;
import com.orangecaraibe.soa.v2.model.commons.UseCase;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.commons.criteria.Eq;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CustomerOrder;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledAtomicOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPlay;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledFunctionValue;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledProduct;

import fcm.gp.oc.schemas.dunning.data.dunningregistercard.RegisterCardActionData;
import fcm.gp.oc.schemas.dunning.data.dunningregistercard.WbsDunningRegistercardSoap;
import fcm.gp.oc.schemas.dunning.data.dunningregistercard.WbsDunningRegistercardSoapUpdateStatusValidationFaultFaultFaultMessage;
import fcm.gp.oc.schemas.dunning.data.dunningworkitem.CreateWorkLoadData;
import fcm.gp.oc.schemas.dunning.data.dunningworkitem.WbsDunningWorkitemSoap;
import fcm.gp.oc.schemas.dunning.data.dunningworkitem.WbsDunningWorkitemSoapCreateWorkLoadValidationFaultFaultFaultMessage;

@Service( "externalWSService" )
public class ExternalWSServiceImpl
	implements ExternalWSService
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( ExternalWSServiceImpl.class );

	/**
	 * Client WS vers le Super MCIB
	 */
	@Autowired
	@Qualifier( "superMCIBConsumer" )
	private ManageCommercialInstalledBase superMCIB;

	/**
	 * client WS vers l'Order Manager
	 */
	@Autowired
	@Qualifier( "manageCustomerOrderConsumer" )
	private ManageCustomerOrderCapture manageCustomerOrderCapture;

	/**
	 * Client WS vers l'enabler VTR
	 */
	@Autowired
	@Qualifier( "enablerVTRConsumer" )
	private ManageCommercialInstalledBase enablerVTR;

	/**
	 * Builder de customerOrder pour les commandes d'options
	 */
	@Autowired
	private CustomerOrderOptionBuilder customerOrderOptionBuilder;

	/**
	 * Builder de customerOrder pour les commandes d'options
	 */
	@Autowired
	private CustomerOrderActBuilder customerOrderActBuilder;

	/**
	 * Builder de l'enabler VTR pour les tranferts de credit
	 */
	@Autowired
	private TransfertCreditBuilder transfertCreditBuilder;

	/**
	 * Client WS vers le web service Dunning workitem
	 */
	@Autowired
	@Qualifier( "wbsDunningWorkitemConsumer" )
	private WbsDunningWorkitemSoap WbsDunningWorkitemConsumer;

	/**
	 * Client WS vers le service de gestion des demandes DCRM
	 */
	@Autowired
	@Qualifier( "manageDemandeConsumer" )
	private ManageDemande manageDemandeConsumer;

	/**
	 * Client WS vers le service de gestion des interactions DCRM
	 */
	@Autowired
	@Qualifier( "manageDCRMInteractionConsumer" )
	private ManageInteraction manageInteractionConsumer;

	/**
	 * Client WS vers le service de gestion des interactions DCRM
	 */
	@Autowired
	@Qualifier( "asyncManageInteractionConsumer" )
	private AsyncManageInteraction asyncManageInteractionConsumer;

	/**
	 * Client WS vers les themes DCRM
	 */
	@Autowired
	@Qualifier( "manageThemeConsumer" )
	private ManageTheme manageThemeConsumer;

	/**
	 * Client WS vers le web service Dunning RegisterCard
	 */
	@Autowired
	@Qualifier( "manageWbsDunningRegisterCardConsumer" )
	private WbsDunningRegistercardSoap wbsDunningRegisterCardConsumer;

	/**
	 * ObjectFactory pour la creation des criteria
	 */
	@Autowired
	@Qualifier( "objectFactoryDCRM" )
	private ObjectFactory objectFactory;

	/**
	 * objectFactory microsoft
	 */
	@Autowired
	@Qualifier( "objectFactoryMicrosoft" )
	private com.microsoft.schemas._2003._10.serialization.ObjectFactory objectFactoryMicrosoft;

	/** adapter pour le modele objet DCRM */
	@Autowired
	@Qualifier( "wsCrmAdapter" )
	private WSCrmAdapter wsCrmAdapter;

	/** consumer pour les appels a OCEANE */
	@Autowired
	@Qualifier( "managetroubleTicketConsumer" )
	private Noname troubleTicket;

	/** adapter pour adapter les objets Metier en objets OCEANE */
	@Autowired
	private TroubleTicketAdapter troubleTicketAdapter;

	@Autowired
	@Qualifier( "managePartyConsumer" )
	/** ManageAPrty DCRM */
	private ManageParty manageParty;

	/** ManageCampagne DCRM */
	@Autowired
	@Qualifier( "manageCampagneConsumer" )
	private ManageCampagne manageCampagne;

	private final static Map<MediaEnum, TypeInteraction> mediaMap = new HashMap<MediaEnum, TypeInteraction>();
	static
	{
		mediaMap.put( MediaEnum.Phone, TypeInteraction.APPEL_TELEPHONIQUE );
		mediaMap.put( MediaEnum.Letter, TypeInteraction.COURRIER );
		mediaMap.put( MediaEnum.Email, TypeInteraction.EMAIL );
		mediaMap.put( MediaEnum.Fax, TypeInteraction.FAX );
		mediaMap.put( MediaEnum.Shop, TypeInteraction.PDV_PHYSIQUE );
		mediaMap.put( MediaEnum.SocialNetwork, TypeInteraction.RESEAU_SOCIAL );
		mediaMap.put( MediaEnum.Selfcare, TypeInteraction.SELFCARE );
		mediaMap.put( MediaEnum.SMS, TypeInteraction.SMS );
	}

	@Override
	public CustomerOrder createOrderRemoveService(	String coId, String msisdn, String tmCode, String snCode,
													String spCode, String customerId )
		throws CreateCustomerOrderException, InterfaceUnavailableException
	{
		// Appel du builder d'Order d'option
		CustomerOrder customerOrder =
			customerOrderOptionBuilder.createRemoveOptionCustomerOrderFromLegacy(	coId, msisdn, tmCode, snCode, spCode,
																					customerId );

		// Appel du WS ManageCustomerOrderCapture
		return manageCustomerOrderCapture.createCustomerOrder( customerOrder );
	}

	@Override
	public CustomerOrder createOrderAddCatalogueService(	String coId, String msisdn, String tmCode,
															String offerSpecificationCode, String customerId )
		throws InterfaceUnavailableException, CreateCustomerOrderException
	{
		// Appel du builder d'Order d'option
		CustomerOrder customerOrder =
			customerOrderOptionBuilder.createAddOptionCustomerOrderFromCatalogue(	coId, msisdn, tmCode,
																					offerSpecificationCode,
																					customerId );

		// Appel du WS ManageCustomerOrderCapture
		return manageCustomerOrderCapture.createCustomerOrder( customerOrder );
	}

	@Override
	public CustomerOrder creditIN(	String coId, String msisdn, String tmCode, String offerSpecificationCode,
									String customerId )
		throws InterfaceUnavailableException, CreateCustomerOrderException
	{
		// Appel du builder d'Order d'option
		CustomerOrder customerOrder =
			customerOrderActBuilder.createAddActCustomerOrderFromCatalogue( coId, msisdn, tmCode,
																			offerSpecificationCode, customerId );

		// Appel du WS ManageCustomerOrderCapture
		return manageCustomerOrderCapture.createCustomerOrder( customerOrder );
	}

	@Override
	public Double getContractCredit( String msisdn )
		throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		FindAndGetException
	{
		Double credit = 0.0;
		Eq restriction = Restrictions.eq( ExternalWSConstants.INSTALLED_PUBLIC_KEY_ID, msisdn );
		Criteria criteria =
			CriteriaComposer.getInstance().add( restriction ).add( Variations.include( ExternalWSConstants.CREDIT_INCUDING ) ).getCriteria();

		List<InstalledOffer> offers = superMCIB.findAndGet( criteria );

		for ( InstalledOffer offer : offers )
		{
			if ( offer instanceof InstalledContract )
			{

				InstalledContract contract = (InstalledContract) offer;

				for ( InstalledOffer installedOffer : contract.getChildrenInstalledOffers() )
				{
					if ( installedOffer instanceof InstalledPlay )
					{
						InstalledPlay play = (InstalledPlay) installedOffer;
						for ( InstalledOffer childOffer : play.getChildrenInstalledOffers() )
						{
							if ( childOffer != null && childOffer.getOfferSpecification() != null
								&& ExternalWSConstants.ATOMIC_OFFER_CREDIT.equals( childOffer.getOfferSpecification().getOfferSpecificationCode() ) )
							{
								if ( childOffer instanceof InstalledAtomicOffer )
								{
									InstalledAtomicOffer atomicOffer = (InstalledAtomicOffer) childOffer;
									if ( atomicOffer.getInstalledProducts() != null )
									{
										for ( InstalledProduct product : atomicOffer.getInstalledProducts() )
										{
											if ( product.getInstalledFunctionValues() != null )
											{
												for ( InstalledFunctionValue functionValue : product.getInstalledFunctionValues() )
												{
													return Double.parseDouble( functionValue.getFunctionValue() );
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

		}
		return credit;
	}

	@Override
	public void setCreditIN( String msisdn, Double amount, int daysActive, int daysInactive )
		throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		CreateUpdateException
	{
		List<UseCase> useCases = new ArrayList<UseCase>();
		List<InstalledOffer> installedOffers = new ArrayList<InstalledOffer>();
		InstalledContract installedContract =
			transfertCreditBuilder.createTransertCreditInstalledOffer( msisdn, amount, daysActive, daysInactive );
		installedOffers.add( installedContract );
		enablerVTR.createUpdate( installedOffers, useCases );
	}

	@Override
	public List<InstalledOffer> findAndGetSuperMCIB( Criteria criteria )
		throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		FindAndGetException
	{
		return superMCIB.findAndGet( criteria );
	}

	@Override
	public CreateWorkLoadData createDunningWorkLoad( CreateWorkLoadData createWorkLoadData )
		throws CustomerProblemUpdateException
	{
		try
		{
			return WbsDunningWorkitemConsumer.createWorkLoad( createWorkLoadData );
		}
		catch ( WbsDunningWorkitemSoapCreateWorkLoadValidationFaultFaultFaultMessage e )
		{
			LOGGER.error( "Error dans l'appel de la création de la charge de travail " + e.getMessage() );
			CustomerProblemUpdateException exception =
				new CustomerProblemUpdateException( "Erreur lors de la notification au dunning : " + e.getMessage(),
													e.toString() );
			throw exception;
		}
	}

	@Override
	public RegisterCardActionData updateDunningRegisterCard( RegisterCardActionData registerCardActionData )
		throws CustomerProblemUpdateException
	{
		try
		{
			return wbsDunningRegisterCardConsumer.updateStatus( registerCardActionData );
		}
		catch ( WbsDunningRegistercardSoapUpdateStatusValidationFaultFaultFaultMessage e )
		{
			LOGGER.error( "Erreur dans l'appel pour l'update du statut d'homologation du contrat " + e.getMessage() );
			CustomerProblemUpdateException exception =
				new CustomerProblemUpdateException( "Erreur dans l'appel pour l'update du statut d'homologation du contrat : "
					+ e.getMessage(), e.toString() );
			throw exception;
		}

	}

	@Override
	public Request createDemande( Request request )
		throws CustomerProblemCreateException, CustomerProblemFindReasonException
	{
		try
		{
			Demande demande = wsCrmAdapter.adaptCreateDemande( request );
			CreateDemandeResponse response = manageDemandeConsumer.create( demande );
			request.setGuid( response.getGUIDDemande() );
			LOGGER.debug( "Création de la demande {} avec GUID: {}", request.getTitle(), request.getGuid() );
		}
		catch ( ManageDemandeCreateServiceFaultFaultFaultMessage exception )
		{
			String message = exception.getMessage();
			if ( exception.getFaultInfo() != null && exception.getFaultInfo().getMessage() != null
				&& StringUtils.isNotEmpty( exception.getFaultInfo().getMessage().getValue() ) )
			{
				message += " message:".concat( exception.getFaultInfo().getMessage().getValue() );
			}
			LOGGER.error( "Erreur lors de l'appel au web service DCRM : {}", message );
			CustomerProblemCreateException customerProblemCreateException =
				new CustomerProblemCreateException( "Erreur lors de l'appel au web service DCRM : ".concat( message ),
													"CUSP012" );
			throw customerProblemCreateException;
		}
		return request;

	}

	@Override
	public com.orangecaraibe.enabler.crm.business.bean.Interaction createInteraction( com.orangecaraibe.enabler.crm.business.bean.Interaction businessInteraction )
		throws InteractionCreateException
	{
		Interaction dcrmInteraction;
		try
		{
			dcrmInteraction = wsCrmAdapter.adaptBusinessInteraction( businessInteraction );
		}
		catch ( Exception e )
		{
			LOGGER.error( "Erreur lors de l'adaptation de l'interaction metier en interaction DCRM", e );
			InteractionCreateException interactionCreateException =
				new InteractionCreateException( "Erreur lors de l'adaptation de l'interaction : " + e.getMessage(),
												"INT014" );
			throw interactionCreateException;
		}

		CreateInteractionResponse dcrmResponse = null;
		try
		{
			dcrmResponse = manageInteractionConsumer.create( dcrmInteraction );
		}
		catch ( ManageInteractionCreateServiceFaultFaultFaultMessage e )
		{

			String message = e.getMessage();
			if ( e.getFaultInfo() != null && e.getFaultInfo().getMessage() != null
				&& StringUtils.isNotEmpty( e.getFaultInfo().getMessage().getValue() ) )
			{
				message += " message:" + e.getFaultInfo().getMessage().getValue();
			}
			LOGGER.error( "Erreur lors de l'appel au web service DCRM : " + e.getMessage(), e );
			InteractionCreateException interactionCreateException =
				new InteractionCreateException( "Erreur lors de l'appel au web service DCRM - " + message, "INT012" );
			throw interactionCreateException;
		}

		if ( dcrmResponse != null )
		{
			// postionnement de l'ID d'interaction DCRM
			if ( StringUtils.isNotEmpty( dcrmResponse.getGUIDInteraction() ) )
			{
				businessInteraction.setId( dcrmResponse.getGUIDInteraction() );
			}
			// postionnement du statut "en cours" par defaut
			if ( businessInteraction.getStatus() == null )
			{
				businessInteraction.setStatus( EnumInteractionStatus.IN_PROGRESS );
			}
		}

		return businessInteraction;

	}

	@Override
	public List<com.orangecaraibe.enabler.crm.business.bean.Interaction> findAndGetInteraction( InteractionCriteriaType dcrmCriteria,
																								String dcrmCriteriaValue,
																								InteractionCriteria interactionCriteria )
		throws InteractionFindAndGetException
	{
		List<com.orangecaraibe.enabler.crm.business.bean.Interaction> interactions =
			new ArrayList<com.orangecaraibe.enabler.crm.business.bean.Interaction>();

		/*
		 * filtres additionnels
		 */
		FilterCriteriaInteraction dcrmAdditionalCriteria = new FilterCriteriaInteraction();

		// filtre sur le sens de l'interaction
		if ( interactionCriteria.getInteractionWay() == InteractionWayEnum.IN )
		{
			dcrmAdditionalCriteria.setDirection( objectFactory.createFilterCriteriaInteractionDirection( false ) );
		}
		else if ( interactionCriteria.getInteractionWay() == InteractionWayEnum.OUT )
		{
			dcrmAdditionalCriteria.setDirection( objectFactory.createFilterCriteriaInteractionDirection( true ) );
		}

		// filtre sur le statut de l'interaction
		if ( CollectionUtils.isNotEmpty( interactionCriteria.getInteractionStatusList() ) )
		{
			ArrayOfint arrayOfStatus = new ArrayOfint();
			for ( EnumInteractionStatus status : interactionCriteria.getInteractionStatusList() )
			{
				Integer dcrmStatus = MapUtils.invertMap( WSCrmAdapter.interactionStatusMap ).get( status );
				arrayOfStatus.getInt().add( dcrmStatus );
			}
			JAXBElement<ArrayOfint> jaxbElementStatus =
				objectFactory.createFilterCriteriaInteractionStatuts( arrayOfStatus );
			dcrmAdditionalCriteria.setStatuts( jaxbElementStatus );
		}

		// filtre sur les medias de l'interaction
		if ( CollectionUtils.isNotEmpty( interactionCriteria.getMedias() ) )
		{
			ArrayOfTypeInteraction arrayOfTypeInteraction = new ArrayOfTypeInteraction();
			for ( MediaEnum media : interactionCriteria.getMedias() )
			{
				TypeInteraction dcrmInteractionType = mediaMap.get( media );
				arrayOfTypeInteraction.getTypeInteraction().add( dcrmInteractionType );
			}
			JAXBElement<ArrayOfTypeInteraction> filterCriteriaInteractionMedia =
				objectFactory.createFilterCriteriaInteractionMedia( arrayOfTypeInteraction );

			dcrmAdditionalCriteria.setMedia( filterCriteriaInteractionMedia );
		}

		// periode de recherche - fromDate
		if ( interactionCriteria.getFromDate() != null )
		{
			dcrmAdditionalCriteria.setFromDate( objectFactory.createFilterCriteriaInteractionFromDate( DateUtils.convertDateToXMLGregorianCalendar( interactionCriteria.getFromDate() ) ) );
		}
		// periode de recherche - toDate
		if ( interactionCriteria.getToDate() != null )
		{
			dcrmAdditionalCriteria.setToDate( objectFactory.createFilterCriteriaInteractionToDate( DateUtils.convertDateToXMLGregorianCalendar( interactionCriteria.getToDate() ) ) );
		}

		// limitation du nombre max d'enregistrements
		if ( interactionCriteria.getMaxResults() != null && ( interactionCriteria.getMaxResults() > 0 ) )
		{
			dcrmAdditionalCriteria.setNombreMaxEnregistrements( objectFactory.createFilterCriteriaInteractionNombreMaxEnregistrements( interactionCriteria.getMaxResults() ) );
		}

		// inclusion des notes et attachements
		if ( interactionCriteria.isIncludeNotes() || interactionCriteria.isIncludeAttachments() )
		{
			FilterCriteriaNotes filterNote = new FilterCriteriaNotes();
			filterNote.setGetNote( objectFactory.createFilterCriteriaNotesGetNote( interactionCriteria.isIncludeNotes() ) );
			if ( interactionCriteria.isIncludeAttachments() )
			{
				filterNote.setGetNote( objectFactory.createFilterCriteriaNotesGetNote( true ) );
			}

			filterNote.setGetPieceJointe( objectFactory.createFilterCriteriaNotesGetPieceJointe( interactionCriteria.isIncludeAttachments() ) );
			dcrmAdditionalCriteria.setFilterNotes( objectFactory.createFilterCriteriaInteractionFilterNotes( filterNote ) );
		}

		// inclusion des demandes liees
		if ( interactionCriteria.isIncludeCustomerProblems() )
		{
			dcrmAdditionalCriteria.setGetDemandes( objectFactory.createFilterCriteriaInteractionGetDemandes( interactionCriteria.isIncludeCustomerProblems() ) );
		}

		// inclusion des campagnes liees
		if ( CollectionUtils.isNotEmpty( interactionCriteria.getInteractionTypes() ) )
		{
			for ( InteractionTypeEnum interactionType : interactionCriteria.getInteractionTypes() )
			{
				if ( interactionType == InteractionTypeEnum.MARKETING_CAMPAIGN )
				{
					dcrmAdditionalCriteria.setGetAvecCampagnesLiees( objectFactory.createFilterCriteriaInteractionGetAvecCampagnesLiees( interactionType == InteractionTypeEnum.MARKETING_CAMPAIGN ) );
				}
			}
		}

		/*
		 * Appel au WS DCRM et adaptation de la reponse
		 */
		try
		{
			// appel au WS DCRM
			FindAndGetInteractionResponse findAndGetInteractionResponse =
				manageInteractionConsumer.findAndGet( dcrmCriteria, dcrmCriteriaValue, dcrmAdditionalCriteria );
			// adaptation de la reponse renvoyee par le WS DCRM
			interactions = wsCrmAdapter.adaptFindAndGetInteractionResponse( findAndGetInteractionResponse );
		}
		catch ( ManageInteractionFindAndGetServiceFaultFaultFaultMessage e )
		{
			LOGGER.error( "erreur lors de la recherche des interactions dans le DCRM", e );
			String message = e.getMessage();
			if ( e.getFaultInfo() != null && e.getFaultInfo().getMessage() != null
				&& StringUtils.isNotEmpty( e.getFaultInfo().getMessage().getValue() ) )
			{
				message += " message:" + e.getFaultInfo().getMessage().getValue();
			}
			InteractionFindAndGetException interactionFindAndGetException =
				new InteractionFindAndGetException( "Erreur lors de l'appel au web service DCRM : " + message,
													"INT012" );
			throw interactionFindAndGetException;
		}

		return interactions;
	}

	@Override
	public List<Request> findAndGet(	DemandeCriteriaType demandeCriteria, String value, Date fromDate, Date toDate,
										List<EnumCustomerProblemStatus> status, boolean includeNote,
										boolean includeAttachment, int maxResults, boolean excludeTheme,
										List<Theme> themes )
		throws CustomerProblemFindAndGetException
	{
		return findAndGet(	demandeCriteria, value, fromDate, toDate, status, includeNote, includeAttachment, maxResults,
							excludeTheme, themes, false );
	}

	@Override
	public List<Request> findAndGet(	DemandeCriteriaType demandeCriteria, String value, Date fromDate, Date toDate,
										List<EnumCustomerProblemStatus> status, boolean includeNote,
										boolean includeAttachment, int maxResults, boolean excludeTheme,
										List<Theme> themes, boolean isUseCaseEscalade )
		throws CustomerProblemFindAndGetException
	{
		List<Request> request = null;

		FilterCriteriaDemande filterCriteria = new FilterCriteriaDemande();

		if ( fromDate != null )
		{
			filterCriteria.setFromDate( objectFactory.createFilterCriteriaDemandeFromDate( DateUtils.convertDateToXMLGregorianCalendar( fromDate ) ) );
			if ( toDate != null )
			{
				filterCriteria.setToDate( objectFactory.createFilterCriteriaDemandeToDate( DateUtils.convertDateToXMLGregorianCalendar( toDate ) ) );
			}
		}

		FilterCriteriaNotes filterNote = new FilterCriteriaNotes();
		filterNote.setGetNote( objectFactory.createFilterCriteriaNotesGetNote( includeNote ) );
		filterNote.setGetPieceJointe( objectFactory.createFilterCriteriaNotesGetPieceJointe( includeAttachment ) );
		filterCriteria.setFilterNotes( objectFactory.createFilterCriteriaDemandeFilterNotes( filterNote ) );

		ThemesNiveau1Filter filterTheme = new ThemesNiveau1Filter();
		filterTheme.setExcludeThemes( objectFactory.createThemesNiveau1FilterExcludeThemes( excludeTheme ) );
		ArrayOfEntityReference arrayThemes = new ArrayOfEntityReference();
		if ( CollectionUtils.isNotEmpty( themes ) )
		{
			for ( Theme theme : themes )
			{
				EntityReference entity = new EntityReference();
				// entity.setName( objectFactory.createEntityReferenceName( theme.getLabel() ) );
				// RUNO-6100 - Correctif en référence au code enabler-crm-4.X.X
				// entity.setName( objectFactory.createEntityReferenceName( theme.getLabel() ) );
				entity.setLogicalName( theme.getLabel() );
				entity.setId( theme.getGuid() );
				arrayThemes.getEntityReference().add( entity );
			}
			// [TORI-2321] Déplacement de l'ajout des critères thèmes qui étaient positionnés tout le temps même
			// quand pas de thèmes tranmsis ce qui provoque une exception au niveau du WS DCRM
			filterTheme.setThemesNiveau1( objectFactory.createThemesNiveau1FilterThemesNiveau1( arrayThemes ) );
			filterCriteria.setThemesNiveau1( filterTheme );
		}

		if ( maxResults != 0 )
		{
			filterCriteria.setNombreMaxEnregistrements( objectFactory.createFilterCriteriaDemandeNombreMaxEnregistrements( maxResults ) );
		}

		if ( status != null )
		{
			ArrayOfint arrayOfInt = new ArrayOfint();
			for ( EnumCustomerProblemStatus customerProblemStatus : status )
			{
				final Integer stId = customerProblemStatus.getEnumRequestStatus().getId();
				if ( !arrayOfInt.getInt().contains( stId ) )
					arrayOfInt.getInt().add( stId );
			}
			filterCriteria.setStatuts( objectFactory.createFilterCriteriaDemandeStatuts( arrayOfInt ) );
		}

		if ( isUseCaseEscalade )
		{
			// filterCriteria.setIsDemandeMere( true ); Correction problème findandGet demande non mère
			filterCriteria.setGetOnlyNewOceanDemandeFilles( true );
		}

		try
		{
			// JAXBElement<String> param;

			FindAndGetDemandeResponse response =
				manageDemandeConsumer.findAndGet( demandeCriteria, value, filterCriteria );

			request = wsCrmAdapter.adaptFindAndGetRequest( response, includeNote || includeAttachment );
		}
		catch ( ManageDemandeFindAndGetServiceFaultFaultFaultMessage e )
		{
			String message = e.getMessage();
			if ( e.getFaultInfo() != null && e.getFaultInfo().getMessage() != null
				&& StringUtils.isNotEmpty( e.getFaultInfo().getMessage().getValue() ) )
			{
				message += " message:" + e.getFaultInfo().getMessage().getValue();
			}
			CustomerProblemFindAndGetException customerProblemFindAndGetException =
				new CustomerProblemFindAndGetException( "ErrorFindAndGet DCRM :" + message, null );
			throw customerProblemFindAndGetException;
		}
		return request;

	}

	@Override
	public List<CustomerProblemReason> findCustomerProblemReason( String code, String canalEmetteur, long niveau )
		throws CustomerProblemFindReasonException
	{

		ObjectFactory objfacto = new ObjectFactory();

		FilterCriteriaTheme criteria = new FilterCriteriaTheme();

		if ( canalEmetteur != null )
		{
			criteria.setCanal( objfacto.createFilterCriteriaThemeCanal( canalEmetteur ) );
		}
		if ( code != null )
		{
			criteria.setType( objfacto.createFilterCriteriaThemeType( EnumThemeType.getByTypeSata( code ).getDcrmId() ) );
		}
		// criteria.setType(
		List<CustomerProblemReason> customerProblems = null;
		FindAndGetThemeResponse response;
		try
		{
			response = manageThemeConsumer.findAndGet( criteria );
			customerProblems = wsCrmAdapter.adaptCustomerProblemFindReason( response );
		}
		catch ( ManageThemeFindAndGetServiceFaultFaultFaultMessage e )
		{
			String message = e.getMessage();
			if ( e.getFaultInfo() != null && e.getFaultInfo().getMessage() != null
				&& StringUtils.isNotEmpty( e.getFaultInfo().getMessage().getValue() ) )
			{
				message += " message:" + e.getFaultInfo().getMessage().getValue();
			}
			CustomerProblemFindReasonException ex = new CustomerProblemFindReasonException( message, "" );
			throw ex;
		}

		return customerProblems;
	}

	@Override
	public List<Campagne> findAndGetCampagne( String partyGuid, String contractId, Date fromDate, Date toDate )
	{

		CampagneCriteriaType criteria = null;
		String value = null;
		FilterCriteriaCampagne additionalCriteria = objectFactory.createFilterCriteriaCampagne();
		;
		List<Campagne> interaction = null;
		try
		{
			if ( contractId != null )
			{
				criteria = CampagneCriteriaType.COID;
				value = contractId;
			}
			else if ( partyGuid != null )
			{
				criteria = CampagneCriteriaType.GUID_PARTY;
				value = partyGuid;
			}
			if ( fromDate != null )
			{
				additionalCriteria.setFromDate( objectFactory.createFilterCriteriaCampagneFromDate( DateUtils.convertDateToXMLGregorianCalendar( fromDate ) ) );
			}
			if ( toDate != null )
			{
				additionalCriteria.setFromDate( objectFactory.createFilterCriteriaCampagneFromDate( DateUtils.convertDateToXMLGregorianCalendar( toDate ) ) );
			}

			FindAndGetCampagneResponse campagne = manageCampagne.findAndGet( criteria, value, additionalCriteria );
			if ( campagne.getCampagnes() != null && campagne.getCampagnes().getValue() != null )
			{
				interaction = wsCrmAdapter.adapteCamapagne( campagne.getCampagnes().getValue() );
			}
		}
		catch ( ManageCampagneFindAndGetServiceFaultFaultFaultMessage e )
		{
			String errorMessage = "";
			if ( e.getFaultInfo() != null && e.getFaultInfo().getMessage() != null )
			{
				errorMessage = e.getFaultInfo().getErrorCode() + e.getFaultInfo().getMessage().getValue();
			}
			LOGGER.error( "Erreur lors de la récupération des campgne  " + errorMessage, e );

		}
		return interaction;
	}

	@Override
	public Request updateCustomerProblem( Request request )
		throws CustomerProblemUpdateException
	{

		Demande demande = wsCrmAdapter.adaptUpdateDemande( request );
		try
		{
			manageDemandeConsumer.update( demande );
		}
		catch ( ManageDemandeUpdateServiceFaultFaultFaultMessage e )
		{
			String message = e.getMessage();
			if ( e.getFaultInfo() != null && e.getFaultInfo().getMessage() != null
				&& StringUtils.isNotEmpty( e.getFaultInfo().getMessage().getValue() ) )
			{
				message += " message:" + e.getFaultInfo().getMessage().getValue();
			}
			CustomerProblemUpdateException exception = new CustomerProblemUpdateException( message, "" );

			throw exception;
		}

		return request;
	}

	@Override
	public Request createTroubleTicketOceane( Request request, String cuid )
		throws CustomerProblemUpdateException
	{
		CreateTroubleTicketMessage parameters = troubleTicketAdapter.createTroubleTicketAdapter( request, cuid );
		CreateTroubleTicketResponseMessage result;
		try
		{
			result = troubleTicket.createTroubleTicket( parameters );
		}
		catch ( CreateTroubleTicketFault e )
		{
			LOGGER.error( e.getFaultInfo().getFault().getFaultLabel() );
			CustomerProblemUpdateException exception =
				new CustomerProblemUpdateException( "Erreur lors de l'appel à CreateTroubleTicket :" + e.getMessage(),
													"CUSP015" );
			throw exception;

		}
		request.setTroubleTicketID( result.getTroubleTicket().getTroubleTicketID() );
		return request;
	}

	@Override
	public Request updateTroubleTicketOceane( Request request, String cuid )
		throws CustomerProblemUpdateException
	{
		UpdateTroubleTicketMessage parameters = troubleTicketAdapter.createUpdateTroubleTicketMessage( request, cuid );
		UpdateTroubleTicketResponseMessage result;
		try
		{
			result = troubleTicket.updateTroubleTicket( parameters );
		}
		catch ( UpdateTroubleTicketFault e )
		{
			LOGGER.error( e.getFaultInfo().getFault().getFaultLabel() );
			CustomerProblemUpdateException exception =
				new CustomerProblemUpdateException( "Erreur lors de l'appel à UpdateTroubleTicket", "CUSP016" );
			throw exception;
		}
		if ( request.getDemandesFilles() != null )
		{
			for ( ChildRequest cr : request.getDemandesFilles() )
			{
				cr.setTroubleTicketID( result.getTroubleTicket().getTroubleTicketID() );
			}
		}
		return request;
	}

	public List<Holder> findPartyInformation(	PartyCriteriaType criteria, String param, boolean includePartyRoleSet,
												boolean incluseOptIn, boolean excludeContactMethode,
												boolean excludePartyRole )
		throws CustomerProblemUpdateException
	{

		FilterCriteriaParty additionalCriteria = new FilterCriteriaParty();
		additionalCriteria.setGetConnexions( objectFactory.createFilterCriteriaPartyGetConnexions( includePartyRoleSet ) );
		additionalCriteria.setGetMoyensDeContact( objectFactory.createFilterCriteriaPartyGetMoyensDeContact( !excludeContactMethode ) );
		additionalCriteria.setGetRolesClient( objectFactory.createFilterCriteriaPartyGetRolesClient( !excludePartyRole ) );
		additionalCriteria.setGetElectronicIdentity( objectFactory.createFilterCriteriaPartyGetElectronicIdentity( false ) );
		List<Holder> customerPersonalInfos = null;

		try
		{

			FindAndGetPartyResponse response = manageParty.findAndGet( criteria, param, additionalCriteria );
			customerPersonalInfos = wsCrmAdapter.adapteFindAndGetParty( response );

		}
		catch ( ManagePartyFindAndGetServiceFaultFaultFaultMessage e )
		{
			CustomerProblemUpdateException exception =
				new CustomerProblemUpdateException( "CRM0030",
													"Erreur lors de la consolidation de la demande avec les information du party" );
			throw exception;
		}

		return customerPersonalInfos;
	}

}
