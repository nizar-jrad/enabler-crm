/**
 *
 */
package com.orangecaraibe.enabler.ussd.business.beans;

/**
 * @author sgodard Bean contenant toutes les propriétés sous forme de chaînes de caractères
 */
public class FormatedPortabilite
{
	String nom;

	String prenom;

	String dateDebut;

	String dateFin;

	String rio;

	String typeClient;

	String denomination;

	/**
	 * @return the nom
	 */
	public String getNom()
	{
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom( String nom )
	{
		this.nom = nom;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom()
	{
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom( String prenom )
	{
		this.prenom = prenom;
	}

	/**
	 * @return the dateDebut
	 */
	public String getDateDebut()
	{
		return dateDebut;
	}

	/**
	 * @param dateDebut the dateDebut to set
	 */
	public void setDateDebut( String dateDebut )
	{
		this.dateDebut = dateDebut;
	}

	/**
	 * @return the dateFin
	 */
	public String getDateFin()
	{
		return dateFin;
	}

	/**
	 * @param dateFin the dateFin to set
	 */
	public void setDateFin( String dateFin )
	{
		this.dateFin = dateFin;
	}

	/**
	 * @return the rio
	 */
	public String getRio()
	{
		return rio;
	}

	/**
	 * @param rio the rio to set
	 */
	public void setRio( String rio )
	{
		this.rio = rio;
	}

	/**
	 * @return the typeClient
	 */
	public String getTypeClient()
	{
		return typeClient;
	}

	/**
	 * @param typeClient the typeClient to set
	 */
	public void setTypeClient( String typeClient )
	{
		this.typeClient = typeClient;
	}

	/**
	 * @return the denomination
	 */
	public String getDenomination()
	{
		return denomination;
	}

	/**
	 * @param denomination the denomination to set
	 */
	public void setDenomination( String denomination )
	{
		this.denomination = denomination;
	}

}
