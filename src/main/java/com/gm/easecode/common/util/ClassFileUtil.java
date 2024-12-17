package com.gm.easecode.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gm.easecode.common.vo.AliasConstants;
import com.gm.easecode.common.vo.AppAnnotation;
import com.gm.easecode.common.vo.AppClass;
import com.gm.easecode.common.vo.AppClassConstructor;
import com.gm.easecode.common.vo.AppClassField;
import com.gm.easecode.common.vo.AppClassFieldBody;
import com.gm.easecode.common.vo.AppClassFieldList;
import com.gm.easecode.common.vo.AppClassFieldSerial;
import com.gm.easecode.common.vo.AppClassMethod;
import com.gm.easecode.common.vo.AppClassMethodBody;
import com.gm.easecode.common.vo.AppClassMethodGetAndSet;
import com.gm.easecode.common.vo.AppClassMethodList;
import com.gm.easecode.common.vo.AppClassMethodParam;
import com.gm.easecode.common.vo.AppContext;
import com.gm.easecode.common.vo.AppTableContext;
import com.gm.easecode.common.vo.ClassType;
import com.gm.easecode.config.AppConfig;

public final class ClassFileUtil { 
	
	/**
	 * 创建类文件
	 * @param context
	 * @param appClass
	 * @return
	 * @throws Exception
	 */
	public final static String crtClassContent(AppTableContext context, AppClass appClass) throws Exception {
		return crtClassContent(context.getConfig(), appClass);
	}
	public final static String crtClassContent(AppConfig config, AppClass appClass) throws Exception {
		//获取类的模板内容
		String content = FileUtil.read(config.getClassTpl(), config.getCharset());
		//公共内容替换
		content = replace(content, "version", config.getVersion());
		content = replace(content, "date", config.getFileDate());
		content = replace(content, "copyright", config.getCopyright());
		content = replace(content, "company", config.getCompany());
		content = replace(content, "author", config.getAuthor());
		
		content = replace(content, "fileName", appClass.getFileName());
		content = replace(content, "title", appClass.getDesc());
		content = replace(content, "description", appClass.getDescription());
		content = replace(content, "package", "package " + appClass.getPackageName() + ";");
		
		content = replace(content, "imports", generateClassImport(appClass));
		content = replace(content, "annotations", generateClassAnnotation(appClass));
		content = replace(content, "accessModifier", appClass.getModifier());
		content = replace(content, "classType", appClass.getType());
		content = replace(content, "className", appClass.getClassName());
		content = replace(content, "extendsClass", generateClassRelation("extends", appClass.getExtendsClasses()));
		content = replace(content, "implementsClass", generateClassRelation("implements", appClass.getImplementsClasses()));
		content = replace(content, "fields", generateClassField(appClass));
		content = replace(content, "constructors", generateClassConstructor(appClass));
		content = replace(content, "methods", generateClassMethod(appClass));
		return content;
	}
	/**
	 * 替换内容中的关键字
	 * @param content
	 * @param replaceKey
	 * @param replaceContent
	 * @return
	 */
	private static String replace(String content, String replaceKey, String replaceContent) {
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(replaceKey)) {
			return content;
		}
		return content.replace(AliasConstants.generalAliasVariable(replaceKey), StringUtils.isEmpty(replaceContent) ? "" : replaceContent);
	}
	/**
	 * 生成类的导入类信息
	 * @param context
	 * @param importClasses
	 * @return
	 */
	private static String generateClassImport(AppClass appClass) {
		CustomStringBuilder builder = new CustomStringBuilder();
		Set<String> importClasses = new HashSet<>();
		//继承基类等需要导入的类
		if (appClass.getImportClasses() != null) {
			importClasses.addAll(appClass.getImportClasses());
		}
		if (importClasses != null && !importClasses.isEmpty()) {
			Set<String> realImportClasses = new HashSet<>();
			for (String className : importClasses) {
				if (AppContext.isFilterImport(className)) {
					continue;
				}
				String qualifiedClassName = null;
				if (StringUtils.isNotEmpty(TableUtil.getPackage(className))) {//类名中包含包路径，则直接导入
					qualifiedClassName = className;
				} else {
					qualifiedClassName = AppContext.getQualifiedClassName(className);
					if (StringUtils.isEmpty(qualifiedClassName)) {
						continue;
					}
				}
				realImportClasses.add(qualifiedClassName);
			}
			List<String> importClasseList = new ArrayList<>(realImportClasses);
			String packageName = appClass.getPackageName();
			String rootPackage = TableUtil.getSubPackage(packageName, 3);
			//对导入为进行排序，sun开头>java开头>org开头>基类框架开头>其它
			Collections.sort(importClasseList, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					if (o1.startsWith("sun.") && o2.startsWith("sun.")) {
						return o1.compareTo(o2);
					}
					if (o1.startsWith("sun.")) {
						return -1;
					}
					if (o2.startsWith("sun.")) {
						return 1;
					}
					if (o1.startsWith("java.") && o2.startsWith("java.")) {
						return o1.compareTo(o2);
					}
					if (o1.startsWith("java.")) {
						return -1;
					}
					if (o2.startsWith("java.")) {
						return 1;
					}
					if (o1.startsWith("org.") && o2.startsWith("org.")) {
						return o1.compareTo(o2);
					}
					if (o1.startsWith("org.")) {
						return -1;
					}
					if (o2.startsWith("org.")) {
						return 1;
					}
					if (StringUtils.isNotEmpty(rootPackage)) {
						if (o1.startsWith(rootPackage) && o2.startsWith(rootPackage)) {
							return o1.compareTo(o2);
						}
						if (o1.startsWith(rootPackage)) {
							return 1;
						}
						if (o2.startsWith(rootPackage)) {
							return -1;
						}
					}
					return o1.compareTo(o2);
				}
			});
			for (String className : importClasseList) {
				builder.append("import ").append(className).append(";").newLine();
			}
		}
		return builder.toString();
	}
	/**
	 * 生成类的Annotation信息
	 * @param annotations
	 * @return
	 */
	private static String generateClassAnnotation(AppClass appClass) {
		CustomStringBuilder builder = new CustomStringBuilder();
		List<AppAnnotation> annotations = new ArrayList<>();
		if (appClass.getAnnotations() != null) {
			annotations.addAll(appClass.getAnnotations());
		}
		for (int i = 0; i < annotations.size(); i++) {
			AppAnnotation classAnnotation = annotations.get(i);
			if (builder.length() > 0) {
				builder.newLine();
			}
			builder.append(classAnnotation.toString());
		}
		return builder.toString();
	}
	/**
	 * 生成类的关系信息，比如extends A<Long>, B<Long>
	 * @param relation
	 * @param classList
	 * @return
	 */
	private static String generateClassRelation(String relation, List<AppClass> classList) {
		CustomStringBuilder builder = new CustomStringBuilder();
		if (classList != null && !classList.isEmpty()) {
			builder.append(relation).append(" ");
			for (int i = 0; i < classList.size(); i++) {
				AppClass appClass = classList.get(i);
				builder.append(appClass.getClassName());
				List<AppClass> genericClasses = appClass.getGenericClasses();
				if (genericClasses != null && !genericClasses.isEmpty()) {
					builder.append("<");
					for (int m = 0; m < genericClasses.size(); m++) {
						AppClass genericClass = genericClasses.get(m);
						builder.append(genericClass.getClassName());
						if (m < genericClasses.size() - 1) {
							builder.append(", ");
						}
					}
					builder.append(">");
				}
				if (i < classList.size() - 1) {
					builder.append(", ");
				}
			}
		}
		return builder.toString();
	}
	/**
	 * 生成类的字段信息（包括基础字段，扩展字段）
	 * @param context
	 * @param appClass
	 * @return
	 */
	private static String generateClassField(AppClass appClass) {
		CustomStringBuilder builder = new CustomStringBuilder();
		builder.append(generateClassField(appClass.getFields(), appClass.isOverrideParentField()));
		builder.append(generateClassField(appClass.getExtFields(), appClass.isOverrideParentField()));
		return builder.toString();
	}
	/**
	 * 生成类的字段信息
	 * @param fieldList
	 * @param isOverride	是否重写父类字段
	 * @return
	 */
	private static String generateClassField(List<AppClassField> fieldList, boolean isOverride) {
		CustomStringBuilder builder = new CustomStringBuilder();
		if (fieldList != null && !fieldList.isEmpty()) {
			for (AppClassField classField : fieldList) {
				CustomStringBuilder fieldCommentBuild = new CustomStringBuilder("");
				CustomStringBuilder fieldBuild = new CustomStringBuilder("");
				if (classField instanceof AppClassFieldList) {
					AppClassFieldList field = (AppClassFieldList)classField;
					if (field.isParentExistsField() && !isOverride) {//父级已经存在该字段
						continue;
					}
					if (StringUtils.isNotEmpty(field.getDesc())) {
						fieldCommentBuild.appendTab().append("/** ").append(StringUtils.trim(field.getFullDesc())).append(" */");
					}
					if (field.isAutowired()) {
						fieldBuild.newLine().appendTab().append("@Autowired");
					}
					if (field.getAnnotation() != null) {
						AppAnnotation annotation = field.getAnnotation();
						fieldBuild.newLine().appendTab().append("@" + annotation.getName());
						if (annotation.getValue() != null) {
							fieldBuild.append("(").append(annotation.getValue()).append(")");
						}
					}
					fieldBuild.newLine().appendTab().append(StringUtils.trim(field.getModifier()));
					if (field.isStatic()) {
						fieldBuild.append(" static");
					}
					if (field.isFinal()) {
						fieldBuild.append(" final");
					}
					fieldBuild.append(" ").append(field.getType());
					String[] typeGenericClasses = field.getTypeGenericClasses();
					if (typeGenericClasses != null && typeGenericClasses.length > 0) {
						fieldBuild.append("<");
						for (int n = 0; n < typeGenericClasses.length; n++) {
							String genericClasses = typeGenericClasses[n];
							fieldBuild.append(genericClasses);
							if (n < typeGenericClasses.length - 1) {
								fieldBuild.append(", ");
							}
						}
						fieldBuild.append(">");
					}
					fieldBuild.append(" ").append(field.getName());
					if (field.getValue() != null) {
						fieldBuild.append(" = ").append(field.getValue().toString());
					}
					fieldBuild.append(";").newLine();
				} else if (classField instanceof AppClassFieldBody) {
					AppClassFieldBody field = (AppClassFieldBody)classField;
					fieldBuild.newLine().appendTab().append(StringUtils.trim(field.getBody()));
				}
				builder.append(fieldCommentBuild).append(fieldBuild);
			}
		}
		return builder.toString();
	}
	/**
	 * 生成类的构建函数
	 * @param classConstructorList
	 * @return
	 */
	private static String generateClassConstructor(AppClass appClass) {
		CustomStringBuilder builder = new CustomStringBuilder();
		List<AppClassConstructor> constructorList = new ArrayList<>();
		if (appClass.getConstructors() != null) {
			constructorList.addAll(appClass.getConstructors());
		}
		if (!constructorList.isEmpty()) {
			for (AppClassConstructor classConstructor : constructorList) {
				builder.appendTab().append(classConstructor.getModifiers()).append(" ").append(appClass.getClassName()).append("(");
				List<AppClassMethodParam> params = classConstructor.getParameters();
				if (params != null && !params.isEmpty()) {
					for (int i = 0; i < params.size(); i++) {
						AppClassMethodParam param = params.get(i);
						builder.append(param.getType()).append(" ").append(param.getName());
						if (i < params.size() - 1) {
							builder.append(", ");
						}
					}
				}
				builder.append(") {");
				if (classConstructor.getBody() != null) {
					builder.newLine().appendTab(2).append(StringUtils.trim(classConstructor.getBody().toString()));
				}
				builder.newLine().appendTab().append("}");
			}
		}
		return builder.toString();
	}
	/**
	 * 生成字段的get与set方法
	 * @param fieldList
	 * @return
	 */
	private static String generateClassFieldMethod(List<AppClassField> fieldList, boolean isOverride) {
		CustomStringBuilder builder = new CustomStringBuilder();
		if (fieldList != null && !fieldList.isEmpty()) {
			CustomStringBuilder fieldMethods = new CustomStringBuilder("");
			for (AppClassField classField : fieldList) {
				if (classField instanceof AppClassFieldSerial) {//序列化字段不生成get、set方法
					continue;
				} else if (classField instanceof AppClassFieldList) {
					AppClassFieldList field = (AppClassFieldList)classField;
					if (field.isParentExistsField() && !isOverride) {//父级已经存在该字段
						continue;
					}
					String desc = StringUtils.trim(field.getFullDesc());
					String fieldName = field.getName();
					String fieldType = field.getType();
					
					String[] typeGenericClasses = field.getTypeGenericClasses();
					if (typeGenericClasses != null && typeGenericClasses.length > 0) {
						CustomStringBuilder fieldGenericBuild = new CustomStringBuilder("");
						fieldGenericBuild.append("<");
						for (int n = 0; n < typeGenericClasses.length; n++) {
							String genericClasses = typeGenericClasses[n];
							fieldGenericBuild.append(genericClasses);
							if (n < typeGenericClasses.length - 1) {
								fieldGenericBuild.append(", ");
							}
						}
						fieldGenericBuild.append(">");
						fieldType = field.getType() + fieldGenericBuild.toString();
					}
					fieldMethods.appendTab().append("/**")
					.newLine().appendTab().append(" * 获取 " + desc)
					.newLine().appendTab().append(" * @return " + fieldType)
					.newLine().appendTab().append(" */")
				    .newLine().appendTab().append("public " + fieldType + " " + TableUtil.getBeanGetMethod(fieldName) + "(){")
				    .newLine().appendTab(2).append("return this." + fieldName + ";")
					.newLine().appendTab().append("}")
					//set方法
					.newLine(2).appendTab().append("/**")
					.newLine().appendTab().append(" * 设置 " + desc)
					.newLine().appendTab().append(" * @param " + fieldName)
					.newLine().appendTab().append(" */")
					.newLine().appendTab().append("public void " + TableUtil.getBeanSetMethod(fieldName) + "(" + fieldType + " " + fieldName).append("){")
					.newLine().appendTab(2).append("this." + fieldName + " = " + fieldName + ";")
					.newLine().appendTab().append("}").newLine(2);
				} else if (classField instanceof AppClassFieldBody) {
					AppClassFieldBody field = (AppClassFieldBody)classField;
					fieldMethods.newLine().append(StringUtils.trim(field.getBody()));
				}
			}
			builder.append(fieldMethods);
		}
		return builder.toString();
	}
	/**
	 * 生成对象的方法信息
	 * @param methodList
	 * @return
	 */
	private static String generateClassMethod(AppClass appClass) {
		CustomStringBuilder builder = new CustomStringBuilder();
		if (appClass == null) {
			return builder.toString();
		}
		List<AppClassMethod> methodList = new ArrayList<>();
		if (appClass.getMethods() != null) {
			methodList.addAll(appClass.getMethods());
		}
		ClassType classType = ClassType.getByValue(appClass.getType());
		for (AppClassMethod classMethod : methodList) {
			CustomStringBuilder methodCommentBuild = new CustomStringBuilder("");
			CustomStringBuilder methodBuild = new CustomStringBuilder("");
			if (classMethod instanceof AppClassMethodGetAndSet) {//字段的get与set方法
				builder.append(generateClassFieldMethod(appClass.getFields(), appClass.isOverrideParentField()));
				builder.append(generateClassFieldMethod(appClass.getExtFields(), appClass.isOverrideParentField()));
			} else if (classMethod instanceof AppClassMethodList) {
				AppClassMethodList method = (AppClassMethodList)classMethod;
				methodCommentBuild.newLine().appendTab().append("/**")
				 		.newLine().appendTab().append(" * ").append(StringUtils.trim(method.getDesc()));
				methodBuild.newLine().appendTab();
				if (StringUtils.isNotEmpty(method.getModifier())) {
					methodBuild.append(method.getModifier()).append(" ");
				}
				if (method.isStatic()) {
					methodBuild.append("static").append(" ");
				}
				if (method.isFinal()) {
					methodBuild.append("final").append(" ");
				}
				methodBuild.append(StringUtils.trim(method.getReturnType())).append(" ").append(StringUtils.trim(method.getName())).append("(");
				List<AppClassMethodParam> paramList = method.getParams();
				if (paramList != null) {
					for (int index = 0; index < paramList.size(); index++) {
						AppClassMethodParam param = paramList.get(index);
						methodBuild.append(param.getType()).append(" ").append(param.getName());
						methodCommentBuild.newLine().appendTab().append(" * @param ").append(param.getName()).appendTab().append(StringUtils.trim(param.getDesc()));
						if (index < paramList.size() - 1) {
							methodBuild.append(", ");
						}
					}
				}
				methodBuild.append(")");
				String[] throwsType = method.getThrowsType();
				if (throwsType != null && throwsType.length > 0) {
					methodBuild.append(" throws ").append(StringUtils.converArray2Str(throwsType));
				}
				if (method.getBody() == null) {
					if (classType == ClassType.Class) {
						methodBuild.append(" {").newLine().appendTab().append("}");
					} else {
						methodBuild.append(";");
					}
				} else {
					methodBuild.append(" {").newLine().appendTab(2).append(method.getBody()).newLine().appendTab().append("}");
				}
				methodCommentBuild.newLine().appendTab().append(" */");
				if (StringUtils.isEmpty(method.getDesc()) && (paramList == null || paramList.isEmpty())) {
					methodCommentBuild = new CustomStringBuilder();//清空注释
				}
			} else if (classMethod instanceof AppClassMethodBody){
				AppClassMethodBody method = (AppClassMethodBody)classMethod;
				methodBuild.newLine().appendTab(2).append(StringUtils.trim(method.getBody()));
			}
			builder.append(methodCommentBuild).append(methodBuild).newLine();
		}
		return builder.toString();
	}
	
}	
