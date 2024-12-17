package com.gm.easecode.common.vo;

import org.apache.commons.lang3.StringUtils;

import com.gm.easecode.common.AppException;
import com.gm.easecode.common.util.TableUtil;
import com.gm.easecode.config.AppConfig;

/**
 * 应用采用的配置文件
 *
 */
public class AppTableNameSpace {
	/** 模块名称 */
	private String moduleName;
	/** 实体对象文件名称*/
	private String entityName;
	/** 联合主健名称 */
	private String entityKey;
	/** 查询对象文件名称  */
	private String queryName;
	/** dao接口文件名 */
	private String daoName;
	/** dao实现文件名 */
	private String daoImplName;
	/** dao的Annotation名 */
	private String daoAnnotationName;
	/** service接口文件名 */
	private String serviceName;
	/** service实现文件名 */
	private String serviceImplName;
	/** service的Annotation名 */
	private String serviceAnnotationName;
	/** controller文件名 */
	private String controllerName;
	/** form文件名 */
	private String contFormName;
	/** controller映射路径 */
	private String controllerMappingPath;
	/** controller请求的对象名 */
	private String controllerDtoReqName;
	/** controller分页请求的对象名 */
	private String controllerDtoReqPageName;
	/** controller响应的对象名 */
	private String controllerDtoRspName;
	/** ibatis映射文件路径 */
	private String ibatisConfPath;
	/** 数据库访问文件名 */
	private String ibatisName;
	/** entity 包名 */
	private String entityPkgName;
	/** entity 路径 */
	private String entityPath;
	/** dao包名 */
	private String daoPkgName;
	/** dao路径 */
	private String daoPath;
	/** dao实现类包名 */
	private String daoImplPkgName;
	/** dao实现类路径 */
	private String daoImplPath;
	/** service接口包名 */
	private String servicePkgName;
	/** service接口路径 */
	private String servicePath;
	/** service实现类包名 */
	private String serviceImplPkgName;
	/** service实现类路径 */
	private String serviceImplPath;
	/** controller 包名 */
	private String controllerPkgName;
	/** controller路径 */
	private String controllerPath;
	/** controllerDto 包名 */
	private String controllerDtoPkgName;
	/** controllerDto路径 */
	private String controllerDtoPath;
	/** SQL文件路径 */
	private String sqlFilePath;
	/** SQL文件名称 */
	private String sqlFileName;
	
	/** 模块编号 */
	private String categoryNum;
	/** 是否区分子模块 */
	private boolean subModuleEnable;
	/** 区分子模块的层级 */
	private int subModuleDepth = 1;
	
	/**
	 * 
	 * @param config       常用配置
	 * @param tableName    表名
	 * @param module       表归属模块
	 */
	public AppTableNameSpace(AppNameSpace appNameSpace, String tableName, AppModule module) {
		AppConfig config = appNameSpace.getConfig();
		CodeStyleMode codeStyle = config.getCodeStyle();//编码风格，0：普通，1：Maven
        this.categoryNum = module.getNum();
        this.subModuleEnable = module.isSubModuleEnable();
        this.subModuleDepth = module.getSubModuleDepth();
        TableNameInfo tableNameInfo = TableUtil.parseTableName(tableName);
        if (tableNameInfo == null) {
        	throw new AppException("不能解析的表名称[" + tableName + "]");
        }
        moduleName = tableNameInfo.getModuleName();
        String modulePackageJoin = tableNameInfo.getModulePackageJoin();
		controllerMappingPath = convertPackage2ControllerMappingPath(modulePackageJoin);
		entityName = moduleName + config.getEntitySuffix();
		entityKey = entityName + "Key";
		queryName = moduleName + config.getQuerySuffix();
		ibatisName = com.gm.easecode.common.util.StringUtils.firstToLowerCase(moduleName) + ".xml";
		daoName = moduleName + config.getDaoSuffix();
		daoImplName = moduleName + config.getDaoImplSuffix();
		daoAnnotationName = com.gm.easecode.common.util.StringUtils.firstToLowerCase(daoName);
		serviceName = moduleName + config.getServiceSuffix();
		serviceImplName = moduleName + config.getServiceImplSuffix();
		serviceAnnotationName = com.gm.easecode.common.util.StringUtils.firstToLowerCase(serviceName);
		controllerName = moduleName + config.getControllerSuffix();
		contFormName = moduleName + config.getContFormSuffix();
		controllerDtoReqName = moduleName + config.getControllerReqDtoSuffix();
		controllerDtoReqPageName = moduleName + config.getControllerReqPageDtoSuffix();
		controllerDtoRspName = moduleName + config.getControllerRspDtoSuffix();
		String[] modulePackages = tableNameInfo.getModulePackages();
		String identify = module.getIdentify();
		String category = identify;
		String subPackage = null;//class类的包路径
		if (AppModule.DEFAULT_IDENTIFY.equalsIgnoreCase(identify)) {//未分组
			category = modulePackages != null && modulePackages.length > 0 ? modulePackages[0] : identify;
			subPackage = modulePackageJoin;
		} else {
			category = identify;
			subPackage = identify;
			if (subModuleEnable) {
				if (modulePackages != null) {
					int appendChildDept = 0;
					for (int i = 0; i < modulePackages.length; i++) {
						String pke = modulePackages[i];
						if (i == 0 && pke.equalsIgnoreCase(identify)) {
							continue;
						}
						subPackage = subPackage + "." + pke;
						appendChildDept++;
						if (appendChildDept >= subModuleDepth) {
							break;
						}
					}
					if (appendChildDept < this.subModuleDepth && module.hasSameLevel(tableName)) {
						subPackage += ".base";
					}
				}
			}
		}
		String pkgRoot = TableUtil.getPackage(config.getRootPackage(), subPackage);
		String baseJavaPath = appNameSpace.getBaseJavaPath(categoryNum, category);
		String baseResourcePath = appNameSpace.getBaseResourcePath(categoryNum, category);
		sqlFileName = config.getProjectName() + ".sql." + config.getProjectMark();
		sqlFilePath = StringUtils.appendIfMissing(config.getSqlFilePath() ,"/");
		entityPkgName = TableUtil.getPackage(pkgRoot, config.getEntityPathSpecs());
		daoPkgName = TableUtil.getPackage(pkgRoot, config.getDaoPathSpecs());
		servicePkgName = TableUtil.getPackage(pkgRoot, config.getServicePathSpecs());
		controllerPkgName = TableUtil.getPackage(pkgRoot, config.getControllerPathSpecs());
		controllerDtoPkgName = TableUtil.getPackage(controllerPkgName, config.getControllerDtoPathSpecs());
		entityPath = TableUtil.getPath(baseJavaPath, entityPkgName);
		daoPath = TableUtil.getPath(baseJavaPath, daoPkgName);
		servicePath = TableUtil.getPath(baseJavaPath, servicePkgName);
		controllerPath = TableUtil.getPath(baseJavaPath, controllerPkgName);
		controllerDtoPath = TableUtil.getPath(baseJavaPath, controllerDtoPkgName);
		String ibatisRelativePath = null;
		if (codeStyle == CodeStyleMode.MAVEN || config.getConfigStyle() == ConfigStyleMode.Annotation) {
			ibatisRelativePath = "sqlmap" + "/" + TableUtil.getEntityName(category) + "/";
		} else {
			ibatisRelativePath = "dao" + "/" + config.getDaoImplPathSpecs() + "/";
		}
		ibatisConfPath = baseResourcePath + ibatisRelativePath;
		daoImplPkgName = daoPkgName + "." + config.getDaoImplPathSpecs();
		serviceImplPkgName = servicePkgName + "." + config.getServiceImplPathSpecs();
		daoImplPath = daoPath + config.getDaoImplPathSpecs() + "/";
		serviceImplPath = servicePath + config.getServiceImplPathSpecs() + "/";
	}

	private String convertPackage2ControllerMappingPath(String pke) {
		if (StringUtils.isEmpty(pke)) {
			return "";
		}
		return pke.replace(".", "/");
	}
	public String getModuleName() {
		return moduleName;
	}
	public String getControllerMappingPath() {
		return controllerMappingPath;
	}
	public String getEntityName() {
		return entityName;
	}
	public String getEntityKey() {
		return entityKey;
	}
	public String getQueryName() {
		return queryName;
	}
	public String getDaoName() {
		return daoName;
	}
	public String getDaoImplName() {
		return daoImplName;
	}
	public String getDaoAnnotationName() {
		return daoAnnotationName;
	}
	public String getIbatisName() {
		return ibatisName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public String getServiceImplName() {
		return serviceImplName;
	}
	public String getServiceAnnotationName() {
		return serviceAnnotationName;
	}
	public String getControllerName() {
		return controllerName;
	}
	public String getControllerDtoReqName() {
		return controllerDtoReqName;
	}
	public String getControllerDtoReqPageName() {
		return controllerDtoReqPageName;
	}
	public String getControllerDtoRspName() {
		return controllerDtoRspName;
	}
	public String getIbatisConfPath() {
		return ibatisConfPath;
	}
	public String getContFormName() {
		return contFormName;
	}
	public String getEntityPkgName() {
		return entityPkgName;
	}
	public String getEntityPath() {
		return entityPath;
	}
	public String getDaoPkgName() {
		return daoPkgName;
	}
	public String getDaoPath() {
		return daoPath;
	}
	public String getDaoImplPkgName() {
		return daoImplPkgName;
	}
	public String getDaoImplPath() {
		return daoImplPath;
	}
	public String getServicePkgName() {
		return servicePkgName;
	}
	public String getServicePath() {
		return servicePath;
	}
	public String getServiceImplPkgName() {
		return serviceImplPkgName;
	}
	public String getServiceImplPath() {
		return serviceImplPath;
	}
	public String getControllerPkgName() {
		return controllerPkgName;
	}
	public String getControllerPath() {
		return controllerPath;
	}
	public String getSqlFilePath() {
		return sqlFilePath;
	}
	public String getSqlFileName() {
		return sqlFileName;
	}
	public String getControllerDtoPkgName() {
		return controllerDtoPkgName;
	}
	public void setControllerDtoPkgName(String controllerDtoPkgName) {
		this.controllerDtoPkgName = controllerDtoPkgName;
	}
	public String getControllerDtoPath() {
		return controllerDtoPath;
	}
	public void setControllerDtoPath(String controllerDtoPath) {
		this.controllerDtoPath = controllerDtoPath;
	}
}
