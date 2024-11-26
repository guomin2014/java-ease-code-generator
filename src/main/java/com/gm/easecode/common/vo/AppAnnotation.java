package com.gm.easecode.common.vo;

import java.io.Serializable;

/**
 * 类、属性、方法的annotation修饰
 * @author GM
 * @date 2024-04-28
 */
public class AppAnnotation implements Serializable {

	private static final long serialVersionUID = -6281593493652564883L;
	/** 名称 */
	private String name;
	/** 值 */
	private Object value;
	
	public AppAnnotation() {}
	
	public AppAnnotation(String name) {
		this(name, null);
	}
	
	public AppAnnotation(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		build.append("@").append(getName());
		Object value = getValue();
		if (value != null) {
			build.append("(");
			build.append(value.toString());
			build.append(")");
		}
		return build.toString();
	}
	
}
