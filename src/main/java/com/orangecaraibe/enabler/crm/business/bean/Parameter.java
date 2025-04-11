/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumParameterType;

/**
 * @author NCR
 */
public class Parameter
{
	/** Code du parametre */
	private String code;

	/** Intitulé du parametre */
	private String label;

	/** Valeur du parametre */
	private String value;

	/** Type du parametre */
	private EnumParameterType type;

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode( String code )
	{
		this.code = code;
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

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue( String value )
	{
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public EnumParameterType getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType( EnumParameterType type )
	{
		this.type = type;
	}

}
