/**
 *
 */
package com.orangecaraibe.enabler.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author sgodard
 */
public class DateUtils
{
	/**
	 * Renvoie une date sous forme de chaîne sous la forme dd/MM/yyyy (jj/mm/aaaa)
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateFormat( Date date )
	{
		DateFormat dateFormat = new SimpleDateFormat( Constantes.JJ_MM_AAAA );
		String chaineDate = dateFormat.format( date );
		return chaineDate;
	}

	/**
	 * Renvoie une date sous forme de chaîne sous la forme YYYYMMDD
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateFormatYYYYMMDD( Date date )
	{
		DateFormat dateFormat = new SimpleDateFormat( Constantes.DATE_FORMAT_YYYYMMDDHHMM );
		String chaineDate = dateFormat.format( date );
		return chaineDate;
	}

	public static String getDateFormatDDMMYYYY( Date date )
	{
		DateFormat dateFormat = new SimpleDateFormat( Constantes.DATE_FORMAT_DDMMYYYY );
		String chaineDate = dateFormat.format( date );
		return chaineDate;
	}

	/**
	 * Renvoie une date qui se correspond à date en paramètre moins nombre de jours en paramètre
	 * 
	 * @param currentDate
	 * @param nbDay
	 * @return
	 */
	public static Date getXDayBeforeDate( Date currentDate, int nbDay )
	{

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime( currentDate );
		// Initialisé avec une instance de Date.
		calendar.add( Calendar.DATE, -nbDay );
		return calendar.getTime();
	}

	/**
	 * Transforme un type xmlGregorianCalendar en type Date
	 * 
	 * @param xmlDate xmlGregorianCalendar
	 * @return
	 */
	public static Date getDateFromXmlGregorianCalendar( XMLGregorianCalendar xmlDate )
	{
		return xmlDate.toGregorianCalendar().getTime();
	}

	/**
	 * Renvoie une date sous forme de chaîne sous la forme dd/MM/yyyy (jj/mm/aaaa)
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateFromString( String date )
		throws ParseException
	{
		DateFormat dateFormat = new SimpleDateFormat( Constantes.JJ_MM_AAAA );
		Date dateResult = dateFormat.parse( date );
		return dateResult;
	}

	/**
	 * Renvoie une date sous forme de chaîne sous la forme format
	 * 
	 * @param date en chaine
	 * @param format le format de la date en chaine
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateFromString( String date, String format )
		throws ParseException
	{
		DateFormat dateFormat = new SimpleDateFormat( format );
		Date dateResult = dateFormat.parse( date );
		return dateResult;
	}

	/**
	 * Convertit un objet Date en objet XMLGregorianCalendar
	 * 
	 * @param dateToConvert the date to convert
	 * @return the xML gregorian calendar
	 */
	public static XMLGregorianCalendar convertDateToXMLGregorianCalendar( Date dateToConvert )
	{
		if ( dateToConvert == null )
		{
			return null;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime( dateToConvert );
		XMLGregorianCalendar convertedXMLGregorianCalendar = null;
		try
		{
			convertedXMLGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar );
		}
		catch ( DatatypeConfigurationException e )
		{
			throw new RuntimeException( e );
		}
		return convertedXMLGregorianCalendar;
	}

	/**
	 * Transforme un objet Date en LocalDate.
	 * 
	 * @param dateToConvert
	 * @return LocalDate
	 */
	public static LocalDate convertDateToLocalDate( Date dateToConvert )
	{
		if ( dateToConvert == null )
		{
			return null;
		}

		return dateToConvert.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
	}

	/**
	 * Transforme un objet LocalDate en Date.
	 * 
	 * @param localDateToConvert
	 * @return Date
	 */
	public static Date convertLocalDateToDate( LocalDate localDateToConvert )
	{
		if ( localDateToConvert == null )
		{
			return null;
		}

		return Date.from( localDateToConvert.atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() );
	}

	/**
	 * Transforme un type xmlGregorianCalendar en type LocalDate
	 * 
	 * @param xmlDate xmlGregorianCalendar
	 * @return LocalDate
	 */
	public static LocalDate getLocalDateFromXmlGregorianCalendar( XMLGregorianCalendar xmlDate )
	{
		if ( xmlDate == null )
		{
			return null;
		}

		return convertDateToLocalDate( getDateFromXmlGregorianCalendar( xmlDate ) );
	}

	/**
	 * Transforme un type LocalDate en type xmlGregorianCalendar
	 * 
	 * @param LocalDate localDate
	 * @return xmlGregorianCalendar
	 */
	public static XMLGregorianCalendar getXmlGregorianCalendarFromLocalDate( LocalDate localDate )
	{
		if ( localDate == null )
		{
			return null;
		}

		return convertDateToXMLGregorianCalendar( convertLocalDateToDate( localDate ) );
	}

}
