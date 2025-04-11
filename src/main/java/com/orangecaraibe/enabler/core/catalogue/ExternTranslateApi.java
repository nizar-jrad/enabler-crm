package com.orangecaraibe.enabler.core.catalogue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orangecaraibe.api.catalogue.ws.translate.api.TranslateApi;
import com.orangecaraibe.api.catalogue.ws.translate.model.InstalledContractResponse;
import com.orangecaraibe.enabler.core.catalogue.adapter.TranslateApiAdapter;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledContract;

@Component
public class ExternTranslateApi
	extends TranslateApi
{

	private static final Logger LOGGER = LoggerFactory.getLogger( ExternTranslateApi.class );

	@Autowired
	private TranslateApi translateApi;

	@Autowired
	private TranslateApiAdapter translateApiAdapter;

	/**
	 * Retourne un installedContract de type Castor via l'API Catalogue WS.
	 * 
	 * @param installedContract de type Legacy à convertir
	 * @return installedContract de type Castor
	 */
	public InstalledContract convertLegacyContractToCastor( InstalledContract installedContract )
	{
		LOGGER.debug(	"appel de l'api catalogue-ws pour conversion du contrat {0}",
						installedContract != null ? installedContract.getInstalledOfferID() : null );
		List<com.orangecaraibe.api.catalogue.ws.translate.model.InstalledContract> installedContracts =
			new ArrayList<com.orangecaraibe.api.catalogue.ws.translate.model.InstalledContract>();
		installedContracts.add( translateApiAdapter.adaptToInstalledContract( installedContract ) );
		InstalledContractResponse response = translateApi.translateTypePost1( "castor", installedContracts ).block();
		if ( response != null && CollectionUtils.isNotEmpty( response.getInstalledContract() ) )
		{
			return translateApiAdapter.adaptToSoaInstalledContract( response.getInstalledContract().get( 0 ) );
		}
		return null;
	}

}