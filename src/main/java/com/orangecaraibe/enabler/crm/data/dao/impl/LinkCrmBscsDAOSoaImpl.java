package com.orangecaraibe.enabler.crm.data.dao.impl;

import java.sql.CallableStatement;
import java.sql.Types;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.orangecaraibe.enabler.crm.data.dao.LinkCrmBscsDAOSoa;

@Repository( "linkCrmBscsDAOSoa" )
public class LinkCrmBscsDAOSoaImpl
	implements LinkCrmBscsDAOSoa
{
	private static final Logger LOGGER = LoggerFactory.getLogger( LinkCrmBscsDAOSoaImpl.class );

	@Qualifier( "crmEntityManager" )
	@Autowired
	private EntityManager crmEntityManager;

	@Override
	public String getPaymentResponsible( final String customerID )
	{
		if ( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "? = call ADM_SOA_SICLIENT.GATEWAY_TO_SYSADM.GET_PAYMNTRESP(" + customerID + ")" );
		}

		Session session = crmEntityManager.unwrap( Session.class );

		return session.doReturningWork( connection -> {
			CallableStatement call =
				connection.prepareCall( "{ ? = call ADM_SOA_SICLIENT.GATEWAY_TO_SYSADM.GET_PAYMNTRESP(?) }" );
			call.registerOutParameter( 1, Types.INTEGER );
			call.setString( 2, customerID );

			call.execute();
			int result = call.getInt( 1 );
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "result =>" + ( result != 0 ? result : "" ) );
			}
			return String.valueOf( result );
		} );

	}

}
