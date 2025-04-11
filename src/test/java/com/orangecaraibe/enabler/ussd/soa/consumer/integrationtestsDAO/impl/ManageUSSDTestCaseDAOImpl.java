/**
 *
 */
package com.orangecaraibe.enabler.ussd.soa.consumer.integrationtestsDAO.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.orangecaraibe.enabler.ussd.soa.consumer.integrationtestsDAO.ManageUSSDTestCaseDAO;

/**
 * @author sgodard
 */
@SuppressWarnings( { "unchecked", "rawtypes" } )
@Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
@Component( "manageUSSDTestCaseDAO" )
public class ManageUSSDTestCaseDAOImpl
	implements ManageUSSDTestCaseDAO
{

	@Qualifier( "bscsEntityManager" )
	@Autowired
	private EntityManager bscsEntityManager;

	/** * GP Engage (39 sec)* */
	@Override
	public List<String> getMSISDNForGPEngage()
	{
		Session session = bscsEntityManager.unwrap( Session.class );
		return session.doReturningWork( connection -> {

			String sql =
				"SELECT distinct(mp.dn_num) FROM contract_all con join contr_services_cap csc on csc.co_id = con.co_id "
					+ "join directory_number mp on mp.dn_id = csc.dn_id "
					+ "join contract_history ch on con.co_id = ch.co_id " + "and con.tmcode=104 "
					+ "AND csc.main_dirnum = 'X' "
					+ "and ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id) "
					+ "and rownum <20 and mp.dn_status !='d'";

			Query query = session.createSQLQuery( sql );
			return (List<String>) query.getResultList();

		} );
	}

	/** * GP non Engage (39 sec)* */
	@Override
	public List<String> getMSISDNForGPNonEngage()
	{
		Session session = bscsEntityManager.unwrap( Session.class );
		return session.doReturningWork( connection -> {
			String sql =
				"SELECT distinct(mp.dn_num) FROM contract_all con join contr_services_cap csc on csc.co_id = con.co_id "
					+ "join directory_number mp on mp.dn_id = csc.dn_id "
					+ "join contract_history ch on con.co_id = ch.co_id "
					+ "join  ADM_DUNNING.VIEW_ENGAGEMENT@LINK_BSCS_APPS.INFO.GP.OC a ON a.customer_id = con.customer_id "
					+ "WHERE (a.end_engagement<sysdate and a.end_reengagement<sysdate) " + "and con.tmcode=104 "
					+ "and ch.ch_status='a' " + "AND csc.main_dirnum = 'X' "
					+ "and ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id) "
					+ "and rownum <20 and mp.dn_status !='d'";

			Query query = session.createSQLQuery( sql );
			return (List<String>) query.getResultList();

		} );
	}

	/** * Pro non Engage Gestionnaire (30 sec)* */
	@Override
	public List<String> getMSISDNForProNonEngageGestionnaire()
	{
		Session session = bscsEntityManager.unwrap( Session.class );
		return session.doReturningWork( connection -> {

			String sql = "SELECT distinct(mp.dn_num) FROM contract_all con "
				+ "join contr_services_cap csc on csc.co_id = con.co_id "
				+ "join directory_number mp on mp.dn_id = csc.dn_id "
				+ "join contract_history ch on con.co_id = ch.co_id "
				+ "join  ADM_DUNNING.VIEW_ENGAGEMENT@LINK_BSCS_APPS.INFO.GP.OC a ON a.customer_id = con.customer_id "
				+ "join ccontact_all cc on cc.customer_id = a.customer_id " + "WHERE con.tmcode=344 "
				+ "and cc.ccsmsno = mp.dn_num "
				+ "and ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id) and rownum <20 and mp.dn_status !='d'";

			Query query = session.createSQLQuery( sql );
			return (List<String>) query.getResultList();
		} );
	}

	/** * Pro non Engage non Gestionnaire (22 sec)* */
	@Override
	public List<String> getMSISDNForProNonEngageNonGestionnaire()
	{
		Session session = bscsEntityManager.unwrap( Session.class );
		return session.doReturningWork( connection -> {

			String sql = "SELECT distinct(mp.dn_num ) FROM contract_all con "
				+ "join contr_services_cap csc on csc.co_id = con.co_id "
				+ "join directory_number mp on mp.dn_id = csc.dn_id "
				+ "join customer_all cus on  cus.customer_id = con.customer_id "
				+ "join contract_history ch on con.co_id = ch.co_id join ccontact_all cco on cco.customer_id = cus.customer_id "
				+ "WHERE cco.cscusttype='B' " + "and cco.ccsmsno != mp.dn_num and con.tmcode=189 and ch.ch_status='a' "
				+ "and ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id)  and rownum<50 and mp.dn_status !='d'";

			Query query = session.createSQLQuery( sql );
			return (List<String>) query.getResultList();

		} );
	}

	/** * Pro Engage Gestionnaire (25sec)* */
	@Override
	public List<String> getMSISDNForProEngageGestionnaire()
	{
		Session session = bscsEntityManager.unwrap( Session.class );
		return session.doReturningWork( connection -> {

			String sql = "SELECT distinct(mp.dn_num) FROM contract_all con "
				+ "join contr_services_cap csc on csc.co_id = con.co_id "
				+ "join directory_number mp on mp.dn_id = csc.dn_id "
				+ "join contract_history ch on con.co_id = ch.co_id "
				+ "join  ADM_DUNNING.VIEW_ENGAGEMENT@LINK_BSCS_APPS.INFO.GP.OC a ON a.customer_id = con.customer_id "
				+ "join ccontact_all cc on cc.customer_id = a.customer_id "
				+ "join customer_all cus on  cus.customer_id = a.customer_id "
				+ "WHERE (a.end_engagement>sysdate and a.end_reengagement>sysdate) " + "and con.tmcode=344 "
				+ "and cc.cscusttype='B' " + "and a.ch_status='a' " + "and cc.ccsmsno = mp.dn_num "
				+ "and ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id) "
				+ "and cus.customer_id !=2962525" + "and rownum <20 and mp.dn_status !='d'";

			Query query = session.createSQLQuery( sql );
			return (List<String>) query.getResultList();

		} );
	}

	/** * Pro Engage non Gestionnaire (1,64sec)* */
	@Override
	public List<String> getMSISDNForProEngageNonGestionnaire()
	{
		Session session = bscsEntityManager.unwrap( Session.class );
		return session.doReturningWork( connection -> {
			String sql = "SELECT distinct(mp.dn_num ) FROM contract_all con "
				+ "join contr_services_cap csc on csc.co_id = con.co_id "
				+ "join directory_number mp on mp.dn_id = csc.dn_id "
				+ "join customer_all cus on  cus.customer_id = con.customer_id "
				+ "join contract_history ch on con.co_id = ch.co_id "
				+ "join ccontact_all cco on cco.customer_id = cus.customer_id "
				+ "join  ADM_DUNNING.VIEW_ENGAGEMENT@LINK_BSCS_APPS.INFO.GP.OC a ON a.customer_id = con.customer_id "
				+ "WHERE con.tmcode=83 and cco.ccsmsno != mp.dn_num  and ch.ch_status='a' "
				+ "and ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id) "
				+ "AND cus.customer_id BETWEEN 3100000 and 3500000 and rownum<50 and mp.dn_status !='d'";

			Query query = session.createSQLQuery( sql );
			return (List<String>) query.getResultList();

		} );
	}

}
