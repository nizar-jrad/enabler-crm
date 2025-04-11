/**
 *
 */
package com.orangecaraibe.enabler.crm.business.bean;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumRetentionStatus;

/**
 * Classe des données de retention
 *
 * @author NCR
 */
public class RetentionOffer {

	/** Code de l'offre de retention */
	public String Code;

	/** Intitulé de l'offre de retention */
	public String Label;

	/** status de l'offre de retention */
	public EnumRetentionStatus Status;

	private double amount;

	public String getCode() {

		return Code;
	}

	public void setCode(String code) {

		Code = code;
	}

	public String getLabel() {

		return Label;
	}

	public void setLabel(String label) {

		Label = label;
	}

	public double getAmount() {

		return amount;
	}

	public void setAmount(double amount) {

		this.amount = amount;
	}
}
