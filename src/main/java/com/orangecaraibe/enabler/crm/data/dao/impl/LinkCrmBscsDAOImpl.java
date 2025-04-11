package com.orangecaraibe.enabler.crm.data.dao.impl;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.orangecaraibe.enabler.crm.dao.bean.LinkCrmBscsBean;
import com.orangecaraibe.enabler.crm.data.dao.LinkCrmBscsDAO;

@Repository( "linkCrmBscsDAO" )
public class LinkCrmBscsDAOImpl
	implements LinkCrmBscsDAO
{

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( LinkCrmBscsDAOImpl.class );

	@Qualifier( "crmEntityManager" )
	@Autowired
	private EntityManager crmEntityManager;

	public LinkCrmBscsDAOImpl()
	{

	}

	@Override
	public LinkCrmBscsBean getCrmGuidByCustomerId( final Long customerId )
	{
		LOGGER.info( "get crm guid for customerId {}", customerId );
		Session session = crmEntityManager.unwrap( Session.class );
		return session.get( LinkCrmBscsBean.class, customerId );
	}

}
