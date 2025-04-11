/**
 * 
 */
package com.orangecaraibe.enabler.crm.business.bean;

/**
 * @author bdavid Résolution action de la couche Business
 */
public class ResolutionAction
{

	/**
	 * description de la mesure financière sera remontée par la description de la résolution action
	 */
	private String actionDescription;

	/**
	 * @return the actionDescription
	 */
	public String getActionDescription()
	{
		return actionDescription;
	}

	/**
	 * @param actionDescription the actionDescription to set
	 */
	public void setActionDescription( String actionDescription )
	{
		this.actionDescription = actionDescription;
	}

	/**
	 * @return the actionResult
	 */
	public String getActionResult()
	{
		return actionResult;
	}

	/**
	 * @param actionResult the actionResult to set
	 */
	public void setActionResult( String actionResult )
	{
		this.actionResult = actionResult;
	}

	/**
	 * @return the localActionType
	 */
	public String getLocalActionType()
	{
		return localActionType;
	}

	/**
	 * @param localActionType the localActionType to set
	 */
	public void setLocalActionType( String localActionType )
	{
		this.localActionType = localActionType;
	}

	/**
	 * type de mesure financière sera remontée par l’action de résolution
	 */
	private String actionResult;

	/**
	 * permet d’identifier qu’une ResolutionAction est une mesure financière
	 */
	private String localActionType;
}
