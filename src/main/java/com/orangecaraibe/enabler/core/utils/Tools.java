package com.orangecaraibe.enabler.core.utils;

import java.util.ArrayList;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Cette classe regroupe un ensemble de methodes utilitaires :<br>
 * - affichage des erreurs d'exceptions<br>
 * - modification de chaines pour avoir le meme nombre de tokens sur une serie de chaines<br>
 * - traitement de chaines (extraction, inversion, remplacement sous chaine, gestion des quotes)<br>
 * - verification de masques (savoir si une chaine est un nombre, un decimal)
 */
public class Tools
{

	/**
	 * Découpe une chaine en tableau de chaines, selon un s�parateur
	 * 
	 * @param str chaine d'entr�e
	 * @param sep s�parateur
	 * @return tableau des �l�ments trouv�s (sans le s�p), ou null si chaine d'entr�e vide
	 */
	public static String[] stringToArray( String str, String sep )
	{
		if ( str == null || sep == null )
			return null;
		int lg = str.length() - sep.length();
		if ( lg < 0 )
		{
			String res[] = new String[1];
			res[0] = str;
			return res;
		}
		String tmp = str;
		ArrayList<String> vct = new ArrayList<String>();
		while ( true )
		{
			int deb = tmp.indexOf( sep );
			if ( deb < 0 )
				break;
			vct.add( tmp.substring( 0, deb ) );
			int fin = deb + sep.length();
			if ( fin < tmp.length() )
				tmp = tmp.substring( fin );
			else
				tmp = "";
		}
		if ( tmp.length() > 0 )
			vct.add( tmp );
		if ( str.endsWith( sep ) )
			vct.add( "" );

		String val[] = new String[vct.size()];
		for ( int i = 0; i < val.length; i++ )
			val[i] = vct.get( i );
		vct.clear();
		return val;
	}


		/**
		 * 
		 * 
		 * @param mapper
		 * @param objectToLog
		 */
		public static String jsonMappingWrite(Logger LOGGER, Object objectToLog) throws JsonProcessingException {
			String formattedJson;
			try {
				formattedJson = getJsonMapper().writeValueAsString(objectToLog);
				LOGGER.debug("MAPPED CONTENT : {}", formattedJson);
			} catch (Exception e) {
				LOGGER.debug("Une erreur s'est produite lors du logage du mapped content.");
				throw e;
			}
			return formattedJson;
		}

		/**
		 * 
		 * @return
		 */
		public static ObjectMapper getJsonMapper() {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			return mapper;
		}


}
