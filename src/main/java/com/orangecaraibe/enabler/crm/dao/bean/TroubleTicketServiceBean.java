/**
 * 
 */
package com.orangecaraibe.enabler.crm.dao.bean;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author ncrtest2
 */
@Entity
@Table( name = "THEME_TO_SERVICE", schema = "ADM_OCEANE" )
public class TroubleTicketServiceBean
{

	private ThemePKBean themePkBean;

	private String service;

	/**
	 * @return the service
	 */
	@Column( name = "SERVICE" )
	public String getService()
	{
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService( String service )
	{
		this.service = service;
	}

	/**
	 * @return the themePkBean
	 */
	@EmbeddedId
	public ThemePKBean getThemePkBean()
	{
		return themePkBean;
	}

	public TroubleTicketServiceBean()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param themePkBean the themePkBean to set
	 */
	public void setThemePkBean( ThemePKBean themePkBean )
	{
		this.themePkBean = themePkBean;
	}

}
