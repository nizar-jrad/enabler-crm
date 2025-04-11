package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumTitle;

/**
 * Titulaire du compte de facturation dans le cas d'un particulier
 * 
 * @author NCR
 */
public class Holder
	extends Party
{
	/** Titre du titulaire */
	public EnumTitle title;

	/** Prénom du titulaire */
	public String firstName;

	/** Nom du titulaire */
	public String lastName;

	/** Date de naissance du titulaire */
	public Date birthDate;

	/** Lieu de naissance du titulaire */
	public String birthStateCode;

	/** Lieu de naissance du titulaire */
	public String birthStateName;

	/** Nationalité du titulaire */
	public Object nationality;

	/** Type de piece d'identité du titulaire */
	// public EnumIdentityType identityType;

	/** Numero de la piece d'identité du titulaire */
	public String identityNum;

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

	public String getBirthStateCode()
	{
		return birthStateCode;
	}

	public void setBirthStateCode( String birthStateCode )
	{
		this.birthStateCode = birthStateCode;
	}

	public String getBirthStateName()
	{
		return birthStateName;
	}

	public void setBirthStateName( String birthStateName )
	{
		this.birthStateName = birthStateName;
	}

	public Object getNationality()
	{
		return nationality;
	}

	public void setNationality( Object nationality )
	{
		this.nationality = nationality;
	}

	public String getIdentityNum()
	{
		return identityNum;
	}

	public void setIdentityNum( String identityNum )
	{
		this.identityNum = identityNum;
	}

}
