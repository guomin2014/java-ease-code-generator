package com.gm.easecode.source.provider.spi;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gm.easecode.common.util.FileUtil;
import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.vo.AppClass;
import com.gm.easecode.common.vo.AppClassField;
import com.gm.easecode.common.vo.AppClassFieldBody;
import com.gm.easecode.common.vo.AppClassFieldList;
import com.gm.easecode.common.vo.AppClassMethod;
import com.gm.easecode.common.vo.AppClassMethodBody;
import com.gm.easecode.common.vo.AppClassMethodList;
import com.gm.easecode.common.vo.AppClassMethodParam;
import com.gm.easecode.common.vo.AppTable;
import com.gm.easecode.common.vo.AppTableColumn;
import com.gm.easecode.common.vo.FieldVO;
import com.gm.easecode.common.vo.FileAliasMode;
import com.gm.easecode.common.vo.InnerModuleConstants;
import com.gm.easecode.message.MessageEntity;
import com.gm.easecode.message.MsgCallback;
import com.gm.easecode.source.DataSource;
import com.gm.easecode.source.provider.AbstractDataSourceProvider;

public class InnerDataSourceProvider extends AbstractDataSourceProvider{

	private static final String MODULE_FILE_EXT = ".tpl";
	@Override
	public List<AppTable> findTable(DataSource dataSource, MsgCallback callback) {
		if (!(dataSource instanceof InnerDataSource)) {
			callback.notifyMsg(new MessageEntity("Mysql数据源不支持的配置[" + dataSource.getClass().getName() + "]"));
			return null;
		}
		callback.notifyMsg(new MessageEntity("开始从数据源[" + dataSource.getType().name() + "]获取表信息..."));
		InnerDataSource source = (InnerDataSource)dataSource;
		Map<String, AppTable> map = readInnerModule(source.getModulePath(), source.getModules(), source.getTableNamePrefix(), source.getFrameworkName(), callback);
		List<AppTable> retList = new ArrayList<AppTable>(map.values());
		Collections.sort(retList, new Comparator<AppTable>(){
			@Override
			public int compare(AppTable o1, AppTable o2)
			{
				return o1.getTableName().compareTo(o2.getTableName());
			}});
		return retList;
	}
	
	/**
	 * 读取内置模块
	 * @param modulePath
	 * @param modules
	 * @param callback
	 * @return
	 */
	public Map<String, AppTable> readInnerModule(String modulePath, String modules, String tableNamePrefix, String frameworkName, MsgCallback callback) {
		Map<String, AppTable> retMap = new LinkedHashMap<String, AppTable>();
		if (StringUtils.isEmpty(modules)) {
			callback.notifyMsg(new MessageEntity("未指定模块，将获取全部内置模块"));
			String path = StringUtils.addSeparator(ClassLoader.getSystemResource("").getPath()) + StringUtils.removeFirstSeparator(modulePath);
			File dir = new File(path);
	        if (dir.exists()) {
	        	File[] files = dir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(MODULE_FILE_EXT);
					}});
	        	if (files != null) {
	        		StringBuilder build = new StringBuilder();
	        		for (File ff : files) {
	        			build.append(FileUtil.getFileNameWithoutExt(ff.getName())).append(",");
	        		}
	        		modules = build.toString();
	        	}
	        }
		}
		if (StringUtils.isEmpty(modules)) {
			callback.notifyMsg(new MessageEntity("不能创建内置系统模块，未找到匹配的模块"));
			return retMap;
		}
		modulePath = StringUtils.addSeparator(modulePath);
		String[] temps = modules.split(",");
		for (String module : temps) {
			if (StringUtils.isEmpty(module)) {
				continue;
			}
			callback.notifyMsg(new MessageEntity("开始解析模板：" + module));
			String tplPath = modulePath + module + MODULE_FILE_EXT;
			try {
				String content = FileUtil.read(tplPath, "UTF-8");
				if (StringUtils.isEmpty(content)) {
					continue;
				}
				AppTable table = parseInnerModule(module, tableNamePrefix, frameworkName, content);
				if (table != null) {
					retMap.put(table.getTableName(), table);
				}
			} catch (Exception e) {
				callback.notifyMsg(new MessageEntity("加载系统模块异常[" + tplPath + "]-->" + e.getMessage()));
			}
		}
		callback.notifyMsg(new MessageEntity("共获取" + retMap.size() + "个内置模块信息"));
		return retMap;
	}
	
	private AppTable parseInnerModule(String module, String tableNamePrefix, String frameworkName, String content) {
		if (StringUtils.isEmpty(content)) {
			return null;
		}
		String tableName = module;
		String tableDesc = null;
		boolean treeTable = false;
		String info = parseNode(content, FileAliasMode.Entity.name(), InnerModuleConstants.INFO_KEY, frameworkName);
		List<AppTableColumn> columnList = new ArrayList<>();
		if (StringUtils.isNotEmpty(info)) {
			JSONObject infoJson = JSON.parseObject(info);
			tableName = infoJson.getString("name");
			tableDesc = infoJson.getString("desc");
			treeTable = infoJson.getBooleanValue("treeEnable");
			JSONArray fieldArrs = infoJson.getJSONArray("fields");
			if (fieldArrs != null) {
				for (int i = 0; i < fieldArrs.size(); i++) {
					JSONObject fieldJson = fieldArrs.getJSONObject(i);
					String fieldName = fieldJson.getString("name");
					String fieldDesc = fieldJson.getString("desc");
					String fieldType = fieldJson.getString("type");
					String fieldRequired = fieldJson.getString("required");
					String fieldComment = fieldJson.getString("comment");
					String fieldForList = fieldJson.getString("listEnable");
					String fieldForEdit = fieldJson.getString("editEnable");
					String fieldForQuery = fieldJson.getString("queryEnable");
					FieldVO fieldInfo = new FieldVO(fieldName, fieldDesc, fieldType, fieldComment, null, fieldForList, fieldForEdit, fieldForQuery, null, fieldRequired);
					AppTableColumn column = convertTableColumn(fieldInfo);
					columnList.add(column);
				}
			}
		}
		if (StringUtils.isEmpty(tableName)) {
			tableName = module;
		}
		if (StringUtils.isNotEmpty(tableNamePrefix) && !tableName.startsWith(tableNamePrefix)) {
			tableName = tableNamePrefix + tableName;
		}
		AppTable table = convertTable(tableName, tableDesc, treeTable, true, columnList);
		FileAliasMode[] aliases = FileAliasMode.values();
		for (FileAliasMode alias : aliases) {
			if (alias == FileAliasMode.EntityKey || alias == FileAliasMode.EntityXml) {
				continue;
			}
			parseNodeImport(content, alias.name(), InnerModuleConstants.IMPORTS_KEY, frameworkName, table);
			parseNodeField(content, alias.name(), InnerModuleConstants.FIELDS_KEY, frameworkName, table);
			parseNodeMethod(content, alias.name(), InnerModuleConstants.METHODS_KEY, frameworkName, table);
		}
		return table;
	}
	
	private static String parseNode(String content, String nodeName) {
		String startKey = "--" + nodeName + "#start--------";
		String endKey = "--" + nodeName + "#end--------";
		int startIndex = content.indexOf(startKey);
		int endIndex = content.indexOf(endKey);
		if (startIndex != -1 && endIndex != -1 && startIndex + + startKey.length() < endIndex) {
			return content.substring(startIndex + startKey.length(), endIndex);
		}
		return null;
	}
	private String parseNode(String content, String aliasName, String nodeName, String frameworkName) {
		String ret = parseNode(content, aliasName + "-" + nodeName + "#" + frameworkName);//先按框架获取内容
		if (StringUtils.isEmpty(ret)) {//未取到内容，则按能用模式获取
			ret = parseNode(content, aliasName + "-" + nodeName);
		}
		return ret;
	}
	private void parseNodeImport(String content, String aliasName, String nodeName, String frameworkName, AppTable table) {
		String imports = parseNode(content, aliasName, nodeName, frameworkName);
		if (StringUtils.isNotEmpty(imports)) {
			Set<String> importClasses = new TreeSet<>();
			JSONArray importArrs = null;
			try {
				importArrs = JSON.parseArray(imports);
			} catch (Exception e) {}
			if (importArrs == null) {
				String regex = "import[\\s]*([^;]*)[\\s]*;";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(imports);
				while(m.find()) {
					importClasses.add(m.group(1).trim());
				}
			} else {
				for (int i = 0; i < importArrs.size(); i++) {
					String importClass = importArrs.getString(i);
					importClasses.add(importClass);
				}
			}
			if (StringUtils.isNotEmpty(aliasName)) {
				AppClass appClass = table.getInnerClass(aliasName);
				if (appClass == null) {
					appClass = new AppClass();
				}
				appClass.addImportClasses(importClasses);
				table.addInnerClass(aliasName, appClass);
			}
		}
	}
	
	private void parseNodeField(String content, String aliasName, String nodeName, String frameworkName, AppTable table) {
		String fields = parseNode(content, aliasName, nodeName, frameworkName);
		if (StringUtils.isNotEmpty(fields)) {
			List<AppClassField> fieldList = new ArrayList<>();
			Set<String> importClasses = new TreeSet<>();
			JSONArray fieldArrs = null;
			try {
				fieldArrs = JSON.parseArray(fields);
			} catch (Exception e) {}
			if (fieldArrs == null) {//转换失败，原样写入
				AppClassFieldBody field = new AppClassFieldBody();
				field.setBody(fields);
				fieldList.add(field);
			} else {
				for (int i = 0; i < fieldArrs.size(); i++) {
					AppClassFieldList field = fieldArrs.getObject(i, AppClassFieldList.class);
					fieldList.add(field);
					importClasses.add(field.getType());
				}
			}
			if (StringUtils.isNotEmpty(aliasName)) {
				AppClass appClass = table.getInnerClass(aliasName);
				if (appClass == null) {
					appClass = new AppClass();
				}
				appClass.setFields(fieldList);
				appClass.addImportClasses(importClasses);
				table.addInnerClass(aliasName, appClass);
			}
		}
	}
	
	private void parseNodeMethod(String content, String aliasName, String nodeName, String frameworkName, AppTable table) {
		String methods = parseNode(content, aliasName, nodeName, frameworkName);
		if (StringUtils.isNotEmpty(methods)) {
			List<AppClassMethod> methodList = new ArrayList<>();
			Set<String> importClasses = new TreeSet<>();
			JSONArray methodArrs = null;
			try {
				methodArrs = JSON.parseArray(methods);
			} catch (Exception e) {}
			if (methodArrs == null) {//转换失败，原样写入
				AppClassMethodBody method = new AppClassMethodBody();
				method.setBody(methods);
				methodList.add(method);
			} else {
				for (int i = 0; i < methodArrs.size(); i++) {
					AppClassMethodList method = methodArrs.getObject(i, AppClassMethodList.class);
					methodList.add(method);
					importClasses.add(method.getReturnType());
					List<AppClassMethodParam> methodParams = method.getParams();
					if (methodParams != null) {
						for (AppClassMethodParam methodParam : methodParams) {
							importClasses.add(methodParam.getType());
						}
					}
					String[] throwsType = method.getThrowsType();
					if (throwsType != null) {
						for (String type : throwsType) {
							if (StringUtils.isNotEmpty(type)) {
								importClasses.add(type);
							}
						}
					}
					//获取方法的body
					String methodNodeName = aliasName + "-" + nodeName + "#" + frameworkName + "#" + method.getName();
					String methodBody = parseNode(content, methodNodeName);
					if (StringUtils.isEmpty(methodBody)) {
						methodNodeName = aliasName + "-" + nodeName + "#" + method.getName();
						methodBody = parseNode(content, methodNodeName);
					}
					if (StringUtils.isNotEmpty(methodBody)) {
						method.setBody(methodBody);
					}
				}
			}
			if (StringUtils.isNotEmpty(aliasName)) {
				AppClass appClass = table.getInnerClass(aliasName);
				if (appClass == null) {
					appClass = new AppClass();
				}
				appClass.setMethods(methodList);
				appClass.addImportClasses(importClasses);
				table.addInnerClass(aliasName, appClass);
			}
		}
	}

}
