package com.orangecaraibe.enabler;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings( { "rawtypes", "unchecked" } )
@Component
@Transactional( propagation = Propagation.SUPPORTS, readOnly = true )
public class UseCaseTestFindAndGetDAO
{
	@Qualifier( "bscsEntityManager" )
	@Autowired
	private EntityManager bscsEntityManager;

	public InfoClient findAndGetCustomer()
	{
		Session session = bscsEntityManager.unwrap( Session.class );
		return (InfoClient) session.doReturningWork( connection -> {

			String sql =
				"SELECT con.customer_id,con.co_id,mp.dn_num,ctc.ccemail,con.tmcode,cus.custcode FROM contract_all con, contract_history ch,ccontact_all ctc,"
					+ "contr_services_cap csc,directory_number mp ,  sysadm.mpulktmb mpu ,sysadm.customer_all cus "
					+ "WHERE mp.dn_id = csc.dn_id  AND mpu.tmcode = con.tmcode "
					+ "AND cus.customer_id = con.customer_id "
					+ "AND csc.co_id = con.co_id AND csc.main_dirnum = 'X' AND ch.ch_status = 'a' "
					+ "AND con.co_id = ch.co_id AND ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id) "
					+ "AND ctc.ccemail like  '%@%.%' and rownum <= 20 order by 1 desc";

			Query query = session.createSQLQuery( sql );
			List<Object[]> lst = query.getResultList();

			return getInfoClient( lst.get( 0 ) );

		} );
	}

	private Object getInfoClient( Object[] obj )
	{
		InfoClient infoClient = new InfoClient();
		BigDecimal customerId = (BigDecimal) obj[0];
		BigDecimal coId = (BigDecimal) obj[1];
		String msisdn = (String) obj[2];
		String email = (String) obj[3];
		BigDecimal tmCode = (BigDecimal) obj[4];
		String custcode = (String) obj[5];
		String idClientPublic = "123456";

		infoClient.setCo_id( String.valueOf( coId ) );
		infoClient.setCustomer_id( String.valueOf( customerId ) );
		infoClient.setMsisdn( msisdn );
		infoClient.setEmail( email );
		infoClient.setTmcode( String.valueOf( tmCode ) );
		infoClient.setCustcode( custcode );
		infoClient.setIdClientPublic( idClientPublic );

		return infoClient;

	}

	public InfoClient findAndGetCustomerForAccordCommercial()
	{

		Session session = bscsEntityManager.unwrap( Session.class );
		return (InfoClient) session.doReturningWork( connection -> {

			String sql =
				"SELECT con.customer_id,con.co_id,mp.dn_num,ctc.ccemail,con.tmcode,cus.custcode FROM contract_all con, contract_history ch,ccontact_all ctc,"
					+ "contr_services_cap csc,directory_number mp ,  sysadm.mpulktmb mpu, sysadm.customer_all cus  "
					+ "WHERE mp.dn_id = csc.dn_id  AND mpu.tmcode = con.tmcode "
					+ "AND csc.co_id = con.co_id AND csc.main_dirnum = 'X' AND ch.ch_status = 'a' "
					+ "AND con.co_id = ch.co_id AND ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id) "
					+ " AND cus.customer_id=con.customer_id  "
					+ " AND ctc.ccemail like  '%@%.%' and rownum <= 20 order by 1 desc";

			Query query = session.createSQLQuery( sql );
			List<Object[]> lst = query.list();

			return getInfoClient( lst.get( 0 ) );

		} );
	}

	public InfoClient findAndGetCustomerPPD()
	{

		Session session = bscsEntityManager.unwrap( Session.class );
		return (InfoClient) session.doReturningWork( connection -> {

			String sql =
				"SELECT con.customer_id,con.co_id,mp.dn_num,ctc.ccemail,con.tmcode ,cus.custcode FROM contract_all con, contract_history ch,ccontact_all ctc,"
					+ "contr_services_cap csc,directory_number mp,sysadm.mpulktmb mpu, adm_bscs_oca.rateplans rat, sysadm.customer_all cus  "
					+ "WHERE mp.dn_id = csc.dn_id  AND mpu.tmcode = con.tmcode "
					+ "AND csc.co_id = con.co_id AND csc.main_dirnum = 'X' AND ch.ch_status = 'a' "
					+ "AND con.co_id = ch.co_id AND ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id) "
					+ "AND ctc.ccemail like  '%@%.%' " + "AND rat.tm_type ='PPS' "
					+ "AND cus.customer_id=con.customer_id " + "and rownum <= 20 order by 1 desc";

			Query query = session.createSQLQuery( sql );
			List<Object[]> lst = query.list();

			return getInfoClient( lst.get( 0 ) );

		} );
	}

	public InfoClient findAndGetCustomerPPS()
	{

		Session session = bscsEntityManager.unwrap( Session.class );
		return (InfoClient) session.doReturningWork( connection -> {

			String sql =
				"SELECT con.customer_id,con.co_id,mp.dn_num,ctc.ccemail,con.tmcode ,cus.custcode FROM contract_all con, contract_history ch,ccontact_all ctc,"
					+ "contr_services_cap csc,directory_number mp ,  sysadm.mpulktmb mpu , adm_bscs_oca.rateplans rat ,sysadm.customer_all cus  "
					+ "WHERE mp.dn_id = csc.dn_id  AND mpu.tmcode = con.tmcode "
					+ "AND csc.co_id = con.co_id AND csc.main_dirnum = 'X' AND ch.ch_status = 'a' "
					+ "AND con.co_id = ch.co_id AND ch.ch_seqno = (select max(ch1.ch_seqno) from contract_history ch1 where ch1.co_id = ch.co_id) "
					+ "AND ctc.ccemail like  '%@%.%' " + "AND rat.tm_type ='PPS' "
					+ "AND cus.customer_id=con.customer_id " + "AND rownum <= 20 order by 1 desc";

			Query query = session.createSQLQuery( sql );
			List<Object[]> lst = query.list();

			return getInfoClient( lst.get( 0 ) );

		} );
	}
}
