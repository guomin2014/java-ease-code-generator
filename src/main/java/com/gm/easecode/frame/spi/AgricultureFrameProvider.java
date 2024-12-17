package com.gm.easecode.frame.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gm.easecode.common.util.CustomStringBuilder;
import com.gm.easecode.common.util.ObjectUtil;
import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.vo.AliasConstants;
import com.gm.easecode.common.vo.AliasVO;
import com.gm.easecode.common.vo.AppAnnotation;
import com.gm.easecode.common.vo.AppClass;
import com.gm.easecode.common.vo.AppClassConstructor;
import com.gm.easecode.common.vo.AppClassDefinition;
import com.gm.easecode.common.vo.AppClassMethod;
import com.gm.easecode.common.vo.AppClassMethodList;
import com.gm.easecode.common.vo.ControllerClassStyleMode;
import com.gm.easecode.common.vo.FileAliasMode;
import com.gm.easecode.frame.AbstractFrameworkProvider;
import com.gm.easecode.frame.FrameworkProviderFactory;
import com.gm.easecode.frame.common.FrameDependey;

public class AgricultureFrameProvider extends AbstractFrameworkProvider {

	/** 框架包路径 */
	private String frameworkPackage = "";
	/** 框架包Util路径 */
	private String frameworkUtilPackage = "";
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
	
	public AgricultureFrameProvider() {
		super(FrameworkProviderFactory.FrameworkProviderMode.AgricultureFrame.getName(), FrameworkProviderFactory.FrameworkProviderMode.AgricultureFrame.getVersion());
		this.initPackage();
		this.initQualifiedClassName();
		this.initBaseClass();
	}
	
	public void initPackage() {
		this.frameworkPackage = "com.wisdom.agriculture";
		this.frameworkBootPackage = this.frameworkPackage + ".boot";
		this.frameworkUtilPackage = this.frameworkPackage + ".util";
		this.frameworkExtendsPackage = this.frameworkPackage + ".framework";
		this.frameworkExtendsModelPackage = this.frameworkExtendsPackage + ".model";
		this.frameworkExtendsDaoPackage = this.frameworkExtendsPackage + ".dao";
		this.frameworkExtendsDaoImplPackage = this.frameworkExtendsPackage + ".dao.ibatis";
		this.frameworkExtendsServicePackage = this.frameworkExtendsPackage + ".service";
		this.frameworkExtendsServiceImplPackage = this.frameworkExtendsPackage + ".service.impl";
		this.frameworkExtendsWebPackage = this.frameworkExtendsPackage + ".web";
		this.frameworkExtendsWebDtoPackage = this.frameworkExtendsPackage + ".web.dto";
	}
	
	public void initQualifiedClassName() {
		addQualifiedClassName("AppException", this.frameworkExtendsPackage + ".exception.AppException");
		addQualifiedClassName("Context", this.frameworkExtendsModelPackage + ".Context");
		addQualifiedClassName("IUser", this.frameworkExtendsServicePackage + ".IUser");
		addQualifiedClassName("TableStrategy", this.frameworkExtendsModelPackage + ".TableStrategy");
		addQualifiedClassName("StringUtils", this.frameworkUtilPackage + ".StringUtils");
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
		initBaseControllerClass("BaseCRUDDtoController", this.frameworkExtendsWebPackage, ControllerClassStyleMode.DTO.name(), ControllerClassStyleMode.DTO);
		initBaseControllerClass("BaseCRUDDtoMappingController", this.frameworkExtendsWebPackage, ControllerClassStyleMode.DTO_MAPPING.name(), ControllerClassStyleMode.DTO_MAPPING);
		
		initBaseFormClass("BaseCRUDForm", this.frameworkExtendsWebPackage, "PK", "Default");
		initBaseFormClass("BaseCRUDFormInt", this.frameworkExtendsWebPackage, "Integer");
		initBaseFormClass("BaseCRUDFormLong", this.frameworkExtendsWebPackage, "Long");
		initBaseFormClass("BaseCRUDFormStr", this.frameworkExtendsWebPackage, "String");
		
		initBaseDtoClass(FileAliasMode.RequestDto.name(), "BaseRequestDto", this.frameworkExtendsWebDtoPackage, FileAliasMode.RequestDto.name());
		initBaseDtoClass(FileAliasMode.RequestPageDto.name(), "BaseRequestPageDto", this.frameworkExtendsWebDtoPackage, FileAliasMode.RequestPageDto.name());
		initBaseDtoClass(FileAliasMode.ResponseDto.name(), "BaseResponseDto", this.frameworkExtendsWebDtoPackage, FileAliasMode.ResponseDto.name());
		
		initBaseDaoClass("ICRUDDao", this.frameworkExtendsDaoPackage, "Default");
		initBaseDaoImplClass("BaseCRUDDaoMybatis", this.frameworkExtendsDaoImplPackage, "Default");
		initBaseDaoClass("ICRUDSubmeterDao", this.frameworkExtendsDaoPackage, "Submeter");
		initBaseSubmeterDaoClass("SubmeterDaoImpl", this.frameworkExtendsDaoImplPackage, "Submeter");
		
		initBaseServiceClass("ICRUDService", this.frameworkExtendsServicePackage, "Default");
		initBaseServiceClass("ICRUDCacheService", this.frameworkExtendsServicePackage, "Cache");
		initBaseServiceImplClass("AbstractCRUDServiceImpl", this.frameworkExtendsServiceImplPackage, "Default");
		initBaseServiceImplClass("AbstractCRUDCacheServiceImpl", this.frameworkExtendsServiceImplPackage, "Cache");
		
		initBaseBootstrapClass("BaseApplication", this.frameworkBootPackage, "Default");
	}
	
	private void initBaseSubmeterDaoClass(String className, String packageName, String pkType) {
		AppClass submeterDaoImplClass = initBaseDaoImplClass(className, packageName, pkType);
		List<AppClassMethod> abstractMethods = new ArrayList<>();
		AppClassMethodList getTableStrategyMethod = new AppClassMethodList("protected", "getTableStrategy", "TableStrategy", "获取分表策略");
		getTableStrategyMethod.setBody(new AliasVO("return new TableStrategy(\"" + AliasConstants.generalAliasVariable(AliasConstants.MODULE_TABLE_NAME) + "\", " 
		+ AliasConstants.generalAliasVariable(AliasConstants.MODULE_TABLE_STRATEGY) + ");"));
		abstractMethods.add(getTableStrategyMethod);
		AppClassMethodList getTableParamMethod = new AppClassMethodList("protected", "getTableParam", "TableParam", "根据实体和上下文获取分表表名");
		getTableParamMethod.setBody("return new TableParam();");
		abstractMethods.add(getTableParamMethod);
		submeterDaoImplClass.setAbstractMethods(abstractMethods);
	}
	@Override
	public String getFrameworkPackage() {
		return this.frameworkPackage;
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
		boolean isSubmeter = classDefinition.isSubmeter();
		int submeterTableStrategy = classDefinition.getSubmeterTableStrategy();
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
				appClass = this.getExtClass(classMap, "Tree-" + baseClassKey);
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
			if (isSubmeter) {
				baseClassKey = "Submeter";
			}
			appClass = this.getExtClass(classMap, baseClassKey);
			if (appClass != null && isSubmeter) {
				try {
					AppClass extendsClass = ObjectUtil.clone(appClass);
					List<AppClassMethod> abstractMethods = extendsClass.getAbstractMethods();
					if (abstractMethods != null) {
						for (AppClassMethod method : abstractMethods) {
							if (method instanceof AppClassMethodList) {
								AppClassMethodList newMethod = (AppClassMethodList)method;
								if (newMethod.getBody() instanceof AliasVO) {
									String aName = ((AliasVO)newMethod.getBody()).getAliasName();
									if (StringUtils.isNotEmpty(aName)) {
										String tableStrategy = this.getSubmeterTableStrategyEnumName(submeterTableStrategy);
										aName = aName.replace(AliasConstants.generalAliasVariable(AliasConstants.MODULE_TABLE_STRATEGY), StringUtils.trim(tableStrategy));
										newMethod.setBody(aName);
									}
								}
							}
						}
					}
					appClass = extendsClass;
				} catch (Exception e) {}
			}
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
		switch (alias) {
		case DaoImpl:
			list.add(new AppAnnotation("Repository", new AliasVO("\"" + AliasConstants.generalAliasVariable(AliasConstants.DAO_ANNOTATION_NAME) + "\"")));
			break;
		case ServiceImpl:
			list.add(new AppAnnotation("Service", new AliasVO("\"" + AliasConstants.generalAliasVariable(AliasConstants.SERVICE_ANNOTATION_NAME) + "\"")));
			break;
		case Controller:
			list.add(new AppAnnotation("RestController"));
			list.add(new AppAnnotation("RequestMapping", new AliasVO("\"" + AliasConstants.generalAliasVariable(AliasConstants.CONTROLLER_REQUEST_MAPPING_PATH) + "\"")));
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
				controllerConstructorBody.appendTab(2).append("super.setFormClass(" + AliasConstants.generalAliasVariable(AliasConstants.CONTROLLER_FORM_NAME) + ".class);").newLine();
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
		return null;
	}
	
	/**
	 * 获取分表策略枚举名称
	 * @param keyType
	 * @param submeterTableStrategy
	 * @return
	 */
	public String getSubmeterTableStrategyEnumName(int submeterTableStrategy) {
		String tableStrategy = null;
		switch (submeterTableStrategy) {
		    case 1:
		        tableStrategy = "TableStrategy.STRATEGY_BY_DAY";
		        break;
		    case 2:
		        tableStrategy = "TableStrategy.STRATEGY_BY_WEEK";
		        break;
		    case 3:
		        tableStrategy = "TableStrategy.STRATEGY_BY_MONTH";
		        break;
		    case 4:
		        tableStrategy = "TableStrategy.STRATEGY_BY_MOD";
		        break;
		    case 5:
		        tableStrategy = "TableStrategy.STRATEGY_BY_DAY_AND_MOD";
		        break;
		    case 6:
		        tableStrategy = "TableStrategy.STRATEGY_BY_WEEK_AND_MOD";
		        break;
		    case 7:
		        tableStrategy = "TableStrategy.STRATEGY_BY_MONTH_AND_MOD";
		        break;
		}
		return tableStrategy;
	}

	@Override
	public FrameDependey getFrameDependey() {
		// TODO Auto-generated method stub
		return null;
	}
}
