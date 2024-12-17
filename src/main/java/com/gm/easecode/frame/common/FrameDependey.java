package com.gm.easecode.frame.common;

import java.util.Map;

public class FrameDependey {
	/** 框架依赖信息 */
	private Dependey dependey;
	/** 框架提供的依赖包信息，key：标识，比如：swagger，value：依赖信息 */
	private Map<String, Dependey> dependencyMap;
	/** 框架提供的Plugin信息，key：标识，比如：compiler */
	private Map<String, Plugin> pluginMap;
	/** 框架的动态变量值映射 */
	private Map<String, String> aliasVariableMap;
	
	public FrameDependey() {
		
	}
	
	public FrameDependey(Dependey dependey, Map<String, Dependey> dependencyMap, Map<String, Plugin> pluginMap, Map<String, String> aliasVariableMap) {
		this.dependey = dependey;
		this.dependencyMap = dependencyMap;
		this.pluginMap = pluginMap;
		this.aliasVariableMap = aliasVariableMap;
	}
	public Dependey getDependey() {
		return dependey;
	}
	public Map<String, Dependey> getDependencyMap() {
		return dependencyMap;
	}
	public Map<String, Plugin> getPluginMap() {
		return pluginMap;
	}
	public void setDependey(Dependey dependey) {
		this.dependey = dependey;
	}
	public void setDependencyMap(Map<String, Dependey> dependencyMap) {
		this.dependencyMap = dependencyMap;
	}
	public void setPluginMap(Map<String, Plugin> pluginMap) {
		this.pluginMap = pluginMap;
	}

	public Map<String, String> getAliasVariableMap() {
		return aliasVariableMap;
	}

	public void setAliasVariableMap(Map<String, String> aliasVariableMap) {
		this.aliasVariableMap = aliasVariableMap;
	}
	
}
