package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;

/**
 * Classe des données du manager de l'entreprise
 * 
 * @author NCR
 */
public class Manager
{
	/** Prenom du manager */
	public String firstName;

	/** Nom du manager */
	public String lastName;

	/** Telephone du manager */
	public String phone;

	/** Date de naissance du manager */
	public Date birthDate;

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

	public String getPhone()
	{
		return phone;
	}

	public void setPhone( String phone )
	{
		this.phone = phone;
	}

	public Date getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate( Date birthDate )
	{
		this.birthDate = birthDate;
	}

}
