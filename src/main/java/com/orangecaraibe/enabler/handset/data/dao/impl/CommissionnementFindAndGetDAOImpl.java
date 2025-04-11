package com.orangecaraibe.enabler.handset.data.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.orangecaraibe.enabler.handset.data.bean.Vente;

@Component( "commissionnementFindAndGetDAO" )
public class CommissionnementFindAndGetDAOImpl
	implements com.orangecaraibe.enabler.handset.data.dao.CommissionnementFindAndGetDAO
{

	@Qualifier( "crmEntityManager" )
	@Autowired
	private EntityManager crmEntityManager;

	public CommissionnementFindAndGetDAOImpl()
	{

	}

	@SuppressWarnings( "rawtypes" )
	@Override
	public Vente getVenteByImei( String imei )
		throws DataAccessException
	{

		if ( imei.startsWith( "12345678901234" ) )
		{
			throw new DataRetrievalFailureException( "Test DAO : Could not load information" );
		}

		String sql = "select vente From Vente vente where imei like '" + imei + "%'";
		Session session = crmEntityManager.unwrap( Session.class );
		Query query = session.createSQLQuery( sql );
		List results = query.getResultList();

		if ( !results.isEmpty() )
		{
			return (Vente) results.get( 0 );
		}
		else
		{
			return null;
		}
	}

	@SuppressWarnings( "rawtypes" )
	@Transactional( propagation = Propagation.REQUIRED, readOnly = false )
	@Override
	public String getImeiCodeOkDateOk()
	{
		// obtention d'un numero d'imei
		// disposant d'un code de sim
		// et dont la date d'achat remonte à plus de 3 mois
		String imei = null;
		SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
		Calendar calmoins3mois = Calendar.getInstance();
		calmoins3mois.add( Calendar.MONTH, -3 );
		Date dateAchat = calmoins3mois.getTime();

		String sql =
			"select vente.imei from Vente vente where vente.dateAchat < :dateAchat order by vente.dateAchat desc";
		Session session = crmEntityManager.unwrap( Session.class );
		Query query = session.createSQLQuery( sql );
		query.setParameter( "dateAchat", dateAchat );
		List results = query.getResultList();

		if ( CollectionUtils.isNotEmpty( results ) )
		{
			imei = (String) results.get( 0 );
			System.out.println( " >>>> getImeiCodeOkDateOk : " + imei );
			System.out.println( " date actuelle : " + sdf.format( new Date() ) );
			System.out.println( " date achat : " + sdf.format( dateAchat ) );

			return imei;
		}

		return null;
	}

}
