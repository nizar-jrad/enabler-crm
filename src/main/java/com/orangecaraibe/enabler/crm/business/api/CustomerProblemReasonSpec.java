package com.orangecaraibe.enabler.crm.business.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO LDE : Est-ce qu'il faut deplacer ce bean dans le package du modele metier et l'appeler ArbreTheme. Est-ce que ce
 * bean pourrait s'appuyer sur la classe Theme ? The Class CustomerProblemReasonSpec.
 */
// TODO LDE / JDE : vielle classe a supprimer
public class CustomerProblemReasonSpec
	implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The selection. */
	String selection;

	/** The categorie. */
	String categorie;

	/** The url. */
	String url;

	/** The description. */
	String description;

	CustomerProblemReasonSpec parent;

	/** The children. */
	List<CustomerProblemReasonSpec> children = new ArrayList<CustomerProblemReasonSpec>();

	/**
	 * Gets the selection.
	 * 
	 * @return the selection
	 */
	public String getSelection()
	{
		return selection;
	}

	/**
	 * Sets the selection.
	 * 
	 * @param selection the new selection
	 */
	public void setSelection( String selection )
	{
		this.selection = selection;
	}

	/**
	 * Gets the categorie.
	 * 
	 * @return the categorie
	 */
	public String getCategorie()
	{
		return categorie;
	}

	/**
	 * Sets the categorie.
	 * 
	 * @param categorie the new categorie
	 */
	public void setCategorie( String categorie )
	{
		this.categorie = categorie;
	}

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * Sets the url.
	 * 
	 * @param url the new url
	 */
	public void setUrl( String url )
	{
		this.url = url;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description the new description
	 */
	public void setDescription( String description )
	{
		this.description = description;
	}

	public void addChild( CustomerProblemReasonSpec child )
	{
		if ( this.children == null )
		{
			this.children = new ArrayList<CustomerProblemReasonSpec>();
		}
		children.add( child );
	}

	/**
	 * Gets the children.
	 * 
	 * @return the children
	 */
	public List<CustomerProblemReasonSpec> getChildren()
	{
		return children;
	}

	/**
	 * Sets the children.
	 * 
	 * @param children the new children
	 */
	public void setChildren( List<CustomerProblemReasonSpec> children )
	{
		this.children = children;
	}

	public CustomerProblemReasonSpec getParent()
	{
		return parent;
	}

	public void setParent( CustomerProblemReasonSpec parent )
	{
		this.parent = parent;
	}

}
