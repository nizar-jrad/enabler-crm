/**
 *
 */
package com.orangecaraibe.enabler.ussd.soa.consumer.integrationtestsDAO;

import java.util.List;

/**
 * @author sgodard
 */
public interface ManageUSSDTestCaseDAO
{
	/** * GP Engage (1 sec)* */
	@SuppressWarnings( "unchecked" )
	public List<String> getMSISDNForGPEngage();

	/** * GP non Engage (50 sec)* */
	@SuppressWarnings( "unchecked" )
	public List<String> getMSISDNForGPNonEngage();

	/** * Pro non Engage Gestionnaire (76 sec)* */
	@SuppressWarnings( "unchecked" )
	public List<String> getMSISDNForProNonEngageGestionnaire();

	/** * Pro non Engage non Gestionnaire (88 sec)* */
	@SuppressWarnings( "unchecked" )
	public List<String> getMSISDNForProNonEngageNonGestionnaire();

	/** * Pro Engage Gestionnaire (6sec)* */
	@SuppressWarnings( "unchecked" )
	public List<String> getMSISDNForProEngageGestionnaire();

	/** * Pro Engage non Gestionnaire* */
	@SuppressWarnings( "unchecked" )
	public List<String> getMSISDNForProEngageNonGestionnaire();
}
