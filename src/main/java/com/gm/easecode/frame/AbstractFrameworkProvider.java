package com.gm.easecode.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.vo.AppClass;
import com.gm.easecode.common.vo.AppClassField;
import com.gm.easecode.common.vo.AppClassFieldList;
import com.gm.easecode.common.vo.ControllerClassStyleMode;
import com.gm.easecode.common.vo.FileAliasMode;

public abstract class AbstractFrameworkProvider implements FrameworkProvider {

	/** 实体全量名映射，如：key=AppException, value=com.wisdom.agriculture.framework.exception.AppException */
	protected Map<String, String> qualifiedClassNameMap = new HashMap<String, String>();
	/** 需要被过滤的导入类 */
	protected Set<String> filterImportClasses = new HashSet<>();
	/** 基类的Class，key：归属类型，见FileAliasEnum，key-key：基类标识，key-value：基类 */
	protected Map<String, Map<String, AppClass>> baseClassMap = new HashMap<>();
	
	/** 框架名称 */
	private String frameworkName;
	/** 框架版本 */
	private String frameworkVersion;
	
	public AbstractFrameworkProvider(String frameworkName, String frameworkVersion) {
		this.frameworkName = frameworkName;
		this.frameworkVersion = frameworkVersion;
		this.initFilterImportClass();
		this.initCommonQualifiedClassName();
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

	private void initFilterImportClass() {
	}
	private void initCommonQualifiedClassName() {
		qualifiedClassNameMap.put("Repository", "org.springframework.stereotype.Repository");
		qualifiedClassNameMap.put("Service", "org.springframework.stereotype.Service");
		qualifiedClassNameMap.put("RestController", "org.springframework.web.bind.annotation.RestController");
		qualifiedClassNameMap.put("RequestMapping", "org.springframework.web.bind.annotation.RequestMapping");
	}
	
	public void addQualifiedClassName(String className, String qualifiedClassName) {
		qualifiedClassNameMap.put(className, qualifiedClassName);
	}
	
	@Override
	public String getQualifiedClassName(String className) {
		if (StringUtils.isEmpty(className)) {
			return null;
		}
		return qualifiedClassNameMap.get(className);
	}

	/**
	 * 初始化类的配置
	 * @param appClass
	 */
	private void initClassConfig(AppClass appClass) {
		//添加到全量路径集合中
		this.qualifiedClassNameMap.put(appClass.getClassName(), appClass.getPackageName() + "." + appClass.getClassName());
		//添加到基类集合中
		if (appClass.getAliasName() != null && appClass.getKey() != null) {
			Map<String, AppClass> map = this.baseClassMap.get(appClass.getAliasName());
			if (map == null) {
				map = new HashMap<>();
				this.baseClassMap.put(appClass.getAliasName(), map);
			}
			map.put(appClass.getKey(), appClass);
		}
	}
	/**
	 * 初始化Entity基类
	 * @param className
	 * @param packageName
	 * @param pkType
	 * @return
	 */
	public AppClass initBaseEntityClass(String className, String packageName, String pkType) {
		return this.initBaseEntityClass(className, packageName, pkType, pkType);
	}
	/**
	 * 初始化Entity基类
	 * @param className		类名
	 * @param packageName	类的包路径
	 * @param pkType		类的主键类型
	 * @param classKey		类的标识
	 * @return
	 */
	public AppClass initBaseEntityClass(String className, String packageName, String pkType, String classKey) {
		AppClass entityClass = new AppClass();
		entityClass.setClassName(className);
		entityClass.setPackageName(packageName);
		entityClass.setAliasName(FileAliasMode.Entity.name());
		entityClass.setKey(classKey);
		List<AppClassField> entityFields = new ArrayList<>();
		entityFields.add(new AppClassFieldList("id", pkType));
		entityFields.add(new AppClassFieldList("createTime", "Date"));
		entityFields.add(new AppClassFieldList("createUser", "String"));
		entityFields.add(new AppClassFieldList("createUserId", "Long"));
		entityFields.add(new AppClassFieldList("createUserName", "String"));
		entityFields.add(new AppClassFieldList("createUserDeptId", "Long"));
		entityFields.add(new AppClassFieldList("createUserDeptName", "String"));
		entityFields.add(new AppClassFieldList("updateTime", "Date"));
		entityFields.add(new AppClassFieldList("updateUser", "String"));
		entityFields.add(new AppClassFieldList("updateUserId", "Long"));
		entityFields.add(new AppClassFieldList("updateUserName", "String"));
		entityFields.add(new AppClassFieldList("updateUserDeptId", "Long"));
		entityFields.add(new AppClassFieldList("updateUserDeptName", "String"));
		entityClass.setFields(entityFields);
		this.initClassConfig(entityClass);
		return entityClass;
	}
	public AppClass initBaseTreeEntityClass(String className, String packageName, String pkType, String classKey) {
		AppClass entityClass = this.initBaseEntityClass(className, packageName, pkType, classKey);
		List<AppClassField> entityFields = new ArrayList<>();
		entityFields.add(new AppClassFieldList("parentId", pkType));
		entityFields.add(new AppClassFieldList("name", "String"));
		entityFields.add(new AppClassFieldList("childSize", "Integer"));
		entityFields.add(new AppClassFieldList("level", "Integer"));
		entityFields.add(new AppClassFieldList("remark", "String"));
		entityFields.add(new AppClassFieldList("type", "Integer"));
		entityFields.add(new AppClassFieldList("orderId", "Integer"));
		entityFields.add(new AppClassFieldList("childList", "List"));
		entityClass.addFields(entityFields);
		return entityClass;
	}
	public AppClass initBaseControllerClass(String className, String packageName, String classKey, ControllerClassStyleMode controllerStyle) {
		AppClass baseClass = new AppClass();
		baseClass.setClassName(className);
		baseClass.setPackageName(packageName);
		baseClass.setAliasName(FileAliasMode.Controller.name());
		baseClass.setKey(classKey);
		List<AppClass> genericClasses = new ArrayList<>();
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.Service.name()));
		if(controllerStyle == ControllerClassStyleMode.DTO || controllerStyle == ControllerClassStyleMode.DTO_MAPPING) {
			genericClasses.add(AppClass.createVariableInstance(FileAliasMode.RequestDto.name()));
			genericClasses.add(AppClass.createVariableInstance(FileAliasMode.RequestPageDto.name()));
			genericClasses.add(AppClass.createVariableInstance(FileAliasMode.ResponseDto.name()));
		} else {
			genericClasses.add(AppClass.createVariableInstance(FileAliasMode.Form.name()));
		}
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.Entity.name()));
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.EntityKey.name()));
		baseClass.setGenericClasses(genericClasses);
		this.initClassConfig(baseClass);
		return baseClass;
	}
	public AppClass initBaseFormClass(String className, String packageName, String pkType) {
		return this.initBaseFormClass(className, packageName, pkType, pkType);
	}
	public AppClass initBaseFormClass(String className, String packageName, String pkType, String classKey) {
		AppClass entityClass = new AppClass();
		entityClass.setClassName(className);
		entityClass.setPackageName(packageName);
		entityClass.setAliasName(FileAliasMode.Form.name());
		entityClass.setKey(classKey);
		List<AppClass> genericClasses = new ArrayList<>();
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.Entity.name()));
		entityClass.setGenericClasses(genericClasses);
		List<AppClassField> entityFields = new ArrayList<>();
		entityFields.add(new AppClassFieldList("id", pkType + "[]"));
		entityClass.setFields(entityFields);
		this.initClassConfig(entityClass);
		return entityClass;
	}
	public AppClass initBaseDtoClass(String aliasName, String className, String packageName, String pkType) {
		AppClass entityClass = new AppClass();
		entityClass.setClassName(className);
		entityClass.setPackageName(packageName);
		entityClass.setAliasName(aliasName);
		entityClass.setKey(pkType);
		this.initClassConfig(entityClass);
		return entityClass;
	}
	public AppClass initBaseDaoClass(String className, String packageName, String pkType) {
		AppClass baseClass = new AppClass(className, packageName, null);
		baseClass.setAliasName(FileAliasMode.Dao.name());
		baseClass.setKey(pkType);
		List<AppClass> genericClasses = new ArrayList<>();
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.Entity.name()));
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.EntityKey.name()));
		baseClass.setGenericClasses(genericClasses);
		this.initClassConfig(baseClass);
		return baseClass;
	}
	public AppClass initBaseDaoImplClass(String className, String packageName, String pkType) {
		AppClass baseClass = new AppClass(className, packageName, null);
		baseClass.setAliasName(FileAliasMode.DaoImpl.name());
		baseClass.setKey(pkType);
		List<AppClass> genericClasses = new ArrayList<>();
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.Entity.name()));
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.EntityKey.name()));
		baseClass.setGenericClasses(genericClasses);
		this.initClassConfig(baseClass);
		return baseClass;
	}
	public AppClass initBaseServiceClass(String className, String packageName, String pkType) {
		AppClass baseClass = new AppClass(className, packageName, null);
		baseClass.setAliasName(FileAliasMode.Service.name());
		baseClass.setKey(pkType);
		List<AppClass> genericClasses = new ArrayList<>();
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.Entity.name()));
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.EntityKey.name()));
		baseClass.setGenericClasses(genericClasses);
		this.initClassConfig(baseClass);
		return baseClass;
	}
	public AppClass initBaseServiceImplClass(String className, String packageName, String pkType) {
		AppClass baseClass = new AppClass(className, packageName, null);
		baseClass.setAliasName(FileAliasMode.ServiceImpl.name());
		baseClass.setKey(pkType);
		List<AppClass> genericClasses = new ArrayList<>();
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.Dao.name()));
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.Entity.name()));
		genericClasses.add(AppClass.createVariableInstance(FileAliasMode.EntityKey.name()));
		baseClass.setGenericClasses(genericClasses);
		this.initClassConfig(baseClass);
		return baseClass;
	}
	
	public AppClass getExtClass(Map<String, AppClass> classMap, String classType) {
		if (classMap == null || classMap.isEmpty()) {
			return null;
		}
		AppClass parentEntity = null;
		if (parentEntity == null) {
			parentEntity = classMap.get(classType);
		}
		if (parentEntity == null) {
			parentEntity = classMap.get("Default");
		}
		return parentEntity;
	}
}
