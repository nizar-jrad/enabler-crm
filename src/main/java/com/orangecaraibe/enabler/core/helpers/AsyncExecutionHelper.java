/**
 * 
 */
package com.orangecaraibe.enabler.core.helpers;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import com.orangecaraibe.enabler.crm.soa.provider.ThreadMonitoringService;

/**
 * @author ncrtest2
 */
@SuppressWarnings( "rawtypes" )
@Component( "core.helper.asyncExecutionHelper" )
public class AsyncExecutionHelper
	implements ApplicationListener
{

	private static final Logger log = LoggerFactory.getLogger( AsyncExecutionHelper.class );

	// private ScheduledThreadPoolExecutor taskExec = null;
	/**
	 * ORANGE-1381 - QC-913 - Ajout du MBean de suppervision
	 */
	private ThreadMonitoringService taskExec;

	/**
	 * ORANGE-1381 - QC-913 - Ajout de paramètres au constructeur
	 * 
	 * @param threadPoolSize
	 * @param applicationId
	 * @param processMethod
	 */
	public AsyncExecutionHelper( @Value( "${threadPoolSize}" )
	int threadPoolSize, @Value( "${application.id}" )
	final String applicationId, @Value( "${thread.monitoring.method.findandget}" )
	final String processMethod )
	{
		if ( taskExec == null )
		{
			ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor( Math.round( threadPoolSize / 2 ) );
			stpe.setMaximumPoolSize( threadPoolSize );
			stpe.setExecuteExistingDelayedTasksAfterShutdownPolicy( false );
			this.taskExec = new ThreadMonitoringService( applicationId, processMethod, stpe );
		}
	}

	public <T> Future<T> executeTask( Callable<T> task )
	{
		return taskExec.getExecutorService().submit( task );
	}

	private void contextDestroyed()
	{
		log.info( "Arret du du pool de thread." );
		taskExec.getExecutorService().shutdownNow();

	}

	@Override
	public void onApplicationEvent( ApplicationEvent event )
	{
		if ( event instanceof ContextStoppedEvent )
		{
			contextDestroyed();
		}
	}

}
