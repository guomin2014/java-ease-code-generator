package com.gm.easecode.frame;

import java.util.List;

import com.gm.easecode.common.vo.AppAnnotation;
import com.gm.easecode.common.vo.AppClass;
import com.gm.easecode.common.vo.AppClassConstructor;
import com.gm.easecode.common.vo.AppClassDefinition;

public interface FrameworkProvider {

	String getFrameworkName();
	
	String getFrameworkVersion();
	/**
	 * 获取框架的基础包名
	 * @return
	 */
	String getFrameworkPackage();
	/**
	 * 获取类的完全限定类名
	 * @param className
	 * @return
	 */
	String getQualifiedClassName(String className);
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
