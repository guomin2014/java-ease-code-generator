package com.gm.easecode.common.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

/**
 * 应用类
 * @author GM
 * @date 2024-04-28
 */
public class AppClass implements Serializable {
	private static final long serialVersionUID = -6464781573010557894L;
	/** 类名 */
	private String className;
	/** 文件名 */
	private String fileName;
	/** 文件路径 */
	private String filePath;
	/** 包名 */
	private String packageName;
	/** 访问控制修饰符，private，public，protected */
	private String modifier = "public";
	/** 类类型，class/interface */
	private String type = ClassType.Class.getValue();
	/** annotation修饰集合 */
	private List<AppAnnotation> annotations;
	/** 导入的类集合 */
	private Set<String> importClasses;
	/** 构造函数列表 */
	private List<AppClassConstructor> constructors;
	/** 字段集合 */
	private List<AppClassField> fields;
	/** 扩展字段集合(不参与toString、init、表sql等方法输出) */
	private List<AppClassField> extFields;
	/** 方法集合 */
	private List<AppClassMethod> methods;
	/** 抽像方法集合（需要子类实现） */
	private List<AppClassMethod> abstractMethods;
	/** 继承的类 */
	private List<AppClass> extendsClasses;
	/** 实现的类 */
	private List<AppClass> implementsClasses;
	/** 类的泛型 */
	private List<AppClass> genericClasses;
	/** 类简单描述信息 */
	private String desc;
	/** 类详细描述信息 */
	private String description;
	/** 是否是可变对象，true：表示类名(className)是占位符，需要根据实际情况转换 */
	private boolean isVariable = false;
	/** 类的别名 */
	private String aliasName;
	/** 类的标识 */
	private String key;
	/** 是否重写父类字段 */
	private boolean isOverrideParentField = false;
	
	public AppClass() {}
	
	public AppClass(String className) {
		this(className, null, null);
	}
	
	public AppClass(String className, String packageName, String filePath) {
		this(className, packageName, filePath, null, null);
	}
	
	public AppClass(String className, String packageName, String filePath, String aliasName, String desc) {
		this(ClassType.Class.getValue(), className, packageName, filePath, aliasName, desc);
	}
	
	public AppClass(String classType, String className, String packageName, String filePath, String aliasName, String desc) {
		this.type = classType;
		this.className = className;
		this.packageName = packageName;
		this.filePath = filePath;
		this.fileName = className + ".java";
		this.aliasName = aliasName;
		this.desc = desc;
		StringBuilder descBuild = new StringBuilder();
		descBuild.append(StringUtils.trim(desc));
		if (StringUtils.isNotEmpty(aliasName)) {
			if (descBuild.length() > 0) {
				descBuild.append("-");
			}
			descBuild.append(StringUtils.trim(aliasName));
		}
		if (StringUtils.isNotEmpty(classType)) {
			if (classType.equalsIgnoreCase(ClassType.Interface.getValue())) {
				descBuild.append("接口");
			} else {
				descBuild.append("实现");
			}
		}
		this.description = descBuild.toString();
	}
	
	public static AppClass createVariableInstance(String classNameVariable) {
		AppClass clazz = new AppClass(classNameVariable);
		clazz.isVariable = true;
		return clazz;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Set<String> getImportClasses() {
		return importClasses;
	}
	public void setImportClasses(Set<String> importClasses) {
		this.importClasses = importClasses;
	}
	public List<AppClassConstructor> getConstructors() {
		return constructors;
	}
	public void setConstructors(List<AppClassConstructor> constructors) {
		this.constructors = constructors;
	}
	public List<AppClassField> getFields() {
		return fields;
	}
	public void setFields(List<AppClassField> fields) {
		this.fields = fields;
	}
	public List<AppClassMethod> getMethods() {
		return methods;
	}
	public void setMethods(List<AppClassMethod> methods) {
		this.methods = methods;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public List<AppAnnotation> getAnnotations() {
		return annotations;
	}
	public void setAnnotations(List<AppAnnotation> annotations) {
		this.annotations = annotations;
	}
	public List<AppClassField> getExtFields() {
		return extFields;
	}
	public void setExtFields(List<AppClassField> extFields) {
		this.extFields = extFields;
	}
	public List<AppClass> getExtendsClasses() {
		return extendsClasses;
	}
	public void setExtendsClasses(List<AppClass> extendsClasses) {
		this.extendsClasses = extendsClasses;
	}
	public List<AppClass> getImplementsClasses() {
		return implementsClasses;
	}
	public void setImplementsClasses(List<AppClass> implementsClasses) {
		this.implementsClasses = implementsClasses;
	}
	public List<AppClass> getGenericClasses() {
		return genericClasses;
	}
	public void setGenericClasses(List<AppClass> genericClasses) {
		this.genericClasses = genericClasses;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isVariable() {
		return isVariable;
	}
	public void setVariable(boolean isVariable) {
		this.isVariable = isVariable;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public boolean isOverrideParentField() {
		return isOverrideParentField;
	}
	public void setOverrideParentField(boolean isOverrideParentField) {
		this.isOverrideParentField = isOverrideParentField;
	}
	public List<AppClassMethod> getAbstractMethods() {
		return abstractMethods;
	}
	public void setAbstractMethods(List<AppClassMethod> abstractMethods) {
		this.abstractMethods = abstractMethods;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getFullClassName() {
		return (StringUtils.isNotEmpty(this.packageName) ? StringUtils.trim(this.packageName) + "." : "") + this.className;
	}
	public void addImportClass(String importClass) {
		if (this.importClasses == null) {
			this.importClasses = new TreeSet<>();
		}
		if (StringUtils.isNotEmpty(importClass)) {
			this.importClasses.add(importClass);
		}
	}
	public void addImportClasses(Set<String> importClasses) {
		if (this.importClasses == null) {
			this.importClasses = new TreeSet<>();
		}
		if (importClasses == null || importClasses.isEmpty()) {
			return;
		}
		this.importClasses.addAll(importClasses);
	}
	public void addAnnotation(AppAnnotation annotation) {
		if (this.annotations == null) {
			this.annotations = new ArrayList<>();
		}
		if (annotation != null) {
			this.annotations.add(annotation);
			this.addImportClass(annotation.getName());
		}
	}
	public void addAnnotations(List<AppAnnotation> annotations) {
		if (this.annotations == null) {
			this.annotations = new ArrayList<>();
		}
		if (annotations == null || annotations.isEmpty()) {
			return;
		}
		this.annotations.addAll(annotations);
		for (AppAnnotation annotation : annotations) {
			this.addImportClass(annotation.getName());
		}
	}
	public void addExtendsClass(AppClass extendsClass) {
		if (this.extendsClasses == null) {
			this.extendsClasses = new ArrayList<>();
		}
		if (extendsClass != null) {
			this.extendsClasses.add(extendsClass);
			this.addImportClass(extendsClass.getFullClassName());
		}
	}
	
	public void addImplementsClass(AppClass implementsClass) {
		if (this.implementsClasses == null) {
			this.implementsClasses = new ArrayList<>();
		}
		if (implementsClass != null) {
			this.implementsClasses.add(implementsClass);
			this.addImportClass(implementsClass.getFullClassName());
		}
	}
	
	public void addImplementsClasses(List<AppClass> implementsClasses) {
		if (this.implementsClasses == null) {
			this.implementsClasses = new ArrayList<>();
		}
		if (implementsClasses == null || implementsClasses.isEmpty()) {
			return;
		}
		this.implementsClasses.addAll(implementsClasses);
		for (AppClass implementsClass : implementsClasses) {
			this.addImportClass(implementsClass.getFullClassName());
		}
	}
	public void addConstructor(AppClassConstructor constructor) {
		if (this.constructors == null) {
			this.constructors = new ArrayList<>();
		}
		if (constructor != null) {
			this.constructors.add(constructor);
		}
	}
	public void addConstructors(List<AppClassConstructor> constructors) {
		if (this.constructors == null) {
			this.constructors = new ArrayList<>();
		}
		if (constructors == null || constructors.isEmpty()) {
			return;
		}
		this.constructors.addAll(constructors);
	}
	public void addField(AppClassField field) {
		if (field == null) {
			return;
		}
		List<AppClassField> fields = new ArrayList<>();
		fields.add(field);
		this.addFields(fields);
	}
	
	public void addFields(List<AppClassField> fields) {
		if (this.fields == null) {
			this.fields = new ArrayList<>();
		}
		if (fields == null || fields.isEmpty()) {
			return;
		}
		this.fields.addAll(fields);
		this.addImportByFields(fields);
	}
	
	public void addExtFields(List<AppClassField> extFields) {
		if (this.extFields == null) {
			this.extFields = new ArrayList<>();
		}
		if (extFields == null || extFields.isEmpty()) {
			return;
		}
		this.extFields.addAll(extFields);
		this.addImportByFields(extFields);
	}
	private void addImportByFields(List<AppClassField> fields) {
		if (fields == null || fields.isEmpty()) {
			return;
		}
		Set<String> importClasses = new TreeSet<>();
		for (AppClassField classField : fields) {
			if (classField instanceof AppClassFieldList) {
				AppClassFieldList field = (AppClassFieldList)classField;
				if (field.isParentExistsField() && !this.isOverrideParentField) {
					continue;
				}
				importClasses.add(field.getType());
				String[] genericClasses = field.getTypeGenericClasses();
				if (genericClasses != null && genericClasses.length > 0) {
					for (String genericClass : genericClasses) {
						importClasses.add(genericClass);
					}
				}
				AppAnnotation annotation = field.getAnnotation();
				if (annotation != null) {
					importClasses.add(annotation.getName());
				}
			}
		}
		this.addImportClasses(importClasses);
	}
	private void addImportByMethods(List<AppClassMethod> methods) {
		if (methods == null || methods.isEmpty()) {
			return;
		}
		Set<String> importClasses = new TreeSet<>();
		for (AppClassMethod classMethod : methods) {
			if (classMethod instanceof AppClassMethodList) {
				AppClassMethodList method = (AppClassMethodList)classMethod;
				if (StringUtils.isNotEmpty(method.getReturnType())) {
					importClasses.add(method.getReturnType());
				}
				List<AppClassMethodParam> params = method.getParams();
				if (params != null) {
					for (AppClassMethodParam param : params) {
						importClasses.add(param.getType());
					}
				}
			}
		}
		this.addImportClasses(importClasses);
	}
	/**
	 * 当前类是否已经存在指定字段
	 * @param fieldName
	 * @return
	 */
	public boolean isExistsField(String fieldName) {
		if (fields != null) {
			for (AppClassField classField : fields) {
				if (classField instanceof AppClassFieldList) {
					AppClassFieldList field = (AppClassFieldList)classField;
					if (field.getName().equals(fieldName)) {
						return true;
					}
				}
			}
		}
		if (extFields != null) {
			for (AppClassField classField : extFields) {
				if (classField instanceof AppClassFieldList) {
					AppClassFieldList field = (AppClassFieldList)classField;
					if (field.getName().equals(fieldName)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void addMethod(AppClassMethod method) {
		if (method == null) {
			return;
		}
		List<AppClassMethod> methods = new ArrayList<>();
		methods.add(method);
		this.addMethods(methods);
	}
	public void addMethods(List<AppClassMethod> methods) {
		if (this.methods == null) {
			this.methods = new ArrayList<>();
		}
		if (methods == null || methods.isEmpty()) {
			return;
		}
		this.methods.addAll(methods);
		this.addImportByMethods(methods);
	}
}
