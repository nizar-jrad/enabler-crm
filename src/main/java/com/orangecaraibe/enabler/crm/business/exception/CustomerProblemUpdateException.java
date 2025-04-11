package com.orangecaraibe.enabler.crm.business.exception;

import com.orangecaraibe.enabler.utils.exception.functional.FunctionalException;

/**
 * The Class CustomerProblemUpdateException.
 */
public class CustomerProblemUpdateException
	extends FunctionalException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6634592926696865590L;

	/**
	 * Instantiates a new customer problem update exception.
	 * 
	 * @param message the message
	 * @param id the id
	 */
	public CustomerProblemUpdateException( String message, String id )
	{
		super( message, new String[] { id } );
	}
}
