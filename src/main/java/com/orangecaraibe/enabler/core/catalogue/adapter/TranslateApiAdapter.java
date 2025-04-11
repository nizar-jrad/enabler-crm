/**
 * 
 */
package com.orangecaraibe.enabler.core.catalogue.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.orangecaraibe.api.catalogue.ws.translate.model.InstalledOfferStatus.StatusCodeEnum;
import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccount;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.OfferSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.Commitment;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOfferStatus;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPublicKey;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.StatusCode;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.StatusReason;

import io.micrometer.core.instrument.util.StringUtils;

/**
 * @author ncrtest2
 */
@Component
public class TranslateApiAdapter
{

	/**
	 * Adapte un installedContract de l'Enabler SOA V2 en installedContract de l'API Catalogue WS.
	 * 
	 * @param soaInstalledContract
	 * @return installedContract
	 */
	public com.orangecaraibe.api.catalogue.ws.translate.model.InstalledContract adaptToInstalledContract( InstalledContract soaInstalledContract )
	{

		com.orangecaraibe.api.catalogue.ws.translate.model.InstalledContract installedContract =
			new com.orangecaraibe.api.catalogue.ws.translate.model.InstalledContract();

		if ( soaInstalledContract != null )
		{
			installedContract.setInstalledOfferID( soaInstalledContract.getInstalledOfferID() );

			if ( soaInstalledContract.getOfferSpecification() != null )
			{
				installedContract.setOfferSpecificationCode( soaInstalledContract.getOfferSpecification().getOfferSpecificationCode() );
			}

			installedContract.setBillingAccount( adaptToBillingAccount( soaInstalledContract.getBillingAccount() ) );
			installedContract.setLocalRIO( soaInstalledContract.getLocalRIO() );
			installedContract.setLocalModificationDate( DateUtils.getLocalDateFromXmlGregorianCalendar( soaInstalledContract.getModificationDate() ) );
			installedContract.setLocalCreationDate( DateUtils.getLocalDateFromXmlGregorianCalendar( soaInstalledContract.getCreationDate() ) );
			installedContract.setLocalCommitmentExcluded( soaInstalledContract.getLocalCommitmentExcluded() );
			installedContract.setInstalledPublicKeys( adaptToInstalledPublicKeys( soaInstalledContract.getInstalledPublicKey() ) );
			installedContract.setInstalledOfferStatus( adaptToInstalledOfferStatusList( soaInstalledContract.getInstalledOfferStatus() ) );
			installedContract.setCommitments( adaptToCommitments( soaInstalledContract.getCommitment() ) );
		}

		return installedContract;
	}

	/**
	 * Adapte un installedContract de l'API Catalogue WS en installedContract de l'Enabler SOA V2.
	 * 
	 * @param installedContract
	 * @return soaInstalledContract
	 */
	public InstalledContract adaptToSoaInstalledContract( com.orangecaraibe.api.catalogue.ws.translate.model.InstalledContract installedContract )
	{

		InstalledContract soaInstalledContract = new InstalledContract();

		if ( installedContract != null )
		{
			soaInstalledContract.setInstalledOfferID( installedContract.getInstalledOfferID() );

			OfferSpecification offerSpecification = new OfferSpecification();
			offerSpecification.setOfferSpecificationCode( installedContract.getOfferSpecificationCode() );
			soaInstalledContract.setOfferSpecification( offerSpecification );

			soaInstalledContract.setBillingAccount( adaptToSoaBillingAccount( installedContract.getBillingAccount() ) );
			soaInstalledContract.setLocalRIO( installedContract.getLocalRIO() );
			soaInstalledContract.setModificationDate( DateUtils.getXmlGregorianCalendarFromLocalDate( installedContract.getLocalModificationDate() ) );
			soaInstalledContract.setCreationDate( DateUtils.getXmlGregorianCalendarFromLocalDate( installedContract.getLocalCreationDate() ) );
			soaInstalledContract.setLocalCommitmentExcluded( installedContract.getLocalCommitmentExcluded() );
			soaInstalledContract.getInstalledPublicKey().addAll( adaptToSoaInstalledPublicKeys( installedContract.getInstalledPublicKeys() ) );
			soaInstalledContract.getInstalledOfferStatus().addAll( adaptToSoaInstalledOfferStatusList( installedContract.getInstalledOfferStatus() ) );
			soaInstalledContract.getCommitment().addAll( adaptToSoaCommitments( installedContract.getCommitments() ) );
		}

		return soaInstalledContract;
	}

	/**
	 * Adapte un BillingAccount de l'Enabler SOA V2 en BillingAccount de l'API Catalogue WS.
	 * 
	 * @param soaBillingAccount
	 * @return BillingAccount de l'API Catalogue WS
	 */
	private com.orangecaraibe.api.catalogue.ws.translate.model.BillingAccount adaptToBillingAccount( BillingAccount soaBillingAccount )
	{

		com.orangecaraibe.api.catalogue.ws.translate.model.BillingAccount billingAccount =
			new com.orangecaraibe.api.catalogue.ws.translate.model.BillingAccount();

		if ( soaBillingAccount != null )
		{
			billingAccount.setBillingAccountID( soaBillingAccount.getBillingAccountID() );
		}

		return billingAccount;

	}

	/**
	 * Adapte un BillingAccount de l'API Catalogue WS en BillingAccount de l'Enabler SOA V2.
	 * 
	 * @param billingAccount
	 * @return soaBillingAccount
	 */
	private BillingAccount adaptToSoaBillingAccount( com.orangecaraibe.api.catalogue.ws.translate.model.BillingAccount billingAccount )
	{

		BillingAccount soaBillingAccount = new BillingAccount();

		if ( billingAccount != null )
		{
			soaBillingAccount.setBillingAccountID( billingAccount.getBillingAccountID() );
		}

		return soaBillingAccount;

	}

	/**
	 * Adapte une liste de InstalledPublicKey de l'Enabler SOA V2 en liste de InstalledPublicKey de l'API Catalogue WS.
	 * 
	 * @param soaInstalledPublicKeys
	 * @return List<InstalledPublicKey> de l'API Catalogue WS
	 */
	private List<com.orangecaraibe.api.catalogue.ws.translate.model.InstalledPublicKey> adaptToInstalledPublicKeys( List<InstalledPublicKey> soaInstalledPublicKeys )
	{

		List<com.orangecaraibe.api.catalogue.ws.translate.model.InstalledPublicKey> installedPublicKeys =
			new ArrayList<com.orangecaraibe.api.catalogue.ws.translate.model.InstalledPublicKey>();

		if ( CollectionUtils.isNotEmpty( soaInstalledPublicKeys ) )
		{
			for ( InstalledPublicKey soaInstalledPublicKey : soaInstalledPublicKeys )
			{
				installedPublicKeys.add( adaptToInstalledPublicKey( soaInstalledPublicKey ) );
			}
		}

		return installedPublicKeys;

	}

	/**
	 * Adapte une InstalledPublicKey de l'Enabler SOA V2 en InstalledPublicKey de l'API Catalogue WS.
	 * 
	 * @param soaInstalledPublicKey
	 * @return InstalledPublicKey
	 */
	private com.orangecaraibe.api.catalogue.ws.translate.model.InstalledPublicKey adaptToInstalledPublicKey( InstalledPublicKey soaInstalledPublicKey )
	{

		com.orangecaraibe.api.catalogue.ws.translate.model.InstalledPublicKey installedPublicKey =
			new com.orangecaraibe.api.catalogue.ws.translate.model.InstalledPublicKey();

		if ( soaInstalledPublicKey != null )
		{
			installedPublicKey.setInstalledPublicKeyID( soaInstalledPublicKey.getInstalledPublicKeyID() );
			installedPublicKey.setValue( soaInstalledPublicKey.getFunctionValue() );
		}

		return installedPublicKey;

	}

	/**
	 * Adapte une InstalledPublicKey de l'API Catalogue WS en InstalledPublicKey de l'Enabler SOA V2.
	 * 
	 * @param installedPublicKey
	 * @return soaInstalledPublicKey
	 */
	private InstalledPublicKey adaptToSoaInstalledPublicKey( com.orangecaraibe.api.catalogue.ws.translate.model.InstalledPublicKey installedPublicKey )
	{

		InstalledPublicKey soaInstalledPublicKey = new InstalledPublicKey();

		if ( installedPublicKey != null )
		{
			soaInstalledPublicKey.setInstalledPublicKeyID( installedPublicKey.getInstalledPublicKeyID() );
			soaInstalledPublicKey.setFunctionValue( installedPublicKey.getValue() );
		}

		return soaInstalledPublicKey;

	}

	/**
	 * Adapte une liste de InstalledPublicKey de l'API Catalogue WS en liste de InstalledPublicKey de l'Enabler SOA V2.
	 * 
	 * @param installedPublicKeys
	 * @return List<InstalledPublicKey> de l'Enabler SOA V2
	 */
	private List<InstalledPublicKey> adaptToSoaInstalledPublicKeys( List<com.orangecaraibe.api.catalogue.ws.translate.model.InstalledPublicKey> installedPublicKeys )
	{

		List<InstalledPublicKey> soaInstalledPublicKeys = new ArrayList<InstalledPublicKey>();

		if ( CollectionUtils.isNotEmpty( installedPublicKeys ) )
		{
			for ( com.orangecaraibe.api.catalogue.ws.translate.model.InstalledPublicKey installedPublicKey : installedPublicKeys )
			{
				soaInstalledPublicKeys.add( adaptToSoaInstalledPublicKey( installedPublicKey ) );
			}
		}

		return soaInstalledPublicKeys;

	}

	/**
	 * Adapte un InstalledOfferStatus de l'Enabler SOA V2 en InstalledOfferStatus de l'API Catalogue WS.
	 * 
	 * @param soaInstalledOfferStatus
	 * @return InstalledOfferStatus
	 */
	private com.orangecaraibe.api.catalogue.ws.translate.model.InstalledOfferStatus adaptToInstalledOfferStatus( InstalledOfferStatus soaInstalledOfferStatus )
	{

		com.orangecaraibe.api.catalogue.ws.translate.model.InstalledOfferStatus installedOfferStatus =
			new com.orangecaraibe.api.catalogue.ws.translate.model.InstalledOfferStatus();

		if ( soaInstalledOfferStatus != null )
		{
			installedOfferStatus.setLocalTechnicalStatusReason( soaInstalledOfferStatus.getLocalTechnicalStatusReason() );
			StatusCodeEnum statusCode = StatusCodeEnum.valueOf( soaInstalledOfferStatus.getStatusCode().name() );
			installedOfferStatus.setStatusCode( statusCode );
			installedOfferStatus.setStatusReason( soaInstalledOfferStatus.getStatusReason().toString() );
			installedOfferStatus.setStartDate( DateUtils.getLocalDateFromXmlGregorianCalendar( soaInstalledOfferStatus.getStartDate() ) );

		}

		return installedOfferStatus;
	}

	/**
	 * Adapte une liste de InstalledOfferStatus de l'Enabler SOA V2 en liste de InstalledOfferStatus de l'API Catalogue
	 * WS.
	 * 
	 * @param soaInstalledOfferStatus
	 * @return List<InstalledOfferStatus> de l'API Catalogue WS
	 */
	private List<com.orangecaraibe.api.catalogue.ws.translate.model.InstalledOfferStatus> adaptToInstalledOfferStatusList( List<InstalledOfferStatus> soaInstalledOfferStatusList )
	{

		List<com.orangecaraibe.api.catalogue.ws.translate.model.InstalledOfferStatus> installedOfferStatusList =
			new ArrayList<com.orangecaraibe.api.catalogue.ws.translate.model.InstalledOfferStatus>();

		if ( CollectionUtils.isNotEmpty( soaInstalledOfferStatusList ) )
		{
			for ( InstalledOfferStatus soaInstalledOfferStatus : soaInstalledOfferStatusList )
			{
				installedOfferStatusList.add( adaptToInstalledOfferStatus( soaInstalledOfferStatus ) );
			}
		}

		return installedOfferStatusList;

	}

	/**
	 * Adapte un InstalledOfferStatus de l'API Catalogue WS en InstalledOfferStatus de l'Enabler SOA V2.
	 * 
	 * @param InstalledOfferStatus
	 * @return soaInstalledOfferStatus
	 */
	private InstalledOfferStatus adaptToSoaInstalledOfferStatus( com.orangecaraibe.api.catalogue.ws.translate.model.InstalledOfferStatus installedOfferStatus )
	{

		InstalledOfferStatus soaInstalledOfferStatus = new InstalledOfferStatus();

		if ( installedOfferStatus != null )
		{
			soaInstalledOfferStatus.setLocalTechnicalStatusReason( installedOfferStatus.getLocalTechnicalStatusReason() );
			StatusCode statusCode = StatusCode.valueOf( installedOfferStatus.getStatusCode().name() );
			soaInstalledOfferStatus.setStatusCode( statusCode );
			soaInstalledOfferStatus.setStatusReason( StatusReason.fromValue( installedOfferStatus.getStatusReason() ) );
			soaInstalledOfferStatus.setStartDate( DateUtils.getXmlGregorianCalendarFromLocalDate( installedOfferStatus.getStartDate() ) );

		}

		return soaInstalledOfferStatus;
	}

	/**
	 * Adapte une liste de InstalledOfferStatus de l'API Catalogue WS en liste de InstalledOfferStatus de l'Enabler SOA
	 * V2.
	 * 
	 * @param soaInstalledOfferStatus
	 * @return List<InstalledOfferStatus> de l'Enabler SOA V2
	 */
	private List<InstalledOfferStatus> adaptToSoaInstalledOfferStatusList( List<com.orangecaraibe.api.catalogue.ws.translate.model.InstalledOfferStatus> installedOfferStatusList )
	{

		List<InstalledOfferStatus> soaInstalledOfferStatusList = new ArrayList<InstalledOfferStatus>();

		if ( CollectionUtils.isNotEmpty( installedOfferStatusList ) )
		{
			for ( com.orangecaraibe.api.catalogue.ws.translate.model.InstalledOfferStatus installedOfferStatus : installedOfferStatusList )
			{
				soaInstalledOfferStatusList.add( adaptToSoaInstalledOfferStatus( installedOfferStatus ) );
			}
		}

		return soaInstalledOfferStatusList;

	}

	/**
	 * Adapte un Commitment de l'Enabler SOA V2 en Commitment de l'API Catalogue WS.
	 * 
	 * @param soaCommitment
	 * @return commitment
	 */
	private com.orangecaraibe.api.catalogue.ws.translate.model.Commitment adaptToCommitment( Commitment soaCommitment )
	{

		com.orangecaraibe.api.catalogue.ws.translate.model.Commitment commitment =
			new com.orangecaraibe.api.catalogue.ws.translate.model.Commitment();

		if ( soaCommitment != null )
		{
			commitment.setDurationChosen( soaCommitment.getDurationChosen() );
			commitment.setEndDate( DateUtils.getLocalDateFromXmlGregorianCalendar( soaCommitment.getEndDate() ) );
			commitment.setLocalAmount( soaCommitment.getLocalAmount().toString() );
			commitment.setLocalDelay( String.valueOf( soaCommitment.getLocalDelay() ) );
			commitment.setLocalDelayDay( soaCommitment.getLocalDelayDay().toString() );
			commitment.setLocalDelayMonth( soaCommitment.getLocalDelayMonth().toString() );
			commitment.setStartDate( DateUtils.getLocalDateFromXmlGregorianCalendar( soaCommitment.getStartDate() ) );
			commitment.setTrigger( soaCommitment.getTrigger() );
		}

		return commitment;
	}

	/**
	 * Adapte une liste de Commitment de l'Enabler SOA V2 en liste de Commitment de l'API Catalogue WS.
	 * 
	 * @param soaCommitments
	 * @return List<Commitment> de l'API Catalogue WS
	 */
	private List<com.orangecaraibe.api.catalogue.ws.translate.model.Commitment> adaptToCommitments( List<Commitment> soaCommitments )
	{

		List<com.orangecaraibe.api.catalogue.ws.translate.model.Commitment> commitments =
			new ArrayList<com.orangecaraibe.api.catalogue.ws.translate.model.Commitment>();

		if ( CollectionUtils.isNotEmpty( soaCommitments ) )
		{
			for ( Commitment soaCommitment : soaCommitments )
			{
				commitments.add( adaptToCommitment( soaCommitment ) );
			}
		}

		return commitments;

	}

	/**
	 * Adapte un Commitment de l'API Catalogue WS en Commitment de l'Enabler SOA V2.
	 * 
	 * @param commitment
	 * @return soaCommitment
	 */
	private Commitment adaptToSoaCommitment( com.orangecaraibe.api.catalogue.ws.translate.model.Commitment commitment )
	{

		Commitment soaCommitment = new Commitment();

		if ( commitment != null )
		{
			soaCommitment.setDurationChosen( commitment.getDurationChosen() );
			soaCommitment.setEndDate( DateUtils.getXmlGregorianCalendarFromLocalDate( commitment.getEndDate() ) );

			if ( StringUtils.isNotEmpty( commitment.getLocalAmount() ) )
			{
				soaCommitment.setLocalAmount( Double.parseDouble( commitment.getLocalAmount() ) );
			}
			if ( StringUtils.isNotEmpty( commitment.getLocalDelay() ) )
			{
				soaCommitment.setLocalDelay( Integer.parseInt( commitment.getLocalDelay() ) );
			}
			if ( StringUtils.isNotEmpty( commitment.getLocalDelayDay() ) )
			{
				soaCommitment.setLocalDelayDay( Integer.valueOf( commitment.getLocalDelay() ) );
			}
			if ( StringUtils.isNotEmpty( commitment.getLocalDelayMonth() ) )
			{
				soaCommitment.setLocalDelayMonth( Integer.valueOf( commitment.getLocalDelayMonth() ) );
			}

			soaCommitment.setStartDate( DateUtils.getXmlGregorianCalendarFromLocalDate( commitment.getStartDate() ) );
			soaCommitment.setTrigger( commitment.getTrigger() );
		}

		return soaCommitment;
	}

	/**
	 * Adapte une liste de Commitment de l'API Catalogue WS en liste de Commitment de l'Enabler SOA V2.
	 * 
	 * @param commitments
	 * @return List<Commitment> de l'Enabler SOA V2
	 */
	private List<Commitment> adaptToSoaCommitments( List<com.orangecaraibe.api.catalogue.ws.translate.model.Commitment> commitments )
	{

		List<Commitment> soaCommitments = new ArrayList<Commitment>();

		if ( CollectionUtils.isNotEmpty( commitments ) )
		{
			for ( com.orangecaraibe.api.catalogue.ws.translate.model.Commitment commitment : commitments )
			{
				soaCommitments.add( adaptToSoaCommitment( commitment ) );
			}
		}

		return soaCommitments;

	}

}
