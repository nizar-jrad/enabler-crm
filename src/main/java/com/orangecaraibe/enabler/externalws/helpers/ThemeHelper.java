/**
 * 
 */
package com.orangecaraibe.enabler.externalws.helpers;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.crm.business.bean.CustomerProblemReason;
import com.orangecaraibe.enabler.crm.business.bean.Theme;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemFindReasonException;
import com.orangecaraibe.enabler.externalws.services.ExternalWSService;

/**
 * @author ncrtest2
 */
@Component
public class ThemeHelper
{

	private List<CustomerProblemReason> listeTheme;

	@Autowired
	private ExternalWSService externalWSService;

	public Theme getGUIDTheme( Theme theme )
		throws CustomerProblemFindReasonException
	{

		if ( CollectionUtils.isEmpty( listeTheme ) )
		{
			listeTheme = externalWSService.findCustomerProblemReason( null, null, 0 );
		}
		for ( CustomerProblemReason mainReason : listeTheme )
		{
			// RUNO-6100 - Ajout du test sur l'equivalence du GUID
			if ( mainReason.getDescription().equals( theme.getLabel() )
				|| mainReason.getGuid().equals( theme.getGuid() ) )
			{
				theme.setGuid( mainReason.getGuid() );
				for ( CustomerProblemReason reason : mainReason.getCustomerProblemReason() )
				{
					if ( CollectionUtils.isNotEmpty( theme.getTheme() ) )
					{
						if ( theme.getTheme().get( 0 ).getLabel().equals( reason.getDescription() ) )
						{
							theme.getTheme().get( 0 ).setGuid( reason.getGuid() );

							for ( CustomerProblemReason subReason : reason.getCustomerProblemReason() )
							{
								if ( CollectionUtils.isNotEmpty( theme.getTheme().get( 0 ).getTheme() ) )
								{
									if ( theme.getTheme().get( 0 ).getTheme().get( 0 ).getLabel().equals( subReason.getDescription() ) )
									{
										theme.getTheme().get( 0 ).getTheme().get( 0 ).setGuid( subReason.getGuid() );
										break;
									}
								}
							}
						}
					}
				}
			}
		}

		return theme;
	}

	/**
	 * @param externalWSService the externalWSService to set
	 */
	public void setExternalWSService( ExternalWSService externalWSService )
	{
		this.externalWSService = externalWSService;
	}

}
