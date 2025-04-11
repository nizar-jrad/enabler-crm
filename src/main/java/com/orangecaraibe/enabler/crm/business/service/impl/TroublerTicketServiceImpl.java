/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.orange.sidom.soa.dcrm.datacontract.PartyCriteriaType;
import com.orangecaraibe.enabler.crm.business.bean.Holder;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumContractStatus;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;
import com.orangecaraibe.enabler.crm.business.service.TroubleTicketService;
import com.orangecaraibe.enabler.externalws.services.impl.ExternalWSServiceImpl;
import com.orangecaraibe.enabler.externalws.utils.ExternalWSConstants;
import com.orangecaraibe.soa.v2.criteria.CriteriaComposer;
import com.orangecaraibe.soa.v2.criteria.Restrictions;
import com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.FindAndGetException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.InterfaceUnavailableException;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.CommercialClassification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.OfferSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledAtomicOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOfferStatus;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPlay;

/**
 * @author NCR
 */
@Service( "troubleTicketService" )
public class TroublerTicketServiceImpl
	implements TroubleTicketService
{

	private static final String NON_ELIGIBLE_FIDE = "NON_ELIGIBLE_FIDE";

	public final static String COID_PREFIX = "CO_ID:";

	public final static String RATEPLAN_PREFIX = "RATEPLAN:";

	public final static String CONSUMER_TYPE = "CONSUMER";

	public final static String BUSINESS_TYPE = "BUSINESS";

	public final static String PPS = "PPS";

	public final static String PPD = "PPD";

	public final static String PPC = "PPC";

	public final static String DATA = "DATA";

	@Autowired
	@Qualifier( "externalWSService" )
	ExternalWSServiceImpl externalWSService;

	private static final Logger LOGGER = LoggerFactory.getLogger( TroublerTicketServiceImpl.class );

	@Override
	public Request createTroubleTicket( Request request, String cuid )
		throws CustomerProblemUpdateException, InterfaceUnavailableException
	{

		List<Holder> lstholder = new ArrayList<Holder>();
		if ( request.getHolder() != null )
		{
			String partyGuid = request.getHolder().getGuid();

			lstholder = externalWSService.findPartyInformation( PartyCriteriaType.GUID_PARTY, partyGuid, false, false,
																false, true );

		}

		if ( CollectionUtils.isNotEmpty( lstholder ) )
		{
			request.setHolder( lstholder.get( 0 ) );
		}

		String coId = null;
		if ( request.getContract() != null )
		{
			coId = request.getContract().getCoId();
			Criteria criteria = CriteriaComposer.getInstance().getCriteria();
			criteria.getRestrictions().add( Restrictions.eq( ExternalWSConstants.INSTALLED_OFFER_ID, coId ) );

			List<InstalledOffer> installedOffers = null;

			try
			{
				installedOffers = externalWSService.findAndGetSuperMCIB( criteria );
			}
			catch ( com.orangecaraibe.soa.v2.interfaces.managecommercialinstalledbase.InterfaceUnavailableException e )
			{
				LOGGER.error( "Interface Oceane not availabler ", e );

			}
			catch ( FindAndGetException e )
			{
				LOGGER.error( "FindAndGetException", e );
			}

			if ( CollectionUtils.isNotEmpty( installedOffers ) )
			{
				InstalledOffer installedOffer = installedOffers.get( 0 );
				InstalledContract installedContract = (InstalledContract) installedOffer;
				if ( installedOffer instanceof InstalledContract )
				{
					// Recupération du statut
					if ( installedContract.getInstalledOfferStatus() != null
						&& !installedContract.getInstalledOfferStatus().isEmpty() )
					{
						InstalledOfferStatus opStatus = installedContract.getInstalledOfferStatus().get( 0 );
						if ( opStatus != null && opStatus.getStatusCode() != null )
						{
							EnumContractStatus status =
								EnumContractStatus.getEnumContractStatus( opStatus.getStatusCode().name() );
							request.getContract().setStatus( status );
						}

						for ( InstalledOffer childPlayOffer : installedContract.getChildrenInstalledOffers() )
						{
							InstalledPlay installedPlay = (InstalledPlay) childPlayOffer;
							for ( InstalledOffer childOffer : installedPlay.getChildrenInstalledOffers() )
							{
								if ( childOffer instanceof InstalledAtomicOffer )
								{
									InstalledAtomicOffer childAtomicOffer = (InstalledAtomicOffer) childOffer;

									if ( childAtomicOffer != null && childAtomicOffer.getOfferSpecification() != null )
									{
										OfferSpecification offerSpecification =
											childAtomicOffer.getOfferSpecification();
										List<CommercialClassification> classifications =
											offerSpecification.getCommercialClassifications();
										for ( CommercialClassification commercialClassification : classifications )
										{
											if ( commercialClassification.getCode() != null
												&& commercialClassification.getCode().equals( BUSINESS_TYPE ) )
											{
												request.getContract().setSegment( BUSINESS_TYPE );
											}
											else if ( commercialClassification.getCode() != null
												&& commercialClassification.getCode().equals( CONSUMER_TYPE ) )
											{
												request.getContract().setSegment( CONSUMER_TYPE );
											}

											else if ( commercialClassification.getCode() != null
												&& commercialClassification.getCode().equals( PPS ) )
											{
												request.getContract().setProduct( PPS );
											}
											else if ( commercialClassification.getCode() != null
												&& commercialClassification.getCode().equals( PPC ) )
											{
												request.getContract().setProduct( PPC );
											}
											else if ( commercialClassification.getCode() != null
												&& commercialClassification.getCode().equals( PPD ) )
											{
												request.getContract().setProduct( PPD );
											}
											else if ( commercialClassification.getCode() != null
												&& commercialClassification.getCode().equals( DATA ) )
											{
												request.getContract().setData( true );
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

		return externalWSService.createTroubleTicketOceane( request, cuid );
	}

	/**
	 * @param externalWSService the externalWSService to set
	 */
	public void setExternalWSService( ExternalWSServiceImpl externalWSService )
	{
		this.externalWSService = externalWSService;
	}

	@Override
	public void updateStatusTicket( Request request )
	{

		try
		{
			externalWSService.updateCustomerProblem( request );
		}
		catch ( CustomerProblemUpdateException e )
		{
			LOGGER.error( e.getMessage(), e );
		}
	}

	@Override
	public Request updateTroubleTicket( Request request, String cuid )
		throws CustomerProblemUpdateException
	{
		return externalWSService.updateTroubleTicketOceane( request, cuid );
	}

}
