package com.orangecaraibe.enabler.externalws.helpers;

import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionChannel;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionStatus;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumInteractionWay;

/**
 * Ce bean represente un triplet (canal,statut,sens) pour typer une interaction. Il sert de cle a la Map permettant de
 * determiner la raison du statut a utiliser par defaut lors d'une creation d'interaction.
 */
public class DefaultInteractionStatusReasonBean
{
	private EnumInteractionChannel channel;

	private EnumInteractionStatus status;

	private EnumInteractionWay way;

	public EnumInteractionChannel getChannel()
	{
		return channel;
	}

	public void setChannel( EnumInteractionChannel channel )
	{
		this.channel = channel;
	}

	public EnumInteractionStatus getStatus()
	{
		return status;
	}

	public void setStatus( EnumInteractionStatus status )
	{
		this.status = status;
	}

	public EnumInteractionWay getWay()
	{
		return way;
	}

	public void setWay( EnumInteractionWay way )
	{
		this.way = way;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( obj instanceof DefaultInteractionStatusReasonBean )
		{
			DefaultInteractionStatusReasonBean bean = (DefaultInteractionStatusReasonBean) obj;
			return ( bean.getChannel() == this.channel ) && ( bean.getStatus() == this.status )
				&& ( bean.getWay() == this.getWay() );
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( channel == null ) ? 0 : channel.hashCode() );
		result = prime * result + ( ( status == null ) ? 0 : status.hashCode() );
		result = prime * result + ( ( way == null ) ? 0 : way.hashCode() );
		return result;
	}

}
