/**
 * 
 */
package com.orangecaraibe.enabler.crm.dao.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author ncrtest2
 */
@Embeddable
public class ThemePKBean
	implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8726895768616700937L;

	private String theme1;

	private String theme2;

	private String theme3;

	public ThemePKBean()
	{
		super();
	}

	public ThemePKBean( String theme1, String theme2, String theme3 )
	{
		super();
		this.theme1 = theme1;
		this.theme2 = theme2;
		this.theme3 = theme3;
	}

	/**
	 * @return the theme1
	 */
	@Column( name = "theme1" )
	public String getTheme1()
	{
		return theme1;
	}

	/**
	 * @param theme1 the theme1 to set
	 */
	public void setTheme1( String theme1 )
	{
		this.theme1 = theme1;
	}

	/**
	 * @return the theme2
	 */
	@Column( name = "theme2" )
	public String getTheme2()
	{
		return theme2;
	}

	/**
	 * @param theme2 the theme2 to set
	 */
	public void setTheme2( String theme2 )
	{
		this.theme2 = theme2;
	}

	/**
	 * @return the theme3
	 */
	@Column( name = "theme3" )
	public String getTheme3()
	{
		return theme3;
	}

	/**
	 * @param theme3 the theme3 to set
	 */
	public void setTheme3( String theme3 )
	{
		this.theme3 = theme3;
	}

}
