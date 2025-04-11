package com.orangecaraibe.enabler.crm.business.bean.enums;

public enum EnumTypeCampagne
{

	COMMERCIAL( "Commerciale", "Commerciale" ), TECHNICAL( "", "" ), INFORMATION( "Information", "Information" );

	private String dcrmValue;

	private String gdmValue;

	private EnumTypeCampagne( String dcrmValue, String gdmValue )
	{
		this.dcrmValue = dcrmValue;
		this.gdmValue = gdmValue;
	}

	public static EnumTypeCampagne getFromDcrmValue( String dcrmValue )
	{

		for ( EnumTypeCampagne campagne : EnumTypeCampagne.values() )
		{
			if ( campagne.getDcrmValue().equals( dcrmValue ) )
			{
				return campagne;
			}
		}

		return null;
	}

	public String getDcrmValue()
	{
		return dcrmValue;
	}

	public void setDcrmValue( String dcrmValue )
	{
		this.dcrmValue = dcrmValue;
	}

	public String getGdmValue()
	{
		return gdmValue;
	}

	public void setGdmValue( String gdmValue )
	{
		this.gdmValue = gdmValue;
	}

}
