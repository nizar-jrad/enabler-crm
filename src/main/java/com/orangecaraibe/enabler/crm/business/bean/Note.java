/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;

/**
 * Classe pour les notes des interactions et demande
 * 
 * @author NCR
 */
public class Note
{

	private String note;

	private String guid;

	private String subject;

	private String userGuid;

	private String userName;

	private Date dateCreation;

	/**
	 * @return the note
	 */
	public String getNote()
	{
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote( String note )
	{
		this.note = note;
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
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject( String subject )
	{
		this.subject = subject;
	}
}
