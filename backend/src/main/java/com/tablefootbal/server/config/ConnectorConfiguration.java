package com.tablefootbal.server.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.File;

@Configuration
@PropertySource("classpath:connector.properties")
public class ConnectorConfiguration {
    @Value("${server.ssl.key-store}")
    private String keystoreLocation;

    @Value("${server.ssl.trust-store}")
    private String truststoreLocation;

    @Value("${server.ssl.trust-store-password}")
    private String truststorePass;

    @Value("${server.ssl.key-store-password}")
    private String keystorePass;

    final private
    Environment env;

    @Value("${server.port}")
    private int httpsPort;

    @Value("${server.sensor.connection.port}")
    private int sensorHttpsPort;

    @Autowired
    public ConnectorConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        tomcat.addAdditionalTomcatConnectors(sensorHttpsConnector());

        return tomcat;
    }

    @Bean
    public Connector httpConnector() {
        final int RANDOM_PORT = 0;

        Connector connector = new Connector(env.getProperty("http.protocol"));
        connector.setScheme("http");

        String httpPort = env.getProperty("http.port");
        if (httpPort != null) {
            connector.setPort(Integer.parseInt(httpPort));
        } else {
            connector.setPort(RANDOM_PORT);
        }
        connector.setRedirectPort(httpsPort);
        connector.setSecure(Boolean.getBoolean(env.getProperty("http.secure")));

        return connector;
    }

    @Bean
    public Connector sensorHttpsConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();

        File keystore = new File(keystoreLocation);
        File truststore = new File(truststoreLocation);
        connector.setScheme("https");
        connector.setSecure(true);
        connector.setPort(sensorHttpsPort);
        protocol.setSSLEnabled(true);
        protocol.setClientAuth("true");
        protocol.setSSLCipherSuite("TLS_RSA_WITH_AES_128_CBC_SHA, TLS_RSA_WITH_AES_256_CBC_SHA");
        protocol.setKeystoreFile(keystore.getAbsolutePath());
        protocol.setKeystorePass(keystorePass);
        protocol.setTruststoreFile(truststore.getAbsolutePath());
        protocol.setTruststorePass(truststorePass);
        return connector;
    }
}

