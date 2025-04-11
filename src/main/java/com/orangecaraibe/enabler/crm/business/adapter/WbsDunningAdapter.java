package com.orangecaraibe.enabler.crm.business.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.core.utils.DateUtils;
import com.orangecaraibe.enabler.crm.business.bean.Account;
import com.orangecaraibe.enabler.crm.business.bean.LegalStatus;
import com.orangecaraibe.enabler.crm.business.bean.Option;
import com.orangecaraibe.enabler.crm.business.bean.Parameter;
import com.orangecaraibe.enabler.crm.business.bean.Request;
import com.orangecaraibe.enabler.crm.business.bean.enums.EnumLegalStatus;
import com.orangecaraibe.soa.v2.model.customerdomain.customerbilling.BillingAccount;

import fcm.gp.oc.schemas.dunning.data.RegisterCardStatus;
import fcm.gp.oc.schemas.dunning.data.WorkItemType;
import fcm.gp.oc.schemas.dunning.data.dunningregistercard.ObjectFactory;
import fcm.gp.oc.schemas.dunning.data.dunningregistercard.RegisterCardActionData;
import fcm.gp.oc.schemas.dunning.data.dunningworkitem.CreateWorkLoadData;

// TODO LDE : est-ce que cet adapter doit figurer dans ce package ou un autre package ?
@Component
public class WbsDunningAdapter
{

	@Autowired
	@Qualifier( "objectFactoryRegisterCard" )
	private ObjectFactory objectFactoryRegisterCard;

	@Autowired
	private fcm.gp.oc.schemas.dunning.data.dunningworkitem.ObjectFactory objectFactoryWorkLoad;

	public CreateWorkLoadData adaptWorkLoad( Request request )
	{
		CreateWorkLoadData createWorkLoadData = new CreateWorkLoadData();

		// le controle du channel (donnee obligatoire) a ete effectue en amont
		String applicationName = request.getChannel();

		Date today = new Date();
		String todayDDMMYYYY = DateUtils.getDateFormatDDMMYYYY( today );

		// Nom (login) de l'utilisateur faisant la demande TODO a faire
		String userName = StringUtils.EMPTY;
		if ( request.getActor() != null )
		{

			userName = request.getActor().getGuid();

		}

		// Customer_id du client pour lequel on souhaite creer la charge de travail
		String customerId = StringUtils.EMPTY;
		InstanceofPredicate BillingAccountInstancePredicate = new InstanceofPredicate( BillingAccount.class );
		Account account = request.getAccount();

		if ( account != null )
		{
			customerId = account.getAccountId();
		}

		createWorkLoadData.setApplicationName( applicationName );
		createWorkLoadData.setUserName( userName );
		createWorkLoadData.setCreationDate( todayDDMMYYYY );
		createWorkLoadData.setCustomerId( Long.valueOf( customerId ) );

		LegalStatus legalRedress = request.getLegalStatus();

		createWorkLoadData.setPrestataireName( objectFactoryWorkLoad.createCreateWorkLoadDataPrestataireName( legalRedress.getResponsible() ) );
		String legalDate = DateUtils.getDateFormatDDMMYYYY( legalRedress.getDateRedress() );

		JAXBElement<String> itemDate = objectFactoryWorkLoad.createCreateWorkLoadDataItemDate( legalDate );
		createWorkLoadData.setItemDate( itemDate );
		WorkItemType workItemType = getWorkItemTypeFromTheme( legalRedress.getLegarRedressType() );
		createWorkLoadData.setType( workItemType );

		if ( CollectionUtils.isNotEmpty( request.getNotes() ) )
		{

			createWorkLoadData.setComment( request.getNotes().get( 0 ).getNote() );
		}

		return createWorkLoadData;

	}

	private WorkItemType getWorkItemTypeFromTheme( EnumLegalStatus legalRedressEnum )
	{

		if ( legalRedressEnum.equals( EnumLegalStatus.LIQUIDATION ) )
		{
			return WorkItemType.MISE_EN_LIQUIDATION;
		}
		else if ( legalRedressEnum.equals( EnumLegalStatus.RECEIVERSHIP ) )
		{
			return WorkItemType.MISE_EN_REDRESSEMENT;
		}

		return WorkItemType.AUTRE;

	}

	/**
	 * Methode pour l'adaptation des appels vers le Dunning RegisterCard
	 * 
	 * @param customerProblem
	 * @return RegisterCardActionData
	 */
	public RegisterCardActionData adaptRegisterCard( Request customerProblem )
	{

		RegisterCardActionData registerCardActionData = new RegisterCardActionData();

		registerCardActionData.setCoid( Long.valueOf( customerProblem.getContract().getCoId() ) );
		if ( customerProblem.getTeam() != null )
		{
			registerCardActionData.setUserName( customerProblem.getTeam() );
		}
		else
		{
			registerCardActionData.setUserName( customerProblem.getChannel() );
		}

		Option option = customerProblem.getOption();
		RegisterCardStatus registerCardStatus = null;
		for ( Parameter param : option.getParameters() )
		{
			registerCardStatus = RegisterCardStatus.valueOf( param.getValue() );
		}
		registerCardActionData.setRegisterCardStatus( registerCardStatus );
		String date = new SimpleDateFormat( "ddMMyyyy" ).format( new Date() );
		registerCardActionData.setCreationDate( date ); // date courante format DDMMYYYY

		return registerCardActionData;
	}

	/**
	 * @param objectFactoryRegisterCard the objectFactoryRegisterCard to set
	 */
	public void setObjectFactoryRegisterCard( ObjectFactory objectFactoryRegisterCard )
	{
		this.objectFactoryRegisterCard = objectFactoryRegisterCard;
	}

	/**
	 * @param objectFactoryWorkLoad the objectFactoryWorkLoad to set
	 */
	public void setObjectFactoryWorkLoad( fcm.gp.oc.schemas.dunning.data.dunningworkitem.ObjectFactory objectFactoryWorkLoad )
	{
		this.objectFactoryWorkLoad = objectFactoryWorkLoad;
	}
}
