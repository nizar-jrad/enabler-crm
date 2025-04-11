package com.orangecaraibe.enabler.svi.business.service;


import com.orangecaraibe.enabler.svi.core.exception.TechnicalErrorException;
import com.orangecaraibe.enabler.svi.core.exception.UnknownClientException;
import com.orangecaraibe.enabler.svi.core.exception.UnknownMsisdnException;
import com.orangecaraibe.enabler.svi.business.beans.FormattedPortabilite;
/**
 * @author exelcia Recherche des infos de portabilité à partir du msisdn
 */
public interface SVIService
{
	public FormattedPortabilite getSVIFormattedPortabilityInfo( String msisdn )
		throws UnknownClientException, UnknownMsisdnException, TechnicalErrorException;
}
