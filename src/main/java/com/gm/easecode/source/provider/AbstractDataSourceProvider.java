package com.gm.easecode.source.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gm.easecode.common.AppException;
import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.util.TableUtil;
import com.gm.easecode.common.vo.AppTable;
import com.gm.easecode.common.vo.AppTableColumn;
import com.gm.easecode.common.vo.FieldVO;

public abstract class AbstractDataSourceProvider implements DataSourceProvider {

	/** 备注说明中的分类说明 */
	private static String regex = "(\\d{1,})[：:]([^，]*)";
	private static Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	/** 获取字段的数据库类型 */
	private static String fieldTypeRegex = "([^\\(]*)[\\(]*([^\\)]*)[\\)]*";
	private static Pattern fieldTypePattern = Pattern.compile(fieldTypeRegex, Pattern.CASE_INSENSITIVE);
	/** 获取字段的默认值 */
	private static String fieldDefaultRegex = ".*默认[\\s]*[:：]*[\\s]*(\\d{1,}).*";
	private static Pattern fieldDefaultPattern = Pattern.compile(fieldDefaultRegex, Pattern.CASE_INSENSITIVE);
	/** 获取字段的单位 */
	private static String fieldUnitRegex = ".*单位[\\s]*[:：]*[\\s]*([^,|，]*).*";
	private static Pattern fieldUnitPattern = Pattern.compile(fieldUnitRegex, Pattern.CASE_INSENSITIVE);
	/**
	 * 转换表字段信息
	 * @param fieldInfo
	 * @return
	 */
	public AppTableColumn convertTableColumn(FieldVO fieldInfo) {
		String fieldName = fieldInfo.getName();//字段名
		String fieldDesc = fieldInfo.getDesc();//字段名称
		String fieldType = fieldInfo.getType();//字段类型
		String fieldRequired = fieldInfo.getIsRequired();//是否必填
		String fieldComment = fieldInfo.getComment();//字段描述
		String fieldForList = fieldInfo.getIsListField();
		String fieldForEdit = fieldInfo.getIsEditField();
		String fieldForQuery = fieldInfo.getIsQueryField();
		String fieldIsAutoIncr = fieldInfo.getIsAutoIncr();
		String fieldDefaultValue = fieldInfo.getDefaultValue();
		AppTableColumn column = new AppTableColumn();
		column.setColumnName(fieldName);
		column.setColumnDefault(StringUtils.isNotEmpty(fieldDefaultValue) ? fieldDefaultValue : getColumnDefault(fieldComment));
		column.setColumnDesc(fieldDesc);
		column.setColumnComment(fieldComment);
		column.setColumnUnit(getColumnUnit(fieldComment));
		column.setColumnType(fieldType);
		List<String[]> list = getColumnType(fieldType);
		if (list.isEmpty()) {
			throw new AppException("不能识别的字段类型[fieldName:" + fieldName + "][fieldType:" + fieldType + "]");
		}
		String[] types = list.get(0);
		column.setDbType(types[0]);
		try {
			if (StringUtils.isNotEmpty(types[1])) {
				int len = Integer.parseInt(types[1]);
				if (column.getDbType().indexOf("int") != -1 || column.getDbType().indexOf("double") != -1 || column.getDbType().indexOf("float") != -1) {
					column.setNumericLength(len);
					column.setNumericScale(0);
				}
				column.setCharLength(len);
			}
		} catch (Exception e) {
		}

		StringBuilder javaPropertyName = new StringBuilder();
		String[] arr = fieldName.replace("-", "_").split("_");
		for (int n = 0; n < arr.length; n++) {
			String item = arr[n];
			if (n > 0) {
				javaPropertyName.append(item.substring(0, 1).toUpperCase()).append(item.substring(1));
			} else {
				javaPropertyName.append(item);
			}
		}
		String propertyName = javaPropertyName.toString();
		column.setJavaPropertyName(propertyName);
		column.setJavaType(TableUtil.getJavaType(column.getDbType()));
		column.setJdbcType(TableUtil.getJdbcType(column.getJdbcType()));

		if ("id".equalsIgnoreCase(fieldName) 
				|| (StringUtils.isNotEmpty(fieldComment) && fieldComment.indexOf("主键") != -1) 
				|| (StringUtils.isNotEmpty(fieldIsAutoIncr) && (fieldIsAutoIncr.equalsIgnoreCase("Yes") || fieldIsAutoIncr.equalsIgnoreCase("true")))) {
			column.setPri(true);
		}
		if (StringUtils.isNotEmpty(fieldComment)) {
			if (fieldComment.indexOf("自增长") != -1) {
				fieldComment = fieldComment.replaceAll("自增长", "");
				column.setAutoIncrement(true);
			}
			if (fieldComment.indexOf("或条件匹配") != -1) {
				fieldComment = fieldComment.replaceAll("或条件匹配", "");
	            column.setOrCondMatch(true);
			}
			if (fieldComment.indexOf("前置匹配") != -1) {
				fieldComment = fieldComment.replaceAll("前置匹配", "");
	            column.setStartWithMatch(true);
			}
			if (fieldComment.indexOf("后置匹配") != -1) {
				fieldComment = fieldComment.replaceAll("后置匹配", "");
			    column.setEndWithMatch(true);
			}
			if (fieldComment.indexOf("前后匹配") != -1) {
				fieldComment = fieldComment.replaceAll("前后匹配", "");
				column.setStartAndEndMatch(true);
			}
			if (fieldComment.indexOf("大小匹配") != -1) {
				fieldComment = fieldComment.replaceAll("大小匹配", "");
			    column.setCompareSize(true);
			}
		}
		if (StringUtils.isNotEmpty(fieldForList) && (fieldForList.equals("是") || fieldForList.equalsIgnoreCase("true") || fieldForList.equalsIgnoreCase("Yes") || fieldForList.equals("1"))) {
			column.setListField(true);
		}
		if (StringUtils.isNotEmpty(fieldForEdit) && (fieldForEdit.equals("是") || fieldForEdit.equalsIgnoreCase("true") || fieldForEdit.equalsIgnoreCase("Yes") || fieldForEdit.equals("1"))) {
			column.setEditField(true);
		}
		if (StringUtils.isNotEmpty(fieldForQuery) && (fieldForQuery.equals("是") || fieldForQuery.equalsIgnoreCase("true") || fieldForQuery.equalsIgnoreCase("Yes") || fieldForQuery.equals("1"))) {
			column.setQueryField(true);
		}
		if (StringUtils.isNotEmpty(fieldRequired) && (fieldRequired.equals("是") || fieldRequired.equalsIgnoreCase("true") || fieldRequired.equalsIgnoreCase("Yes") || fieldRequired.equals("1"))) {
			column.setRequestFields(true);
		}
		return column;
	}
	
	/**
	 * 转换表信息
	 * @param tableName		表名
	 * @param tableDesc		表描述
	 * @param treeTable		是否是树级表
	 * @param innerTable	是否是内部表
	 * @param columnList	表的字段列表
	 * @return
	 */
	public AppTable convertTable(String tableName, String tableDesc, boolean treeTable, boolean innerTable, List<AppTableColumn> columnList) {
		String realTableName = getTableRealName(tableName);
		AppTable table = new AppTable();
		table.setOldTableName(tableName);
		table.setTableName(realTableName);
		table.setIsSubmeterTable(!realTableName.equals(tableName));
		table.setTreeTable(treeTable);
		table.setInnerTable(innerTable);
		table.setComment(tableDesc);
		List<AppTableColumn> priColList = new ArrayList<AppTableColumn>();
		//设置自增长列表配置、页面显示字段配置
		for (AppTableColumn column : columnList) {
			if (column.isPri()) {
				priColList.add(column);
			}
			if (column.isAutoIncrement()) {
				table.setAutoIncrKey(true);
				table.setAutoIncrCol(column);
			}
			if (column.isListField()) {
				table.getListFields().add(column.getJavaPropertyName());
			}
			if (column.isEditField()) {
				table.getEditFields().add(column.getJavaPropertyName());
			}
			if (column.isQueryField()) {
				table.getQueryFields().add(column.getJavaPropertyName());
			}
			if (column.isRequestFields()) {
				table.getRequestFields().add(column.getJavaPropertyName());
			}
		}
		table.setColumnList(columnList);
		table.setPriColList(priColList);
		return table;
	}
	
	/**
	 * 获取表的真实名称（去yyyyMM、中文等参数）
	 * @param tableName
	 * @return
	 */
	public final String getTableRealName(String tableName) {
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

	public final static String getSubmeterTableName(String tableName) {
		if (StringUtils.isEmpty(tableName)) {
			return "";
		}
		tableName = tableName.replaceAll("_[y|Y]{2,4}[m|M]{0,2}[d|D]{0,2}", "_${unionTime}");
		StringBuffer sb = new StringBuffer();
		String[] arr = tableName.split("_");
		for (int i = 0; i < arr.length; ++i) {
			String name = arr[i];
			if (StringUtils.isContainChinese(arr[i])) {
				name = "${unionId}";
			}
			sb.append(name).append("_");
		}
		if (sb.length() > 0) {
			return sb.substring(0, sb.length() - 1);
		}
		return sb.toString();
	}
	
	public List<String[]> getColumnType(String columnType) {
		List<String[]> list = new ArrayList<String[]>();
		if (columnType == null || columnType.trim().length() == 0) {
			return list;
		}
		Matcher matcher = fieldTypePattern.matcher(columnType);
		int index = 0;
		while (index < columnType.length() && matcher.find(index)) {
			int count = matcher.groupCount();
			if (count >= 2) {
				list.add(new String[] { matcher.group(1), matcher.group(2) });
			}
			index = matcher.end() + 1;
		}
		return list;
	}

	public String getColumnDefault(String columnComment) {
		if (columnComment == null || columnComment.trim().length() == 0) {
			return "";
		}
		Matcher matcher = fieldDefaultPattern.matcher(columnComment);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

	public String getColumnUnit(String columnComment) {
		if (columnComment == null || columnComment.trim().length() == 0) {
			return "";
		}
		Matcher matcher = fieldUnitPattern.matcher(columnComment);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

	/**
	 * 从字段备注中获取字段的描述名称
	 * 
	 * @param columnComment
	 * @return
	 */
	public String getColumnDescName(String columnComment) {
		if (columnComment == null || columnComment.trim().length() == 0) {
			return "";
		}
		columnComment = columnComment.replaceAll("[,，]", " ");
		return columnComment.split(" ")[0].trim();
	}

	public List<String[]> getColumnCategorys(String columnComment) {
		List<String[]> list = new ArrayList<String[]>();
		if (columnComment == null || columnComment.trim().length() == 0) {
			return list;
		}
		Matcher matcher = pattern.matcher(columnComment);
		int index = 0;
		while (index < columnComment.length() && matcher.find(index)) {
			int count = matcher.groupCount();
			if (count >= 2) {
				list.add(new String[] { matcher.group(1), matcher.group(2) });
			}
			index = matcher.end() + 1;
		}
		return list;
	}
}
