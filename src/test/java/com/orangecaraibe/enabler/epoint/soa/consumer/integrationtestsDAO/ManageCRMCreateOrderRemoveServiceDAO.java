/**
 *
 */
package com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO;

import java.util.List;

import com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO.api.RemoveService;

/**
 * DAO de recherche de client pour la suppresion de service
 * 
 * @author sgodard
 */
@Deprecated
public interface ManageCRMCreateOrderRemoveServiceDAO
{
	public List<RemoveService> getRemoveServicePref();

	public List<RemoveService> getRemoveServiceXtrem();

	public List<RemoveService> getRemoveServiceFlotte();

	public List<RemoveService> getRemoveServicePro();
}
