/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean.enums;

/**
 * @author ncrtest2
 */
public enum EnumThemeType
{
	ACTION( 100000000, "Action", "" ),
	MD_ENT_COMP( 100000004, "MdEntComp", "Type6" ),
	MD_ENT_DESIM( 100000005, "MdEntDesim", "Type" ),
	MD_ENT_DUPLI( 100000006, "MdEntDupli", "Type4" ),
	MD_ENT_INCON( 100000007, "MdEntIncon", "Type" ),
	MD_ENT_PAT( 100000008, "MdEntJPAT", "Type" ),
	MD_ENT_JUSTI( 100000009, "MdEntJusti", "Type5" ),
	MD_ENT_MODFI( 100000010, "MdEntModif", "Type3" ),
	MD_ENT_RECLA( 100000011, "MdEntRecla", "Type2" ),
	MD_ENT_RESIL( 100000012, "MdEntResil", "Type1" ),
	ISSUE( 100000013, "question", "question" ),
	ANSWER( 100000014, "complaint", "complaint" ),
	MD_ENT_RECOUV( 100000015, "MdEntRecouv", "Type7" ),
	MD_ENT_CONT( 100000016, "MdEntCont", "Type8" ),
	MD_ENT_OPTSUPP( 100000017, "MdEntOptSupp", "Type10" ),
	UNKNOW( 0, "Unconnue", "UNKNOW" )

	;

	private int dcrmId;

	private String dcrmTypeLabel;

	private String sataType;

	private EnumThemeType( int dcrmId, String dcrmTypeLabel, String sataType )
	{
		this.dcrmId = dcrmId;
		this.dcrmTypeLabel = dcrmTypeLabel;
		this.sataType = sataType;

	}

	/**
	 * @return the dcrmId
	 */
	public int getDcrmId()
	{
		return dcrmId;
	}

	/**
	 * @param dcrmId the dcrmId to set
	 */
	public void setDcrmId( int dcrmId )
	{
		this.dcrmId = dcrmId;
	}

	/**
	 * @return the dcrmType
	 */
	public String getDcrmTypeLabel()
	{
		return dcrmTypeLabel;
	}

	/**
	 * @param dcrmType the dcrmType to set
	 */
	public void setDcrmTypeLabel( String dcrmTypeLabel )
	{
		this.dcrmTypeLabel = dcrmTypeLabel;
	}

	/**
	 * @return the sataType
	 */
	public String getSataType()
	{
		return sataType;
	}

	/**
	 * @param sataType the sataType to set
	 */
	public void setSataType( String sataType )
	{
		this.sataType = sataType;
	}

	public static EnumThemeType getByTypeSata( String sataType )
	{
		for ( EnumThemeType typeThemeStatus : EnumThemeType.values() )
		{
			if ( typeThemeStatus.getDcrmTypeLabel().equals( sataType ) )
				return typeThemeStatus;
		}
		return null;
	}

	public static EnumThemeType getByDCRMId( Integer type )
	{
		for ( EnumThemeType typeThemeStatus : EnumThemeType.values() )
		{
			if ( typeThemeStatus.getDcrmId() == type )
				return typeThemeStatus;
		}
		return null;
	}

}
