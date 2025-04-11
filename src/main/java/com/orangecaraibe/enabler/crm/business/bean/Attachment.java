package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;

public class Attachment
{
	private String document;

	private String nom;

	private String type;

	private String guid;

	private String userGuid;

	private String userName;

	private Date dateCreation;

	/** note concernant l'attachement */
	private Note note;

	/**
	 * @return the document
	 */
	public String getDocument()
	{
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument( String document )
	{
		this.document = document;
	}

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
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType( String type )
	{
		this.type = type;
	}

	/**
	 * @return the guid
	 */
	public String getGuid()
	{
		return guid;
	}

	/**
	 * @param guid the guid to set
	 */
	public void setGuid( String guid )
	{
		this.guid = guid;
	}

	/**
	 * @return the userGuid
	 */
	public String getUserGuid()
	{
		return userGuid;
	}

	/**
	 * @param userGuid the userGuid to set
	 */
	public void setUserGuid( String userGuid )
	{
		this.userGuid = userGuid;
	}

	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName( String userName )
	{
		this.userName = userName;
	}

	/**
	 * @return the dateCreation
	 */
	public Date getDateCreation()
	{
		return dateCreation;
	}

	/**
	 * @param dateCreation the dateCreation to set
	 */
	public void setDateCreation( Date dateCreation )
	{
		this.dateCreation = dateCreation;
	}

	/**
	 * @return the note
	 */
	public Note getNote()
	{
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote( Note note )
	{
		this.note = note;
	}

}
