package com.gm.easecode.common.vo;

import com.gm.easecode.common.util.StringUtils;

public class AppTableColumn {
	/** 列名 */
	private String columnName;
	/** 列描述 */
	private String columnDesc;
	/** 缺省值 */
	private String columnDefault;
	/** 数据库数据类型 */
	private String dbType;
	/** Java对应的属性名 */
	private String javaPropertyName;
	/** java对应数据类型 */
	private String javaType;
	/** jdbc对应的类型 */
	private String jdbcType;
	/** 字符长度 */
	private int charLength;
	/** 数字长度 */
	private int numericLength;
	/** 小数位数 */
	private int numericScale;
	/** 备注 */
	private String columnComment;
	/** 字段单位 */
	private String columnUnit;
	/** 是否自增长 */
	private boolean autoIncrement;
	/** 列数据类型 */
	private String columnType;
	/** 是否主健字段 */
	private boolean isPri;
	/** 是否列表显示字段 */
	private boolean isListField;
	/** 是否查询字段 */
	private boolean isQueryField;
	/** 是否编辑字段 */
	private boolean isEditField;
	/** 是否必填字段 */
	private boolean isRequestFields;
	/** 或条件匹配 */
	private boolean orCondMatch;
	/** 前置匹配 */
	private boolean startWithMatch;
	/** 后置匹配 */
	private boolean endWithMatch;
	/** 前后匹配 */
	private boolean startAndEndMatch;
	/** 大小匹配 */
	private boolean compareSize;
	
	public AppTableColumn(){
		
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnDesc() {
		return columnDesc;
	}

	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}

	public String getColumnDefault() {
		return columnDefault;
	}

	public void setColumnDefault(String columnDefault) {
		this.columnDefault = columnDefault;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getJavaPropertyName() {
		return javaPropertyName;
	}

	public void setJavaPropertyName(String javaPropertyName) {
		this.javaPropertyName = javaPropertyName;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public int getCharLength() {
		return charLength;
	}

	public void setCharLength(int charLength) {
		this.charLength = charLength;
	}

	public int getNumericLength() {
		return numericLength;
	}

	public void setNumericLength(int numericLength) {
		this.numericLength = numericLength;
	}

	public int getNumericScale() {
		return numericScale;
	}

	public void setNumericScale(int numericScale) {
		this.numericScale = numericScale;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public boolean isPri() {
		return isPri;
	}

	public void setPri(boolean isPri) {
		this.isPri = isPri;
	}

	public boolean isListField() {
		return isListField;
	}

	public void setListField(boolean isListField) {
		this.isListField = isListField;
	}

	public boolean isQueryField() {
		return isQueryField;
	}

	public void setQueryField(boolean isQueryField) {
		this.isQueryField = isQueryField;
	}

	public boolean isEditField() {
		return isEditField;
	}

	public void setEditField(boolean isEditField) {
		this.isEditField = isEditField;
	}

	public boolean isRequestFields() {
		return isRequestFields;
	}

	public void setRequestFields(boolean isRequestFields) {
		this.isRequestFields = isRequestFields;
	}

	public String getColumnUnit() {
		return columnUnit;
	}

	public void setColumnUnit(String columnUnit) {
		this.columnUnit = columnUnit;
	}

	public boolean isOrCondMatch() {
		return orCondMatch;
	}

	public void setOrCondMatch(boolean orCondMatch) {
		this.orCondMatch = orCondMatch;
	}

	public boolean isStartWithMatch() {
		return startWithMatch;
	}

	public void setStartWithMatch(boolean startWithMatch) {
		this.startWithMatch = startWithMatch;
	}

	public boolean isEndWithMatch() {
		return endWithMatch;
	}

	public void setEndWithMatch(boolean endWithMatch) {
		this.endWithMatch = endWithMatch;
	}

	public boolean isStartAndEndMatch() {
		return startAndEndMatch;
	}

	public void setStartAndEndMatch(boolean startAndEndMatch) {
		this.startAndEndMatch = startAndEndMatch;
	}

	public boolean isCompareSize() {
		return compareSize;
	}

	public void setCompareSize(boolean compareSize) {
		this.compareSize = compareSize;
	}
	
	/**
	 * 获取完整的字段描述信息
	 * @return
	 */
	public String getFullDesc() {
		return StringUtils.trim(this.getColumnDesc()) + (StringUtils.isNotEmpty(this.getColumnComment()) ? "，" + StringUtils.trim(this.getColumnComment()) : "");
	}
}