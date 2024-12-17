package com.gm.easecode.frame.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gm.easecode.common.util.CustomStringBuilder;
import com.gm.easecode.common.vo.AliasConstants;
import com.gm.easecode.common.vo.AliasVO;
import com.gm.easecode.common.vo.AppAnnotation;
import com.gm.easecode.common.vo.AppClass;
import com.gm.easecode.common.vo.AppClassConstructor;
import com.gm.easecode.common.vo.AppClassDefinition;
import com.gm.easecode.common.vo.AppClassMethod;
import com.gm.easecode.common.vo.AppClassMethodList;
import com.gm.easecode.common.vo.AppClassMethodParam;
import com.gm.easecode.common.vo.ControllerClassStyleMode;
import com.gm.easecode.common.vo.FileAliasMode;
import com.gm.easecode.frame.AbstractFrameworkProvider;
import com.gm.easecode.frame.FrameworkProviderFactory;
import com.gm.easecode.frame.common.Dependey;
import com.gm.easecode.frame.common.FrameDependey;
import com.gm.easecode.frame.common.Plugin;

public class JavaEaseFrameProvider extends AbstractFrameworkProvider {

	/** 框架包路径 */
	private String frameworkPackage = "";
	/** 框架包common路径 */
	private String frameworkCommonPackage = "";
	/** 框架包core路径 */
	private String frameworkCorePackage = "";
	/** 框架包boot路径 */
	private String frameworkBootPackage = "";
	/** 框架包继承类路径 */
	private String frameworkExtendsPackage = "";
	/** 框架包继承Model类路径 */
	private String frameworkExtendsModelPackage = "";
	/** 框架包继承Dao类路径 */
	private String frameworkExtendsDaoPackage = "";
	/** 框架包继承DaoImpl类路径 */
	private String frameworkExtendsDaoImplPackage = "";
	/** 框架包继承Service类路径 */
	private String frameworkExtendsServicePackage = "";
	/** 框架包继承ServiceImpl类路径 */
	private String frameworkExtendsServiceImplPackage = "";
	/** 框架包继承Web类路径 */
	private String frameworkExtendsWebPackage = "";
	/** 框架包继承WebDto类路径 */
	private String frameworkExtendsWebDtoPackage = "";
	
	private FrameDependey frameDependey;
	
	public JavaEaseFrameProvider() {
		super(FrameworkProviderFactory.FrameworkProviderMode.JavaEaseFrame.getName(), FrameworkProviderFactory.FrameworkProviderMode.JavaEaseFrame.getVersion());
		this.initPackage();
		this.initQualifiedClassName();
		this.initBaseClass();
		this.initFrameDependey();
	}
	public void initPackage() {
		this.frameworkPackage = "com.gm.javaeaseframe";
		this.frameworkCommonPackage = this.frameworkPackage + ".common";
		this.frameworkCorePackage = this.frameworkPackage + ".core";
		this.frameworkBootPackage = this.frameworkCorePackage + ".boot";
		this.frameworkExtendsPackage = this.frameworkCorePackage + ".context";
		this.frameworkExtendsModelPackage = this.frameworkExtendsPackage + ".model";
		this.frameworkExtendsDaoPackage = this.frameworkExtendsPackage + ".dao";
		this.frameworkExtendsDaoImplPackage = this.frameworkExtendsPackage + ".dao.mybatis";
		this.frameworkExtendsServicePackage = this.frameworkExtendsPackage + ".service";
		this.frameworkExtendsServiceImplPackage = this.frameworkExtendsPackage + ".service.impl";
		this.frameworkExtendsWebPackage = this.frameworkExtendsPackage + ".web";
		this.frameworkExtendsWebDtoPackage = this.frameworkExtendsPackage + ".web.dto";
	}
	public void initQualifiedClassName() {
		//添加框架常用全量类路径
		addQualifiedClassName("Context", this.frameworkExtendsModelPackage + ".Context");
		addQualifiedClassName("PageInfo", this.frameworkExtendsModelPackage + ".PageInfo");
		addQualifiedClassName("GlobalException", this.frameworkPackage + ".web.spring.boot.autoconfigure.annotation.GlobalException");
		addQualifiedClassName("BusinessException", this.frameworkCommonPackage + ".exception.BusinessException");
		addQualifiedClassName("IUser", this.frameworkExtendsServicePackage + ".IUser");
		addQualifiedClassName("CustomApi", this.frameworkCommonPackage + ".annotation.CustomApi");
		addQualifiedClassName("CustomApiModel", this.frameworkCommonPackage + ".annotation.CustomApiModel");
		addQualifiedClassName("CustomApiModelProperty", this.frameworkCommonPackage + ".annotation.CustomApiModelProperty");
	}
	
	public void initBaseClass() {
		this.initBaseEntityClass("BaseEntity", this.frameworkExtendsModelPackage, "PK", "Default");
		this.initBaseEntityClass("BaseEntityInt", this.frameworkExtendsModelPackage, "Integer");
		this.initBaseEntityClass("BaseEntityLong", this.frameworkExtendsModelPackage, "Long");
		this.initBaseEntityClass("BaseEntityStr", this.frameworkExtendsModelPackage, "String");
		this.initBaseTreeEntityClass("BaseTreeEntityLong", this.frameworkExtendsModelPackage, "Long", "Tree-Long");
		
		initBaseControllerClass("BaseCRUDController", this.frameworkExtendsWebPackage, ControllerClassStyleMode.SPRING_MVC.name(), ControllerClassStyleMode.SPRING_MVC);
		initBaseControllerClass("BaseCRUDMappingController", this.frameworkExtendsWebPackage, ControllerClassStyleMode.SPRING_MVC_MAPPING.name(), ControllerClassStyleMode.SPRING_MVC_MAPPING);
		initBaseControllerClass("BaseCRUDJsonController", this.frameworkExtendsWebPackage, ControllerClassStyleMode.JSON.name(), ControllerClassStyleMode.JSON);
		initBaseControllerClass("BaseCRUDJsonMappingController", this.frameworkExtendsWebPackage, ControllerClassStyleMode.JSON_MAPPING.name(), ControllerClassStyleMode.JSON_MAPPING);
		AppClass baseCRUDDtoControllerClass = initBaseControllerClass("BaseCRUDDtoController", this.frameworkExtendsWebPackage, ControllerClassStyleMode.DTO.name(), ControllerClassStyleMode.DTO);
		AppClass baseCRUDDtoMappingControllerClass = initBaseControllerClass("BaseCRUDDtoMappingController", this.frameworkExtendsWebPackage, ControllerClassStyleMode.DTO_MAPPING.name(), ControllerClassStyleMode.DTO_MAPPING);
		initBaseControllerClassMethod(baseCRUDDtoControllerClass);
		initBaseControllerClassMethod(baseCRUDDtoMappingControllerClass);
		
		initBaseFormClass("BaseCRUDForm", this.frameworkExtendsWebPackage, "PK", "Default");
		initBaseFormClass("BaseCRUDFormInt", this.frameworkExtendsWebPackage, "Integer");
		initBaseFormClass("BaseCRUDFormLong", this.frameworkExtendsWebPackage, "Long");
		initBaseFormClass("BaseCRUDFormStr", this.frameworkExtendsWebPackage, "String");
		
		initBaseDtoClass(FileAliasMode.RequestDto.name(), "BaseRequestDto", this.frameworkExtendsWebDtoPackage, FileAliasMode.RequestDto.name());
		initBaseDtoClass(FileAliasMode.RequestPageDto.name(), "BaseRequestPageDto", this.frameworkExtendsWebDtoPackage, FileAliasMode.RequestPageDto.name());
		initBaseDtoClass(FileAliasMode.ResponseDto.name(), "BaseResponseDto", this.frameworkExtendsWebDtoPackage, FileAliasMode.ResponseDto.name());
		
		initBaseDaoClass("ICRUDDao", this.frameworkExtendsDaoPackage, "Default");
		initBaseDaoImplClass("BaseCRUDDaoMybatis", this.frameworkExtendsDaoImplPackage, "Default");
		
		initBaseServiceClass("ICRUDService", this.frameworkExtendsServicePackage, "Default");
		initBaseServiceClass("ICRUDCacheService", this.frameworkExtendsServicePackage, "Cache");
		initBaseServiceImplClass("AbstractCRUDServiceImpl", this.frameworkExtendsServiceImplPackage, "Default");
		initBaseServiceImplClass("AbstractCRUDCacheServiceImpl", this.frameworkExtendsServiceImplPackage, "Cache");
		
		initBaseBootstrapClass("BaseApplication", this.frameworkBootPackage, "Default");
	}
	
	private void initBaseControllerClassMethod(AppClass appClass) {
		List<AppClassMethod> methods = new ArrayList<>();
		List<AppClassMethodParam> doListBeforeMethodParams = new ArrayList<>();
		doListBeforeMethodParams.add(new AppClassMethodParam("request", "HttpServletRequest"));
		doListBeforeMethodParams.add(new AppClassMethodParam("response", "HttpServletResponse"));
		doListBeforeMethodParams.add(new AppClassMethodParam("data", AliasConstants.generalAliasVariable(AliasConstants.CONTROLLER_DTO_REQ_PAGE_NAME)));
		doListBeforeMethodParams.add(new AppClassMethodParam("entity", AliasConstants.generalAliasVariable(AliasConstants.MODULE_ENTITY_NAME)));
		doListBeforeMethodParams.add(new AppClassMethodParam("model", "Map<String, Object>"));
		doListBeforeMethodParams.add(new AppClassMethodParam("context", "Context"));
		AppClassMethodList doListBeforeMethod = new AppClassMethodList("protected", "doListBefore", "void", "查询前预处理", doListBeforeMethodParams);
		String[] throwsType = new String[] {"BusinessException"};
		doListBeforeMethod.setThrowsType(throwsType);
		methods.add(doListBeforeMethod);
		appClass.setMethods(methods);
	}
	
	private void initFrameDependey() {
		Dependey dependey = new Dependey("com.gm.framework", "javaeaseframe-parent", "1.0.0-SNAPSHOT");
		Map<String, Dependey> dependencyMap = new LinkedHashMap<>();
		dependencyMap.put("guava", new Dependey("com.google.guava", "guava"));
		dependencyMap.put("fastjson", new Dependey("com.alibaba", "fastjson"));
		dependencyMap.put("common", new Dependey("com.gm.framework", "javaeaseframe-common"));
		dependencyMap.put("core", new Dependey("com.gm.framework", "javaeaseframe-core"));
		dependencyMap.put("knife4j", new Dependey("com.gm.framework", "javaeaseframe-knife4j-spring-boot-starter"));
		dependencyMap.put("interceptor", new Dependey("com.gm.framework", "javaeaseframe-interceptor-spring-boot-starter"));
		dependencyMap.put("mybatis", new Dependey("com.gm.framework", "javaeaseframe-mybatis-spring-boot-starter"));
		dependencyMap.put("transaction", new Dependey("com.gm.framework", "javaeaseframe-transaction-spring-boot-starter"));
		dependencyMap.put("web", new Dependey("com.gm.framework", "javaeaseframe-web-spring-boot-starter"));
		dependencyMap.put("openfeign", new Dependey("com.gm.framework", "javaeaseframe-openfeign-spring-boot-starter"));
		dependencyMap.put("xxljob", new Dependey("com.gm.framework", "javaeaseframe-xxljob-spring-boot-starter"));
		Map<String, Plugin> pluginMap = new HashMap<>();
		pluginMap.put("compiler", new Plugin("org.apache.maven.plugins", "maven-compiler-plugin"));
		Map<String, String> aliasVariableMap = new HashMap<>();
		aliasVariableMap.put(AliasConstants.generalAliasVariable(AliasConstants.EXCEPTION), "BusinessException");
		this.frameDependey = new FrameDependey(dependey, dependencyMap, pluginMap, aliasVariableMap);
	}
	
	@Override
	public String getFrameworkPackage() {
		return this.frameworkPackage;
	}
	@Override
	public FrameDependey getFrameDependey() {
		return this.frameDependey;
	}
	/**
	 * 获取指定别名对应的继承对象
	 * @param aliasName
	 * @param keyType
	 * @return
	 */
	public AppClass getClassExtendsClass(AppClassDefinition classDefinition) {
		String aliasName = classDefinition.getAliasName();
		String pkType = classDefinition.getPkType();
		boolean isTree = classDefinition.isTree();
		String baseClassKey = pkType;
		FileAliasMode alias = FileAliasMode.getByName(aliasName);
		if (alias == null) {
			return null;
		}
		Map<String, AppClass> classMap = this.baseClassMap.get(aliasName);
		AppClass appClass = null;
		switch (alias) {
		case EntityKey:
			appClass = new AppClass(baseClassKey);
			break;
		case Entity:
			if (isTree) {
				appClass = this.getExtClass(classMap, "Tree-" + pkType);
			}
			if (appClass == null) {
				appClass = this.getExtClass(classMap, baseClassKey);
			}
			break;
		case Query:
			appClass = AppClass.createVariableInstance(FileAliasMode.Entity.name());
			break;
		case Form:
			appClass = this.getExtClass(classMap, baseClassKey);
			break;
		case RequestDto:
		case RequestPageDto:
		case ResponseDto:
			appClass = this.getExtClass(classMap, aliasName);
			break;
		case Dao:
			appClass = this.getExtClass(classMap, baseClassKey);
			break;
		case DaoImpl:
			appClass = this.getExtClass(classMap, baseClassKey);
			break;
		case Service:
			appClass = this.getExtClass(classMap, baseClassKey);
			break;
		case ServiceImpl:
			appClass = this.getExtClass(classMap, baseClassKey);
			break;
		case Controller:
			ControllerClassStyleMode classStyle = classDefinition.getControllerClassStyle();
			if (classStyle == null) {
				classStyle = ControllerClassStyleMode.SPRING_MVC;
			}
			appClass = this.getExtClass(classMap, classStyle.name());
			break;
		case Bootstrap:
			appClass = this.getExtClass(classMap, baseClassKey);
			break;
		default:
			break;
		}
		return appClass;
	}
	
	@Override
	public List<AppClass> getClassImplementsClass(AppClassDefinition classDefinition) {
		return null;
	}
	/**
	 * 获取指定别名对应的Annotation
	 * @param aliasName
	 * @return
	 */
	public List<AppAnnotation> getClassAnnotation(AppClassDefinition classDefinition) {
		List<AppAnnotation> list = new ArrayList<>();
		FileAliasMode alias = FileAliasMode.getByName(classDefinition.getAliasName());
		if (alias == null) {
			return list;
		}
		String moduleDesc = AliasConstants.generalAliasVariable(AliasConstants.MODULE_DESC);
		switch (alias) {
		case RequestDto:
		case RequestPageDto:
		case ResponseDto:
			String desc = "";
			switch (alias) {
				case RequestDto:
					desc = "请求对象";
					break;
				case RequestPageDto:
					desc = "分页请求对象";
					break;
				case ResponseDto:
					desc = "响应对象";
					break;
				default:
					break;
			}
			list.add(new AppAnnotation("CustomApiModel", new AliasVO("\"" + moduleDesc + desc + "\"")));
			break;
		case DaoImpl:
			list.add(new AppAnnotation("Repository", new AliasVO("\"" + AliasConstants.generalAliasVariable(AliasConstants.DAO_ANNOTATION_NAME) + "\"")));
			break;
		case ServiceImpl:
			list.add(new AppAnnotation("Service", new AliasVO("\"" + AliasConstants.generalAliasVariable(AliasConstants.SERVICE_ANNOTATION_NAME) + "\"")));
			break;
		case Controller:
			list.add(new AppAnnotation("RestController"));
			list.add(new AppAnnotation("RequestMapping", new AliasVO("\"" + AliasConstants.generalAliasVariable(AliasConstants.CONTROLLER_REQUEST_MAPPING_PATH) + "\"")));
			list.add(new AppAnnotation("GlobalException"));
			list.add(new AppAnnotation("CustomApi", new AliasVO("value = \"" + moduleDesc + "\", tags = {\"" + moduleDesc + "\"}")));
			break;
		case Bootstrap:
			list.add(new AppAnnotation("SpringBootApplication"));
			break;
		default:
			break;
		}
		return list;
	}
	/**
	 * 获取指定别名对应的Constructor
	 * @param aliasName
	 * @return
	 */
	public List<AppClassConstructor> getClassConstructor(AppClassDefinition classDefinition) {
		List<AppClassConstructor> list = new ArrayList<>();
		FileAliasMode alias = FileAliasMode.getByName(classDefinition.getAliasName());
		if (alias == null) {
			return list;
		}
		switch (alias) {
			case Controller:
				AppClassConstructor controllerConstructor = new AppClassConstructor();
				controllerConstructor.setModifiers("public");
				CustomStringBuilder controllerConstructorBody = new CustomStringBuilder("");
				controllerConstructorBody.appendTab(2).append("super.setModuleDesc(\"" + AliasConstants.generalAliasVariable(AliasConstants.MODULE_DESC) + "\");");
				controllerConstructor.setBody(new AliasVO(controllerConstructorBody.toString()));
				list.add(controllerConstructor);
				break;
			default:
				break;
		}
		return list;
	}
	/**
	 * 获取类属性的Annotation
	 * @param aliasName
	 * @return
	 */
	public AppAnnotation getClassFieldAnnotation(AppClassDefinition classDefinition) {
		FileAliasMode alias = FileAliasMode.getByName(classDefinition.getAliasName());
		if (alias == null) {
			return null;
		}
		AppAnnotation annotation = null;
		switch (alias) {
		case RequestDto:
		case RequestPageDto:
		case ResponseDto:
			annotation = new AppAnnotation();
			annotation.setName("CustomApiModelProperty");
			annotation.setValue(new AliasVO("\"" + AliasConstants.generalAliasVariable(AliasConstants.MODULE_ENTITY_FIELD_DESC) + "\""));
			break;
		default:
			break;
		}
		return annotation;
	}
}
