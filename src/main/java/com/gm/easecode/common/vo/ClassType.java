package com.gm.easecode.common.vo;

/**
 * 文件别名
 * @author GM
 * @date 2024-04-30
 */
public enum ClassType {
	Class("class", "Class对象"),
	Interface("interface", "interface对象"),
	;

	private String value;
	private String desc;

	ClassType(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return this.value;
	}

	public String getDesc() {
		return desc;
	}

	public static ClassType getByValue(String value) {
		for (ClassType status : values()) {
			if (status.getValue().equalsIgnoreCase(value)) {
				return status;
			}
		}
		return null;
	}
	
	public static ClassType getByName(String name) {
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		for (ClassType status : values()) {
			if (status.name().equalsIgnoreCase(name)) {
				return status;
			}
		}
		return null;
	}

}
