package com.orangecaraibe.enabler.crm.business.bean.enums;

public enum EnumRequestStatusReason
{
	OPENED( 100000000, "Ouverte", "Opened" ),
	REQUALIFY( 100000001, "A requalifier", "Requalify" ),
	// WAITING_CODE( 100000002, "En attente de code", "WaitingCode" ),
	// AVAILABLE_CODE( 100000003, "Code diponible", "AvailableCode" ),
	WAITING_CONSENT( 100000004, "Attente de consentement", "WaitingConsent" ),
	CONSENT_OK( 100000005, "Consentement OK", "Consent" ),
	// WAIT_FATHER_OK( 100000006, " En attente résolution de la Demande mère", "WaitFather" ),
	// MANAGER_VALIDATED( 100000007, "Validée par le manager", "ManagerValidated" ),
	// WAIT_RESOLVE( 100000008, "En attente de résolution", "WaitResolve" ),
	OPENED_INCIDENT_RESOLVED( 100000009, "Incident résolu", "OpenedIncidentResolved" ),
	WAITING_APPROVAL( 100000010, "En attente d'approbation", "WaitingApproval" ),
	REFUND_VALIDATED( 100000011, "Avoir/Remboursement validé", "RefundValidated" ),
	REFUND_VALIDATION_IN_PROGRESS( 100000012, "Avoir/Remboursement en cours de validation",
		"RefundValidationInProgress" ),
	REFUND_REFUSED( 100000013, "Avoir/Remboursement refusé", "RefundRefused" ),
	COMMERCIAL_GESTURE_VALIDATED( 100000014, "Geste Commercial validé", "CommercialGestureValidated" ),
	COMMERCIAL_GESTURE_NOT_VALIDATED( 100000015, "Geste Commercial non validé", "CommercialGestureNotValidated" ),
	TRANSFERED( 100000016, "Transférée", "Transfered" ),
	WAITING( 100000017, "En attente", "Waiting" ),
	CLOSED( 100000018, "Traitée", "Closed" ),
	// APPLE_TRANFERT( 100000019, "Transférée à Apple", "AppelTransfert" ),
	NOT_INTERESTED( 100000020, "Client non intéressé", "NotInterested" ),
	VALIDATED( 100000021, "Validé", "Validated" ),
	REFUSED( 100000022, "Refusée", "Refused" ),
	// TO_RESOLVED( 100000023, "TO résolu", "ToResolved" ),
	INCIDENT_RESOLVED( 100000024, "Incident résolu", "IncidentResolved" ),
	CLIENT_RETAINED( 100000025, "Client retenu", "ClientRetained" ),
	ABANDONED( 100000026, "Abandon demande", "Abandoned" ),
	PAYMENT_CANCELLATION( 100000027, "Abandon paiement", "PaymentCancellation" ),
	REFUSED_CANCELLED( 100000028, "Refusé", "RefusedCancelled" ),
	NOT_JUSTIFIED( 100000029, "Non Fondée", "NotJustified" ),
	CLIENT_NOT_RETAINED( 100000030, "Client non retenue", "ClientNotRetained" );
	// COMPLETUDE_WITH_MODIFICATION( 100000031, "Avec modification", "CompletudeWithModification" ),
	// COMPLETUDE_WITHOUT_MODIFICATION( 100000032, "Sans modification", "CompletudeWithoutModification" );

	private int id;

	private String label;

	private String gmdLabel;

	private EnumRequestStatusReason( int id, String label, String gmdLabel )
	{
		this.label = label;
		this.id = id;
		this.gmdLabel = gmdLabel;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId( int id )
	{
		this.id = id;
	}

	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel( String label )
	{
		this.label = label;
	}

	/**
	 * Revoie le statut correspondant au gmd
	 * 
	 * @param gmd
	 * @return
	 */
	public static EnumRequestStatusReason getBygmdLabel( String gmd )
	{
		for ( EnumRequestStatusReason requestStatus : EnumRequestStatusReason.values() )
		{
			if ( requestStatus.getGmdLabel().equals( gmd ) )
				return requestStatus;
		}
		return null;
	}

	public static EnumRequestStatusReason getById( int id )
	{

		for ( EnumRequestStatusReason requestStatusReason : EnumRequestStatusReason.values() )
		{
			if ( requestStatusReason.getId() == id )
			{
				return requestStatusReason;
			}
		}
		return null;

	}

	/**
	 * @return the gmdLabel
	 */
	public String getGmdLabel()
	{
		return gmdLabel;
	}

	/**
	 * @param gmdLabel the gmdLabel to set
	 */
	public void setGmdLabel( String gmdLabel )
	{
		this.gmdLabel = gmdLabel;
	}
}
