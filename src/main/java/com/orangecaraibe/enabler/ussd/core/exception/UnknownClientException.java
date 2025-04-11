/**
 *
 */
package com.orangecaraibe.enabler.ussd.core.exception;

/**
 * @author sgodard
 */
public class UnknownClientException
	extends RuntimeException
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public UnknownClientException()
	{
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UnknownClientException( String arg0, Throwable arg1 )
	{
		super( arg0, arg1 );
	}

	/**
	 * @param arg0
	 */
	public UnknownClientException( String arg0 )
	{
		super( arg0 );
	}

	/**
	 * @param arg0
	 */
	public UnknownClientException( Throwable arg0 )
	{
		super( arg0 );
	}

}
