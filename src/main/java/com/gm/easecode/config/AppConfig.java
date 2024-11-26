package com.gm.easecode.config;

import java.util.ArrayList;
import java.util.List;

import com.gm.easecode.common.util.DateUtils;
import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.vo.AppModule;
import com.gm.easecode.common.vo.CodeStyleMode;
import com.gm.easecode.common.vo.ConfigStyleMode;
import com.gm.easecode.common.vo.ControllerClassStyleMode;
import com.gm.easecode.source.DataSource;

public class AppConfig {
	/** 应用名称 */
	private String appName;
	/** 字符集 */
	private String charset = "UTF-8";
	/** 版本 */
	private String version = "1.0.0";
	/** 作者 */
	private String author = "";
	/** 版权声明 */
	private String copyright = "";
	/** 企业名称 */
	private String company = "";
	/** 数据来源 */
	private DataSource dataSource;
	/** 代码风格，0：普通，1：Maven */
	private CodeStyleMode codeStyle = CodeStyleMode.MAVEN;
	/** 代码是否按模块分组 */
	private boolean codeGroupByModule = false;
	/** 配置风格，0：XML，1：Annotation，默认0 */
	private ConfigStyleMode configStyle = ConfigStyleMode.Annotation;
	/** Controller类风格，0：MVC，1：MVC-MAPPING，2：JSON，3：JSON-MAPPING，默认0 */
	private ControllerClassStyleMode controllerClassStyle = ControllerClassStyleMode.JSON_MAPPING;
	/** 页面风格，0：JSP，1：HTML，默认0 */
	private int webPageStyle = 0;
	/** 代码根目录  */
	private String codeSavePath = "";
	/** 数据存储目录，比如：sql文件  */
	private String dataSavePath = "";
	/** 框架名称 */
	private String frameworkName = "";
	/** 框架版本 */
	private String frameworkVersion = "1.0.0";
	
	private final String templetePath = "code/template/";
	private final String builtInModulePath = templetePath + "module/";
	
	private final String ibatisXmlTpl = "ibatisXmlTemplete.tpl";
	private final String classTpl = "classTemplete.tpl";
	
	private String entitySuffix = "Entity";
	private String querySuffix = "Query";
	private String daoSuffix = "Dao";
	private String daoImplSuffix = "DaoImpl";
	private String serviceSuffix = "Service";
	private String serviceImplSuffix = "ServiceImpl";
	private String contFormSuffix = "Form";
	private String controllerSuffix = "Controller";
	private String controllerReqDtoSuffix = "RequestDto";
	private String controllerReqPageDtoSuffix = "RequestPageDto";
	private String controllerRspDtoSuffix = "ResponseDto";
	
	private String entityPathSpecs = "model";
	private String daoPathSpecs = "dao";
	private String daoImplPathSpecs = "mybatis";
	private String servicePathSpecs = "service";
	private String serviceImplPathSpecs = "impl";
	private String contFormPathSpecs = "web";
	private String controllerPathSpecs = "web";
	private String controllerDtoPathSpecs = "dto";
	/** 公司级包名称，默认com.gm，将与appName组合成完成父级包路径，比如：com.gm.appname */
	private String companyPackage = "com.gm";
	private String subSysPackage = "";
	private String sqlFilePath = "01.db";
	private String projConfPath = "02.config";
	private String projSrcPath = "03.src";
	private String webRootPath = "webapp";
	/** 根包路径，如果指定，则直接使用，否则使用companyPackage+appName，默认为companyPackage+appName */
	private String rootPackage = "";
	
	private String fileDate = "";
	
	/** 是否创建dao代码 */
	private boolean createDaoFile = true;
	/** 是否创建service代码 */
	private boolean createServiceFile = true;
	/** 是否创建controller代码(包含dto代码) */
	private boolean createControllerFile = true;
	/** 是否创建实体文件代码（model与xml） */
	private boolean createEntityFile = true;
	/** 是否创建SQL文件 */
	private boolean createSqlFile = true;
	/** 是否生成swagger注解 */
	private boolean createSwagger = true;
	/** 是否创建系统模块(有系统模块，就可以直接启动) */
	private boolean createSystemModule = false;
	/** 是否清除历史生成代码 */
	private boolean cleanOldCode = true;
	/** 项目名称 */
	private String projectName = "";
	private String projectMark = "";
	/** 需要过滤的表 */
	private String filterTables = "";
	/** 可用的表，不填表示全部 */
	private String enableTables = "";
	/** 内置模块名，多个模块以逗号分隔，如：dept,user */
	private String innerModules;
	/** 表名前缀 */
	private String tableNamePrefix;
	/** 模块列表（将表按模块划分） */
	private List<AppModule> modules = new ArrayList<>();
	
	public AppConfig(String appName, String companyPackage, String frameworkName, String frameworkVersion, DataSource dataSource, String codeSavePath) {
		this(appName, companyPackage, frameworkName, frameworkVersion, ControllerClassStyleMode.JSON_MAPPING, dataSource, codeSavePath);
	}
	public AppConfig(String appName, String companyPackage, String frameworkName, String frameworkVersion, ControllerClassStyleMode controllerClassStyle, DataSource dataSource, String codeSavePath) {
		this.appName = appName;
		this.companyPackage = companyPackage;
		this.frameworkName = frameworkName;
		this.frameworkVersion = frameworkVersion;
		this.controllerClassStyle = controllerClassStyle;
		this.dataSource = dataSource;
		this.codeSavePath = codeSavePath;
	}
	
	public void init() {
		String name = "unknow";
		if(StringUtils.isNotEmpty(companyPackage)) {
			if (StringUtils.isNotEmpty(appName)) {
				if (!companyPackage.endsWith("." + this.appName)) {
					this.companyPackage = this.companyPackage + "." + this.appName;
				}
			}
			String[] packs = companyPackage.split("\\.");//[0]:com或org,[1]:公司标识，[2]:应用标识
			name = packs[packs.length-1];
			if (StringUtils.isEmpty(this.tableNamePrefix) && packs.length >= 3) {
				this.tableNamePrefix = packs[1] + "_" + packs[2] + "_";
			}
		} else {
			if (StringUtils.isNotEmpty(appName)) {
				name = appName;
			}
		}
		this.projectName = name;
		this.projectMark = System.currentTimeMillis() + "";
		if (StringUtils.isEmpty(rootPackage)) {
			rootPackage = this.companyPackage;
		}
		if (StringUtils.isEmpty(this.dataSavePath)) {
			this.dataSavePath = this.codeSavePath;
		}
		if (StringUtils.isEmpty(this.fileDate)) {
			this.fileDate = DateUtils.getCurrDateTime(DateUtils.P_yyyy_MM_dd);
		}
	}
	public String getTempletePath() {
		return templetePath;
	}
	public String getBuiltInModulePath() {
		return builtInModulePath;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getIbatisXmlTpl() {
		return this.getTempletePath() + ibatisXmlTpl;
	}
	public String getClassTpl() {
		return this.getTempletePath() + classTpl;
	}
	public String getEntitySuffix() {
		return entitySuffix;
	}
	public void setEntitySuffix(String entitySuffix) {
		this.entitySuffix = entitySuffix;
	}
	public String getQuerySuffix() {
		return querySuffix;
	}
	public void setQuerySuffix(String querySuffix) {
		this.querySuffix = querySuffix;
	}
	public String getDaoSuffix() {
		return daoSuffix;
	}
	public void setDaoSuffix(String daoSuffix) {
		this.daoSuffix = daoSuffix;
	}
	public String getDaoImplSuffix() {
		return daoImplSuffix;
	}
	public void setDaoImplSuffix(String daoImplSuffix) {
		this.daoImplSuffix = daoImplSuffix;
	}
	public String getServiceSuffix() {
		return serviceSuffix;
	}
	public void setServiceSuffix(String serviceSuffix) {
		this.serviceSuffix = serviceSuffix;
	}
	public String getServiceImplSuffix() {
		return serviceImplSuffix;
	}
	public void setServiceImplSuffix(String serviceImplSuffix) {
		this.serviceImplSuffix = serviceImplSuffix;
	}
	public String getContFormSuffix() {
		return contFormSuffix;
	}
	public void setContFormSuffix(String contFormSuffix) {
		this.contFormSuffix = contFormSuffix;
	}
	public String getControllerSuffix() {
		return controllerSuffix;
	}
	public void setControllerSuffix(String controllerSuffix) {
		this.controllerSuffix = controllerSuffix;
	}
	public String getControllerReqDtoSuffix() {
		return controllerReqDtoSuffix;
	}
	public void setControllerReqDtoSuffix(String controllerReqDtoSuffix) {
		this.controllerReqDtoSuffix = controllerReqDtoSuffix;
	}
	public String getControllerReqPageDtoSuffix() {
		return controllerReqPageDtoSuffix;
	}
	public void setControllerReqPageDtoSuffix(String controllerReqPageDtoSuffix) {
		this.controllerReqPageDtoSuffix = controllerReqPageDtoSuffix;
	}
	public String getControllerRspDtoSuffix() {
		return controllerRspDtoSuffix;
	}
	public void setControllerRspDtoSuffix(String controllerRspDtoSuffix) {
		this.controllerRspDtoSuffix = controllerRspDtoSuffix;
	}
	public String getEntityPathSpecs() {
		return entityPathSpecs;
	}
	public void setEntityPathSpecs(String entityPathSpecs) {
		this.entityPathSpecs = entityPathSpecs;
	}
	public String getDaoPathSpecs() {
		return daoPathSpecs;
	}
	public void setDaoPathSpecs(String daoPathSpecs) {
		this.daoPathSpecs = daoPathSpecs;
	}
	public String getDaoImplPathSpecs() {
		return daoImplPathSpecs;
	}
	public void setDaoImplPathSpecs(String daoImplPathSpecs) {
		this.daoImplPathSpecs = daoImplPathSpecs;
	}
	public String getServicePathSpecs() {
		return servicePathSpecs;
	}
	public void setServicePathSpecs(String servicePathSpecs) {
		this.servicePathSpecs = servicePathSpecs;
	}
	public String getServiceImplPathSpecs() {
		return serviceImplPathSpecs;
	}
	public void setServiceImplPathSpecs(String serviceImplPathSpecs) {
		this.serviceImplPathSpecs = serviceImplPathSpecs;
	}
	public String getContFormPathSpecs() {
		return contFormPathSpecs;
	}
	public void setContFormPathSpecs(String contFormPathSpecs) {
		this.contFormPathSpecs = contFormPathSpecs;
	}
	public String getControllerPathSpecs() {
		return controllerPathSpecs;
	}
	public void setControllerPathSpecs(String controllerPathSpecs) {
		this.controllerPathSpecs = controllerPathSpecs;
	}
	public String getControllerDtoPathSpecs() {
		return controllerDtoPathSpecs;
	}
	public void setControllerDtoPathSpecs(String controllerDtoPathSpecs) {
		this.controllerDtoPathSpecs = controllerDtoPathSpecs;
	}
	public String getProjPackage() {
		return companyPackage;
	}
	public void setProjPackage(String projPackage) {
		this.companyPackage = projPackage;
	}
	
	public String getTableNamePrefix() {
		return tableNamePrefix;
	}
	public void setTableNamePrefix(String tableNamePrefix) {
		this.tableNamePrefix = tableNamePrefix;
	}
	public String getProjectName()
	{
		return projectName;
	}
	public String getSubSysPackage() {
		return subSysPackage;
	}
	public void setSubSysPackage(String subSysPackage) {
		this.subSysPackage = subSysPackage;
	}
	public String getProjSrcPath() {
		return projSrcPath;
	}
	public void setProjSrcPath(String projSrcPath) {
		this.projSrcPath = projSrcPath;
	}
	public String getProjConfPath() {
		return projConfPath;
	}
	public void setProjConfPath(String projConfPath) {
		this.projConfPath = projConfPath;
	}
	public String getFileDate() {
		return fileDate;
	}
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	public String getWebRootPath() {
		return webRootPath;
	}
	public String getSqlFilePath() {
		return sqlFilePath;
	}
	public boolean isCreateDaoFile()
	{
		return createDaoFile;
	}
	public boolean isCreateServiceFile()
	{
		return createServiceFile;
	}
	public boolean isCreateControllerFile()
	{
		return createControllerFile;
	}
	public boolean isCreateEntityFile()
	{
		return createEntityFile;
	}
	public boolean isCreateSqlFile() {
		return createSqlFile;
	}
	public String getProjectMark() {
		return projectMark;
	}
	public String getFrameworkName() {
		return frameworkName;
	}
	public void setFrameworkName(String frameworkName) {
		this.frameworkName = frameworkName;
	}
	public String getFrameworkVersion() {
		return frameworkVersion;
	}
	public void setFrameworkVersion(String frameworkVersion) {
		this.frameworkVersion = frameworkVersion;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public ConfigStyleMode getConfigStyle() {
		return configStyle;
	}
	public void setConfigStyle(ConfigStyleMode configStyle) {
		this.configStyle = configStyle;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public CodeStyleMode getCodeStyle() {
		return codeStyle;
	}
	public void setCodeStyle(CodeStyleMode codeStyle) {
		this.codeStyle = codeStyle;
	}
	public boolean isCodeGroupByModule() {
		return codeGroupByModule;
	}
	public void setCodeGroupByModule(boolean codeGroupByModule) {
		this.codeGroupByModule = codeGroupByModule;
	}
	public ControllerClassStyleMode getControllerClassStyle() {
		return controllerClassStyle;
	}
	public void setControllerClassStyle(ControllerClassStyleMode controllerClassStyle) {
		this.controllerClassStyle = controllerClassStyle;
	}
	public int getWebPageStyle() {
		return webPageStyle;
	}
	public void setWebPageStyle(int webPageStyle) {
		this.webPageStyle = webPageStyle;
	}
	public String getCodeSavePath() {
		return codeSavePath;
	}
	public void setCodeSavePath(String codeSavePath) {
		this.codeSavePath = codeSavePath;
	}
	public String getDataSavePath() {
		return dataSavePath;
	}
	public void setDataSavePath(String dataSavePath) {
		this.dataSavePath = dataSavePath;
	}
	public String getCompanyPackage() {
		return companyPackage;
	}
	public void setCompanyPackage(String companyPackage) {
		this.companyPackage = companyPackage;
	}
	public String getRootPackage() {
		return rootPackage;
	}
	public void setRootPackage(String rootPackage) {
		this.rootPackage = rootPackage;
	}
	public boolean isCreateSwagger() {
		return createSwagger;
	}
	public void setCreateSwagger(boolean createSwagger) {
		this.createSwagger = createSwagger;
	}
	public boolean isCreateSystemModule() {
		return createSystemModule;
	}
	public void setCreateSystemModule(boolean createSystemModule) {
		this.createSystemModule = createSystemModule;
	}
	public boolean isCleanOldCode() {
		return cleanOldCode;
	}
	public void setCleanOldCode(boolean cleanOldCode) {
		this.cleanOldCode = cleanOldCode;
	}
	public String getFilterTables() {
		return filterTables;
	}
	public void setFilterTables(String filterTables) {
		this.filterTables = filterTables;
	}
	public String getEnableTables() {
		return enableTables;
	}
	public void setEnableTables(String enableTables) {
		this.enableTables = enableTables;
	}
	public String getInnerModules() {
		return innerModules;
	}
	public void setInnerModules(String innerModules) {
		this.innerModules = innerModules;
	}
	public List<AppModule> getModules() {
		return modules;
	}
	public void setModules(List<AppModule> modules) {
		this.modules = modules;
	}
	public void setSqlFilePath(String sqlFilePath) {
		this.sqlFilePath = sqlFilePath;
	}
	public void setWebRootPath(String webRootPath) {
		this.webRootPath = webRootPath;
	}
	public void setCreateDaoFile(boolean createDaoFile) {
		this.createDaoFile = createDaoFile;
	}
	public void setCreateServiceFile(boolean createServiceFile) {
		this.createServiceFile = createServiceFile;
	}
	public void setCreateControllerFile(boolean createControllerFile) {
		this.createControllerFile = createControllerFile;
	}
	public void setCreateEntityFile(boolean createEntityFile) {
		this.createEntityFile = createEntityFile;
	}
	public void setCreateSqlFile(boolean createSqlFile) {
		this.createSqlFile = createSqlFile;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public void setProjectMark(String projectMark) {
		this.projectMark = projectMark;
	} 
	
}
