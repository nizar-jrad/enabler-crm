package com.orangecaraibe.enabler.crm.business.bean.enums;

public enum EnumCommercialGestureStatusReason {
	OPENED(1, "Ouverte", "Opened"),
	VALIDATED(2, "Validé", "Validated"),
	REFUSED(100000001, "Refusé", "Refused");

	private int id;

	private String label;

	private String gdmLabel;

	private EnumCommercialGestureStatusReason(int id, String label, String gdmLabel) {

		this.label = label;
		this.id = id;
		this.gdmLabel = gdmLabel;
	}

	/**
	 * @return the id
	 */
	public int getId() {

		return id;
	}

	/**
	 * @param id
	 * 		the id to set
	 */
	public void setId(int id) {

		this.id = id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {

		return label;
	}

	/**
	 * @param label
	 * 		the label to set
	 */
	public void setLabel(String label) {

		this.label = label;
	}

	public String getGdmLabel() {

		return gdmLabel;
	}

	public void setGdmLabel(String gdmLabel) {

		this.gdmLabel = gdmLabel;
	}

	public static EnumCommercialGestureStatusReason getById(int id) {

		for (EnumCommercialGestureStatusReason requestStatusReason : EnumCommercialGestureStatusReason.values()) {
			if (requestStatusReason.getId() == id) {
				return requestStatusReason;
			}
		}
		return null;
	}
}
