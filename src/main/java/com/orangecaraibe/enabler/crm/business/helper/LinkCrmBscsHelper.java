package com.orangecaraibe.enabler.crm.business.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.crm.business.bean.Holder;
import com.orangecaraibe.enabler.crm.dao.bean.LinkCrmBscsBean;
import com.orangecaraibe.enabler.crm.data.dao.LinkCrmBscsDAO;
import com.orangecaraibe.enabler.crm.data.dao.LinkCrmBscsDAOSoa;

@Component
public class LinkCrmBscsHelper
{
	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( LinkCrmBscsHelper.class );

	@Autowired
	@Qualifier( "linkCrmBscsDAO" )
	private LinkCrmBscsDAO linkCrmBscsDAO;

	@Autowired
	@Qualifier( "linkCrmBscsDAOSoa" )
	private LinkCrmBscsDAOSoa linkCrmBscsDAOSoa;

	@Value( "${lnk_crm_bscs.partyid.enabled}" )
	private boolean linkEnabled;

	/**
	 * Renvoie le GUID CRM du titulaire a partir du customerId
	 * 
	 * @param string
	 * @return
	 */
	public LinkCrmBscsBean getCrmGuidByCustomerId( String billingAccountID )
	{

		if ( linkEnabled )
		{

			if ( StringUtils.isNotEmpty( billingAccountID ) )
			{
				// on interroge la table de synchro BSCS CRM
				Long customerId = Long.valueOf( billingAccountID );
				String paymentResponsible = getPaymentResponsible( String.valueOf( customerId ) );
				return getCrmGuidByCustId( Long.valueOf( paymentResponsible ) );
			}
		}
		return null;
	}

	private String getPaymentResponsible( String customerId )
	{
		return linkCrmBscsDAOSoa.getPaymentResponsible( customerId );
	}

	private LinkCrmBscsBean getCrmGuidByCustId( Long customerId )
	{
		return linkCrmBscsDAO.getCrmGuidByCustomerId( customerId );
	}

	/**
	 * Construit un Holder a partir du customerId
	 * 
	 * @param customerId
	 * @return
	 */
	public Holder getHolderByCustomerId( LinkCrmBscsBean linkCrmBscsBean )
	{
		Holder businessHolder = null;

		String crmGuid = linkCrmBscsBean.getCrmGuid();
		if ( StringUtils.isNotEmpty( crmGuid ) )
		{
			businessHolder = new Holder();
			businessHolder.setGuid( crmGuid );
		}
		else
		{
			LOGGER.warn( "No CRM GUID correspondance found for customer_id " + linkCrmBscsBean );
		}

		return businessHolder;
	}

	public void setLinkCrmBscsDAO( LinkCrmBscsDAO linkCrmBscsDAO )
	{
		this.linkCrmBscsDAO = linkCrmBscsDAO;
	}

	public boolean isLinkEnabled()
	{
		return linkEnabled;
	}

	public void setLinkEnabled( boolean linkEnabled )
	{
		this.linkEnabled = linkEnabled;
	}

	public void setLinkCrmBscsDAOSoa( LinkCrmBscsDAOSoa linkCrmBscsDAOSoa )
	{
		this.linkCrmBscsDAOSoa = linkCrmBscsDAOSoa;
	}

}
