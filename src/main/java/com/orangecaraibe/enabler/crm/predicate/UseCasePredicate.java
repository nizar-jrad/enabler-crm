package com.orangecaraibe.enabler.crm.predicate;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import com.orangecaraibe.soa.v2.model.commons.UseCase;

/**
 * This predicate evaluates the useCase ID
 */
public class UseCasePredicate
	implements Predicate
{

	private String useCaseID = StringUtils.EMPTY;

	public void setUseCaseID( String useCaseID )
	{
		this.useCaseID = useCaseID;
	}

	@Override
	public boolean evaluate( Object object )
	{

		if ( object instanceof UseCase )
		{
			UseCase t = (UseCase) object;

			if ( StringUtils.isEmpty( t.getUseCaseID() ) )
			{
				return false;
			}

			return t.getUseCaseID().equals( useCaseID );
		}

		return false;
	}

}
