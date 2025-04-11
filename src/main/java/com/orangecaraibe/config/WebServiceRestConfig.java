package com.orangecaraibe.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangecaraibe.api.catalogue.ws.ApiClient;
import com.orangecaraibe.api.catalogue.ws.RFC3339DateFormat;
import com.orangecaraibe.api.catalogue.ws.api.CastorApi;
import com.orangecaraibe.api.catalogue.ws.api.ExternalIdentifiersApi;
import com.orangecaraibe.enabler.RestClientInterceptorEnabler;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebServiceRestConfig
{

	@Value( "${soa.username}" )
	private String userName;

	@Value( "${soa.password}" )
	private String password;

	@Value( "${url.castor.api}" )
	private String castorApiUrl;

	@Value( "${url.externalidentifiers.api}" )
	private String externalIdentifiersUrl;

	@Value( "${connection.request.timeout:120000}" )
	private int responseTimeOut;

	@Value( "${connection.timeout:2000}" )
	private int connectionTimeOut;

	@Value( "${rest.max.idle.time:540000}" )
	private int maxIdleTime;

	@Value( "${rest.max.life.time:110000}" )
	private int maxLifeTime;

	@Value( "${rest.max.connections:50}" )
	private int maxConnection;

	@Autowired
	RestClientInterceptorEnabler restClientInterceptorEnabler;

	@Bean
	public CastorApi castorApi( @Qualifier( "restMapper" )
	ObjectMapper mapper )
	{

		ApiClient webclient =
			new ApiClient(	WebClient.builder().clientConnector( clientHttpConnector( "castorApi" ) ).filter( restClientInterceptorEnabler.createEnablerLogIn() ).filter( restClientInterceptorEnabler.createEnablerLogOUt() ).build(),
							mapper, new RFC3339DateFormat() );
		CastorApi castorApi = new CastorApi( webclient );
		castorApi.getApiClient().setBasePath( castorApiUrl );
		castorApi.getApiClient().setUsername( userName );
		castorApi.getApiClient().setPassword( password );
		return castorApi;
	}

	@Bean
	public ExternalIdentifiersApi externalIdentifiersApi( @Qualifier( "restMapper" )
	ObjectMapper mapper )
	{
		ApiClient webclient =
			new ApiClient(	WebClient.builder().clientConnector( clientHttpConnector( "externalIdentifierApi" ) ).filter( restClientInterceptorEnabler.createEnablerLogIn() ).filter( restClientInterceptorEnabler.createEnablerLogOUt() ).build(),
							mapper, new RFC3339DateFormat() );
		ExternalIdentifiersApi externalIdentifiersApi = new ExternalIdentifiersApi( webclient );
		externalIdentifiersApi.getApiClient().setBasePath( externalIdentifiersUrl );
		externalIdentifiersApi.getApiClient().setUsername( userName );
		externalIdentifiersApi.getApiClient().setPassword( password );
		return externalIdentifiersApi;
	}

	ClientHttpConnector clientHttpConnector( String name )
	{

		ConnectionProvider provider =
			ConnectionProvider.builder( name ).maxConnections( maxConnection ).maxIdleTime( Duration.ofMillis( maxIdleTime ) ).maxLifeTime( Duration.ofMillis( maxLifeTime ) ).build();
		HttpClient httpClient = HttpClient.create( provider ).responseTimeout(
																				Duration.ofMillis( responseTimeOut ) ).tcpConfiguration( tcpClient -> tcpClient.option( ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeOut ) );
		return new ReactorClientHttpConnector( httpClient );
	}
}
