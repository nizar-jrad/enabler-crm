/**
 * 
 */
package com.orangecaraibe.enabler.crm.dao.impl;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.crm.dao.TroubleTicketDAO;
import com.orangecaraibe.enabler.crm.dao.bean.ThemePKBean;
import com.orangecaraibe.enabler.crm.dao.bean.TroubleTicketServiceBean;

/**
 * @author ncrtest2
 */
@Component( "troubleTicketDAO" )
public class TroubleTicketDAOImpl
	implements TroubleTicketDAO
{
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( TroubleTicketDAOImpl.class );

	@Qualifier( "crmEntityManager" )
	@Autowired
	private EntityManager crmEntityManager;

	@Override
	public String getServiceOceane( final TroubleTicketServiceBean troubleTicketServiceBean )
	{
		ThemePKBean themePkBean = troubleTicketServiceBean.getThemePkBean();
		Session session = crmEntityManager.unwrap( Session.class );
		TroubleTicketServiceBean result = session.load( TroubleTicketServiceBean.class, themePkBean );

		if ( result == null )
		{
			return null;
		}
		return result.getService();
	}
}
