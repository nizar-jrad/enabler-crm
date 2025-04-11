/**
 *
 */
package com.orangecaraibe.enabler.core.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author sgodard classe utilitaire permettant de récupérer un fichier de properties dans un singleton
 */
@Component
public class PropertiesHelper
{

	/** Fichier de Properties */
	private Properties propertiesFile = null;

	public Properties getPropertiesFileFromClassPath( String fileName )
		throws FileNotFoundException, IOException
	{
		if ( this.propertiesFile == null )
		{

			this.propertiesFile = new Properties();

			if ( fileName != null && !StringUtils.isEmpty( fileName ) )
			{
				InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream( fileName );

				if ( inputStream != null )
				{

					this.propertiesFile.load( inputStream );

				}
			}

		}

		return this.propertiesFile;
	}

	public String getProperty( String key, String fileName )
		throws FileNotFoundException, IOException
	{

		return getPropertiesFileFromClassPath( fileName ).getProperty( key );
	}

	public void setPropertiesFile( Properties propertiesFile )
	{
		this.propertiesFile = propertiesFile;
	}

}
