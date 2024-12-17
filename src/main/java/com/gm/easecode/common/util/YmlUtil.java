package com.gm.easecode.common.util;

import java.util.Map;

import com.gm.easecode.common.vo.AppNameSpace;
import com.gm.easecode.config.AppConfig;
import com.gm.easecode.frame.common.Dependey;
import com.gm.easecode.frame.common.FrameDependey;
import com.gm.easecode.frame.common.Plugin;
import com.gm.easecode.source.DataSource;
import com.gm.easecode.source.provider.spi.MysqlDataSource;

public class YmlUtil {

	public static String getApplicationYmlContent(AppNameSpace nameSpace) throws Exception{
		AppConfig config = nameSpace.getConfig();
		String content = FileUtil.read(config.getApplicationYmlTpl(), config.getCharset());
		content = content.replace("${AppName}", config.getAppName());
		return content;
	}
	public static String getApplicationServiceYmlContent(AppNameSpace nameSpace) throws Exception{
		AppConfig config = nameSpace.getConfig();
		String content = FileUtil.read(config.getApplicationServiceYmlTpl(), config.getCharset());
		content = content.replace("${AppName}", config.getAppName());
		content = content.replace("${RootPackage}", config.getRootPackage());
		content = content.replace("${ControllerPackages}", config.getRootPackage());
		return content;
	}
	public static String getApplicationServiceDevYmlContent(AppNameSpace nameSpace) throws Exception{
		AppConfig config = nameSpace.getConfig();
		DataSource dataSource = config.getDataSource();
		String dataSourceUrl = "";
		String dataSourceUserName = "";
		String dataSourcePassword = "";
		if (dataSource instanceof MysqlDataSource) {
			MysqlDataSource mysqlDataSource = (MysqlDataSource)dataSource;
			dataSourceUrl = mysqlDataSource.getDbUrl();
			dataSourceUserName = mysqlDataSource.getDbUser();
			dataSourcePassword = mysqlDataSource.getDbPwd();
		}
		String content = FileUtil.read(config.getApplicationServiceDevYmlTpl(), config.getCharset());
		content = content.replace("${AppName}", config.getAppName());
		content = content.replace("${RootPackage}", config.getRootPackage());
		content = content.replace("${ControllerPackages}", config.getRootPackage());
		content = content.replace("${DataSourceUrl}", dataSourceUrl);
		content = content.replace("${DataSourceUserName}", dataSourceUserName);
		content = content.replace("${DataSourcePassword}", dataSourcePassword);
		return content;
	}
	public static String getLogbackXmlContent(AppNameSpace nameSpace) throws Exception{
		AppConfig config = nameSpace.getConfig();
		String content = FileUtil.read(config.getLogbackXmlTpl(), config.getCharset());
		content = content.replace("${AppName}", config.getAppName());
		return content;
	}
	public static String getPomXmlContent(AppNameSpace nameSpace) throws Exception{
		AppConfig config = nameSpace.getConfig();
		String content = FileUtil.read(config.getPomXmlTpl(), config.getCharset());
		content = content.replace("${AppName}", config.getAppName());
		content = content.replace("${AppDesc}", StringUtils.trim(config.getAppDesc()));
		//获取框架的父级依赖parentDependey
		//获取框架的plugins
		//获取Maven的依赖
		CustomStringBuilder parentBuild = new CustomStringBuilder("");
		CustomStringBuilder dependenciesBuild = new CustomStringBuilder("");
		CustomStringBuilder pluginBuild = new CustomStringBuilder("");
		FrameDependey frameDependey = nameSpace.getFrameDependey();
		if (frameDependey != null) {
			Dependey dependey = frameDependey.getDependey();
			if (dependey != null) {
				parentBuild.append("<parent>")
				.newLine().appendTab(2).append("<groupId>").append(dependey.getGroupId()).append("</groupId>")
				.newLine().appendTab(2).append("<artifactId>").append(dependey.getArtifactId()).append("</artifactId>")
				.newLine().appendTab(2).append("<version>").append(dependey.getVersion()).append("</version>")
				.newLine().appendTab(1).append("</parent>");
			}
			Map<String, Dependey> dependencyMap = frameDependey.getDependencyMap();
			if (dependencyMap != null) {
				dependenciesBuild.append("<dependencies>");
				for (Map.Entry<String, Dependey> entry : dependencyMap.entrySet()) {
					Dependey dep = entry.getValue();
					dependenciesBuild.newLine().appendTab(2).append("<dependency>")
					.newLine().appendTab(3).append("<groupId>").append(dep.getGroupId()).append("</groupId>")
					.newLine().appendTab(3).append("<artifactId>").append(dep.getArtifactId()).append("</artifactId>")
					.newLine().appendTab(2).append("</dependency>");
				}
				dependenciesBuild.newLine().appendTab().append("</dependencies>");
			}
			Map<String, Plugin> pluginMap = frameDependey.getPluginMap();
			if (pluginMap != null) {
				Plugin plugin = pluginMap.get("compiler");
				if (plugin != null) {
					pluginBuild.append("<plugins>")
					.newLine().appendTab(3).append("<plugin>")
					.newLine().appendTab(4).append("<groupId>").append(plugin.getGroupId()).append("</groupId>")
					.newLine().appendTab(4).append("<artifactId>").append(plugin.getArtifactId()).append("</artifactId>")
					.newLine().appendTab(3).append("</plugin>")
					.newLine().appendTab(2).append("</plugins>")
					;
				}
			}
		}
		content = content.replace("${ParentDependey}", parentBuild.toFormatString());
		content = content.replace("${Dependencies}", dependenciesBuild.toFormatString());
		content = content.replace("${Plugins}", pluginBuild.toFormatString());
		return content;
	}
}
