package com.orangecaraibe.enabler.crm.business.adapter;

import com.orangecaraibe.enabler.core.catalogue.CatalogueHelper;
import com.orangecaraibe.enabler.core.utils.Constantes;
import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.crm.business.bean.*;
import com.orangecaraibe.enabler.crm.business.bean.enums.*;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;
import com.orangecaraibe.enabler.crm.business.helper.LinkCrmBscsHelper;
import com.orangecaraibe.enabler.crm.dao.bean.LinkCrmBscsBean;
import com.orangecaraibe.enabler.crm.enums.ThemesCRMEnum;
import com.orangecaraibe.enabler.crm.webservice.criteria.WebservicesConstantes;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.CreateException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.schemas.CreateFault;
import com.orangecaraibe.soa.v2.model.commonbusiness.agreement.FrameworkAgreement;
import com.orangecaraibe.soa.v2.model.commonbusiness.identity.UserIdentity;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionSubject;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.LocalComment;
import com.orangecaraibe.soa.v2.model.commonbusiness.location.Site;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Party;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.*;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.EmailAddress;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.PhoneNumber;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.PostalAddress;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.paymentmeans.BankAccount;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.paymentmeans.DirectDebit;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.paymentmeans.PaymentMean;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.CustomerServiceRepresentative;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.Operator;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.PartyRole;
import com.orangecaraibe.soa.v2.model.commonbusiness.troubleticket.TroubleTicket;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccount;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.payment.Refund;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.payment.*;
import com.orangecaraibe.soa.v2.model.customerdomain.customerorder.CustomerOrder;
import com.orangecaraibe.soa.v2.model.customerdomain.customerorder.CustomerOrderItem;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.ResolutionAction;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.*;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.CommercialClassification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.OfferSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.SaleChannel;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.TariffSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledAtomicOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPublicKey;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionValueSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.ProductSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledFunctionValue;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledProduct;
import com.orangecaraibe.soa.v2.model.resourcedomain.resourcecatalogue.ResourceSpecification;
import com.orangecaraibe.soa.v2.model.resourcedomain.resourceinstalledbase.InstalledResource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * The Class CustomerProblemAdapter.
 */
@Component
public class CustomerProblemAdapter {

	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerProblemAdapter.class);

	private static final String THEME_RESILIATION = "résiliation";

	/** helper pour les offres du catalogue */
	@Autowired
	@Qualifier("crm.catalogueHelper")
	private CatalogueHelper catalogueHelper;

	@Autowired
	private LinkCrmBscsHelper linkCrmBscsHelper;

	@Autowired
	private InteractionAdapter interactionAdapter;

	/**
	 * Adapt find and get reason specifications.
	 *
	 * @param crmReasonSpecList
	 * 		the crm reason spec list
	 * @return the list<local reason specification>
	 */
	public List<LocalReasonSpecification> adaptFindAndGetReasonSpec(List<CustomerProblemReason> crmReasonSpecList) {

		List<LocalReasonSpecification> result = new ArrayList<LocalReasonSpecification>();
		for (CustomerProblemReason childCrmReason : crmReasonSpecList) {
			LocalReasonSpecification reasonSpec = getLocalReasonSpecification(childCrmReason);
			recurseChildren(childCrmReason, reasonSpec);
			result.add(reasonSpec);
		}
		return result;
	}

	/**
	 * Recurse children.
	 *
	 * @param crmReason
	 * 		the crm reason
	 * @param reasonSpec
	 * 		the reason spec
	 */
	private void recurseChildren(CustomerProblemReason crmReason, LocalReasonSpecification reasonSpec) {

		for (Iterator<CustomerProblemReason> iterator = crmReason.getCustomerProblemReason()
				.iterator(); iterator.hasNext(); ) {
			CustomerProblemReason child = iterator.next();
			LocalReasonSpecification childReasonSpec = getLocalReasonSpecification(child);
			reasonSpec.getChildrenReasonSpecifcations().add(childReasonSpec);
			recurseChildren(child, childReasonSpec);
		}
	}

	/**
	 * Gets the local reason specification.
	 *
	 * @param crmReason
	 * 		the crm reason
	 * @return the local reason specification
	 */
	private LocalReasonSpecification getLocalReasonSpecification(CustomerProblemReason crmReason) {

		LocalReasonSpecification reasonSpec = new LocalReasonSpecification();
		reasonSpec.setReasonSpecificationCode(crmReason.getGuid());
		reasonSpec.setReasonSpecificationLabel(crmReason.getDescription());
		if (crmReason.getCategorie() != null) {
			reasonSpec.setLocalReasonSpecificationType(crmReason.getCategorie().getSataType());
		}
		if (StringUtils.isNotEmpty(crmReason.getUrl())) {
			reasonSpec.setReasonSpecificationURL(crmReason.getUrl());
		}
		return reasonSpec;
	}

	/**
	 * Request adapter depuis un CustomerProblem
	 *
	 * @return Request
	 */
	public Request adapteCreate(CustomerProblem customerProblem) throws IOException, CreateException {

		Request request = new Request();
		// RUNO-4557 on ajoute la date depuis le champ interactionDate
		if (customerProblem.getInteractionDate() != null) {
			Date interactionDate = customerProblem.getInteractionDate().toGregorianCalendar().getTime();
			request.setCreationDate(interactionDate);
		} else if (customerProblem.getDateCreation() != null) {
			Date dateCreation = customerProblem.getDateCreation().toGregorianCalendar().getTime();
			request.setCreationDate(dateCreation);
		}
		request.setTitle(customerProblem.getLocalTitle());
		if (CollectionUtils.isNotEmpty(customerProblem.getLocalCommercialClassifications())) {
			if (customerProblem.getLocalCommercialClassifications().get(0) instanceof SaleChannel) {
				request.setChannel(customerProblem.getLocalCommercialClassifications().get(0).getLabel());
			}
		}
		request.setDescription(customerProblem.getDescription());
		request.setActor(
				adaptCustomerServiceRepresentativesToActor(customerProblem.getCustomerServiceRepresentative()));
		try {
			request = adaptStatusAndReason(request, customerProblem);
		}
		catch (CustomerProblemUpdateException e) {
			LOGGER.error("CustomerProblemUpdateException occurred while adapting status and reason", e);
			CreateFault cFault = new CreateFault();
			cFault.setCode("CUSP014");
			cFault.setLabel(e.getMessage());
			throw new CreateException("customerProblem is null", cFault);
		}
		request.setLastNote(customerProblem.getDescription());
		if (customerProblem.getLocalPriority() != null) {
			request.setPriority(EnumPriority.valueOf(customerProblem.getLocalPriority().value()));
		}
		request.setKeyWord(customerProblem.getLocalKeyword());
		Theme theme1 = new Theme();
		Theme theme2 = new Theme();
		Theme theme3 = new Theme();
		Reason reason = null;
		if (customerProblem.getReason() != null) {
			reason = customerProblem.getReason();
			String[] themeGuid = null;
			if (reason.getLocalReasonSpecification() != null) {
				themeGuid = reason.getLocalReasonSpecification().getReasonSpecificationCode()
						.split(Constantes.REGEX_GUID_THEME);
			}
			if (themeGuid != null && themeGuid.length == 3) {
				theme3.setGuid(themeGuid[2]);
				theme2.setGuid(themeGuid[1]);
				theme1.setGuid(themeGuid[0]);
			} else if (themeGuid != null && themeGuid.length == 2) {
				theme2.setGuid(themeGuid[1]);
				theme1.setGuid(themeGuid[0]);
			} else if (themeGuid != null && themeGuid.length == 1) {
				theme1.setGuid(themeGuid[0]);
			}
			theme3.setLabel(reason.getSubReason());
			theme2.setLabel(reason.getReason());
			theme2.getTheme().add(theme3);
			theme1.setLabel(reason.getMainReason());
			theme1.getTheme().add(theme2);
		}
		request.setTheme(theme1);
		if (CollectionUtils.isNotEmpty(customerProblem.getInteraction())) {
			com.orangecaraibe.enabler.crm.business.bean.Interaction interaction = new com.orangecaraibe.enabler.crm.business.bean.Interaction();
			com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction interactionOrigin = customerProblem.getInteraction()
					.get(0);
			interaction.setId(interactionOrigin.getInteractionID());
			request.getInteractions().add(interaction);
		}
		List<InteractionSubject> interactionSubject = customerProblem.getInteractionSubject();
		request = adaptInteractionSubjects(request, interactionSubject, reason, false);
		// le holder ou le custcode n'est pas renseigne
		if (request.getAccount() != null && request.getAccount()
				.getAccountId() != null && (request.getHolder() == null || request.getAccount()
				.getCustCode() == null)) {
			// on tente de retrouver son GUID a partir du billingAccountID dans la table de correspondance BSCS CRM
			LinkCrmBscsBean linkCrmBscsBean = linkCrmBscsHelper.getCrmGuidByCustomerId(
					request.getAccount().getAccountId());
			if (linkCrmBscsBean != null) {
				if (request.getHolder() == null) {
					Holder businessHolder = linkCrmBscsHelper.getHolderByCustomerId(linkCrmBscsBean);
					if (businessHolder != null) {
						request.setHolder(businessHolder);
					}
				} else {
					String crmGuid = linkCrmBscsBean.getCrmGuid();
					if (StringUtils.isNotEmpty(crmGuid)) {
						request.getHolder().setGuid(crmGuid);
					}
				}
				if (request.getAccount().getCustCode() == null && linkCrmBscsBean.getCustcode() != null) {
					request.getAccount().setCustCode(linkCrmBscsBean.getCustcode());
				}
			} else {
				LOGGER.info(
						"Le customer_id {} n'a pas été trouvé dans la table de lien. Utilisation du party par defaut (à renseigner) ",
						request.getAccount().getAccountId());
			}
		}
		if (CollectionUtils.isNotEmpty(customerProblem.getCustomerInterlocutor())) {
			if (customerProblem.getCustomerInterlocutor().get(0).getMayBeContactedUsing() instanceof EmailAddress) {
				request.setCommunicationChannel(EnumCommunicationChannel.EMAIL);
			}
			if (customerProblem.getCustomerInterlocutor().get(0).getMayBeContactedUsing() instanceof PhoneNumber) {
				request.setCommunicationChannel(EnumCommunicationChannel.SMS);
			}
			if (customerProblem.getCustomerInterlocutor().get(0).getMayBeContactedUsing() instanceof PostalAddress) {
				request.setCommunicationChannel(EnumCommunicationChannel.COURRIER);
			}
		}
		if (CollectionUtils.isNotEmpty(customerProblem.getCFSTroubleTicket())) {
			TroubleTicket troubleTicket = customerProblem.getCFSTroubleTicket().get(0);
			request.setTroubleTicketID(troubleTicket.getTroubleTicketID());
		}
		// adaptation du conseiller/equipe en charge
		if (CollectionUtils.isNotEmpty(customerProblem.getCustomerServiceRepresentative())) {
			CustomerServiceRepresentative csr = customerProblem.getCustomerServiceRepresentative().get(0);
			// TODO - definir un adapter mutualise pour les elements commun a CustomerProblem et Interaction
			CustomerService customerService = interactionAdapter.adaptCustomerService(csr);
			if (customerService != null) {
				request.setCustomerService(customerService);
			}
		}
		if (CollectionUtils.isNotEmpty(customerProblem.getLocalCommercialClassifications())) {
			if (customerProblem.getLocalCommercialClassifications().get(0) instanceof SaleChannel) {
				request.setChannel(customerProblem.getLocalCommercialClassifications().get(0).getLabel());
			}
		}
		if (CollectionUtils.isNotEmpty(customerProblem.getLocalComment())) {
			List<Note> notes = new ArrayList<Note>();
			for (LocalComment localComment : customerProblem.getLocalComment()) {
				Note note = new Note();
				note.setNote(localComment.getLabel());
				notes.add(note);
			}
			request.getNotes().addAll(notes);
		}
		if (CollectionUtils.isNotEmpty(customerProblem.getLocalCustomerProblemImages())) {
			List<Attachment> attachments = new ArrayList<Attachment>();
			for (LocalCustomerProblemImage localCustomerProblemImage : customerProblem.getLocalCustomerProblemImages()) {
				Attachment attachment = new Attachment();
				attachment.setNom(localCustomerProblemImage.getImageName());
				attachment.setType(localCustomerProblemImage.getImageMimeType());
				String stringToWrite = null;
				if (localCustomerProblemImage.getBinaryData() != null) {
					try {
						// encodage de la piece jointe en base 64
						stringToWrite = Base64.encodeBase64String(
								IOUtils.toByteArray(localCustomerProblemImage.getBinaryData().getInputStream()));
					}
					catch (IOException e) {
						LOGGER.error("IOException lors de l'encodage de l'attachement en base 64", e);
						throw e;
					}
				}
				attachment.setDocument(stringToWrite);
				attachments.add(attachment);
			}
			request.getAttachments().addAll(attachments);
		}
		return request;
	}

	private Actor adaptCustomerServiceRepresentativesToActor(
			List<CustomerServiceRepresentative> CustomerServiceRepresentatives) {

		Actor actor = null;
		for (CustomerServiceRepresentative customerServiceRepresentative : CustomerServiceRepresentatives) {
			UserIdentity userIdentity = customerServiceRepresentative.getUserIdentity();
			if (customerServiceRepresentative != null && customerServiceRepresentative.getParty() != null && customerServiceRepresentative.getParty()
					.getPartyName() != null && customerServiceRepresentative.getParty()
					.getPartyName() instanceof PersonName) {
				PersonName personName = (PersonName) customerServiceRepresentative.getParty().getPartyName();
				actor = new Actor();
				actor.setGuid(userIdentity.getGID());
				actor.setName(personName.getFirstName() + " " + personName.getLastName());
			}
		}
		return actor;
	}

	private Actor adaptCustomerServiceRepresentativesToActorLogin(
			List<CustomerServiceRepresentative> CustomerServiceRepresentatives) {

		Actor actor = null;
		for (CustomerServiceRepresentative customerServiceRepresentative : CustomerServiceRepresentatives) {
			UserIdentity userIdentity = customerServiceRepresentative.getUserIdentity();
			if (userIdentity != null) {
				actor = new Actor();
				actor.setGuid(userIdentity.getGID());
			}
		}
		return actor;
	}

	private Request adaptStatusAndReason(Request request, CustomerProblem customerProblem)
			throws CustomerProblemUpdateException {

		if (CollectionUtils.isEmpty(customerProblem.getCustomerProblemStatus())) {
			request.setStatus(EnumRequestStatus.RESOLVED);
			request.setStatusReason(EnumRequestStatusReason.CLOSED);
		} else {
			request.setStatus(EnumRequestStatus.getByGdmLabel(
					customerProblem.getCustomerProblemStatus().get(0).getStatusCode().toString()));
			if (customerProblem.getCustomerProblemStatus().get(0).getLocalStatusReason() != null) {
				EnumRequestStatusReason requestStatusReason = EnumRequestStatusReason.getBygmdLabel(
						customerProblem.getCustomerProblemStatus().get(0).getLocalStatusReason());
				if (requestStatusReason == null) {
					throw new CustomerProblemUpdateException("localStatusReason invalide", "CUSP014");
				}
				request.setStatusReason(requestStatusReason);
			} else {
				if (request.getStatus().equals(EnumRequestStatus.ACTIVE)) {
					request.setStatusReason(EnumRequestStatusReason.OPENED);
				}
				if (request.getStatus().equals(EnumRequestStatus.CANCELLED)) {
					request.setStatusReason(EnumRequestStatusReason.ABANDONED);
				}
				if (request.getStatus().equals(EnumRequestStatus.RESOLVED)) {
					request.setStatusReason(EnumRequestStatusReason.CLOSED);
				}
			}
		}
		return request;
	}

	private Request adaptInteractionSubjects(Request request, List<InteractionSubject> interactionSubjects,
			Reason reason, boolean dunning) {

		for (InteractionSubject interactionSubject : interactionSubjects) {
			if (interactionSubject instanceof Party) {
				Party partyTitulaire = (Party) interactionSubject;
				Holder businessHolder = new Holder();
				if (partyTitulaire.getPartyID() != null && !partyTitulaire.getPartyID()
						.contains(Constantes.ODL_PARTY_ID_REGEX)) {
					businessHolder.setGuid(partyTitulaire.getPartyID());
				}
				if (CollectionUtils.isNotEmpty(partyTitulaire.getPartyExternalIdentification())) {
					for (PartyExternalIdentification partyExternalIdentification : partyTitulaire.getPartyExternalIdentification()) {
						if (partyExternalIdentification.getExternalOwnerPartyID()
								.equals(WebservicesConstantes.EXTERNAL_OWER_PARTY_ID)) {
							businessHolder.setFunctionalId(partyExternalIdentification.getPartyExternalID());
						}
					}
				}
				request.setHolder(businessHolder);
				if (interactionSubject instanceof Organisation) {
					Organisation organisation = (Organisation) interactionSubject;
					if (dunning || reason.getMainReason().equals(ThemesCRMEnum.THEME1_GESTION_CLIENT)) {
						LegalStatus legalRedress = new LegalStatus();
						legalRedress.setResponsible(organisation.getLocalLegalStatusRepresentative());
						if (organisation.getLocalJudgementDate() != null) {
							legalRedress.setDateRedress(
									DateUtils.getDateFromXmlGregorianCalendar(organisation.getLocalJudgementDate()));
						}
						if (organisation.getLegalStatusCode() != null) {
							legalRedress.setLegarRedressType(
									EnumLegalStatus.valueOf(organisation.getLegalStatusCode().name()));
						}
						request.setLegalStatus(legalRedress);
					}
				}
			}
			if (interactionSubject instanceof BillingAccount) {
				BillingAccount billingAccount = (BillingAccount) interactionSubject;
				Account account = new Account();
				if (StringUtils.isNotEmpty(billingAccount.getLocalBillingAccountCode())) {
					account.setCustCode(billingAccount.getLocalBillingAccountCode());
				}
				if (StringUtils.isNotEmpty(billingAccount.getBillingAccountID())) {
					account.setAccountId(billingAccount.getBillingAccountID());
				}
				if (billingAccount.getPaymentMeans() != null) {
					for (PaymentMean pm : billingAccount.getPaymentMeans()) {
						if (pm != null && pm instanceof DirectDebit) {
							DirectDebit dd = (DirectDebit) pm;
							BankAccount bankAccount = dd.getBankAccount();
							if (bankAccount != null) {
								BankDetails bankDetails = new BankDetails();
								bankDetails.setBic(bankAccount.getBIC());
								bankDetails.setCountry(bankAccount.getDomiciliation());
								bankDetails.setIban(bankAccount.getIBAN());
								bankDetails.setName(bankAccount.getLocalCounterBank());
								bankDetails.setRum(bankAccount.getRUM());
								account.setBankDetails(bankDetails);
							}
						}
					}
				}
				request.setAccount(account);
			}
			if (interactionSubject instanceof FrameworkAgreement) {
				FrameworkAgreement fa = (FrameworkAgreement) interactionSubject;
				if (fa != null) {
					request.setAgreementDate(fa.getAgreementPeriod());
					request.setAgreementNumber(fa.getAgreementDocumentNumber());
				}
			}
			if (interactionSubject instanceof InstalledContract) {
				InstalledContract installedContract = (InstalledContract) interactionSubject;
				Contract contract = request.getContract();
				if (StringUtils.isNotEmpty(installedContract.getInstalledOfferID())) {
					String[] tab = installedContract.getInstalledOfferID().split(":");
					if (tab.length == 2) {
						contract.setCoId(tab[1]);
					}
				}
				if (CollectionUtils.isNotEmpty(installedContract.getInstalledPublicKey())) {
					for (InstalledPublicKey installedPublicKey : installedContract.getInstalledPublicKey()) {
						// TODO gestion des type d'installedPublicKey
						contract.setMsisdn(installedPublicKey.getInstalledPublicKeyID());
					}
				}
				request.setContract(contract);
			}
			if (interactionSubject instanceof CustomerOrder) {
				CustomerOrder customerOrder = (CustomerOrder) interactionSubject;
				if (StringUtils.isNotEmpty(customerOrder.getCustomerOrderID())) {
					Order order = new Order();
					order.setId(customerOrder.getCustomerOrderID());
					request.setOrder(order);
				}
				if (reason != null && reason.getMainReason().equals(THEME_RESILIATION)) {
					RetentionOffer retention = new RetentionOffer();
					for (CustomerOrderItem customerOrderItem : customerOrder.getCustomerOrderItem()) {
						retention.setCode(customerOrderItem.getInstalledOffer().getOfferSpecification()
								.getOfferSpecificationCode());
						retention.setLabel(customerOrderItem.getInstalledOffer().getOfferSpecification()
								.getOfferSpecificationCode());
					}
					request.setRetentionOffer(retention);
				} else {
					CommercialGesture commercialGesture = new CommercialGesture();
					Iterator<CustomerOrderItem> customerOrderItem_ITE = customerOrder.getCustomerOrderItem().iterator();
					while (customerOrderItem_ITE.hasNext()) {
						// TODO LDE : a implementer ou a supprimer
						// CustomerOrderItem customerOrderItem = customerOrderItem_ITE.next();
						// commercialGesture.setMontant(customerOrder.getLo );(
						// commercialGesture.setLabel(
						// customerOrderItem.getInstalledOffer().getOfferSpecification().getOfferSpecificationCode() );
					}
					request.setCommercialGesture(commercialGesture);
				}
			}
			if (interactionSubject instanceof LocalPaymentFacility) {
				LocalPaymentFacility payementFacility = (LocalPaymentFacility) interactionSubject;
				Recovery recovery = new Recovery();
				// recovery.setNbFacilitiesDone( payementFacility.getNbreFacility() );
				Facilities facilities = new Facilities();
				if (payementFacility.getExclusionDate() != null) {
					Facility facility = new Facility();
					facility.setAmount(payementFacility.getAmount());
					facility.setDateDelais(
							DateUtils.getDateFromXmlGregorianCalendar(payementFacility.getExclusionDate()));
					facilities.setDelay(facility);
				} else if (CollectionUtils.isNotEmpty(payementFacility.getPaymentSchedule())) {
					Facility facility1 = new Facility();
					Facility facility2 = new Facility();
					Facility facility3 = new Facility();
					PaymentSchedule schelude1 = payementFacility.getPaymentSchedule().get(0);
					PaymentSchedule schelude2 = payementFacility.getPaymentSchedule().get(1);
					PaymentSchedule schelude3 = null;
					if (payementFacility.getPaymentSchedule().size() == 3) {
						schelude3 = payementFacility.getPaymentSchedule().get(2);
					}
					facility1.setAmount(schelude1.getAmount());
					facility1.setDate(DateUtils.getDateFromXmlGregorianCalendar(schelude1.getLocalPlanificationDate()));
					facilities.getSchedules().add(facility1);
					facility2.setAmount(schelude2.getAmount());
					facility2.setDate(DateUtils.getDateFromXmlGregorianCalendar(schelude2.getLocalPlanificationDate()));
					facilities.getSchedules().add(facility2);
					if (schelude3 != null) {
						facility3.setAmount(schelude3.getAmount());
						facility3.setDate(
								DateUtils.getDateFromXmlGregorianCalendar(schelude3.getLocalPlanificationDate()));
						facilities.getSchedules().add(facility3);
					}
				}
				recovery.setFacilities(facilities);
				request.setRecovery(recovery);
			}
			if (interactionSubject instanceof Refund) {
				Refund refundGdm = (Refund) interactionSubject;
				com.orangecaraibe.enabler.crm.business.bean.Refund refund = new com.orangecaraibe.enabler.crm.business.bean.Refund();
				refund.setAmount(refundGdm.getAmount());
				refund.setDateRefund(DateUtils.getDateFromXmlGregorianCalendar(refundGdm.getDate()));
				// refund.setCostCenter( refundGdm.getCostCenter() );
				refund.setGlCode(refund.getGlCode());
				refund.setFamillieId(refundGdm.getLocalRefundFamily().getFamilyID());
				refund.setSiteId(refundGdm.getLocalSite().getSiteID());
				refund.setType(refundGdm.getLocalRefundType().value());
				request.setRefund(refund);
			}
			if (interactionSubject instanceof InstalledAtomicOffer) {
				InstalledAtomicOffer atomicOffer = (InstalledAtomicOffer) interactionSubject;
				Option option = new Option();
				// option.setOfferId( atomicOffer.getInstalledOfferID() );
				for (InstalledProduct installedProduct : atomicOffer.getInstalledProducts()) {
					for (InstalledFunctionValue installedFonctionValue : installedProduct.getInstalledFunctionValues()) {
						Parameter parameter = new Parameter();
						FunctionSpecification functionSpecification = installedFonctionValue.getFunctionSpecification();
						parameter.setCode(functionSpecification.getFunctionSpecificationCode());
						for (FunctionValueSpecification fonctionValueSpecificaiton : functionSpecification.getFunctionValueSpecifications()) {
							FunctionValueSpecification fonctionValue = catalogueHelper.getFunctionValueSpecification(
									fonctionValueSpecificaiton.getFunctionValue());
							parameter.setValue(fonctionValue.getFunctionValueLabel());
						}
						option.getParameters().add(parameter);
					}
				}
				request.setOption(option);
			}
			if (interactionSubject instanceof InstalledOffer) {
				InstalledOffer installedOffer = (InstalledOffer) interactionSubject;
				if (installedOffer.getOfferSpecification() != null) {
					OfferSpecification offerSpecification = installedOffer.getOfferSpecification();
					String offerLabel = offerSpecification.getOfferSpecificationLabel();
					request.getContract().setOfferLabel(offerLabel);
				}
			}
			if (interactionSubject instanceof InstalledResource) {
				InstalledResource installedRessource = (InstalledResource) interactionSubject;
				if (installedRessource.getResourceSpecification() != null) {
					ResourceSpecification offerSpecification = installedRessource.getResourceSpecification();
					String equipementLabel = offerSpecification.getResourceSpecificationLabel();
					String imei = "";
					request.getContract().getCellPhone().setModel(equipementLabel);
					for (ProductSpecification productSpecificaiton : offerSpecification.getProductSpecifications()) {
						for (FunctionSpecification instaleldFonctionValues : productSpecificaiton.getFunctionSpecifications()) {
							if (instaleldFonctionValues.getFunctionSpecificationCode().equals("FUHA_IMEI")) {
								for (FunctionValueSpecification functionValueSpecification : instaleldFonctionValues.getFunctionValueSpecifications()) {
									imei = functionValueSpecification.getFunctionValue();
								}
							}
						}
					}
					request.getContract().getCellPhone().setImei(imei);
				}
			}
		}
		return request;
	}

	private List<InteractionSubject> adaptRequestToInteractionSubjects(Request request) {

		List<InteractionSubject> interactionSubjects = new ArrayList<InteractionSubject>();
		if (request.getHolder() != null) {
			com.orangecaraibe.enabler.crm.business.bean.Party partyTitulaire = request.getHolder();
			Party party = new Party();
			party.setPartyID(partyTitulaire.getGuid());
			if (partyTitulaire.getFunctionalId() != null) {
				PartyExternalIdentification partyExternalIdentification = new PartyExternalIdentification();
				partyExternalIdentification.setExternalOwnerPartyID(WebservicesConstantes.EXTERNAL_OWER_PARTY_ID);
				partyExternalIdentification.setPartyExternalID(partyTitulaire.getFunctionalId());
				party.getPartyExternalIdentification().add(partyExternalIdentification);
			}
			interactionSubjects.add(party);
		}
		if (request.getAccount() != null) {
			BillingAccount billingAccount = new BillingAccount();
			Account account = request.getAccount();
			if (StringUtils.isNotEmpty(account.getCustCode())) {
				billingAccount.setLocalBillingAccountCode(account.getCustCode());
			}
			if (StringUtils.isNotEmpty(account.getAccountId())) {
				billingAccount.setBillingAccountID(account.getAccountId());
			}
			interactionSubjects.add(billingAccount);
		}
		if (request.getContract() != null) {
			InstalledContract installedContract = new InstalledContract();
			Contract contract = request.getContract();
			if (StringUtils.isNotEmpty(contract.getCoId())) {
				StringBuffer installedOfferId = new StringBuffer();
				installedOfferId.append(Constantes.REGEX_GUID_THEME);
				installedOfferId.append(contract.getCoId());
				installedContract.setInstalledOfferID(installedOfferId.toString());
			}
			if (StringUtils.isNotEmpty(contract.getMsisdn())) {
				// TODO reprendre gestion nouveau model SOA
				InstalledPublicKey msisdn = new InstalledPublicKey();
				msisdn.setInstalledPublicKeyID(contract.getMsisdn());
				installedContract.getInstalledPublicKey().add(msisdn);
			}
			if (contract.getCellPhone() != null) {
				if (contract.getCellPhone().getModel() != null) {
					InstalledResource installedRessource = new InstalledResource();
					ResourceSpecification offerSpecification = new ResourceSpecification();
					offerSpecification.setResourceSpecificationLabel(contract.getCellPhone().getModel());
					installedRessource.setResourceSpecification(offerSpecification);
					interactionSubjects.add(installedRessource);
				}
			}
			if (contract.getOfferLabel() != null) {
				InstalledOffer installedOffer = new InstalledOffer();
				OfferSpecification offerSpecification = new OfferSpecification();
				offerSpecification.setOfferSpecificationLabel(contract.getOfferLabel());
				installedOffer.setOfferSpecification(offerSpecification);
				interactionSubjects.add(installedOffer);
			}
			interactionSubjects.add(installedContract);
		}
		if (request.getRetentionOffer() != null) {
			// RUNO-10853 / TMAOREKA-1430 - Les rétentions seront remontés par une offer specification (vu avec
			// architecte SOA)
			RetentionOffer retention = request.getRetentionOffer();
			OfferSpecification offerSpecification = new OfferSpecification();
			offerSpecification.setOfferSpecificationCode(retention.getCode());
			offerSpecification.setOfferSpecificationLabel(retention.getLabel());
			TariffSpecification tariffSpecification = new TariffSpecification();
			tariffSpecification.setValue(retention.getAmount());
			offerSpecification.getTariffSpecifications().add(tariffSpecification);
			interactionSubjects.add(offerSpecification);
		}
		if (request.getCommercialGesture() != null) {
			CommercialGesture commercialGesture = request.getCommercialGesture();
			Refund refundGdm = new Refund();
			refundGdm.setLocalRefundType(LocalRefundTypeEnum.COMMERCIAL_GESTURE);
			refundGdm.setAmount((float) commercialGesture.getAmount());
			refundGdm.setInteractionSubjectLabel(commercialGesture.getName());
			if (commercialGesture.getStatusReason() != null) {
				refundGdm.setStatusCode(String.valueOf(commercialGesture.getStatusReason().getId()));
				refundGdm.setStatusLabel(commercialGesture.getStatusReason().getGdmLabel());
			}
			interactionSubjects.add(refundGdm);
		}
		if (request.getRecovery() != null) {
			Recovery recovery = request.getRecovery();
			Facilities facilities = recovery.getFacilities();
			LocalPaymentFacility payementFacility = new LocalPaymentFacility();
			if (facilities.getDelay() != null) {
				Facility facility = facilities.getDelay();
				payementFacility.setAmount(facility.getAmount());
				payementFacility.setExclusionDate(
						DateUtils.convertDateToXMLGregorianCalendar(facility.getDateDelais()));
				interactionSubjects.add(payementFacility);
			} else if (facilities.getSchedules() != null) {
				for (Facility facility : facilities.getSchedules()) {
					PaymentSchedule schelude = new PaymentSchedule();
					schelude.setAmount(facility.getAmount());
					schelude.setLocalPlanificationDate(DateUtils.convertDateToXMLGregorianCalendar(facility.getDate()));
					payementFacility.getPaymentSchedule().add(schelude);
				}
			}
		}
		if (request.getRefund() != null) {
			Refund refundGdm = new Refund();
			com.orangecaraibe.enabler.crm.business.bean.Refund refund = request.getRefund();
			refundGdm.setAmount(refund.getAmount());
			refundGdm.setDate(DateUtils.convertDateToXMLGregorianCalendar(refund.getDateRefund()));
			LocalRefundFamily localFamily = new LocalRefundFamily();
			localFamily.setFamilyID(refund.getFamillieId());
			LocalLedgerSegment segment = new LocalLedgerSegment();
			segment.setLocalGLCode(refund.getGlCode());
			localFamily.setLocalLedgerSegment(segment);
			refundGdm.setLocalRefundFamily(localFamily);
			LocalRefundNature nature = new LocalRefundNature();
			nature.setNatureID(refund.getNatureId());
			refundGdm.setLocalRefundNature(nature);
			Site site = new Site();
			site.setSiteID(refund.getSiteId());
			refundGdm.setLocalSite(site);
			refundGdm.setLocalRefundType(LocalRefundTypeEnum.valueOf(refund.getType()));
			request.setRefund(refund);
		}
		if (request.getOrder() != null) {
			CustomerOrder customerOrder = new CustomerOrder();
			customerOrder.setCustomerOrderID(request.getOrder().getId());
			interactionSubjects.add(customerOrder);
		}
		return interactionSubjects;
	}

	public Request updateNotifyDunningAdaptation(CustomerProblem customerProblem) {

		Request request = new Request();
		request.setGuid(customerProblem.getTroubleTicketID());
		if (CollectionUtils.isEmpty(customerProblem.getCustomerProblemStatus())) {
			request.setStatus(EnumRequestStatus.RESOLVED);
		} else {
			request.setStatus(EnumRequestStatus.getByGdmLabel(
					customerProblem.getCustomerProblemStatus().get(0).getStatusCode().toString()));
		}
		if (CollectionUtils.isNotEmpty(customerProblem.getLocalCommercialClassifications())) {
			if (customerProblem.getLocalCommercialClassifications().get(0) instanceof SaleChannel) {
				request.setChannel(customerProblem.getLocalCommercialClassifications().get(0).getLabel());
			}
		}
		if (CollectionUtils.isNotEmpty(customerProblem.getLocalComment())) {
			List<Note> notes = new ArrayList<Note>();
			for (LocalComment localComment : customerProblem.getLocalComment()) {
				Note note = new Note();
				note.setNote(localComment.getLabel());
				notes.add(note);
			}
			request.getNotes().addAll(notes);
		}
		List<InteractionSubject> interactionSubject = customerProblem.getInteractionSubject();
		Reason reason = null;
		if (customerProblem.getReason() != null) {
			reason = customerProblem.getReason();
		}
		request = adaptInteractionSubjects(request, interactionSubject, reason, true);
		request.setActor(
				adaptCustomerServiceRepresentativesToActorLogin(customerProblem.getCustomerServiceRepresentative()));
		return request;
	}

	public Request updateToAddNote(CustomerProblem customerProblem) {

		Request request = new Request();
		return request;
	}

	/**
	 * Methode pour l'adaptation d'une mise à jour d'une demande
	 */
	public Request adaptCustomerProblemToRequestUpdate(CustomerProblem customerProblem, boolean adaptStatusAndReason)
			throws CustomerProblemUpdateException {

		Request request = new Request();
		request.setGuid(customerProblem.getTroubleTicketID());
		if (adaptStatusAndReason) {
			request = adaptStatusAndReason(request, customerProblem);
		}
		request = adaptInteractionSubjects(request, customerProblem.getInteractionSubject(),
				customerProblem.getReason(), false);
		if (CollectionUtils.isNotEmpty(customerProblem.getCustomerServiceRepresentative())) {
			CustomerServiceRepresentative csr = customerProblem.getCustomerServiceRepresentative().get(0);
			// TODO - definir un adapter mutualise pour les elements commun a CustomerProblem et Interaction
			CustomerService customerService = interactionAdapter.adaptCustomerService(csr);
			if (customerService != null) {
				request.setCustomerService(customerService);
			}
		} else {
			if (CollectionUtils.isNotEmpty(customerProblem.getLocalCommercialClassifications())) {
				request.setChannel(customerProblem.getLocalCommercialClassifications().get(0).getLabel());
				// for ( com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.CommercialClassification
				// classif : customerProblem.getLocalCommercialClassifications() )
				// {
				// if ( classif instanceof SaleChannel )
				// {
				// // request.setCustomerServiceGuid( USER_TECHNIQUE_WEBCARE );
				// // request.setCustomerServiceType( EnumCustomerServiceType.USER );
				// }
				// }
			}
		}
		List<Note> notes = new ArrayList<Note>();
		for (LocalComment localComment : customerProblem.getLocalComment()) {
			Note note = new Note();
			note.setNote(localComment.getLabel());
			notes.add(note);
		}
		request.getNotes().addAll(notes);
		List<Attachment> attachments = new ArrayList<Attachment>();
		for (LocalCustomerProblemImage localCustomerProblemImage : customerProblem.getLocalCustomerProblemImages()) {
			Attachment attachment = new Attachment();
			attachment.setNom(localCustomerProblemImage.getImageName());
			attachment.setType(localCustomerProblemImage.getImageMimeType());
			attachment.setDocument(localCustomerProblemImage.getImageLink());
			attachments.add(attachment);
		}
		request.getAttachments().addAll(attachments);
		if (customerProblem.getInteraction() != null && !customerProblem.getInteraction().isEmpty()) {
			List<Interaction> interactions = new ArrayList<Interaction>();
			Interaction interaction = new Interaction();
			interaction.setId(customerProblem.getInteraction().get(0).getInteractionID());
			interactions.add(interaction);
			request.setInteractions(interactions);
		}
		if (customerProblem.getPriority() != null && StringUtils.isNotEmpty(customerProblem.getPriority())) {
			request.setPriority(EnumPriority.valueOf(customerProblem.getPriority()));
		}
		return request;
	}

	public List<CustomerProblem> adaptRequestsToCustomerProblems(List<Request> requests) {

		List<CustomerProblem> customerProblems = new ArrayList<CustomerProblem>();
		if (requests != null) {
			for (Request request : requests) {
				CustomerProblem customerProblem = adaptRequestToCustomerProblem(request);
				customerProblems.add(customerProblem);
			}
		}
		return customerProblems;
	}

	public CustomerProblem adaptRequestToCustomerProblem(Request request) {

		CustomerProblem customerProblem = new CustomerProblem();
		customerProblem.setTroubleTicketID(request.getGuid());
		customerProblem.setLocalFunctionalID(request.getId());
		if (request.getCreationDate() != null) {
			customerProblem.setDateCreation(DateUtils.convertDateToXMLGregorianCalendar(request.getCreationDate()));
			customerProblem.setInteractionDate(DateUtils.convertDateToXMLGregorianCalendar(request.getCreationDate()));
		}
		if (request.getCloseDate() != null) {
			customerProblem.setInteractionDateCompleted(
					DateUtils.convertDateToXMLGregorianCalendar(request.getCloseDate()));
		}
		customerProblem.setDescription(request.getDescription());
		CustomerProblemStatus customerProblemStatus = new CustomerProblemStatus();
		// adaptation de l'interaction a laquelle est rattachee la demande
		if (CollectionUtils.isNotEmpty(request.getInteractions())) {
			List<com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction> gdminteractions = interactionAdapter.adaptBusinessInteractions(
					request.getInteractions());
			customerProblem.getInteraction().addAll(gdminteractions);
		}
		if (request.getStatus() != null) {
			if (request.getStatus().equals(EnumRequestStatus.CANCELLED)) {
				customerProblemStatus.setStatusCode(CustomerProblemStatusCode.CANCELED);
			}
			if (request.getStatus().equals(EnumRequestStatus.RESOLVED)) {
				customerProblemStatus.setStatusCode(CustomerProblemStatusCode.CLOSED);
			}
			if (request.getStatus().equals(EnumRequestStatus.ACTIVE)) {
				customerProblemStatus.setStatusCode(CustomerProblemStatusCode.OPENED);
			}
			if (request.getStatus().equals(EnumCustomerProblemStatus.IN_PROGRESS)) {
				customerProblemStatus.setStatusCode(CustomerProblemStatusCode.OPENED);
			}
		}
		if (request.getStatusReason() != null) {
			customerProblemStatus.setLocalStatusReason(request.getStatusReason().getGmdLabel());
		}
		customerProblem.getCustomerProblemStatus().add(customerProblemStatus);
		// du titre de la demande
		customerProblem.setLocalTitle(request.getTitle());
		// adaptation du channel de la demande
		if (request.getChannel() != null) {
			CommercialClassification commercialClasificaiton = new CommercialClassification();
			commercialClasificaiton.setLabel(request.getChannel());
			customerProblem.getLocalCommercialClassifications().add(commercialClasificaiton);
		}
		customerProblem.setLocalKeyword(request.getKeyWord());
		customerProblem.setDescription(request.getDescription());
		Reason reason = new Reason();
		if (request.getTheme() != null) {
			Theme theme = request.getTheme();
			StringBuffer reasonSpecificationCode = new StringBuffer();
			reasonSpecificationCode.append(theme.guid);
			reasonSpecificationCode.append(Constantes.REGEX_GUID_THEME);
			reason.setMainReason(theme.label);
			if (CollectionUtils.isNotEmpty(theme.getTheme())) {
				Theme th2 = theme.getTheme().get(0);
				reason.setReason(th2.getLabel());
				reasonSpecificationCode.append(th2.guid);
				reasonSpecificationCode.append(Constantes.REGEX_GUID_THEME);
				if (CollectionUtils.isNotEmpty(th2.getTheme())) {
					Theme th3 = th2.getTheme().get(0);
					reason.setSubReason(th3.getLabel());
					reasonSpecificationCode.append(th3.guid);
					reasonSpecificationCode.append(Constantes.REGEX_GUID_THEME);
				}
			}
			LocalReasonSpecification localReasonSpecification = new LocalReasonSpecification();
			localReasonSpecification.setReasonSpecificationCode(reasonSpecificationCode.toString());
			reason.setLocalReasonSpecification(localReasonSpecification);
			customerProblem.setReason(reason);
		}
		List<LocalComment> localComments = new ArrayList<LocalComment>();
		for (Note note : request.getNotes()) {
			localComments.add(adaptNote(note));
		}
		customerProblem.getLocalComment().addAll(localComments);
		customerProblem.getInteractionSubject().addAll(adaptRequestToInteractionSubjects(request));
		for (Attachment attachment : request.getAttachments()) {
			LocalCustomerProblemImage localCustomerProblemImage = new LocalCustomerProblemImage();
			localCustomerProblemImage.setImageName(attachment.getNom());
			localCustomerProblemImage.setImageMimeType(attachment.getType());
			localCustomerProblemImage.setImageLink(attachment.getDocument());
			// Adaptation de la note associé à la pièce jointe
			if (attachment.getNote() != null) {
				localCustomerProblemImage.getLocalComment().add(adaptNote(attachment.getNote()));
			}
			customerProblem.getLocalCustomerProblemImages().add(localCustomerProblemImage);
		}
		if (request.getEquipeServicelabel() != null || request.getCustomerService() != null) {
			// CustomerService cusService = request.getCustomerService();
			CustomerServiceRepresentative cusServices = new CustomerServiceRepresentative();
			// cusServices.setName( cusService.getName() );
			if (request.getEquipeServicelabel() != null) {
				cusServices.setName(request.getEquipeServicelabel());
			}
			if (request.getCustomerService() != null) {
				Person party = new Person();
				com.orangecaraibe.soa.v2.model.commonbusiness.party.PersonName partyName = new com.orangecaraibe.soa.v2.model.commonbusiness.party.PersonName();
				partyName.setLastName(request.getCustomerService().getName());
				party.setPartyName(partyName);
				cusServices.setParty(party);
			}
			customerProblem.getCustomerServiceRepresentative().add(cusServices);
		}
		// on adapte la mesure financière de la demande en resolutionAction
		ResolutionAction resolutionAction = adaptResolutionAction(request.getResolutionAction());
		if (resolutionAction != null) {
			customerProblem.getTreatedBy().add(resolutionAction);
		}
		return customerProblem;
	}

	/**
	 * adapte une demande business en {@link ResolutionAction}
	 *
	 * @return {@link ResolutionAction}
	 */
	private ResolutionAction adaptResolutionAction(
			com.orangecaraibe.enabler.crm.business.bean.ResolutionAction resAction) {

		ResolutionAction resolutionAction = null;
		if (resAction != null && resAction.getLocalActionType() != null) {
			resolutionAction = new ResolutionAction();
			/**
			 * setLocalActionType n'existe pas dans la classe
			 * com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.ResolutionAction
			 */
			// resolutionAction.setLocalActionType( resAction.getLocalActionType() );
			if (resAction.getActionDescription() != null) {
				resolutionAction.setActionDescription(resAction.getActionDescription());
			}
			if (resAction.getActionResult() != null) {
				resolutionAction.setActionResult(resAction.getActionResult());
			}
		}
		return resolutionAction;
	}

	/**
	 * Adapte les notes DCRM au format note SOA
	 */
	private LocalComment adaptNote(Note note) {

		LocalComment localComment = new LocalComment();
		if (note.getSubject() != null) {
			localComment.setSubject(note.getSubject().toString());
		}
		if (note.getNote() != null) {
			localComment.setLabel(note.getNote().toString());
		}
		localComment.setCode(note.getGuid());
		PartyRole partyRole = new PartyRole();
		Party party = new Party();
		party.setPartyID(note.getUserGuid());
		Operator ope = new Operator();
		ope.setName(note.getUserName());
		party.setOperator(ope);
		partyRole.setParty(party);
		localComment.setPartyRole(partyRole);
		if (note.getDateCreation() != null) {
			localComment.setCommentDate(DateUtils.convertDateToXMLGregorianCalendar(note.getDateCreation()));
		}
		return localComment;
	}

	/**
	 * @param catalogueHelper
	 * 		the catalogueHelper to set
	 */
	public void setCatalogueHelper(CatalogueHelper catalogueHelper) {

		this.catalogueHelper = catalogueHelper;
	}

	public void setLinkCrmBscsHelper(LinkCrmBscsHelper linkCrmBscsHelper) {

		this.linkCrmBscsHelper = linkCrmBscsHelper;
	}

	public void setInteractionAdapter(InteractionAdapter interactionAdapter) {

		this.interactionAdapter = interactionAdapter;
	}
}
