/**
 *
 */
package com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO.ManageCRMCreateOrderRemoveServiceDAO;
import com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO.api.RemoveService;

/**
 * @author sgodard
 */
@SuppressWarnings( { "unchecked", "rawtypes" } )
@Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
@Component( "manageCRMCreateOrderRemoveServiceDAO" )
@Deprecated
public class ManageCRMCreateOrderRemoveServiceDAOImpl
	implements ManageCRMCreateOrderRemoveServiceDAO
{

	@Qualifier( "bscsEntityManager" )
	@Autowired
	private EntityManager bscsEntityManager;

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enablers.installedbase.soa.consumer.integrationtests.processorder.dao.
	 * ManageCustomerOrderTestCaseDAO#getRateplan1hForUpdateRateplan(java.lang.String))
	 */
	private List<RemoveService> getRateplanByTmCode( final String tmCode, final String snCode, final String spCode )
	{
		Session session = bscsEntityManager.unwrap( Session.class );
		return session.doReturningWork( connection -> {

			String sql = "SELECT con.customer_id,con.co_id,mp.dn_num FROM contract_all con, contract_history ch,"
				+ "contr_services_cap csc,directory_number mp ,  sysadm.mpulktmb mpu " + "WHERE con.tmcode =" + tmCode
				+ " AND mp.dn_id = csc.dn_id  AND mpu.tmcode = con.tmcode "
				+ "AND csc.co_id = con.co_id AND csc.main_dirnum = 'X' AND ch.ch_status = 'a' "
				+ "AND con.co_id = ch.co_id AND ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id) "
				+ "AND con.customer_id BETWEEN 2500000 AND 3000000 AND rownum < 10 and mpu.sncode  = " + snCode
				+ " and mpu.spcode=" + spCode + "";

			Query query = session.createSQLQuery( sql );
			List<Object[]> lst = query.getResultList();
			List<RemoveService> clients = new ArrayList<RemoveService>();
			getRemoveService( lst, clients, tmCode, snCode, spCode );
			return clients;

		} );
	}

	/**
	 * @param lst
	 * @param clients
	 */
	private void getRemoveService(	List<Object[]> lst, List<RemoveService> clients, String tmCode, String snCode,
									String spCode )
	{
		for ( Object[] obj : lst )
		{
			BigDecimal customerId = (BigDecimal) obj[0];
			BigDecimal coId = (BigDecimal) obj[1];
			String msisdn = (String) obj[2];

			RemoveService RemoveService = new RemoveService();
			RemoveService.setCustomerId( String.valueOf( customerId ) );
			RemoveService.setCoId( String.valueOf( coId ) );
			RemoveService.setMsisdn( msisdn );
			RemoveService.setTmCode( tmCode );
			RemoveService.setSnCode( snCode );
			RemoveService.setSpCode( spCode );
			clients.add( RemoveService );
		}
	}

	@Override
	public List<RemoveService> getRemoveServiceXtrem()
	{
		// Xtrem2h
		return getRateplanByTmCode( "267", "23", "6" );
	}

	@Override
	public List<RemoveService> getRemoveServiceFlotte()
	{
		return getRateplanByTmCode( "87", "23", "6" );
	}

	@Override
	public List<RemoveService> getRemoveServicePro()
	{ // PRO OBS 3H
		return getRateplanByTmCode( "333", "23", "6" );
	}

	@Override
	public List<RemoveService> getRemoveServicePref()
	{
		// Pref 2h
		return getRateplanByTmCode( "104", "23", "6" );
	}
}
