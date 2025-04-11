package com.orangecaraibe.enabler.crm.business.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumCommitmentStatus;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumContractStatus;

/**
 * Classe des données contrat
 * 
 * @author NCR
 */
public class Contract
{

	/** Identifiant du contrat */
	private String coId;

	/** Numero du mobile */
	private String msisdn;

	/*** Libelle de l'offre */
	private String offerLabel;

	/** Tarriff de l'offre */
	private float offerTarif;

	/** Date de debut d'adhesion a l'offre */
	private Date startDate;

	/** Status du contrat */
	private EnumContractStatus status;

	/** Date du dernier statut */
	private Date statusDate;

	/** Raison du statut en cours */
	private String statusReason;

	/** Date de transfert si en cours */
	private Date transfertDate;

	/** Raison du transfert en cours */
	private String transfertReason;

	/** Offre du transfert en cours */
	private String transfertLabel;

	/*** Stut de l'engagement */
	private EnumCommitmentStatus commitmentStatus;

	/** Numero de la SIM */
	private String sim;

	/** Numero IMSI */
	private String imsi;

	/** Code puk 1 */
	private String puk1;

	/** Code puk 2 */
	private String puk2;

	private Boolean isRegisteredWebCare;

	/** Liste des option du contrat */
	private List<Option> options;

	/** Utilisateur de la ligne */
	private User user;

	/** Infos engagement */
	private Commitment commitment;

	/** rio du numero mobile */
	private String rio;

	/** Mobile du contrat */
	private CellPhone cellPhone;

	/** Segment */
	private String segment;

	/** Produit du contrat */
	private String product;

	/** Le contrat est de type DATA only */
	private boolean data;

	public String getCoId()
	{
		return coId;
	}

	public void setCoId( String coId )
	{
		this.coId = coId;
	}

	public String getMsisdn()
	{
		return msisdn;
	}

	public void setMsisdn( String msisdn )
	{
		this.msisdn = msisdn;
	}

	public String getOfferLabel()
	{
		return offerLabel;
	}

	public void setOfferLabel( String offerLabel )
	{
		this.offerLabel = offerLabel;
	}

	public float getOfferTarif()
	{
		return offerTarif;
	}

	public void setOfferTarif( float offerTarif )
	{
		this.offerTarif = offerTarif;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate( Date startDate )
	{
		this.startDate = startDate;
	}

	public EnumContractStatus getStatus()
	{
		return status;
	}

	public void setStatus( EnumContractStatus status )
	{
		this.status = status;
	}

	public Date getStatusDate()
	{
		return statusDate;
	}

	public void setStatusDate( Date statusDate )
	{
		this.statusDate = statusDate;
	}

	public String getStatusReason()
	{
		return statusReason;
	}

	public void setStatusReason( String statusReason )
	{
		this.statusReason = statusReason;
	}

	public Date getTransfertDate()
	{
		return transfertDate;
	}

	public void setTransfertDate( Date transfertDate )
	{
		this.transfertDate = transfertDate;
	}

	public String getTransfertReason()
	{
		return transfertReason;
	}

	public void setTransfertReason( String transfertReason )
	{
		this.transfertReason = transfertReason;
	}

	public String getTransfertLabel()
	{
		return transfertLabel;
	}

	public void setTransfertLabel( String transfertLabel )
	{
		this.transfertLabel = transfertLabel;
	}

	public EnumCommitmentStatus getCommitmentStatus()
	{
		return commitmentStatus;
	}

	public void setCommitmentStatus( EnumCommitmentStatus commitmentStatus )
	{
		this.commitmentStatus = commitmentStatus;
	}

	public String getSim()
	{
		return sim;
	}

	public void setSim( String sim )
	{
		this.sim = sim;
	}

	public String getImsi()
	{
		return imsi;
	}

	public void setImsi( String imsi )
	{
		this.imsi = imsi;
	}

	public String getPuk1()
	{
		return puk1;
	}

	public void setPuk1( String puk1 )
	{
		this.puk1 = puk1;
	}

	public String getPuk2()
	{
		return puk2;
	}

	public void setPuk2( String puk2 )
	{
		this.puk2 = puk2;
	}

	public Boolean getIsRegisteredWebCare()
	{
		return isRegisteredWebCare;
	}

	public void setIsRegisteredWebCare( Boolean isRegisteredWebCare )
	{
		this.isRegisteredWebCare = isRegisteredWebCare;
	}

	public List<Option> getOptions()
	{
		if ( options == null )
			options = new ArrayList<Option>();

		return options;
	}

	public void setOptions( List<Option> options )
	{
		this.options = options;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser( User user )
	{
		this.user = user;
	}

	public Commitment getCommitment()
	{
		return commitment;
	}

	public void setCommitment( Commitment commitment )
	{
		this.commitment = commitment;
	}

	public String getRio()
	{
		return rio;
	}

	public void setRio( String rio )
	{
		this.rio = rio;
	}

	public CellPhone getCellPhone()
	{
		if ( cellPhone == null )
		{
			cellPhone = new CellPhone();
		}
		return cellPhone;
	}

	public void setCellPhone( CellPhone cellPhone )
	{
		this.cellPhone = cellPhone;
	}

	/**
	 * @return the segment
	 */
	public String getSegment()
	{
		return segment;
	}

	/**
	 * @param segment the segment to set
	 */
	public void setSegment( String segment )
	{
		this.segment = segment;
	}

	/**
	 * @return the product
	 */
	public String getProduct()
	{
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct( String product )
	{
		this.product = product;
	}

	/**
	 * @return the data
	 */
	public boolean isData()
	{
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData( boolean data )
	{
		this.data = data;
	}

}
