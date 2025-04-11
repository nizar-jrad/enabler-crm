package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumTitle;

/**
 * Classe des données du payeur
 * 
 * @author NCR
 */
public class Payer
{

	/** identifiant */
	public String id;

	/** Titre du payer */
	public EnumTitle title;

	/** Prenom du payer */
	public String firstName;

	/** Nom du payer */
	public String lastName;

	/** Date de naissance du payer */
	public Date birthDate;

	/** Adresse du payer */
	public Address address;

	public String getId()
	{
		return id;
	}

	public void setId( String id )
	{
		this.id = id;
	}

	public EnumTitle getTitle()
	{
		return title;
	}

	public void setTitle( EnumTitle title )
	{
		this.title = title;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName( String firstName )
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName( String lastName )
	{
		this.lastName = lastName;
	}

	public Date getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate( Date birthDate )
	{
		this.birthDate = birthDate;
	}

	public Address getAddress()
	{
		return address;
	}

	public void setAddress( Address address )
	{
		this.address = address;
	}

}
