package com.orangecaraibe.enabler.crm.business.service;

import java.util.List;

import com.orangecaraibe.enabler.crm.business.exception.InteractionCreateException;
import com.orangecaraibe.enabler.crm.business.exception.InteractionFindAndGetException;
import com.orangecaraibe.enabler.crm.webservice.criteria.InteractionCriteria;

public interface InteractionService
{

	/**
	 * createInteraction methode pour la creation d'interaction
	 * 
	 * @param interaction l'interaction
	 * @return
	 */
	public com.orangecaraibe.enabler.crm.business.bean.Interaction createInteraction( com.orangecaraibe.enabler.crm.business.bean.Interaction interaction )
		throws InteractionCreateException;

	/**
	 * Methode pour la recherche d'interaction en fonction des criteres
	 * 
	 * @param interactionCriteria
	 * @return
	 * @throws InteractionFindAndGetException
	 */
	public List<com.orangecaraibe.enabler.crm.business.bean.Interaction> findAndGetInteraction( InteractionCriteria interactionCriteria )
		throws InteractionFindAndGetException;

	// methode d'update non implemente car pas de use case de mise a jour identifie
	// public Interaction updateInteraction( Interaction interaction )

}
