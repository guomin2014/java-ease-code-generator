package com.gm.easecode.common.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.gm.easecode.common.util.CustomStringBuilder;
import com.gm.easecode.common.util.ObjectUtil;
import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.util.TableUtil;
import com.gm.easecode.config.AppConfig;
import com.gm.easecode.frame.FrameworkProvider;

public class AppTableContext {
	/** 类文件 */
	private Map<String, AppClass> classMap;
	/** 前端页面文件 */
	private List<AppPage> pageList;
	
	private FrameworkProvider frameworkProvider;
	
	public AppConfig config;
	
	public AppTable table;
	
	public AppModule appModule;
	
	public AppTableNameParam nameParam;
	
	private final String incrementSuff = "Increment";
	private final String startSuff = "Start";
	private final String endSuff = "End";
	private final String startWithSuff = "StartWith";
	private final String endWithSuff = "EndWith";
	private final String matchSuff = "MatchWith";
	private final String listSuff = "List";
	private final String orListSuff = "ORList";
	
	/** 需要被过滤的导入类 */
	protected Set<String> filterImportClasses = new HashSet<>();
	/** 实体全量名映射，如：key=AppException, value=com.wisdom.agriculture.framework.exception.AppException */
	protected Map<String, String> qualifiedClassNameMap = new HashMap<String, String>();
	
	public AppTableContext(FrameworkProvider frameworkProvider, AppConfig config, AppTable table, AppModule appModule) {
		this.frameworkProvider = frameworkProvider;
		this.config = config;
		this.table = table;
		this.appModule = appModule;
		this.nameParam = new AppTableNameParam(config, table.getTableName(), appModule);
		this.classMap = new HashMap<>();
		this.init();
	}
	
	private void init() {
		this.initFilterImportClass();
		this.initCommonQualifiedClassName();
		//获取主键类型
		String keyType = "";
		if(this.table.getPriNum() > 1){//联合主键
			keyType = this.nameParam.getEntityKey();
		} else {
			if (this.table.getPriColList().size() > 0){
				keyType = this.table.getPriColList().get(0).getJavaType();
			}else{
				keyType = "Long";
			}
		}
		List<AppTableColumn> columnList = table.getColumnList();
		//初始化Entity对象
		AppClass entityClass = this.createAppClass(FileAliasMode.Entity.name(), this.nameParam.getEntityName(), this.nameParam.getEntityPkgName(), this.nameParam.getEntityPath(), this.table.getComment(), keyType);
		entityClass.addField(new AppClassFieldSerial());
		this.createAppClassFields(entityClass, columnList);
		List<AppClassMethod> entityMethods = new ArrayList<>();
		entityMethods.add(new AppClassMethodGetAndSet());
		entityMethods.add(this.createToStringMethod(entityClass));
		entityMethods.add(this.createInitAttrValueMethod(entityClass));
		entityClass.addMethods(entityMethods);
		classMap.put(entityClass.getAliasName(), entityClass);
		classMap.put("EntityKey", new AppClass(keyType));
		//初始化Query对象
		AppClass queryClass = this.createAppClass(FileAliasMode.Query.name(), this.nameParam.getQueryName(), this.nameParam.getEntityPkgName(), this.nameParam.getEntityPath(), this.table.getComment(), keyType);
		queryClass.addField(new AppClassFieldSerial());
		this.createAppClassFields(queryClass, columnList);
		queryClass.addMethod(new AppClassMethodGetAndSet());
		classMap.put(queryClass.getAliasName(), queryClass);
		//初始化Form对象
		AppClass formClass = this.createAppClass(FileAliasMode.Form.name(), this.nameParam.getContFormName(), this.nameParam.getControllerPkgName(), this.nameParam.getControllerPath(), this.table.getComment(), keyType);
		List<AppClassField> formFields = new ArrayList<>();
		formFields.add(new AppClassFieldList("entity", entityClass.getClassName(), "new " + entityClass.getClassName() + "()", "实体对象"));
		formFields.add(new AppClassFieldList("query", queryClass.getClassName(), "new " + queryClass.getClassName() + "()", "查询对象"));
		formClass.setFields(formFields);
		formClass.addMethod(new AppClassMethodGetAndSet());
		formClass.addImportClass(queryClass.getFullClassName());
		classMap.put(formClass.getAliasName(), formClass);
		//初始化Dto对象
		AppClass requestDtoClass = this.createAppClass(FileAliasMode.RequestDto.name(), this.nameParam.getControllerDtoReqName(), this.nameParam.getControllerDtoPkgName(), this.nameParam.getControllerDtoPath(), this.table.getComment(), keyType);
		requestDtoClass.setOverrideParentField(true);
		requestDtoClass.addField(new AppClassFieldSerial());
		this.createAppClassFields(requestDtoClass, columnList);
		requestDtoClass.addMethod(new AppClassMethodGetAndSet());
		classMap.put(requestDtoClass.getAliasName(), requestDtoClass);
		AppClass requestPageDtoClass = this.createAppClass(FileAliasMode.RequestPageDto.name(), this.nameParam.getControllerDtoReqPageName(), this.nameParam.getControllerDtoPkgName(), this.nameParam.getControllerDtoPath(), this.table.getComment(), keyType);
		requestPageDtoClass.setOverrideParentField(true);
		requestPageDtoClass.addField(new AppClassFieldSerial());
		this.createAppClassFields(requestPageDtoClass, columnList);
		requestPageDtoClass.addMethod(new AppClassMethodGetAndSet());
		classMap.put(requestPageDtoClass.getAliasName(), requestPageDtoClass);
		AppClass responseDtoClass = this.createAppClass(FileAliasMode.ResponseDto.name(), this.nameParam.getControllerDtoRspName(), this.nameParam.getControllerDtoPkgName(), this.nameParam.getControllerDtoPath(), this.table.getComment(), keyType);
		responseDtoClass.setOverrideParentField(true);
		responseDtoClass.addField(new AppClassFieldSerial());
		this.createAppClassFields(responseDtoClass, columnList);
		responseDtoClass.addMethod(new AppClassMethodGetAndSet());
		classMap.put(responseDtoClass.getAliasName(), responseDtoClass);
		//初始化Dao对象
		AppClass daoClass = this.createAppInterface(FileAliasMode.Dao.name(), this.nameParam.getDaoName(), this.nameParam.getDaoPkgName(), this.nameParam.getDaoPath(), this.table.getComment(), keyType);
		classMap.put(daoClass.getAliasName(), daoClass);
		//初始化DaoImpl对象
		AppClass daoImplClass = this.createAppClass(FileAliasMode.DaoImpl.name(), this.nameParam.getDaoImplName(), this.nameParam.getDaoImplPkgName(), this.nameParam.getDaoImplPath(), this.table.getComment(), keyType);
		daoImplClass.addImplementsClass(daoClass);
		classMap.put(daoImplClass.getAliasName(), daoImplClass);
		//初始化Service对象
		AppClass serviceClass = this.createAppInterface(FileAliasMode.Service.name(), this.nameParam.getServiceName(), this.nameParam.getServicePkgName(), this.nameParam.getServicePath(), this.table.getComment(), keyType);
		classMap.put(serviceClass.getAliasName(), serviceClass);
		//初始化ServiceImpl对象
		AppClass serviceImplClass = this.createAppClass(FileAliasMode.ServiceImpl.name(), this.nameParam.getServiceImplName(), this.nameParam.getServiceImplPkgName(), this.nameParam.getServiceImplPath(), this.table.getComment(), keyType);
		serviceImplClass.addImplementsClass(serviceClass);
		classMap.put(serviceImplClass.getAliasName(), serviceImplClass);
		//初始化controller对象
		AppClass controllerClass = this.createAppClass(FileAliasMode.Controller.name(), this.nameParam.getControllerName(), this.nameParam.getControllerPkgName(), this.nameParam.getControllerPath(), this.table.getComment(), keyType);
		classMap.put(controllerClass.getAliasName(), controllerClass);
		//合并类的业务实现，比如内置模块
		mergeInnerClass();
	}
	
	private void initFilterImportClass() {
		this.filterImportClasses.add("void");
		this.filterImportClasses.add("Void");
		this.filterImportClasses.add("String");
		this.filterImportClasses.add("int");
		this.filterImportClasses.add("Integer");
		this.filterImportClasses.add("long");
		this.filterImportClasses.add("Long");
		this.filterImportClasses.add("double");
		this.filterImportClasses.add("Double");
		this.filterImportClasses.add("float");
		this.filterImportClasses.add("Float");
	}
	
	private void initCommonQualifiedClassName() {
		qualifiedClassNameMap.put("List", "java.util.List");
		qualifiedClassNameMap.put("Date", "java.util.Date");
		qualifiedClassNameMap.put("BigDecimal", "java.math.BigDecimal");
	}
	
	private AppClass createAppInterface(String aliasName, String className, String packageName, String filePath, String desc, String baseClassKey) {
		return this.createAppClass(aliasName, ClassType.Interface.getValue(), className, packageName, filePath, desc, baseClassKey);
	}
	
	private AppClass createAppClass(String aliasName, String className, String packageName, String filePath, String desc, String baseClassKey) {
		return this.createAppClass(aliasName, ClassType.Class.getValue(), className, packageName, filePath, desc, baseClassKey);
	}
	
	private AppClass createAppClass(String aliasName, String classType, String className, String packageName, String filePath, String desc, String baseClassKey) {
		AppClass appClass = new AppClass(classType, className, packageName, filePath, aliasName, desc);
		AppClassDefinition classDefinition = new AppClassDefinition(aliasName, baseClassKey, this.config.getControllerClassStyle(), this.table.isTreeTable(), this.table.isSubmeterTable(), this.table.getSubmeterTableStrategy());
		//设置类的继承类
		AppClass baseClass = this.frameworkProvider.getClassExtendsClass(classDefinition);
		handlerClassExtendsOrImplements(appClass, baseClass);
		//设置类的实现接口
		List<AppClass> superInterfaceList = this.frameworkProvider.getClassImplementsClass(classDefinition);
		if (superInterfaceList != null) {
			for (AppClass interfaceClass : superInterfaceList) {
				handlerClassExtendsOrImplements(appClass, interfaceClass);
			}
		}
		//设置类的注解修饰
		List<AppAnnotation> baseAnnotations = this.frameworkProvider.getClassAnnotation(classDefinition);
		if (baseAnnotations != null && !baseAnnotations.isEmpty()) {
			for (AppAnnotation anno : baseAnnotations) {
				AppAnnotation newAnno = null;
				try {
					newAnno = ObjectUtil.clone(anno);
				} catch (Exception e) {}
				if (newAnno != null) {
					if (newAnno.getValue() instanceof AliasVO) {//值是占位符，则需要获取真实值
						String aName = ((AliasVO)anno.getValue()).getAliasName();
						newAnno.setValue(this.replaceAliasVariable(aName));
					}
					appClass.addAnnotation(newAnno);
				}
			}
		}
		//设置类的构建函数
		List<AppClassConstructor> baseConstructors = this.frameworkProvider.getClassConstructor(classDefinition);
		if (baseConstructors != null && !baseConstructors.isEmpty()) {
			for (AppClassConstructor constructor : baseConstructors) {
				AppClassConstructor cons = null;
				try {
					cons = ObjectUtil.clone(constructor);
				} catch (Exception e) {}
				if (cons != null) {
					if (cons.getBody() instanceof AliasVO) {
						String body = ((AliasVO)constructor.getBody()).getAliasName();
						cons.setBody(this.replaceAliasVariable(body));
					}
					appClass.addConstructor(cons);
				}
			}
		}
		return appClass;
	}
	/**
	 * 解析Class，获取类的泛型、需要引入的包、构造方法、方法
	 * @param baseClass
	 * @return
	 */
	private void handlerClassExtendsOrImplements(AppClass appClass, AppClass baseClass) {
		if (baseClass != null) {
			if (baseClass.isVariable()) {//是可变对象，则className为变量名
				baseClass = this.classMap.get(baseClass.getClassName());
			}
			AppClass extendsClass = null;
			if (baseClass != null) {
				try {
					extendsClass = ObjectUtil.clone(baseClass);
				} catch (Exception e) {}
			}
			if (extendsClass != null) {
				appClass.addExtendsClass(extendsClass);
				List<AppClass> genericClasses =  extendsClass.getGenericClasses();
				if (genericClasses != null) {
					for (AppClass genericClass : genericClasses) {
						if (genericClass.isVariable()) {//是可变对象，则className为变量名
							//获取真正的对象
							AppClass realClass = this.classMap.get(genericClass.getClassName());
							if (realClass != null) {
								try {
									BeanUtils.copyProperties(genericClass, realClass);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						if (genericClass != null) {
							appClass.addImportClass(genericClass.getFullClassName());
						}
					}
				}
				//设置继承类的抽像方法实现
				List<AppClassMethod> abstractMethods = extendsClass.getAbstractMethods();
				String classType = appClass.getType();
				if (classType.equalsIgnoreCase(ClassType.Class.getValue()) && abstractMethods != null) {
					for (AppClassMethod classMethod : abstractMethods) {
						try {
							AppClassMethod newMethod = ObjectUtil.clone(classMethod);
							if (newMethod instanceof AppClassMethodList) {
								AppClassMethodList method = (AppClassMethodList)newMethod;
								if (method.getBody() instanceof AliasVO) {
									String aName = ((AliasVO)method.getBody()).getAliasName();
									method.setBody(this.replaceAliasVariable(aName));
								}
								appClass.addImportClass(method.getReturnType());
								List<AppClassMethodParam> params = method.getParams();
								if (params != null) {
									for (AppClassMethodParam param : params) {
										appClass.addImportClass(param.getType());
									}
								}
							}
							appClass.addMethod(newMethod);
						} catch (Exception e) {}
					}
				}
			}
		}
	}
	/**
	 * 合并内部模块相关类的属性
	 */
	private void mergeInnerClass() {
		if (table.isInnerTable()) {
			for(Map.Entry<String, AppClass> entry : classMap.entrySet()) {
				String aliasName = entry.getKey();
				AppClass appClass = entry.getValue();
				AppClass innerClass = table.getInnerClass(aliasName);
				if (innerClass != null) {
					if (innerClass.getImportClasses() != null && !innerClass.getImportClasses().isEmpty()) {
						appClass.addImportClasses(innerClass.getImportClasses());
					}
					if (innerClass.getAnnotations() != null && !innerClass.getAnnotations().isEmpty()) {
						appClass.addAnnotations(innerClass.getAnnotations());
					}
					if (innerClass.getFields() != null && !innerClass.getFields().isEmpty()) {
						appClass.addFields(innerClass.getFields());
					}
					if (innerClass.getExtFields() != null && !innerClass.getExtFields().isEmpty()) {
						appClass.addExtFields(innerClass.getExtFields());
					}
					if (innerClass.getConstructors() != null && !innerClass.getConstructors().isEmpty()) {
						appClass.addConstructors(innerClass.getConstructors());
					}
					if (innerClass.getMethods() != null && !innerClass.getMethods().isEmpty()) {
						appClass.addMethods(innerClass.getMethods());
					}
				}
			}
		}
	}
	
	private void createAppClassFields(AppClass appClass, List<AppTableColumn> columnList) {
		if (columnList == null || columnList.isEmpty()) {
			return;
		}
		List<AppClassField> fields = new ArrayList<>();
		List<AppClassField> extFields = new ArrayList<>();
		List<AppClassField> identifyFields = new ArrayList<>();//标识查询字段
		String aliasName = appClass.getAliasName();
		List<AppClass> extendsClasses = appClass.getExtendsClasses();
		AppClassDefinition classDefinition = new AppClassDefinition(aliasName);
		AppAnnotation annotation = this.frameworkProvider.getClassFieldAnnotation(classDefinition);
		FileAliasMode alias = FileAliasMode.getByName(aliasName);
		for (AppTableColumn column : columnList) {
			String javaType = column.getJavaType();
			String javaPropertyName = column.getJavaPropertyName();
			String javaDefaultValue = column.getColumnDefault();
			String colDesc = column.getColumnDesc();
			String colComment = StringUtils.trim(column.getColumnComment());
			if (alias == FileAliasMode.Entity) {//实体
				AppClassFieldList field = new AppClassFieldList();
				field.setName(javaPropertyName);
				field.setDesc(colDesc);
				field.setComment(colComment);
				field.setType(javaType);
				field.setDefaultValue(javaDefaultValue);
				if (extendsClasses != null && !extendsClasses.isEmpty()) {
					for (AppClass extendsClass : extendsClasses) {
						if (extendsClass.isExistsField(javaPropertyName)) {
							field.setParentExistsField(true);//父类已经存在的字段
							break;
						}
					}
				}
				this.fillFieldAnnotation(field, annotation);
				fields.add(field);
				//需要添加扩展字段（增量字段）
				if(javaType.equalsIgnoreCase("Integer") || javaType.equalsIgnoreCase("Long") || javaType.equalsIgnoreCase("BigDecimal")){
					AppClassFieldList extField = new AppClassFieldList();
					extField.setName(javaPropertyName + incrementSuff);
					extField.setDesc("增加 " + colDesc);
					extField.setComment(colComment);
					extField.setType(javaType);
					this.fillFieldAnnotation(extField, annotation);
					extFields.add(extField);
				}
			} else if (alias == FileAliasMode.Query) {//查询对象
				boolean orCondMatch = column.isOrCondMatch();
				boolean startWithMatch = column.isStartWithMatch();
				boolean endWithMatch = column.isEndWithMatch();
				boolean startAndEndMatch = column.isStartAndEndMatch();
				boolean compareSize = column.isCompareSize();
				//添加查询字段
				if("Date".equalsIgnoreCase(javaType)){//日期类型处理，按string处理，可进行大小比较
					AppClassFieldList queryFieldStart = new AppClassFieldList();
					queryFieldStart.setName(javaPropertyName + startSuff);
					queryFieldStart.setDesc("开始 " + colDesc);
					queryFieldStart.setComment(colComment);
					queryFieldStart.setType("String");
					this.fillFieldAnnotation(queryFieldStart, annotation);
					AppClassFieldList queryFieldEnd = new AppClassFieldList();
					queryFieldEnd.setName(javaPropertyName + endSuff);
					queryFieldEnd.setDesc("结束 " + colDesc);
					queryFieldEnd.setComment(colComment);
					queryFieldEnd.setType("String");
					this.fillFieldAnnotation(queryFieldEnd, annotation);
					fields.add(queryFieldStart);
					fields.add(queryFieldEnd);
				} else if("Integer".equalsIgnoreCase(javaType)
						|| "Long".equalsIgnoreCase(javaType)
						|| "String".equalsIgnoreCase(javaType)
						|| "BigDecimal".equalsIgnoreCase(javaType)){
					if (orCondMatch) {
						AppClassFieldList queryFieldORList = new AppClassFieldList();
						queryFieldORList.setName(javaPropertyName + orListSuff);
						queryFieldORList.setDesc("或条件匹配 " + colDesc);
						queryFieldORList.setComment(colComment);
						queryFieldORList.setType("List");
						queryFieldORList.setTypeGenericClasses(new String[] {javaType});
						this.fillFieldAnnotation(queryFieldORList, annotation);
						fields.add(queryFieldORList);
					}
					if ((javaType.equalsIgnoreCase("String") && compareSize) || !javaType.equalsIgnoreCase("String")) {
						AppClassFieldList queryFieldStart = new AppClassFieldList();
						queryFieldStart.setName(javaPropertyName + startSuff);
						queryFieldStart.setDesc("开始 " + colDesc);
						queryFieldStart.setComment(colComment);
						queryFieldStart.setType(javaType);
						this.fillFieldAnnotation(queryFieldStart, annotation);
						AppClassFieldList queryFieldEnd = new AppClassFieldList();
						queryFieldEnd.setName(javaPropertyName + endSuff);
						queryFieldEnd.setDesc("结束 " + colDesc);
						queryFieldEnd.setComment(colComment);
						queryFieldEnd.setType(javaType);
						this.fillFieldAnnotation(queryFieldEnd, annotation);
						fields.add(queryFieldStart);
						fields.add(queryFieldEnd);
					}
					AppClassFieldList queryFieldList = new AppClassFieldList();
					queryFieldList.setName(javaPropertyName + listSuff);
					queryFieldList.setDesc(colDesc + "集合");
					queryFieldList.setComment(colComment);
					queryFieldList.setType("List");
					queryFieldList.setTypeGenericClasses(new String[] {javaType});
					this.fillFieldAnnotation(queryFieldList, annotation);
					fields.add(queryFieldList);
				}
				if (startWithMatch) {
					AppClassFieldList queryFieldStart = new AppClassFieldList();
					queryFieldStart.setName(javaPropertyName + startWithSuff);
					queryFieldStart.setDesc("前置匹配 " + colDesc);
					queryFieldStart.setComment(colComment);
					queryFieldStart.setType(javaType);
					this.fillFieldAnnotation(queryFieldStart, annotation);
					fields.add(queryFieldStart);
				}
				if (endWithMatch) {
					AppClassFieldList queryFieldEnd = new AppClassFieldList();
					queryFieldEnd.setName(javaPropertyName + endWithSuff);
					queryFieldEnd.setDesc("后置匹配 " + colDesc);
					queryFieldEnd.setComment(colComment);
					queryFieldEnd.setType(javaType);
					this.fillFieldAnnotation(queryFieldEnd, annotation);
					fields.add(queryFieldEnd);
	            }
				if (startAndEndMatch) {
					AppClassFieldList queryFieldMatch = new AppClassFieldList();
					queryFieldMatch.setName(javaPropertyName + matchSuff);
					queryFieldMatch.setDesc("前后匹配 " + colDesc);
					queryFieldMatch.setComment(colComment);
					queryFieldMatch.setType(javaType);
					this.fillFieldAnnotation(queryFieldMatch, annotation);
					fields.add(queryFieldMatch);
	            }
			} else if (alias == FileAliasMode.RequestDto) {
				if("Date".equalsIgnoreCase(javaType)){//日期类型处理，按string处理
					javaType = "String";
				}
				AppClassFieldList field = new AppClassFieldList();
				field.setName(javaPropertyName);
				field.setDesc(colDesc);
				field.setComment(colComment);
				field.setType(javaType);
				field.setDefaultValue(javaDefaultValue);
				this.fillFieldAnnotation(field, annotation);
				fields.add(field);
			} else if (alias == FileAliasMode.RequestPageDto) {
				if("Date".equalsIgnoreCase(javaType)){//日期类型处理，按string处理
					javaType = "String";
					AppClassFieldList queryFieldStart = new AppClassFieldList();
					queryFieldStart.setName(javaPropertyName + startSuff);
					queryFieldStart.setDesc("开始 " + colDesc);
					queryFieldStart.setComment(colComment);
					queryFieldStart.setType("String");
					this.fillFieldAnnotation(queryFieldStart, annotation);
					AppClassFieldList queryFieldEnd = new AppClassFieldList();
					queryFieldEnd.setName(javaPropertyName + endSuff);
					queryFieldEnd.setDesc("结束 " + colDesc);
					queryFieldEnd.setComment(colComment);
					queryFieldEnd.setType("String");
					this.fillFieldAnnotation(queryFieldEnd, annotation);
					fields.add(queryFieldStart);
					fields.add(queryFieldEnd);
					if (column.isQueryField()) {
						identifyFields.add(queryFieldStart);
						identifyFields.add(queryFieldEnd);
					}
				} else {
					AppClassFieldList field = new AppClassFieldList();
					field.setName(javaPropertyName);
					field.setDesc(colDesc);
					field.setComment(colComment);
					field.setType(javaType);
					field.setDefaultValue(javaDefaultValue);
					this.fillFieldAnnotation(field, annotation);
					fields.add(field);
					if (column.isQueryField()) {
						identifyFields.add(field);
					}
				}
			} else if (alias == FileAliasMode.ResponseDto) {
				if("Date".equalsIgnoreCase(javaType)){//日期类型处理，按string处理
					javaType = "String";
				}
				AppClassFieldList field = new AppClassFieldList();
				field.setName(javaPropertyName);
				field.setDesc(colDesc);
				field.setComment(colComment);
				field.setType(javaType);
				field.setDefaultValue(javaDefaultValue);
				this.fillFieldAnnotation(field, annotation);
				fields.add(field);
			}
		}
		if (alias == FileAliasMode.Query) {//查询对象，添加除字段外的其它字段
			AppClassFieldList queryFieldOrCondition = new AppClassFieldList();
			queryFieldOrCondition.setName("orConditionList");
			queryFieldOrCondition.setDesc("OR条件集合，列表项之间是OR，项内容之间是AND，如：(list[0].1 and list[0].2) or (list[1].3 and list[1].4)");
//			queryFieldOrCondition.setType("List<" + this.nameParam.getQueryName() + ">");
			queryFieldOrCondition.setType("List");
			queryFieldOrCondition.setTypeGenericClasses(new String[] {this.nameParam.getQueryName()});
			this.fillFieldAnnotation(queryFieldOrCondition, annotation);
			fields.add(queryFieldOrCondition);
			AppClassFieldList queryFieldAndCondition = new AppClassFieldList();
			queryFieldAndCondition.setName("andConditionList");
			queryFieldAndCondition.setDesc("AND条件集合，列表项之间是AND，项内容之间是OR，如：(list[0].1 or list[0].2) and (list[1].3 or list[1].4)");
//			queryFieldAndCondition.setType("List<" + this.nameParam.getQueryName() + ">");
			queryFieldAndCondition.setType("List");
			queryFieldAndCondition.setTypeGenericClasses(new String[] {this.nameParam.getQueryName()});
			this.fillFieldAnnotation(queryFieldAndCondition, annotation);
			fields.add(queryFieldAndCondition);
		}
		appClass.addFields(identifyFields.isEmpty() ? fields : identifyFields);
		appClass.addExtFields(extFields);
	}
	/**
	 * 填充字段的Annotation
	 * @param field
	 * @param annotation
	 */
	private void fillFieldAnnotation(AppClassFieldList field, AppAnnotation annotation) {
		if (annotation != null) {
			AppAnnotation fieldAnnotation = new AppAnnotation();
			fieldAnnotation.setName(annotation.getName());
			if (annotation.getValue() != null) {
				Object value = annotation.getValue();
				if (value instanceof AliasVO) {
					String content = ((AliasVO)value).getAliasName();
					content = content.replace("${fieldDesc}", field.getDesc());
					content = content.replace("${fieldName}", field.getName());
					content = content.replace("${fieldType}", field.getType());
					fieldAnnotation.setValue(content);
				} else {
					fieldAnnotation.setValue(value);
				}
			}
			field.setAnnotation(fieldAnnotation);
		}
	}
	
	private String replaceAliasVariable(String content) {
		if (StringUtils.isNotEmpty(content)) {
			content = content.replace("${requestMappingPath}", this.nameParam.getControllerMappingPath());
			content = content.replace("${serviceName}", StringUtils.firstToLowerCase(this.nameParam.getServiceName()));
			content = content.replace("${daoName}", StringUtils.firstToLowerCase(this.nameParam.getDaoName()));
			content = content.replace("${entityName}", StringUtils.firstToLowerCase(this.nameParam.getEntityName()));
			content = content.replace("${formName}", StringUtils.firstToLowerCase(this.nameParam.getContFormName()));
			content = content.replace("${formClassName}", this.nameParam.getContFormName());
			content = content.replace("${moduleDesc}", StringUtils.trim(this.table.getComment()));
			content = content.replace("${tableName}", StringUtils.trim(this.table.getTableName()));
		}
		return content;
	}
	/**
	 * 创建toString方法
	 * @param appClass
	 * @return
	 */
	private AppClassMethod createToStringMethod(AppClass appClass) {
		CustomStringBuilder toStringBody = new CustomStringBuilder("");
		toStringBody.append("StringBuilder sb = new StringBuilder(\"\");");
		if (appClass != null && appClass.getFields() != null) {
			boolean toStringBodyHasFields = false;
			for (AppClassField classField : appClass.getFields()) {
				if (classField instanceof AppClassFieldSerial) {
					continue;
				} else if (classField instanceof AppClassFieldList) {
					AppClassFieldList field = (AppClassFieldList)classField;
					String javaPropertyName = field.getName();
					if(!toStringBodyHasFields){
						toStringBody.newLine().appendTab(2).append("sb.append(\"" + javaPropertyName + ":\").append(" + TableUtil.getBeanGetMethod(javaPropertyName) + "())");
						toStringBodyHasFields = true;
					}else{
						toStringBody.newLine().appendTab(2).append("  .append(\"," + javaPropertyName + ":\").append(" + TableUtil.getBeanGetMethod(javaPropertyName) + "())");
					}
				}
			}
		}
		toStringBody.append(";").newLine().appendTab(2).append("return sb.toString();");
		AppClassMethodList toStringMethod = new AppClassMethodList();
		toStringMethod.setName("toString");
		toStringMethod.setReturnType("String");
		toStringMethod.setModifier("public");
		toStringMethod.setBody(toStringBody.toString());
		return toStringMethod;
	}
	/**
	 * 创建initAttrValue(初始化属性值)方法
	 * @param appClass
	 * @return
	 */
	private AppClassMethod createInitAttrValueMethod(AppClass appClass) {
		CustomStringBuilder initAttrValueBody = new CustomStringBuilder("");
		if (appClass != null && appClass.getFields() != null) {
			for (AppClassField classField : appClass.getFields()) {
				if (classField instanceof AppClassFieldSerial) {
					continue;
				} else if (classField instanceof AppClassFieldList) {
					AppClassFieldList field = (AppClassFieldList)classField;
					String javaType = field.getType();
					String javaPropertyName = field.getName();
					String javaDefaultValue = field.getDefaultValue() != null ? field.getDefaultValue().toString() : "";
					if (!javaPropertyName.equalsIgnoreCase("id")) {
						if (initAttrValueBody.length() > 0) {
							initAttrValueBody.newLine().appendTab(2);
						}
						if (StringUtils.isNotEmpty(javaDefaultValue)) {
							if (javaType.equalsIgnoreCase("BigDecimal")) {
								initAttrValueBody.append("this." + javaPropertyName + " = new BigDecimal(" + StringUtils.trim(javaDefaultValue) + ");");
							} else if (javaType.equalsIgnoreCase("Long")) {
								initAttrValueBody.append("this." + javaPropertyName + " = " + StringUtils.trim(javaDefaultValue) + "L;");
							} else if (javaType.equalsIgnoreCase("String")) {
								initAttrValueBody.append("this." + javaPropertyName + " = \"" + StringUtils.trim(javaDefaultValue) + "\";");
							} else {
								initAttrValueBody.append("this." + javaPropertyName + " = " + StringUtils.trim(javaDefaultValue) + ";");
							}
						} else {
							initAttrValueBody.append("this." + javaPropertyName + " = null;");
						}
					}
				}
			}
		}
		AppClassMethodList initAttrValueMethod = new AppClassMethodList();
		initAttrValueMethod.setName("initAttrValue");
		initAttrValueMethod.setReturnType("void");
		initAttrValueMethod.setModifier("public");
		initAttrValueMethod.setBody(initAttrValueBody.toString());
		return initAttrValueMethod;
	}
	
	public boolean isFilterImport(String className) {
		if (StringUtils.isEmpty(className)) {
			return false;
		}
		return this.filterImportClasses.contains(className);
	}
	
	public String getQualifiedClassName(String className) {
		if (StringUtils.isEmpty(className)) {
			return null;
		}
		String qualifiedClassName = this.qualifiedClassNameMap.get(className);
		if (StringUtils.isEmpty(qualifiedClassName)) {
			qualifiedClassName =  this.frameworkProvider.getQualifiedClassName(className);
		}
		return qualifiedClassName;
	}
	
	public String getFrameworkPackage() {
		return this.frameworkProvider.getFrameworkPackage();
	}

	public Map<String, AppClass> getClassMap() {
		return classMap;
	}

	public void setClassMap(Map<String, AppClass> classMap) {
		this.classMap = classMap;
	}

	public List<AppPage> getPageList() {
		return pageList;
	}

	public void setPageList(List<AppPage> pageList) {
		this.pageList = pageList;
	}

	public AppConfig getConfig() {
		return config;
	}

	public void setConfig(AppConfig config) {
		this.config = config;
	}

	public AppTable getTable() {
		return table;
	}

	public void setTable(AppTable table) {
		this.table = table;
	}

	public AppModule getAppModule() {
		return appModule;
	}

	public void setAppModule(AppModule appModule) {
		this.appModule = appModule;
	}

	public AppTableNameParam getNameParam() {
		return nameParam;
	}

	public void setNameParam(AppTableNameParam nameParam) {
		this.nameParam = nameParam;
	}
	
}
