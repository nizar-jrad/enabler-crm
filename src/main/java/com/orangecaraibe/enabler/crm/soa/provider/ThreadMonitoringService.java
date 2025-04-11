/**
 * 
 */
package com.orangecaraibe.enabler.crm.soa.provider;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * ORANGE-1381 - QC-913 - Implementation de la classe ThreadMonitoringServiceMBean
 * 
 * @author sverdun
 */
@ManagedResource
public class ThreadMonitoringService
	implements ThreadMonitoringServiceMBean
{

	private static final Logger logger = LoggerFactory.getLogger( ThreadMonitoringService.class );

	private String applicationId;

	private String processMethod;

	private ThreadPoolExecutor executorService;

	/**
	 * Constructor of ExecuteProcessStepMBean
	 * 
	 * @param applicationId
	 * @param processMethod
	 * @param executorService
	 */
	public ThreadMonitoringService( String applicationId, String processMethod, ThreadPoolExecutor executorService )
	{

		this.applicationId = applicationId;
		this.processMethod = processMethod;
		this.executorService = executorService;

		// on enregistre la supervision
		this.registerMBean();
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.technicaldeliverydevice.provider.mbean.
	 * ExecuteProcessStepMBean#getCompletedTaskCount()
	 */
	@Override
	@ManagedAttribute( description = "Returns the total number of completed tasks" )
	public long getCompletedTaskCount()
	{
		return executorService.getCompletedTaskCount();
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.technicaldeliverydevice.provider.mbean.
	 * ExecuteProcessStepMBean#getLargestPoolSize()
	 */
	@Override
	@ManagedAttribute( description = "Returns the largest number of threads that have been in the pool" )
	public int getLargestPoolSize()
	{
		return executorService.getLargestPoolSize();
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.technicaldeliverydevice.provider.mbean.
	 * ExecuteProcessStepMBean#getMaximumPoolSize()
	 */
	@Override
	@ManagedAttribute( description = "Returns the max size allowed in the pool of threads" )
	public int getMaximumPoolSize()
	{
		return executorService.getMaximumPoolSize();
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.technicaldeliverydevice.provider.
	 * ExecuteProcessStepServiceMBean#setMaximumPoolSize(int)
	 */
	@Override
	@ManagedAttribute( description = "Sets the max size allowed in the pool of threads" )
	public void setMaximumPoolSize( int maximumPoolSize )
	{
		executorService.setMaximumPoolSize( maximumPoolSize );
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.technicaldeliverydevice.provider.mbean. ExecuteProcessStepMBean#getTaskCount()
	 */
	@Override
	@ManagedAttribute( description = "Returns the total number of tasks that have ever been scheduled for execution " )
	public long getTaskCount()
	{
		return executorService.getTaskCount();
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.technicaldeliverydevice.provider.
	 * ExecuteProcessStepServiceMBean#getCorePoolSize()
	 */
	@Override
	@ManagedAttribute( description = "Returns the size of the core pool of threads" )
	public int getCorePoolSize()
	{
		return executorService.getCorePoolSize();
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.technicaldeliverydevice.provider.
	 * ExecuteProcessStepServiceMBean#setCorePoolSize(int)
	 */
	@Override
	@ManagedAttribute( description = "Sets the core size of the pool" )
	public void setCorePoolSize( int corePoolSize )
	{
		executorService.setCorePoolSize( corePoolSize );
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.technicaldeliverydevice.provider.
	 * ExecuteProcessStepServiceMBean#getApplicationId()
	 */
	@Override
	public String getApplicationId()
	{
		return this.applicationId;
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.technicaldeliverydevice.provider.
	 * ExecuteProcessStepServiceMBean#setApplicationId(java.lang.String)
	 */
	@Override
	public void setApplicationId( String applicationId )
	{
		this.applicationId = applicationId;
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.ordermanagement.provider. ThreadMonitoringServiceMBean#getProcessMethod()
	 */
	@Override
	public String getProcessMethod()
	{
		return this.processMethod;
	}

	/*
	 * (non-Javadoc)
	 * @see com.orangecaraibe.enabler.soa.ordermanagement.provider.
	 * ThreadMonitoringServiceMBean#setProcessMethod(java.lang.String)
	 */
	@Override
	public void setProcessMethod( String processMethod )
	{
		this.processMethod = processMethod;
	}

	/**
	 * @return the executorService
	 */
	public ThreadPoolExecutor getExecutorService()
	{
		return executorService;
	}

	/**
	 * @param executorService the executorService to set
	 */
	public void setExecutorService( ThreadPoolExecutor executorService )
	{
		this.executorService = executorService;
	}

	/**
	 * Enregistrement du MBean
	 */
	public void registerMBean()
	{
		try
		{

			List<MBeanServer> servers = MBeanServerFactory.findMBeanServer( null );
			for ( MBeanServer beanServer : servers )
			{
				ObjectName isAliveName = new ObjectName( applicationId + ":name=" + getClass().getSimpleName()
					+ ( ( processMethod != null && processMethod.length() > 0 ) ? " - " + processMethod : "" ) );
				if ( beanServer.isRegistered( isAliveName ) )
				{
					beanServer.unregisterMBean( isAliveName );
				}
				beanServer.registerMBean( this, isAliveName );
				logger.info( "IsAliveServiceMBean successfully registered for application " + applicationId );
			}
		}
		catch ( Exception e )
		{
			logger.error( "impossible to register isAlive MBean", e );
		}
	}
}
