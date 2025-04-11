package com.orangecaraibe.enabler.crm.webservice.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.orange.drm.interfaces.webserviceoreka.v1.ObjectFactory;
import com.orange.drm.interfaces.webserviceoreka.v1.Troubleticket;
import com.orange.drm.interfaces.webserviceoreka.v1.Troubleticket.Causes.Cause;
import com.orange.drm.interfaces.webserviceoreka.v1.Troubleticket.Comments.Comment;
import com.orange.drm.interfaces.webserviceoreka.v1.TroubleticketResponse;
import com.orange.drm.interfaces.webserviceoreka.v1.WSTroubleTicket;
import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.crm.business.bean.Note;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumRequestStatus;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumRequestStatusReason;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;
import com.orangecaraibe.enabler.crm.business.helper.TroubleTicketHelper;
import com.orangecaraibe.enabler.crm.business.service.CustomerProblemService;

/**
 * Base implementation of WSTroubleTicket Interface.
 */
@Transactional( propagation = Propagation.SUPPORTS, readOnly = false )
@Service( "wsTroubleTicketService" )
public class WSTroubleTicketServiceImpl
	implements WSTroubleTicket
{

	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger( WSTroubleTicketServiceImpl.class );

	/** definition du service pour la mise a jour d'une demande */
	@Autowired
	@Qualifier( "customerProblemService" )
	private CustomerProblemService customerProblemService;

	@Autowired
	@Qualifier( "objectfactoryWSTroubleTicket" )
	private ObjectFactory objectFactory;

	@Autowired
	private TroubleTicketHelper troubleTicketHelper;

	@Override
	public TroubleticketResponse troubleticket( Troubleticket troubleTicket )
	{
		Request request = new Request();
		TroubleticketResponse troubleticketResponse = objectFactory.createTroubleticketResponse();
		String troubleTicketId = null;

		if ( troubleTicket != null && troubleTicket.getSystem().getMsticketid().getValue() != null
			&& troubleTicket.getSystem().getMsticketid().getValue().getTicketid() != null
			&& troubleTicket.getSystem().getMsticketid().getValue().getTicketid().getValue() != null
			&& troubleTicket.getSystem().getMsticketid().getValue().getTicketid().getValue().getId() != null )
		{
			troubleTicketId =
				troubleTicket.getSystem().getMsticketid().getValue().getTicketid().getValue().getId().getValue();
		}

		try
		{
			if ( troubleTicket.getComments() != null && troubleTicket.getComments().getValue() != null )
			{
				for ( Comment comment : troubleTicket.getComments().getValue().getComment() )
				{
					Note note = new Note();
					if ( comment.getLabel() != null )
					{
						note.setNote( comment.getLabel().getValue() );
					}
					Date date;
					try
					{
						date = DateUtils.getDateFromString( comment.getRecordingdate().getValue(), "yyyyMMddhhmm" );
					}
					catch ( ParseException e )
					{
						LOGGER.error(	"Erreur lors de la mise à jour de la demande due à un problème de parsing de la date",
										e );
						date = new Date();
					}

					note.setDateCreation( date );
					if ( comment.getActor() != null && comment.getActor().getValue() != null
						&& comment.getActor().getValue().getName() != null
						&& comment.getActor().getValue().getName().getValue() != null )
					{
						note.setUserName( comment.getActor().getValue().getName().getValue() );
					}
					request.getNotes().add( note );
				}

			}

			request.setNotes( getLastNote( request.getNotes() ) );

			if ( troubleTicket.getCauses() != null && troubleTicket.getCauses().getValue() != null
				&& CollectionUtils.isNotEmpty( troubleTicket.getCauses().getValue().getCause() ) )
			{
				String shortLabel = null;
				if ( troubleTicket.getTicket() != null && troubleTicket.getTicket().getValue() != null
					&& troubleTicket.getTicket().getValue().getShortlabel() != null
					&& troubleTicket.getTicket().getValue().getShortlabel().getValue() != null )
				{

					shortLabel = troubleTicket.getTicket().getValue().getShortlabel().getValue();
				}
				for ( Cause cause : troubleTicket.getCauses().getValue().getCause() )
				{
					Note note = new Note();

					note.setNote( " Retour expert : " + shortLabel + " " + cause.getLabel().getValue() );

					request.getNotes().add( note );
				}
			}

			if ( troubleTicket.getTicket() != null && troubleTicket.getTicket().getValue() != null
				&& troubleTicket.getTicket().getValue().getCurrentstatus().getValue() != null
				&& troubleTicket.getTicket().getValue().getCurrentstatus().getValue().equals( String.valueOf( EnumRequestStatus.RESOLVED.getIdOceane() ) ) )
			{
				request.setStatus( EnumRequestStatus.ACTIVE );
				request.setStatusReason( EnumRequestStatusReason.OPENED_INCIDENT_RESOLVED );
			}

			if ( troubleTicket.getTicket() != null && troubleTicket.getTicket().getValue() != null
				&& troubleTicket.getTicket().getValue().getThirdreference() != null )
			{
				request.setGuid( decodeGuid( troubleTicket.getTicket().getValue().getThirdreference().getValue() ) );
			}

			customerProblemService.update( request );
		}
		catch ( CustomerProblemUpdateException e )
		{
			LOGGER.error( "Erreur lors de la mise à jour de la demande " + request.getGuid()
				+ " pour le trouble ticket : " + troubleTicket, e );
		}

		troubleticketResponse.setTroubleticketResultCode( objectFactory.createTroubleticketResponseTroubleticketResultCode( troubleTicketHelper.getTroubletTicketResponseCode() ) );

		troubleticketResponse.setTroubleticketResultString( objectFactory.createTroubleticketResponseTroubleticketResultString( troubleTicketHelper.getTroubletTicketResponseString() ) );

		return troubleticketResponse;
	}

	private List<Note> getLastNote( List<Note> notes )
	{
		Note lastNote = null;
		for ( Note note : notes )
		{

			if ( lastNote == null || lastNote.getDateCreation().before( note.getDateCreation() ) )
			{
				lastNote = note;
			}
		}
		List<Note> retour = new ArrayList<Note>();
		retour.add( lastNote );
		return retour;
	}

	private String decodeGuid( String value )
	{
		String[] guidTable = value.split( "-" );
		StringBuffer guidTemp = new StringBuffer();
		int i = 0;
		for ( String temp : guidTable )
		{

			long guidBase10 = Long.parseLong( temp, 36 );
			String base16 = Long.toString( guidBase10, 16 );
			if ( i < 2 )
			{
				while ( base16.length() < 10 )
				{
					base16 = "0" + base16;
				}
			}
			else if ( i == 2 )
			{

				while ( base16.length() < 12 )
				{
					base16 = "0" + base16;
				}

			}
			guidTemp.append( base16 );
			i++;
		}

		StringBuffer guid = new StringBuffer();
		guid.append( guidTemp.substring( 0, 8 ) );
		guid.append( "-" );
		guid.append( guidTemp.substring( 8, 12 ) );
		guid.append( "-" );
		guid.append( guidTemp.substring( 12, 16 ) );
		guid.append( "-" );
		guid.append( guidTemp.substring( 16, 20 ) );
		guid.append( "-" );
		guid.append( guidTemp.substring( 20 ) );

		return guid.toString();
	}

}
