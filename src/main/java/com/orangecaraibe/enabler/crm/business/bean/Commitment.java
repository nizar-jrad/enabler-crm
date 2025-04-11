package com.orangecaraibe.enabler.crm.business.bean;

import java.util.Date;
import java.util.List;

/**
 * Classe pour les données d'engagement
 * 
 * @author NCR
 */
public class Commitment
{

	/** Date de début d'engagement */
	public Date startDate;

	/** Date de fin d'engagement */
	public Date endDate;

	/** Duréé de l'engagement */
	public int duration;

	/** Mois restant dus */
	public int delayMonth;

	/** Jours restants dus */
	public int delayDay;

	/** Montant des mois restant dus */
	public Double amount;

	/** Montant total des mois restant dus */
	public Double accountAmount;

	/** Cause de réengagement */
	public String reason;

	/** Date de fin d'engagement reelle */
	public Date realEndDate;

	/** Historique des engagements */
	public List<Commitment> CommitmentHistory;

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate( Date startDate )
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate( Date endDate )
	{
		this.endDate = endDate;
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration( int duration )
	{
		this.duration = duration;
	}

	public int getDelayMonth()
	{
		return delayMonth;
	}

	public void setDelayMonth( int delayMonth )
	{
		this.delayMonth = delayMonth;
	}

	public int getDelayDay()
	{
		return delayDay;
	}

	public void setDelayDay( int delayDay )
	{
		this.delayDay = delayDay;
	}

	public Double getAmount()
	{
		return amount;
	}

	public void setAmount( Double amount )
	{
		this.amount = amount;
	}

	public Double getAccountAmount()
	{
		return accountAmount;
	}

	public void setAccountAmount( Double accountAmount )
	{
		this.accountAmount = accountAmount;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason( String reason )
	{
		this.reason = reason;
	}

	public Date getRealEndDate()
	{
		return realEndDate;
	}

	public void setRealEndDate( Date realEndDate )
	{
		this.realEndDate = realEndDate;
	}

	public List<Commitment> getCommitmentHistory()
	{
		return CommitmentHistory;
	}

	public void setCommitmentHistory( List<Commitment> commitmentHistory )
	{
		CommitmentHistory = commitmentHistory;
	}

}
