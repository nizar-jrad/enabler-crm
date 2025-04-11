package com.orangecaraibe.enabler.externalws.utils;

import org.apache.commons.collections4.Predicate;

import com.orange.sidom.soa.dcrm.datacontract.Note;

/**
 * This predicate checks if the DCRM note is document
 */
public class DcrmNoteIsDocumentPredicate
	implements Predicate
{

	// private final Note note;
	//
	// public DcrmNoteIsDocumentPredicate( Note note )
	// {
	// super();
	// this.note = note;
	// }
	//
	@Override
	public boolean evaluate( Object object )
	{

		if ( object instanceof Note )
		{
			Note t = (Note) object;

			if ( t.getEstDocument() != null )
			{
				return t.getEstDocument().getValue();
			}

		}

		return false;
	}

}
