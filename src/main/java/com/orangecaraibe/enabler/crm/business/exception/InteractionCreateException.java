package com.orangecaraibe.enabler.crm.business.exception;

import com.orangecaraibe.enabler.utils.exception.functional.FunctionalException;

/**
 * The Class InteractionCreateException.
 */
public class InteractionCreateException
	extends FunctionalException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7003005245913667262L;

	/**
	 * Instantiates a new interaction create exception.
	 * 
	 * @param message the message
	 * @param id the id
	 */
	public InteractionCreateException( String message, String id )
	{
		super( message, new String[] { id } );
	}
}
