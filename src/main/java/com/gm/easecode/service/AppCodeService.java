package com.gm.easecode.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gm.easecode.common.AppException;
import com.gm.easecode.common.util.ClassFileUtil;
import com.gm.easecode.common.util.FileUtil;
import com.gm.easecode.common.util.IbatisXmlUtil;
import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.util.TableUtil;
import com.gm.easecode.common.util.YmlUtil;
import com.gm.easecode.common.vo.AppClass;
import com.gm.easecode.common.vo.AppClassBuilder;
import com.gm.easecode.common.vo.AppClassHandler;
import com.gm.easecode.common.vo.AppClassMethodMain;
import com.gm.easecode.common.vo.AppModule;
import com.gm.easecode.common.vo.AppModuleBuilder;
import com.gm.easecode.common.vo.AppNameSpace;
import com.gm.easecode.common.vo.AppTable;
import com.gm.easecode.common.vo.AppTableContext;
import com.gm.easecode.common.vo.ClassType;
import com.gm.easecode.common.vo.ControllerClassStyleMode;
import com.gm.easecode.common.vo.FileAliasMode;
import com.gm.easecode.config.AppConfig;
import com.gm.easecode.frame.FrameworkProvider;
import com.gm.easecode.frame.FrameworkProviderFactory;
import com.gm.easecode.message.MessageEntity;
import com.gm.easecode.message.MsgCallback;
import com.gm.easecode.source.DataSource;
import com.gm.easecode.source.provider.DataSourceProvider;
import com.gm.easecode.source.provider.DataSourceProviderFactory;
import com.gm.easecode.source.provider.spi.InnerDataSource;

public class AppCodeService {

	public static void createCode(AppConfig config) {
		if (config == null) {
			throw new AppException("配置信息不能为空");
		}
		config.init();//初始化配置
		
		MsgCallback callback = new MsgCallback() {
			@Override
			public void notifyMsg(MessageEntity msg) {
				System.out.println(msg.msg);
			}
		};
		DataSource dataSource = config.getDataSource();
		if (dataSource == null) {
			throw new AppException("未配置数据源");
		}
		DataSourceProvider dataSourceProvider = DataSourceProviderFactory.createDataSourceProvider(dataSource);
		if (dataSourceProvider == null) {
			callback.notifyMsg(new MessageEntity("没有符合条件的DataSourceProvider[" + dataSource + "]"));
			return;
		}
		List<AppTable> tableList = dataSourceProvider.findTable(dataSource, callback);
		if (tableList == null || tableList.isEmpty()) {
			callback.notifyMsg(new MessageEntity("没有符合格式的表"));
			return;
		}
		callback.notifyMsg(new MessageEntity("共获取" + tableList.size() + "张表信息."));
		//判断是否添加内置模块
		if (config.isCreateSystemModule()) {
			callback.notifyMsg(new MessageEntity("开始初始化系统模块..."));
			InnerDataSource innerDataSource = new InnerDataSource(config.getBuiltInModulePath(), config.getInnerModules(), config.getTableNamePrefix(), config.getFrameworkName());
			DataSourceProvider innerDataSourceProvider = DataSourceProviderFactory.createDataSourceProvider(innerDataSource);
			List<AppTable> innerTableList = innerDataSourceProvider.findTable(innerDataSource, callback);
			StringBuilder tableBuild = new StringBuilder();
			if (innerTableList != null && !innerTableList.isEmpty()) {
				tableList.addAll(innerTableList);
				for (AppTable table : innerTableList) {
					tableBuild.append(table.getTableName()).append(",");
				}
			}
			AppModule innerModule = new AppModuleBuilder().forName("系统管理").forIdentify("system").forSubModuleEnable(true).forTables(tableBuild.toString()).build();
			config.getModules().add(innerModule);
		}
		FrameworkProvider frameworkProvider = FrameworkProviderFactory.createFrameworkProvider(config.getFrameworkName(), config.getFrameworkVersion());
		if (frameworkProvider == null) {
			callback.notifyMsg(new MessageEntity("没有符合条件的FrameworkProvider[name:" + config.getFrameworkName()+"][version:" + config.getFrameworkVersion() + "]"));
			return;
		}
		AppNameSpace appNameSpace = new AppNameSpace(config);
		appNameSpace.setFrameDependey(frameworkProvider.getFrameDependey());
		AppModule defaultModule = new AppModuleBuilder().build();
		List<AppModule> modules = config.getModules();
		Map<String, AppModule> tableModuleMap = new HashMap<>();
		if (modules != null && modules.size() > 0) {
			for (AppModule module : modules) {
				Set<String> tables = StringUtils.converStr2Set(module.getTables());
				for (String table : tables) {
					tableModuleMap.put(table, module);
				}
			}
		} else {
			callback.notifyMsg(new MessageEntity("无模板规则，使用默认规则！", false, false));
		}
		List<AppTableContext> appContextList = new ArrayList<AppTableContext>();
		Set<String> createTables = new HashSet<String>();
		Set<String> enableTables = StringUtils.converStr2Set(config.getEnableTables());
		Set<String> filterTables = StringUtils.converStr2Set(config.getFilterTables());
		for (AppTable table : tableList) {
			String tableName = table.getTableName();
			String tableComment = table.getComment();
			if (tableComment != null && tableComment.trim().length() > 0) {
				tableComment = tableComment.trim();
				if (tableComment.endsWith("表")) {
					tableComment = tableComment.substring(0, tableComment.length() - 1);
				}
			}
			String realTableName = tableName.toLowerCase();
			if (createTables.contains(realTableName) 
					|| filterTables.contains(realTableName) 
					|| (!enableTables.isEmpty() && !enableTables.contains(realTableName) && !table.isInnerTable())) {
				continue;
			}
			createTables.add(realTableName);
			AppModule appModule = tableModuleMap.get(realTableName);
			if (appModule == null) {// 无匹配规则，使用默认规则
				appModule = defaultModule;
			}
			AppTableContext appContext = new AppTableContext(frameworkProvider, appNameSpace, table, appModule);
			appContextList.add(appContext);
		}
		callback.notifyMsg(new MessageEntity("共" + appContextList.size() + "张表需要生成代码"));
		try {
			createCode(config, appNameSpace, appContextList, frameworkProvider, callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 创建代码
	 * @param category
	 * @param configStyle
	 * @param buildModule	内置模块名，多个名称使用逗号分隔
	 * @param appConfig		配置信息
	 * @param contexts
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	public static void createCode(AppConfig config, AppNameSpace appNameSpace, List<AppTableContext> contexts, FrameworkProvider frameworkProvider, MsgCallback callback) throws Exception {
		if (contexts == null || contexts.isEmpty()) {
			throw new AppException("没有需要创建的表");
		}
		try {
			callback.notifyMsg(new MessageEntity("构建文件"));
			if (config.isCleanOldCode()) {
				FileUtil.deleteAll(config.getCodeSavePath());
				callback.notifyMsg(new MessageEntity("清除历史文件--->" + config.getCodeSavePath()));
			}
			callback.notifyMsg(new MessageEntity("预存储文件路径--->" + config.getCodeSavePath()));
			callback.notifyMsg(new MessageEntity("开始构建模块代码"));
			for (AppTableContext context : contexts) {
				callback.notifyMsg(new MessageEntity("构建模块[" + context.table.getTableName() + "]"));
				if (config.isCreateEntityFile()) {
					crtIbatisXmlFile(context, FileAliasMode.EntityXml.name(), callback);
					crtClassFile(context, FileAliasMode.Entity.name(), callback);
					crtClassFile(context, FileAliasMode.Query.name(), callback);
				}
				if (config.isCreateDaoFile()) {
					crtClassFile(context, FileAliasMode.Dao.name(), callback);
					crtClassFile(context, FileAliasMode.DaoImpl.name(), callback);
				}
				if (config.isCreateServiceFile()) {
					crtClassFile(context, FileAliasMode.Service.name(), callback);
					crtClassFile(context, FileAliasMode.ServiceImpl.name(), callback);
				}
				if (config.isCreateControllerFile()) {
					crtClassFile(context, FileAliasMode.Controller.name(), callback);
					if (config.getControllerClassStyle() == ControllerClassStyleMode.DTO
							|| config.getControllerClassStyle() == ControllerClassStyleMode.DTO_MAPPING) {
						crtClassFile(context, FileAliasMode.RequestDto.name(), callback);
						crtClassFile(context, FileAliasMode.RequestPageDto.name(), callback);
						crtClassFile(context, FileAliasMode.ResponseDto.name(), callback);
					} else {
						crtClassFile(context, FileAliasMode.Form.name(), callback);
					}
				}
				if (config.isCreateSqlFile()) {
					crtSqlFile(context, null, callback);
				}
			}
			if (config.isCreateBootstrapFile()) {
				callback.notifyMsg(new MessageEntity("开始构建启动代码"));
				crtBootstrapClassFile(appNameSpace, frameworkProvider, callback);
				crtApplicationYmlFile(appNameSpace, frameworkProvider, callback);
				crtApplicationServiceYmlFile(appNameSpace, frameworkProvider, callback);
				crtApplicationServiceDevYmlFile(appNameSpace, frameworkProvider, callback);
				crtLogbckXmlFile(appNameSpace, frameworkProvider, callback);
				crtPomXmlFile(appNameSpace, frameworkProvider, callback);
			}
		} finally {
			callback.notifyMsg(new MessageEntity("构建完成"));
		}
	}
	
	private static void crtClassFile(AppTableContext context, String classKey, MsgCallback callback) throws Exception {
		AppClass appClass = context.getClassMap().get(classKey);
		if (appClass != null) {
			String content = ClassFileUtil.crtClassContent(context, appClass);
			String filePath = appClass.getFilePath() + appClass.getFileName();
			callback.notifyMsg(new MessageEntity(filePath));
			filePath = StringUtils.addSeparator(context.getConfig().getCodeSavePath()) + filePath;
			FileUtil.write(filePath, content, false, true, context.getConfig().getCharset());
		}
	}
	
	private static void crtIbatisXmlFile(AppTableContext context, String classKey, MsgCallback callback) throws Exception{
		String content = IbatisXmlUtil.getIbatisXmlContent(context);
		String filePath = context.getNameParam().getIbatisConfPath() + context.getNameParam().getIbatisName();
		callback.notifyMsg(new MessageEntity(filePath));
		filePath = StringUtils.addSeparator(context.getConfig().getCodeSavePath()) + filePath;
		FileUtil.write(filePath, content, false, true, context.getConfig().getCharset());
	}
	
	private static void crtSqlFile(AppTableContext context, String classKey, MsgCallback callback) throws Exception{
		String content = TableUtil.getSqlContent(context);
		String filePath = context.getNameParam().getSqlFilePath() + context.getNameParam().getSqlFileName();
		callback.notifyMsg(new MessageEntity(filePath));
		filePath = StringUtils.addSeparator(context.getConfig().getDataSavePath()) + filePath;
		FileUtil.write(filePath, content, true, true, context.getConfig().getCharset());
	}
	
	private static void crtBootstrapClassFile(AppNameSpace appNameSpace, FrameworkProvider frameworkProvider, MsgCallback callback) throws Exception{
		AppConfig config = appNameSpace.getConfig();
		String className = StringUtils.firstToUpperCase(config.getAppName()) + config.getBootstrapSuffix();
		String packageName = config.getRootPackage();
		String baseJavaPath = appNameSpace.getBaseJavaPath(null, null);
		String path = TableUtil.getPath(baseJavaPath, packageName);
		AppClass appClass = new AppClassBuilder(new AppClassHandler(), frameworkProvider)
		.setAliasName(FileAliasMode.Bootstrap.name())
		.setClassName(className)
		.setClassType(ClassType.Class.getValue())
		.setPackageName(packageName)
		.setFilePath(path)
		.setDesc("启动类")
		.build();
		appClass.addImportClass("SpringApplication");
		appClass.addMethod(new AppClassMethodMain("SpringApplication.run(" + className + ".class, args);"));
		String content = ClassFileUtil.crtClassContent(config, appClass);
		String filePath = appClass.getFilePath() + appClass.getFileName();
		callback.notifyMsg(new MessageEntity(filePath));
		filePath = StringUtils.addSeparator(config.getCodeSavePath()) + filePath;
		FileUtil.write(filePath, content, false, true, config.getCharset());
	}
	private static void crtApplicationYmlFile(AppNameSpace appNameSpace, FrameworkProvider frameworkProvider, MsgCallback callback) throws Exception{
		AppConfig config = appNameSpace.getConfig();
		String content = YmlUtil.getApplicationYmlContent(appNameSpace);
		String filePath = appNameSpace.getApplicationYmlFilePath() + appNameSpace.getApplicationYmlFileName();
		callback.notifyMsg(new MessageEntity(filePath));
		filePath = StringUtils.addSeparator(config.getCodeSavePath()) + filePath;
		FileUtil.write(filePath, content, false, true, config.getCharset());
	}
	private static void crtApplicationServiceYmlFile(AppNameSpace appNameSpace, FrameworkProvider frameworkProvider, MsgCallback callback) throws Exception{
		AppConfig config = appNameSpace.getConfig();
		String content = YmlUtil.getApplicationServiceYmlContent(appNameSpace);
		String filePath = appNameSpace.getApplicationServiceYmlFilePath() + appNameSpace.getApplicationServiceYmlFileName();
		callback.notifyMsg(new MessageEntity(filePath));
		filePath = StringUtils.addSeparator(config.getCodeSavePath()) + filePath;
		FileUtil.write(filePath, content, false, true, config.getCharset());
	}
	private static void crtApplicationServiceDevYmlFile(AppNameSpace appNameSpace, FrameworkProvider frameworkProvider, MsgCallback callback) throws Exception{
		AppConfig config = appNameSpace.getConfig();
		String content = YmlUtil.getApplicationServiceDevYmlContent(appNameSpace);
		String filePath = appNameSpace.getApplicationServiceDevYmlFilePath() + appNameSpace.getApplicationServiceDevYmlFileName();
		callback.notifyMsg(new MessageEntity(filePath));
		filePath = StringUtils.addSeparator(config.getCodeSavePath()) + filePath;
		FileUtil.write(filePath, content, false, true, config.getCharset());
	}
	private static void crtPomXmlFile(AppNameSpace appNameSpace, FrameworkProvider frameworkProvider, MsgCallback callback) throws Exception{
		AppConfig config = appNameSpace.getConfig();
		String content = YmlUtil.getPomXmlContent(appNameSpace);
		String filePath = appNameSpace.getPomXmlFilePath() + appNameSpace.getPomXmlFileName();
		callback.notifyMsg(new MessageEntity(filePath));
		filePath = StringUtils.addSeparator(config.getCodeSavePath()) + filePath;
		FileUtil.write(filePath, content, false, true, config.getCharset());
	}
	private static void crtLogbckXmlFile(AppNameSpace appNameSpace, FrameworkProvider frameworkProvider, MsgCallback callback) throws Exception{
		AppConfig config = appNameSpace.getConfig();
		String content = YmlUtil.getLogbackXmlContent(appNameSpace);
		String filePath = appNameSpace.getLogbackXmlFilePath() + appNameSpace.getLogbackXmlFileName();
		callback.notifyMsg(new MessageEntity(filePath));
		filePath = StringUtils.addSeparator(config.getCodeSavePath()) + filePath;
		FileUtil.write(filePath, content, false, true, config.getCharset());
	}
}
