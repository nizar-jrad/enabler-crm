package com.orangecaraibe.enabler.crm.business.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class Theme
{

	public String guid;

	public String label;

	public List<Theme> theme;

	/**
	 * @return the guid
	 */
	public String getGuid()
	{
		return guid;
	}

	/**
	 * @return the theme
	 */
	public List<Theme> getTheme()
	{
		if ( CollectionUtils.isEmpty( theme ) )
		{
			theme = new ArrayList<Theme>();
		}
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme( List<Theme> theme )
	{
		this.theme = theme;
	}

	/**
	 * @param guid the guid to set
	 */
	public void setGuid( String guid )
	{
		this.guid = guid;
	}

	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel( String label )
	{
		this.label = label;
	}
}
