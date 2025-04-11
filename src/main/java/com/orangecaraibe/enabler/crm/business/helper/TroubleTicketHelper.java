/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.helper;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;
import com.orangecaraibe.enabler.crm.dao.TroubleTicketDAO;
import com.orangecaraibe.enabler.crm.dao.bean.ThemePKBean;
import com.orangecaraibe.enabler.crm.dao.bean.TroubleTicketServiceBean;

/**
 * @author ncrtest2
 */
@Component
public class TroubleTicketHelper
{

	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( TroubleTicketHelper.class );

	/** nom de l'application pour OCEANE */
	@Value( "${trouble.ticket.oceane.application.name}" )
	private String applicationName;

	@Value( "${trouble.ticket.oceane.generique.login}" )
	private String generiqueLogin;

	@Autowired
	private TroubleTicketDAO troubleTicketDAO;

	public String getApplicationName()
	{

		return applicationName;
	}

	public String getServiceOceane( String theme1, String theme2, String theme3 )
		throws CustomerProblemUpdateException
	{

		// TODO: suppression du commenataire sur l'appel base de donnée une fois m:apping theme service reçut
		TroubleTicketServiceBean troubleTicketServiceBean = new TroubleTicketServiceBean();
		ThemePKBean themePkBean = new ThemePKBean( theme1, theme2, theme3 );
		troubleTicketServiceBean.setThemePkBean( themePkBean );
		String Service = troubleTicketDAO.getServiceOceane( troubleTicketServiceBean );

		if ( Service == null )
		{
			throw new CustomerProblemUpdateException( StringUtils.join( new String[] {
				"Pas de service trouvé pour les themes : ", theme1, ", ", theme2, ", ", theme3 } ), "" );
		}

		return Service;
	}

	/**
	 * @param troubleTicketDAO the troubleTicketDAO to set
	 */
	public void setTroubleTicketDAO( TroubleTicketDAO troubleTicketDAO )
	{
		this.troubleTicketDAO = troubleTicketDAO;
	}

	/**
	 * @param generiqueLogin the generiqueLogin to set
	 */
	public void setGeneriqueLogin( String generiqueLogin )
	{
		this.generiqueLogin = generiqueLogin;
	}

	public BigInteger getTroubleTickepriority()
	{
		return BigInteger.valueOf( 3 );
	}

	public String getTroubleTicketCategory()
	{
		return "23";
	}

	public String getTroubleTicketType()
	{
		return "2";
	}

	public String getTroubleTicketOriginator()
	{
		return "1";
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName( String applicationName )
	{
		this.applicationName = applicationName;
	}

	/**
	 * @return the generiqueLogin
	 */
	public String getGeneriqueLogin()
	{
		return generiqueLogin;
	}

	public String getTroubletTicketResponseString()
	{

		return "success";
	}

	public String getTroubletTicketResponseCode()
	{
		return "0";
	}
}
