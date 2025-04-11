package com.orangecaraibe.enabler.crm.business.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumThemeType;

public class CustomerProblemReason
{
	private String selection;

	private EnumThemeType categorie;

	private String url;

	private String description;

	private String guid;

	private List<CustomerProblemReason> customerProblemReason;

	public String getDescription()
	{
		return description;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}

	public String getSelection()
	{
		return selection;
	}

	public void setSelection( String selection )
	{
		this.selection = selection;
	}

	public EnumThemeType getCategorie()
	{
		return categorie;
	}

	public void setCategorie( EnumThemeType categorie )
	{
		this.categorie = categorie;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	/**
	 * @return the guid
	 */
	public String getGuid()
	{
		return guid;
	}

	/**
	 * @param guid the guid to set
	 */
	public void setGuid( String guid )
	{
		this.guid = guid;
	}

	/**
	 * @return the customerProblemReason
	 */
	public List<CustomerProblemReason> getCustomerProblemReason()
	{
		if ( CollectionUtils.isEmpty( this.customerProblemReason ) )
		{
			this.customerProblemReason = new ArrayList<CustomerProblemReason>();
		}
		return customerProblemReason;
	}

	/**
	 * @param customerProblemReason the customerProblemReason to set
	 */
	public void setCustomerProblemReason( List<CustomerProblemReason> customerProblemReason )
	{
		this.customerProblemReason = customerProblemReason;
	}

}
