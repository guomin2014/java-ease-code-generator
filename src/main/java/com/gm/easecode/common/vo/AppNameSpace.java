package com.gm.easecode.common.vo;

import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.util.TableUtil;
import com.gm.easecode.config.AppConfig;
import com.gm.easecode.frame.common.FrameDependey;

public class AppNameSpace {

	private final AppConfig config;
	
	private String baseJavaPath;
	private String baseResourcePath;
	private String baseWebPath;
	
	private String applicationYmlFilePath;
	private String applicationYmlFileName;
	private String applicationServiceYmlFilePath;
	private String applicationServiceYmlFileName;
	private String applicationServiceDevYmlFilePath;
	private String applicationServiceDevYmlFileName;
	private String logbackXmlFilePath;
	private String logbackXmlFileName;
	private String pomXmlFilePath;
	private String pomXmlFileName;
	
	private FrameDependey frameDependey;
	
	public AppNameSpace(AppConfig config) {
		this.config = config;
		String moduleFullName = "${moduleNum}.${moduleName}";
//		String pkgRoot = config.getRootPackage();
		String javaPathPrefix = "";
		String resourcePathPrefix = "";
		String webPathPrefix = "";
		String javaPath;
		String resourcePath;
		String webPath;
		CodeStyleMode codeStyle = this.config.getCodeStyle();//编码风格，0：普通，1：Maven
		if(codeStyle == CodeStyleMode.MAVEN) {//maven风格
			javaPath = "src/main/java/";
			resourcePath = "src/main/resources/";
			webPath = "src/main/webapp/";
			if (config.isCodeGroupByModule()) {
				String projSrcPath = TableUtil.formatPath(config.getProjSrcPath());
				javaPathPrefix = projSrcPath + moduleFullName;
				resourcePathPrefix = projSrcPath + moduleFullName;
				webPathPrefix = projSrcPath + moduleFullName;
			}
		} else {//普通风格
			javaPath = "";
			resourcePath = "";
			webPath = "";
			javaPathPrefix = TableUtil.formatPath(config.getProjSrcPath());
			resourcePathPrefix = TableUtil.formatPath(config.getProjConfPath());
			webPathPrefix = TableUtil.formatPath(config.getWebRootPath());
			if (config.isCodeGroupByModule()) {
				if (config.getConfigStyle() == ConfigStyleMode.XML) {
					resourcePathPrefix = resourcePathPrefix + "${moduleName}";
				}
				javaPathPrefix = javaPathPrefix + moduleFullName;
			}
		}
		this.baseJavaPath = TableUtil.formatPath(javaPathPrefix) + javaPath;
		this.baseResourcePath = TableUtil.formatPath(resourcePathPrefix) + resourcePath;
		this.baseWebPath = TableUtil.formatPath(webPathPrefix) + webPath;
		this.applicationYmlFilePath = baseResourcePath;
		this.applicationYmlFileName = "application.yml";
		this.applicationServiceYmlFilePath = baseResourcePath;
		this.applicationServiceYmlFileName = config.getAppName() + ".yml";
		this.applicationServiceDevYmlFilePath = baseResourcePath;
		this.applicationServiceDevYmlFileName = config.getAppName() + "-dev" + ".yml";
		this.logbackXmlFilePath = baseResourcePath;
		this.logbackXmlFileName = "logback-spring.xml";
		this.pomXmlFilePath = TableUtil.formatPath(javaPathPrefix);
		this.pomXmlFileName = "pom.xml";
	}
	public AppConfig getConfig() {
		return config;
	}
	public String getBaseJavaPath(String moduleNum, String moduleName) {
		if (StringUtils.isEmpty(moduleNum)) {
			moduleNum = "00";
		}
		if (StringUtils.isEmpty(moduleName)) {
			moduleName = "default";
		}
		return this.baseJavaPath.replace("${moduleNum}", moduleNum).replace("${moduleName}", moduleName);
	}
	public String getBaseResourcePath(String moduleNum, String moduleName) {
		if (StringUtils.isEmpty(moduleNum)) {
			moduleNum = "00";
		}
		if (StringUtils.isEmpty(moduleName)) {
			moduleName = "default";
		}
		return this.baseResourcePath.replace("${moduleNum}", moduleNum).replace("${moduleName}", moduleName);
	}
	public String getBaseWebPath(String moduleNum, String moduleName) {
		if (StringUtils.isEmpty(moduleNum)) {
			moduleNum = "00";
		}
		if (StringUtils.isEmpty(moduleName)) {
			moduleName = "default";
		}
		return this.baseWebPath.replace("${moduleNum}", moduleNum).replace("${moduleName}", moduleName);
	}
	public String getApplicationYmlFilePath() {
		return applicationYmlFilePath;
	}
	public String getApplicationYmlFileName() {
		return applicationYmlFileName;
	}
	public String getApplicationServiceYmlFilePath() {
		return applicationServiceYmlFilePath;
	}
	public String getApplicationServiceYmlFileName() {
		return applicationServiceYmlFileName;
	}
	public String getApplicationServiceDevYmlFilePath() {
		return applicationServiceDevYmlFilePath;
	}
	public String getApplicationServiceDevYmlFileName() {
		return applicationServiceDevYmlFileName;
	}
	public String getLogbackXmlFilePath() {
		return logbackXmlFilePath;
	}
	public String getLogbackXmlFileName() {
		return logbackXmlFileName;
	}
	public String getPomXmlFilePath() {
		return pomXmlFilePath;
	}
	public String getPomXmlFileName() {
		return pomXmlFileName;
	}
	public FrameDependey getFrameDependey() {
		return frameDependey;
	}
	public void setFrameDependey(FrameDependey frameDependey) {
		this.frameDependey = frameDependey;
	}
}
