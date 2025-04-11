package com.orangecaraibe.enabler.crm.business.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.crm.business.bean.Interaction;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.webservice.criteria.InteractionCriteria;

@Component
public class FilterInteractionHelper
{
	public Interaction filterInteractionRequest( Interaction interaction, InteractionCriteria interactionCriteria )
	{
		List<Request> requests = interaction.getRequests();
		for ( Request request : requests )
		{
			if ( interactionCriteria.getBillingAccountID() != null
				&& interactionCriteria.getInstalledContractID() == null )
			{
				if ( request.getAccount() != null && request.getAccount().getAccountId() != null
					&& request.getAccount().getAccountId().equals( interactionCriteria.getBillingAccountID() ) )
				{
					return interaction;
				}
			}
			else if ( interactionCriteria.getBillingAccountID() == null
				&& interactionCriteria.getInstalledContractID() != null )
			{
				if ( request.getContract() != null && request.getContract().getCoId() != null
					&& request.getContract().getCoId().equals( interactionCriteria.getInstalledContractID() ) )
				{
					return interaction;
				}
			}
			else if ( interactionCriteria.getBillingAccountID() != null
				&& interactionCriteria.getInstalledContractID() != null )
			{
				if ( request.getAccount() != null && request.getContract().getCoId() != null
					&& request.getContract().getCoId().equals( interactionCriteria.getInstalledContractID() )
					&& request.getAccount().getAccountId() != null
					&& request.getAccount().getAccountId().equals( interactionCriteria.getBillingAccountID() ) )
				{
					return interaction;
				}
			}
			else if ( request.getAccount() == null && request.getContract().getCoId() != null )
			{
				if ( request.getContract().getCoId().equals( interactionCriteria.getInstalledContractID() ) )
				{
					return interaction;
				}
			}
			else if ( request.getAccount() != null )
			{
				if ( request.getAccount().getAccountId() != null
					&& request.getAccount().getAccountId().equals( interactionCriteria.getBillingAccountID() ) )
				{
					return interaction;
				}
			}
			else
			{
				return interaction;
			}

		}
		return null;

	}
}
