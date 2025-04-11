package com.orangecaraibe.enabler.crm.business.exception;

import com.orangecaraibe.enabler.utils.exception.functional.FunctionalException;

/**
 * Classe pour la gestion des exceptions sur les findAndget demande
 * 
 * @author NCR
 */
public class CustomerProblemFindAndGetException
	extends FunctionalException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7104699430447009736L;

	/**
	 * Instantiates a new customer problem find and get exception.
	 * 
	 * @param message the message
	 * @param id the id
	 */
	public CustomerProblemFindAndGetException( String message, String id )
	{
		super( message, new String[] { id } );
	}

}