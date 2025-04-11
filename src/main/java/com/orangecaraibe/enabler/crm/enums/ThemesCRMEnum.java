package com.orangecaraibe.enabler.crm.enums;

public enum ThemesCRMEnum
{
	THEME1_ACTE_COMMERCIAL( "Acte commercial" ), THEME1_GESTION_CLIENT( "Gestion client" ), THEME2_HOMOLOGATION(
		"Homologation" ), THEME2_LIQUIDATION( "Liquidation Judiciaire" ), THEME3_PRE_IDENTIFICATION(
		"Pré-identification" ), THEME3_IDENTIFICATION( "Identification" );

	private String themeLabel;

	public String getThemeLabel()
	{
		return themeLabel;
	}

	private ThemesCRMEnum( String themeLabel )
	{
		this.themeLabel = themeLabel;
	}

}
