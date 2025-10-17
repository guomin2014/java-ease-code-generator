package com.gm.easecode.frame;

import java.util.List;

import com.gm.easecode.common.vo.AppAnnotation;
import com.gm.easecode.common.vo.AppClass;
import com.gm.easecode.common.vo.AppClassConstructor;
import com.gm.easecode.common.vo.AppClassDefinition;
import com.gm.easecode.common.vo.AppModuleGroup;
import com.gm.easecode.common.vo.AppNameSpace;
import com.gm.easecode.common.vo.AppModuleNameSpace;
import com.gm.easecode.config.AppConfig;
import com.gm.easecode.frame.common.FrameDependey;

public interface FrameworkProvider {

	String getFrameworkName();
	
	String getFrameworkVersion();
	/**
	 * 获取框架的基础包名
	 * @return
	 */
	String getFrameworkPackage();
	/**
	 * 获取应用命名空间规则
	 * @return
	 */
	AppNameSpace getAppNameSpace(AppConfig config);
	/**
	 * 获取应用模块的命名空间规则
	 * @param appNameSpace	应用命名规则
	 * @param moduleName	业务模块名
	 * @param appModuleGroup		业务模块归属模块(分组)
	 * @return
	 */
	AppModuleNameSpace getAppModuleNameSpace(AppNameSpace appNameSpace, String moduleName, AppModuleGroup appModuleGroup);
	/**
	 * 获取框架依赖信息
	 * @return
	 */
	FrameDependey getFrameDependey();
	/**
	 * 获取指定别名对应的继承对象
	 * @param classDefinition
	 * @return
	 */
	AppClass getClassExtendsClass(AppClassDefinition classDefinition);
	/**
	 * 获取指定别名对应的实现接口
	 * @param aliasName
	 * @return
	 */
	List<AppClass> getClassImplementsClass(AppClassDefinition classDefinition);
	/**
	 * 获取指定别名对应的Annotation
	 * @param aliasName
	 * @return
	 */
	List<AppAnnotation> getClassAnnotation(AppClassDefinition classDefinition);
	/**
	 * 获取指定别名对应的Constructor
	 * @param aliasName
	 * @return
	 */
	List<AppClassConstructor> getClassConstructor(AppClassDefinition classDefinition);
	/**
	 * 获取类属性的Annotation
	 * @param aliasName
	 * @return
	 */
	AppAnnotation getClassFieldAnnotation(AppClassDefinition classDefinition);
}
