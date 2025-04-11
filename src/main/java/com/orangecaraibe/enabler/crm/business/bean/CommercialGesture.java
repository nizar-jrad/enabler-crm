/**
 *
 */
package com.orangecaraibe.enabler.crm.business.bean;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumCommercialGestureStatusReason;

/**
 * @author ncrtest2
 */
public class CommercialGesture {

	private String name;

	private String type;

	private double amount;

	private EnumCommercialGestureStatusReason statusReason;

	public String getName() {

		return name;
	}
	
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {

		return type;
	}

	/**
	 * @param type
	 * 		the type to set
	 */
	public void setType(String type) {

		this.type = type;
	}

	public double getAmount() {

		return amount;
	}

	public void setAmount(double amount) {

		this.amount = amount;
	}

	public EnumCommercialGestureStatusReason getStatusReason() {

		return statusReason;
	}

	public void setStatusReason(EnumCommercialGestureStatusReason statusReason) {

		this.statusReason = statusReason;
	}
}
