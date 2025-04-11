package com.orangecaraibe.enabler.crm.webservice.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.orangecaraibe.enabler.Enabler;
import com.orangecaraibe.enabler.crm.business.adapter.CustomerProblemAdapter;
import com.orangecaraibe.enabler.crm.business.bean.CustomerProblemReason;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.bean.Theme;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumCustomerProblemStatus;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemCreateException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindAndGetException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindReasonException;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;
import com.orangecaraibe.enabler.crm.business.service.CustomerProblemService;
import com.orangecaraibe.enabler.crm.business.service.TroubleTicketService;
import com.orangecaraibe.enabler.crm.predicate.UseCasePredicate;
import com.orangecaraibe.enabler.crm.webservice.criteria.CriteriaConstants;
import com.orangecaraibe.enabler.crm.webservice.criteria.CustomerProblemManager;
import com.orangecaraibe.enabler.crm.webservice.criteria.WebservicesConstantes;
import com.orangecaraibe.enabler.crm.webservice.helper.CustomerProblemHelper;
import com.orangecaraibe.enabler.crm.webservice.validator.CustomerProblemValidator;
import com.orangecaraibe.enabler.externalws.helpers.ThemeHelper;
import com.orangecaraibe.soa.v2.criteria.CriteriaComposer;
import com.orangecaraibe.soa.v2.criteria.Variations;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.CreateException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.FindAndGetReasonSpecException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.ManageCustomerProblem;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.UpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.schemas.CreateFault;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.schemas.FindAndGetFault;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.schemas.FindAndGetReasonSpecFault;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.schemas.InterfaceUnavailableFault;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.schemas.UpdateFault;
import com.orangecaraibe.soa.v2.interfaces.manageinteraction.ManageInteraction;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.AtomicInteraction;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionDetail;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionDetailType;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.ContactMethod;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.contactmethods.EmailAddress;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.role.CustomerServiceRepresentative;
import com.orangecaraibe.soa.v2.model.commonbusiness.troubleticket.ResourceTroubleTicket;
import com.orangecaraibe.soa.v2.model.commons.UseCase;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.commons.criteria.In;
import com.orangecaraibe.soa.v2.model.commons.criteria.Not;
import com.orangecaraibe.soa.v2.model.commons.criteria.Restriction;
import com.orangecaraibe.soa.v2.model.commons.exception.FaultDetail;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblem;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblemStatusCode;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.LocalReasonSpecification;
import com.orangecaraibe.soa.v2.model.marketsales.marketingcampaigns.Email;

/**
 * Base implementation of CustomerProblemService Interface.
 */
@SuppressWarnings( "unchecked" )
@Transactional( propagation = Propagation.SUPPORTS, readOnly = false )
@Service( "manageCustomerProblemService" )
public class CustomerProblemServiceImpl
	implements ManageCustomerProblem
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( CustomerProblemServiceImpl.class );

	/** The customer problem manager. */
	@Autowired
	private CustomerProblemManager customerProblemManager;

	/** The customer problem adapter. */
	@Autowired
	private CustomerProblemAdapter customerProblemAdapter;

	/** The manage interaction. */
	@Autowired
	@Qualifier( "manageInteractionConsumer" )
	private ManageInteraction manageInteraction;

	/** The customer problem helper */
	@Autowired
	private CustomerProblemHelper customerProblemHelper;

	@Autowired
	@Qualifier( "customerProblemService" )
	private CustomerProblemService customerProblemService;

	@Autowired
	private CustomerProblemValidator customerProblemValidator;

	@Autowired
	@Qualifier( "troubleTicketService" )
	private TroubleTicketService troubleTicketService;

	@Value( "${lnk_crm_bscs.partyid.enabled}" )
	private boolean linkEnabled;

	@Autowired
	private ThemeHelper themeHelper;

	@Override
	@Transactional( propagation = Propagation.REQUIRED, readOnly = false )
	public CustomerProblem create( CustomerProblem customerProblem )
		throws CreateException, InterfaceUnavailableException
	{
		if ( LOGGER.isDebugEnabled() )
			LOGGER.debug( "create CustomerProblem call" );

		try
		{
			if ( linkEnabled )
			{
				boolean isValid = customerProblemValidator.createCustomerProblemValidator( customerProblem );
				if ( isValid )
				{
					Request request = customerProblemAdapter.adapteCreate( customerProblem );

					request = customerProblemService.createCustomerProblem( request );

					customerProblem.setTroubleTicketID( request.getGuid() );

					if ( CollectionUtils.isNotEmpty( customerProblem.getCustomerInterlocutor() ) )
					{
						try
						{
							ContactMethod contactMethod =
								customerProblem.getCustomerInterlocutor().get( 0 ).getMayBeContactedUsing();

							if ( contactMethod instanceof EmailAddress )
							{
								sendMessageReceiptEmail( ( (EmailAddress) contactMethod ).getEMailAddress() );
							}

						}
						catch ( com.orangecaraibe.soa.v2.interfaces.manageinteraction.CreateException e )
						{
							CreateFault cFault = new CreateFault();
							cFault.setCode( "CUSP11" );
							cFault.setLabel( "problem during sending email" );
							throw new CreateException( "customerProblem is null", cFault );
						}
						catch ( com.orangecaraibe.soa.v2.interfaces.manageinteraction.InterfaceUnavailableException e )
						{
							InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
							fault.setLabel( "problem during sending email" );
							fault.setCode( "CUSP11" );
							InterfaceUnavailableException exception =
								new InterfaceUnavailableException( fault.getLabel(), fault, e );
							throw exception;
						}
					}
				}
				else
				{

					if ( customerProblem.getCustomerProblemStatus() != null
						&& !customerProblem.getCustomerProblemStatus().isEmpty() )
					{
						customerProblem.getCustomerProblemStatus().get( 0 ).setStatusCode( CustomerProblemStatusCode.OPENED );
					}
					else
					{
						CreateFault cFault = new CreateFault();
						cFault.setCode( "CUSP002" );
						cFault.setLabel( "customerProblem status is null or empty" );
						throw new CreateException( "customerProblem status is null or empty", cFault );
					}
				}
			}
			return customerProblem;

		}
		catch ( RuntimeException e )
		{
			if ( LOGGER.isErrorEnabled() )
				LOGGER.error( StringUtils.EMPTY, e );

			InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
			fault.setLabel( "Create CustomerProblem error" );
			fault.setCode( "CUSP001" );
			InterfaceUnavailableException exception = new InterfaceUnavailableException( fault.getLabel(), fault, e );
			throw exception;
		}
		catch ( CustomerProblemCreateException e1 )
		{
			if ( LOGGER.isErrorEnabled() )
				LOGGER.error( StringUtils.EMPTY, e1 );

			InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
			fault.setLabel( "Create CustomerProblem error" );
			fault.setCode( "CUSP01" );
			if ( StringUtils.isNotEmpty( e1.getMessage() ) )
			{
				FaultDetail faultDetail = new FaultDetail();
				faultDetail.setFieldName( "message" );
				faultDetail.setFieldValue( e1.getMessage() );
				fault.getFaultDetail().add( faultDetail );
			}
			InterfaceUnavailableException exception = new InterfaceUnavailableException( fault.getLabel(), fault, e1 );
			throw exception;
		}
		catch ( CustomerProblemFindReasonException e1 )
		{
			if ( LOGGER.isErrorEnabled() )
				LOGGER.error( StringUtils.EMPTY, e1 );

			InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
			fault.setLabel( "Create CustomerProblem error" );
			fault.setCode( "CUSP01" );
			InterfaceUnavailableException exception = new InterfaceUnavailableException( fault.getLabel(), fault, e1 );
			throw exception;
		}
		catch ( IOException e1 )
		{
			InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
			fault.setLabel( "Create CustomerProblem error" );
			fault.setCode( "CUSP01" );
			if ( StringUtils.isNotEmpty( e1.getMessage() ) )
			{
				FaultDetail faultDetail = new FaultDetail();
				faultDetail.setFieldName( "message" );
				faultDetail.setFieldValue( e1.getMessage() );
				fault.getFaultDetail().add( faultDetail );
			}
			InterfaceUnavailableException exception = new InterfaceUnavailableException( fault.getLabel(), fault, e1 );
			throw exception;
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.ManageCustomerProblem#findAndGet(com.orangecaraibe.
	 * soa.v2.model.commons.criteria.Criteria)
	 */
	@Override
	public List<CustomerProblem> findAndGet( Criteria criteria )
		throws FindAndGetReasonSpecException, FindAndGetException, InterfaceUnavailableException
	{

		if ( LOGGER.isDebugEnabled() )
			LOGGER.debug( "findAndGet call" );
		List<CustomerProblem> customerProblems = new ArrayList<CustomerProblem>();
		if ( linkEnabled )
		{
			List<Request> requests = new ArrayList<Request>();
			try
			{

				String interactionId =
					(String) CriteriaComposer.extract( criteria ).get( CriteriaConstants.INTERACTION_ID );
				String customerProblemID =
					(String) CriteriaComposer.extract( criteria ).get( CriteriaConstants.CUSTOMER_PROBLEM_ID );
				String billingAccountID =
					(String) CriteriaComposer.extract( criteria ).get( CriteriaConstants.BILLING_ACCOUNT_ID );
				String installedContractID =
					(String) CriteriaComposer.extract( criteria ).get( CriteriaConstants.INSTALLED_CONTRACT_ID );
				String installedPublicKeyID =
					(String) CriteriaComposer.extract( criteria ).get( CriteriaConstants.INSTALLED_PUBLIC_KEY_ID );
				String partyID = (String) CriteriaComposer.extract( criteria ).get( CriteriaConstants.PARTY_ID );
				XMLGregorianCalendar fromDateXml =
					(XMLGregorianCalendar) CriteriaComposer.extract( criteria ).get( CriteriaConstants.FROM_DATE );
				XMLGregorianCalendar toDateXml =
					(XMLGregorianCalendar) CriteriaComposer.extract( criteria ).get( CriteriaConstants.TO_DATE );
				// plusieurs statuts possibles
				List<EnumCustomerProblemStatus> customerProblemStatus = criteriaGetCustomerProblemStatus( criteria );

				Date fromDate = null;
				Date toDate = null;
				if ( fromDateXml != null )
				{
					fromDate = fromDateXml.toGregorianCalendar().getTime();
					if ( toDateXml != null )
					{
						toDate = toDateXml.toGregorianCalendar().getTime();
					}
				}

				boolean includeNote = isInclusion( criteria, CriteriaConstants.NOTES );
				boolean includeAttachements = isInclusion( criteria, CriteriaConstants.ATTACHMENTS );
				int maxResults = 0;
				if ( CriteriaComposer.extract( criteria ).get( CriteriaConstants.MAX_RESULT ) != null )
				{
					maxResults = (Integer) CriteriaComposer.extract( criteria ).get( CriteriaConstants.MAX_RESULT );
				}
				boolean excludeTheme = criteriaExcludeTheme( criteria );
				List<Theme> themes = criteriaGetTheme( criteria );
				for ( Theme theme : themes )
				{
					try
					{
						// theme.setLabel(criteria.);
						themeHelper.getGUIDTheme( theme );
					}
					catch ( CustomerProblemFindReasonException e )
					{
						LOGGER.error( "Error could not get GUID for theme : " + theme.getLabel(), e );
						FindAndGetFault fault = new FindAndGetFault();
						fault.setCode( "CUSP14" );
						fault.setLabel( "Error could not get GUID for theme: " + theme.getLabel() + " message : "
							+ e.getMessage() );
						FindAndGetException ex = new FindAndGetException( fault.getLabel(), fault, e );
						throw ex;
					}
				}
				if ( customerProblemID != null )
				{
					Enabler.addLogDetail( "customerProblemeID ", customerProblemID );
					requests =
						customerProblemService.findAndGetCustomerProblemByCustomerProblemId(	customerProblemID,
																								fromDate, toDate,
																								customerProblemStatus,
																								includeNote,
																								includeAttachements,
																								maxResults,
																								excludeTheme, themes );
				}
				else if ( interactionId != null )
				{
					Enabler.addLogDetail( "interactionId ", interactionId );
					requests = customerProblemService.findAndGetCustomerProblemByInteractionId( interactionId, fromDate,
																								toDate,
																								customerProblemStatus,
																								includeNote,
																								includeAttachements,
																								maxResults,
																								excludeTheme, themes );
				}
				else if ( StringUtils.isNotEmpty( installedContractID ) )
				{
					Enabler.addLogDetail( "co_id ", installedContractID );
					requests =
						customerProblemService.findAndGetCustomerProblemByInstalledContractID(	installedContractID,
																								fromDate, toDate,
																								customerProblemStatus,
																								includeNote,
																								includeAttachements,
																								maxResults,
																								excludeTheme, themes );
				}
				else if ( installedPublicKeyID != null )
				{
					Enabler.addLogDetail( "installedPublicKeyID ", installedPublicKeyID );
					requests =
						customerProblemService.findAndGetCustomerProblembyInstalledPublicKey(	installedPublicKeyID,
																								fromDate, toDate,
																								customerProblemStatus,
																								includeNote,
																								includeAttachements,
																								maxResults,
																								excludeTheme, themes );
				}
				else if ( partyID != null )
				{
					Enabler.addLogDetail( "party guid ", partyID );
					requests =
						customerProblemService.findAndGetCustomerProblemPartyId(	partyID, fromDate, toDate,
																					customerProblemStatus, includeNote,
																					includeAttachements, maxResults,
																					excludeTheme, themes );
				}
				else if ( StringUtils.isNotEmpty( billingAccountID ) )
				{
					Enabler.addLogDetail( "customer_id ", billingAccountID );
					requests =
						customerProblemService.findAndGetCustomerBillingAccountId(	billingAccountID, fromDate, toDate,
																					customerProblemStatus, includeNote,
																					includeAttachements, maxResults,
																					excludeTheme, themes );
				}

				else
				{

					FindAndGetReasonSpecFault fault = new FindAndGetReasonSpecFault();
					fault.setCode( "CRM08" );
					fault.setLabel( "unsupported criteria" );
					FindAndGetReasonSpecException exception =
						new FindAndGetReasonSpecException( "unsupported criteria", fault );
					throw exception;
				}
			}
			catch ( RuntimeException e )
			{
				if ( LOGGER.isErrorEnabled() )
					LOGGER.error( StringUtils.EMPTY, e );

				InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
				fault.setCode( "CRM07" );
				fault.setLabel( "FindAndGet CustomerProblem error " );
				InterfaceUnavailableException exception =
					new InterfaceUnavailableException( fault.getLabel(), fault, e );
				throw exception;
			}
			catch ( CustomerProblemFindAndGetException e )
			{
				LOGGER.error(	"CustomerProblemFindAndGetException occurred while calling ManageCustomerProblem.findAndGet EBS",
								e );
				FindAndGetFault fault = new FindAndGetFault();
				fault.setCode( "CUSP14" );
				fault.setLabel( "FindAndGet CustomerProblem error : " + e.getMessage() );
			}

			customerProblems = customerProblemAdapter.adaptRequestsToCustomerProblems( requests );
		}
		return customerProblems;
	}

	/**
	 * RUNO-6100 - Correctif en référence au code enabler-crm-4.X.X
	 * 
	 * @param criteria
	 * @return true si le theme est exclu
	 */
	private boolean criteriaExcludeTheme( Criteria criteria )
	{
		boolean isToExclude = false;
		for ( Restriction restriction : criteria.getRestrictions() )
		{
			String properties = null;
			if ( restriction instanceof In )
			{
				properties = ( (In) restriction ).getProperty();
				if ( CriteriaConstants.MAIN_REASON.equals( properties ) )
				{
					isToExclude = false;
				}
			}
			else if ( restriction instanceof Not && ( (Not) restriction ).getLeft() instanceof In )
			{
				properties = ( (In) ( (Not) restriction ).getLeft() ).getProperty();
				if ( CriteriaConstants.MAIN_REASON.equals( properties ) )
				{
					isToExclude = true;
				}
			}
		}
		return isToExclude;
	}

	/**
	 * RUNO-6100 - Correctif en référence au code enabler-crm-4.X.X
	 * 
	 * @param criteria
	 * @return une liste de theme suivant les criteria
	 */
	private List<Theme> criteriaGetTheme( Criteria criteria )
	{
		List<String> themesCriteriaList =
			(List<String>) CriteriaComposer.extract( criteria ).get( CriteriaConstants.MAIN_REASON );
		List<Theme> themesList = new ArrayList<Theme>();

		if ( CollectionUtils.isNotEmpty( themesCriteriaList ) )
		{
			for ( String themesCriteria : themesCriteriaList )
			{
				Theme theme = new Theme();
				if ( themesCriteria.matches( "^[A-Za-z0-9]{8}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{12}$" ) )
				{
					theme.setGuid( themesCriteria );
				}
				else
				{
					theme.setLabel( themesCriteria );
				}
				themesList.add( theme );
			}
		}
		return themesList;
	}

	private List<EnumCustomerProblemStatus> criteriaGetCustomerProblemStatus( Criteria criteria )
	{

		List<String> statusCriteriaList =
			(List<String>) CriteriaComposer.extract( criteria ).get( CriteriaConstants.CUSTOMER_PROBLEM_STATUS );
		List<EnumCustomerProblemStatus> statusList = new ArrayList<EnumCustomerProblemStatus>();
		if ( CollectionUtils.isNotEmpty( statusCriteriaList ) )
		{
			for ( String statusCriteria : statusCriteriaList )
			{
				final EnumCustomerProblemStatus st = EnumCustomerProblemStatus.valueOfGDM( statusCriteria );
				if ( null != st )
					statusList.add( st );
			}
		}
		return statusList;
	}

	private boolean isInclusion( Criteria criteria, String criteriaSearch )
	{
		Criteria inclusionCriteria =
			CriteriaComposer.getInstance().add( Variations.include( criteriaSearch ) ).getCriteria();

		return CriteriaComposer.isUseCase( criteria, inclusionCriteria );
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.ManageCustomerProblem#findAndGetReasonSpec(com.
	 * orangecaraibe.soa.v2.model.commons.criteria.Criteria)
	 */
	@Override
	public List<LocalReasonSpecification> findAndGetReasonSpec( Criteria criteria )
		throws FindAndGetReasonSpecException, InterfaceUnavailableException
	{
		if ( LOGGER.isDebugEnabled() )
			LOGGER.debug( "findAndGetReasonSpec call" );
		List<LocalReasonSpecification> result = null;
		if ( linkEnabled )
		{
			try
			{
				String canalEmetteur = customerProblemManager.searchBySaleChannel( criteria );
				String code = customerProblemManager.searchByTyoe( criteria );

				List<CustomerProblemReason> crmReasonSpecList;
				try
				{

					long niveau = 0;
					crmReasonSpecList = customerProblemService.findAndGetReasonSpec( code, canalEmetteur, niveau );

					if ( crmReasonSpecList != null )
					{
						result = customerProblemAdapter.adaptFindAndGetReasonSpec( crmReasonSpecList );
					}

				}
				catch ( CustomerProblemFindReasonException e )
				{
					FindAndGetReasonSpecFault fault = new FindAndGetReasonSpecFault();
					fault.setCode( "CUSP13" );
					fault.setLabel( "FindAndGet Reason spec error : " + e.getMessage() );
					throw new FindAndGetReasonSpecException( fault.getLabel(), fault, e );
				}
			}
			catch ( RuntimeException e )
			{
				if ( LOGGER.isErrorEnabled() )
					LOGGER.error( StringUtils.EMPTY, e );

				InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
				fault.setCode( "CRM07" );
				fault.setLabel( "FindAndGet CustomerProblem error" );
				InterfaceUnavailableException exception =
					new InterfaceUnavailableException( fault.getLabel(), fault, e );
				throw exception;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.ManageCustomerProblem#update(com.orangecaraibe.soa.
	 * v2.model.customerdomain.customerproblem.CustomerProblem, java.util.List)
	 */
	@Override
	public CustomerProblem update( CustomerProblem customerProblem, List<UseCase> useCase )
		throws InterfaceUnavailableException, UpdateException
	{

		if ( linkEnabled )
		{

			UseCasePredicate useCaseUpdateStatus = new UseCasePredicate();
			useCaseUpdateStatus.setUseCaseID( WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_UPDATE_STATUS );

			UseCasePredicate useCaseUpdateOrder = new UseCasePredicate();
			useCaseUpdateOrder.setUseCaseID( WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_UPDATE_ORDER );

			UseCasePredicate useCaseAddNote = new UseCasePredicate();
			useCaseAddNote.setUseCaseID( WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_ADD_NOTE );

			UseCasePredicate useEscalade = new UseCasePredicate();
			useEscalade.setUseCaseID( WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_ESCALATION );

			Request request = new Request();
			if ( CollectionUtils.exists( useCase, useCaseUpdateStatus )
				|| CollectionUtils.exists( useCase, useCaseAddNote )
				|| CollectionUtils.exists( useCase, useCaseUpdateOrder ) )
			{
				Enabler.addLogDetail( "customerProblemeID ", customerProblem.getTroubleTicketID() );
				Enabler.addLogDetail(	"UseCase ",
										WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_UPDATE_STATUS + "|"
											+ WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_ADD_NOTE + "|"
											+ WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_UPDATE_ORDER );
				try
				{
					customerProblemValidator.customerProblemUpdateValidator( customerProblem );
					boolean adaptStatusAndReason = false;
					// on ne fait l'adaptation des status et reason que pour le use case de mise a jour du statut
					// car par defaut, on passe la demande au statut resolu
					if ( CollectionUtils.exists( useCase, useCaseUpdateStatus ) )
					{
						adaptStatusAndReason = true;
					}
					request = customerProblemAdapter.adaptCustomerProblemToRequestUpdate(	customerProblem,
																							adaptStatusAndReason );
					request = customerProblemService.update( request );
				}
				catch ( CustomerProblemUpdateException e )
				{
					UpdateFault fault = new UpdateFault();
					fault.setCode( "CUSP08" );
					fault.setLabel( "UPDATE CustomerProblem error :" + e.getMessage() );
					if ( StringUtils.isNotEmpty( e.getMessage() ) )
					{
						FaultDetail faultDetail = new FaultDetail();
						faultDetail.setFieldName( "message" );
						faultDetail.setFieldValue( e.getMessage() );
						fault.getFaultDetail().add( faultDetail );
					}
					throw new UpdateException( fault.getLabel(), fault, e );
				}
			}

			UseCasePredicate useCaseNotifyDunning = new UseCasePredicate();
			useCaseNotifyDunning.setUseCaseID( WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_NOTIFY_DUNNING );
			if ( CollectionUtils.exists( useCase, useCaseNotifyDunning ) )
			{

				Enabler.addLogDetail( "customerProblemeID ", customerProblem.getTroubleTicketID() );
				Enabler.addLogDetail( "UseCase ", WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_NOTIFY_DUNNING );
				customerProblemValidator.updateValidator( customerProblem );
				try
				{
					request = customerProblemAdapter.updateNotifyDunningAdaptation( customerProblem );
					request = customerProblemService.updateNotifyDunning( request );
				}
				catch ( CustomerProblemUpdateException e1 )
				{
					if ( LOGGER.isErrorEnabled() )
						LOGGER.error( StringUtils.EMPTY, e1 );

					InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
					fault.setLabel( "UPDATE CustomerProblem error " + e1.getMessage() );
					fault.setCode( "CUSP08" );
					InterfaceUnavailableException exception =
						new InterfaceUnavailableException( fault.getLabel(), fault, e1 );
					throw exception;
				}
			}

			if ( CollectionUtils.exists( useCase, useEscalade ) )
			{

				Enabler.addLogDetail( "customerProblemeID ", customerProblem.getTroubleTicketID() );
				Enabler.addLogDetail( "UseCase ", WebservicesConstantes.CUSTOMER_PROBLEM_USE_CASE_ESCALATION );
				String requestId = customerProblem.getTroubleTicketID();
				List<Request> lstRequest = new ArrayList<Request>();

				String cuid = null;

				for ( CustomerServiceRepresentative customerService : customerProblem.getCustomerServiceRepresentative() )
				{
					cuid = customerService.getUserIdentity().getGID();
					// on sort pour eviter des eventuels tours de boucle mais on boucle pour anticiper le cas ou l'on
					// aurait plusieurs CSR
					break;
				}

				try
				{
					lstRequest =
						customerProblemService.findAndGetCustomerProblemByCustomerProblemId(	requestId, null, null,
																								null, false, false, 1,
																								false, null, true );
					if ( CollectionUtils.isNotEmpty( lstRequest ) )
					{
						request = lstRequest.get( 0 );
						// Si le TroubleTicketID est vide ==> création, sinon update
						if ( StringUtils.isEmpty( request.getTroubleTicketID() ) )
						{
							request = troubleTicketService.createTroubleTicket( request, cuid );
						}
						else
						{
							request = troubleTicketService.updateTroubleTicket( request, cuid );
						}

						ResourceTroubleTicket troubleTicket = new ResourceTroubleTicket();
						troubleTicket.setInteractionID( request.getTroubleTicketID() );

						customerProblem.getCFSTroubleTicket().add( troubleTicket );
					}

				}
				catch ( CustomerProblemFindAndGetException e )
				{
					if ( LOGGER.isErrorEnabled() )
						LOGGER.error( StringUtils.EMPTY, e );

					InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
					fault.setLabel( "UPDATE CustomerProblem error " + e.getMessage() );
					fault.setCode( "CUSP08" );
					InterfaceUnavailableException exception =
						new InterfaceUnavailableException( fault.getLabel(), fault, e );
					throw exception;
				}
				catch ( CustomerProblemUpdateException e )
				{
					if ( LOGGER.isErrorEnabled() )
						LOGGER.error( StringUtils.EMPTY, e );

					InterfaceUnavailableFault fault = new InterfaceUnavailableFault();
					fault.setLabel( "UPDATE CustomerProblem error " + e.getMessage() );
					fault.setCode( "CUSP08" );
					InterfaceUnavailableException exception =
						new InterfaceUnavailableException( fault.getLabel(), fault, e );
					throw exception;
				}
			}
		}
		return customerProblem;
	}

	/**
	 * Send message receipt.
	 * 
	 * @param mailAddress the mail address
	 * @throws CreateException the create exception
	 * @throws InterfaceUnavailableException the interface unavailable exception
	 */
	private void sendMessageReceiptEmail( String mailAddress )
		throws com.orangecaraibe.soa.v2.interfaces.manageinteraction.CreateException,
		com.orangecaraibe.soa.v2.interfaces.manageinteraction.InterfaceUnavailableException
	{
		String htmlText = customerProblemHelper.getCustomerProblemHTML1();
		htmlText += customerProblemHelper.getCustomerProblemHTML2();
		htmlText += customerProblemHelper.getCustomerProblemHTML3();
		htmlText += customerProblemHelper.getCustomerProblemHTML4();
		;

		AtomicInteraction mail = new AtomicInteraction();
		mail.setMedia( new Email() );
		InteractionDetail detail = new InteractionDetail();
		detail.setInteractionBody( htmlText );
		detail.setInteractionSubject( "Accusé de reception" );
		detail.setInteractionDetailType( InteractionDetailType.HTML );
		mail.getDetails().add( detail );
		EmailAddress to2 = new EmailAddress();
		to2.setEMailAddress( mailAddress );
		EmailAddress from2 = new EmailAddress();
		from2.setEMailAddress( customerProblemHelper.getMailAdresse() );

		mail.setFromContactMethod( from2 );
		mail.setToContactMethod( to2 );

		mail = (AtomicInteraction) manageInteraction.create( mail );

	}

	/**
	 * @return the linkEnabled
	 */
	public boolean isLinkEnabled()
	{
		return linkEnabled;
	}

	/**
	 * @param linkEnabled the linkEnabled to set
	 */
	public void setLinkEnabled( boolean linkEnabled )
	{
		this.linkEnabled = linkEnabled;
	}
}
