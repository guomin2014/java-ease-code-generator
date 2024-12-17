package com.gm.easecode.common.vo;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.gm.easecode.common.util.ObjectUtil;
import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.frame.FrameworkProvider;

public class AppClassBuilder {

	private AppClassHandler handler;
	private FrameworkProvider frameworkProvider;
	/** 类名 */
	private String className;
	/** 文件路径 */
	private String filePath;
	/** 包名 */
	private String packageName;
	/** 类类型，class/interface */
	private String classType = ClassType.Class.getValue();
	/** 类的别名 */
	private String aliasName;
	/** 类简单描述信息 */
	private String desc;
	/** 主键类型 */
	private String pkType;
	/** controller类代码风格 */
	private ControllerClassStyleMode controllerClassStyle;
	/** 是否是树形结构 */
	private boolean isTree;
	/** 是否分表 */
	private boolean isSubmeter;
	/** 分表策略，0：无，1：按日，2：按周，3：按月，4：按余数，5：按日分余数，6：按周分余数，7：按月分余数，9：其它规则 */
	private int submeterTableStrategy = 0;
	
	public AppClassBuilder(AppClassHandler handler, FrameworkProvider frameworkProvider) {
		this.handler = handler;
		this.frameworkProvider = frameworkProvider;
	}

	public AppClassBuilder setClassName(String className) {
		this.className = className;
		return this;
	}

	public AppClassBuilder setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	public AppClassBuilder setPackageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public AppClassBuilder setClassType(String classType) {
		this.classType = classType;
		return this;
	}

	public AppClassBuilder setAliasName(String aliasName) {
		this.aliasName = aliasName;
		return this;
	}

	public AppClassBuilder setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public AppClassBuilder setPkType(String pkType) {
		this.pkType = pkType;
		return this;
	}

	public AppClassBuilder setControllerClassStyle(ControllerClassStyleMode controllerClassStyle) {
		this.controllerClassStyle = controllerClassStyle;
		return this;
	}

	public AppClassBuilder setTree(boolean isTree) {
		this.isTree = isTree;
		return this;
	}

	public AppClassBuilder setSubmeter(boolean isSubmeter) {
		this.isSubmeter = isSubmeter;
		return this;
	}

	public AppClassBuilder setSubmeterTableStrategy(int submeterTableStrategy) {
		this.submeterTableStrategy = submeterTableStrategy;
		return this;
	}

	public AppClass build() {
		AppClass appClass = new AppClass(classType, className, packageName, filePath, aliasName, desc);
		AppContext.addQualifiedClassName(className, (StringUtils.isNotEmpty(packageName) ? packageName + "." : "") + className);
		AppClassDefinition classDefinition = new AppClassDefinition(aliasName, pkType, controllerClassStyle, isTree, isSubmeter, submeterTableStrategy);
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
						newAnno.setValue(handler.replaceAliasVariable(aName));
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
						cons.setBody(handler.replaceAliasVariable(body));
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
				baseClass = this.handler.getAppClassByVariable(baseClass.getClassName());
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
							AppClass realClass = this.handler.getAppClassByVariable(genericClass.getClassName());
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
									method.setBody(handler.replaceAliasVariable(aName));
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
}
