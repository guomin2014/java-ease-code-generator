package com.gm.easecode.frame.common;

import java.util.Properties;

public class Plugin extends Dependey {

	private Properties configuration;
	
	public Plugin() {
		
	}
	
	public Plugin(String groupId, String artifactId) {
		super(groupId, artifactId);
	}

	public Properties getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Properties configuration) {
		this.configuration = configuration;
	}
	
}
