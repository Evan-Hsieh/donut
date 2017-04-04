package com.evanxie.donut.web.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.evanxie.donut.commons.config.HierarchicalConfigProperties;
import com.evanxie.donut.commons.exception.ApplicationException;

//In order to make develop code conveniently, developers could set the parameter as "-DDONUT_HOME=../integration -Dlog4j.configuration=file:../integration/conf/application-log4j.properties"
//and then run the class StartWeb as Java application.
public class StartWeb{
	private static final Logger logger = LoggerFactory.getLogger(StartWeb.class);
	private static EmbeddedServer embeddedServer;
	public static void main(String[] args) throws ApplicationException {
		//If failed to init config properties, web server will not start.
		if(false==new HierarchicalConfigProperties().initConfigProperties()){
			return;
		}
		
		
		
		//Start web server
		int port=Integer.valueOf(System.getProperty("donut.webserver.port"));
        logger.info("starting application in port:{}",port);
        try{
        	if(args.length==0){
            	embeddedServer=new EmbeddedServer(port, "src/main/webapp");
            }else{
            	embeddedServer=new EmbeddedServer(port, true, args[0]);
            }
        	embeddedServer.start();
        }catch(Exception e){
        	logger.error(e.getMessage());      	
        	System.exit(0);
        }
	}
	
}