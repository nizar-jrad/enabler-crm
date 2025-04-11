package com.orangecaraibe.enabler.crm.webservice.criteria;

import java.util.Date;
import java.util.List;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionStatus;
import com.orangecaraibe.enabler.crm.business.enums.InteractionTypeEnum;
import com.orangecaraibe.enabler.crm.business.enums.InteractionWayEnum;
import com.orangecaraibe.enabler.crm.business.enums.MediaEnum;

/**
 * Cette classe encapsule les criteres utilises dans le cadre de recherche d'interaction
 */
public class InteractionCriteria
{

	/** identifiant d'interaction : */
	private String interactionID;

	/** identifiant du Party */
	private String partyID;

	/** identifiant de contrat */
	private String installedContractID;

	/** identifiant du compte de facturation */
	private String billingAccountID;

	/** Filtre sur la periode de recherche */
	private Date fromDate;

	private Date toDate;

	/** Inclusion des demandes rattachees aux interactions */
	private boolean includeCustomerProblems;

	/** Inclusion des resolutionAction rattachees aux interactions */
	private boolean isOnlyResolutionAction;

	/** Inclusion des notes rattachees aux interactions */
	private boolean includeNotes;

	/** Inclusion des pieces jointes aux interactions */
	private boolean includeAttachments;

	/** Filtre sur le media de l'interaction */
	private List<MediaEnum> medias;

	/** Filtre sur le sens de l'interaction */
	private InteractionWayEnum interactionWay;

	/** Filtre sur le statut de l'interaction */
	private List<EnumInteractionStatus> interactionStatusList;

	/** Filtre sur le type d'interaction */
	private List<InteractionTypeEnum> interactionTypes;

	/** limitation du nombre max d'enregistrements */
	private Integer maxResults;

	public String getInteractionID()
	{
		return interactionID;
	}

	public void setInteractionID( String interactionID )
	{
		this.interactionID = interactionID;
	}

	public String getPartyID()
	{
		return partyID;
	}

	public void setPartyID( String partyID )
	{
		this.partyID = partyID;
	}

	public String getInstalledContractID()
	{
		return installedContractID;
	}

	public void setInstalledContractID( String installedContractID )
	{
		this.installedContractID = installedContractID;
	}

	public String getBillingAccountID()
	{
		return billingAccountID;
	}

	public void setBillingAccountID( String billingAccountID )
	{
		this.billingAccountID = billingAccountID;
	}

	public Date getFromDate()
	{
		return fromDate;
	}

	public void setFromDate( Date fromDate )
	{
		this.fromDate = fromDate;
	}

	public Date getToDate()
	{
		return toDate;
	}

	public void setToDate( Date toDate )
	{
		this.toDate = toDate;
	}

	public boolean isIncludeCustomerProblems()
	{
		return includeCustomerProblems;
	}

	public void setIncludeCustomerProblems( boolean includeCustomerProblems )
	{
		this.includeCustomerProblems = includeCustomerProblems;
	}

	public boolean isIncludeNotes()
	{
		return includeNotes;
	}

	public void setIncludeNotes( boolean includeNotes )
	{
		this.includeNotes = includeNotes;
	}

	/**
	 * @return the includeResolutionAction
	 */
	public boolean isOnlyResolutionAction()
	{
		return isOnlyResolutionAction;
	}

	/**
	 * @param isOnlyResolutionAction the includeResolutionAction to set
	 */
	public void setIsOnlyResolutionAction( boolean isOnlyResolutionAction )
	{
		this.isOnlyResolutionAction = isOnlyResolutionAction;
	}

	public boolean isIncludeAttachments()
	{
		return includeAttachments;
	}

	public void setIncludeAttachments( boolean includeAttachments )
	{
		this.includeAttachments = includeAttachments;
	}

	public List<MediaEnum> getMedias()
	{
		return medias;
	}

	public void setMedias( List<MediaEnum> medias )
	{
		this.medias = medias;
	}

	public InteractionWayEnum getInteractionWay()
	{
		return interactionWay;
	}

	public void setInteractionWay( InteractionWayEnum interactionWay )
	{
		this.interactionWay = interactionWay;
	}

	public List<EnumInteractionStatus> getInteractionStatusList()
	{
		return interactionStatusList;
	}

	public void setInteractionStatusList( List<EnumInteractionStatus> interactionStatusList )
	{
		this.interactionStatusList = interactionStatusList;
	}

	public List<InteractionTypeEnum> getInteractionTypes()
	{
		return interactionTypes;
	}

	public void setInteractionTypes( List<InteractionTypeEnum> interactionTypes )
	{
		this.interactionTypes = interactionTypes;
	}

	public Integer getMaxResults()
	{
		return maxResults;
	}

	public void setMaxResults( Integer maxResults )
	{
		this.maxResults = maxResults;
	}

}
