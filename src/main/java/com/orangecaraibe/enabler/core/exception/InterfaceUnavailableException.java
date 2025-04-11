/**
 *
 */
package com.orangecaraibe.enabler.core.exception;

/**
 * @author sgodard
 */
public class InterfaceUnavailableException
	extends Exception
{
	/**
	 * *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	private InterfaceUnavailableException()
	{
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	private InterfaceUnavailableException( String arg0, Throwable arg1 )
	{
		super( arg0, arg1 );
	}

	/**
	 * @param arg0
	 */
	private InterfaceUnavailableException( String arg0 )
	{
		super( arg0 );
	}

	/**
	 * @param arg0
	 */
	private InterfaceUnavailableException( Throwable arg0 )
	{
		super( arg0 );
	}

}
