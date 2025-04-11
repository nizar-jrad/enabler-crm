package com.orangecaraibe.enabler.handset.data.dao.simapp.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.handset.data.bean.SimAppInfo;

@Component( "simAppFindAndGetDAO" )
public class SimAppFindAndGetDAOImpl
	implements com.orangecaraibe.enabler.handset.data.dao.simapp.SimAppFindAndGetDAO
{

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( SimAppFindAndGetDAOImpl.class );

	@Qualifier( "simAppEntityManager" )
	@Autowired
	private EntityManager simAppEntityManager;

	public SimAppFindAndGetDAOImpl()
	{

	}

	/*
	 * obtention du code Sim à partir de l'imei
	 */
	@Override
	@SuppressWarnings( "rawtypes" )
	public SimAppInfo getSimAppInfoByImei( String imei )
		throws DataAccessException
	{
		SimAppInfo sim = null;

		if ( imei.startsWith( "12345678901234" ) )
			throw new DataRetrievalFailureException( "Test DAO : Could not load  information" );

		String sql = "select sim From SimAppInfo sim where sim.imei like '" + imei + "%'";
		Session session = simAppEntityManager.unwrap( Session.class );
		Query query = session.createSQLQuery( sql );
		List results = query.getResultList();

		if ( !results.isEmpty() )
		{
			sim = (SimAppInfo) results.get( 0 );
			System.out.println( " getSimIfnoByImei : SIM=" + sim.getCode() + " IMEI=" + imei );
			LOGGER.debug( " getSimInfoByImei : SIM=" + sim.getCode() + " IMEI=" + imei );
			return sim;
		}
		else
			return null;
	}

}
