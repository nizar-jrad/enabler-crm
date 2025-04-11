package com.orangecaraibe.enabler.svi.business.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sgodard Bean contenant toutes les propriétés sous forme de chaînes de caractères
 */
public class FormattedPortabilite
{
	@JsonIgnore
	String nom;

	@JsonIgnore
	String prenom;

	@JsonProperty("commitmentDateStart")
	String dateDebut;
	
	@JsonProperty("commitmentDateEnd")
	String dateFin;
	
	@JsonProperty("RIO")
	String rio;

	@JsonIgnore
	String typeClient;

	@JsonIgnore
	String denomination;
	
	@JsonProperty("SMS")
	String sms;

	@JsonProperty("StatusCode")
	String statusCode;
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

	public String getSms() {
		return sms;
	}

	public void setSms(String message) {
		this.sms = message;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String status) {
		this.statusCode = status;
	}

	
}
