package com.orangecaraibe.enabler.externalws.helpers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.orange.sidom.soa.dcrm.datacontract.AppelTelephonique;
import com.orange.sidom.soa.dcrm.datacontract.Courrier;
import com.orange.sidom.soa.dcrm.datacontract.Email;
import com.orange.sidom.soa.dcrm.datacontract.Fax;
import com.orange.sidom.soa.dcrm.datacontract.Interaction;
import com.orange.sidom.soa.dcrm.datacontract.PDVPhysique;
import com.orange.sidom.soa.dcrm.datacontract.ReseauSocial;
import com.orange.sidom.soa.dcrm.datacontract.SMS;
import com.orange.sidom.soa.dcrm.datacontract.Selfcare;
import com.orangecaraibe.enabler.core.utils.Tools;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionChannel;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionStatus;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionWay;

/**
 * Helper qui lit le fichier de properties application.properties pour construire les Map :<br>
 * - statusReasonMap : donne les libelles business associes au statusCode DCRM<br>
 * - defaultStatusReasonMap : donne le libelle metier par defaut a utiliser lors de la creation d'une interaction en
 * fonction du channel et du sens. On retrouve ensuite le statusCode DCRM correspondant a ce libelle metier.
 */
@Component
public class InteractionReasonStatusPropertiesHelper
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger( InteractionReasonStatusPropertiesHelper.class );

	@Autowired
	private Environment env;

	@PostConstruct
	public void initProperties()
	{
		this.setStatusReasonListForFax( env.getProperty( "dcrm.interaction.statusreason.fax" ) );
		this.setStatusReasonListForCall( env.getProperty( "dcrm.interaction.statusreason.appel" ) );
		this.setStatusReasonListForEmail( env.getProperty( "dcrm.interaction.statusreason.email" ) );
		this.setStatusReasonListForLetter( env.getProperty( "dcrm.interaction.statusreason.courrier" ) );
		this.setStatusReasonListForShop( env.getProperty( "dcrm.interaction.statusreason.pdv" ) );
		this.setStatusReasonListForSms( env.getProperty( "dcrm.interaction.statusreason.sms" ) );
		this.setStatusReasonListForSelfcare( env.getProperty( "dcrm.interaction.statusreason.selfcare" ) );
		this.setStatusReasonListForSocialNetwork( env.getProperty( "dcrm.interaction.statusreason.reseausocial" ) );
		this.setDefaultStatusReasonListForFax( env.getProperty( "dcrm.interaction.statusreason.fax.default" ) );
		this.setDefaultStatusReasonListForCall( env.getProperty( "dcrm.interaction.statusreason.appel.default" ) );
		this.setDefaultStatusReasonListForEmail( env.getProperty( "dcrm.interaction.statusreason.email.default" ) );
		this.setDefaultStatusReasonListForLetter( env.getProperty( "dcrm.interaction.statusreason.courrier.default" ) );
		this.setDefaultStatusReasonListForShop( env.getProperty( "dcrm.interaction.statusreason.pdv.default" ) );
		this.setDefaultStatusReasonListForSms( env.getProperty( "dcrm.interaction.statusreason.sms.default" ) );
		this.setDefaultStatusReasonListForSelfcare( env.getProperty( "dcrm.interaction.statusreason.selfcare.default" ) );
		this.setDefaultStatusReasonListForSocialNetwork( env.getProperty( "dcrm.interaction.statusreason.reseausocial.default" ) );
	}

	Map<Class<? extends Interaction>, Map<Integer, String>> statusReasonMap =
		new HashMap<Class<? extends Interaction>, Map<Integer, String>>();

	Map<DefaultInteractionStatusReasonBean, String> defaultStatusReasonMap =
		new HashMap<DefaultInteractionStatusReasonBean, String>();

	public Map<Class<? extends Interaction>, Map<Integer, String>> getStatusReasonMap()
	{
		return statusReasonMap;
	}

	public Map<DefaultInteractionStatusReasonBean, String> getDefaultStatusReasonMap()
	{
		return defaultStatusReasonMap;
	}

	public void setStatusReasonListForFax( String list )
	{
		Map<Integer, String> statusReasonsForThisType = adaptStatusReasonListToMap( list );
		if ( MapUtils.isNotEmpty( statusReasonsForThisType ) )
		{
			statusReasonMap.put( Fax.class, statusReasonsForThisType );
		}
	}

	public void setStatusReasonListForCall( String list )
	{
		Map<Integer, String> statusReasonsForThisType = adaptStatusReasonListToMap( list );
		statusReasonMap.put( AppelTelephonique.class, statusReasonsForThisType );
	}

	public void setStatusReasonListForEmail( String list )
	{
		Map<Integer, String> statusReasonsForThisType = adaptStatusReasonListToMap( list );
		statusReasonMap.put( Email.class, statusReasonsForThisType );
	}

	public void setStatusReasonListForLetter( String list )
	{
		Map<Integer, String> statusReasonsForThisType = adaptStatusReasonListToMap( list );
		statusReasonMap.put( Courrier.class, statusReasonsForThisType );
	}

	public void setStatusReasonListForShop( String list )
	{
		Map<Integer, String> statusReasonsForThisType = adaptStatusReasonListToMap( list );
		statusReasonMap.put( PDVPhysique.class, statusReasonsForThisType );
	}

	public void setStatusReasonListForSms( String list )
	{
		Map<Integer, String> statusReasonsForThisType = adaptStatusReasonListToMap( list );
		statusReasonMap.put( SMS.class, statusReasonsForThisType );
	}

	public void setStatusReasonListForSelfcare( String list )
	{
		Map<Integer, String> statusReasonsForThisType = adaptStatusReasonListToMap( list );
		statusReasonMap.put( Selfcare.class, statusReasonsForThisType );
	}

	public void setStatusReasonListForSocialNetwork( String list )
	{
		Map<Integer, String> statusReasonsForThisType = adaptStatusReasonListToMap( list );
		statusReasonMap.put( ReseauSocial.class, statusReasonsForThisType );
	}

	public void setDefaultStatusReasonListForFax( String list )
	{
		adaptDefaultStatusReasonListToMap( list, EnumInteractionChannel.FAX );
	}

	public void setDefaultStatusReasonListForCall( String list )
	{
		adaptDefaultStatusReasonListToMap( list, EnumInteractionChannel.PHONE_CALL );
	}

	public void setDefaultStatusReasonListForEmail( String list )
	{
		adaptDefaultStatusReasonListToMap( list, EnumInteractionChannel.EMAIL );
	}

	public void setDefaultStatusReasonListForLetter( String list )
	{
		adaptDefaultStatusReasonListToMap( list, EnumInteractionChannel.MAIL );
	}

	public void setDefaultStatusReasonListForShop( String list )
	{
		adaptDefaultStatusReasonListToMap( list, EnumInteractionChannel.STORE );
	}

	public void setDefaultStatusReasonListForSms( String list )
	{
		adaptDefaultStatusReasonListToMap( list, EnumInteractionChannel.SMS );
	}

	public void setDefaultStatusReasonListForSelfcare( String list )
	{
		adaptDefaultStatusReasonListToMap( list, EnumInteractionChannel.SELFCARE );
	}

	public void setDefaultStatusReasonListForSocialNetwork( String list )
	{
		adaptDefaultStatusReasonListToMap( list, EnumInteractionChannel.SOCIAL_NETWORK );
	}

	private Map<Integer, String> adaptStatusReasonListToMap( String list )
	{
		Map<Integer, String> statusReasonsForThisType = new HashMap<Integer, String>();
		if ( StringUtils.isNotEmpty( list ) )
		{
			String[] statusReasonArray = Tools.stringToArray( list, "|" );
			for ( String statusReason : statusReasonArray )
			{
				String[] valueLabelPair = statusReason.split( ";" );
				// la cle est le statusCode DCRM, la valeur est le libelle business
				statusReasonsForThisType.put( Integer.valueOf( valueLabelPair[0] ), valueLabelPair[1] );
			}
		}
		return statusReasonsForThisType;
	}

	private void adaptDefaultStatusReasonListToMap( String list, EnumInteractionChannel channel )
	{

		if ( StringUtils.isNotEmpty( list ) )
		{
			String[] statusReasonArray = Tools.stringToArray( list, "|" );
			for ( String statusReason : statusReasonArray )
			{
				String[] values = statusReason.split( ";" );
				if ( values.length != 3 )
				{
					LOGGER.error( "verifier la configuration des status reason par defaut pour la creation d'interaction" );
				}
				else
				{
					DefaultInteractionStatusReasonBean bean = new DefaultInteractionStatusReasonBean();
					bean.setStatus( EnumInteractionStatus.valueOf( values[0] ) );
					bean.setWay( EnumInteractionWay.valueOf( values[1] ) );
					bean.setChannel( channel );
					// la cle est un bean constituer du triplet (statut, direction canal), la valeur est le libelle
					// business
					defaultStatusReasonMap.put( bean, values[2] );
				}
			}
		}

	}

}
