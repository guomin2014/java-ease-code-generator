package com.gm.easecode.common.vo;

/**
 * 原始字段信息（从DB或word中读取的原始信息）
 * @author GM
 * @date 2024-05-07
 */
public class FieldVO {
	/** 字段名 */
	private String name;
	/** 字段描述 */
	private String desc;
	/** 字段类型 */
	private String type;
	/** 备注 */
	private String comment;
	/** 默认值 */
	private String defaultValue;
	/** 是否列表显示字段 */
	private String isListField;
	/** 是否查询字段 */
	private String isQueryField;
	/** 是否编辑字段 */
	private String isEditField;
	/** 是否自增 */
	private String isAutoIncr;
	/** 是否必填 */
	private String isRequired;
	
	public FieldVO() {}
	
	public FieldVO(String name, String desc, String type, String comment, String defaultValue, String isAutoIncr, String isRequired) {
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.comment = comment;
		this.defaultValue = defaultValue;
		this.isAutoIncr = isAutoIncr;
		this.isRequired = isRequired;
	}
	public FieldVO(String name, String desc, String type, String comment, String defaultValue, String isListField, String isQueryField, String isEditField, 
			String isAutoIncr, String isRequired) {
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.comment = comment;
		this.defaultValue = defaultValue;
		this.isListField = isListField;
		this.isQueryField = isQueryField;
		this.isEditField = isEditField;
		this.isAutoIncr = isAutoIncr;
		this.isRequired = isRequired;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getIsListField() {
		return isListField;
	}
	public void setIsListField(String isListField) {
		this.isListField = isListField;
	}
	public String getIsQueryField() {
		return isQueryField;
	}
	public void setIsQueryField(String isQueryField) {
		this.isQueryField = isQueryField;
	}
	public String getIsEditField() {
		return isEditField;
	}
	public void setIsEditField(String isEditField) {
		this.isEditField = isEditField;
	}
	public String getIsAutoIncr() {
		return isAutoIncr;
	}
	public void setIsAutoIncr(String isAutoIncr) {
		this.isAutoIncr = isAutoIncr;
	}
	public String getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}
