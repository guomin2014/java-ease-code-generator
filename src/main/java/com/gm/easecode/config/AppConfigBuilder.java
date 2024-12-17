package com.gm.easecode.config;

import java.util.ArrayList;
import java.util.List;

import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.vo.AppModule;
import com.gm.easecode.common.vo.ControllerClassStyleMode;
import com.gm.easecode.frame.FrameworkProviderFactory.FrameworkProviderMode;
import com.gm.easecode.source.DataSource;

public class AppConfigBuilder {

	/** 应用名称 */
	private String appName;
	/** 数据来源 */
	private DataSource dataSource;
	/** 代码根目录  */
	private String codeSavePath;
	/** 数据存储目录，比如：sql文件  */
	private String dataSavePath;
	/** 框架名称 */
	private FrameworkProviderMode framework;
	/** Controller类风格 */
	private ControllerClassStyleMode controllerClassStyle;
	/** 公司级包名称，默认com.gm，将与appName组合成完成父级包路径，比如：com.gm.appname */
	private String companyPackage;
	/** 根包路径，如果指定，则直接使用，否则使用companyPackage+appName，默认为companyPackage+appName */
	private String rootPackage = "";
	/** 是否创建dao代码 */
	private Boolean createDaoFile;
	/** 是否创建service代码 */
	private Boolean createServiceFile;
	/** 是否创建controller代码(包含dto代码) */
	private Boolean createControllerFile;
	/** 是否创建实体文件代码（model与xml） */
	private Boolean createEntityFile;
	/** 是否创建SQL文件 */
	private Boolean createSqlFile;
	/** 是否生成swagger注解 */
	private Boolean createSwagger;
	/** 是否创建系统模块(有系统模块，就可以直接启动) */
	private Boolean createSystemModule;
	/** 是否创建启动相关文件代码 */
	private Boolean createBootstrapFile;
	/** 是否清除历史生成代码 */
	private Boolean cleanOldCode;
	/** 需要过滤的表 */
	private String filterTables;
	/** 可用的表，不填表示全部 */
	private String enableTables;
	/** 模块列表（将表按模块划分） */
	private List<AppModule> modules = new ArrayList<>();
	
	public AppConfigBuilder setAppName(String appName) {
		this.appName = appName;
		return this;
	}

	public AppConfigBuilder setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

	public AppConfigBuilder setCodeSavePath(String codeSavePath) {
		this.codeSavePath = codeSavePath;
		return this;
	}

	public AppConfigBuilder setDataSavePath(String dataSavePath) {
		this.dataSavePath = dataSavePath;
		return this;
	}

	public AppConfigBuilder setFramework(FrameworkProviderMode framework) {
		this.framework = framework;
		return this;
	}

	public AppConfigBuilder setControllerClassStyle(ControllerClassStyleMode controllerClassStyle) {
		this.controllerClassStyle = controllerClassStyle;
		return this;
	}

	public AppConfigBuilder setCompanyPackage(String companyPackage) {
		this.companyPackage = companyPackage;
		return this;
	}

	public AppConfigBuilder setRootPackage(String rootPackage) {
		this.rootPackage = rootPackage;
		return this;
	}

	public AppConfigBuilder createDaoFile(boolean createDaoFile) {
		this.createDaoFile = createDaoFile;
		return this;
	}

	public AppConfigBuilder createServiceFile(boolean createServiceFile) {
		this.createServiceFile = createServiceFile;
		return this;
	}

	public AppConfigBuilder createControllerFile(boolean createControllerFile) {
		this.createControllerFile = createControllerFile;
		return this;
	}

	public AppConfigBuilder createEntityFile(boolean createEntityFile) {
		this.createEntityFile = createEntityFile;
		return this;
	}

	public AppConfigBuilder createSqlFile(boolean createSqlFile) {
		this.createSqlFile = createSqlFile;
		return this;
	}

	public AppConfigBuilder createSwagger(boolean createSwagger) {
		this.createSwagger = createSwagger;
		return this;
	}

	public AppConfigBuilder createSystemModule(boolean createSystemModule) {
		this.createSystemModule = createSystemModule;
		return this;
	}

	public AppConfigBuilder createBootstrapFile(Boolean createBootstrapFile) {
		this.createBootstrapFile = createBootstrapFile;
		return this;
	}

	public AppConfigBuilder cleanOldCode(boolean cleanOldCode) {
		this.cleanOldCode = cleanOldCode;
		return this;
	}

	public AppConfigBuilder setFilterTables(String filterTables) {
		this.filterTables = filterTables;
		return this;
	}

	public AppConfigBuilder setEnableTables(String enableTables) {
		this.enableTables = enableTables;
		return this;
	}
	
	public AppConfigBuilder addModule(AppModule module) {
		this.modules.add(module);
		return this;
	}

	public AppConfig build() {
		String frameworkName = null;
		String frameworkVersion = null;
		if (framework != null) {
			frameworkName = framework.getName();
			frameworkVersion = framework.getVersion();
		}
		AppConfig config = new AppConfig(appName, companyPackage, frameworkName, frameworkVersion, controllerClassStyle, dataSource, codeSavePath);
		if (StringUtils.isNotEmpty(rootPackage)) {
			config.setRootPackage(rootPackage);
		}
		if (StringUtils.isNotEmpty(dataSavePath)) {
			config.setDataSavePath(dataSavePath);
		}
		if (createEntityFile != null) {
			config.setCreateEntityFile(createEntityFile);
		}
		if (createDaoFile != null) {
			config.setCreateDaoFile(createDaoFile);
		}
		if (createServiceFile != null) {
			config.setCreateServiceFile(createServiceFile);
		}
		if (createControllerFile != null) {
			config.setCreateControllerFile(createControllerFile);
		}
		if (createSqlFile != null) {
			config.setCreateSqlFile(createSqlFile);
		}
		if (createSwagger != null) {
			config.setCreateSwagger(createSwagger);
		}
		if (createSystemModule != null) {
			config.setCreateSystemModule(createSystemModule);
		}
		if (createBootstrapFile != null) {
			config.setCreateBootstrapFile(createBootstrapFile);
		}
		if (cleanOldCode != null) {
			config.setCleanOldCode(cleanOldCode);
		}
		if (StringUtils.isNotEmpty(filterTables)) {
			config.setFilterTables(filterTables);
		}
		if (StringUtils.isNotEmpty(enableTables)) {
			config.setEnableTables(enableTables);
		}
		config.setModules(modules);
		return config;
	}
}
