package com.orangecaraibe.enabler.externalws.services;

import java.util.Date;
import java.util.List;

import com.orange.sidom.soa.dcrm.datacontract.DemandeCriteriaType;
import com.orange.sidom.soa.dcrm.datacontract.InteractionCriteriaType;
import com.orangecaraibe.enabler.crm.business.bean.Campagne;
import com.orangecaraibe.enabler.crm.business.bean.CustomerProblemReason;
import com.orangecaraibe.enabler.crm.business.bean.Interaction;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.bean.Theme;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumCustomerProblemStatus;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemCreateException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindAndGetException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindReasonException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;
import com.orangecaraibe.enabler.crm.business.exception.InteractionCreateException;
import com.orangecaraibe.enabler.crm.business.exception.InteractionFindAndGetException;
import com.orangecaraibe.enabler.crm.webservice.criteria.InteractionCriteria;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.CreateUpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.CreateCustomerOrderException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.customerdomain.managecustomerorder.CustomerOrder;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOffer;

import fcm.gp.oc.schemas.dunning.data.dunningregistercard.RegisterCardActionData;
import fcm.gp.oc.schemas.dunning.data.dunningworkitem.CreateWorkLoadData;

public interface ExternalWSService
{

	/**
	 * Méthode de création d'une commande de suppression de service
	 * 
	 * @param coId
	 * @param msisdn
	 * @param tmCode
	 * @param snCode
	 * @param spCode
	 * @param customerId
	 * @return the customer order
	 * @throws CreateCustomerOrderException
	 * @throws InterfaceUnavailableException
	 */
	public CustomerOrder createOrderRemoveService( String coId, String msisdn, String tmCode, String snCode,
													String spCode, String customerId )
		throws CreateCustomerOrderException, InterfaceUnavailableException;

	/**
	 * Méthode de création d'une commande d'ajout de service catalogue
	 * 
	 * @param coId
	 * @param msisdn
	 * @param tmCode
	 * @param offerSpecificationCode
	 * @param customerId
	 * @return the customer order
	 * @throws CreateCustomerOrderException
	 * @throws InterfaceUnavailableException
	 */
	public CustomerOrder createOrderAddCatalogueService( String coId, String msisdn, String tmCode,
															String offerSpecificationCode, String customerId )
		throws InterfaceUnavailableException, CreateCustomerOrderException;

	/**
	 * Méthode de création d'une commande d'acaht à l'act
	 * 
	 * @param coId
	 * @param msisdn
	 * @param tmCode
	 * @param offerSpecificationCode
	 * @param customerId
	 * @return the customer order
	 * @throws InterfaceUnavailableException
	 * @throws CreateCustomerOrderException
	 */
	public CustomerOrder creditIN( String coId, String msisdn, String tmCode, String offerSpecificationCode,
									String customerId )
		throws InterfaceUnavailableException, CreateCustomerOrderException;

	/**
	 * Méthode de récupération du crédit de communication d'un contrat
	 * 
	 * @param msisdn
	 * @return
	 * @throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException
	 * @throws FindAndGetException
	 */
	public Double getContractCredit( String msisdn )
		throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		FindAndGetException;

	public void setCreditIN( String msisdn, Double amount, int daysActive, int daysInactive )
		throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		CreateUpdateException;

	/**
	 * Méthode de récupération des installed offers
	 * 
	 * @param criteria
	 * @return
	 * @throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException
	 * @throws FindAndGetException
	 */
	public List<InstalledOffer> findAndGetSuperMCIB( Criteria criteria )
		throws com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException,
		FindAndGetException;

	/**
	 * Methode de creation d'une charge de travail dans le Dunning (Prompt)
	 */
	public CreateWorkLoadData createDunningWorkLoad( CreateWorkLoadData createWorkLoadData )
		throws CustomerProblemUpdateException;

	/**
	 * Methode de creation d'une demande dans DCRM
	 * 
	 * @param demande
	 */
	public Request createDemande( Request demande )
		throws CustomerProblemCreateException, CustomerProblemFindReasonException;

	/**
	 * Methode de creation d'une interaction dans DCRM
	 * 
	 * @param interaction
	 * @return
	 * @throws InteractionCreateException
	 */
	public Interaction createInteraction( Interaction interaction )
		throws InteractionCreateException;

	/**
	 * Methode de recherche d'interaction dans DCRM
	 * 
	 * @param dcrmCriteriaValue
	 * @param dcrmCriteria
	 * @param InteractionCriteria
	 * @return
	 * @throws InteractionFindAndGetException
	 */
	public List<com.orangecaraibe.enabler.crm.business.bean.Interaction> findAndGetInteraction( InteractionCriteriaType dcrmCriteria,
																								String dcrmCriteriaValue,
																								InteractionCriteria InteractionCriteria )
		throws InteractionFindAndGetException;

	/**
	 * @param code
	 * @param canalEmetteur
	 * @param niveau
	 * @return
	 */
	public List<CustomerProblemReason> findCustomerProblemReason( String code, String canalEmetteur, long niveau )
		throws CustomerProblemFindReasonException;

	/**
	 * Methode de notification des etapes d'homologation
	 * 
	 * @param registerCardActionData
	 * @return
	 */
	public RegisterCardActionData updateDunningRegisterCard( RegisterCardActionData registerCardActionData )
		throws CustomerProblemUpdateException;

	/**
	 * Methode de recherche de demande
	 * 
	 * @param customerId
	 * @param billingAccountID
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param includeNote
	 * @param includeAttachment
	 * @param maxResults
	 * @param excludeTheme
	 * @param themes
	 * @return
	 * @throws CustomerProblemFindAndGetException
	 */
	public List<Request> findAndGet( DemandeCriteriaType customerId, String billingAccountID, Date fromDate,
										Date toDate, List<EnumCustomerProblemStatus> status, boolean includeNote,
										boolean includeAttachment, int maxResults, boolean excludeTheme,
										List<Theme> themes )
		throws CustomerProblemFindAndGetException;

	/**
	 * Methode de recherche de demande
	 * 
	 * @param customerId
	 * @param billingAccountID
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param includeNote
	 * @param includeAttachment
	 * @param maxResults
	 * @param excludeTheme
	 * @param themes
	 * @param isUseCaseEscalade
	 * @return
	 * @throws CustomerProblemFindAndGetException
	 */
	public List<Request> findAndGet( DemandeCriteriaType customerId, String billingAccountID, Date fromDate,
										Date toDate, List<EnumCustomerProblemStatus> status, boolean includeNote,
										boolean includeAttachment, int maxResults, boolean excludeTheme,
										List<Theme> themes, boolean isUseCaseEscalade )
		throws CustomerProblemFindAndGetException;

	/**
	 * Methode pour la mise à jour d'un request
	 * 
	 * @param request
	 * @return
	 */
	public Request updateCustomerProblem( Request request )
		throws CustomerProblemUpdateException;

	public Request createTroubleTicketOceane( Request request, String cuid )
		throws CustomerProblemUpdateException;

	public Request updateTroubleTicketOceane( Request request, String cuid )
		throws CustomerProblemUpdateException;

	public List<Campagne> findAndGetCampagne( String partyGuid, String contractId, Date startDate, Date endDate );
}
