package com.gm.easecode.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gm.easecode.common.vo.AppTable;
import com.gm.easecode.common.vo.AppTableColumn;
import com.gm.easecode.common.vo.AppTableContext;
import com.gm.easecode.common.vo.TableNameInfo;

public class TableUtil
{
	/** dbtype和javaType类型映射表 */
	private static Map<String,String> javaTypeMap = new HashMap<String,String>();
	/** 映射表不存时采用的javaType */
	private static String defaultJavaType = "String";
	
	static{
		javaTypeMap.put("varchar", "String");
		javaTypeMap.put("tinytext", "String");
		javaTypeMap.put("text", "String");
		javaTypeMap.put("mediumtext", "String");
		javaTypeMap.put("longtext", "String");
		javaTypeMap.put("tinyint", "Integer");
		javaTypeMap.put("smallint", "Integer");
		javaTypeMap.put("mediumint", "Integer");
		javaTypeMap.put("int", "Integer");
		javaTypeMap.put("integer", "Integer");
		javaTypeMap.put("bigint", "Long");
		javaTypeMap.put("float", "BigDecimal");
		javaTypeMap.put("real", "BigDecimal");
		javaTypeMap.put("double", "BigDecimal");
		javaTypeMap.put("numeric", "BigDecimal");
		javaTypeMap.put("decimal", "BigDecimal");
		javaTypeMap.put("date", "Date");
		javaTypeMap.put("time", "Date");
		javaTypeMap.put("timestamp", "Date");
		javaTypeMap.put("datetime", "Date");
		
	}
	
	/**
	 * 根据运行环境格式化路径字符串
	 * @param path
	 * @return
	 */
	public final static String formatPath(String path){
		path = StringUtils.addSeparator(StringUtils.replaceAllSeparator(path));
		return path;
	}
	public final static String trimPackTailSub(String pack){
		pack = formatPackage(pack);
		int pos = pack.lastIndexOf(".");
		if(pos >= 0){
			pack = pack.substring(0,pos);
		}else{
			pack = "";
		}
		return pack;
	}
	
	public final static String getTailSubName(String pack){
		pack = formatPackage(pack);
		int pos = pack.lastIndexOf(".");
		if(pos >= 0){
			pack = pack.substring(pos + 1);
		}
		return pack;
	}
	public final static String getSubPackage(String pack, int dept){
		if (StringUtils.isEmpty(pack)) {
			return "";
		}
		pack = formatPackage(pack);
		String[] packs = pack.split("\\.");
		if (packs.length <= dept) {
			return pack;
		} else {
			return StringUtils.converArray2Str(Arrays.copyOf(packs, dept), ".");
		}
	}
	public final static String getPackage(String rootPackage,String subPackage){
		return formatPackage(rootPackage) + "." + formatPackage(subPackage);
	}
	public final static String getPackage(String className) {
		if (className == null) {
			return null;
		}
		int pos = className.lastIndexOf(".");
		if (pos > 0) {
			return className.substring(0, pos);
		}
		return null;
	}
	public final static String formatPackage(String pkg){
		if(pkg.startsWith(".")){
			pkg = pkg.substring(1);
		}
		if(pkg.endsWith(".")){
			pkg = pkg.substring(0,pkg.length() - 1);
		}
		return pkg;
	}
	public static String formatPath2Package(String path) {
		path = StringUtils.replaceAllSeparator(path);
		return path.replaceAll("/", ".");
	}
	/**
	 * 将包转换为系统路径
	 * @param rootPath
	 * @param packageName
	 * @return
	 */
	public final static String getPath(String rootPath,String packageName){
		String path = formatPath(rootPath);
		String[] pkgPathArr = packageName.split("\\.");
		if(pkgPathArr.length > 0){
			for(String pkgPath:pkgPathArr){
				if(pkgPath.length() > 0){
					path = path + pkgPath + "/";
				}
			}
		}else{
			if(packageName.length() > 0){
				path = path + packageName + "/";
			}
		}
		return path;
	}
	public final static String getEntityName(String packageName){
		String[] pkgPathArr = packageName.split("\\.");
		StringBuilder build = new StringBuilder();
		for (String pkg : pkgPathArr) {
			if (build.length() > 0) {
				build.append(pkg.substring(0,1).toUpperCase()).append(pkg.substring(1));
			} else {
				build.append(pkg);
			}
		}
		return build.toString();
	}
	public final static String getTableAliasName(String tableName){
		String[] arr = tableName.split("_");
		for(int i = 1; i < arr.length; ++i){
			if(i == 1){
				tableName = arr[i].substring(0,1).toLowerCase() + arr[i].substring(1);
			}else{
				tableName = tableName + arr[i].substring(0,1).toUpperCase() + arr[i].substring(1);
			}
		}
		return tableName;
	}
	/**
	 * 获取表的真实名称（去yyyyMM、中文等参数）
	 * @param tableName
	 * @return
	 */
	public final static String getTableRealName(String tableName)
	{
		if (StringUtils.isEmpty(tableName)) {
			return "";
		}
		tableName = tableName.replaceAll("_[y|Y]{2,4}[m|M]{0,2}[d|D]{0,2}", "");
		StringBuffer sb = new StringBuffer();
		String[] arr = tableName.split("_");
		for (int i = 0; i < arr.length; ++i) {
			if (StringUtils.isContainChinese(arr[i])) {
				continue;
			}
			sb.append(arr[i]).append("_");
		}
		if (sb.length() > 0) {
			return sb.substring(0, sb.length() - 1);
		}
		return sb.toString();
	}
	/**
	 * 解析表名称
	 * 表命名规则：公司_平台_模块名称_XX，否则，表命名规则：模块名称_XX
	 * @param tableName
	 * @return
	 */
	public static TableNameInfo parseTableName(String tableName) {
		if (StringUtils.isEmpty(tableName)) {
			return null;
		}
		String company = null;
		String appName = null;
		String moduleName = "";
		String modulePackage = "";
		List<String> modulePackageList = new ArrayList<>();
		String[] arr = tableName.split("_");
		int moduleIndex = 0;
		if (arr.length > 2) {//数组长度大于2，表命名规则：公司_平台_模块名称_XX，否则，表命名规则：模块名称_XX
			moduleIndex = 2;
			company = arr[0];
			appName = arr[1];
		}
		for (int i = moduleIndex; i < arr.length; ++i) {
			String name = arr[i];
			moduleName = moduleName + StringUtils.firstToUpperCase(name);//组合模块名，如：gm_mqtransfer_cluster_node => ClusterNode
			modulePackage = modulePackage + (modulePackage.length() > 0 ? "." : "") + name.toLowerCase();//组合模块包名，如：gm_mqtransfer_cluster_node => cluster.node
			modulePackageList.add(name.toLowerCase());
		}
		TableNameInfo info = new TableNameInfo();
		info.setCompany(company);
		info.setAppName(appName);
		info.setModuleName(moduleName);
		info.setModulePackageJoin(modulePackage);
		info.setModulePackages(modulePackageList.toArray(new String[0]));
		return info;
	}
	/**
	 * 得到get方法
	 * @param fieldName
	 * @return
	 */
	public final static String getBeanGetMethod(String fieldName){
		return "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1,fieldName.length());
	}
	/**
	 * 得到set方法名称
	 * @param fieldName
	 * @return
	 */
	public final static String getBeanSetMethod(String fieldName){
		return "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1,fieldName.length());
	}
	

	/**
	 * 获取数据库类型对应的java类型
	 * @param dbType
	 * @return
	 */
	public final static String getJavaType(String dbType){
		String javaType = javaTypeMap.get(dbType.toLowerCase());
		if(javaType == null){
			javaType = defaultJavaType;
		}
		return javaType;
	}
	public static String formatColumns(String source){
		if(source != null && source.trim().length() > 0 && !source.endsWith(",")){
			source = source + ",";
		}
		if(source != null && source.trim().length() > 0 && !source.startsWith(",")){
			source = "," + source;
		}
		return source;
	}
	/**
	 * 获取javaType对应的jdbcType
	 * 未实现，jdbcType和javaType采用一样
	 * @param javaType
	 * @return
	 */
	public final static String getJdbcType(String javaType){
		return javaType;
	}
	/**
	 * 将首字母转换成小写
	 * @param str
	 * @return
	 */
	public static String convertFirstToLowerCase(String str)
	{
		if(str == null || str.trim().length() == 0)
		{
			return "";
		}
		str = str.trim();
		if(str.length() == 1)
		{
			return str.toLowerCase();
		}
		return str.substring(0,1).toLowerCase() + str.substring(1);
	}
	public static String convertFirstToUpperCase(String str)
	{
		if(str == null || str.trim().length() == 0)
		{
			return "";
		}
		str = str.trim();
		if(str.length() == 1)
		{
			return str.toUpperCase();
		}
		return str.substring(0,1).toUpperCase() + str.substring(1);
	}
	
	public static String generationCreateTableSql(AppTable table, int tabSize)
	{
		StringBuffer sql = new StringBuffer();
		StringBuffer tab = new StringBuffer();
		while(tabSize > 0)
		{
			tab.append("\t");
			tabSize--;
		}
		String tableName = table.getTableName();
		if(table.isSubmeterTable())
		{
			tableName = "${tableName}";
		}
		String comment = table.getComment();
		sql.append(tab);
		sql.append("CREATE TABLE `" + tableName + "`(");
		sql.append("\n");
		String priKey = "id";
		List<AppTableColumn> columns = table.getColumnList();
		for(AppTableColumn column : columns)
		{
		    if (column.isPri()) {
		        priKey = column.getColumnName();
		    }
			sql.append(tab);
			sql.append("\t");
			sql.append("`" + column.getColumnName() + "` ")
			.append(column.getColumnType())
			.append(column.isPri() ? " NOT NULL " : 
			    (column.isRequestFields() ? " NOT NULL " : "") + 
			    (StringUtils.isNotEmpty(column.getColumnDefault()) ? (" default '" + column.getColumnDefault() + "'") : (!column.isRequestFields() ? " default NULL " : "")))
			.append(column.isAutoIncrement() ? " auto_increment " : "")
			.append(" COMMENT '" + column.getFullDesc() + "'")
			.append(",");
			sql.append("\n");
		}
		sql.append(tab);
		sql.append("\t");
		sql.append("PRIMARY KEY  (`" + priKey + "`)");
		sql.append("\n");
		sql.append(tab);
		sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='" + comment + "';");
		sql.append("\n");
		return sql.toString();
	}
	
	/**
	 * 生成创建表语句
	 * @param table
	 * @return
	 */
	public static String generationCreateTableSql(AppTable table) {
		return generationCreateTableSql(table, 0);
	}
	
	public static String getSqlContent(AppTableContext context) {
		StringBuffer sql = new StringBuffer();
		String tableName = context.getTable().getTableName();
		sql.append("\n");
		sql.append("DROP TABLE IF EXISTS `" + tableName + "`;");
		sql.append("\n");
		sql.append(generationCreateTableSql(context.getTable()));
		return sql.toString();
	}
	
}
