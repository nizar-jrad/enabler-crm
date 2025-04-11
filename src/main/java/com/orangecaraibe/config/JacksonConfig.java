package com.orangecaraibe.config;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.orangecaraibe.api.catalogue.ws.RFC3339DateFormat;

@Configuration
public class JacksonConfig
{

	@Bean( name = "restMapper" )
	public ObjectMapper comarchMapper()
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
		mapper.configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false );
		mapper.setSerializationInclusion( Include.NON_EMPTY );
		mapper.setDateFormat( new RFC3339DateFormat() );
		mapper.registerModule( new JavaTimeModule() );
		mapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
		JsonNullableModule jnm = new JsonNullableModule();
		mapper.registerModule( jnm );
		return mapper;
	}
}
