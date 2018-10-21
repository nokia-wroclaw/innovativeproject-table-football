package com.tablefootbal.server.config;

import org.apache.catalina.Valve;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Collection;

@Configuration
@PropertySource("classpath:connector.properties")
public class ConnectorConfiguration
{
	final private
	Environment env;
	
	@Value("${server.port}")
	private int httpsPort;
	
	@Autowired
	public ConnectorConfiguration(Environment env)
	{
		this.env = env;
	}
	
	@Bean
	ServletWebServerFactory servletContainer()
	{
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(httpConnector());
		
		Collection<Valve> valves = tomcat.getContextValves();
		
		return tomcat;
	}
	
	@Bean
	public Connector httpConnector()
	{
		final int RANDOM_PORT = 0;
		
		Connector connector = new Connector(env.getProperty("http.protocol"));
		connector.setScheme("http");
		
		String httpPort = env.getProperty("http.port");
		if (httpPort != null)
		{
			connector.setPort(Integer.parseInt(httpPort));
		}
		else
		{
			connector.setPort(RANDOM_PORT);
		}
		connector.setRedirectPort(httpsPort);
		connector.setSecure(Boolean.getBoolean(env.getProperty("http.secure")));
		
		return connector;
	}
}

