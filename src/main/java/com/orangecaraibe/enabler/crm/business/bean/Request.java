package com.orangecaraibe.enabler.crm.business.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumCommunicationChannel;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumPriority;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumRequestStatus;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumRequestStatusReason;

/**
 * Classe regroupant les données d'une demande
 * 
 * @author NCR
 */
public class Request
{

	/** guid de la demande */
	private String guid;

	/** Identifiant de la demande */
	private String id;

	/** Theme 1 de la demande */
	private Theme theme;

	/** Type de la demande */
	private String type;

	/** Derniere note de la demande */
	private String lastNote;

	/** Statut de la demande */
	private EnumRequestStatus status;

	/** Equipe en charge de la demande */
	private String team;

	/** Interactions liées a la demande */
	private List<Interaction> interactions;

	/** Date de création de la demande */
	private Date creationDate;

	/** Date de création de la demande */
	private Date closeDate;

	/** titre de la demande */
	private String title;

	/** description de la demande */
	private String description;

	/** mots clé de la demande */
	private String keyWord;

	/** priorité de la demande */
	private EnumPriority priority;

	/** account associe a la demande */
	private Account account;

	/** contract associe a la demande */
	private Contract contract;

	/** application d'origine */
	private String channel;

	/** canal de communication demande par le client */
	private EnumCommunicationChannel communicationChannel;

	/** identifiant du ticket oceane de la demande */
	private String troubleTicketID;

	/** note attachee a la demande */
	private List<Note> notes;

	private List<Attachment> attachments;

	/** */
	private RetentionOffer retentionOffer;

	/** equipe ou conseiller en charge de l'interaction */
	private CustomerService customerService;

	private Refund refund;

	private CommercialGesture commercialGesture;

	private Recovery recovery;

	private LegalStatus legalStatus;

	private Option option;

	private EnumRequestStatusReason statusReason;

	private Holder holder;

	private String equipeServicelabel;

	private String equipeServiceCode;

	private String AgreementNumber;

	private String AgreementDate;

	/**
	 * mesure financière
	 */
	private com.orangecaraibe.enabler.crm.business.bean.ResolutionAction resolutionAction;

	/** commande associee a la demande */
	private Order order;

	private List<ChildRequest> demandesFilles;

	/** utilisateurs */
	private Actor actor;

	public Actor getActor()
	{
		return actor;
	}

	public void setActor( Actor actor )
	{
		this.actor = actor;
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

	public String getType()
	{
		return type;
	}

	public void setType( String type )
	{
		this.type = type;
	}

	public String getLastNote()
	{
		return lastNote;
	}

	public void setLastNote( String lastNote )
	{
		this.lastNote = lastNote;
	}

	public EnumRequestStatus getStatus()
	{
		return status;
	}

	public void setStatus( EnumRequestStatus status )
	{
		this.status = status;
	}

	public String getTeam()
	{
		return team;
	}

	public void setTeam( String team )
	{
		this.team = team;
	}

	public List<Interaction> getInteractions()
	{
		if ( CollectionUtils.isEmpty( interactions ) )
			interactions = new ArrayList<Interaction>();
		return interactions;
	}

	public void setInteractions( List<Interaction> interactions )
	{
		this.interactions = interactions;
	}

	public String getId()
	{
		return id;
	}

	public void setId( String id )
	{
		this.id = id;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate( Date creationDate )
	{
		this.creationDate = creationDate;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle( String title )
	{
		this.title = title;
	}

	/**
	 * @return the theme
	 */
	public Theme getTheme()
	{
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme( Theme theme )
	{
		this.theme = theme;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription( String description )
	{
		this.description = description;
	}

	/**
	 * @return the keyWord
	 */
	public String getKeyWord()
	{
		return keyWord;
	}

	/**
	 * @param keyWord the keyWord to set
	 */
	public void setKeyWord( String keyWord )
	{
		this.keyWord = keyWord;
	}

	/**
	 * @return the priority
	 */
	public EnumPriority getPriority()
	{
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority( EnumPriority priority )
	{
		this.priority = priority;
	}

	/**
	 * @return the account
	 */
	public Account getAccount()
	{
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount( Account account )
	{
		this.account = account;
	}

	/**
	 * @return the contract
	 */
	public Contract getContract()
	{
		if ( contract == null )
		{
			contract = new Contract();
		}
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract( Contract contract )
	{
		this.contract = contract;
	}

	/**
	 * @return the channel
	 */
	public String getChannel()
	{
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel( String channel )
	{
		this.channel = channel;
	}

	/**
	 * @return the communicationChannel
	 */
	public EnumCommunicationChannel getCommunicationChannel()
	{
		return communicationChannel;
	}

	/**
	 * @param communicationChannel the communicationChannel to set
	 */
	public void setCommunicationChannel( EnumCommunicationChannel communicationChannel )
	{
		this.communicationChannel = communicationChannel;
	}

	/**
	 * @return the troubleTicketID
	 */
	public String getTroubleTicketID()
	{
		return troubleTicketID;
	}

	/**
	 * @param troubleTicketID the troubleTicketID to set
	 */
	public void setTroubleTicketID( String troubleTicketID )
	{
		this.troubleTicketID = troubleTicketID;
	}

	/**
	 * @return the notes
	 */
	public List<Note> getNotes()
	{
		if ( this.notes == null )
		{
			this.notes = new ArrayList<Note>();
		}
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
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

	/**
	 * @return the retentionOffer
	 */
	public RetentionOffer getRetentionOffer()
	{
		return retentionOffer;
	}

	/**
	 * @param retentionOffer the retentionOffer to set
	 */
	public void setRetentionOffer( RetentionOffer retentionOffer )
	{
		this.retentionOffer = retentionOffer;
	}

	public CustomerService getCustomerService()
	{
		return customerService;
	}

	public void setCustomerService( CustomerService customerService )
	{
		this.customerService = customerService;
	}

	/**
	 * @return the refund
	 */
	public Refund getRefund()
	{
		return refund;
	}

	/**
	 * @param refund the refund to set
	 */
	public void setRefund( Refund refund )
	{
		this.refund = refund;
	}

	/**
	 * @return the commercialGesture
	 */
	public CommercialGesture getCommercialGesture()
	{
		return commercialGesture;
	}

	/**
	 * @param commercialGesture the commercialGesture to set
	 */
	public void setCommercialGesture( CommercialGesture commercialGesture )
	{
		this.commercialGesture = commercialGesture;
	}

	/**
	 * @return the recovery
	 */
	public Recovery getRecovery()
	{
		return recovery;
	}

	/**
	 * @param recovery the recovery to set
	 */
	public void setRecovery( Recovery recovery )
	{
		this.recovery = recovery;
	}

	/**
	 * @return the legalStatus
	 */
	public LegalStatus getLegalStatus()
	{
		return legalStatus;
	}

	/**
	 * @param legalStatus the legalStatus to set
	 */
	public void setLegalStatus( LegalStatus legalStatus )
	{
		this.legalStatus = legalStatus;
	}

	/**
	 * @return the option
	 */
	public Option getOption()
	{
		return option;
	}

	/**
	 * @param option the option to set
	 */
	public void setOption( Option option )
	{
		this.option = option;
	}

	/**
	 * @return the getStatusReason
	 */
	public EnumRequestStatusReason getStatusReason()
	{
		return statusReason;
	}

	/**
	 * @param statusReason the statusReason to set
	 */
	public void setStatusReason( EnumRequestStatusReason statusReason )
	{
		this.statusReason = statusReason;
	}

	/**
	 * @return the holder
	 */
	public Holder getHolder()
	{
		return holder;
	}

	/**
	 * @param holder the holder to set
	 */
	public void setHolder( Holder holder )
	{
		this.holder = holder;
	}

	/**
	 * @return the closeDate
	 */
	public Date getCloseDate()
	{
		return closeDate;
	}

	/**
	 * @param closeDate the closeDate to set
	 */
	public void setCloseDate( Date closeDate )
	{
		this.closeDate = closeDate;
	}

	/**
	 * @return the equipeServicelabel
	 */
	public String getEquipeServicelabel()
	{
		return equipeServicelabel;
	}

	/**
	 * @param equipeServicelabel the equipeServicelabel to set
	 */
	public void setEquipeServicelabel( String equipeServicelabel )
	{
		this.equipeServicelabel = equipeServicelabel;
	}

	/**
	 * @return the equipeServiceCode
	 */
	public String getEquipeServiceCode()
	{
		return equipeServiceCode;
	}

	/**
	 * @param equipeServiceCode the equipeServiceCode to set
	 */
	public void setEquipeServiceCode( String equipeServiceCode )
	{
		this.equipeServiceCode = equipeServiceCode;
	}

	public String getAgreementDate()
	{
		return AgreementDate;
	}

	public void setAgreementDate( String agreementDate )
	{
		AgreementDate = agreementDate;
	}

	public String getAgreementNumber()
	{
		return AgreementNumber;
	}

	public void setAgreementNumber( String agreementNumber )
	{
		AgreementNumber = agreementNumber;
	}

	public Order getOrder()
	{
		return order;
	}

	public void setOrder( Order order )
	{
		this.order = order;
	}

	public List<ChildRequest> getDemandesFilles()
	{
		if ( demandesFilles == null )
		{
			this.demandesFilles = new ArrayList<ChildRequest>();
		}
		return demandesFilles;
	}

	public void setDemandesFilles( List<ChildRequest> demandesFilles )
	{
		this.demandesFilles = demandesFilles;
	}

	/**
	 * @return the resolutionAction
	 */
	public com.orangecaraibe.enabler.crm.business.bean.ResolutionAction getResolutionAction()
	{
		return resolutionAction;
	}

	/**
	 * @param resolutionAction the resolutionAction to set
	 */
	public void setResolutionAction( com.orangecaraibe.enabler.crm.business.bean.ResolutionAction resolutionAction )
	{
		this.resolutionAction = resolutionAction;
	}

}
