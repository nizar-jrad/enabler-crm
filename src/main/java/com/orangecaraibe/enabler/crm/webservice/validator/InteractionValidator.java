package com.orangecaraibe.enabler.crm.webservice.validator;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.CreateException;
import com.orangecaraibe.soa.v2.interfaces.managecustomerproblem.schemas.CreateFault;
import com.orangecaraibe.soa.v2.model.commonbusiness.interaction.Interaction;

@Component
public class InteractionValidator
{

	public boolean createInteractionValidator( Interaction interaction )
		throws CreateException
	{
		if ( interaction == null )
		{
			CreateFault cFault = new CreateFault();
			cFault.setCode( "INTC001" );
			cFault.setLabel( "Interaction is null" );
			throw new CreateException( "Interaction is null", cFault );
		}
		if ( CollectionUtils.isEmpty( interaction.getFrom() ) && CollectionUtils.isEmpty( interaction.getTo() )
			&& CollectionUtils.isEmpty( interaction.getInteractionSubject() ) )
		{
			CreateFault cFault = new CreateFault();
			cFault.setCode( "INTC002" );
			cFault.setLabel( "Any customer information presents in Interaction" );
			throw new CreateException( "Any customer information presents in Interaction ", cFault );
		}
		else
		{

		}

		return true;
	}

}
