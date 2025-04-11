package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumGuardianType;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumPaymentType;

/**
 * Classe des donn�es du compte de facturation
 * 
 * @author NCR
 */
public class Account
{

	/** Identifiant du compte */
	private String accountId;

	/** Identifiant du compte */
	private String custCode;

	/** Segment du compte */
	private String segment;

	/** Solde du compte */
	private float balance;

	/** Jour de facturation du compte */
	private int billDay;

	/** type de paiement */
	private EnumPaymentType paymentType;

	/** Mot de passe du compte */
	private String password;

	/** Montant de la derni�re facture */
	private float lastInvoiceAmount;

	/** Date de la derniere facture */
	private Date lastInvoiceDate;

	/** Indicateur de mise sous responsabilit� */
	private EnumGuardianType guardianType;

	/** Label de la TVA */
	private String tvaLabel;

	/** Montant de la TVA */
	private String tvaValue;

	/** Payeur du compte */
	private Payer payer;

	/** Titulaire du compte */
	private Holder holder;

	/** informations banquaire */
	private BankDetails bankDetails;

	/** Adresse du compte de facturation */
	private Address address;

	/** manager du compte */
	private Manager manager;

	/** Segment BSCS du compte */
	private String segmentBscs;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId( String accountId )
	{
		this.accountId = accountId;
	}

	public String getCustCode()
	{
		return custCode;
	}

	public void setCustCode( String custCode )
	{
		this.custCode = custCode;
	}

	public String getSegment()
	{
		return segment;
	}

	public void setSegment( String segment )
	{
		this.segment = segment;
	}

	public float getBalance()
	{
		return balance;
	}

	public void setBalance( float balance )
	{
		this.balance = balance;
	}

	public int getBillDay()
	{
		return billDay;
	}

	public void setBillDay( int billDay )
	{
		this.billDay = billDay;
	}

	public EnumPaymentType getPaymentType()
	{
		return paymentType;
	}

	public void setPaymentType( EnumPaymentType paymentType )
	{
		this.paymentType = paymentType;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword( String password )
	{
		this.password = password;
	}

	public float getLastInvoiceAmount()
	{
		return lastInvoiceAmount;
	}

	public void setLastInvoiceAmount( float lastInvoiceAmount )
	{
		this.lastInvoiceAmount = lastInvoiceAmount;
	}

	public Date getLastInvoiceDate()
	{
		return lastInvoiceDate;
	}

	public void setLastInvoiceDate( Date lastInvoiceDate )
	{
		this.lastInvoiceDate = lastInvoiceDate;
	}

	public EnumGuardianType getGuardianType()
	{
		return guardianType;
	}

	public void setGuardianType( EnumGuardianType guardianType )
	{
		this.guardianType = guardianType;
	}

	public String getTvaLabel()
	{
		return tvaLabel;
	}

	public void setTvaLabel( String tvaLabel )
	{
		this.tvaLabel = tvaLabel;
	}

	public String getTvaValue()
	{
		return tvaValue;
	}

	public void setTvaValue( String tvaValue )
	{
		this.tvaValue = tvaValue;
	}

	public Payer getPayer()
	{
		return payer;
	}

	public void setPayer( Payer payer )
	{
		this.payer = payer;
	}

	public Holder getHolder()
	{
		return holder;
	}

	public void setHolder( Holder holder )
	{
		this.holder = holder;
	}

	public BankDetails getBankDetails()
	{
		return bankDetails;
	}

	public void setBankDetails( BankDetails bankDetails )
	{
		this.bankDetails = bankDetails;
	}

	public Address getAddress()
	{
		return address;
	}

	public void setAddress( Address address )
	{
		this.address = address;
	}

	public Manager getManager()
	{
		return manager;
	}

	public void setManager( Manager manager )
	{
		this.manager = manager;
	}

	public String getSegmentBscs()
	{
		return segmentBscs;
	}

	public void setSegmentBscs( String segmentBscs )
	{
		this.segmentBscs = segmentBscs;
	}

}
