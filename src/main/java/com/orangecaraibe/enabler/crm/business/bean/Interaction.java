package com.orangecaraibe.enabler.crm.business.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionChannel;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionSocialMediaType;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionStatus;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionWay;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumPriority;

/**
 * Classe des données des interactions
 * 
 * @author NCR
 */
public class Interaction
	implements Comparable<Interaction>
{
	/** Identifiant de l'interaction */
	private String id;

	/** Titre de l'interaction */
	private String title;

	private EnumPriority priority;

	/** Canal de l'interaction (type d'interaction) */
	private EnumInteractionChannel canal;

	/** application (ex : WEBCARE, FEL, PROMPT, ) */
	private String application;

	/** Date de creation de l'interaction */
	private Date creationDate;

	/** Moyen de l'interaction */
	private EnumInteractionWay way;

	/** Status de l'interaction */
	private EnumInteractionStatus status;

	/** Raison du Status de l'interaction */
	private String statusReason;

	/** Liste des demandes attachees */
	private List<Request> requests;

	/** notes attachees a l'interaction */
	private List<Note> notes;

	/** pieces jointes a l'interaction */
	private List<Attachment> attachments;

	/** party titulaire */
	private Holder holder;

	/** party ou/et methodes de contact a l'origine */
	private List<Actor> from;

	/** party ou/et methodes de contact destinataire */
	private List<Actor> to;

	/** equipe ou conseiller en charge de l'interaction */
	private CustomerService customerService;

	/** flag indiquant si l'interaction est une notification */
	private boolean notification;

	/** id du courrier */
	private String courrierId;

	/** Type de l'interaction de reseau sociaux */
	private EnumInteractionSocialMediaType socialMediaType;

	/** Détails de l'interactionBody */
	private String description;

	/** Numéro de téléphone destinataire */
	private String numeroTelephoneAppele;

	/** Numéro de téléphone de l'appelant */
	private String numeroTelephoneAppelant;

	/**
	 * @return the socialMediaType
	 */
	public EnumInteractionSocialMediaType getSocialMediaType()
	{
		return socialMediaType;
	}

	/**
	 * @param socialMediaType the socialMediaType to set
	 */
	public void setSocialMediaType( EnumInteractionSocialMediaType socialMediaType )
	{
		this.socialMediaType = socialMediaType;
	}

	/**
	 * @param id the id to set
	 */
	public void setId( String id )
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public EnumInteractionChannel getCanal()
	{
		return canal;
	}

	public void setCanal( EnumInteractionChannel canal )
	{
		this.canal = canal;
	}

	public String getApplication()
	{
		return application;
	}

	public void setApplication( String application )
	{
		this.application = application;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate( Date creationDate )
	{
		this.creationDate = creationDate;
	}

	public EnumInteractionWay getWay()
	{
		return way;
	}

	public void setWay( EnumInteractionWay way )
	{
		this.way = way;
	}

	public List<Request> getRequests()
	{
		if ( requests == null )
			requests = new ArrayList<Request>();
		return requests;
	}

	public void setRequests( List<Request> requests )
	{
		this.requests = requests;
	}

	public List<Note> getNotes()
	{
		return notes;
	}

	public void setNotes( List<Note> notes )
	{
		this.notes = notes;
	}

	/**
	 * @return the attachments
	 */
	public List<Attachment> getAttachments()
	{
		if ( CollectionUtils.isEmpty( attachments ) )
		{
			attachments = new ArrayList<Attachment>();
		}
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments( List<Attachment> attachments )
	{
		this.attachments = attachments;
	}

	public CustomerService getCustomerService()
	{
		return customerService;
	}

	public void setCustomerService( CustomerService customerService )
	{
		this.customerService = customerService;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle( String title )
	{
		this.title = title;
	}

	public EnumPriority getPriority()
	{
		return priority;
	}

	public void setPriority( EnumPriority priority )
	{
		this.priority = priority;
	}

	public List<Actor> getFrom()
	{
		if ( CollectionUtils.isEmpty( from ) )
		{
			this.from = new ArrayList<Actor>();
		}
		return from;
	}

	public void setFrom( List<Actor> from )
	{
		this.from = from;
	}

	public List<Actor> getTo()
	{
		if ( CollectionUtils.isEmpty( this.to ) )
		{
			this.to = new ArrayList<Actor>();
		}
		return to;
	}

	public void setTo( List<Actor> to )
	{
		this.to = to;
	}

	public Holder getHolder()
	{
		return holder;
	}

	public void setHolder( Holder holder )
	{
		this.holder = holder;
	}

	public EnumInteractionStatus getStatus()
	{
		return status;
	}

	public void setStatus( EnumInteractionStatus status )
	{
		this.status = status;
	}

	public String getStatusReason()
	{
		return statusReason;
	}

	public void setStatusReason( String statusReason )
	{
		this.statusReason = statusReason;
	}

	public boolean isNotification()
	{
		return notification;
	}

	public void setNotification( boolean notification )
	{
		this.notification = notification;
	}

	@Override
	public int compareTo( Interaction o )
	{

		if ( this.creationDate.before( o.creationDate ) )
		{
			return 1;
		}
		else if ( this.creationDate.after( o.creationDate ) )
		{
			return -1;
		}
		return 0;
	}

	@Override
	public boolean equals( Object object )
	{

		if ( object instanceof Interaction )
		{
			Interaction interaction = (Interaction) object;
			if ( interaction.getId().equals( this.id ) )
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	public boolean contains( Object object )
	{

		if ( object instanceof Interaction )
		{
			Interaction interaction = (Interaction) object;
			if ( interaction.getId().equals( this.id ) )
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * @return the courrierId
	 */
	public String getCourrierId()
	{
		return courrierId;
	}

	/**
	 * @param courrierId the courrierId to set
	 */
	public void setCourrierId( String courrierId )
	{
		this.courrierId = courrierId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}

	public String getNumeroTelephoneAppele()
	{
		return numeroTelephoneAppele;
	}

	public void setNumeroTelephoneAppele( String numeroTelephoneAppele )
	{
		this.numeroTelephoneAppele = numeroTelephoneAppele;
	}

	public String getNumeroTelephoneAppelant()
	{
		return numeroTelephoneAppelant;
	}

	public void setNumeroTelephoneAppelant( String numeroTelephoneAppelant )
	{
		this.numeroTelephoneAppelant = numeroTelephoneAppelant;
	}

}
