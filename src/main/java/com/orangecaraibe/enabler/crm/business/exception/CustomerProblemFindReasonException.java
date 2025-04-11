/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.exception;

import com.orangecaraibe.enabler.utils.exception.functional.FunctionalException;

/**
 * @author ncrtest2
 */
public class CustomerProblemFindReasonException
	extends FunctionalException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new customer problem find and get exception.
	 * 
	 * @param message the message
	 * @param id the id
	 */
	public CustomerProblemFindReasonException( String message, String id )
	{
		super( message, new String[] { id } );
	}
}
