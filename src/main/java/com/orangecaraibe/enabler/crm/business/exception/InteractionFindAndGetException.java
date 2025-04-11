package com.orangecaraibe.enabler.crm.business.exception;

import com.orangecaraibe.enabler.utils.exception.functional.FunctionalException;

/**
 * The Class InteractionFindAndGetException.
 */
public class InteractionFindAndGetException
	extends FunctionalException
{

	private static final long serialVersionUID = 4086823450377568660L;

	/**
	 * Instantiates a new interaction find and get exception.
	 * 
	 * @param message the message
	 * @param id the id
	 */
	public InteractionFindAndGetException( String message, String id )
	{
		super( message, new String[] { id } );
	}

}
