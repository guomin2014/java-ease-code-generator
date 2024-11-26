package com.gm.easecode.common.vo;

import com.gm.easecode.common.util.StringUtils;

/**
 * 类字段（列表模式）
 * @author GM
 * @date 2024-04-28
 */
public class AppClassFieldList extends AppClassField {

	private static final long serialVersionUID = 3508070979047277502L;
	/** 名称 */
	private String name;
	/** 描述 */
	private String desc;
	/** 类型 */
	private String type;
	/** 类型的泛型列表 */
	private String[] typeGenericClasses;
	/** 备注 */
	private String comment;
	/** 值，申明属性时使用 */
	private Object value;
	/** 默认值，初始化方法时使用 */
	private Object defaultValue;
	/** 访问控制修饰符，private，public，protected */
	private String modifier = "private";
	/** 是否final修饰 */
	private boolean isFinal;
	/** 是否static修饰 */
	private boolean isStatic;
	/** 是否@Autowired修饰 */
	private boolean isAutowired;
	/** 是否是父级存在字段 */
	private boolean isParentExistsField;
	/** 属性的annotation修饰 */
	private AppAnnotation annotation;
	
	public AppClassFieldList() {}
	
	public AppClassFieldList(String name, String type) {
		this(name, type, null);
	}
	public AppClassFieldList(String name, String type, Object value) {
		this(name, type, value, null);
	}
	public AppClassFieldList(String name, String type, Object value, String desc) {
		this(name, type, value, desc, null);
	}
	public AppClassFieldList(String name, String type, Object value, String desc, String comment) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.desc = desc;
		this.comment = comment;
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
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public boolean isFinal() {
		return isFinal;
	}
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	public boolean isStatic() {
		return isStatic;
	}
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	public boolean isAutowired() {
		return isAutowired;
	}
	public void setAutowired(boolean isAutowired) {
		this.isAutowired = isAutowired;
	}
	public boolean isParentExistsField() {
		return isParentExistsField;
	}
	public void setParentExistsField(boolean isParentExistsField) {
		this.isParentExistsField = isParentExistsField;
	}
	public String[] getTypeGenericClasses() {
		return typeGenericClasses;
	}
	public void setTypeGenericClasses(String[] typeGenericClasses) {
		this.typeGenericClasses = typeGenericClasses;
	}
	public AppAnnotation getAnnotation() {
		return annotation;
	}
	public void setAnnotation(AppAnnotation annotation) {
		this.annotation = annotation;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * 获取完整的字段描述信息
	 * @return
	 */
	public String getFullDesc() {
		return StringUtils.trim(this.getDesc()) + (StringUtils.isNotEmpty(this.getComment()) ? "，" + StringUtils.trim(this.getComment()) : "");
	}
}
