/**
 *
 */
package com.orangecaraibe.enabler.epoint.business.service;

import com.orangecaraibe.enabler.epoint.webservice.impl.exception.AddOrRemoveAccordInfoCommercialeException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CheckOutgoingRIOException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CreateOrderAddCatalogueServiceException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CreateOrderCreditINException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.CreateOrderRemoveServiceException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.FindAndGetAccordInfoCommercialeException;
import com.orangecaraibe.enabler.epoint.webservice.impl.exception.GetPresenceCompteWebcareException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.GetOutgoingRIOException;
import com.orangecaraibe.soa.v2.interfaces.manageportability.InterfaceUnavailableException;

/**
 * @author sgodard Interface pour le service métier de ManageCRM
 */
@Deprecated
public interface CRMService
{
	/**
	 * Vérifier l'éligibilité pour un portage sortant
	 * 
	 * @param msisdn
	 * @param rio
	 * @param coId
	 * @param typeClient
	 * @return
	 * @throws InterfaceUnavailableException
	 * @throws CheckOutgoingRIOException
	 */
	public boolean checkOutgoingRIO( String msisdn, String rio, String coId, String typeClient )
		throws InterfaceUnavailableException, CheckOutgoingRIOException;

	/**
	 * Méthode permettant de calculer le RIO pour un portage sortant
	 * 
	 * @param msisdn
	 * @param coId
	 * @param typeClient
	 * @return
	 * @throws InterfaceUnavailableException
	 * @throws GetOutgoingRIOException
	 */
	public String getOutgoingRIO( String msisdn, String coId, String typeClient )
		throws InterfaceUnavailableException, GetOutgoingRIOException;

	/**
	 * Supprimer un service sur un contrat donné à partir de ses identifiants LEGACY. La suppression se traduit par la
	 * création et l'envoi d'un order de suppression d'option On fait un appel au WS ManageCustomerOrder pour créer
	 * l'order
	 * 
	 * @param coId
	 * @param msisdn
	 * @param tmCode
	 * @param snCode
	 * @param spCode
	 * @param customerId
	 * @throws CreateOrderRemoveServiceException
	 * @throws com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException
	 */
	public void createOrderRemoveService(	String coId, String msisdn, String tmCode, String snCode, String spCode,
											String customerId )
		throws CreateOrderRemoveServiceException,
		com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException;

	/**
	 * Ajouter un service sur un contrat donné à partir de son code CASTOR. L'ajout se traduit par la création et
	 * l'envoi d'un order de création d'option On fait un appel au WS ManageCustomerOrder pour créer l'order
	 * 
	 * @param coId
	 * @param msisdn
	 * @param tmCode
	 * @param offerSpecificationCode
	 * @param customerId
	 * @throws CreateOrderAddCatalogueServiceException
	 * @throws com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException
	 */
	public void createOrderAddCatalogueService( String coId, String msisdn, String tmCode,
												String offerSpecificationCode, String customerId )
		throws CreateOrderAddCatalogueServiceException,
		com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException;

	/**
	 * Effectuer un geste commercial envers le client en lui offrant une recharge désignée par son code CASTOR. L'ajout
	 * se traduit par la création et l'envoi d'un order d'achat à l'acte. On fait un appel au WS ManageCustomerOrder
	 * pour créer l'order
	 * 
	 * @param coId
	 * @param msisdn
	 * @param tmCode
	 * @param offerSpecificationCode
	 * @param customerId
	 * @throws CreateOrderCreditINException
	 * @throws com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException
	 */
	public void creditIN( String coId, String msisdn, String tmCode, String offerSpecificationCode, String customerId )
		throws CreateOrderCreditINException,
		com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException;

	/**
	 * @param coId
	 * @param msisdn
	 * @param tmCode
	 * @param offerSpecificationCode
	 * @param customerId
	 * @throws CreateOrderCreditINException
	 * @throws com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException
	 */
	public String getOfferCodeSpecficationCode( String coId, String msisdn, String tmCode,
												String offerSpecificationCode, String customerId )
		throws CreateOrderCreditINException,
		com.orangecaraibe.soa.v2.interfaces.managecustomerordercapture.InterfaceUnavailableException;

	/**
	 * Vérifier la présence d'un compte webcare
	 * 
	 * @param coId
	 * @param msisdn
	 * @return
	 * @throws GetPresenceCompteWebcareException
	 */
	public boolean getPresenceCompteWebcare( String coId, String msisdn )
		throws GetPresenceCompteWebcareException;

	/**
	 * Rechercher l'accord sur l'envoi d'informations commerciales
	 * 
	 * @param coId
	 * @param customerId
	 * @return
	 * @throws FindAndGetAccordInfoCommercialeException
	 */
	public boolean findAndGetAccordInfoCommerciale( String coId, String customerId )
		throws FindAndGetAccordInfoCommercialeException;

	/**
	 * Mettre à jour l'accord sur l'envoi d'informations commerciales
	 * 
	 * @param accord
	 * @param coId
	 * @param customerId
	 * @return
	 * @throws AddOrRemoveAccordInfoCommercialeException
	 */
	public boolean addOrRemoveAccordInfoCommerciale( boolean accord, String coId, String customerId, String channel )
		throws AddOrRemoveAccordInfoCommercialeException;

	/**
	 * Récupérer l'opérateur téléphonique ( ARM/ORM) en fonction du tmCode on renvoie la liste des TmCode et de la
	 * classification associer
	 * 
	 * @throws Exception
	 * @throws GetRateplanClassificationException
	 */
	public String getClasification( String tmCode, String snCode, String spCode, String libeller )
		throws InterfaceUnavailableException, Exception;

	/**
	 * Récupérer l'opérateur téléphonique ( ARM/ORM) en fonction du tmCode on renvoie la liste des TmCode et de la
	 * classification associer
	 * 
	 * @throws Exception
	 * @throws GetRateplanClassificationException
	 */
	public String getClasifications( String tmCode, String snCode, String spCode )
		throws InterfaceUnavailableException, Exception;
}
