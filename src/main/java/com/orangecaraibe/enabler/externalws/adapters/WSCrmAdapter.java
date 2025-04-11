/**
 *
 */
package com.orangecaraibe.enabler.externalws.adapters;

import com.orange.sidom.soa.dcrm.datacontract.Campagne;
import com.orange.sidom.soa.dcrm.datacontract.Email;
import com.orange.sidom.soa.dcrm.datacontract.Interaction;
import com.orange.sidom.soa.dcrm.datacontract.Note;
import com.orange.sidom.soa.dcrm.datacontract.*;
import com.orange.sidom.soa.dcrm.servicecontract.demande._1_0.ManageDemandeCreateServiceFaultFaultFaultMessage;
import com.orangecaraibe.enabler.core.utils.Constantes;
import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.crm.business.bean.Party;
import com.orangecaraibe.enabler.crm.business.bean.*;
import com.orangecaraibe.enabler.crm.business.bean.enums.*;
import com.orangecaraibe.enabler.crm.business.enums.CommunicationCanalEnum;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemCreateException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindReasonException;
import com.orangecaraibe.enabler.externalws.helpers.DefaultInteractionStatusReasonBean;
import com.orangecaraibe.enabler.externalws.helpers.InteractionReasonStatusPropertiesHelper;
import com.orangecaraibe.enabler.externalws.helpers.ThemeHelper;
import com.orangecaraibe.enabler.externalws.utils.DcrmNoteIsDocumentPredicate;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.ResolutionAction;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Classe pour l'adaptation vers les services de haut niveau DCRM
 *
 * @author ncr
 */
@Component("wsCrmAdapter")
public class WSCrmAdapter {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(WSCrmAdapter.class);

	@Autowired
	@Qualifier("objectFactoryDCRM")
	private ObjectFactory objectFactory;

	@Autowired
	private InteractionReasonStatusPropertiesHelper interactionReasonStatusPropertiesHelper;

	@Autowired
	private ThemeHelper themeHelper;

	@Value("${dcrm.default.systemuser.guid}")
	private String defaultSystemUserGuid;

	private final static Map<Class<? extends Interaction>, EnumInteractionChannel> interactionTypesMap = new HashMap<Class<? extends Interaction>, EnumInteractionChannel>();

	static {
		interactionTypesMap.put(Fax.class, EnumInteractionChannel.FAX);
		interactionTypesMap.put(Courrier.class, EnumInteractionChannel.MAIL);
		interactionTypesMap.put(Email.class, EnumInteractionChannel.EMAIL);
		interactionTypesMap.put(AppelTelephonique.class, EnumInteractionChannel.PHONE_CALL);
		interactionTypesMap.put(Selfcare.class, EnumInteractionChannel.SELFCARE);
		interactionTypesMap.put(SMS.class, EnumInteractionChannel.SMS);
		interactionTypesMap.put(ReseauSocial.class, EnumInteractionChannel.SOCIAL_NETWORK);
		interactionTypesMap.put(PDVPhysique.class, EnumInteractionChannel.STORE);
	}

	public final static Map<Integer, EnumInteractionStatus> interactionStatusMap = new HashMap<Integer, EnumInteractionStatus>();

	static {
		interactionStatusMap.put(0, EnumInteractionStatus.IN_PROGRESS);
		interactionStatusMap.put(1, EnumInteractionStatus.COMPLETED);
		interactionStatusMap.put(2, EnumInteractionStatus.CANCELLED);
		interactionStatusMap.put(3, EnumInteractionStatus.PLANNED);
	}

	private final static Map<Integer, EnumRequestStatus> requestStatusMap = new HashMap<Integer, EnumRequestStatus>();

	static {
		requestStatusMap.put(0, EnumRequestStatus.ACTIVE);
		requestStatusMap.put(1, EnumRequestStatus.RESOLVED);
		requestStatusMap.put(2, EnumRequestStatus.CANCELLED);
	}

	private final static Map<Integer, EnumPriority> priorityMap = new HashMap<Integer, EnumPriority>();

	static {
		priorityMap.put(0, EnumPriority.LOW);
		priorityMap.put(1, EnumPriority.NORMAL);
		priorityMap.put(2, EnumPriority.HIGH);
	}

	private final static Map<Boolean, EnumInteractionWay> interactionWayMap = new HashMap<Boolean, EnumInteractionWay>();

	static {
		interactionWayMap.put(Boolean.FALSE, EnumInteractionWay.INCOMING);
		interactionWayMap.put(Boolean.TRUE, EnumInteractionWay.OUTGOING);
	}

	public Demande adaptCreateDemande(Request request)
			throws CustomerProblemFindReasonException, ManageDemandeCreateServiceFaultFaultFaultMessage,
			CustomerProblemCreateException {

		Demande demande = new Demande();
		StringBuffer titre = new StringBuffer();
		if (StringUtils.isNotEmpty(request.getTitle())) {
			titre.append(request.getTitle());
		} else if (request.getTheme() != null && request.getTheme().getLabel() != null) {
			titre.append(request.getTheme().getLabel());
			titre.append(Constantes.WHITE_SPACE);
			// récuperation du theme de niveau 2
			if (CollectionUtils.isNotEmpty(request.getTheme().getTheme()) && request.getTheme().getTheme().get(0)
					.getLabel() != null) {
				titre.append(request.getTheme().getTheme().get(0).getLabel());
				titre.append(Constantes.WHITE_SPACE);
				// récuperation du theme de niveau 3
				if (CollectionUtils.isNotEmpty(request.getTheme().getTheme().get(0).getTheme()) && request.getTheme()
						.getTheme().get(0).getTheme().get(0).getLabel() != null) {
					titre.append(request.getTheme().getTheme().get(0).getTheme().get(0).getLabel());
					titre.append(Constantes.WHITE_SPACE);
				}
			}
		}
		demande.setTitre(objectFactory.createDemandeTitre(titre.toString()));
		demande.setApplication(objectFactory.createDemandeApplication(request.getChannel()));
		if (MapUtils.invertMap(requestStatusMap).containsKey(request.getStatus())) {
			Integer dcrmStatus = MapUtils.invertMap(requestStatusMap).get(request.getStatus());
			if (dcrmStatus != null) {
				demande.setStatut(objectFactory.createDemandeStatut(dcrmStatus));
			} else {
				LOGGER.warn("Erreur lors la récupération du status dcrm");
			}
		}
		if (request.getStatusReason() != null) {
			demande.setRaisonStatut(objectFactory.createDemandeRaisonStatut(request.getStatusReason().getId()));
		} else if (request.getStatus() != null) {
			demande.setRaisonStatut(getRaisonFromStatus(request.getStatus()));
		}
		demande.setDescription(objectFactory.createDemandeDescription(request.getDescription()));
		if (request.getPriority() != null && MapUtils.invertMap(priorityMap).containsKey(request.getPriority())) {
			Integer dcrmPriority = MapUtils.invertMap(priorityMap).get(request.getPriority());
			demande.setPriorite(objectFactory.createDemandePriorite(dcrmPriority));
		}
		// demande.setConsigne( value );
		demande.setMotClef(objectFactory.createDemandeMotClef(request.getKeyWord()));
		ThemeNiveau1 theme1 = creationTheme(request.getTheme());
		JAXBElement<ThemeNiveau1> jaxbElementTheme1 = objectFactory.createDemandeThemeNiveau1(theme1);
		demande.setThemeNiveau1(jaxbElementTheme1);
		if (CollectionUtils.isNotEmpty(request.getInteractions())) {
			Interaction interac = new Interaction();
			ArrayOfInteraction arrayOfInteractions = new ArrayOfInteraction();
			interac.setGUIDInteraction(
					objectFactory.createInteractionGUIDInteraction(request.getInteractions().get(0).getId()));
			// [RUNO-17152] [SOA - OAGRCLI-3168] - [DCC-Contrôle CRM] ajout du titre
			interac.setTitre(objectFactory.createInteractionTitre(titre.toString()));
			arrayOfInteractions.getInteraction().add(interac);
			JAXBElement<ArrayOfInteraction> interaction = objectFactory.createDemandeInteractions(arrayOfInteractions);
			demande.setInteractions(interaction);
		}
		if (request.getCommunicationChannel() != null) {
			demande.setCanalCommunication(objectFactory.createDemandeCanalCommunication(
					CommunicationCanalEnum.valueOf(request.getCommunicationChannel().getCommunicationCanal())
							.getCommunicationCanalValue()));
		}
		demande.setTicketClientOceane(objectFactory.createDemandeTicketClientOceane(request.getTroubleTicketID()));
		// adaptation du conseiller
		if (request.getCustomerService() != null && StringUtils.isNotEmpty(request.getCustomerService().getGuid())) {
			// adaptation du proprietaire
			Proprietaire proprietaire = adaptBusinessCustomerService(request.getCustomerService());
			demande.setCorbeille(objectFactory.createDemandeCorbeille(proprietaire));
			// on recupere l'entityRef defini sous le proprietaire nouvellement cree pour positionner le champ
			// "cree par"
			demande.setCreePar(objectFactory.createDemandeCreePar(proprietaire.getUtilisateurOuEquipe().getValue()));
		}
		ArrayOfNote notes = adaptNote(request);
		demande.setNotes(objectFactory.createDemandeNotes(notes));
		if (request.getRetentionOffer() != null) {
			RetentionOffer retentionOffer = new RetentionOffer();
			ArrayOfPropositionRetention retentions = new ArrayOfPropositionRetention();
			PropositionRetention retention = new PropositionRetention();
			retention.setCode(objectFactory.createPropositionRetentionCode(retentionOffer.getCode()));
			retention.setNom(objectFactory.createPropositionRetentionNom(retentionOffer.getLabel()));
			retentions.getPropositionRetention().add(retention);
			demande.setPropositionsRetentions(objectFactory.createArrayOfPropositionRetention(retentions));
		}
		if (request.getRefund() != null) {
			AvoirRemboursement avoirRembousement = new AvoirRemboursement();
			ArrayOfLigneComptable arrayOfLigneComptable = new ArrayOfLigneComptable();
			LigneComptable ligneComptable = new LigneComptable();
			EntityReference entity = new EntityReference();
			entity.setName(objectFactory.createEntityReferenceName(request.getRefund().getGlCode()));
			ligneComptable.setGLCode(objectFactory.createLigneComptableGLCode(entity));
			ligneComptable.setMontant(this.objectFactory.createLigneComptableMontant(
					BigDecimal.valueOf(request.getRefund().getAmount())));
			EntityReference costCenter = new EntityReference();
			costCenter.setName(objectFactory.createEntityReferenceName(request.getRefund().getCostCenter()));
			ligneComptable.setCostCenter(objectFactory.createLigneComptableCostCenter(costCenter));
			avoirRembousement.setMontant(
					objectFactory.createAvoirRemboursementMontant(BigDecimal.valueOf(request.getRefund().getAmount())));
			avoirRembousement.setLignesComptables(
					objectFactory.createAvoirRemboursementLignesComptables(arrayOfLigneComptable));
			demande.setAvoirRemboursement(objectFactory.createDemandeAvoirRemboursement(avoirRembousement));
		}
		if (request.getRecovery() != null) {
			Recovery recovery = request.getRecovery();
			Recouvrement recouvrement = new Recouvrement();
			if (recovery.getFacilities() != null) {
				Facilities facilities = recovery.getFacilities();
				if (facilities.getDelay() != null) {
					Facility delay = facilities.getDelay();
					recouvrement.setTypeFacilite(objectFactory.createRecouvrementTypeFacilite(100000000));
					recouvrement.setMontantDelai(
							objectFactory.createRecouvrementMontantDelai(BigDecimal.valueOf(delay.getAmount())));
					recouvrement.setDateDelai(objectFactory.createRecouvrementDateDelai(
							DateUtils.convertDateToXMLGregorianCalendar(delay.getDateDelais())));
				}
				if (facilities.getSchedules() != null) {
					List<Facility> schedules = facilities.getSchedules();
					recouvrement.setTypeFacilite(objectFactory.createRecouvrementTypeFacilite(100000001));
					Facility schedule1 = facilities.getSchedules().get(0);
					Facility schedule2 = facilities.getSchedules().get(1);
					Facility schedule3 = null;
					if (schedules.size() > 1) {
						schedule3 = facilities.getSchedules().get(2);
					}
					recouvrement.setDateEcheance1(objectFactory.createRecouvrementDateEcheance1(
							DateUtils.convertDateToXMLGregorianCalendar(schedule1.getDate())));
					recouvrement.setMontantEcheance1(objectFactory.createRecouvrementMontantEcheance1(
							BigDecimal.valueOf(schedule1.getAmount())));
					recouvrement.setDateEcheance2(objectFactory.createRecouvrementDateEcheance1(
							DateUtils.convertDateToXMLGregorianCalendar(schedule2.getDate())));
					recouvrement.setMontantEcheance2(objectFactory.createRecouvrementMontantEcheance1(
							BigDecimal.valueOf(schedule2.getAmount())));
					if (schedule3 != null) {
						recouvrement.setDateEcheance3(objectFactory.createRecouvrementDateEcheance1(
								DateUtils.convertDateToXMLGregorianCalendar(schedule3.getDate())));
						recouvrement.setMontantEcheance3(objectFactory.createRecouvrementMontantEcheance1(
								BigDecimal.valueOf(schedule3.getAmount())));
					}
				}
			}
			demande.setRecouvrement(objectFactory.createDemandeRecouvrement(recouvrement));
		}
		if (request.getHolder() != null) {
			Party party = request.getHolder();
			Individu partyTitulaire = new Individu();
			partyTitulaire.setGUIDParty(objectFactory.createPartyGUIDParty(party.getGuid()));
			demande.setPartyTitulaire(objectFactory.createDemandePartyTitulaire(partyTitulaire));
		}
		if (request.getAccount() != null) {
			Account account = request.getAccount();
			ClientFacturation clientFacturation = new ClientFacturation();
			clientFacturation.setCustomerID(objectFactory.createClientFacturationCustomerID(account.getAccountId()));
			clientFacturation.setCustcode(objectFactory.createClientFacturationCustcode(account.getCustCode()));
			demande.setClientFacturation(objectFactory.createDemandeClientFacturation(clientFacturation));
			if (account.getBankDetails() != null) {
				BankDetails bd = account.getBankDetails();
				demande.setNumeroCompteBancaire(objectFactory.createDemandeNumeroCompteBancaire(bd.getIban()));
			}
		}
		if (request.getContract() != null) {
			Contract contract = request.getContract();
			Contrat contrat = new Contrat();
			contrat.setIDContrat(objectFactory.createContratIDContrat(contract.getCoId()));
			contrat.setMSISDN(objectFactory.createContratMSISDN(contract.getMsisdn()));
			demande.setContrat(objectFactory.createDemandeContrat(contrat));
			if (contract.getCellPhone() != null) {
				demande.setEquipementConcerne(
						objectFactory.createDemandeEquipementConcerne(contract.getCellPhone().getModel()));
				demande.setNumeroImei(objectFactory.createDemandeNumeroImei(contract.getCellPhone().getImei()));
			}
			if (contract.getOfferLabel() != null) {
				demande.setOffreConcernee(objectFactory.createDemandeOffreConcernee(contract.getOfferLabel()));
			}
		}
		if (StringUtils.isNotBlank(request.getAgreementNumber()) && StringUtils.isNotBlank(
				request.getAgreementDate())) {
			try {
				demande.setNumMandatMobiliteBancaire(
						objectFactory.createDemandeNumMandatMobiliteBancaire(request.getAgreementNumber()));
				Date date = DateUtils.getDateFromString(request.getAgreementDate(), "yyyyMMddhhmm");
				demande.setMandatSigneLe(
						objectFactory.createDemandeMandatSigneLe(DateUtils.convertDateToXMLGregorianCalendar(date)));
			}
			catch (ParseException e) {
				LOGGER.error("Erreur lors de la récupération de la date du mandat de mobilité bancaire", e);
				CustomerProblemCreateException CustomerProblemCreateException = new CustomerProblemCreateException(
						"Erreur lors de l'adaptation de l'interaction : " + e.getMessage(), "INT014");
				throw CustomerProblemCreateException;
			}
		}
		if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getId())) {
			demande.setIDOM(objectFactory.createDemandeIDOM(request.getOrder().getId()));
		}
		// ajout de l'utilisateur TODO
		if (request != null && request.getActor() != null) {
			UserInformation userInformation = new UserInformation();
			userInformation.setUserID(objectFactory.createUserInformationUserID(request.getActor().getGuid()));
			userInformation.setUserName(objectFactory.createUserInformationUserName(request.getActor().getName()));
			demande.setUserInformation(objectFactory.createDemandeUserInformation(userInformation));
		}
		return demande;
	}

	private ThemeNiveau1 creationTheme(Theme theme) throws CustomerProblemFindReasonException {

		ThemeNiveau1 theme1 = new ThemeNiveau1();
		ThemeNiveau2 theme2 = new ThemeNiveau2();
		ThemeNiveau3 theme3 = new ThemeNiveau3();
		if (theme.getGuid() == null) {
			theme = themeHelper.getGUIDTheme(theme);
		}
		if (CollectionUtils.isNotEmpty(theme.getTheme())) {
			Theme th2 = theme.getTheme().get(0);
			theme2.setLibelle(objectFactory.createThemeNiveau2Libelle(th2.getLabel()));
			theme2.setGUIDThemeNiveau2(th2.getGuid());
			if (CollectionUtils.isNotEmpty(th2.getTheme())) {
				Theme th3 = th2.getTheme().get(0);
				theme3.setLibelle(objectFactory.createThemeNiveau3Libelle(th3.getLabel()));
				theme3.setGUIDThemeNiveau3(th3.getGuid());
			}
		}
		theme1.setLibelle(objectFactory.createThemeNiveau1Libelle(theme.getLabel()));
		theme1.setGUIDThemeNiveau1(theme.getGuid());
		ArrayOfThemeNiveau3 arrayThemes3 = new ArrayOfThemeNiveau3();
		arrayThemes3.getThemeNiveau3().add(theme3);
		JAXBElement<ArrayOfThemeNiveau3> jaxbElementTheme3 = objectFactory.createThemeNiveau2ThemesNiveau3(
				arrayThemes3);
		theme2.setThemesNiveau3(jaxbElementTheme3);
		ArrayOfThemeNiveau2 arrayThemes2 = new ArrayOfThemeNiveau2();
		arrayThemes2.getThemeNiveau2().add(theme2);
		JAXBElement<ArrayOfThemeNiveau2> jaxbElementTheme2 = objectFactory.createThemeNiveau1ThemesNiveau2(
				arrayThemes2);
		theme1.setThemesNiveau2(jaxbElementTheme2);
		return theme1;
	}

	public ArrayOfNote adaptNote(Request request) {
		// le WS DCRM ManageDemande ne gere pas l'utilisateur de la note, conformement au CI (OREKA-1747)
		// EntityReference utilisateur = null;
		// if ( request.getCustomerService() != null && StringUtils.isNotEmpty( request.getCustomerService().getGuid() )
		// )
		// {
		// utilisateur = new EntityReference();
		// utilisateur.setId( request.getCustomerService().getGuid() );
		// utilisateur.setLogicalName( Constantes.DCRM_LOGICAL_NAME_SYSTEM_USER );
		// // TODO LDE : utile (TEAM/USER) ?
		// // utilisateur.setName( objectFactory.createEntityReferenceName( request.getCustomerServiceType().name() )
		// // );
		// }
		ArrayOfNote notes = new ArrayOfNote();
		for (com.orangecaraibe.enabler.crm.business.bean.Note noteCrm : request.getNotes()) {
			Note note = new Note();
			// if ( utilisateur != null )
			// {
			// note.setUtilisateur( objectFactory.createNoteUtilisateur( utilisateur ) );
			// }
			note.setTexte(objectFactory.createNoteTexte(noteCrm.getNote()));
			note.setEstDocument(objectFactory.createNoteEstDocument(false));
			if (StringUtils.isNotEmpty(noteCrm.getNote())) {
				notes.getNote().add(note);
			}
		}
		for (Attachment attachment : request.getAttachments()) {
			PieceJointe pieceJointe = new PieceJointe();
			pieceJointe.setNom(objectFactory.createPieceJointeNom(attachment.getNom()));
			pieceJointe.setType(objectFactory.createPieceJointeType(attachment.getType()));
			pieceJointe.setDocument(objectFactory.createPieceJointeDocument(attachment.getDocument()));
			Note note = new Note();
			note.setEstDocument(objectFactory.createNoteEstDocument(true));
			note.setPieceJointe(objectFactory.createNotePieceJointe(pieceJointe));
			notes.getNote().add(note);
		}
		return notes;
	}

	/**
	 * Methode pour adapter une demande
	 */
	public Demande adaptUpdateDemande(Request request) {

		Demande demande = new Demande();
		demande.setGUIDDemande(objectFactory.createDemandeGUIDDemande(request.getGuid()));
		if (MapUtils.invertMap(requestStatusMap).containsKey(request.getStatus())) {
			Integer dcrmStatus = MapUtils.invertMap(requestStatusMap).get(request.getStatus());
			demande.setStatut(objectFactory.createDemandeStatut(dcrmStatus));
		}
		if (request.getStatusReason() != null) {
			demande.setRaisonStatut(objectFactory.createDemandeRaisonStatut(request.getStatusReason().getId()));
		} else if (request.getStatus() != null) {
			demande.setRaisonStatut(getRaisonFromStatus(request.getStatus()));
		}
		if (request.getCustomerService() != null && StringUtils.isNotEmpty(request.getCustomerService().getGuid())) {
			Proprietaire proprietaire = adaptBusinessCustomerService(request.getCustomerService());
			demande.setCorbeille(objectFactory.createDemandeCorbeille(proprietaire));
		}
		ArrayOfNote notes = adaptNote(request);
		demande.setNotes(objectFactory.createDemandeNotes(notes));
		demande.setApplication(objectFactory.createDemandeApplication(request.getChannel()));
		if (request.getContract() != null && StringUtils.isNotEmpty(request.getContract().getCoId())) {
			Contrat contrat = new Contrat();
			contrat.setIDContrat(objectFactory.createContratIDContrat(request.getContract().getCoId()));
			contrat.setMSISDN(objectFactory.createContratMSISDN(request.getContract().getMsisdn()));
			demande.setContrat(objectFactory.createContrat(contrat));
		}
		if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getId())) {
			demande.setIDOM(objectFactory.createDemandeIDOM(request.getOrder().getId()));
		}
		if (request.getInteractions() != null && !request.getInteractions().isEmpty()) {
			for (com.orangecaraibe.enabler.crm.business.bean.Interaction interaction : request.getInteractions()) {
				ArrayOfInteraction arrayOfInteraction = objectFactory.createArrayOfInteraction();
				Interaction interactionDcrm = objectFactory.createInteraction();
				interactionDcrm.setGUIDInteraction(objectFactory.createInteractionGUIDInteraction(interaction.getId()));
				arrayOfInteraction.getInteraction().add(interactionDcrm);
				JAXBElement<ArrayOfInteraction> arrayInteraction = objectFactory.createArrayOfInteraction(
						arrayOfInteraction);
				demande.setInteractions(arrayInteraction);
			}
		}
		if (request.getPriority() != null) {
			Integer key = 0;
			for (Entry<Integer, EnumPriority> map : priorityMap.entrySet()) {
				if (map.getValue().equals(request.getPriority())) {
					key = map.getKey();
					break;
				}
			}
			demande.setPriorite(objectFactory.createDemandePriorite(key));
		}
		return demande;
	}

	private JAXBElement<Integer> getRaisonFromStatus(EnumRequestStatus status) {

		if (status.equals(EnumRequestStatus.RESOLVED)) {
			return objectFactory.createDemandeRaisonStatut(EnumRequestStatusReason.CLOSED.getId());
		} else if (status.equals(EnumRequestStatus.ACTIVE)) {
			return objectFactory.createDemandeRaisonStatut(EnumRequestStatusReason.OPENED.getId());
		} else if (status.equals(EnumRequestStatus.CANCELLED)) {
			return objectFactory.createDemandeRaisonStatut(EnumRequestStatusReason.ABANDONED.getId());
		}
		return null;
	}

	public List<Request> adaptFindAndGetRequest(FindAndGetDemandeResponse response, Boolean includeNoteOrAttachment) {

		List<Request> requests = new ArrayList<Request>();
		if (response.getDemandes().getValue() != null) {
			for (Demande demande : response.getDemandes().getValue().getDemande()) {
				Request request = new Request();
				request.setGuid(demande.getGUIDDemande().getValue());
				request.setId(demande.getNumeroDemande().getValue());
				request.setTitle(demande.getTitre().getValue());
				request.setDescription(demande.getDescription().getValue());
				if (demande.getApplication() != null && demande.getApplication().getValue() != null) {
					request.setChannel(demande.getApplication().getValue());
				}
				// adaptation de l'interaction a laquelle est rattachee la demande
				if (demande.getInteractions() != null && demande.getInteractions()
						.getValue() != null && CollectionUtils.isNotEmpty(
						demande.getInteractions().getValue().getInteraction())) {
					List<Interaction> dcrmInteractions = demande.getInteractions().getValue().getInteraction();
					for (Interaction dcrmInteraction : dcrmInteractions) {
						com.orangecaraibe.enabler.crm.business.bean.Interaction businessInteraction = adaptDcrmInteractionLight(
								dcrmInteraction);
						request.getInteractions().add(businessInteraction);
					}
				}
				if (demande.getDateCreation().getValue() != null) {
					request.setCreationDate(demande.getDateCreation().getValue().toGregorianCalendar().getTime());
				}
				if (demande.getStatut() != null) {
					request.setStatus(EnumRequestStatus.getById(demande.getStatut().getValue()));
				}
				if (demande.getRaisonStatut() != null) {
					request.setStatusReason(EnumRequestStatusReason.getById(demande.getRaisonStatut().getValue()));
				}
				request.setKeyWord(demande.getMotClef().getValue());
				if (demande.getDateCreation().getValue() != null) {
					request.setCreationDate(
							DateUtils.getDateFromXmlGregorianCalendar(demande.getDateCreation().getValue()));
				}
				if (demande.getClotureLe().getValue() != null) {
					request.setCloseDate(DateUtils.getDateFromXmlGregorianCalendar(demande.getClotureLe().getValue()));
				}
				if (demande.getClientFacturation() != null) {
					ClientFacturation clFacturation = demande.getClientFacturation().getValue();
					if (clFacturation != null) {
						Account account = new Account();
						account.setCustCode(clFacturation.getCustcode().getValue());
						account.setAccountId(clFacturation.getCustomerID().getValue());
						request.setAccount(account);
					}
				}
				if (demande.getContrat() != null) {
					Contrat contrat = demande.getContrat().getValue();
					if (contrat != null) {
						Contract contract = new Contract();
						if (contrat.getIDContrat() != null && contrat.getIDContrat().getValue() != null) {
							contract.setCoId(contrat.getIDContrat().getValue());
						}
						if (contrat.getMSISDN() != null && contrat.getMSISDN().getValue() != null) {
							contract.setMsisdn(contrat.getMSISDN().getValue());
						}
						if (demande.getEquipementConcerne() != null && demande.getEquipementConcerne()
								.getValue() != null) {
							contract.getCellPhone().setModel(demande.getEquipementConcerne().getValue());
						}
						if (demande.getOffreConcernee() != null && demande.getOffreConcernee().getValue() != null) {
							contract.setOfferLabel(demande.getOffreConcernee().getValue());
						}
						request.setContract(contract);
					}
				}
				ThemeNiveau1 themeNiveau1 = demande.getThemeNiveau1().getValue();
				ThemeNiveau2 themeNiveau2 = themeNiveau1.getThemesNiveau2().getValue().getThemeNiveau2().get(0);
				ThemeNiveau3 themeNiveau3 = themeNiveau2.getThemesNiveau3().getValue().getThemeNiveau3().get(0);
				Theme th1 = new Theme();
				Theme th2 = new Theme();
				Theme th3 = new Theme();
				th3.setLabel(themeNiveau3.getLibelle().getValue());
				th3.setGuid(themeNiveau3.getGUIDThemeNiveau3());
				th2.setLabel(themeNiveau2.getLibelle().getValue());
				th2.setGuid(themeNiveau2.getGUIDThemeNiveau2());
				th2.getTheme().add(th3);
				th1.setLabel(themeNiveau1.getLibelle().getValue());
				th1.setGuid(themeNiveau1.getGUIDThemeNiveau1());
				th1.getTheme().add(th2);
				request.setTheme(th1);
				List<Attachment> attachments = new ArrayList<Attachment>();
				List<com.orangecaraibe.enabler.crm.business.bean.Note> requestNotes = new ArrayList<com.orangecaraibe.enabler.crm.business.bean.Note>();
				// Adaptation des notes et des pièces jointes
				if (demande.getNotes() != null && demande.getNotes().getValue() != null) {
					for (Note dcmNote : demande.getNotes().getValue().getNote()) {
						if (dcmNote.getGUIDNote() != null && dcmNote.getGUIDNote().getValue() != null) {
							// adaptation document
							if (dcmNote.getEstDocument() != null && dcmNote.getEstDocument()
									.getValue() != null && dcmNote.getEstDocument().getValue().booleanValue()) {
								Attachment attachment = adaptDcrmDocumentToAttachment(dcmNote);
								attachments.add(attachment);
							}
							// adaptation note
							else {
								com.orangecaraibe.enabler.crm.business.bean.Note businessNote = adaptDcrmNote(dcmNote);
								requestNotes.add(businessNote);
							}
						}
					}
				}
				// Adaptation du champ dernière note uniquement si on a pas demandé de note ou de pièce jointe dans
				// l'appel DCRM
				if (!includeNoteOrAttachment && demande.getDerniereNote() != null && demande.getDerniereNote()
						.getValue() != null) {
					com.orangecaraibe.enabler.crm.business.bean.Note businessDerniereNote = adaptDerniereNote(demande);
					requestNotes.add(businessDerniereNote);
				}
				request.getAttachments().addAll(attachments);
				request.getNotes().addAll(requestNotes);
				if (demande.getRecouvrement() != null) {
					Recouvrement recouvrement = demande.getRecouvrement().getValue();
					Recovery recovery = new Recovery();
					Facilities facilities = new Facilities();
					if (recouvrement.getDateDelai() != null && recouvrement.getDateDelai().getValue() != null) {
						Facility facility = new Facility();
						if (recouvrement.getMontantDelai() != null && recouvrement.getMontantDelai()
								.getValue() != null) {
							facility.setAmount(recouvrement.getMontantDelai().getValue().floatValue());
						}
						facility.setDateDelais(recouvrement.getDateDelai().getValue().toGregorianCalendar().getTime());
						facilities.setDelay(facility);
					}
					if (recouvrement.getDateEcheance1() != null && recouvrement.getDateEcheance1().getValue() != null) {
						List<Facility> facilitys = new ArrayList<Facility>();
						Facility facility = new Facility();
						if (recouvrement.getMontantEcheance1() != null && recouvrement.getMontantEcheance1()
								.getValue() != null) {
							facility.setAmount(recouvrement.getMontantEcheance1().getValue().floatValue());
						}
						facility.setDate(
								DateUtils.getDateFromXmlGregorianCalendar(recouvrement.getDateEcheance1().getValue()));
						if (recouvrement.getDateEcheance2() != null) {
							Facility facility2 = new Facility();
							if (recouvrement.getMontantEcheance1() != null && recouvrement.getMontantEcheance1()
									.getValue() != null) {
								facility2.setAmount(recouvrement.getMontantEcheance1().getValue().floatValue());
							}
							facility2.setDate(DateUtils.getDateFromXmlGregorianCalendar(
									recouvrement.getDateEcheance1().getValue()));
							facilitys.add(facility2);
						}
						if (recouvrement.getDateEcheance3() != null) {
							Facility facility3 = new Facility();
							if (recouvrement.getMontantEcheance1() != null && recouvrement.getMontantEcheance1()
									.getValue() != null) {
								facility3.setAmount(recouvrement.getMontantEcheance1().getValue().floatValue());
							}
							facility3.setDate(DateUtils.getDateFromXmlGregorianCalendar(
									recouvrement.getDateEcheance1().getValue()));
							facilitys.add(facility3);
						}
						facilitys.add(facility);
						facilities.setSchedules(facilitys);
					}
					recovery.setFacilities(facilities);
				}
				if (demande.getAvoirRemboursement() != null && demande.getAvoirRemboursement().getValue() != null) {
					AvoirRemboursement avoirRemboursement = demande.getAvoirRemboursement().getValue();
					Refund refund = new Refund();
					if (avoirRemboursement.getMontant() != null && avoirRemboursement.getMontant().getValue() != null) {
						refund.setAmount(avoirRemboursement.getMontant().getValue().floatValue());
					}
					refund.setCostCenter(avoirRemboursement.getCostCenter().getValue().getName().getValue());
					if (CollectionUtils.isNotEmpty(
							avoirRemboursement.getLignesComptables().getValue().getLigneComptable())) {
						refund.setGlCode(avoirRemboursement.getLignesComptables().getValue().getLigneComptable().get(0)
								.getGLCode().getValue().getName().getValue());
					}
					request.setRefund(refund);
				}
				// block de la gestion commercial modifié suite au changement de datacontract et de ManageDemande.wsdl
				if (demande.getGestesCommerciaux() != null && demande.getGestesCommerciaux().getValue() != null) {
					ArrayOfGesteCommercial gestesCommerciaux = demande.getGestesCommerciaux().getValue();
					for (GesteCommercial gesteCommercial : gestesCommerciaux.getGesteCommercial()) {
						if (gesteCommercial != null && gesteCommercial.getMontant() != null && gesteCommercial.getMontant()
								.getValue() != null) {
							CommercialGesture commercialGesture = new CommercialGesture();
							if (gesteCommercial.getMontant() != null && gesteCommercial.getMontant()
									.getValue() != null) {
								commercialGesture.setAmount(gesteCommercial.getMontant().getValue());
							}
							if (gesteCommercial.getRaisonStatut() != null && gesteCommercial.getRaisonStatut()
									.getValue() != null) {
								commercialGesture.setStatusReason(EnumCommercialGestureStatusReason.getById(
										gesteCommercial.getRaisonStatut().getValue()));
							}
							commercialGesture.setType(String.valueOf(gesteCommercial.getTypeGesteCommercial()));
							if (gesteCommercial.getNom() != null) {
								commercialGesture.setName(gesteCommercial.getNom().getValue());
							}
							request.setCommercialGesture(commercialGesture);
						}
					}
				}
				if (demande.getPropositionsRetentions() != null && demande.getPropositionsRetentions()
						.getValue() != null) {
					ArrayOfPropositionRetention propositionRetentions = demande.getPropositionsRetentions().getValue();
					for (PropositionRetention propositionRetention : propositionRetentions.getPropositionRetention()) {
						if (propositionRetention.getCode() != null && propositionRetention.getCode()
								.getValue() != null && propositionRetention.getNom() != null && propositionRetention.getNom()
								.getValue() != null && propositionRetention.getStatut() != null && propositionRetention.getStatut()
								.getValue() != null && EnumRetentionStatus.getValueFromDCRM(
								propositionRetention.getStatut().getValue()).equals(EnumRetentionStatus.VALIDE)) {
							RetentionOffer retentionOffer = new RetentionOffer();
							retentionOffer.setCode(propositionRetention.getCode().getValue());
							retentionOffer.setLabel(propositionRetention.getNom().getValue());
							if (propositionRetention.getMontant() != null) {
								retentionOffer.setAmount(propositionRetention.getMontant().doubleValue());
							}
							request.setRetentionOffer(retentionOffer);
						}
					}
				}
				if (demande.getPartyTitulaire() != null && demande.getPartyTitulaire().getValue() != null) {
					com.orange.sidom.soa.dcrm.datacontract.Party partyTitulaire = demande.getPartyTitulaire()
							.getValue();
					Holder holder = new Holder();
					holder.setGuid(partyTitulaire.getGUIDParty().getValue());
					holder.setFunctionalId(partyTitulaire.getNumeroParty().getValue());
					request.setHolder(holder);
				}
				if (demande.getCreePar() != null && demande.getCreePar().getValue() != null) {
					EntityReference creerPar = demande.getCreePar().getValue();
					CustomerService customerService = new CustomerService();
					customerService.setName(creerPar.getName().getValue());
					customerService.setGuid(creerPar.getId());
					request.setCustomerService(customerService);
				}
				if (demande.getCorbeille() != null && demande.getCorbeille()
						.getValue() != null && demande.getCorbeille().getValue()
						.getUtilisateurOuEquipe() != null && demande.getCorbeille().getValue().getUtilisateurOuEquipe()
						.getValue() != null) {
					EntityReference corbeille = demande.getCorbeille().getValue().getUtilisateurOuEquipe().getValue();
					if (corbeille != null && corbeille.getName() != null && corbeille.getName().getValue() != null) {
						request.setEquipeServicelabel(corbeille.getName().getValue());
					}
				}
				if (demande.getIdentifiantEquipedeService() != null && demande.getIdentifiantEquipedeService()
						.getValue() != null) {
					request.setEquipeServiceCode(demande.getIdentifiantEquipedeService().getValue());
				}
				if (demande.getIDOM() != null && demande.getIDOM().getValue() != null) {
					Order order = new Order();
					order.setId(demande.getIDOM().getValue());
					request.setOrder(order);
				}
				// TODO
				// Ajout des demandes filles
				if (demande.getDemandesFilles() != null && demande.getDemandesFilles()
						.getValue() != null && demande.getDemandesFilles().getValue().getDemandeFille() != null) {
					for (DemandeFille df : demande.getDemandesFilles().getValue().getDemandeFille()) {
						if (df != null) {
							ChildRequest cr = new ChildRequest();
							cr.setDemandeMere(request);
							// Themes
							ThemeNiveau1 themeNiveau1DF = df.getThemeNiveau1().getValue();
							ThemeNiveau2 themeNiveau2DF = themeNiveau1DF.getThemesNiveau2().getValue().getThemeNiveau2()
									.get(0);
							ThemeNiveau3 themeNiveau3DF = themeNiveau2DF.getThemesNiveau3().getValue().getThemeNiveau3()
									.get(0);
							Theme th1DF = new Theme();
							Theme th2DF = new Theme();
							Theme th3DF = new Theme();
							th3DF.setLabel(themeNiveau3DF.getLibelle().getValue());
							th3DF.setGuid(themeNiveau3DF.getGUIDThemeNiveau3());
							th2DF.setLabel(themeNiveau2DF.getLibelle().getValue());
							th2DF.setGuid(themeNiveau2DF.getGUIDThemeNiveau2());
							th2DF.getTheme().add(th3DF);
							th1DF.setLabel(themeNiveau1DF.getLibelle().getValue());
							th1DF.setGuid(themeNiveau1DF.getGUIDThemeNiveau1());
							th1DF.getTheme().add(th2DF);
							cr.setTheme(th1DF);
							Contract contract = new Contract();
							contract.setMsisdn(df.getNuméroDeTéléphone().getValue());
							cr.setContract(contract);
							// Description
							cr.setDescription(df.getDescription().getValue());
							Account account = new Account();
							account.setAccountId(df.getNuméroDeCompte().getValue());
							Holder holder = new Holder();
							holder.setFirstName(df.getNomDeClient().getValue());
							account.setHolder(holder);
							cr.setAccount(account);
							request.getDemandesFilles().add(cr);
						}
					}
				}
				if (demande.getTicketClientOceane() != null && StringUtils.isNotBlank(
						demande.getTicketClientOceane().getValue())) {
					request.setTroubleTicketID(demande.getTicketClientOceane().getValue());
				}
				// on adapte la mesure financière de la demande en resolutionAction
				com.orangecaraibe.enabler.crm.business.bean.ResolutionAction resolutionAction = adaptResolutionAction(
						demande);
				if (resolutionAction != null) {
					request.setResolutionAction(resolutionAction);
				}
				requests.add(request);
			}
		}
		return requests;
	}

	/**
	 * Adapt the DRM FindAndGetInteractionResponse
	 */
	public List<com.orangecaraibe.enabler.crm.business.bean.Interaction> adaptFindAndGetInteractionResponse(
			FindAndGetInteractionResponse findAndGetInteractionResponse) {

		List<com.orangecaraibe.enabler.crm.business.bean.Interaction> interactions = new ArrayList<com.orangecaraibe.enabler.crm.business.bean.Interaction>();
		if (findAndGetInteractionResponse.getInteractions() != null && findAndGetInteractionResponse.getInteractions()
				.getValue() != null && CollectionUtils.isNotEmpty(
				findAndGetInteractionResponse.getInteractions().getValue().getInteraction())) {
			ArrayOfInteraction arrayDcrmInteractions = findAndGetInteractionResponse.getInteractions().getValue();
			for (Interaction interactionDCRM : arrayDcrmInteractions.getInteraction()) {
				com.orangecaraibe.enabler.crm.business.bean.Interaction businessInteraction = adaptDcrmInteraction(
						interactionDCRM);
				interactions.add(businessInteraction);
			}
		}
		return interactions;
	}

	/**
	 * Adapte une interaction DCRM en interaction metier
	 */
	private com.orangecaraibe.enabler.crm.business.bean.Interaction adaptDcrmInteraction(Interaction dcrmInteraction) {

		com.orangecaraibe.enabler.crm.business.bean.Interaction businessInteraction = new com.orangecaraibe.enabler.crm.business.bean.Interaction();
		// mapping du type d'interaction en media
		if (interactionTypesMap.containsKey(dcrmInteraction.getClass())) {
			businessInteraction.setCanal(interactionTypesMap.get(dcrmInteraction.getClass()));
		}
		// application
		// Le DataModel du WS DCRM pourrait etre ameliore en definissant le champ application sur la classe
		// abstraite Interaction
		String application = getDCRMInteractionApplication(dcrmInteraction);
		if (application != null) {
			businessInteraction.setApplication(application);
		}
		// ID d'interaction
		businessInteraction.setId(dcrmInteraction.getGUIDInteraction().getValue());
		// description de l'interaction
		businessInteraction.setTitle(dcrmInteraction.getTitre().getValue());
		// date de creation de l'interaction
		if (dcrmInteraction.getEnregistrementCreeLe() != null && dcrmInteraction.getEnregistrementCreeLe()
				.getValue() != null) {
			businessInteraction.setCreationDate(
					DateUtils.getDateFromXmlGregorianCalendar(dcrmInteraction.getEnregistrementCreeLe().getValue()));
		}
		// sens de l'interaction
		if ((dcrmInteraction.getDirection() != null) && (dcrmInteraction.getDirection()
				.getValue() != null) && interactionWayMap.containsKey(
				dcrmInteraction.getDirection().getValue().booleanValue())) {
			businessInteraction.setWay(interactionWayMap.get(dcrmInteraction.getDirection().getValue().booleanValue()));
		}
		// statut de l'interaction
		if (dcrmInteraction.getStatut() != null && interactionStatusMap.containsKey(
				dcrmInteraction.getStatut().getValue())) {
			businessInteraction.setStatus(interactionStatusMap.get(dcrmInteraction.getStatut().getValue()));
		}
		// raison du statut
		// if ( dcrmInteraction.getRaisonStatut() != null
		// && interactionStatusMap.containsKey( dcrmInteraction.getStatut().getValue() ) )
		if (dcrmInteraction.getRaisonStatut() != null) {
			if (interactionReasonStatusPropertiesHelper.getStatusReasonMap().containsKey(
					dcrmInteraction.getClass()) && interactionReasonStatusPropertiesHelper.getStatusReasonMap()
					.get(dcrmInteraction.getClass()).containsKey(dcrmInteraction.getRaisonStatut().getValue())) {
				String businessStatusReason = interactionReasonStatusPropertiesHelper.getStatusReasonMap()
						.get(dcrmInteraction.getClass()).get(dcrmInteraction.getRaisonStatut());
				// businessStatusReason
				businessInteraction.setStatusReason(businessStatusReason);
			} else {
				businessInteraction.setStatusReason(
						Constantes.TO_BE_DEFINED + dcrmInteraction.getRaisonStatut().getValue());
			}
		}
		// demandes liees a l'interaction
		if (dcrmInteraction.getDemandes() != null && dcrmInteraction.getDemandes().getValue() != null) {
			List<com.orangecaraibe.enabler.crm.business.bean.Request> requests = adaptDcrmDemandesLight(
					dcrmInteraction.getDemandes().getValue().getDemande());
			businessInteraction.setRequests(requests);
		}
		// notes liees a l'interaction
		if (dcrmInteraction.getNotes() != null && dcrmInteraction.getNotes().getValue() != null) {
			// on filtre et on adapte les notes
			DcrmNoteIsDocumentPredicate isDocumentPredicate = new DcrmNoteIsDocumentPredicate();
			List<Note> filteredNotes = (List<Note>) CollectionUtils.selectRejected(
					dcrmInteraction.getNotes().getValue().getNote(), isDocumentPredicate);
			List<com.orangecaraibe.enabler.crm.business.bean.Note> notes = adaptDcrmNotes(filteredNotes);
			businessInteraction.setNotes(notes);
			// on filtre et on adapte les documents
			List<Note> filteredDocuments = (List<Note>) CollectionUtils.select(
					dcrmInteraction.getNotes().getValue().getNote(), isDocumentPredicate);
			List<Attachment> attachments = adaptDcrmDocumentsToAttachments(filteredDocuments);
			businessInteraction.setAttachments(attachments);
		}
		// champ specifique a l'interaction Email
		if (dcrmInteraction instanceof Email && (((Email) dcrmInteraction).getPiecesJointes() != null) && (((Email) dcrmInteraction).getPiecesJointes()
				.getValue() != null)) {
			List<PieceJointe> piecesJointes = ((Email) dcrmInteraction).getPiecesJointes().getValue().getPieceJointe();
			List<Attachment> attachments = adaptPiecesJointesToAttachments(piecesJointes);
			businessInteraction.getAttachments().addAll(attachments);
		}
		// sms
		if (dcrmInteraction instanceof SMS && dcrmInteraction.getDescription() != null) {
			businessInteraction.setDescription(dcrmInteraction.getDescription().getValue());
		}
		// adaptation des InteractionActor "from"
		if (dcrmInteraction.getDe() != null && dcrmInteraction.getDe().getValue() != null && CollectionUtils.isNotEmpty(
				dcrmInteraction.getDe().getValue().getActivityParty())) {
			List<Actor> businessParties = new ArrayList<Actor>();
			for (ActivityParty activityParty : dcrmInteraction.getDe().getValue().getActivityParty()) {
				Actor businessActor = adaptDcrmActivityPartyToBusinessActor(activityParty);
				businessParties.add(businessActor);
				// champ specifique a l'interaction appel telephonique - appel entrant
				if (dcrmInteraction instanceof AppelTelephonique && (((AppelTelephonique) dcrmInteraction).getNumeroTelephone() != null) && dcrmInteraction.getDirection()
						.getValue()) {
					String numeroTelephone = ((AppelTelephonique) dcrmInteraction).getNumeroTelephone().getValue();
					businessActor.getContactMethods().setMobilePhone(numeroTelephone);
				}
			}
			businessInteraction.setFrom(businessParties);
		}
		// adaptation des InteractionActor "to"
		if (dcrmInteraction.getA() != null && dcrmInteraction.getA().getValue() != null && CollectionUtils.isNotEmpty(
				dcrmInteraction.getA().getValue().getActivityParty())) {
			List<Actor> businessParties = new ArrayList<Actor>();
			for (ActivityParty activityParty : dcrmInteraction.getA().getValue().getActivityParty()) {
				Actor businessActor = adaptDcrmActivityPartyToBusinessActor(activityParty);
				businessParties.add(businessActor);
				// champ specifique a l'interaction appel telephonique - appel sortant
				if (dcrmInteraction instanceof AppelTelephonique && (((AppelTelephonique) dcrmInteraction).getNumeroTelephone() != null) && !dcrmInteraction.getDirection()
						.getValue()) {
					String numeroTelephone = ((AppelTelephonique) dcrmInteraction).getNumeroTelephone().getValue();
					businessActor.getContactMethods().setMobilePhone(numeroTelephone);
				}
			}
			businessInteraction.setTo(businessParties);
		}
		// adaptation du proprietaire de l'interaction
		if (dcrmInteraction.getProprietaire() != null) {
			Proprietaire proprietaire = dcrmInteraction.getProprietaire().getValue();
			CustomerService customerService = adaptDcrmInteractionOwner(proprietaire);
			businessInteraction.setCustomerService(customerService);
		}
		// adaptation du party titulaire
		if (dcrmInteraction.getPartyTitulaire() != null) {
			com.orange.sidom.soa.dcrm.datacontract.Party dcrmHolderParty = dcrmInteraction.getPartyTitulaire()
					.getValue();
			Holder businessHolder = new Holder();
			if (dcrmHolderParty.getGUIDParty() != null && StringUtils.isNotEmpty(
					dcrmHolderParty.getGUIDParty().getValue())) {
				businessHolder.setGuid(dcrmHolderParty.getGUIDParty().getValue());
			}
			if (dcrmHolderParty.getLibelle() != null && StringUtils.isNotEmpty(
					dcrmHolderParty.getLibelle().getValue())) {
				businessHolder.setLastName(dcrmHolderParty.getLibelle().getValue());
			}
			businessInteraction.setHolder(businessHolder);
		}
		// interaction de type notification
		if (dcrmInteraction.getNotification() != null && dcrmInteraction.getNotification().getValue() != null) {
			businessInteraction.setNotification(dcrmInteraction.getNotification().getValue());
		}
		// Récupération de l'id de courrier ALF
		if (dcrmInteraction instanceof Courrier) {
			if (((Courrier) dcrmInteraction).getIDCourrierALF() != null) {
				String idCourrier = String.valueOf(((Courrier) dcrmInteraction).getIDCourrierALF());
				businessInteraction.setCourrierId(idCourrier);
			}
		}
		if (dcrmInteraction instanceof ReseauSocial) {
			if (((ReseauSocial) dcrmInteraction).getReseau() != null && ((ReseauSocial) dcrmInteraction).getReseau()
					.getValue() != null) {
				Integer reseauSocial = ((ReseauSocial) dcrmInteraction).getReseau().getValue();
				businessInteraction.setSocialMediaType(
						EnumInteractionSocialMediaType.getInteractionTypeByCode(reseauSocial));
			}
		}
		return businessInteraction;
	}

	/**
	 * Adaptation light d'une interaction DCRM en interaction metier
	 */
	private com.orangecaraibe.enabler.crm.business.bean.Interaction adaptDcrmInteractionLight(
			Interaction dcrmInteraction) {

		com.orangecaraibe.enabler.crm.business.bean.Interaction businessInteraction = new com.orangecaraibe.enabler.crm.business.bean.Interaction();
		// mapping du type d'interaction en media
		if (interactionTypesMap.containsKey(dcrmInteraction.getClass())) {
			businessInteraction.setCanal(interactionTypesMap.get(dcrmInteraction.getClass()));
		}
		// ID d'interaction
		businessInteraction.setId(dcrmInteraction.getGUIDInteraction().getValue());
		// description de l'interaction
		businessInteraction.setTitle(dcrmInteraction.getTitre().getValue());
		// date de creation de l'interaction
		if (dcrmInteraction.getEnregistrementCreeLe() != null && dcrmInteraction.getEnregistrementCreeLe()
				.getValue() != null) {
			businessInteraction.setCreationDate(
					DateUtils.getDateFromXmlGregorianCalendar(dcrmInteraction.getEnregistrementCreeLe().getValue()));
		}
		// sens de l'interaction
		if (dcrmInteraction.getDirection() != null && interactionWayMap.containsKey(
				dcrmInteraction.getDirection().getValue().booleanValue())) {
			businessInteraction.setWay(interactionWayMap.get(dcrmInteraction.getDirection().getValue().booleanValue()));
		}
		return businessInteraction;
	}

	/**
	 * Extrait le champ application d'une interaction DCRM<br>
	 * TODO : methode a supprimer des que le dataContract des WS DCRM aura defini le champ application au niveau de la
	 * classe abstraite Interaction (Jira OREKA-1513)
	 */
	private String getDCRMInteractionApplication(Interaction dcrmInteraction) {

		String application = null;
		if (dcrmInteraction instanceof Fax) {
			if (((Fax) dcrmInteraction).getApplication() != null && StringUtils.isNotEmpty(
					((Fax) dcrmInteraction).getApplication().getValue())) {
				application = ((Fax) dcrmInteraction).getApplication().getValue();
			}
		} else if (dcrmInteraction instanceof Courrier) {
			if (((Courrier) dcrmInteraction).getApplication() != null && StringUtils.isNotEmpty(
					((Courrier) dcrmInteraction).getApplication().getValue())) {
				application = ((Courrier) dcrmInteraction).getApplication().getValue();
			}
		} else if (dcrmInteraction instanceof Email) {
			if (((Email) dcrmInteraction).getApplication() != null && StringUtils.isNotEmpty(
					((Email) dcrmInteraction).getApplication().getValue())) {
				application = ((Email) dcrmInteraction).getApplication().getValue();
			}
		} else if (dcrmInteraction instanceof AppelTelephonique) {
			if (((AppelTelephonique) dcrmInteraction).getApplication() != null && StringUtils.isNotEmpty(
					((AppelTelephonique) dcrmInteraction).getApplication().getValue())) {
				application = ((AppelTelephonique) dcrmInteraction).getApplication().getValue();
			}
		} else if (dcrmInteraction instanceof Selfcare) {
			if (((Selfcare) dcrmInteraction).getApplication() != null && StringUtils.isNotEmpty(
					((Selfcare) dcrmInteraction).getApplication().getValue())) {
				application = ((Selfcare) dcrmInteraction).getApplication().getValue();
			}
		} else if (dcrmInteraction instanceof SMS) {
			if (((SMS) dcrmInteraction).getApplication() != null && StringUtils.isNotEmpty(
					((SMS) dcrmInteraction).getApplication().getValue())) {
				application = ((SMS) dcrmInteraction).getApplication().getValue();
			}
		} else if (dcrmInteraction instanceof PDVPhysique) {
			if (((PDVPhysique) dcrmInteraction).getApplication() != null && StringUtils.isNotEmpty(
					((PDVPhysique) dcrmInteraction).getApplication().getValue())) {
				application = ((PDVPhysique) dcrmInteraction).getApplication().getValue();
			}
		}
		return application;
	}

	public Interaction adaptBusinessInteraction(
			com.orangecaraibe.enabler.crm.business.bean.Interaction businessInteraction) throws Exception {

		Interaction dcrmInteraction = null;
		if (MapUtils.invertMap(interactionTypesMap).containsKey(businessInteraction.getCanal())) {
			// creation de l'interaction DCRM specialisee correspondant au canal
			try {
				Class<? extends Interaction> interactionClass = MapUtils.invertMap(interactionTypesMap)
						.get(businessInteraction.getCanal());
				dcrmInteraction = interactionClass.newInstance();
			}
			catch (InstantiationException | IllegalAccessException e) {
				LOGGER.error(
						"InstantiationException or IllegalAccessException occurred while creating the specialised DCRM interaction",
						e);
			}
		} else {
			dcrmInteraction = new Interaction();
		}
		// Adaptation de l'application
		if (StringUtils.isNotEmpty(businessInteraction.getApplication())) {
			// TODO - Le DataModel du WS DCRM pourrait etre ameliore en definissant le champ application sur la
			// classe abstraite Interaction
			String application = businessInteraction.getApplication();
			if (dcrmInteraction instanceof Fax) {
				((Fax) dcrmInteraction).setApplication(objectFactory.createFaxApplication(application));
			} else if (dcrmInteraction instanceof Courrier) {
				((Courrier) dcrmInteraction).setApplication(objectFactory.createCourrierApplication(application));
			} else if (dcrmInteraction instanceof Email) {
				((Email) dcrmInteraction).setApplication(objectFactory.createEmailApplication(application));
			} else if (dcrmInteraction instanceof AppelTelephonique) {
				((AppelTelephonique) dcrmInteraction).setApplication(
						objectFactory.createAppelTelephoniqueApplication(application));
			} else if (dcrmInteraction instanceof Selfcare) {
				((Selfcare) dcrmInteraction).setApplication(objectFactory.createSelfcareApplication(application));
				// si le destinataire de l'interaction n'est pas renseigne, on le complete avec le GUID du
				// systemuser par defaut defini en conf
			} else if (dcrmInteraction instanceof SMS) {
				((SMS) dcrmInteraction).setApplication(objectFactory.createSMSApplication(application));
				dcrmInteraction.setDescription(
						objectFactory.createInteractionDescription(businessInteraction.getDescription()));
			} else if (dcrmInteraction instanceof PDVPhysique) {
				((PDVPhysique) dcrmInteraction).setApplication(objectFactory.createPDVPhysiqueApplication(application));
			} else if (dcrmInteraction instanceof ReseauSocial) {
				((ReseauSocial) dcrmInteraction).setApplication(
						objectFactory.createReseauSocialApplication(application));
			}
		}
		// RUNO-2081 : le num de l'appelant n'était pas renseigné dans le DCRM
		// Gestion du numéro appelant
		if (StringUtils.isNotEmpty(businessInteraction.getNumeroTelephoneAppelant())) {
			String numeroTelephoneAppelant = businessInteraction.getNumeroTelephoneAppelant();
			if (dcrmInteraction instanceof AppelTelephonique) {
				((AppelTelephonique) dcrmInteraction).setNumeroTelephone(
						objectFactory.createAppelTelephoniqueNumeroTelephone(numeroTelephoneAppelant));
			} else if (dcrmInteraction instanceof SMS) {
				((SMS) dcrmInteraction).setNumeroTelephone(objectFactory.createAppelTelephoniqueNumeroTelephone(
						businessInteraction.getNumeroTelephoneAppelant()));
			}
		}
		// RUNO-2081 : le num de l'appelant n'était pas renseigné dans le DCRM
		// Gestion du numéros appelé
		if (StringUtils.isNotEmpty(businessInteraction.getNumeroTelephoneAppele())) {
			String numeroTelephoneAppele = businessInteraction.getNumeroTelephoneAppele();
			if (dcrmInteraction instanceof AppelTelephonique) {
				((AppelTelephonique) dcrmInteraction).setNumeroTelephone(
						objectFactory.createAppelTelephoniqueNumeroTelephone(numeroTelephoneAppele));
			} else if (dcrmInteraction instanceof SMS) {
				((SMS) dcrmInteraction).setNumeroTelephone(objectFactory.createAppelTelephoniqueNumeroTelephone(
						businessInteraction.getNumeroTelephoneAppele()));
			}
		}
		// Instanciation du type de réseau social pour les intéractions de type reseaux sociaux
		if (dcrmInteraction instanceof ReseauSocial) {
			if (businessInteraction.getSocialMediaType() != null) {
				((ReseauSocial) dcrmInteraction).setReseau(
						objectFactory.createReseauSocialReseau(businessInteraction.getSocialMediaType().getCode()));
			}
		}
		if (CollectionUtils.isEmpty(businessInteraction.getTo())) {
			CustomerService customerService = new CustomerService();
			customerService.setGuid(defaultSystemUserGuid);
			businessInteraction.getTo().add(customerService);
		} else {
			boolean haveGuid = false;
			for (Actor actor : businessInteraction.getTo()) {
				if (actor.getGuid() != null) {
					haveGuid = true;
				}
			}
			if (!haveGuid) {
				CustomerService customerService = new CustomerService();
				customerService.setGuid(defaultSystemUserGuid);
				businessInteraction.getTo().add(customerService);
			}
		}
		if (CollectionUtils.isEmpty(businessInteraction.getFrom())) {
			CustomerService customerService = new CustomerService();
			customerService.setGuid(defaultSystemUserGuid);
			businessInteraction.getFrom().add(customerService);
		} else {
			boolean haveGuid = false;
			for (Actor actor : businessInteraction.getFrom()) {
				if (actor.getGuid() != null) {
					haveGuid = true;
				}
			}
			if (!haveGuid) {
				CustomerService customerService = new CustomerService();
				customerService.setGuid(defaultSystemUserGuid);
				businessInteraction.getFrom().add(customerService);
			}
		}
		// adaptation de l'emetteur voire des emetteurs de l'interaction
		if (CollectionUtils.isNotEmpty(businessInteraction.getFrom())) {
			ArrayOfActivityParty arrayOfActivityParty = adaptBusinessPartyListToArrayOfActivityParty(
					businessInteraction.getFrom());
			if (CollectionUtils.isNotEmpty(arrayOfActivityParty.getActivityParty())) {
				dcrmInteraction.setDe(objectFactory.createInteractionDe(arrayOfActivityParty));
			}
		}
		// adaptation du destinataire voire des destinataires de l'interaction
		if (CollectionUtils.isNotEmpty(businessInteraction.getTo())) {
			ArrayOfActivityParty arrayOfActivityParty = adaptBusinessPartyListToArrayOfActivityParty(
					businessInteraction.getTo());
			if (CollectionUtils.isNotEmpty(arrayOfActivityParty.getActivityParty())) {
				dcrmInteraction.setA(objectFactory.createInteractionA(arrayOfActivityParty));
			}
		}
		dcrmInteraction.setNotification(
				objectFactory.createInteractionNotification(businessInteraction.isNotification()));
		dcrmInteraction.setTitre(objectFactory.createInteractionTitre(businessInteraction.getTitle()));
		dcrmInteraction.setGUIDInteraction(objectFactory.createInteractionGUIDInteraction(businessInteraction.getId()));
		dcrmInteraction.setDirection(objectFactory.createInteractionDirection(
				(MapUtils.invertMap(interactionWayMap).get(businessInteraction.getWay()))));
		Integer dcrmStatut = MapUtils.invertMap(interactionStatusMap).get(businessInteraction.getStatus());
		dcrmInteraction.setStatut(objectFactory.createInteractionStatut(dcrmStatut));
		dcrmInteraction.setEnregistrementCreeLe(objectFactory.createInteractionEnregistrementCreeLe(
				DateUtils.convertDateToXMLGregorianCalendar(businessInteraction.getCreationDate())));
		DefaultInteractionStatusReasonBean statusReasonBean = new DefaultInteractionStatusReasonBean();
		statusReasonBean.setChannel(businessInteraction.getCanal());
		statusReasonBean.setWay(businessInteraction.getWay());
		statusReasonBean.setStatus(businessInteraction.getStatus());
		// raison du statut
		Integer dcrmStatusReason = null;
		// si la status reason a ete fournie, on recherche la valeur DCRM associe dans les Map
		if (StringUtils.isNotEmpty(
				businessInteraction.getStatusReason()) && interactionReasonStatusPropertiesHelper.getStatusReasonMap()
				.containsKey(dcrmInteraction.getClass())) {
			Map<Integer, String> statusReasonForThisType = interactionReasonStatusPropertiesHelper.getStatusReasonMap()
					.get(dcrmInteraction.getClass());
			if (MapUtils.invertMap(statusReasonForThisType).containsKey(businessInteraction.getStatusReason())) {
				dcrmStatusReason = MapUtils.invertMap(statusReasonForThisType)
						.get(businessInteraction.getStatusReason());
			}
		}
		// si la raison statut n'est pas fournie ou trouvee, on recherche la valeur par defaut
		if (dcrmStatusReason == null) {
			dcrmStatusReason = getDefaultInteractionStatusReason(dcrmInteraction.getClass(), statusReasonBean);
		}
		if (dcrmStatusReason != null) {
			dcrmInteraction.setRaisonStatut(objectFactory.createInteractionRaisonStatut(dcrmStatusReason));
		} else {
			throw new Exception(
					"probleme de configuration : valeur du reasonStatus par defaut manquante pour ce type d'interaction");
		}

		/*
		 * Adaptation des notes et attachements
		 */
		// adaptation des notes
		ArrayOfNote notes = null;
		ArrayOfPieceJointe pjs = null;
		if (CollectionUtils.isNotEmpty(businessInteraction.getNotes())) {
			notes = new ArrayOfNote();
			List<Note> dcrmNotes = adaptBusinessNotes(businessInteraction.getNotes());
			notes.getNote().addAll(dcrmNotes);
		}
		// adaptation des attachements
		if (CollectionUtils.isNotEmpty(businessInteraction.getAttachments())) {
			// Dans le cas d'un email, il faut mettre directement dans la structure PieceJointe
			if (dcrmInteraction instanceof Email) {
				if (pjs == null) {
					pjs = new ArrayOfPieceJointe();
				}
				List<PieceJointe> piecesJointes = adaptBusinessAttachmentsToAttachments(
						businessInteraction.getAttachments());
				pjs.getPieceJointe().addAll(piecesJointes);
				if (pjs != null) {
					((Email) dcrmInteraction).setPiecesJointes(objectFactory.createEmailPiecesJointes(pjs));
				}
			} else {
				// les attachements sont adaptes en note contenant un attachement
				if (notes == null) {
					notes = new ArrayOfNote();
				}
				List<Note> dcrmNotes = adaptBusinessAttachments(businessInteraction.getAttachments());
				notes.getNote().addAll(dcrmNotes);
			}
		}
		// on sette les notes, incluant les notes avec atttachement
		if (notes != null) {
			dcrmInteraction.setNotes(objectFactory.createInteractionNotes(notes));
		}
		// adaptation du titulaire
		if (businessInteraction.getHolder() != null) {
			if (StringUtils.isNotEmpty(businessInteraction.getHolder().getGuid())) {
				com.orange.sidom.soa.dcrm.datacontract.Party titulaire = new com.orange.sidom.soa.dcrm.datacontract.Party();
				titulaire.setGUIDParty(objectFactory.createPartyGUIDParty(businessInteraction.getHolder().getGuid()));
				dcrmInteraction.setPartyTitulaire(objectFactory.createInteractionPartyTitulaire(titulaire));
			}
		}
		// adaptation du customer service representant le conseiller/utilisateur ou l'equipe en charge de l'interaction
		if (businessInteraction.getCustomerService() != null) {
			Proprietaire proprietaire = null;
			if (businessInteraction.getCustomerService() instanceof CustomerServicePerson) {
				CustomerServicePerson csrPerson = (CustomerServicePerson) businessInteraction.getCustomerService();
				proprietaire = new Utilisateur();
				if (StringUtils.isNotEmpty(csrPerson.getLogin())) {
					((Utilisateur) proprietaire).setLogin(objectFactory.createUtilisateurLogin(csrPerson.getLogin()));
				}
			} else if (businessInteraction.getCustomerService() instanceof CustomerServiceOrganisation) {
				proprietaire = new Equipe();
			} else {
				proprietaire = new Proprietaire();
			}
			// on sette l'utilisateurOuEquipe uniquement s'il a un GUID dans le DCRM
			if (StringUtils.isNotEmpty(businessInteraction.getCustomerService().getGuid())) {
				EntityReference utilisateurOuEquipe = new EntityReference();
				utilisateurOuEquipe.setLogicalName(Constantes.DCRM_LOGICAL_NAME_SYSTEM_USER);
				utilisateurOuEquipe.setId(businessInteraction.getCustomerService().getGuid());
				proprietaire.setUtilisateurOuEquipe(
						objectFactory.createProprietaireUtilisateurOuEquipe(utilisateurOuEquipe));
			} // sinon pour les vendeurs du canal BTK, on ne fait rien
			dcrmInteraction.setProprietaire(objectFactory.createInteractionProprietaire(proprietaire));
		}
		// adaptation des demandes liees a l'interaction
		if (CollectionUtils.isNotEmpty(businessInteraction.getRequests())) {
			List<Request> requests = businessInteraction.getRequests();
			ArrayOfDemande demandes = new ArrayOfDemande();
			for (Request request : requests) {
				Demande demande = new Demande();
				if (StringUtils.isNotEmpty(request.getGuid())) {
					demande.setGUIDDemande(objectFactory.createDemandeGUIDDemande(request.getGuid()));
				} else {
					demande = adaptCreateDemande(request);
				}
				demandes.getDemande().add(demande);
			}
			JAXBElement<ArrayOfDemande> jaxbElementDemandes = objectFactory.createInteractionDemandes(demandes);
			dcrmInteraction.setDemandes(jaxbElementDemandes);
		}
		// Récupération de l'id de courrier ALF
		if (dcrmInteraction instanceof Courrier) {
			if (businessInteraction.getCourrierId() != null) {
				JAXBElement<String> jaxbIDCourrier = objectFactory.createCourrierIDCourrierALF(
						businessInteraction.getCourrierId());
				((Courrier) dcrmInteraction).setIDCourrierALF(jaxbIDCourrier);
			}
			for (Actor actor : businessInteraction.getTo()) {
				if (actor.getContactMethods() != null) {
					((Courrier) dcrmInteraction).setAdresse(objectFactory.createCourrierAdresse(
							adapteBusinessPartyContactMethode(actor.getContactMethods())));
				}
			}
		}
		return dcrmInteraction;
	}

	private List<PieceJointe> adaptBusinessAttachmentsToAttachments(List<Attachment> businessAttachments) {

		List<PieceJointe> pjs = null;
		if (CollectionUtils.isNotEmpty(businessAttachments)) {
			pjs = new ArrayList<PieceJointe>();
			for (Attachment businessAttachment : businessAttachments) {
				PieceJointe pj = adaptBusinessAttachmentToAttachment(businessAttachment);
				pjs.add(pj);
			}
		}
		return pjs;
	}

	private PieceJointe adaptBusinessAttachmentToAttachment(Attachment businessAttachment) {

		PieceJointe pieceJointe = new PieceJointe();
		pieceJointe.setNom(objectFactory.createPieceJointeNom(businessAttachment.getNom()));
		pieceJointe.setType(objectFactory.createPieceJointeType(businessAttachment.getType()));
		pieceJointe.setDocument(objectFactory.createPieceJointeDocument(businessAttachment.getDocument()));
		return pieceJointe;
	}

	/**
	 * Adapte une liste de notes business en notes DCRM
	 */
	private List<Note> adaptBusinessNotes(List<com.orangecaraibe.enabler.crm.business.bean.Note> notes) {

		List<Note> dcrmNotes = new ArrayList<Note>();
		for (com.orangecaraibe.enabler.crm.business.bean.Note note : notes) {
			Note dcrmNote = adaptBusinessNote(note);
			dcrmNotes.add(dcrmNote);
		}
		return dcrmNotes;
	}

	/**
	 * Adapte une note business en note DCRM
	 */
	private Note adaptBusinessNote(com.orangecaraibe.enabler.crm.business.bean.Note businessNote) {

		Note dcrmNote = new Note();
		dcrmNote.setGUIDNote(objectFactory.createNoteGUIDNote(businessNote.getUserGuid()));
		dcrmNote.setTexte(objectFactory.createNoteTexte(businessNote.getNote()));
		if (StringUtils.isNotEmpty(businessNote.getUserGuid()) || StringUtils.isNotEmpty(businessNote.getUserName())) {
			EntityReference utilisateur = new EntityReference();
			if (StringUtils.isNotEmpty(businessNote.getUserGuid())) {
				utilisateur.setId(businessNote.getUserGuid());
			}
			if (StringUtils.isNotEmpty(businessNote.getUserName())) {
				utilisateur.setName(objectFactory.createEntityReferenceName(businessNote.getUserName()));
			}
			dcrmNote.setUtilisateur(objectFactory.createNoteUtilisateur(utilisateur));
		}
		return dcrmNote;
	}

	/**
	 * Adapte une liste d'attachement business en pieces jointes DCRM
	 */
	private List<Note> adaptBusinessAttachments(List<Attachment> businessAttachments) {

		List<Note> notes = null;
		if (CollectionUtils.isNotEmpty(businessAttachments)) {
			notes = new ArrayList<Note>();
			for (Attachment businessAttachment : businessAttachments) {
				Note note = adaptBusinessAttachment(businessAttachment);
				notes.add(note);
			}
		}
		return notes;
	}

	/**
	 * Adapte un attachement business en piece jointe DCRM
	 */
	private Note adaptBusinessAttachment(Attachment businessAttachment) {

		PieceJointe pieceJointe = new PieceJointe();
		pieceJointe.setNom(objectFactory.createPieceJointeNom(businessAttachment.getNom()));
		pieceJointe.setType(objectFactory.createPieceJointeType(businessAttachment.getType()));
		pieceJointe.setDocument(objectFactory.createPieceJointeDocument(businessAttachment.getDocument()));
		Note note = null;
		if (businessAttachment.getNote() != null) {
			note = adaptBusinessNote(businessAttachment.getNote());
		} else {
			note = new Note();
		}
		note.setEstDocument(objectFactory.createNoteEstDocument(true));
		note.setPieceJointe(objectFactory.createNotePieceJointe(pieceJointe));
		return note;
	}

	/**
	 * Adapte un customer service business en proprietaire DCRM
	 */
	private Proprietaire adaptBusinessCustomerService(CustomerService customerService) {

		EntityReference utilisateurOuEquipe = new EntityReference();
		Proprietaire proprietaire = null;
		utilisateurOuEquipe.setId(customerService.getGuid());
		utilisateurOuEquipe.setLogicalName(Constantes.DCRM_LOGICAL_NAME_SYSTEM_USER);
		if (customerService instanceof CustomerServicePerson) {
			CustomerServicePerson csrPerson = (CustomerServicePerson) customerService;
			proprietaire = new Utilisateur();
			if (StringUtils.isNotEmpty(csrPerson.getLogin())) {
				((Utilisateur) proprietaire).setLogin(objectFactory.createUtilisateurLogin(csrPerson.getLogin()));
			}
		} else if (customerService instanceof CustomerServiceOrganisation) {
			proprietaire = new Equipe();
		} else {
			proprietaire = new Proprietaire();
		}
		proprietaire.setUtilisateurOuEquipe(objectFactory.createProprietaireUtilisateurOuEquipe(utilisateurOuEquipe));
		return proprietaire;
	}

	private List<Request> adaptDcrmDemandesLight(List<Demande> demandes) {

		List<com.orangecaraibe.enabler.crm.business.bean.Request> businessRequests = new ArrayList<com.orangecaraibe.enabler.crm.business.bean.Request>();
		for (Demande demande : demandes) {
			com.orangecaraibe.enabler.crm.business.bean.Request request = adaptDcrmDemandeLight(demande);
			businessRequests.add(request);
		}
		return businessRequests;
	}

	private Request adaptDcrmDemandeLight(Demande demande) {

		com.orangecaraibe.enabler.crm.business.bean.Request businessRequest = new com.orangecaraibe.enabler.crm.business.bean.Request();
		// GUID de la demande
		if (demande.getGUIDDemande() != null && StringUtils.isNotEmpty(demande.getGUIDDemande().getValue())) {
			businessRequest.setGuid(demande.getGUIDDemande().getValue());
		}
		// ID fonctionnel de la demande
		if (demande.getNumeroDemande() != null && StringUtils.isNotEmpty(demande.getNumeroDemande().getValue())) {
			businessRequest.setId(demande.getNumeroDemande().getValue());
		}
		// Priorite
		if (demande.getPriorite() != null && demande.getPriorite().getValue() != null && priorityMap.containsKey(
				demande.getPriorite().getValue())) {
			EnumPriority businessPriority = priorityMap.get(demande.getPriorite().getValue());
			businessRequest.setPriority(businessPriority);
		}
		businessRequest.setTitle(demande.getTitre().getValue());
		businessRequest.setDescription(demande.getDescription().getValue());
		if (demande.getDateCreation().getValue() != null) {
			businessRequest.setCreationDate(demande.getDateCreation().getValue().toGregorianCalendar().getTime());
		}
		if (demande.getStatut() != null && demande.getStatut().getValue() != null) {
			businessRequest.setStatus(EnumRequestStatus.getById(demande.getStatut().getValue()));
		}
		if (demande.getRaisonStatut() != null && demande.getRaisonStatut().getValue() != null) {
			businessRequest.setStatusReason(EnumRequestStatusReason.getById(demande.getStatut().getValue()));
		}
		ThemeNiveau1 themeNiveau1 = demande.getThemeNiveau1().getValue();
		ThemeNiveau2 themeNiveau2 = null;
		ThemeNiveau3 themeNiveau3 = null;
		if (themeNiveau1.getThemesNiveau2() != null && themeNiveau1.getThemesNiveau2().getValue() != null) {
			themeNiveau2 = themeNiveau1.getThemesNiveau2().getValue().getThemeNiveau2().get(0);
			if (themeNiveau2.getThemesNiveau3() != null && themeNiveau2.getThemesNiveau3()
					.getValue() != null && CollectionUtils.isNotEmpty(
					themeNiveau2.getThemesNiveau3().getValue().getThemeNiveau3())) {
				themeNiveau3 = themeNiveau2.getThemesNiveau3().getValue().getThemeNiveau3().get(0);
			}
		}
		Theme th1 = new Theme();
		Theme th2 = new Theme();
		Theme th3 = new Theme();
		if (themeNiveau3 != null) {
			th3.setLabel(themeNiveau3.getLibelle().getValue());
			th3.setGuid(themeNiveau3.getGUIDThemeNiveau3());
		}
		if (themeNiveau2 != null) {
			th2.setLabel(themeNiveau2.getLibelle().getValue());
			th2.setGuid(themeNiveau2.getGUIDThemeNiveau2());
			th2.getTheme().add(th3);
		}
		th1.setLabel(themeNiveau1.getLibelle().getValue());
		th1.setGuid(themeNiveau1.getGUIDThemeNiveau1());
		th1.getTheme().add(th2);
		businessRequest.setTheme(th1);
		if (demande.getClientFacturation() != null && demande.getClientFacturation().getValue() != null) {
			Account account = new Account();
			ClientFacturation clientFacturation = demande.getClientFacturation().getValue();
			if (clientFacturation.getCustomerID() != null && clientFacturation.getCustomerID().getValue() != null) {
				account.setAccountId(clientFacturation.getCustomerID().getValue());
			}
			businessRequest.setAccount(account);
		}
		if (demande.getContrat() != null && demande.getContrat().getValue() != null) {
			Contract contractBussinnes = new Contract();
			Contrat contract = demande.getContrat().getValue();
			if (contract.getIDContrat() != null && contract.getIDContrat().getValue() != null) {
				contractBussinnes.setCoId(contract.getIDContrat().getValue());
				// TMAOREKA-286
				businessRequest.setContract(contractBussinnes);
			}
		}
		// on récupère la dernière Note de la demande
		com.orangecaraibe.enabler.crm.business.bean.Note derniereNote = adaptDerniereNote(demande);
		if (derniereNote != null) {
			List<com.orangecaraibe.enabler.crm.business.bean.Note> notes = new ArrayList<com.orangecaraibe.enabler.crm.business.bean.Note>();
			notes.add(derniereNote);
			businessRequest.setNotes(notes);
		}
		// on adapte la mesure financière de la demande en resolutionAction
		com.orangecaraibe.enabler.crm.business.bean.ResolutionAction resolutionAction = adaptResolutionAction(demande);
		if (resolutionAction != null) {
			businessRequest.setResolutionAction(resolutionAction);
		}
		return businessRequest;
	}

	/**
	 * adapte les éléments de la demande qui correspondent à une mesure financière en {@link ResolutionAction}
	 *
	 * @return {@link ResolutionAction} qui correspond à une mesure financière
	 */
	private com.orangecaraibe.enabler.crm.business.bean.ResolutionAction adaptResolutionAction(Demande demande) {

		com.orangecaraibe.enabler.crm.business.bean.ResolutionAction resolutionAction = new com.orangecaraibe.enabler.crm.business.bean.ResolutionAction();
		// on vérifie si une mesure financière est appliqué à la demande
		if (demande.isMesuresFianciereAppliquee() != null && demande.isMesuresFianciereAppliquee()) {
			// si oui, la resolutionAction est de type MESFIN
			resolutionAction.setLocalActionType(EnumLocalActionType.MEFIN.toString());
			if (demande.getMesureFinanciereDescription() != null && demande.getMesureFinanciereDescription()
					.getValue() != null) {
				// on récupère la description de la mesure financière
				resolutionAction.setActionDescription(demande.getMesureFinanciereDescription().getValue());
			}
			if (demande.getMesureFinanciereType() != null && demande.getMesureFinanciereType().getValue() != null) {
				// on récupère le type de la mesure financière
				resolutionAction.setActionResult(demande.getMesureFinanciereType().getValue());
			}
		}
		return resolutionAction;
	}

	/**
	 * Fonction d'adaptation du champ string DCRM dernierenote en objet Note SOA
	 *
	 * @param demande
	 * 		: Objet demande DCRM contenant le champ dernier note
	 * @return Un objet SOA Note contenant le champ dernierenote comme valeur du label de l'objet SOA
	 */
	private com.orangecaraibe.enabler.crm.business.bean.Note adaptDerniereNote(Demande demande) {

		com.orangecaraibe.enabler.crm.business.bean.Note businessDerniereNote = null;
		if (demande.getDerniereNote() != null && demande.getDerniereNote().getValue() != null) {
			Note derniereNote = new Note();
			derniereNote.setTexte(demande.getDerniereNote());
			businessDerniereNote = adaptDcrmNote(derniereNote);
		}
		return businessDerniereNote;
	}

	private List<com.orangecaraibe.enabler.crm.business.bean.Note> adaptDcrmNotes(List<Note> notes) {

		List<com.orangecaraibe.enabler.crm.business.bean.Note> businessNotes = new ArrayList<com.orangecaraibe.enabler.crm.business.bean.Note>();
		for (Note note : notes) {
			com.orangecaraibe.enabler.crm.business.bean.Note businessNote = adaptDcrmNote(note);
			businessNotes.add(businessNote);
		}
		return businessNotes;
	}

	/**
	 * Adapte une note DCRM en note business
	 */
	private com.orangecaraibe.enabler.crm.business.bean.Note adaptDcrmNote(Note note) {

		com.orangecaraibe.enabler.crm.business.bean.Note businessNote = new com.orangecaraibe.enabler.crm.business.bean.Note();
		// GUID de la note
		if (note.getGUIDNote() != null && StringUtils.isNotEmpty(note.getGUIDNote().getValue())) {
			businessNote.setGuid(note.getGUIDNote().getValue());
		}
		// objet de la note
		if (note.getSujet() != null && StringUtils.isNotEmpty(note.getSujet().getValue())) {
			businessNote.setSubject(note.getSujet().getValue());
		}
		// texte de la note
		if (note.getTexte() != null && StringUtils.isNotEmpty(note.getTexte().getValue())) {
			businessNote.setNote(note.getTexte().getValue());
		}
		// date de creation de la note
		if (note.getDateCreation() != null && note.getDateCreation().getValue() != null) {
			businessNote.setDateCreation(DateUtils.getDateFromXmlGregorianCalendar(note.getDateCreation().getValue()));
		}
		// conseiller qui a pose la note
		adaptDcrmUtilisateurToUserFromNote(note, businessNote);
		return businessNote;
	}

	/**
	 * Adapatation de la notion de utilisateur depuis le DCRM vers user d'une note dans le modèle soa
	 *
	 * @param note
	 * 		: Objet DCRM représentant la note / pièce jointe contenant l'utilisateur
	 * @param businessNote
	 * 		: Objet SOA qui représente la note / pièce jointe qui contiendra le user
	 */
	private void adaptDcrmUtilisateurToUserFromNote(Note note,
			com.orangecaraibe.enabler.crm.business.bean.Note businessNote) {

		if (note.getUtilisateur() != null && note.getUtilisateur().getValue() != null) {
			EntityReference dcrmUser = note.getUtilisateur().getValue();
			businessNote.setUserGuid(dcrmUser.getId());
			businessNote.setUserName(dcrmUser.getName().getValue());
		}
	}

	private List<Attachment> adaptDcrmDocumentsToAttachments(List<Note> notes) {

		List<Attachment> businessAttachments = new ArrayList<Attachment>();
		for (Note note : notes) {
			Attachment businessAttachment = adaptDcrmDocumentToAttachment(note);
			businessAttachments.add(businessAttachment);
		}
		return businessAttachments;
	}

	/**
	 * Adapte une note DCRM de type Document en Attachement business
	 */
	private Attachment adaptDcrmDocumentToAttachment(Note dcrmNote) {
		// adaptation de la piece jointe
		Attachment attachment = adaptPieceJointeToAttachment(dcrmNote.getPieceJointe().getValue());
		// conseiller utilisateur lie a la note
		EntityReference user = dcrmNote.getUtilisateur().getValue();
		attachment.setUserGuid(user.getId());
		attachment.setUserName(user.getName().getValue());
		// GUID de la note
		attachment.setGuid(dcrmNote.getGUIDNote().getValue());
		// date de creation de la note
		if (dcrmNote.getDateCreation().getValue() != null) {
			attachment.setDateCreation(
					DateUtils.getDateFromXmlGregorianCalendar(dcrmNote.getDateCreation().getValue()));
		}
		// adaptation de la note associee a la piece jointe
		com.orangecaraibe.enabler.crm.business.bean.Note businessNote = new com.orangecaraibe.enabler.crm.business.bean.Note();
		if ((dcrmNote.getSujet() != null && StringUtils.isNotEmpty(
				dcrmNote.getSujet().getValue())) || (dcrmNote.getTexte() != null && StringUtils.isNotEmpty(
				dcrmNote.getTexte().getValue())) || (dcrmNote.getUtilisateur() != null && dcrmNote.getUtilisateur()
				.getValue() != null)) {
			businessNote = adaptDcrmNote(dcrmNote);
		} else {
			// On adapte uniquement l'utilisateur de la pièce jointe dans le modèle SOA car pas de contenu de la note à
			// adapter
			// Néanmoins, on doit toujours adapter le propriétaire de la pièce jointe et dans le modèle SOA, il ne peut
			// être
			// transporter uniquement dans une note associé
			adaptDcrmUtilisateurToUserFromNote(dcrmNote, businessNote);
		}
		attachment.setNote(businessNote);
		return attachment;
	}

	/**
	 * Adapte une liste de piece jointe DCRM en atttachements business
	 */
	private List<Attachment> adaptPiecesJointesToAttachments(List<PieceJointe> piecesJointes) {

		List<Attachment> businessAttachments = new ArrayList<Attachment>();
		for (PieceJointe pieceJointe : piecesJointes) {
			Attachment businessAttachment = adaptPieceJointeToAttachment(pieceJointe);
			businessAttachments.add(businessAttachment);
		}
		return businessAttachments;
	}

	/**
	 * Adapte une piece jointe DCRM de type Document en Attachement business
	 */
	private Attachment adaptPieceJointeToAttachment(PieceJointe piecesJointe) {

		Attachment attachment = new Attachment();
		attachment.setNom(piecesJointe.getNom().getValue());
		attachment.setType(piecesJointe.getType().getValue());
		attachment.setDocument(piecesJointe.getDocument().getValue());
		return attachment;
	}

	public List<CustomerProblemReason> adaptCustomerProblemFindReason(FindAndGetThemeResponse response) {

		List<CustomerProblemReason> customerProblemReasons = new ArrayList<CustomerProblemReason>();
		ArrayOfThemeNiveau1 themes = response.getThemes().getValue();
		for (ThemeNiveau1 theme1 : themes.getThemeNiveau1()) {
			CustomerProblemReason customerProblemReason = new CustomerProblemReason();
			customerProblemReason.setGuid(theme1.getGUIDThemeNiveau1());
			customerProblemReason.setDescription(theme1.getLibelle().getValue());
			if (theme1.getThemesNiveau2() != null) {
				ArrayOfThemeNiveau2 arrayThemeNiveau2 = theme1.getThemesNiveau2().getValue();
				for (ThemeNiveau2 theme2 : arrayThemeNiveau2.getThemeNiveau2()) {
					CustomerProblemReason customerProblemReason2 = new CustomerProblemReason();
					customerProblemReason2.setGuid(theme2.getGUIDThemeNiveau2());
					customerProblemReason2.setDescription(theme2.getLibelle().getValue());
					if (theme2.getThemesNiveau3() != null) {
						ArrayOfThemeNiveau3 arrayThemeNiveau3 = theme2.getThemesNiveau3().getValue();
						for (ThemeNiveau3 theme3 : arrayThemeNiveau3.getThemeNiveau3()) {
							CustomerProblemReason customerProblemReason3 = new CustomerProblemReason();
							customerProblemReason3.setGuid(theme3.getGUIDThemeNiveau3());
							customerProblemReason3.setDescription(theme3.getLibelle().getValue());
							customerProblemReason3.setCategorie(EnumThemeType.getByDCRMId(theme3.getType()));
							customerProblemReason2.getCustomerProblemReason().add(customerProblemReason3);
						}
						customerProblemReason.getCustomerProblemReason().add(customerProblemReason2);
					}
				}
			}
			customerProblemReasons.add(customerProblemReason);
		}
		return customerProblemReasons;
	}

	private Actor adaptDcrmActivityPartyToBusinessActor(ActivityParty activityParty) {
		// bean metier actor
		Actor businessActor = null;
		if (activityParty.getPartyOuContact().getValue() != null && activityParty.getPartyOuContact().getValue()
				.getLogicalName().equals(Constantes.DCRM_LOGICAL_NAME_SYSTEM_USER)) {
			// le party est un conseiller/corbeille, on instancie un CustomerService
			businessActor = new CustomerService();
		} else {
			// TODO : moyen de specialiser le party enn Person/Organisation ?
			businessActor = new Party();
		}
		ContactMethods contactMethods = new ContactMethods();
		businessActor.setContactMethods(contactMethods);
		// adresse utilisee
		if (activityParty.getAdresseUtilisee() != null && StringUtils.isNotEmpty(
				activityParty.getAdresseUtilisee().getValue())) {
			// old : businessParty.setAddress( activityParty.getAdresseUtilisee().getValue() );
			contactMethods.setEmail(activityParty.getAdresseUtilisee().getValue());
		}
		// party ou contact
		if (activityParty.getPartyOuContact().getValue() != null && StringUtils.isNotEmpty(
				activityParty.getPartyOuContact().getValue().getId())) {
			String partyID = activityParty.getPartyOuContact().getValue().getId();
			businessActor.setGuid(partyID);
		}
		if (activityParty.getPartyOuContact().getValue() != null && StringUtils.isNotEmpty(
				activityParty.getPartyOuContact().getValue().getName().getValue())) {
			String name = activityParty.getPartyOuContact().getValue().getName().getValue();
			businessActor.setName(name);
		}
		return businessActor;
	}

	private ArrayOfActivityParty adaptBusinessPartyListToArrayOfActivityParty(List<Actor> businessActors) {

		ArrayOfActivityParty arrayOfActivityParty = new ArrayOfActivityParty();
		for (Actor businessActor : businessActors) {
			if (businessActor != null && businessActor.getGuid() != null) {
				ActivityParty activityParty = adaptBusinessPartyToActivityParty(businessActor);
				arrayOfActivityParty.getActivityParty().add(activityParty);
			}
		}
		return arrayOfActivityParty;
	}

	private String adapteBusinessPartyContactMethode(ContactMethods contactMethods) {

		StringBuffer addressBuffer = null;
		if (contactMethods.getAdress() != null) {
			addressBuffer = new StringBuffer();
			Address address = contactMethods.getAdress();
			if (address.getDetails1() != null) {
				addressBuffer.append(address.getDetails1());
			}
			if (address.getDetails2() != null) {
				addressBuffer.append(" ");
				addressBuffer.append(address.getDetails2());
			}
			if (address.getStreetNum() != null) {
				addressBuffer.append(" ");
				addressBuffer.append(address.getStreetNum());
			}
			if (address.getStreetName() != null) {
				addressBuffer.append(" ");
				addressBuffer.append(address.getStreetName());
			}
			if (address.getDetails3() != null) {
				addressBuffer.append(" ");
				addressBuffer.append(address.getDetails3());
			}
			addressBuffer.append(" ");
			addressBuffer.append(address.getZip() + " " + address.getCity());
		}
		return addressBuffer != null ? addressBuffer.toString() : null;
	}

	private ActivityParty adaptBusinessPartyToActivityParty(Actor businessActor) {

		ActivityParty activityParty = new ActivityParty();
		EntityReference activityPartyEntityRef = new EntityReference();
		if (businessActor instanceof Party) {
			activityPartyEntityRef.setLogicalName(Constantes.DCRM_LOGICAL_NAME_ACCOUNT);
		} else if (businessActor instanceof CustomerService) {
			activityPartyEntityRef.setLogicalName(Constantes.DCRM_LOGICAL_NAME_SYSTEM_USER);
		}
		activityPartyEntityRef.setId(businessActor.getGuid());
		activityParty.setPartyOuContact(objectFactory.createActivityPartyPartyOuContact(activityPartyEntityRef));
		return activityParty;
	}

	/**
	 * Consulte les Map de statusReason chargees depuis les properties afin de determiner la raison du statut DCRM
	 */
	private Integer getDefaultInteractionStatusReason(Class<? extends Interaction> interactionClass,
			DefaultInteractionStatusReasonBean statusReasonBean) {

		Integer dcrmStatusReason = null;
		if (interactionReasonStatusPropertiesHelper.getDefaultStatusReasonMap().containsKey(statusReasonBean)) {
			// on recupere d'abord le libelle business de la raison du statut
			String businessStatusReasonLabel = interactionReasonStatusPropertiesHelper.getDefaultStatusReasonMap()
					.get(statusReasonBean);
			// ensuite on recupere la valeur dcrm (statusCode) associe a ce libelle
			if (interactionReasonStatusPropertiesHelper.getStatusReasonMap().containsKey(interactionClass)) {
				// map des reason status par defaut configures pour ce type d'interaction
				Map<Integer, String> statusReasonMapForThisType = interactionReasonStatusPropertiesHelper.getStatusReasonMap()
						.get(interactionClass);
				if (MapUtils.invertMap(statusReasonMapForThisType).containsKey(businessStatusReasonLabel)) {
					dcrmStatusReason = MapUtils.invertMap(statusReasonMapForThisType).get(businessStatusReasonLabel);
				}
			}
		}
		return dcrmStatusReason;
	}

	private CustomerService adaptDcrmInteractionOwner(Proprietaire proprietaire) {

		CustomerService customerService = null;
		// cas ou le proprietaire est un utilisateur (conseiller)
		if (proprietaire instanceof Utilisateur) {
			Utilisateur utilisateur = (Utilisateur) proprietaire;
			customerService = new CustomerServicePerson();
			if (utilisateur.getLogin() != null && StringUtils.isNotEmpty(utilisateur.getLogin().getValue())) {
				((CustomerServicePerson) customerService).setLogin(utilisateur.getLogin().getValue());
			}
		}
		// cas ou le proprietaire est une equipe
		else if (proprietaire instanceof Equipe) {
			customerService = new CustomerServiceOrganisation();
		} else {
			customerService = new CustomerService();
		}
		customerService.setGuid(proprietaire.getUtilisateurOuEquipe().getValue().getId());
		if (proprietaire.getUtilisateurOuEquipe().getValue() != null && proprietaire.getUtilisateurOuEquipe().getValue()
				.getName() != null && StringUtils.isNotEmpty(
				proprietaire.getUtilisateurOuEquipe().getValue().getName().getValue())) {
			customerService.setName(proprietaire.getUtilisateurOuEquipe().getValue().getName().getValue());
		}
		return customerService;
	}

	/**
	 * @param objectFactory
	 * 		the objectFactory to set
	 */
	public void setObjectFactory(ObjectFactory objectFactory) {

		this.objectFactory = objectFactory;
	}

	/**
	 * @param interactionReasonStatusPropertiesHelper
	 * 		the interactionReasonStatusPropertiesHelper to set
	 */
	public void setInteractionReasonStatusPropertiesHelper(
			InteractionReasonStatusPropertiesHelper interactionReasonStatusPropertiesHelper) {

		this.interactionReasonStatusPropertiesHelper = interactionReasonStatusPropertiesHelper;
	}

	/**
	 * @param demandeCreateHelper
	 * 		the demandeCreateHelper to set
	 */
	public void setThemeHelper(ThemeHelper themeHelper) {

		this.themeHelper = themeHelper;
	}

	public void setDefaultSystemUserGuid(String defaultSystemUserGuid) {

		this.defaultSystemUserGuid = defaultSystemUserGuid;
	}

	public List<Holder> adapteFindAndGetParty(FindAndGetPartyResponse response) {

		List<Holder> customerPersonalInfos = new ArrayList<Holder>();
		ArrayOfParty arrayParty = response.getParties().getValue();
		if (arrayParty != null) {
			for (com.orange.sidom.soa.dcrm.datacontract.Party party : arrayParty.getParty()) {
				Holder customer = new Holder();
				if (party instanceof Individu) {
					Individu indiv = (Individu) party;
					customer.setGuid(party.getGUIDParty().getValue());
					if (party.getNumeroParty() != null) {
						customer.setFunctionalId(party.getNumeroParty().getValue());
					}
					if (indiv.getLibelle() != null && indiv.getLibelle().getValue() != null) {
						customer.setName(indiv.getLibelle().getValue());
					}
					// nom du party
					if (indiv.getCivilite() != null && indiv.getCivilite().getValue() != null) {
						customer.setTitle(EnumTitle.getValueFromDCRM(indiv.getCivilite().getValue()));
					}
					if (indiv.getNom() != null && indiv.getNom().getValue() != null) {
						customer.setLastName(indiv.getNom().getValue());
					}
					if (indiv.getPrenom() != null && indiv.getPrenom().getValue() != null) {
						customer.setFirstName(indiv.getPrenom().getValue());
					}
					if (party.getMoyensDeContact() != null) {
						customer.setContactMethods(adapterContactMethode(party.getMoyensDeContact().getValue()));
					}
				} else if (party instanceof Organisation) {
					Organisation indiv = (Organisation) party;
					customer.setGuid(party.getGUIDParty().getValue());
					if (party.getNumeroParty() != null) {
						customer.setFunctionalId(party.getNumeroParty().getValue());
					}
					if (indiv.getLibelle() != null && indiv.getLibelle().getValue() != null) {
						customer.setName(indiv.getLibelle().getValue());
					}
					if (party.getMoyensDeContact() != null) {
						customer.setContactMethods(adapterContactMethode(party.getMoyensDeContact().getValue()));
					}
				}
				customerPersonalInfos.add(customer);
			}
		}
		return customerPersonalInfos;
	}

	private ContactMethods adapterContactMethode(ArrayOfMoyenDeContact arrayMoyenContact) {

		ContactMethods paddress = new ContactMethods();
		for (MoyenDeContact moyenContact : arrayMoyenContact.getMoyenDeContact()) {
			if (moyenContact instanceof Telephone) {
				Telephone tel = (Telephone) moyenContact;
				if (tel.getType() == TypeTelephone.TELEPHONE_1) {
					paddress.setFixePhone(tel.getNumero().getValue());
				} else if (tel.getType() == TypeTelephone.MOBILE) {
					paddress.setMobilePhone(tel.getNumero().getValue());
				} else if (tel.getType() == TypeTelephone.TELEPHONE_2) {
					paddress.setFixePhone2(tel.getNumero().getValue());
				}
			}
			if (moyenContact instanceof AdresseEmail) {
				AdresseEmail adresseEmail = (AdresseEmail) moyenContact;
				paddress.setEmail(adresseEmail.getAdresse().getValue());
			}
		}
		return paddress;
	}

	public List<com.orangecaraibe.enabler.crm.business.bean.Campagne> adapteCamapagne(ArrayOfCampagne campagnes) {

		List<com.orangecaraibe.enabler.crm.business.bean.Campagne> interactions = new ArrayList<com.orangecaraibe.enabler.crm.business.bean.Campagne>();
		for (Campagne campagne : campagnes.getCampagne()) {
			com.orangecaraibe.enabler.crm.business.bean.Campagne businessCampagne = new com.orangecaraibe.enabler.crm.business.bean.Campagne();
			if (campagne.getNom() != null && campagne.getNom().getValue() != null) {
				businessCampagne.setName(campagne.getNom().getValue());
			}
			if (campagne.getDescription() != null && campagne.getDescription().getValue() != null) {
				businessCampagne.setDescription(campagne.getDescription().getValue());
			}
			if (campagne.getCodeCastor() != null && campagne.getCodeCastor().getValue() != null) {
				businessCampagne.setOfferCode(campagne.getNom().getValue());
			}
			if (campagne.getType() != null) {
				businessCampagne.setType(EnumTypeCampagne.getFromDcrmValue(campagne.getType().value()));
			}
			if (campagne.getDateDebut() != null) {
				businessCampagne.setStartDate(DateUtils.getDateFromXmlGregorianCalendar(campagne.getDateDebut()));
			}
			if (campagne.getDateFin() != null) {
				businessCampagne.setEndDate(DateUtils.getDateFromXmlGregorianCalendar(campagne.getDateFin()));
			}
			if (campagne.getDateLancement() != null) {
				businessCampagne.setCampangeDate(
						DateUtils.getDateFromXmlGregorianCalendar(campagne.getDateLancement()));
			}
			if (campagne.getInteractions() != null) {
				ArrayOfInteractionSMS smsInteractions = campagne.getInteractions().getValue();
				for (InteractionSMS sms : smsInteractions.getInteractionSMS()) {
					com.orangecaraibe.enabler.crm.business.bean.Interaction interaction = new com.orangecaraibe.enabler.crm.business.bean.Interaction();
					interaction.setDescription(sms.getDescription().getValue());
					interaction.setTitle(sms.getTitre().getValue());
					interaction.setId(sms.getGUIDInteraction().getValue());
					interaction.setWay(interactionWayMap.get(sms.getDirection().getValue().booleanValue()));
					interaction.setCanal(EnumInteractionChannel.SMS);
					businessCampagne.addinteraction(interaction);
				}
			}
			interactions.add(businessCampagne);
		}
		return interactions;
	}
}
