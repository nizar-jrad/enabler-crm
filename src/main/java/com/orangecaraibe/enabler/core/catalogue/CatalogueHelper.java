/**
 *
 */
package com.orangecaraibe.enabler.core.catalogue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.AtomicOfferSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.CommercialClassification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.ContractSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.OfferSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledAtomicOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOfferStatus;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledPublicKey;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.StatusCode;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.StatusReason;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionValueSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.GenericProduct;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.ProductSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productinstalledbase.InstalledProduct;

/**
 * Helper à la récupération des offres via l'api-catalogue-ws
 * 
 * @author sgodard
 */
@Component( "crm.catalogueHelper" )
public class CatalogueHelper
{

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger( CatalogueHelper.class );

	@Autowired
	@Qualifier( "externCastorApi" )
	private ExternCastorApi castorApi;

	@Autowired
	@Qualifier( "externTranslateApi" )
	private ExternTranslateApi translateApi;

	/**
	 * Methode de récupération du contrat
	 * 
	 * @param contract
	 * @param customer
	 * @return
	 */
	public InstalledContract getCastorInstalledContract( String coid, String tmCode, String msisdn )
	{
		// Contract
		String contractSpecifCode = CatalogueConstantes.BSCS_TMCODE_PREFIX + tmCode;

		InstalledContract legacyContract = generateContract( coid, contractSpecifCode, msisdn );

		try
		{
			InstalledContract installedContract = translateApi.convertLegacyContractToCastor( legacyContract );
			return installedContract;
		}
		catch ( Exception e )
		{
			throw new RuntimeException( e.getMessage(), e );
		}
	}

	public InstalledOffer getIconAtomicOffer( String offerSpecificationCode )
	{
		InstalledAtomicOffer convertedInstalledOffer = new InstalledAtomicOffer();

		AtomicOfferSpecification offerSpec = castorApi.getAtomicOfferSpecification( offerSpecificationCode );

		if ( offerSpec != null )
		{
			convertedInstalledOffer.setInstalledOfferID( offerSpec.getOfferSpecificationCode() + ":"
				+ CatalogueConstantes.DEFAULT_OFFER_ID );

			convertedInstalledOffer.setOfferSpecification( offerSpec );

			List<ProductSpecification> productSpecifications = offerSpec.getProductSpecifications();
			if ( CollectionUtils.isNotEmpty( productSpecifications ) && productSpecifications.get( 0 ) != null )
			{
				InstalledProduct serviceProduct = new InstalledProduct();
				serviceProduct.setInstalledProductID( productSpecifications.get( 0 ).getProductSpecificationCode() + ":"
					+ CatalogueConstantes.DEFAULT_OFFER_ID );
				serviceProduct.setProductSpecification( productSpecifications.get( 0 ) );
				convertedInstalledOffer.getInstalledProducts().add( serviceProduct );
			}
		}

		return convertedInstalledOffer;
	}

	/**
	 * Methode de récupération de l'offre atomic castor
	 * 
	 * @param tmCode
	 * @param spCode
	 * @param snCode
	 * @return
	 */
	public List<InstalledOffer> getIconAtomicOffer( String tmCode, String spCode, String snCode )
	{

		String key = CatalogueConstantes.BSCS_TMCODE_PREFIX + tmCode;

		List<InstalledOffer> result = new ArrayList<InstalledOffer>();

		if ( spCode != null )
		{
			key += ":" + CatalogueConstantes.BSCS_SPCODE_PREFIX + spCode;
		}

		if ( snCode != null )
		{
			key += ":" + CatalogueConstantes.BSCS_SNCODE_PREFIX + snCode;
		}

		Collection<AtomicOfferSpecification> offerSpecifications =
			castorApi.getAtomicOfferSpecificationsByLegacyCode( key );

		// On crée une installedAtomicOffer si on a rien trouvé dans le catalogue
		// Elle affiche les codes Legacy au lieu des codes CASTOR
		if ( CollectionUtils.isEmpty( offerSpecifications ) )
		{
			offerSpecifications = new ArrayList<AtomicOfferSpecification>();

			String offerSpecificationCode =
				CatalogueConstantes.BSCS_SPCODE_PREFIX + spCode + ":" + CatalogueConstantes.BSCS_SNCODE_PREFIX + snCode;
			InstalledAtomicOffer atomicOffer =
				generateInstalledAtomicOffer( CatalogueConstantes.DEFAULT_OFFER_ID, offerSpecificationCode, null );

			offerSpecifications.add( (AtomicOfferSpecification) atomicOffer.getOfferSpecification() );
		}

		for ( OfferSpecification offerSpecification : offerSpecifications )
		{

			InstalledAtomicOffer convertedInstalledOffer = new InstalledAtomicOffer();

			convertedInstalledOffer.setOfferSpecification( offerSpecification );
			convertedInstalledOffer.setInstalledOfferID( offerSpecification.getOfferSpecificationCode() + ":"
				+ CatalogueConstantes.DEFAULT_OFFER_ID );

			result.add( convertedInstalledOffer );
		}
		return result;
	}

	/**
	 * Génère un InstalledContract
	 * 
	 * @param installedOfferID
	 * @param contractSpecificationCode
	 * @param msisdn
	 * @return un objet InstalledContract
	 */
	private InstalledContract generateContract( String installedOfferID, String contractSpecificationCode,
												String msisdn )
	{
		// le contrat
		InstalledContract contract = new InstalledContract();
		// identifiant du contrat
		contract.setInstalledOfferID( installedOfferID );

		XMLGregorianCalendar calendar = null;
		try
		{
			calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar( new GregorianCalendar( 2008, 10, 1 ) );
		}
		catch ( DatatypeConfigurationException e )
		{
			throw new RuntimeException( e.getMessage(), e );
		}
		// date de création du contrat
		contract.setCreationDate( calendar );
		// date de dernière modification du contrat
		contract.setModificationDate( calendar );

		// Clé "publique" identifiant le contrat (MISDN).
		InstalledPublicKey publicKey = new InstalledPublicKey();
		publicKey.setInstalledPublicKeyID( msisdn );
		// TODO gestion des nouvelle list d'installedPublicKey
		contract.getInstalledPublicKey().add( publicKey );

		// statut du contrat
		InstalledOfferStatus contractStatus = new InstalledOfferStatus();
		// INORDER / INITIALIZED / SUSPENDED / REMOVED
		contractStatus.setStatusCode( StatusCode.INORDER );// 03
		// Raison associée au dernier changement de statut
		// ROBBED / NONPAYMENT / OTHER
		contractStatus.setStatusReason( StatusReason.OTHER );
		// Date de prise en compte du dernier changement de statut
		contractStatus.setStartDate( calendar );

		// Date de fin de validité de l'offre
		// Pour les offres Card, donne la fin de validité de la Card

		contract.getInstalledOfferStatus().add( contractStatus );

		// Spécification catalogue du contrat, i.e. le plan.
		ContractSpecification contractSpec = new ContractSpecification();
		contractSpec.setOfferSpecificationCode( contractSpecificationCode ); // MOBILE:PLAN:174
		contract.setOfferSpecification( contractSpec );
		return contract;
	}

	/**
	 * Génère une InstalledAtomicOffer
	 * 
	 * @param installedOfferID
	 * @param offerSpecificationCode
	 * @param label
	 * @return un objet InstalledAtomicOffer
	 */
	private InstalledAtomicOffer generateInstalledAtomicOffer(	String installedOfferID, String offerSpecificationCode,
																String label )
	{
		InstalledAtomicOffer offer = new InstalledAtomicOffer();
		offer.setInstalledOfferID( installedOfferID );

		XMLGregorianCalendar calendar = null;

		try
		{
			calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar( new GregorianCalendar() );
		}
		catch ( DatatypeConfigurationException e )
		{
			throw new RuntimeException( e );
		}
		offer.setCreationDate( calendar );
		offer.setModificationDate( calendar );

		// statut
		InstalledOfferStatus offerStatus = new InstalledOfferStatus();
		offerStatus.setStatusCode( StatusCode.INORDER );// 03
		offerStatus.setStatusReason( StatusReason.OTHER );
		offerStatus.setStartDate( calendar );
		offer.getInstalledOfferStatus().add( offerStatus );

		AtomicOfferSpecification offerSpec = new AtomicOfferSpecification();

		offerSpec.setOfferSpecificationCode( offerSpecificationCode );
		offerSpec.setOfferSpecificationLabel( label );

		offer.setOfferSpecification( offerSpec );
		offerSpec.setGenericProduct( generateGenericProduct( CatalogueConstantes.GENERIC_PRODUCT ) );
		offerSpec.getCommercialClassifications().add( generateCommercialClassification( CatalogueConstantes.COMMERCIAL_CLASSIFICATION ) );
		offerSpec.getCommercialClassifications().add( generateCommercialClassification( CatalogueConstantes.COMMERCIAL_CLASSIFICATION ) );

		offer.setOfferSpecification( offerSpec );
		return offer;

	}

	/**
	 * Génère un GenericProduct pour une offerSpecification
	 * 
	 * @param code
	 * @return un objet GenericProduct
	 */
	private GenericProduct generateGenericProduct( String code )
	{
		GenericProduct gp = new GenericProduct();
		gp.setGenericProductCode( code );

		return gp;
	}

	/**
	 * Methode renvoyant la liste des classification pour un TmCode ou un SnCode ou un SpCode
	 * 
	 * @param tmCode le Tm Code
	 * @param spCode le Sn Code
	 * @param snCode le Sp Code
	 * @return
	 */
	public List<CommercialClassification> getClassification( String tmCode, String spCode, String snCode )
	{

		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "CatalogueHelper getClasification() - Begin | tmCode=" + tmCode + " | snCode = " + snCode
				+ " | sPcode = " + spCode );
		}

		String key = CatalogueConstantes.BSCS_TMCODE_PREFIX + tmCode;

		return castorApi.getClassificationsByLegacyCode( key );
	}

	/**
	 * Génère des classifications pour une offerSpecification
	 * 
	 * @param label
	 * @return un objet CommercialClassification
	 */
	private CommercialClassification generateCommercialClassification( String label )
	{
		CommercialClassification cc = new CommercialClassification();
		cc.setLabel( label );

		return cc;
	}

	public String getOfferSpecificationCode( String coId, String msisdn, String tmCode, String montant )
	{

		String key = CatalogueConstantes.BSCS_BUNDLE;
		List<AtomicOfferSpecification> offerSpecifications = null;
		try
		{
			offerSpecifications = castorApi.getAtomicOfferSpecificationsByLegacyCode( key );
		}
		catch ( Exception e )
		{
			LOGGER.error( "Exception occurred while searching for offers matching following legacy key:" + key, e );
		}

		// On crée une installedAtomicOffer si on a rient trouvé dans le catalogue
		// Elle affiche les codes Legacy au lieu des codes CASTOR
		if ( offerSpecifications == null || offerSpecifications.isEmpty() )
		{
			return offerSpecifications.get( 0 ).getOfferSpecificationCode();
		}

		return key;
	}

	public List<CommercialClassification> getCommercialClassification( List<InstalledOffer> installedOffers )
	{
		List<CommercialClassification> commercialClassifications = new ArrayList<CommercialClassification>();
		if ( installedOffers.size() == 1 )
		{
			commercialClassifications = installedOffers.get( 0 ).getOfferSpecification().getCommercialClassifications();
		}
		else
		{
			for ( InstalledOffer installedOffer : installedOffers )
			{
				List<CommercialClassification> commercialClassifTemp = new ArrayList<CommercialClassification>();
				commercialClassifTemp = installedOffer.getOfferSpecification().getCommercialClassifications();
				commercialClassifications.addAll( commercialClassifTemp );

			}
		}
		return commercialClassifications;
	}

	public Collection<FunctionSpecification> getClassificationFonction( List<InstalledOffer> installedOffers )
	{
		List<String> codes = new ArrayList<String>();
		for ( InstalledOffer installedOffer : installedOffers )
		{
			List<ProductSpecification> productSpecifications =
				( (AtomicOfferSpecification) installedOffer.getOfferSpecification() ).getProductSpecifications();

			codes.addAll( productSpecifications.stream().map( p -> p.getProductSpecificationCode() ).collect( Collectors.toList() ) );
		}

		return castorApi.getFunctionSpecifications( codes );
	}

	public FunctionValueSpecification getFunctionValueSpecification( String code )
	{
		return castorApi.getFunctionValueSpecification( code );
	}
}
