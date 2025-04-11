package com.orangecaraibe.enabler.crm.webservice.criteria;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.orangecaraibe.soa.v2.criteria.CriteriaComposer;
import com.orangecaraibe.soa.v2.criteria.Restrictions;
import com.orangecaraibe.soa.v2.model.commons.criteria.Criteria;

/**
 * The Class CustomerProblemManager.
 */
@Component
public class CustomerProblemManager
{

	/**
	 * Recherche du canal emetteur.
	 * 
	 * @param criteria the criteria
	 * @return the string
	 */
	public String searchBySaleChannel( Criteria criteria )
	{

		// Criteria type d'une recherche par saleChannel
		Criteria searchSaleChannel =
			CriteriaComposer.getInstance().add( Restrictions.eq(	CriteriaConstants.saleChannel,
																	String.class ) ).getCriteria();
		// Comparaison du criteria reçu à celui d'une recherche par saleChannel
		if ( CriteriaComposer.isUseCase( criteria, searchSaleChannel ) )
		{
			// C est une recherche par saleChannel, on l extrait pour le passer en résultat
			String saleChannel = (String) CriteriaComposer.extract( criteria ).get( CriteriaConstants.saleChannel );
			if ( StringUtils.isEmpty( saleChannel ) )
			{
				return null;
			}

			return saleChannel;
		}
		else
		{
			// Ce n est pas une recherche par saleChannel
			return null;
		}
	}

	/**
	 * Recherche du type de theme
	 * 
	 * @param criteria the criteria
	 * @return the string
	 */
	public String searchByTyoe( Criteria criteria )
	{

		// Criteria type d'une recherche par typeTheme
		Criteria searchSaleChannel =
			CriteriaComposer.getInstance().add( Restrictions.eq(	CriteriaConstants.typeTheme,
																	String.class ) ).getCriteria();
		// Comparaison du criteria recu a celui d'une recherche par typeTheme
		if ( CriteriaComposer.isUseCase( criteria, searchSaleChannel ) )
		{
			// C est une recherche par typeTheme, on l extrait pour le passer en resultat
			String typeTheme = (String) CriteriaComposer.extract( criteria ).get( CriteriaConstants.typeTheme );
			if ( StringUtils.isEmpty( typeTheme ) )
			{
				return null;
			}

			return typeTheme;
		}
		else
		{
			// Ce n est pas une recherche par typeTheme
			return null;
		}
	}
}
