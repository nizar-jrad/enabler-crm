/**
 *
 */
package com.orangecaraibe.enabler.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author sgodard
 */
public class TextUtils
{
	/**
	 * Reduit la longueur d'une chaîne passée en paramètre (text) à la longueur spécifiée par le paramètre (size) et
	 * retourne la chaîne créée
	 * 
	 * @param text
	 * @param size
	 * @return
	 */
	public static String reduceStringLength( String text, int size )
	{

		String formatedText = "";

		if ( text != null && !StringUtils.isEmpty( text ) )
		{

			if ( size > 0 )
			{

				// Si la taille du texte est supérieure à la taille voulue, on
				// réduit la chaîne
				// Sinon, on affiche la chaîne d'origine
				if ( text.length() > size )
				{

					formatedText = text.substring( 0, size );
				}
				else
				{
					formatedText = text;
				}
			}

		}

		return formatedText;

	}
}
