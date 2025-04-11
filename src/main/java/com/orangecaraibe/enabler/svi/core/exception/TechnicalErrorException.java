/**
 *
 */
package com.orangecaraibe.enabler.svi.core.exception;

/**
 * @author sgodard
 */
public class TechnicalErrorException
	extends RuntimeException
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public TechnicalErrorException()
	{
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TechnicalErrorException( String arg0, Throwable arg1 )
	{
		super( arg0, arg1 );
	}

	/**
	 * @param arg0
	 */
	public TechnicalErrorException( String arg0 )
	{
		super( arg0 );
	}

	/**
	 * @param arg0
	 */
	public TechnicalErrorException( Throwable arg0 )
	{
		super( arg0 );
	}

}
