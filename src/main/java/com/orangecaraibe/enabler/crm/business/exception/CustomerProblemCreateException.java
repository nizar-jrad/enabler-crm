package com.orangecaraibe.enabler.crm.business.exception;

import com.orangecaraibe.enabler.utils.exception.functional.FunctionalException;

/**
 * The Class CustomerProblemCreateException.
 */
public class CustomerProblemCreateException
	extends FunctionalException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -515698395045438594L;

	/**
	 * Instantiates a new customer problem create exception.
	 * 
	 * @param message the message
	 * @param id the id
	 */
	public CustomerProblemCreateException( String message, String id )
	{
		super( message, new String[] { id } );
	}

}
