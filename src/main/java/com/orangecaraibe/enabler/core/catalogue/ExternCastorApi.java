package com.orangecaraibe.enabler.core.catalogue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orangecaraibe.api.catalogue.ws.api.CastorApi;
import com.orangecaraibe.api.catalogue.ws.api.ExternalIdentifiersApi;
import com.orangecaraibe.api.catalogue.ws.model.ClassificationResponse;
import com.orangecaraibe.api.catalogue.ws.model.ExternalIdentifier;
import com.orangecaraibe.api.catalogue.ws.model.FunctionSpecificationResponse;
import com.orangecaraibe.api.catalogue.ws.model.OffersReponse;
import com.orangecaraibe.enabler.core.catalogue.adapter.CastorApiAdapter;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.AtomicOfferSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.CommercialClassification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionValueSpecification;

import reactor.core.publisher.Flux;

@Component
public class ExternCastorApi
	extends CastorApi
{

	private static final Logger LOGGER = LoggerFactory.getLogger( ExternCastorApi.class );

	@Autowired
	private CastorApi castorApi;

	@Autowired
	private ExternalIdentifiersApi externalIdentifiersApi;

	@Autowired
	private CastorApiAdapter castorApiAdapter;

	/**
	 * Retourne une AtomicOfferSpecification via l'API Catalogue WS correspondant au code catalogue passé en paramètre.
	 * 
	 * @param catalogueCode
	 * @return AtomicOfferSpecification
	 */
	public AtomicOfferSpecification getAtomicOfferSpecification( String catalogueCode )
	{
		LOGGER.debug( "appel de l'api catalogue-ws pour récupération de l'offre {0}", catalogueCode );
		OffersReponse response = castorApi.castorOfferOfferCodeGet( catalogueCode ).block();
		if ( response != null )
		{
			return castorApiAdapter.adaptToSoaAtomicOfferSpecification( response.getAtomicOfferSpecification() );
		}
		return null;
	}

	/**
	 * Retourne une liste d'ExternalIdentifier via l'API Catalogue WS correspondant au code offre passé en paramètre.
	 * 
	 * @param offerCode (code legacy ou code Catalogue)
	 * @return List<ExternalIdentifier>
	 */
	public List<ExternalIdentifier> getOfferCodes( String offerCode )
	{
		LOGGER.debug(	"appel de l'api catalogue-ws pour récupération des codes legacy ou catalogue à partir du code {0}",
						offerCode );
		Flux<ExternalIdentifier> response = externalIdentifiersApi.externalIdentifiersOfferCodeGet( offerCode );
		return response.collectList().block();
	}

	/**
	 * Retourne une liste d'AtomicOfferSpecification via l'API Catalogue WS correspondant au code legacy passé en
	 * paramètre.
	 * 
	 * @param legacyCode
	 * @return List<AtomicOfferSpecification>
	 */
	public List<AtomicOfferSpecification> getAtomicOfferSpecificationsByLegacyCode( String legacyCode )
	{
		LOGGER.debug( "appel de l'api catalogue-ws pour récupération de(s) l'offre(s) {0}", legacyCode );
		List<AtomicOfferSpecification> offers = new ArrayList<AtomicOfferSpecification>();
		List<ExternalIdentifier> codes = getOfferCodes( legacyCode );
		if ( CollectionUtils.isNotEmpty( codes ) )
		{
			for ( ExternalIdentifier ei : codes )
			{
				if ( ei != null && "CASTOR".equalsIgnoreCase( ei.getReferential() ) )
				{
					AtomicOfferSpecification offer = getAtomicOfferSpecification( ei.getIdentifier() );
					offers.add( offer );
				}
			}
		}

		return offers;
	}

	/**
	 * Retourne une liste de CommercialClassification via l'API Catalogue WS correspondant au code legacy passé en
	 * paramètre.
	 * 
	 * @param legacyCode
	 * @return List<CommercialClassification>
	 */
	public List<CommercialClassification> getClassificationsByLegacyCode( String legacyCode )
	{
		LOGGER.debug( "appel de l'api catalogue-ws pour récupération de(s) classification(s) {0}", legacyCode );
		List<CommercialClassification> classifications = new ArrayList<CommercialClassification>();
		List<ExternalIdentifier> codes = getOfferCodes( legacyCode );
		if ( CollectionUtils.isNotEmpty( codes ) )
		{
			for ( ExternalIdentifier ei : codes )
			{
				if ( ei != null && "CASTOR".equalsIgnoreCase( ei.getReferential() ) )
				{
					List<CommercialClassification> commercialClassifications = getClassifications( ei.getIdentifier() );
					classifications.addAll( commercialClassifications );
				}
			}
		}

		return classifications;
	}

	/**
	 * Retourne une liste de CommercialClassification via l'API Catalogue WS correspondant au code catalogue passé en
	 * paramètre.
	 * 
	 * @param catalogueCode
	 * @return List<CommercialClassification>
	 */
	public List<CommercialClassification> getClassifications( String catalogueCode )
	{
		LOGGER.debug( "appel de l'api catalogue-ws pour récupération de(s) classification(s) {0}", catalogueCode );
		ClassificationResponse response = castorApi.castorClassificationOfferCodeGet( catalogueCode ).block();
		if ( response != null )
		{
			List<CommercialClassification> classifications = new ArrayList<CommercialClassification>();
			castorApiAdapter.adaptToSoaCommercialClassifications( classifications, response.getClassification() );
			return classifications;
		}
		return null;
	}

	/**
	 * Retourne une liste de FunctionSpecification via l'API Catalogue WS correspondant aux codes catalogue passés en
	 * paramètre.
	 * 
	 * @param catalogueCodes
	 * @return List<FunctionSpecification>
	 */
	public List<FunctionSpecification> getFunctionSpecifications( List<String> catalogueCodes )
	{
		if ( CollectionUtils.isNotEmpty( catalogueCodes ) )
		{
			LOGGER.debug(	"appel de l'api catalogue-ws pour récupération de(s) fonction(s) {0}",
							catalogueCodes.stream().collect( Collectors.joining( ", " ) ) );
			List<FunctionSpecificationResponse> response =
				castorApi.castorFunctionGet( catalogueCodes ).collectList().block();
			if ( response != null )
			{
				List<FunctionSpecification> soaFunctionSpecifications = new ArrayList<FunctionSpecification>();
				List<com.orangecaraibe.api.catalogue.ws.model.FunctionSpecification> functionSpecifications =
					response.stream().map( r -> r.getFunction() ).collect( Collectors.toList() );
				castorApiAdapter.adaptToSoaFunctionSpecification( soaFunctionSpecifications, functionSpecifications );
				return soaFunctionSpecifications;
			}
		}

		return null;
	}

	/**
	 * TODO : Comment récupérer une valeur de fonction via l'API Catalogue WS ?<br />
	 * <br />
	 * Retourne une FunctionValueSpecification via l'API Catalogue WS correspondant au code catalogue passé en
	 * paramètre. Comment ?
	 * 
	 * @param catalogueCodes
	 * @return List<FunctionSpecification>
	 */
	public FunctionValueSpecification getFunctionValueSpecification( String catalogueCode )
	{
		LOGGER.debug( "appel de l'api catalogue-ws pour récupération de la valeur de fonction {0}", catalogueCode );
		com.orangecaraibe.api.catalogue.ws.model.FunctionSpecification functionSpecification =
			castorApi.castorFunctionFunctionCodeGet( catalogueCode ).block();
		return castorApiAdapter.adaptToSoaFunctionValueSpecification( functionSpecification );
	}

}