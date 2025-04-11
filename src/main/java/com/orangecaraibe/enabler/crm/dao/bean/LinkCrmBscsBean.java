package com.orangecaraibe.enabler.crm.dao.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "V_CRM_LINK_BSCS" )
public class LinkCrmBscsBean
	implements java.io.Serializable
{

	private String crmGuid;

	private Long customerId;

	private String crmFunctionalId;

	private String custcode;

	@Id
	@Column( name = "CUSTOMER_ID" )
	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId( Long customerId )
	{
		this.customerId = customerId;
	}

	@Column( name = "GUID_PARTY" )
	public String getCrmGuid()
	{
		return crmGuid;
	}

	public void setCrmGuid( String crmGuid )
	{
		this.crmGuid = crmGuid;
	}

	@Column( name = "ID_PARTY" )
	public String getCrmFunctionalId()
	{
		return crmFunctionalId;
	}

	public void setCrmFunctionalId( String crmFunctionalId )
	{
		this.crmFunctionalId = crmFunctionalId;
	}

	/**
	 * @return the custcode
	 */
	@Column( name = "CUSTCODE" )
	public String getCustcode()
	{
		return custcode;
	}

	/**
	 * @param custcode the custcode to set
	 */
	public void setCustcode( String custcode )
	{
		this.custcode = custcode;
	}

}
