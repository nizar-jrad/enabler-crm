/**
 *
 */
package com.orangecaraibe.enabler.epoint.webservice.impl.exception;

/**
 * @author sgodard
 */
@Deprecated
public class CreateOrderRemoveServiceException
	extends Exception
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public CreateOrderRemoveServiceException()
	{
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CreateOrderRemoveServiceException( String arg0, Throwable arg1 )
	{
		super( arg0, arg1 );
	}

	/**
	 * @param arg0
	 */
	public CreateOrderRemoveServiceException( String arg0 )
	{
		super( arg0 );
	}

	/**
	 * @param arg0
	 */
	public CreateOrderRemoveServiceException( Throwable arg0 )
	{
		super( arg0 );
	}

}
