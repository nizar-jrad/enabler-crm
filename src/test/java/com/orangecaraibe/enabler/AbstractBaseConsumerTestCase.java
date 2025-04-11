package com.orangecaraibe.enabler;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.orangecaraibe.EnablerCrmConfiguration;
import com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO.api.RemoveService;
import com.orangecaraibe.enabler.epoint.soa.consumer.integrationtestsDAO.impl.ManageCRMCreateOrderRemoveServiceDAOImpl;
import com.orangecaraibe.enabler.ussd.soa.consumer.integrationtestsDAO.impl.ManageUSSDTestCaseDAOImpl;

@SpringBootTest( classes = EnablerCrmConfiguration.class )
@RunWith( SpringJUnit4ClassRunner.class )
@ExtendWith( SpringExtension.class )
@AutoConfigureMockMvc
@ActiveProfiles( "test" )
public abstract class AbstractBaseConsumerTestCase
{

	protected static final Logger LOGGER = LoggerFactory.getLogger( AbstractBaseConsumerTestCase.class );

	@Resource( name = "manageUSSDTestCaseDAO" )
	public ManageUSSDTestCaseDAOImpl manageUSSDTestCaseDAO;

	@Resource( name = "manageCRMCreateOrderRemoveServiceDAO" )
	public ManageCRMCreateOrderRemoveServiceDAOImpl manageCRMCreateOrderRemoveServiceDAO;

	protected static List<String> msisdnForGPEngage = null;

	protected static List<String> msisdnForGPNonEngage = null;

	protected static List<String> msisdnForProEngageGestionnaire = null;

	protected static List<String> msisdnForProEngageNonGestionnaire = null;

	protected static List<String> msisdnForProNonEngageGestionnaire = null;

	protected static List<String> msisdnForProNonEngageNonGestionnaire = null;

	protected static List<RemoveService> removeServicePref = null;

	protected static List<RemoveService> removeServiceXtrem = null;

	protected static List<RemoveService> removeServiceFlotte = null;

	protected static List<RemoveService> removeServicePro = null;

	public void initRemoveServiceForPref()
	{
		if ( removeServicePref == null )
		{
			LOGGER.debug( "\n-------- init removeService Pref- Start" );

			removeServicePref = manageCRMCreateOrderRemoveServiceDAO.getRemoveServicePref();
			Assert.assertNotNull( removeServicePref );
		}

	}

	public void initRemoveServiceForXtrem()
	{
		if ( removeServiceXtrem == null )
		{
			LOGGER.debug( "\n-------- init removeService Xtrem- Start" );

			removeServiceXtrem = manageCRMCreateOrderRemoveServiceDAO.getRemoveServiceXtrem();
			Assert.assertNotNull( removeServiceXtrem );
		}

	}

	public void initRemoveServiceFlotte()
	{
		if ( removeServiceFlotte == null )
		{
			LOGGER.debug( "\n-------- init removeService Flotte- Start" );

			removeServiceFlotte = manageCRMCreateOrderRemoveServiceDAO.getRemoveServiceFlotte();
			Assert.assertNotNull( removeServiceFlotte );
		}

	}

	public void initRemoveServicePro()
	{
		if ( removeServicePro == null )
		{
			LOGGER.debug( "\n-------- init removeService Pro- Start" );

			removeServicePro = manageCRMCreateOrderRemoveServiceDAO.getRemoveServicePro();
			Assert.assertNotNull( removeServicePro );
		}

	}

	public void initMsisdnForGPEngage()
	{
		if ( msisdnForGPEngage == null )
		{
			LOGGER.debug( "\n-------- init GPEngage(MSISDN) - Start" );

			msisdnForGPEngage = manageUSSDTestCaseDAO.getMSISDNForGPEngage();
			Assert.assertNotNull( msisdnForGPEngage );
		}

	}

	public void initMsisdnForGPNonEngage()
	{
		if ( msisdnForGPNonEngage == null )
		{
			LOGGER.debug( "\n-------- init GPNonEngage(MSISDN) - Start" );

			msisdnForGPNonEngage = manageUSSDTestCaseDAO.getMSISDNForGPNonEngage();
			Assert.assertNotNull( msisdnForGPNonEngage );
		}

	}

	public void initMsisdnForProEngageGestionnaire()
	{
		if ( msisdnForProEngageGestionnaire == null )
		{
			LOGGER.debug( "\n-------- init ProEngageGestionnaire(MSISDN) - Start" );

			msisdnForProEngageGestionnaire = manageUSSDTestCaseDAO.getMSISDNForProEngageGestionnaire();
			Assert.assertNotNull( msisdnForProEngageGestionnaire );
		}

	}

	public void initMsisdnForProEngageNonGestionnaire()
	{
		if ( msisdnForProEngageNonGestionnaire == null )
		{
			LOGGER.debug( "\n-------- init ProEngageNonGestionnaire(MSISDN) - Start" );

			msisdnForProEngageNonGestionnaire = manageUSSDTestCaseDAO.getMSISDNForProEngageNonGestionnaire();
			Assert.assertNotNull( msisdnForProEngageNonGestionnaire );
		}

	}

	public void initMsisdnForProNonEngageGestionnaire()
	{
		if ( msisdnForProNonEngageGestionnaire == null )
		{
			LOGGER.debug( "\n-------- init ProNonEngageGestionnaire(MSISDN) - Start" );

			msisdnForProNonEngageGestionnaire = manageUSSDTestCaseDAO.getMSISDNForProNonEngageGestionnaire();
			Assert.assertNotNull( msisdnForProNonEngageGestionnaire );
		}

	}

	public void initMsisdnForProNonEngageNonGestionnaire()
	{
		if ( msisdnForProNonEngageNonGestionnaire == null )
		{
			LOGGER.debug( "\n-------- init ProNonEngageNonGestionnaire(MSISDN) - Start" );

			msisdnForProNonEngageNonGestionnaire = manageUSSDTestCaseDAO.getMSISDNForProNonEngageNonGestionnaire();
			Assert.assertNotNull( msisdnForProNonEngageNonGestionnaire );
		}

	}

}