/**
 * 
 */
package com.orangecaraibe.enabler.crm.webservice.validator;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.core.utils.Constantes;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.CreateException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.UpdateException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.schemas.CreateFault;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.schemas.UpdateFault;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.InteractionSubject;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Party;
import com.orangecaraibe.soa.v2.model.commonbusiness.party.Person;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccount;
import com.orangecaraibe.soa.v2.model.customerdomain.customerproblem.CustomerProblem;

/**
 * @author ncr Class permettant de valider les entrées des appel web service a l'ebs customer problems
 */
@Component
public class CustomerProblemValidator
{

	public boolean createCustomerProblemValidator( CustomerProblem customerProblem )
		throws CreateException
	{
		if ( customerProblem == null )
		{
			CreateFault cFault = new CreateFault();
			cFault.setCode( "CUSP002" );
			cFault.setLabel( "customerProblem is null" );
			throw new CreateException( "customerProblem is null", cFault );
		}

		if ( customerProblem.getReason() == null )
		{
			CreateFault cFault = new CreateFault();
			cFault.setCode( "CUSP003" );
			cFault.setLabel( "Reason is mandatory" );
			throw new CreateException( "Reason is mandatory", cFault );
		}
		if ( CollectionUtils.isNotEmpty( customerProblem.getCustomerInterlocutor() ) )
		{

			if ( customerProblem.getCustomerInterlocutor().get( 0 ).getParty() instanceof Person )
			{
				Person personne = (Person) customerProblem.getCustomerInterlocutor().get( 0 ).getParty();
				if ( personne.getPartyID() == null )
				{
					CreateFault cFault = new CreateFault();
					cFault.setCode( "CUSP006" );
					cFault.setLabel( "PartyId is mandatory" );
					throw new CreateException( "PartyId is mandatory", cFault );
				}
			}
		}
		else
		{

			if ( CollectionUtils.isNotEmpty( customerProblem.getInteractionSubject() ) )
			{

				boolean isBillingAccount = false;
				boolean isParty = false;
				boolean isOldParty = false;
				for ( InteractionSubject interaction : customerProblem.getInteractionSubject() )
				{
					if ( interaction instanceof BillingAccount )
					{
						BillingAccount billingAccount = (BillingAccount) interaction;
						if ( billingAccount.getBillingAccountID() != null )
						{
							isBillingAccount = true;
						}
					}
					if ( interaction instanceof Party )
					{
						Party party = (Party) interaction;
						if ( party.getPartyID() != null )
						{
							isParty = true;
						}
						else if ( party.getPartyID().contains( Constantes.ODL_PARTY_ID_REGEX ) )
						{
							isOldParty = true;
						}
					}
				}
				if ( !isBillingAccount && !isParty )
				{
					CreateFault cFault = new CreateFault();
					cFault.setCode( "CUSP020" );
					cFault.setLabel( "CustomerInterlocuteur.PartyId  or billingAccount is mandatory" );
					throw new CreateException(	"CustomerInterlocuteur.PartyId  or billingAccount is mandatory",
												cFault );
				}
				else if ( isOldParty && !isBillingAccount )
				{
					return false;
				}
			}
			else
			{
				CreateFault cFault = new CreateFault();
				cFault.setCode( "CUSP020" );
				cFault.setLabel( "CustomerInterlocuteur.PartyId  or billingAccount is mandatory" );
				throw new CreateException( "CustomerInterlocuteur.PartyId  or billingAccount is mandatory", cFault );
			}
		}

		return true;
	}

	public void notifyDunningValidator( CustomerProblem customerProblem )
		throws UpdateException
	{
		if ( customerProblem.getTroubleTicketID() == null )
		{
			UpdateFault uFault = new UpdateFault();
			uFault.setCode( "CUSP008" );
			uFault.setLabel( "TroubleTicketID is mandatory for update" );
			throw new UpdateException( "TroubleTicketID is mandatory for update", uFault );
		}

	}

	public void customerProblemAddNoteValidator( CustomerProblem customerProblem )
		throws UpdateException
	{
		if ( customerProblem.getTroubleTicketID() == null )
		{
			UpdateFault uFault = new UpdateFault();
			uFault.setCode( "CUSP008" );
			uFault.setLabel( "TroubleTicketID is mandatory for update" );
			throw new UpdateException( "TroubleTicketID is mandatory for update", uFault );
		}

		if ( customerProblem.getCustomerServiceRepresentative() == null )
		{
			UpdateFault uFault = new UpdateFault();
			uFault.setCode( "CUSP009" );
			uFault.setLabel( "serviceRepresentative is mandatory for update" );
			throw new UpdateException( "serviceRepresentative is mandatory for update", uFault );
		}

		if ( customerProblem.getLocalComment() == null )
		{
			UpdateFault uFault = new UpdateFault();
			uFault.setCode( "CUSP010" );
			uFault.setLabel( "LocalComment is mandatory to add note" );
			throw new UpdateException( "LocalComment is mandatory to add note", uFault );
		}

	}

	public void updateValidator( CustomerProblem customerProblem )
		throws UpdateException
	{
		if ( customerProblem.getTroubleTicketID() == null )
		{
			UpdateFault uFault = new UpdateFault();
			uFault.setCode( "CUSP008" );
			uFault.setLabel( "TroubleTicketID is mandatory for update" );
			throw new UpdateException( "TroubleTicketID is mandatory for update", uFault );
		}

		if ( customerProblem.getCustomerServiceRepresentative() == null )
		{
			UpdateFault uFault = new UpdateFault();
			uFault.setCode( "CUSP009" );
			uFault.setLabel( "serviceRepresentative is mandatory for update" );
			throw new UpdateException( "serviceRepresentative is mandatory for update", uFault );
		}

	}

	public void customerProblemUpdateValidator( CustomerProblem customerProblem )
		throws UpdateException
	{
		if ( customerProblem.getTroubleTicketID() == null )
		{
			UpdateFault uFault = new UpdateFault();
			uFault.setCode( "CUSP008" );
			uFault.setLabel( "TroubleTicketID is mandatory for update" );
			throw new UpdateException( "TroubleTicketID is mandatory for update", uFault );
		}

		if ( customerProblem.getCustomerServiceRepresentative() == null )
		{
			UpdateFault uFault = new UpdateFault();
			uFault.setCode( "CUSP009" );
			uFault.setLabel( "serviceRepresentative is mandatory for update" );
			throw new UpdateException( "serviceRepresentative is mandatory for update", uFault );
		}

	}
}
