/**
 *
 */
package com.orangecaraibe.enabler.core.classification.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.core.catalogue.CatalogueHelper;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialcatalogue.CommercialClassification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.commercialinstalledbase.InstalledOffer;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionSpecification;
import com.orangecaraibe.soa.v2.model.offeringandproducts.productcatalogue.FunctionValueSpecification;

/**
 * @author lderely
 */
@Component
public class ClassificationBuilder
{

	/** The Constant log. */
	private static final Logger LOGGER = LoggerFactory.getLogger( ClassificationBuilder.class );

	private static final String ARM_LABEL = "ARM";

	private static final String FUNCTION_REGEX_NUMERO_PREFERE = "FUOP_ExpressionReguliere_Num_Pref";

	private static final String ORANGE = "ORANGE_";

	@Autowired
	@Qualifier( "crm.catalogueHelper" )
	private CatalogueHelper catalogueHelper;

	public String getClassifications( String tmCode, String snCode, String spCode )
	{

		LOGGER.info( "DAO Impl getClasifications() - Begin | tmCode=" + tmCode + " | snCode = " + snCode
			+ " | sPcode = " + spCode );

		List<CommercialClassification> listClassification = catalogueHelper.getClassification( tmCode, spCode, snCode );

		StringBuffer stringBuffer = new StringBuffer();

		if ( listClassification.isEmpty() )
		{
			stringBuffer.append( 0 );
			stringBuffer.append( " | erreur pas de classification trouver pour ce TmCode = " + tmCode );
		}
		else
		{
			stringBuffer.append( 1 );
			stringBuffer.append( " | " );

			for ( CommercialClassification classification : listClassification )
			{

				stringBuffer.append( classification.getCode() + "|" );

			}
		}

		return stringBuffer.toString();

	}

	public String getClassification( String tmCode, String snCode, String spCode, String libeller )
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "DAO Impl getClasification() - Begin | tmCode=" + tmCode + " | snCode = " + snCode
				+ " | sPcode = " + spCode );
		}

		List<CommercialClassification> listClassification = catalogueHelper.getClassification( tmCode, spCode, snCode );

		StringBuffer stringBuffer = new StringBuffer();
		String classif = " |" + libeller + " N" + "|";

		if ( listClassification.isEmpty() )
		{
			stringBuffer.append( 0 );
			stringBuffer.append( " | erreur pas de classification trouver pour ce TmCode = " + tmCode + "|" );
		}
		else
		{
			stringBuffer.append( 1 );

			for ( CommercialClassification classification : listClassification )
			{

				if ( classification.getLabel().equals( libeller ) )
				{
					classif = " |" + libeller + " Y" + "|";
				}
			}
		}
		stringBuffer.append( classif );
		return stringBuffer.toString();

	}

	/**
	 * @param catalogueHelper the catalogueHelper to set
	 */
	public void setCatalogueHelper( CatalogueHelper catalogueHelper )
	{
		this.catalogueHelper = catalogueHelper;
	}

	public String getClasificationKDOs( String tmCode, String snCode, String spCode )
	{
		String reponse = "0|Erreur pas d’offre pour cette combinaison tmCode, snCode, et spCode|";
		int nombreNumero = 0;
		String regex = "";
		String classif = "";

		Collection<FunctionValueSpecification> valueFonctions = new ArrayList<FunctionValueSpecification>();

		Collection<CommercialClassification> classification = new ArrayList<CommercialClassification>();
		List<InstalledOffer> installedOffer = catalogueHelper.getIconAtomicOffer( tmCode, spCode, snCode );
		if ( !installedOffer.isEmpty() )
		{

			List<CommercialClassification> commercialClassifications =
				catalogueHelper.getCommercialClassification( installedOffer );
			Collection<FunctionSpecification> functionSpecificaitons =
				catalogueHelper.getClassificationFonction( installedOffer );

			List<String> codeClassif = getCodeClassif( commercialClassifications );

			if ( !functionSpecificaitons.isEmpty() )
			{
				for ( FunctionSpecification functionSpecificaiton : functionSpecificaitons )
				{
					if ( functionSpecificaiton.getFunctionSpecificationCode().equals( FUNCTION_REGEX_NUMERO_PREFERE ) )
					{
						valueFonctions = functionSpecificaiton.getFunctionValueSpecifications();
					}
					else
					{
						nombreNumero++;
					}
				}

				// for ( FunctionValueSpecification valueFonction : valueFonctions )
				// {
				// List<String> codeClassifFunctionValue =
				// getCodeClassifFunctionValue( valueFonction.get );
				// if ( codeClassif.equals( codeClassifFunctionValue ) )
				// {
				//
				// regex = valueFonction.getFunctionValueLabel();
				// for ( String classifTemp : codeClassifFunctionValue )
				// {
				// classif = classif + classifTemp;
				// if ( codeClassifFunctionValue.indexOf( classifTemp ) != codeClassifFunctionValue.size()
				// - 1 )
				// {
				// classif = classif + "_";
				// }
				// }
				//
				// }
				// }
				reponse = "1|" + nombreNumero + " " + classif + " " + regex + "|";
			}
		}

		return reponse;
	}

	private List<String> getCodeClassifFunctionValue( Collection<CommercialClassification> collection )
	{
		List<String> codeClassif = new ArrayList<String>();
		for ( CommercialClassification classification : collection )
		{
			codeClassif.add( classification.getCode() );
		}

		return codeClassif;
	}

	private List<String> getCodeClassif( List<CommercialClassification> commercialClassifications )
	{
		List<String> codeClassif = new ArrayList<String>();
		for ( CommercialClassification commercialClassification : commercialClassifications )
		{
			if ( StringUtils.containsIgnoreCase( commercialClassification.getCode(), ORANGE ) )
			{
				codeClassif.add( commercialClassification.getCode() );
			}
		}

		return codeClassif;
	}

}
