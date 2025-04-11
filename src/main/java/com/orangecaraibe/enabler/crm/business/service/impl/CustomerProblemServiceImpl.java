package com.orangecaraibe.enabler.crm.business.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orange.sidom.soa.dcrm.datacontract.DemandeCriteriaType;
import com.orangecaraibe.enabler.crm.business.adapter.WbsDunningAdapter;
import com.orangecaraibe.enabler.crm.business.bean.CustomerProblemReason;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.bean.Theme;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumCustomerProblemStatus;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemCreateException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindAndGetException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindReasonException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;
import com.orangecaraibe.enabler.crm.business.service.CustomerProblemService;
import com.orangecaraibe.enabler.externalws.services.ExternalWSService;

import fcm.gp.oc.schemas.dunning.data.dunningregistercard.RegisterCardActionData;
import fcm.gp.oc.schemas.dunning.data.dunningworkitem.CreateWorkLoadData;

@Service( "customerProblemService" )
public class CustomerProblemServiceImpl
	implements CustomerProblemService
{

	/**
	 * Service vers les WS externes
	 */
	@Autowired
	@Qualifier( "externalWSService" )
	private ExternalWSService externalWSService;

	@Autowired
	private WbsDunningAdapter wbsDunningAdapter;

	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( CustomerProblemServiceImpl.class );

	private static final Integer CREATION_IMPOSSIBLE = 2;

	private static final Integer ERREUR_CUSTOMER_ID = 1;

	@Override
	public Request updateNotifyDunning( Request customerProblem )
		throws CustomerProblemUpdateException
	{

		if ( customerProblem.getOption() != null )
		{
			return notifyDunningRegisterCard( customerProblem );
		}
		if ( customerProblem.getLegalStatus() != null )
		{

			return notifyDunningWorkLoad( customerProblem );
		}
		return customerProblem;
	}

	private Request notifyDunningRegisterCard( Request customerProblem )
		throws CustomerProblemUpdateException
	{
		RegisterCardActionData registerCardActionData = wbsDunningAdapter.adaptRegisterCard( customerProblem );
		registerCardActionData = externalWSService.updateDunningRegisterCard( registerCardActionData );

		if ( registerCardActionData.getCodeRetour() == CREATION_IMPOSSIBLE )
		{
			LOGGER.error( "Impossible de mettre a jour le statut d'homologation dans prompt" );
			CustomerProblemUpdateException exception =
				new CustomerProblemUpdateException( "error occurred while updating homologation status in the dunning system",
													"CUSP012" );
			throw exception;
		}
		else if ( registerCardActionData.getCodeRetour() == ERREUR_CUSTOMER_ID )
		{
			LOGGER.error( "customer_id is invalid : " + customerProblem.getAccount().getAccountId() + " est inconnu" );
			CustomerProblemUpdateException exception = new CustomerProblemUpdateException( "Customer Id is invalid : "
				+ customerProblem.getAccount().getAccountId(), "CUSP013" );
			throw exception;
		}
		return null;
	}

	private Request notifyDunningWorkLoad( Request customerProblem )
		throws CustomerProblemUpdateException
	{
		CreateWorkLoadData createWorkLoadData = wbsDunningAdapter.adaptWorkLoad( customerProblem );
		createWorkLoadData = externalWSService.createDunningWorkLoad( createWorkLoadData );

		if ( createWorkLoadData.getCodeRetour() == CREATION_IMPOSSIBLE )
		{
			LOGGER.error( "Impossible de créer la charge de travail dans prompt " );
			CustomerProblemUpdateException exception =
				new CustomerProblemUpdateException( "failed to create dunning workload", "CUSP012" );
			throw exception;
		}
		else if ( createWorkLoadData.getCodeRetour() == ERREUR_CUSTOMER_ID )
		{
			LOGGER.error( "customer_id is invalid : " + customerProblem.getAccount().getAccountId() + " est inconnue" );
			CustomerProblemUpdateException exception = new CustomerProblemUpdateException( "Customer Id is invalid : "
				+ customerProblem.getAccount().getAccountId(), "CUSP013" );
			throw exception;
		}

		return customerProblem;

	}

	@Override
	public Request createCustomerProblem( Request request )
		throws CustomerProblemCreateException, CustomerProblemFindReasonException
	{

		return externalWSService.createDemande( request );
	}

	@Override
	public List<Request> findAndGetCustomerBillingAccountId(	String billingAccountID, Date fromDate, Date toDate,
																List<EnumCustomerProblemStatus> status,
																boolean includeNote, boolean includeAttachment,
																int maxResults, boolean excludeTheme,
																List<Theme> themes )
		throws CustomerProblemFindAndGetException
	{
		List<Request> request =
			externalWSService.findAndGet(	DemandeCriteriaType.CUSTOMER_ID, billingAccountID, fromDate, toDate, status,
											includeNote, includeAttachment, maxResults, excludeTheme, themes );

		return request;
	}

	@Override
	public List<Request> findAndGetCustomerProblemByCustomerProblemId(	String customerProblemID, Date fromDate,
																		Date toDate,
																		List<EnumCustomerProblemStatus> status,
																		boolean includeNote, boolean includeAttachment,
																		int maxResults, boolean excludeTheme,
																		List<Theme> themes, boolean isUseCaseEscalade )
		throws CustomerProblemFindAndGetException
	{
		List<Request> request =
			externalWSService.findAndGet(	DemandeCriteriaType.GUID_DEMANDE, customerProblemID, fromDate, toDate, status,
											includeNote, includeAttachment, maxResults, excludeTheme, themes,
											isUseCaseEscalade );

		return request;

	}

	@Override
	public List<Request> findAndGetCustomerProblemByCustomerProblemId(	String customerProblemID, Date fromDate,
																		Date toDate,
																		List<EnumCustomerProblemStatus> status,
																		boolean includeNote, boolean includeAttachment,
																		int maxResults, boolean excludeTheme,
																		List<Theme> themes )
		throws CustomerProblemFindAndGetException
	{
		List<Request> request =
			externalWSService.findAndGet(	DemandeCriteriaType.GUID_DEMANDE, customerProblemID, fromDate, toDate, status,
											includeNote, includeAttachment, maxResults, excludeTheme, themes );

		return request;
	}

	@Override
	public List<Request> findAndGetCustomerProblemByInteractionId(	String interactionId, Date fromDate, Date toDate,
																	List<EnumCustomerProblemStatus> status,
																	boolean includeNote, boolean includeAttachment,
																	int maxResults, boolean excludeTheme,
																	List<Theme> themes )
		throws CustomerProblemFindAndGetException
	{
		List<Request> request =
			externalWSService.findAndGet(	DemandeCriteriaType.GUID_INTERACTION, interactionId, fromDate, toDate, status,
											includeNote, includeAttachment, maxResults, excludeTheme, themes );

		return request;

	}

	@Override
	public List<Request> findAndGetCustomerProblemByInstalledContractID(	String installedContractID, Date fromDate,
																			Date toDate,
																			List<EnumCustomerProblemStatus> status,
																			boolean includeNote,
																			boolean includeAttachment, int maxResults,
																			boolean excludeTheme, List<Theme> themes )
		throws CustomerProblemFindAndGetException
	{
		List<Request> request =
			externalWSService.findAndGet(	DemandeCriteriaType.CO_ID, installedContractID, fromDate, toDate, status,
											includeNote, includeAttachment, maxResults, excludeTheme, themes );

		return request;

	}

	@Override
	public List<Request> findAndGetCustomerProblembyInstalledPublicKey( String installedPublicKeyID, Date fromDate,
																		Date toDate,
																		List<EnumCustomerProblemStatus> status,
																		boolean includeNote, boolean includeAttachment,
																		int maxResults, boolean excludeTheme,
																		List<Theme> themes )
		throws CustomerProblemFindAndGetException
	{
		List<Request> request =
			externalWSService.findAndGet(	DemandeCriteriaType.MSISDN, installedPublicKeyID, fromDate, toDate, status,
											includeNote, includeAttachment, maxResults, excludeTheme, themes );

		return request;

	}

	@Override
	public List<Request> findAndGetCustomerProblemPartyId(	String partyID, Date fromDate, Date toDate,
															List<EnumCustomerProblemStatus> status, boolean includeNote,
															boolean includeAttachment, int maxResults,
															boolean excludeTheme, List<Theme> themes )
		throws CustomerProblemFindAndGetException
	{
		List<Request> request =
			externalWSService.findAndGet(	DemandeCriteriaType.GUID_PARTY, partyID, fromDate, toDate, status,
											includeNote, includeAttachment, maxResults, excludeTheme, themes );

		return request;

	}

	@Override
	public Request update( Request request )
		throws CustomerProblemUpdateException
	{

		return externalWSService.updateCustomerProblem( request );
	}

	// @Override
	// public Request addNote( Request request )
	// throws CustomerProblemUpdateException
	// {
	// return externalWSService.updateCustomerProblem( request );
	// }

	@Override
	public List<CustomerProblemReason> findAndGetReasonSpec( String code, String canalEmetteur, long niveau )
		throws CustomerProblemFindReasonException
	{

		return externalWSService.findCustomerProblemReason( code, canalEmetteur, niveau );
	}

}
