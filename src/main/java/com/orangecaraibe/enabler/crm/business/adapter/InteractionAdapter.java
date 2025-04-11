package com.orangecaraibe.enabler.crm.business.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.crm.business.bean.Actor;
import com.orangecaraibe.enabler.crm.business.bean.Address;
import com.orangecaraibe.enabler.crm.business.bean.Attachment;
import com.orangecaraibe.enabler.crm.business.bean.Campagne;
import com.orangecaraibe.enabler.crm.business.bean.ContactMethods;
import com.orangecaraibe.enabler.crm.business.bean.CustomerService;
import com.orangecaraibe.enabler.crm.business.bean.CustomerServiceOrganisation;
import com.orangecaraibe.enabler.crm.business.bean.CustomerServicePerson;
import com.orangecaraibe.enabler.crm.business.bean.Interaction;
import com.orangecaraibe.enabler.crm.business.bean.Note;
import com.orangecaraibe.enabler.crm.business.bean.Party;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionChannel;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionSocialMediaType;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionStatus;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionWay;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumTitle;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemCreateException;
import com.orangecaraibe.enabler.crm.business.helper.LinkCrmBscsHelper;
import com.orangecaraibe.enabler.crm.dao.bean.LinkCrmBscsBean;
import com.orangecaraibe.soa.v2.model.commonbusiness.identity.PartyRoleType;
import com.orangecaraibe.soa.v2.model.commonbusiness.identity.UserIdentity;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionActor;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionAttachmentImage;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionDetail;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionWay;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.LocalCRMInteractionStatus;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.LocalCRMInteractionStatusCode;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.LocalComment;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Notification;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Organisation;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.OrganisationName;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Person;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.PersonName;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Salutation;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.ContactMethod;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.EmailAddress;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.PhoneNumber;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.CustomerServiceRepresentative;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.Distributor;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.PartyRole;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.PartyRoleSet;
import com.orangecaraibe.soa.v2.model.customerdomain.customer.Customer;
import com.orangecaraibe.soa.v2.model.customerdomain.customer.Holder;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccount;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblem;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Email;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Fax;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalLetter;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalSelfcare;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalShop;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.LocalSocialMedia;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.MarketingCampaign;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Media;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.MediaCharacteristic;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Phone;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Sms;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.OfferSpecification;
import com.sun.istack.ByteArrayDataSource;

/**
 * The Class InteractionAdapter.
 */
@Component
public class InteractionAdapter
{

	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( InteractionAdapter.class );

	// interaction status map that match GDM status to business status
	private final static Map<LocalCRMInteractionStatusCode, EnumInteractionStatus> interactionStatusMap =
		new HashMap<LocalCRMInteractionStatusCode, EnumInteractionStatus>();
	static
	{
		interactionStatusMap.put( LocalCRMInteractionStatusCode.IN_PROGRESS, EnumInteractionStatus.IN_PROGRESS );
		interactionStatusMap.put( LocalCRMInteractionStatusCode.COMPLETED, EnumInteractionStatus.COMPLETED );
		interactionStatusMap.put( LocalCRMInteractionStatusCode.CANCELLED, EnumInteractionStatus.CANCELLED );
	}

	// salutation map that match GDM salutation to business title
	private final static Map<Salutation, EnumTitle> salutationMap = new HashMap<Salutation, EnumTitle>();
	static
	{
		salutationMap.put( Salutation.MLLE, EnumTitle.MLLE );
		salutationMap.put( Salutation.MME, EnumTitle.MME );
		salutationMap.put( Salutation.MR, EnumTitle.MR );
	}

	// interaction types map that match GDM interaction types to business channel
	private final static Map<Class, EnumInteractionChannel> interactionTypesMap =
		new HashMap<Class, EnumInteractionChannel>();
	static
	{
		interactionTypesMap.put( Fax.class, EnumInteractionChannel.FAX );
		interactionTypesMap.put( LocalLetter.class, EnumInteractionChannel.MAIL );
		interactionTypesMap.put( Email.class, EnumInteractionChannel.EMAIL );
		interactionTypesMap.put( Phone.class, EnumInteractionChannel.PHONE_CALL );
		interactionTypesMap.put( LocalSelfcare.class, EnumInteractionChannel.SELFCARE );
		interactionTypesMap.put( Sms.class, EnumInteractionChannel.SMS );
		interactionTypesMap.put( LocalSocialMedia.class, EnumInteractionChannel.SOCIAL_NETWORK );
		interactionTypesMap.put( LocalShop.class, EnumInteractionChannel.STORE );
	}

	/** Code technique permettant de préciser le type de reseau social */
	private static final String interactionSocialMediaType = "SocialMediaType";

	@Autowired
	private CustomerProblemAdapter customerProblemAdapter;

	@Autowired
	private LinkCrmBscsHelper linkCrmBscsHelper;

	/**
	 * Adapts a GDM interaction into a business interaction
	 * 
	 * @param gdmInteraction
	 * @return
	 * @throws Exception
	 */
	public Interaction adaptGdmInteraction( com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction gdmInteraction )
		throws Exception
	{
		Interaction businessInteraction = new Interaction();

		// adaptation de l'ID d'interaction
		if ( StringUtils.isNotEmpty( gdmInteraction.getInteractionID() ) )
		{
			businessInteraction.setId( gdmInteraction.getInteractionID() );
		}

		// sens de l'interaction
		EnumInteractionWay interactionWay = adaptGdmInteractionWay( gdmInteraction.getInteractionWay() );
		if ( interactionWay != null )
		{
			businessInteraction.setWay( interactionWay );
		}

		EnumInteractionChannel channel = null;
		// adaptation du canal
		if ( gdmInteraction.getMedia() != null
			&& interactionTypesMap.containsKey( gdmInteraction.getMedia().getClass() ) )
		{
			businessInteraction.setCanal( interactionTypesMap.get( gdmInteraction.getMedia().getClass() ) );
			channel = interactionTypesMap.get( gdmInteraction.getMedia().getClass() );

			// adaptation des caractéristique du média
			if ( gdmInteraction.getMedia().getMediaCharacteristics() != null
				&& !gdmInteraction.getMedia().getMediaCharacteristics().isEmpty() )
			{
				for ( MediaCharacteristic mediaCharacteristic : gdmInteraction.getMedia().getMediaCharacteristics() )
				{
					// Gestion du type de reseau social
					if ( StringUtils.equalsIgnoreCase(	mediaCharacteristic.getCode().toString(),
														interactionSocialMediaType ) )
					{
						businessInteraction.setSocialMediaType( EnumInteractionSocialMediaType.getInteractionTypeByName( mediaCharacteristic.getValue().toString() ) );
					}
				}
			}
		}

		// adaptation du statut
		if ( CollectionUtils.isNotEmpty( gdmInteraction.getLocalCRMInteractionStatus() ) )
		{
			LocalCRMInteractionStatus crmInteractionStatus = gdmInteraction.getLocalCRMInteractionStatus().get( 0 );
			if ( interactionStatusMap.containsKey( crmInteractionStatus.getStatusCode() ) )
			{
				businessInteraction.setStatus( interactionStatusMap.get( crmInteractionStatus.getStatusCode() ) );
			}

			if ( crmInteractionStatus.getLocalStatusReason() != null )
			{
				businessInteraction.setStatusReason( crmInteractionStatus.getLocalStatusReason() );
			}

		}
		else
		{
			// creation de l'interaction au statut IN_PROGRESS par defaut
			businessInteraction.setStatus( EnumInteractionStatus.IN_PROGRESS );
		}

		// adaptation de la date de creation
		if ( gdmInteraction.getInteractionDate() != null )
		{
			businessInteraction.setCreationDate( DateUtils.getDateFromXmlGregorianCalendar( gdmInteraction.getInteractionDate() ) );
		}

		// adaptation du conseiller/equipe en charge
		// on recherche le customer service representative parmi les actor from et to
		PartyRole csr =
			(PartyRole) CollectionUtils.find(	CollectionUtils.union( gdmInteraction.getFrom(), gdmInteraction.getTo() ),
												PredicateUtils.orPredicate( PredicateUtils.instanceofPredicate( CustomerServiceRepresentative.class ),
																			PredicateUtils.instanceofPredicate( Distributor.class ) ) );
		CustomerService customerService = adaptCustomerService( csr );
		if ( customerService != null )
		{
			businessInteraction.setCustomerService( customerService );
		}
		// adaptation de l'application
		if ( csr instanceof Distributor )
		{
			if ( StringUtils.isNotEmpty( ( (Distributor) csr ).getChannelID() ) )
			{
				businessInteraction.setApplication( ( (Distributor) csr ).getChannelID() );
			}
		}

		// adaptation de la description/titre
		if ( StringUtils.isNotEmpty( gdmInteraction.getDescription() ) )
		{
			businessInteraction.setTitle( gdmInteraction.getDescription() );
		}

		// on recherche le holder parmi les actor from et to
		Holder holder =
			(Holder) CollectionUtils.find(	CollectionUtils.union( gdmInteraction.getFrom(), gdmInteraction.getTo() ),
											PredicateUtils.instanceofPredicate( Holder.class ) );

		// adaptation du holder
		com.orangecaraibe.enabler.crm.business.bean.Holder businessHolder = adaptHolder( holder );
		// le holder a ete transmis
		if ( businessHolder != null )
		{
			businessInteraction.setHolder( businessHolder );
		}
		// le holder n'est pas renseigne
		else if ( CollectionUtils.isNotEmpty( gdmInteraction.getInteractionSubject() )
			&& ( CollectionUtils.find(	gdmInteraction.getInteractionSubject(),
										PredicateUtils.instanceofPredicate( BillingAccount.class ) ) != null ) )
		{
			BillingAccount billingAccount =
				(BillingAccount) CollectionUtils.find(	gdmInteraction.getInteractionSubject(),
														PredicateUtils.instanceofPredicate( BillingAccount.class ) );
			// on tente de retrouver son GUID a partir du billingAccountID dans la table de correspondance BSCS CRM
			LinkCrmBscsBean linkCrmBscsBean =
				linkCrmBscsHelper.getCrmGuidByCustomerId( billingAccount.getBillingAccountID() );
			if ( linkCrmBscsBean != null )
			{
				businessHolder = linkCrmBscsHelper.getHolderByCustomerId( linkCrmBscsBean );
				if ( businessHolder != null )
				{
					businessInteraction.setHolder( businessHolder );
				}
			}
			else
			{
				throw new CustomerProblemCreateException( "No CRM GUID correspondance found for customer_id "
					+ billingAccount.getBillingAccountID(), "INT015" );
			}
		}

		/*
		 * Party (ou plus generalement Actor) a l'origine de l'interaction
		 */
		List<Actor> businessActorsFrom = adaptGdmPartyRolesToBusinessActors( gdmInteraction.getFrom() );
		if ( CollectionUtils.isNotEmpty( businessActorsFrom ) )
		{
			businessInteraction.setFrom( businessActorsFrom );
		}
		else if ( interactionWay != EnumInteractionWay.OUTGOING )
		{
			businessInteraction.getFrom().add( businessHolder );
		}

		/*
		 * Party (ou plus generalement Actor) a destination de l'interaction
		 */
		List<Actor> businessActorsTo = adaptGdmPartyRolesToBusinessActors( gdmInteraction.getTo() );

		if ( CollectionUtils.isNotEmpty( businessActorsTo ) )
		{
			businessInteraction.setTo( businessActorsTo );
		}
		else if ( interactionWay == EnumInteractionWay.OUTGOING )
		{
			businessInteraction.getTo().add( businessHolder );
		}

		// Demande en rapport avec l'interaction (si l'interaction est consecutive au traitement d'une demande).
		List<Request> requests = new ArrayList<Request>();
		if ( CollectionUtils.isNotEmpty( gdmInteraction.getLocalCustomerProblems() ) )
		{
			for ( CustomerProblem customerProblem : gdmInteraction.getLocalCustomerProblems() )
			{
				if ( StringUtils.isNotEmpty( customerProblem.getTroubleTicketID() ) )
				{
					Request request = new Request();
					request.setGuid( customerProblem.getTroubleTicketID() );
					requests.add( request );
				}
				else if ( customerProblem.getReason() != null )
				{
					Request request = customerProblemAdapter.adapteCreate( customerProblem );
					request.setHolder( businessInteraction.getHolder() );

					requests.add( request );
				}
			}
		}
		businessInteraction.setRequests( requests );

		// adaptation des notes
		List<Note> notes = adaptNotes( gdmInteraction.getLocalComment() );
		if ( CollectionUtils.isNotEmpty( notes ) )
		{
			businessInteraction.setNotes( notes );
		}

		List<InteractionDetail> lstInteractionDetails = gdmInteraction.getDetails();
		if ( lstInteractionDetails != null && !lstInteractionDetails.isEmpty() )
		{
			// Il n'y a en a qu'une
			for ( InteractionDetail interactionDetail : lstInteractionDetails )
			{
				businessInteraction.setDescription( interactionDetail.getInteractionBody() );
				break;
			}
		}

		// adaptation des attachements
		List<Attachment> businessAttachments = adaptGdmAttachments( gdmInteraction.getAttachments() );
		if ( CollectionUtils.isNotEmpty( businessAttachments ) )
		{
			businessInteraction.setAttachments( businessAttachments );
		}

		// interaction de type notification
		if ( gdmInteraction.getInteractionRole() != null )
		{
			businessInteraction.setNotification( gdmInteraction.getInteractionRole() instanceof Notification );
		}

		// adaptation de l'ID externe de l'interaction
		if ( StringUtils.isNotEmpty( gdmInteraction.getLocalExternalID() ) )
		{
			businessInteraction.setCourrierId( gdmInteraction.getLocalExternalID() );
		}

		if ( gdmInteraction.getToContactMethod() != null )
		{
			businessInteraction.getTo().add( createContactMethodeTo( gdmInteraction.getToContactMethod() ) );

			if ( gdmInteraction.getToContactMethod() instanceof PhoneNumber )
			{
				businessInteraction.setNumeroTelephoneAppele( ( (PhoneNumber) gdmInteraction.getToContactMethod() ).getNumber() );
			}
		}

		// RUNO-2081 : le num de l'appelant n'était pas renseigné dans le DCRM
		if ( gdmInteraction.getFromContactMethod() != null )
		{
			businessInteraction.getFrom().add( createContactMethodeTo( gdmInteraction.getFromContactMethod() ) );

			if ( gdmInteraction.getFromContactMethod() instanceof PhoneNumber )
			{
				businessInteraction.setNumeroTelephoneAppelant( ( (PhoneNumber) gdmInteraction.getFromContactMethod() ).getNumber() );
			}
		}

		return businessInteraction;
	}

	private Actor createContactMethodeTo( ContactMethod toContactMethod )
	{
		Actor actorContactMethode = new Actor();
		ContactMethods contactMethods = new ContactMethods();
		if ( toContactMethod instanceof com.orangecaraibe.soa.v2.model.commonbusiness.location.Address )
		{

			com.orangecaraibe.soa.v2.model.commonbusiness.location.Address toAddress =
				(com.orangecaraibe.soa.v2.model.commonbusiness.location.Address) toContactMethod;

			Address address = new Address();
			address.setCity( toAddress.getCityName() );
			address.setStreetName( toAddress.getRoadName() );
			address.setStreetNum( toAddress.getRoadNumber() );
			address.setZip( toAddress.getPostalCode() );
			address.setDetails1( toAddress.getLocalAddressDetails1() );
			address.setDetails2( toAddress.getLocalAddressDetails2() );
			address.setDetails3( toAddress.getLocalAddressDetails3() );

			contactMethods.setAdress( address );

		}

		actorContactMethode.setContactMethods( contactMethods );

		return actorContactMethode;
	}

	/**
	 * Adapts GDM (SOA) interactions into a business interactions
	 * 
	 * @param interactions
	 * @return
	 */
	public List<com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction> adaptBusinessInteractions( List<Interaction> interactions )
	{

		List<com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction> gdmInteractions =
			new ArrayList<com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction>();
		for ( Interaction interaction : interactions )
		{
			com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction gdmInteraction =
				adaptBusinessInteraction( interaction );
			gdmInteractions.add( gdmInteraction );
		}

		return gdmInteractions;
	}

	/**
	 * Adapts GDM (SOA) interaction into a business interaction
	 * 
	 * @param interaction
	 * @return
	 */
	private com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction adaptBusinessInteraction( Interaction interaction )
	{
		com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction gdmInteraction =
			new com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction();

		// adaptation du canal sur le media
		if ( MapUtils.invertMap( interactionTypesMap ).containsKey( interaction.getCanal() ) )
		{
			Class<? extends Media> mediaClass = MapUtils.invertMap( interactionTypesMap ).get( interaction.getCanal() );
			try
			{
				Media gdmMedia = mediaClass.newInstance();

				// Gestion du type de reseau social
				if ( interaction.getSocialMediaType() != null )
				{
					MediaCharacteristic gdmMediaCharacteristic = new MediaCharacteristic();
					gdmMediaCharacteristic.setCode( interactionSocialMediaType );
					gdmMediaCharacteristic.setValue( interaction.getSocialMediaType().name() );
					gdmMedia.getMediaCharacteristics().add( gdmMediaCharacteristic );
				}

				gdmInteraction.setMedia( gdmMedia );
			}
			catch ( InstantiationException | IllegalAccessException e )
			{
				LOGGER.error(	"InstantiationException or IllegalAccessException occurred while creating the interaction specialised media class",
								e );
			}
		}

		// adaptation de la date de creation
		if ( interaction.getCreationDate() != null )
		{
			gdmInteraction.setInteractionDate( DateUtils.convertDateToXMLGregorianCalendar( interaction.getCreationDate() ) );
		}

		// adaptation du customer service (proprietaire de l'interaction)
		if ( interaction.getCustomerService() != null )
		{
			CustomerServiceRepresentative gdmCSR = adaptBusinessCsrToGdmCsr( interaction.getCustomerService() );

			// interaction sortante => le CSR est positionne sur le from
			if ( interaction.getWay() == EnumInteractionWay.OUTGOING )
			{
				gdmInteraction.getFrom().add( gdmCSR );
			}
			else
			{
				// CSR positionne sur le to
				gdmInteraction.getTo().add( gdmCSR );
			}
		}

		// adaptation du titulaire
		Holder gdmHolder = adaptBusinessHolder( interaction.getHolder() );
		if ( gdmHolder != null )
		{
			if ( interaction.getWay() == EnumInteractionWay.OUTGOING )
			{
				// interaction sortante => le holder est positionne sur le to
				gdmInteraction.getTo().add( gdmHolder );
			}
			else
			{
				// holder positionne sur le from
				gdmInteraction.getFrom().add( gdmHolder );
			}
		}

		// adaptation des party a l'origine de l'interaction
		if ( CollectionUtils.isNotEmpty( interaction.getFrom() ) )
		{
			// adaptation du party en role correspondant a l'acteur a l'origine
			List<PartyRole> gdmPartyRoles =
				adaptBusinessActorsToGdmRoles( interaction.getFrom(), gdmHolder, interaction.getApplication() );
			if ( CollectionUtils.isNotEmpty( gdmPartyRoles ) )
			{
				gdmInteraction.getFrom().addAll( gdmPartyRoles );
			}
			// adaptation du party en ContactMethod correspondant a la methode de contact a l'origine
			List<ContactMethod> contactMethods =
				adaptBusinessPartyListToContactMethods( interaction.getFrom(), interaction.getCanal() );
			if ( CollectionUtils.isNotEmpty( contactMethods ) )
			{
				// une seule de methode de contact from supportee
				gdmInteraction.setFromContactMethod( contactMethods.get( 0 ) );
			}
		}

		// adaptation des party destinataires de l'interaction
		if ( CollectionUtils.isNotEmpty( interaction.getTo() ) )
		{
			// adaptation du party en role correspondant a l'acteur destinataire
			List<PartyRole> gdmPartyRoles =
				adaptBusinessActorsToGdmRoles( interaction.getTo(), gdmHolder, interaction.getApplication() );
			if ( CollectionUtils.isNotEmpty( gdmPartyRoles ) )
			{
				gdmInteraction.getTo().addAll( gdmPartyRoles );
			}
			// adaptation du party en ContactMethod correspondant a la methode de contact du destinataire
			List<ContactMethod> contactMethods =
				adaptBusinessPartyListToContactMethods( interaction.getTo(), interaction.getCanal() );
			if ( CollectionUtils.isNotEmpty( contactMethods ) )
			{
				// une seule de methode de contact to supportee
				gdmInteraction.setToContactMethod( contactMethods.get( 0 ) );
			}
		}

		// adaptation de l'ID d'interaction
		if ( StringUtils.isNotEmpty( interaction.getId() ) )
		{
			gdmInteraction.setInteractionID( interaction.getId() );
		}

		// TODO : adaptation des notes
		if ( CollectionUtils.isNotEmpty( interaction.getNotes() ) )
		{

		}

		// adaptation des attachements
		if ( CollectionUtils.isNotEmpty( interaction.getAttachments() ) )
		{
			List<InteractionAttachmentImage> gdmAttachments = adaptBusinessAttachments( interaction.getAttachments() );
			gdmInteraction.getAttachments().addAll( gdmAttachments );
		}

		// adaptation des demandes liees
		if ( CollectionUtils.isNotEmpty( interaction.getRequests() ) )
		{
			List<CustomerProblem> customerProblems =
				adaptBusinessRequestsToCustomerProblems( interaction.getRequests() );
			gdmInteraction.getLocalCustomerProblems().addAll( customerProblems );
		}

		// adaptation du statut
		if ( MapUtils.invertMap( interactionStatusMap ).containsKey( interaction.getStatus() ) )
		{
			LocalCRMInteractionStatusCode gdmStatusCode =
				MapUtils.invertMap( interactionStatusMap ).get( interaction.getStatus() );
			LocalCRMInteractionStatus gdmStatus = new LocalCRMInteractionStatus();
			gdmStatus.setStatusCode( gdmStatusCode );
			gdmInteraction.getLocalCRMInteractionStatus().add( gdmStatus );

			// raison du statut
			if ( StringUtils.isNotEmpty( interaction.getStatusReason() ) )
			{
				gdmStatus.setLocalStatusReason( interaction.getStatusReason() );
			}
		}

		// adaptation du titre sur la description
		if ( StringUtils.isNotEmpty( interaction.getTitle() ) && EnumInteractionChannel.SMS != interaction.getCanal() )
		{
			gdmInteraction.setDescription( interaction.getTitle() );
		}
		else if ( StringUtils.isNotEmpty( interaction.getDescription() )
			&& EnumInteractionChannel.SMS == interaction.getCanal() )
		{
			gdmInteraction.setDescription( interaction.getDescription() );
		}

		// sens de l'interaction
		InteractionWay gdmInteractionWay = adaptBusinessInteractionWay( interaction.getWay() );
		if ( gdmInteractionWay != null )
		{
			gdmInteraction.setInteractionWay( gdmInteractionWay );
		}

		// interaction de type notification
		if ( interaction.isNotification() )
		{
			Notification gdmNotification = new Notification();
			gdmInteraction.setInteractionRole( gdmNotification );
		}

		// id courrier ALF
		if ( interaction.getCourrierId() != null )
		{
			gdmInteraction.setLocalExternalID( interaction.getCourrierId() );
		}
		return gdmInteraction;

	}

	/**
	 * Adapts GDM (SOA) holder into a business holder
	 * 
	 * @param gdmHolder
	 * @return
	 */
	private com.orangecaraibe.enabler.crm.business.bean.Holder adaptHolder( Holder gdmHolder )
	{
		com.orangecaraibe.enabler.crm.business.bean.Holder businessHolder = null;
		if ( gdmHolder != null && gdmHolder.getParty() != null )
		{
			businessHolder = new com.orangecaraibe.enabler.crm.business.bean.Holder();
			if ( StringUtils.isNotEmpty( gdmHolder.getParty().getPartyID() ) )
			{
				businessHolder.setGuid( gdmHolder.getParty().getPartyID() );
			}

			if ( gdmHolder.getParty().getPartyName() instanceof PersonName )
			{
				PersonName name = (PersonName) gdmHolder.getParty().getPartyName();
				if ( StringUtils.isNotEmpty( name.getLastName() ) )
				{
					businessHolder.setLastName( name.getLastName() );
				}
				if ( StringUtils.isNotEmpty( name.getFirstName() ) )
				{
					businessHolder.setFirstName( name.getFirstName() );
				}
				if ( salutationMap.containsKey( name.getSalutation() ) )
				{
					businessHolder.setTitle( salutationMap.get( name.getSalutation() ) );
				}
			}
		}

		return businessHolder;
	}

	/**
	 * Adapts business holder into a GDM (SOA) holder
	 * 
	 * @param businessHolder
	 * @return
	 */
	private Holder adaptBusinessHolder( com.orangecaraibe.enabler.crm.business.bean.Holder businessHolder )
	{
		Holder gdmHolder = null;
		if ( businessHolder != null )
		{
			gdmHolder = new Holder();
			Person person = new Person();
			gdmHolder.setParty( person );
			// person.getPartyRoles().add( gdmHolder );
			if ( StringUtils.isNotEmpty( businessHolder.getGuid() ) )
			{
				person.setPartyID( businessHolder.getGuid() );
			}
			if ( StringUtils.isNotEmpty( businessHolder.getLastName() ) )
			{
				PersonName personName = new PersonName();
				personName.setLastName( businessHolder.getLastName() );
				person.setPartyName( personName );
			}
		}
		return gdmHolder;
	}

	/**
	 * Adapts GDM (SOA) comments into a business notes
	 * 
	 * @param comments
	 * @return
	 */
	private List<Note> adaptNotes( List<LocalComment> comments )
	{
		List<Note> notes = null;
		if ( CollectionUtils.isNotEmpty( comments ) )
		{
			for ( LocalComment comment : comments )
			{
				Note note = adaptNote( comment );
				if ( notes == null )
				{
					notes = new ArrayList<Note>();
				}
				notes.add( note );
			}
		}
		return notes;

	}

	/**
	 * Adapts GDM (SOA) comment into a business note
	 * 
	 * @param comment
	 * @return
	 */
	private Note adaptNote( LocalComment comment )
	{
		Note note = new Note();
		note.setNote( comment.getLabel() );
		if ( comment.getCommentDate() != null )
		{
			note.setDateCreation( DateUtils.getDateFromXmlGregorianCalendar( comment.getCommentDate() ) );
		}

		if ( comment.getPartyRole() != null && comment.getPartyRole().getParty() != null
			&& !StringUtils.isNotEmpty( comment.getPartyRole().getParty().getPartyID() ) )
		{
			note.setGuid( comment.getPartyRole().getParty().getPartyID() );
		}

		return note;
	}

	/**
	 * Adapts business note into a GDM (SOA) comment
	 * 
	 * @param note
	 * @return
	 */
	private LocalComment adaptBusinessNote( Note note )
	{
		LocalComment gdmComment = null;
		if ( note != null )
		{
			gdmComment = new LocalComment();
			if ( StringUtils.isNotEmpty( note.getSubject() ) )
			{
				gdmComment.setSubject( note.getSubject() );
			}
			if ( StringUtils.isNotEmpty( note.getNote() ) )
			{
				gdmComment.setLabel( note.getNote() );
			}
			if ( note.getDateCreation() != null )
			{
				gdmComment.setCommentDate( DateUtils.convertDateToXMLGregorianCalendar( note.getDateCreation() ) );
			}
			if ( StringUtils.isNotEmpty( note.getGuid() ) )
			{
				gdmComment.setCode( note.getGuid() );
			}
			if ( StringUtils.isNotEmpty( note.getUserGuid() ) || StringUtils.isNotEmpty( note.getUserName() ) )
			{
				CustomerServiceRepresentative csr = new CustomerServiceRepresentative();
				Person person = new Person();
				csr.setParty( person );
				gdmComment.setPartyRole( csr );
				if ( StringUtils.isNotEmpty( note.getUserGuid() ) )
				{
					person.setPartyID( note.getUserGuid() );
				}
				if ( StringUtils.isNotEmpty( note.getUserName() ) )
				{
					PersonName personName = new PersonName();
					personName.setLastName( note.getUserName() );
					person.setPartyName( personName );
				}
			}
		}

		return gdmComment;
	}

	/**
	 * Adapts GDM (SOA) attachments into business attachments
	 * 
	 * @param gdmAttachments
	 * @return
	 * @throws IOException
	 */
	private List<Attachment> adaptGdmAttachments( List<InteractionAttachmentImage> gdmAttachments )
		throws IOException
	{
		List<Attachment> businessAttachments = null;
		if ( CollectionUtils.isNotEmpty( gdmAttachments ) )
		{
			businessAttachments = new ArrayList<Attachment>();
			for ( InteractionAttachmentImage gdmAttachement : gdmAttachments )
			{
				Attachment businessAttachment = adaptGdmAttachment( gdmAttachement );
				businessAttachments.add( businessAttachment );
			}
		}
		return businessAttachments;

	}

	/**
	 * Adapts GDM (SOA) attachment into a business attachment
	 * 
	 * @param gdmAttachment
	 * @return
	 * @throws IOException
	 */
	private Attachment adaptGdmAttachment( InteractionAttachmentImage gdmAttachment )
		throws IOException
	{
		Attachment businessAttachment = new Attachment();
		businessAttachment.setNom( gdmAttachment.getImageName() );
		businessAttachment.setType( gdmAttachment.getImageMimeType() );

		String stringToWrite = null;
		try
		{
			// encodage de la piece jointe en base 64
			stringToWrite =
				Base64.encodeBase64String( IOUtils.toByteArray( gdmAttachment.getBinaryData().getInputStream() ) );
		}
		catch ( IOException e )
		{
			LOGGER.error( "IOException lors de l'encodage de l'attachement en base 64", e );
			throw e;
		}
		businessAttachment.setDocument( stringToWrite );

		if ( CollectionUtils.isNotEmpty( gdmAttachment.getLocalComment() ) )
		{
			// adaptation du commentaire associe a l'attachement
			Note businessNote = adaptNote( gdmAttachment.getLocalComment().get( 0 ) );
			businessAttachment.setNote( businessNote );
		}

		return businessAttachment;
	}

	/**
	 * Adapts business attachments into GDM (SOA) attachments
	 * 
	 * @param attachments
	 * @return
	 * @throws IOException
	 */
	private List<InteractionAttachmentImage> adaptBusinessAttachments( List<Attachment> attachments )
	{
		List<InteractionAttachmentImage> interactionAttachmentImages = new ArrayList<InteractionAttachmentImage>();
		for ( Attachment attachment : attachments )
		{
			InteractionAttachmentImage interactionAttachmentImage = adaptBusinessAttachment( attachment );
			interactionAttachmentImages.add( interactionAttachmentImage );
		}
		return interactionAttachmentImages;
	}

	/**
	 * Adapts business attachment into a GDM (SOA) attachment
	 * 
	 * @param attachment
	 * @return
	 * @throws IOException
	 */
	private InteractionAttachmentImage adaptBusinessAttachment( Attachment attachment )
	{
		// adaptation de l'attachement
		InteractionAttachmentImage interactionAttachmentImage = new InteractionAttachmentImage();
		interactionAttachmentImage.setImageName( attachment.getNom() );
		interactionAttachmentImage.setImageMimeType( attachment.getType() );

		// decodage de la piece jointe et passage sous forme de flux binary data
		byte[] documentAsBytes = Base64.decodeBase64( attachment.getDocument() );
		interactionAttachmentImage.setImageSize( Long.valueOf( documentAsBytes.length ) );
		DataSource dataSource = new ByteArrayDataSource( documentAsBytes, attachment.getType() );
		DataHandler dataHandler = new DataHandler( dataSource );
		interactionAttachmentImage.setBinaryData( dataHandler );

		if ( attachment.getNote() != null )
		{
			// adaptation du commentaire associe a l'attachement
			LocalComment gdmComment = adaptBusinessNote( attachment.getNote() );
			interactionAttachmentImage.getLocalComment().add( gdmComment );
		}

		return interactionAttachmentImage;
	}

	/**
	 * Adapts a GDM (SOA) customer service representative into a business customer service
	 * 
	 * @param csr
	 * @return
	 */
	protected CustomerService adaptCustomerService( PartyRole csr )
	{
		CustomerService customerService = null;
		if ( csr != null )
		{
			if ( csr.getParty() != null )
			{
				// party de type Person
				if ( csr.getParty() instanceof Person )
				{
					customerService = new CustomerServicePerson();
					// nom du conseiller
					if ( csr.getParty().getPartyName() instanceof PersonName )
					{
						PersonName gdmName = (PersonName) csr.getParty().getPartyName();
						if ( StringUtils.isNotEmpty( gdmName.getLastName() )
							|| StringUtils.isNotEmpty( gdmName.getFirstName() ) )
						{
							// DCRM ne distingue pas le nom et le prenom
							String name = StringUtils.EMPTY;
							if ( StringUtils.isNotEmpty( gdmName.getFirstName() ) )
							{
								name += gdmName.getFirstName();
							}
							if ( StringUtils.isNotEmpty( gdmName.getLastName() ) )
							{
								name += gdmName.getLastName();
							}
							customerService.setName( name );
						}
					}

					if ( csr.getUserIdentity() != null && StringUtils.isNotEmpty( csr.getUserIdentity().getGID() ) )
					{
						( (CustomerServicePerson) customerService ).setLogin( csr.getUserIdentity().getGID() );
					}

				}
				// party de type Organisation
				else if ( csr.getParty() instanceof Organisation
					|| CollectionUtils.isNotEmpty( csr.getPartyRoleSet() ) )
				{
					customerService = new CustomerServiceOrganisation();
					if ( csr.getParty().getPartyName() instanceof OrganisationName )
					{
						OrganisationName name = (OrganisationName) csr.getParty().getPartyName();
						if ( StringUtils.isNotEmpty( name.getTradingName() ) )
						{
							( (CustomerServiceOrganisation) customerService ).setName( name.getTradingName() );
						}
					}
					if ( CollectionUtils.isNotEmpty( csr.getPartyRoleSet() ) )
					{
						PartyRoleSet partyRoleSet = csr.getPartyRoleSet().get( 0 );
						// si le guid n'a pas deja ete renseigne, on le renseigne avec l'ID de l'equipe
						if ( StringUtils.isNotEmpty( partyRoleSet.getPartyRoleSetID() )
							&& StringUtils.isEmpty( customerService.getGuid() ) )
						{
							customerService.setGuid( partyRoleSet.getPartyRoleSetID() );
						}
						// si le nom de l'organisatio n'a pas ete renseigne, on le renseigne avec le nom de l'equipe
						String organisationName = ( (CustomerServiceOrganisation) customerService ).getName();
						if ( StringUtils.isNotEmpty( partyRoleSet.getPartyRoleSetLabel() )
							&& StringUtils.isEmpty( organisationName ) )
						{
							( (CustomerServiceOrganisation) customerService ).setName( partyRoleSet.getPartyRoleSetLabel() );
						}
					}
				}
				// party non specialise
				// else
				// {
				// customerService = new CustomerService();
				// customerService.setGuid( );
				// }

				// si le guid n'a pas ete renseigne, on le renseigne avec le partyID
				if ( StringUtils.isNotEmpty( csr.getParty().getPartyID() )
					&& StringUtils.isEmpty( customerService.getGuid() ) )
				{
					customerService.setGuid( csr.getParty().getPartyID() );
				}
			}
		}

		return customerService;

	}

	/**
	 * Adapts a GDM (SOA) interactionway into a business interaction way
	 * 
	 * @param gdmInteractionWay
	 * @return
	 */
	private EnumInteractionWay adaptGdmInteractionWay( InteractionWay gdmInteractionWay )
	{
		if ( gdmInteractionWay == InteractionWay.IN )
		{
			return EnumInteractionWay.INCOMING;
		}
		else if ( gdmInteractionWay == InteractionWay.OUT )
		{
			return EnumInteractionWay.OUTGOING;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Adapts a GDM (SOA) interactionway into a business interaction way
	 * 
	 * @param gdmInteractionWay
	 * @return
	 */
	private InteractionWay adaptBusinessInteractionWay( EnumInteractionWay interactionWay )
	{
		if ( interactionWay == EnumInteractionWay.INCOMING )
		{
			return InteractionWay.IN;
		}
		else if ( interactionWay == EnumInteractionWay.OUTGOING )
		{
			return InteractionWay.OUT;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Adapts business actors into (SOA) GDM party roles
	 * 
	 * @param businessPartyList
	 * @param gdmHolder
	 * @param application
	 * @return
	 */
	private List<PartyRole> adaptBusinessActorsToGdmRoles(	List<Actor> businessPartyList, Holder gdmHolder,
															String application )
	{

		List<PartyRole> gdmPartyRoles = new ArrayList<PartyRole>();
		for ( Actor party : businessPartyList )
		{
			PartyRole gdmPartyRole = adaptBusinessPartyToGdmRole( party, gdmHolder, application );
			if ( gdmPartyRole != null )
			{
				gdmPartyRoles.add( gdmPartyRole );
			}
		}

		return gdmPartyRoles;

	}

	/**
	 * Adapts contact media for a specific channel into (SOA) GDM contact methods
	 * 
	 * @param businessActors
	 * @param channel
	 * @return
	 */
	private List<ContactMethod> adaptBusinessPartyListToContactMethods( List<Actor> businessActors,
																		EnumInteractionChannel channel )
	{

		List<ContactMethod> contactMethods = new ArrayList<ContactMethod>();
		for ( Actor businessActor : businessActors )
		{
			ContactMethod contactMethod = adaptBusinessPartyToContactMethod( businessActor, channel );
			if ( contactMethod != null )
			{
				contactMethods.add( contactMethod );
			}
		}

		return contactMethods;

	}

	/**
	 * Adapts a business actor into a (SOA) GDM party role
	 * 
	 * @param businessActor
	 * @param gdmHolder
	 * @param application
	 * @return
	 */
	private PartyRole adaptBusinessPartyToGdmRole( Actor businessActor, Holder gdmHolder, String application )
	{
		/*
		 * party
		 */
		com.orangecaraibe.soa.v2.model.commonbusiness.party.Party gdmParty = null;

		// variable indiquant si l'acteur traite est le titulaire
		boolean actorIsHolder = false;

		// GUID du party
		if ( StringUtils.isNotEmpty( businessActor.getGuid() ) )
		{
			// on tente de rapprocher l'acteur du holder passe en entree en comparant le GUID
			if ( gdmHolder != null && gdmHolder.getParty() != null && gdmHolder.getParty().getPartyID() != null
				&& gdmHolder.getParty().getPartyID().equals( businessActor.getGuid() ) )
			{
				// l'acteur est le titulaire, on ne va donc pas le "creer" une 2eme fois
				actorIsHolder = true;
			}
			else
			{
				// l'acteur n'est pas le Holder, on cree le party
				if ( gdmParty == null )
				{
					// TODO : comment distinguer le cas Person de Organisation
					gdmParty = new com.orangecaraibe.soa.v2.model.commonbusiness.party.Party();
				}
				gdmParty.setPartyID( businessActor.getGuid() );
			}
		}

		// Nom du party
		if ( !actorIsHolder && StringUtils.isNotEmpty( businessActor.getName() ) )
		{
			if ( gdmParty == null )
			{
				gdmParty = new com.orangecaraibe.soa.v2.model.commonbusiness.party.Party();
			}
			// TODO : comment distinguer le cas Person de Organisation
			PersonName gdmPartyName = new PersonName();
			gdmPartyName.setLastName( businessActor.getName() );
			gdmParty.setPartyName( gdmPartyName );
		}

		/*
		 * role du party
		 */
		PartyRole gdmPartyRole = null;

		if ( businessActor instanceof CustomerService )
		{
			// conseiller client ou equipe
			if ( StringUtils.isNotEmpty( application ) )
			{
				// application definie sur le channelID du role Distributor
				gdmPartyRole = new Distributor();
				( (Distributor) gdmPartyRole ).setChannelID( application );
			}
			else
			{
				gdmPartyRole = new CustomerServiceRepresentative();
			}
			if ( businessActor instanceof CustomerServicePerson )
			{
				if ( StringUtils.isNotEmpty( ( (CustomerServicePerson) businessActor ).getLogin() ) )
				{
					UserIdentity gdmUserIdentity = new UserIdentity();
					gdmUserIdentity.setGID( ( (CustomerServicePerson) businessActor ).getLogin() );
				}
			}
		}
		else if ( !actorIsHolder )
		{
			// Le party est un client, et il n'a pas deja ete traite en tant que titulaire,
			// on lui attribue le role Customer.
			// note : si une information plus precise remontait du DCRM, on pourrait a l'avenir preciser plus finement
			// le role du client, User, Payer, BillingAccountManager, etc.
			gdmPartyRole = new Customer();
		}

		/*
		 * Association du party au role
		 */
		if ( gdmParty != null && gdmPartyRole != null )
		{
			// on rattache le party au role
			gdmPartyRole.setParty( gdmParty );
		}

		return gdmPartyRole;

	}

	/**
	 * Adapts a contact medium for a specific channel into a (SOA) GDM contact method
	 * 
	 * @param businessActor
	 * @param channel
	 * @return
	 */
	private ContactMethod adaptBusinessPartyToContactMethod( Actor businessActor, EnumInteractionChannel channel )
	{

		ContactMethod gdmContactMethod = null;
		com.orangecaraibe.soa.v2.model.commonbusiness.party.Party gdmParty =
			new com.orangecaraibe.soa.v2.model.commonbusiness.party.Party();
		if ( businessActor.getContactMethods() != null )
		{
			if ( channel == EnumInteractionChannel.EMAIL
				&& StringUtils.isNotEmpty( businessActor.getContactMethods().getEmail() ) )
			{
				gdmContactMethod = new EmailAddress();
				( (EmailAddress) gdmContactMethod ).setEMailAddress( businessActor.getContactMethods().getEmail() );
			}
			else if ( channel == EnumInteractionChannel.PHONE_CALL
				&& StringUtils.isNotEmpty( businessActor.getContactMethods().getMobilePhone() ) )
			{
				gdmContactMethod = new PhoneNumber();
				( (PhoneNumber) gdmContactMethod ).setNumber( businessActor.getContactMethods().getMobilePhone() );
			}

			if ( gdmContactMethod != null && StringUtils.isNotEmpty( businessActor.getGuid() ) )
			{
				gdmParty.setPartyID( businessActor.getGuid() );
				gdmContactMethod.setParty( gdmParty );
			}
		}

		return gdmContactMethod;

	}

	/**
	 * Adapts a business customer service representative into a (SOA) GDM customer service representative
	 * 
	 * @param customerService
	 * @return
	 */
	private CustomerServiceRepresentative adaptBusinessCsrToGdmCsr( CustomerService customerService )
	{
		CustomerServiceRepresentative gdmCSR = new CustomerServiceRepresentative();
		// cas ou le proprietaire est un utilisateur (conseiller)
		if ( customerService instanceof CustomerServicePerson )
		{
			UserIdentity userIdentity = new UserIdentity();
			userIdentity.setGID( ( (CustomerServicePerson) customerService ).getLogin() );
			gdmCSR.setUserIdentity( userIdentity );

			Person gdmCsrPerson = new Person();
			gdmCSR.setParty( gdmCsrPerson );
			if ( StringUtils.isNotEmpty( ( (CustomerServicePerson) customerService ).getName() ) )
			{
				PersonName gdmCsrPersonName = new PersonName();
				gdmCsrPersonName.setLastName( ( (CustomerServicePerson) customerService ).getName() );
				gdmCsrPerson.setPartyName( gdmCsrPersonName );
			}

			if ( StringUtils.isNotEmpty( customerService.getGuid() ) )
			{
				gdmCsrPerson.setPartyID( customerService.getGuid() );
			}

		}
		// cas ou le proprietaire est une equipe
		else if ( customerService instanceof CustomerServiceOrganisation )
		{
			PartyRoleSet partyRoleSet = new PartyRoleSet();
			if ( StringUtils.isNotEmpty( ( (CustomerServiceOrganisation) customerService ).getName() ) )
			{
				partyRoleSet.setPartyRoleSetLabel( ( (CustomerServiceOrganisation) customerService ).getName() );
			}

			if ( StringUtils.isNotEmpty( customerService.getGuid() ) )
			{
				partyRoleSet.setPartyRoleSetID( customerService.getGuid() );
			}

		}
		// cas ou l'on ne dispose pas de detail sur le proprietaire
		else
		{
			gdmCSR = new CustomerServiceRepresentative();
			if ( StringUtils.isNotEmpty( customerService.getGuid() ) )
			{
				com.orangecaraibe.soa.v2.model.commonbusiness.party.Party gdmCsrParty =
					new com.orangecaraibe.soa.v2.model.commonbusiness.party.Party();
				gdmCsrParty.setPartyID( customerService.getGuid() );

			}
		}

		// on preciser que le CustomerServiceRepresentative correspond au Proprietaire en lui ajoutant un partyRoleType
		// "Owner"
		gdmCSR.setDescription( "Propriétaire de l'interaction" );
		PartyRoleType partyRoleType = new PartyRoleType();
		partyRoleType.setPartyRoleTypeID( "Owner" );
		gdmCSR.setPartyRoleType( partyRoleType );

		return gdmCSR;

	}

	/**
	 * Adapts (SOA) GDM interaction actors to business actors
	 * 
	 * @param actors
	 * @return
	 * @throws Exception
	 */
	private List<Actor> adaptGdmPartyRolesToBusinessActors( List<InteractionActor> actors )
		throws Exception
	{
		List<Actor> businessPartyList = null;
		if ( CollectionUtils.isNotEmpty( actors ) )
		{
			businessPartyList = new ArrayList<Actor>();
			for ( InteractionActor interactionActor : actors )
			{
				if ( !( interactionActor instanceof PartyRole ) )
				{
					throw new Exception( "l'acteur de l'interaction doit etre specialise a minima en PartyRole generique ou si possible en une specialisation de PartyRole precisant le role du Party" );
				}
				else if ( interactionActor instanceof CustomerServiceRepresentative
					|| interactionActor instanceof Distributor )
				{
					PartyRole partyRole = (PartyRole) interactionActor;
					if ( partyRole.getParty() != null && StringUtils.isNotEmpty( partyRole.getParty().getPartyID() ) )
					{
						CustomerService businessCustomerService = new CustomerService();
						businessCustomerService.setGuid( partyRole.getParty().getPartyID() );
						businessPartyList.add( businessCustomerService );
					}
				}
				else if ( !( interactionActor instanceof Holder ) )
				{
					PartyRole partyRole = (PartyRole) interactionActor;
					if ( partyRole.getParty() != null && StringUtils.isNotEmpty( partyRole.getParty().getPartyID() ) )
					{
						Party businessParty = new Party();
						businessParty.setGuid( partyRole.getParty().getPartyID() );
						businessPartyList.add( businessParty );
					}
				}
			}
		}

		return businessPartyList;

	}

	/**
	 * Adapts business requests to (SOA) GDM customer problems
	 * 
	 * @param businessRequests
	 * @return
	 */
	private List<CustomerProblem> adaptBusinessRequestsToCustomerProblems( List<Request> businessRequests )
	{
		List<CustomerProblem> customerProblems = new ArrayList<CustomerProblem>();
		for ( Request businessRequest : businessRequests )
		{
			CustomerProblem customerProblem = customerProblemAdapter.adaptRequestToCustomerProblem( businessRequest );
			customerProblems.add( customerProblem );
		}
		return customerProblems;

	}

	public void setCustomerProblemAdapter( CustomerProblemAdapter customerProblemAdapter )
	{
		this.customerProblemAdapter = customerProblemAdapter;
	}

	public void setLinkCrmBscsHelper( LinkCrmBscsHelper linkCrmBscsHelper )
	{
		this.linkCrmBscsHelper = linkCrmBscsHelper;
	}

	public List<com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction> adapteMarketinCampaign( List<Campagne> campagnes )
	{
		List<com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction> interactions =
			new ArrayList<com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction>();

		if ( CollectionUtils.isNotEmpty( campagnes ) )
		{
			for ( Campagne campagne : campagnes )
			{

				MarketingCampaign marketingCampaign = new MarketingCampaign();

				marketingCampaign.setDescription( campagne.getDescription() );
				marketingCampaign.setInteractionDate( DateUtils.convertDateToXMLGregorianCalendar( campagne.getStartDate() ) );
				marketingCampaign.setInteractionDateCompleted( DateUtils.convertDateToXMLGregorianCalendar( campagne.getEndDate() ) );
				Notification interactionRole = new Notification();
				interactionRole.setLocalCategory( campagne.getType().getGdmValue() );
				marketingCampaign.setInteractionRole( interactionRole );
				if ( StringUtils.isNotEmpty( campagne.getOfferCode() ) )
				{
					OfferSpecification offerSpecification = new OfferSpecification();
					offerSpecification.setOfferSpecificationCode( campagne.getOfferCode() );
					marketingCampaign.getInteractionSubject().add( offerSpecification );

				}
				if ( campagne.getInteractions() != null )
				{
					for ( Interaction inter : campagne.getInteractions() )
					{
						marketingCampaign.getReferences().add( adaptBusinessInteraction( inter ) );
					}
				}
				interactions.add( marketingCampaign );
			}
		}
		return interactions;
	}
}
