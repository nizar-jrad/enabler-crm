/**
 *
 */
package com.orangecaraibe.enabler.ussd.business.service;

import com.orangecaraibe.enabler.ussd.core.exception.TechnicalErrorException;
import com.orangecaraibe.enabler.ussd.core.exception.UnknownClientException;
import com.orangecaraibe.enabler.ussd.core.exception.UnknownMsisdnException;

/**
 * @author sgodard Recherche des infos de portabilité à partir du msisdn
 */
public interface USSDService
{
	public String getUSSDFormatedPortabilityInfo( String msisdn )
		throws UnknownClientException, UnknownMsisdnException, TechnicalErrorException;
}
