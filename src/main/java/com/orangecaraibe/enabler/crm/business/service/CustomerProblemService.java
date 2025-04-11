package com.orangecaraibe.enabler.crm.business.service;

import java.util.Date;
import java.util.List;

import com.orange.sidom.soa.dcrm.servicecontract.demande._1_0.ManageDemandeCreateServiceFaultFaultFaultMessage;
import com.orangecaraibe.enabler.crm.business.bean.CustomerProblemReason;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.bean.Theme;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumCustomerProblemStatus;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemCreateException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindAndGetException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindReasonException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;

// TODO LDE : s'il s'agit d'un service Business, le renommer en RequestService ? Meme question pour les exceptions 
public interface CustomerProblemService
{

	/**
	 * Methode pour la notification du dunning au travers d'un update
	 * 
	 * @param request
	 * @return
	 * @throws CustomerProblemUpdateException
	 */
	public Request updateNotifyDunning( Request request )
		throws CustomerProblemUpdateException;

	/**
	 * createCustomerProblem methode pour la creation d'une demande
	 * 
	 * @param request la demande
	 * @return
	 * @throws ManageDemandeCreateServiceFaultFaultFaultMessage
	 */
	public Request createCustomerProblem( Request request )
		throws CustomerProblemCreateException, CustomerProblemFindReasonException;

	/**
	 * Methode pour la recherche des demandes par billing_account_id
	 * 
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
	 */
	public List<Request> findAndGetCustomerBillingAccountId( String billingAccountID, Date fromDate, Date toDate,
																List<EnumCustomerProblemStatus> status,
																boolean includeNote, boolean includeAttachment,
																int maxResults, boolean excludeTheme, List<Theme> themes )
		throws CustomerProblemFindAndGetException;

	/**
	 * Methode pour la recherche des demandes par demande_id
	 * 
	 * @param customerProblemID
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param includeNote
	 * @param includeAttachment
	 * @param maxResults
	 * @param excludeTheme
	 * @param themes
	 * @return
	 */
	public List<Request> findAndGetCustomerProblemByCustomerProblemId( String customerProblemID, Date fromDate,
																		Date toDate,
																		List<EnumCustomerProblemStatus> status,
																		boolean includeNote, boolean includeAttachment,
																		int maxResults, boolean excludeTheme,
																		List<Theme> themes )
		throws CustomerProblemFindAndGetException;

	/**
	 * Methode pour la recherche des demandes par demande_id
	 * 
	 * @param customerProblemID
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
	 */
	public List<Request> findAndGetCustomerProblemByCustomerProblemId( String customerProblemID, Date fromDate,
																		Date toDate,
																		List<EnumCustomerProblemStatus> status,
																		boolean includeNote, boolean includeAttachment,
																		int maxResults, boolean excludeTheme,
																		List<Theme> themes, boolean isUseCaseEscalade )
		throws CustomerProblemFindAndGetException;

	/**
	 * Methode pour la recherche des demandes par interaction_id
	 * 
	 * @param interactionId
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param includeNote
	 * @param includeAttachment
	 * @param maxResults
	 * @param excludeTheme
	 * @param themes
	 * @return
	 */
	public List<Request> findAndGetCustomerProblemByInteractionId( String interactionId, Date fromDate, Date toDate,
																	List<EnumCustomerProblemStatus> status,
																	boolean includeNote, boolean includeAttachment,
																	int maxResults, boolean excludeTheme,
																	List<Theme> themes )
		throws CustomerProblemFindAndGetException;

	/**
	 * Methode pour la recherche des demandes par InstalledContractId
	 * 
	 * @param installedContractID
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param includeNote
	 * @param includeAttachment
	 * @param maxResults
	 * @param excludeTheme
	 * @param themes
	 * @return
	 */
	public List<Request> findAndGetCustomerProblemByInstalledContractID( String installedContractID, Date fromDate,
																			Date toDate,
																			List<EnumCustomerProblemStatus> status,
																			boolean includeNote,
																			boolean includeAttachment, int maxResults,
																			boolean excludeTheme, List<Theme> themes )
		throws CustomerProblemFindAndGetException;

	/**
	 * Methode pour la recherche des demandes par InstalledPublicKey
	 * 
	 * @param installedPublicKeyID
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param includeNote
	 * @param includeAttachment
	 * @param maxResults
	 * @param excludeTheme
	 * @param themes
	 * @return
	 */
	public List<Request> findAndGetCustomerProblembyInstalledPublicKey( String installedPublicKeyID, Date fromDate,
																		Date toDate,
																		List<EnumCustomerProblemStatus> status,
																		boolean includeNote, boolean includeAttachment,
																		int maxResults, boolean excludeTheme,
																		List<Theme> themes )
		throws CustomerProblemFindAndGetException;

	/**
	 * Methode pour la recherche des demandes par PartyId
	 * 
	 * @param partyID
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param includeNote
	 * @param includeAttachment
	 * @param maxResults
	 * @param excludeTheme
	 * @param themes
	 * @return
	 */
	public List<Request> findAndGetCustomerProblemPartyId( String partyID, Date fromDate, Date toDate,
															List<EnumCustomerProblemStatus> status,
															boolean includeNote, boolean includeAttachment,
															int maxResults, boolean excludeTheme, List<Theme> themes )
		throws CustomerProblemFindAndGetException;

	/**
	 * Methode d'update d'une demande
	 * 
	 * @param request
	 * @return
	 */
	public Request update( Request request )
		throws CustomerProblemUpdateException;

	// /**
	// * Methode pour l'ajout des notes sur une demande
	// *
	// * @param request
	// * @return
	// */
	// public Request addNote( Request request )
	// throws CustomerProblemUpdateException;

	/**
	 * Methode pour la remontee des themes
	 * 
	 * @param code
	 * @param canalEmetteur
	 * @param niveau
	 * @return
	 * @throws CustomerProblemUpdateException
	 */
	public List<CustomerProblemReason> findAndGetReasonSpec( String code, String canalEmetteur, long niveau )
		throws CustomerProblemFindReasonException;

}
