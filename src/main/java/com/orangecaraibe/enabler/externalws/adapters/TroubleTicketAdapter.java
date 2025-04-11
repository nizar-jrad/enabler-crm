/**
 * 
 */
package com.orangecaraibe.enabler.externalws.adapters;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orange.interfaces.managetroubleticket.v2.CommentREQCTT;
import com.orange.interfaces.managetroubleticket.v2.CommentREQUTT;
import com.orange.interfaces.managetroubleticket.v2.ComplementaryFieldREQCTT;
import com.orange.interfaces.managetroubleticket.v2.EmailAddressREQCTT;
import com.orange.interfaces.managetroubleticket.v2.HasContactMethodREQCTT;
import com.orange.interfaces.managetroubleticket.v2.InstServiceCharacteristicValueREQCTT;
import com.orange.interfaces.managetroubleticket.v2.InstalledServiceREQCTT;
import com.orange.interfaces.managetroubleticket.v2.ObjectFactory;
import com.orange.interfaces.managetroubleticket.v2.PartyREQCTT;
import com.orange.interfaces.managetroubleticket.v2.PartyRoleREQCTT;
import com.orange.interfaces.managetroubleticket.v2.PartyRoleSetREQCTT;
import com.orange.interfaces.managetroubleticket.v2.PersonNameREQCTT;
import com.orange.interfaces.managetroubleticket.v2.PhoneNumberREQCTT;
import com.orange.interfaces.managetroubleticket.v2.ServiceSpecCharacteristicREQCTT;
import com.orange.interfaces.managetroubleticket.v2.ServiceSpecificationREQCTT;
import com.orange.interfaces.managetroubleticket.v2.SystemREQCTT;
import com.orange.interfaces.managetroubleticket.v2.SystemREQUTT;
import com.orange.interfaces.managetroubleticket.v2.TroubleTicketViewForCreateTroubleTicketRequest;
import com.orange.interfaces.managetroubleticket.v2.TroubleTicketViewForUpdateTroubleTicketRequest;
import com.orange.interfaces.managetroubleticket.v2.root.CreateTroubleTicketMessage;
import com.orange.interfaces.managetroubleticket.v2.root.UpdateTroubleTicketMessage;
import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.core.utils.FormatUtils;
import com.orangecaraibe.enabler.crm.business.bean.Actor;
import com.orangecaraibe.enabler.crm.business.bean.ChildRequest;
import com.orangecaraibe.enabler.crm.business.bean.CustomerService;
import com.orangecaraibe.enabler.crm.business.bean.Holder;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.exception.CustomerProblemUpdateException;
import com.orangecaraibe.enabler.crm.business.helper.TroubleTicketHelper;

/**
 * Classe pour les adaptations entre le modele metier est les objets OCEANE
 * 
 * @author NCR
 */
@Component
public class TroubleTicketAdapter
{

	private static final String TELEPHONIE = "TELEPHONIE";

	private static final String DATA = "DATA";

	@Autowired
	@Qualifier( "objectFactoryOceane" )
	private ObjectFactory objectFactory;

	@Autowired
	@Qualifier( "objectFactoryOceaneWrapper" )
	private com.orange.interfaces.managetroubleticket.v2.root.ObjectFactory objectFactoryOceaneWrapper;

	@Autowired
	private TroubleTicketHelper troubleTicketHelper;

	public CreateTroubleTicketMessage createTroubleTicketAdapter( Request request, String cuid )
		throws CustomerProblemUpdateException
	{
		String msisdn = "";

		if ( request.getContract() != null && request.getContract().getMsisdn() != null )
		{
			msisdn = request.getContract().getMsisdn();
			msisdn = FormatUtils.getMsisdnFromPhoneNumber( msisdn );
		}

		CreateTroubleTicketMessage troubleTicketMessage = objectFactoryOceaneWrapper.createCreateTroubleTicketMessage();

		TroubleTicketViewForCreateTroubleTicketRequest troubleTicket =
			objectFactory.createTroubleTicketViewForCreateTroubleTicketRequest();
		SystemREQCTT sys = objectFactory.createSystemREQCTT();
		sys.setCode( troubleTicketHelper.getApplicationName() );

		troubleTicket.setLocalSystem( sys );
		troubleTicket.setLocalExternalTicketID( objectFactory.createTroubleTicketViewForCreateTroubleTicketRequestLocalExternalTicketID( encodeGUID( request.getGuid() ) ) );
		StringBuffer description = new StringBuffer();
		description.append( msisdn );
		description.append( " " );
		description.append( request.getDescription() );
		troubleTicket.setDescription( objectFactory.createTroubleTicketViewForCreateTroubleTicketRequestDescription( description.toString() ) );
		troubleTicket.setTroubleTicketPriority( objectFactory.createTroubleTicketViewForCreateTroubleTicketRequestTroubleTicketPriority( troubleTicketHelper.getTroubleTickepriority() ) );
		troubleTicket.setTroubleTicketType( "ServiceTroubleTicket" );
		troubleTicket.setTroubleTicketCategory( objectFactory.createTroubleTicketViewForCreateTroubleTicketRequestTroubleTicketCategory( troubleTicketHelper.getTroubleTicketCategory() ) );
		troubleTicket.setTroubleTicketOriginator( troubleTicketHelper.getTroubleTicketOriginator() );
		troubleTicket.setTroubleDetectionDate( objectFactory.createTroubleTicketViewForCreateTroubleTicketRequestTroubleDetectionDate( DateUtils.convertDateToXMLGregorianCalendar( request.getCreationDate() ) ) );
		troubleTicket.setTroubleType( troubleTicketHelper.getTroubleTicketType() );
		//
		// if ( CollectionUtils.isNotEmpty( request.getNotes() ) )
		// {
		// for ( Note note : request.getNotes() )
		// {
		// CommentREQCTT comment = objectFactory.createCommentREQCTT();
		// comment.setLabel( note.getNote() );
		//
		// troubleTicket.getLocalComment().add( comment );
		// }
		//
		// }

		if ( request.getHolder() != null )
		{
			PartyRoleREQCTT partyRole = createPartyRole( request.getHolder(), null, msisdn );
			troubleTicket.getPartyRole().add( partyRole );
		}

		if ( request.getCustomerService() != null )
		{
			PartyRoleREQCTT partyRole = createPartyRole( request.getCustomerService(), cuid, null );
			troubleTicket.getPartyRole().add( partyRole );
		}

		if ( request.getEquipeServiceCode() != null )
		{
			PartyRoleREQCTT partyRole = createPartyRole( null, request.getEquipeServiceCode(), null );
			troubleTicket.getPartyRole().add( partyRole );
		}

		// if ( request.getContract() != null && request.getContract().getMsisdn() != null )
		// {
		// CommentREQCTT comment = objectFactory.createCommentREQCTT();
		// StringBuffer commentaireOceane = new StringBuffer();
		// commentaireOceane.append( "Contact Client Entrant " );
		// commentaireOceane.append( "MISDN=" );
		// commentaireOceane.append( request.getContract().getMsisdn() );
		//
		// comment.setLabel( commentaireOceane.toString() );
		// CommentPartyRoleREQCTT commentPartyRole = objectFactory.createCommentPartyRoleREQCTT();
		// CommentPartyREQCTT commentParty = objectFactory.createCommentPartyREQCTT();
		// comment.
		// commentPartyRole.setParty( commentParty );
		// comment.setPartyRole( commentPartyRole );
		//
		//
		// troubleTicket.getLocalComment().add( comment );
		// }

		List<ComplementaryFieldREQCTT> lstCompl = createComplementaryFiel( request );

		InstalledServiceREQCTT installedService = createInstalleService( request );

		troubleTicket.setInstalledService( objectFactory.createTroubleTicketViewForCreateTroubleTicketRequestInstalledService( installedService ) );

		troubleTicket.getLocalComplementaryField().addAll( lstCompl );

		// Demandes Filles ==> on ajoute en commentaires
		if ( request.getDemandesFilles() != null && !request.getDemandesFilles().isEmpty() )
		{
			generateChildRequestComment( request, troubleTicket );
		}

		troubleTicketMessage.setTroubleTicket( troubleTicket );

		return troubleTicketMessage;
	}

	private String encodeGUID( String guid )
	{

		String guidTemp = guid.replaceAll( "-", "" );
		String guidFirst = guidTemp.substring( 0, 10 );
		String guidSecond = guidTemp.substring( 10, 20 );
		String guidThird = guidTemp.substring( 20 );
		StringBuffer newGuid = new StringBuffer();

		Long guidBase10First = Long.parseLong( guidFirst, 16 );
		Long guidBase10Second = Long.parseLong( guidSecond, 16 );
		Long guidBase10Third = Long.parseLong( guidThird, 16 );
		String base32First = Long.toString( guidBase10First, 36 );
		String base32Second = Long.toString( guidBase10Second, 36 );
		String base32Third = Long.toString( guidBase10Third, 36 );
		newGuid.append( base32First );
		newGuid.append( "-" );
		newGuid.append( base32Second );
		newGuid.append( "-" );
		newGuid.append( base32Third );

		return newGuid.toString();
	}

	public String base10Converter( int number, int finalBase )
	{
		int quo;
		int rem;
		char[] res = new char[1];

		do
		{
			rem = number % finalBase;
			quo = number / finalBase;
			res = Arrays.copyOf( res, res.length + 1 );
			if ( rem < 10 )
			{
				// Converting ints using ASCII values
				rem += 48;
				res[res.length - 1] = (char) rem;
			}
			else
			{
				// Convert int > 9 to A, B, C..
				rem += 55;
				res[res.length - 1] = (char) rem;
			}
			number /= finalBase;
		}
		while ( quo != 0 );

		// Reverse array
		char[] temp = new char[res.length];
		for ( int i = res.length - 1, j = 0; i > 0; i-- )
		{
			temp[j++] = res[i];
		}

		return Arrays.toString( temp );
	}

	private InstalledServiceREQCTT createInstalleService( Request request )
		throws CustomerProblemUpdateException
	{
		InstalledServiceREQCTT installedService = objectFactory.createInstalledServiceREQCTT();

		ServiceSpecificationREQCTT serviceSpecification = objectFactory.createServiceSpecificationREQCTT();
		serviceSpecification.setServiceSpecificationCode( objectFactory.createServiceSpecificationREQCTTServiceSpecificationCode( "" ) );

		installedService.setServiceSpecification( objectFactory.createInstalledServiceREQCTTServiceSpecification( serviceSpecification ) );

		ServiceSpecCharacteristicREQCTT serviceSpecCharacteristic =
			objectFactory.createServiceSpecCharacteristicREQCTT();
		InstServiceCharacteristicValueREQCTT instServiceCharacteriticValue =
			objectFactory.createInstServiceCharacteristicValueREQCTT();
		instServiceCharacteriticValue.setIndex( objectFactory.createInstServiceCharacteristicValueREQCTTIndex( 1 ) );

		String theme1 = request.getTheme().getLabel();
		String theme2 = request.getTheme().getTheme().get( 0 ).getLabel();
		String theme3 = request.getTheme().getTheme().get( 0 ).getTheme().get( 0 ).getLabel();

		String service = troubleTicketHelper.getServiceOceane( theme1, theme2, theme3 );
		instServiceCharacteriticValue.setServiceCharacteristicValue( service );
		serviceSpecCharacteristic.getInstServiceCharacteristicValue().add( instServiceCharacteriticValue );
		serviceSpecCharacteristic.setServiceSpecCharacteristicCode( objectFactory.createServiceSpecCharacteristicREQCTTServiceSpecCharacteristicCode( "" ) );

		installedService.setServiceSpecCharacteristic( objectFactory.createInstalledServiceREQCTTServiceSpecCharacteristic( serviceSpecCharacteristic ) );

		return installedService;
	}

	public List<ComplementaryFieldREQCTT> createComplementaryFiel( Request request )
	{

		List<ComplementaryFieldREQCTT> lstComFieldREQCTTs = new ArrayList<ComplementaryFieldREQCTT>();

		if ( request.getContract() != null && request.getContract().getStatus() != null )
		{
			ComplementaryFieldREQCTT statusContract = objectFactory.createComplementaryFieldREQCTT();

			statusContract.setIndex( BigInteger.valueOf( 1 ) );
			statusContract.setValue( objectFactory.createComplementaryFieldREQCTTValue( request.getContract().getStatus().getOceaneValue() ) );
			lstComFieldREQCTTs.add( statusContract );
		}

		if ( request.getContract() != null )
		{

			ComplementaryFieldREQCTT comMsisdn = objectFactory.createComplementaryFieldREQCTT();
			comMsisdn.setIndex( BigInteger.valueOf( 2 ) );
			if ( request.getContract().isData() )
			{
				comMsisdn.setValue( objectFactory.createComplementaryFieldREQCTTValue( DATA ) );
			}
			else
			{
				comMsisdn.setValue( objectFactory.createComplementaryFieldREQCTTValue( TELEPHONIE ) );
			}
			lstComFieldREQCTTs.add( comMsisdn );
		}

		if ( request.getContract() != null && request.getContract().getCoId() != null )
		{
			ComplementaryFieldREQCTT comCustomer = objectFactory.createComplementaryFieldREQCTT();

			comCustomer.setIndex( BigInteger.valueOf( 3 ) );
			String value = request.getContract().getCoId() + "/" + request.getAccount().getAccountId() + "/";
			comCustomer.setValue( objectFactory.createComplementaryFieldREQCTTValue( value ) );
			lstComFieldREQCTTs.add( comCustomer );
		}

		if ( request.getHolder() != null )
		{
			ComplementaryFieldREQCTT comHolder = objectFactory.createComplementaryFieldREQCTT();
			Holder holder = request.getHolder();
			String email = null;
			if ( holder.getContactMethods() != null )
			{
				email = holder.getContactMethods().getEmail();
			}

			StringBuffer holderInfo = new StringBuffer();
			holderInfo.append( holder.getName() );
			holderInfo.append( "/" );
			holderInfo.append( holder.getLastName() );
			holderInfo.append( "/" );
			holderInfo.append( email );
			holderInfo.append( " / " );
			if ( request.getContract().getMsisdn() != null )
			{
				holderInfo.append( "MISDN=" );
				holderInfo.append( request.getContract().getMsisdn() );
			}

			comHolder.setIndex( BigInteger.valueOf( 4 ) );
			comHolder.setValue( objectFactory.createComplementaryFieldREQCTTValue( holderInfo.toString() ) );
			lstComFieldREQCTTs.add( comHolder );
		}

		if ( request.getContract() != null && request.getContract().getOfferLabel() != null )
		{
			ComplementaryFieldREQCTT typeContract = objectFactory.createComplementaryFieldREQCTT();

			typeContract.setIndex( BigInteger.valueOf( 6 ) );
			typeContract.setValue( objectFactory.createComplementaryFieldREQCTTValue( request.getContract().getOfferLabel() ) );
			lstComFieldREQCTTs.add( typeContract );
		}

		if ( request.getCreationDate() != null )
		{
			ComplementaryFieldREQCTT comDate = objectFactory.createComplementaryFieldREQCTT();

			comDate.setIndex( BigInteger.valueOf( 7 ) );
			comDate.setValue( objectFactory.createComplementaryFieldREQCTTValue( DateUtils.getDateFormatYYYYMMDD( request.getCreationDate() ) ) );
			lstComFieldREQCTTs.add( comDate );
		}

		if ( request.getTheme() != null )
		{
			String theme1 = request.getTheme().getLabel();
			String theme2 = null;
			String theme3 = null;
			if ( request.getTheme().getTheme() != null && !request.getTheme().getTheme().isEmpty()
				&& request.getTheme().getTheme().get( 0 ) != null )
			{
				theme2 = request.getTheme().getTheme().get( 0 ).getLabel();
				if ( request.getTheme().getTheme().get( 0 ).getTheme() != null
					&& !request.getTheme().getTheme().get( 0 ).getTheme().isEmpty()
					&& request.getTheme().getTheme().get( 0 ).getTheme().get( 0 ) != null )
				{
					theme3 = request.getTheme().getTheme().get( 0 ).getTheme().get( 0 ).getLabel();
				}
			}

			// if ( StringUtils.isNotBlank( theme1 ) && StringUtils.isNotBlank( theme2 ) )
			// {
			// ComplementaryFieldREQCTT domain = objectFactory.createComplementaryFieldREQCTT();
			//
			// domain.setIndex( BigInteger.valueOf( 8 ) );
			// domain.setValue( objectFactory.createComplementaryFieldREQCTTValue( theme1 + " " + theme2 ) );
			// lstComFieldREQCTTs.add( domain );
			// // if ( StringUtils.isNotBlank( theme3 ) )
			// // {
			// // ComplementaryFieldREQCTT sdomain = objectFactory.createComplementaryFieldREQCTT();
			// //
			// // sdomain.setIndex( BigInteger.valueOf( 9 ) );
			// // sdomain.setValue( objectFactory.createComplementaryFieldREQCTTValue( theme3 ) );
			// // lstComFieldREQCTTs.add( sdomain );
			// // }
			//
			// }

		}

		return lstComFieldREQCTTs;
	}

	public PartyRoleREQCTT createPartyRole( Actor actor, String cuid, String msisdn )
	{

		PartyRoleREQCTT partyRole = objectFactory.createPartyRoleREQCTT();
		// creation du party role ID obligatoire initialiser a vide.
		partyRole.setPartyRoleID( objectFactory.createPartyRoleREQCTTPartyRoleID( "" ) );

		// gestion du partyRoleSet obligatoire et vide
		PartyRoleSetREQCTT partyRoleset = objectFactory.createPartyRoleSetREQCTT();
		partyRoleset.setPartyRoleSetID( objectFactory.createPartyRoleSetREQCTTPartyRoleSetID( "" ) );
		partyRole.setPartyRoleSet( partyRoleset );

		PartyREQCTT party = objectFactory.createPartyREQCTT();
		HasContactMethodREQCTT contactMethod = objectFactory.createHasContactMethodREQCTT();

		if ( actor != null )
		{

			if ( actor.getName() != null && cuid == null )
			{
				party.setPartyID( objectFactory.createPartyREQCTTPartyID( actor.getName() ) );
			}
			else if ( cuid != null )
			{
				party.setPartyID( objectFactory.createPartyREQCTTPartyID( cuid ) );
			}
			else
			{
				party.setPartyID( objectFactory.createPartyREQCTTPartyID( troubleTicketHelper.getGeneriqueLogin() ) );
			}

			PersonNameREQCTT person = objectFactory.createPersonNameREQCTT();
			person.setLastName( objectFactory.createPersonNameREQCTTLastName( actor.getName() ) );

			if ( actor instanceof Holder )
			{
				Holder holder = (Holder) actor;
				partyRole.setPartyRoleType( "CustomerInterlocutor" );
				if ( holder.getContactMethods().getEmail() != null )
				{
					EmailAddressREQCTT email = objectFactory.createEmailAddressREQCTT();
					email.setEMailAddress( objectFactory.createEmailAddressREQCTTEMailAddress( holder.getContactMethods().getEmail() ) );
					contactMethod.setEmailAddress( objectFactory.createHasContactMethodREQCTTEmailAddress( email ) );
				}
				if ( msisdn != null )
				{
					PhoneNumberREQCTT mobile = objectFactory.createPhoneNumberREQCTT();
					mobile.setNumber( objectFactory.createPhoneNumberREQCTTNumber( msisdn ) );
					mobile.setNumberType( objectFactory.createPhoneNumberREQCTTNumberType( "Mobile" ) );
					contactMethod.getPhoneNumber().add( mobile );
				}
				else if ( holder.getContactMethods().getMobilePhone() != null )
				{
					PhoneNumberREQCTT mobile = objectFactory.createPhoneNumberREQCTT();
					mobile.setNumber( objectFactory.createPhoneNumberREQCTTNumber( holder.getContactMethods().getMobilePhone() ) );
					mobile.setNumberType( objectFactory.createPhoneNumberREQCTTNumberType( "Mobile" ) );
					contactMethod.getPhoneNumber().add( mobile );
				}

				if ( holder.getLastName() != null )
				{
					PersonNameREQCTT personName = objectFactory.createPersonNameREQCTT();
					personName.setLastName( objectFactory.createPersonNameREQCTTLastName( holder.getLastName() ) );
					personName.setSalutation( objectFactory.createPersonNameREQCTTSalutation( holder.getTitle().name() ) );
					personName.setFirstName( objectFactory.createPersonNameREQCTTFirstName( holder.getFirstName() ) );
					party.setPersonName( objectFactory.createPartyREQCTTPersonName( personName ) );

				}
			}
			else if ( actor instanceof CustomerService )
			{

				CustomerService customerService = (CustomerService) actor;

				partyRole.setPartyRoleType( "CustomerRepresentative" );

				if ( customerService.getContactMethods() != null
					&& customerService.getContactMethods().getEmail() != null )
				{
					EmailAddressREQCTT email = objectFactory.createEmailAddressREQCTT();
					email.setEMailAddress( objectFactory.createEmailAddressREQCTTEMailAddress( customerService.getContactMethods().getEmail() ) );
					contactMethod.setEmailAddress( objectFactory.createHasContactMethodREQCTTEmailAddress( email ) );
				}
				else
				{
					EmailAddressREQCTT email = objectFactory.createEmailAddressREQCTT();
					email.setEMailAddress( objectFactory.createEmailAddressREQCTTEMailAddress( "" ) );
					contactMethod.setEmailAddress( objectFactory.createHasContactMethodREQCTTEmailAddress( email ) );

				}

			}

			if ( CollectionUtils.isEmpty( contactMethod.getPhoneNumber() ) )
			{
				PhoneNumberREQCTT mobile = objectFactory.createPhoneNumberREQCTT();
				mobile.setNumber( objectFactory.createPhoneNumberREQCTTNumber( "" ) );
				mobile.setNumberType( objectFactory.createPhoneNumberREQCTTNumberType( "Mobile" ) );
				contactMethod.getPhoneNumber().add( mobile );
			}
			party.setHasContactMethod( objectFactory.createPartyREQCTTHasContactMethod( contactMethod ) );
		}
		else
		{
			partyRole.setPartyRoleType( "TroubleResolutionLeader" );
			// partyRole.setPartyRoleID( cuid );

			partyRole.getPartyRoleSet().setPartyRoleSetID( objectFactory.createPartyRoleSetREQCTTPartyRoleSetID( cuid ) );
			EmailAddressREQCTT email = objectFactory.createEmailAddressREQCTT();
			email.setEMailAddress( objectFactory.createEmailAddressREQCTTEMailAddress( "" ) );
			contactMethod.setEmailAddress( objectFactory.createHasContactMethodREQCTTEmailAddress( email ) );

			PhoneNumberREQCTT mobile = objectFactory.createPhoneNumberREQCTT();
			mobile.setNumber( objectFactory.createPhoneNumberREQCTTNumber( "" ) );
			mobile.setNumberType( objectFactory.createPhoneNumberREQCTTNumberType( "Mobile" ) );
			contactMethod.getPhoneNumber().add( mobile );
		}

		party.setHasContactMethod( objectFactory.createPartyREQCTTHasContactMethod( contactMethod ) );
		partyRole.setParty( party );

		return partyRole;
	}

	/**
	 * @param objectFactory the objectFactory to set
	 */
	public void setObjectFactory( ObjectFactory objectFactory )
	{
		this.objectFactory = objectFactory;
	}

	/**
	 * @param objectFactoryOceaneWrapper the objectFactoryOceaneWrapper to set
	 */
	public void setObjectFactoryOceaneWrapper( com.orange.interfaces.managetroubleticket.v2.root.ObjectFactory objectFactoryOceaneWrapper )
	{
		this.objectFactoryOceaneWrapper = objectFactoryOceaneWrapper;
	}

	/**
	 * @param troubleTicketHelper the troubleTicketHelper to set
	 */
	public void setTroubleTicketHelper( TroubleTicketHelper troubleTicketHelper )
	{
		this.troubleTicketHelper = troubleTicketHelper;
	}

	public UpdateTroubleTicketMessage createUpdateTroubleTicketMessage( Request request, String cuid )
	{
		UpdateTroubleTicketMessage troubleTicketMessage = objectFactoryOceaneWrapper.createUpdateTroubleTicketMessage();
		TroubleTicketViewForUpdateTroubleTicketRequest troubleTicket =
			objectFactory.createTroubleTicketViewForUpdateTroubleTicketRequest();

		SystemREQUTT sys = objectFactory.createSystemREQUTT();
		sys.setCode( troubleTicketHelper.getApplicationName() );
		troubleTicket.setLocalSystem( sys );

		troubleTicket.setTroubleTicketID( objectFactory.createTroubleTicketViewForUpdateTroubleTicketRequestTroubleTicketID( request.getTroubleTicketID() ) );

		generateChildRequestComment( request, troubleTicket );

		troubleTicketMessage.setTroubleTicket( troubleTicket );

		return troubleTicketMessage;
	}

	private void generateChildRequestComment(	Request request,
												TroubleTicketViewForUpdateTroubleTicketRequest troubleTicket )
	{
		if ( request.getDemandesFilles() != null )
		{
			for ( ChildRequest cr : request.getDemandesFilles() )
			{
				if ( cr != null )
				{
					CommentREQUTT comment = objectFactory.createCommentREQUTT();
					comment.setCode( "INT" );
					String label = "";
					// label : thèmes – nom du client – numéro de compte - numéro de téléphone – description de la
					// demande.
					// Debut Themes
					if ( cr.getTheme() != null )
					{
						String theme1 = cr.getTheme().getLabel();
						String theme2 = null;
						String theme3 = null;
						if ( cr.getTheme().getTheme() != null && !cr.getTheme().getTheme().isEmpty()
							&& cr.getTheme().getTheme().get( 0 ) != null )
						{
							theme2 = cr.getTheme().getTheme().get( 0 ).getLabel();
							if ( cr.getTheme().getTheme().get( 0 ).getTheme() != null
								&& !cr.getTheme().getTheme().get( 0 ).getTheme().isEmpty()
								&& cr.getTheme().getTheme().get( 0 ).getTheme().get( 0 ) != null )
							{
								theme3 = cr.getTheme().getTheme().get( 0 ).getTheme().get( 0 ).getLabel();
							}
						}

						label += theme1;
						if ( StringUtils.isNotBlank( theme2 ) )
						{
							label += " " + theme2;
							if ( StringUtils.isNotBlank( theme3 ) )
							{
								label += " " + theme3;
							}
						}
					}
					// Fin Themes
					// Nom Client
					if ( cr.getAccount() != null && cr.getAccount().getHolder() != null )
					{
						label += " - " + cr.getAccount().getHolder().getFirstName();
					}
					// Numero de compte
					if ( cr.getAccount() != null )
					{
						label += " - " + cr.getAccount().getAccountId();
					}
					// MSISDN
					if ( cr.getContract() != null )
					{
						label += " - " + cr.getContract().getMsisdn();
					}
					// Description
					if ( StringUtils.isNotBlank( cr.getDescription() ) )
					{
						label += " - " + cr.getDescription();
					}
					comment.setLabel( label );
					troubleTicket.getLocalComment().add( comment );
				}
			}

		}
	}

	private void generateChildRequestComment(	Request request,
												TroubleTicketViewForCreateTroubleTicketRequest troubleTicket )
	{
		if ( request.getDemandesFilles() != null )
		{
			for ( ChildRequest cr : request.getDemandesFilles() )
			{
				if ( cr != null )
				{

					CommentREQCTT comment = objectFactory.createCommentREQCTT();
					comment.setCode( "INT" );
					String label = "";
					// label : thèmes – nom du client – numéro de compte - numéro de téléphone – description de la
					// demande.
					// Debut Themes
					if ( cr.getTheme() != null )
					{
						String theme1 = cr.getTheme().getLabel();
						String theme2 = null;
						String theme3 = null;
						if ( cr.getTheme().getTheme() != null && !cr.getTheme().getTheme().isEmpty()
							&& cr.getTheme().getTheme().get( 0 ) != null )
						{
							theme2 = cr.getTheme().getTheme().get( 0 ).getLabel();
							if ( cr.getTheme().getTheme().get( 0 ).getTheme() != null
								&& !cr.getTheme().getTheme().get( 0 ).getTheme().isEmpty()
								&& cr.getTheme().getTheme().get( 0 ).getTheme().get( 0 ) != null )
							{
								theme3 = cr.getTheme().getTheme().get( 0 ).getTheme().get( 0 ).getLabel();
							}
						}

						label += theme1;
						if ( StringUtils.isNotBlank( theme2 ) )
						{
							label += " " + theme2;
							if ( StringUtils.isNotBlank( theme3 ) )
							{
								label += " " + theme3;
							}
						}
					}
					// Fin Themes
					// Nom Client
					if ( cr.getAccount() != null && cr.getAccount().getHolder() != null )
					{
						label += " - " + cr.getAccount().getHolder().getFirstName();
					}
					// Numero de compte
					if ( cr.getAccount() != null )
					{
						label += " - " + cr.getAccount().getAccountId();
					}
					// MSISDN
					if ( cr.getContract() != null )
					{
						label += " - " + cr.getContract().getMsisdn();
					}
					// Description
					if ( StringUtils.isNotBlank( cr.getDescription() ) )
					{
						label += " - " + cr.getDescription();
					}
					comment.setLabel( label );
					troubleTicket.getLocalComment().add( comment );
				}
			}

		}
	}

}
