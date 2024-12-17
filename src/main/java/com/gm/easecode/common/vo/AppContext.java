package com.gm.easecode.common.vo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gm.easecode.common.util.StringUtils;

public class AppContext {
	/** 需要被过滤的导入类 */
	private static final Set<String> filterImportClasses = new HashSet<>();
	/** 实体全量名映射，如：key=AppException, value=com.wisdom.agriculture.framework.exception.AppException */
	private static final Map<String, String> qualifiedClassNameMap = new HashMap<String, String>();
	
	static {
		//基础类不需要import
		filterImportClasses.add("void");
		filterImportClasses.add("Void");
		filterImportClasses.add("String");
		filterImportClasses.add("int");
		filterImportClasses.add("Integer");
		filterImportClasses.add("long");
		filterImportClasses.add("Long");
		filterImportClasses.add("double");
		filterImportClasses.add("Double");
		filterImportClasses.add("float");
		filterImportClasses.add("Float");
		//初始化常见常用类的完全限定类名
		qualifiedClassNameMap.put("List", "java.util.List");
		qualifiedClassNameMap.put("ArrayList", "java.util.ArrayList");
		qualifiedClassNameMap.put("Map", "java.util.Map");
		qualifiedClassNameMap.put("HashMap", "java.util.HashMap");
		qualifiedClassNameMap.put("Set", "java.util.Set");
		qualifiedClassNameMap.put("HashSet", "java.util.HashSet");
		qualifiedClassNameMap.put("Date", "java.util.Date");
		qualifiedClassNameMap.put("BigDecimal", "java.math.BigDecimal");
		qualifiedClassNameMap.put("HttpServletRequest", "javax.servlet.http.HttpServletRequest");
		qualifiedClassNameMap.put("HttpServletResponse", "javax.servlet.http.HttpServletResponse");
	}
	/**
	 * 添加类的完全限定类名
	 * @param className
	 * @param qualifiedClassName
	 */
	public static void addQualifiedClassName(String className, String qualifiedClassName) {
		qualifiedClassNameMap.put(className, qualifiedClassName);
	}
	
	/**
	 * 获取类的完全限定类名
	 * @param className
	 * @return
	 */
	public static String getQualifiedClassName(String className) {
		if (StringUtils.isEmpty(className)) {
			return null;
		}
		return qualifiedClassNameMap.get(className);
	}
	/**
	 * 是否需要过滤Import的类
	 * @param className
	 * @return
	 */
	public static boolean isFilterImport(String className) {
		if (StringUtils.isEmpty(className)) {
			return false;
		}
		if (filterImportClasses.contains(className)) {
			return true;
		}
		int index = className.lastIndexOf(".");
		if (index != -1) {
			String name = className.substring(index);
			return filterImportClasses.contains(name);
		}
		return false;
	}
}
