/**
 * 
 */
package com.orangecaraibe.enabler.crm.soa.provider;

import javax.jws.WebMethod;

/**
 * ORANGE-1381 - QC-913 - MBean de suivi des thread alloués à la méthode ExecuteProcessStep
 * 
 * @author sverdun
 */
public interface ThreadMonitoringServiceMBean
{

	/**
	 * Récupération de la méthode exécutée
	 * 
	 * @return methode exécutée
	 */
	public String getProcessMethod();

	/**
	 * Modification du processMethod
	 * 
	 * @param processMethod
	 */
	public void setProcessMethod( String processMethod );

	/**
	 * Récupération du applicationId
	 * 
	 * @return applicationId configuré
	 */
	public String getApplicationId();

	/**
	 * Modification du applicationId
	 */
	public void setApplicationId( String applicationId );

	/**
	 * Récupération du nombre de tâche accomplies
	 * 
	 * @return nombre de tâche réalisées
	 */
	@WebMethod
	public long getCompletedTaskCount();

	/**
	 * Récupération du max de thread atteind
	 * 
	 * @return valeur max du nombre de thread
	 */
	public int getLargestPoolSize();

	/**
	 * Récupération de la taille maximal du pool de thread
	 * 
	 * @return taille max du pool
	 */
	public int getMaximumPoolSize();

	/**
	 * Configuration de la taille maximale du pool de thread
	 * 
	 * @param maximumPoolSize
	 */
	public void setMaximumPoolSize( int maximumPoolSize );

	/**
	 * Récupération du nombre de tâches en attente
	 * 
	 * @return nombre de tâche en attente
	 */
	public long getTaskCount();

	/**
	 * Récupération de la taille de la liste des coeur
	 * 
	 * @return nombre de coeurs
	 */
	public int getCorePoolSize();

	/**
	 * Configure la taille de la liste des coeurs
	 * 
	 * @param corePoolSize
	 */
	public void setCorePoolSize( int corePoolSize );
}
