/**
 *
 */
package com.orangecaraibe.enabler.svi.core.exception;

/**
 * @author sgodard
 */
public class UnknownMsisdnException
	extends RuntimeException
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public UnknownMsisdnException()
	{
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UnknownMsisdnException( String arg0, Throwable arg1 )
	{
		super( arg0, arg1 );
	}

	/**
	 * @param arg0
	 */
	public UnknownMsisdnException( String arg0 )
	{
		super( arg0 );
	}

	/**
	 * @param arg0
	 */
	public UnknownMsisdnException( Throwable arg0 )
	{
		super( arg0 );
	}

}
