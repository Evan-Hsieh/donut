package com.evanxie.donut.web.commons;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evanxie.donut.commons.config.ApplicationProperties;


public class EmbeddedServer {
	public static final Logger logger = LoggerFactory.getLogger(EmbeddedServer.class);
    private static final int DEFAULT_BUFFER_SIZE = 16192;
    protected final Server server = new Server();

    public EmbeddedServer(int port,String path) throws IOException{
    	this(port,path,false,null);
    }
    /**
     * use war to start
     * @param port
     * @param isWar
     * @param warPath
     * @throws IOException
     */
    public EmbeddedServer(int port,boolean isWar,String warPath) throws IOException{
    	this(port,null,isWar,warPath);
    }
    private EmbeddedServer(int port, String path,boolean isWar,String warPath) throws IOException {
        Connector connector = getConnector(port);
        server.addConnector(connector);
        WebAppContext application = getWebAppContext(path,isWar,warPath);
        server.setHandler(application);
        server.setStopAtShutdown(true);
    }

    protected WebAppContext getWebAppContext(String path,boolean isWar,String warPath) {
    	WebAppContext application;
    	if(isWar){
    		application=new WebAppContext();
    		application.setWar(warPath);
    		return application;
        }else{
        	application = new WebAppContext(path, "/");
            application.setConfigurationDiscovered(true);
            application.setParentLoaderPriority(true);
            application.setClassLoader(Thread.currentThread().getContextClassLoader());
            return application;
        }
    }

    protected Connector getConnector(int port) throws IOException {
        HttpConfiguration http_config = new HttpConfiguration();
        // this is to enable large header sizes when Kerberos is enabled with AD
        final int bufferSize = getBufferSize();
        http_config.setResponseHeaderSize(bufferSize);
        http_config.setRequestHeaderSize(bufferSize);

        ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(http_config));
        connector.setPort(port);
        connector.setHost("0.0.0.0");
        server.addConnector(connector);
        return connector;
    }

    protected Integer getBufferSize() {
        try {
            Configuration configuration = ApplicationProperties.get();
            return configuration.getInt("sysimple.jetty.request.buffer.size", DEFAULT_BUFFER_SIZE);
        } catch (Exception e) {
            // do nothing
        }

        return DEFAULT_BUFFER_SIZE;
    }

    public void start() throws Exception {
        server.start();  
        logger.info("********************************************************");
        logger.info("The Dodut Has Started !!!");
        server.join();
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            logger.warn("Error during shutdown", e);
        }
    }

}