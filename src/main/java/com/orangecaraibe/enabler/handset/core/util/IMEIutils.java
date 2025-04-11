package com.orangecaraibe.enabler.handset.core.util;

public class IMEIutils
{
	public boolean isLuhnValid( String imei )
	{
		// http://www.tech-faq.com/the-luhn-check-digit-algorithm-in-java.html
		String digitsOnly = getDigitsOnly( imei );
		int sum = 0;
		int digit = 0;
		int addend = 0;
		boolean timesTwo = false;

		for ( int i = digitsOnly.length() - 1; i >= 0; i-- )
		{
			digit = Integer.parseInt( digitsOnly.substring( i, i + 1 ) );
			if ( timesTwo )
			{
				addend = digit * 2;
				if ( addend > 9 )
				{
					addend -= 9;
				}
			}
			else
			{
				addend = digit;
			}
			sum += addend;
			timesTwo = !timesTwo;
		}

		int modulus = sum % 10;
		return modulus == 0;

	}

	public String completeIMEI( String imei )
	{
		// exclusion des numéros imei inattendus
		if ( imei == null || imei.length() < 14 )
			return imei;

		imei = imei.substring( 0, 14 ) + "0";
		String digitsOnly = getDigitsOnly( imei );
		int sum = 0;
		int digit = 0;
		int addend = 0;
		boolean timesTwo = false;

		for ( int i = digitsOnly.length() - 1; i >= 0; i-- )
		{
			digit = Integer.parseInt( digitsOnly.substring( i, i + 1 ) );
			if ( timesTwo )
			{
				addend = digit * 2;
				if ( addend > 9 )
				{
					addend -= 9;
				}
			}
			else
			{
				addend = digit;
			}
			sum += addend;
			timesTwo = !timesTwo;
		}

		int modulus = sum % 10;
		if ( modulus == 0 )
			return imei;
		else
			return imei.substring( 0, 14 ) + String.valueOf( 10 - modulus );

	}

	private static String getDigitsOnly( String s )
	{
		StringBuffer digitsOnly = new StringBuffer();
		char c;
		for ( int i = 0; i < s.length(); i++ )
		{
			c = s.charAt( i );
			if ( Character.isDigit( c ) )
			{
				digitsOnly.append( c );
			}
		}
		return digitsOnly.toString();
	}

	public static void main( String args[] )
	{
		IMEIutils im = new IMEIutils();

		String imei = "052709046097481";
		System.out.println( imei + "  :  " + im.isLuhnValid( imei ) );
		System.out.println( "iteratif : " + im.completeIMEI( imei ) + "\n\n" );

		imei = "359593027492082";
		System.out.println( imei + "  :  " + im.isLuhnValid( imei ) );
		System.out.println( "iteratif : " + im.completeIMEI( imei ) + "\n\n" );

		imei = "052395024294713";
		System.out.println( imei + "  :  " + im.isLuhnValid( imei ) );
		System.out.println( "iteratif : " + im.completeIMEI( imei ) + "\n\n" );

	}
}
