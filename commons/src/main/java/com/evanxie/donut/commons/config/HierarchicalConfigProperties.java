package com.evanxie.donut.commons.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.evanxie.donut.commons.exception.ApplicationException;

//This class implements the interface ConfigProperties
public class HierarchicalConfigProperties{	
	
	private static final Logger logger = LoggerFactory.getLogger(HierarchicalConfigProperties.class);
	private static Map<String, String> propertiesMap=new HashMap<String,String>();
	
/*	Set properties which this project will use. 
	If some properties have no default value, set it as null.*/	
	public HierarchicalConfigProperties(){
		super();
		//Properties which have not default value
		//These properties must be set one value at least in the method initConfigProperties().
		propertiesMap.put("donut.home", null);
		propertiesMap.put("donut.conf.dir", null);
		//Properties which have default value
		propertiesMap.put("donut.webserver.port", "3000");
		propertiesMap.put("donut.webserver.minthreads", "10");
		propertiesMap.put("donut.webserver.maxthreads", "1000");		
	}	
	
	//init the variable donut_HOME to propertiesMap
	private boolean initDonutHome(){
		//During developing, set the DONUT_HOME=.. 
		String DONUT_HOME=System.getProperty("DONUT_HOME");	
		if(null==DONUT_HOME){
			logger.error("The environment variable DONUT_HOME hava not been configured.");
			logger.error("Set the donut_HOME and try again!.");
			return false;
		}
		propertiesMap.put("donut.home", DONUT_HOME);
		System.setProperty("donut.home",DONUT_HOME);
		return true;
	}
	
	//According a certain rule, set the properties of propertiesMap in the JVM
	/*	rule of setProperties: 
  	the temporary properties have highly priority,
	the properties that configured in the file have 2nd priority,
	the default properties have lowerd priority.*/
	private void setPropertiesByRule(){
		for (String key : propertiesMap.keySet()) {			
			if(null==System.getProperty(key)){
				Configuration config;
				try {
					config = ApplicationProperties.get();
					String propValue=config.getString(key, propertiesMap.get(key));	
					System.setProperty(key, propValue);
				} catch (ApplicationException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	//init all properties that have no default value, then set them in JVM by rule.
	public boolean initConfigProperties(){
		//if faild to init HOME, return false
		if(false==initDonutHome()){
			return false;
		}		
		//set some properties according donut.home
		propertiesMap.put("donut.conf.dir", propertiesMap.get("donut.home")+"/conf");
		//Put the donut.conf.dir in JVM for next method setPropertiesByRule() to use.
		System.setProperty("donut.conf.dir", propertiesMap.get("donut.conf.dir"));
		
		setPropertiesByRule();	
	return true;
	}
}