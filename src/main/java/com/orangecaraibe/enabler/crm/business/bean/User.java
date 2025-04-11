package com.orangecaraibe.enabler.crm.business.bean;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumTitle;

/**
 * Classe des données de l'utilisateur de la ligne
 * 
 * @author NCR
 */
public class User
{

	/** Titre de l'utilisateur */
	private EnumTitle title;

	/** Prenom de l'utilisateur */
	private String firstName;

	/** Nom de l'utilisateur */
	private String lastName;

	/** Email de l'utilisateur */
	private String email;

	public EnumTitle getTitle()
	{
		return title;
	}

	public void setTitle( EnumTitle title )
	{
		title = title;
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

	public String getEmail()
	{
		return email;
	}

	public void setEmail( String email )
	{
		this.email = email;
	}

}
