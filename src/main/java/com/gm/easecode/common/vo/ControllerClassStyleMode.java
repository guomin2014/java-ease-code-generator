package com.gm.easecode.common.vo;

/**
 * 构建Controller类风格
 * 0：SPRING-MVC，1：SPRING-MVC-MAPPING，2：JSON，3：JSON-MAPPING，4：DTO，4：DTO-MAPPING
 * @author	GM
 * @date	2018年9月10日
 */
public enum ControllerClassStyleMode {
	/** 0：MVC */
	SPRING_MVC(0, "SPRING-MVC"),
	/** 1：MVC-MAPPING */
	SPRING_MVC_MAPPING(1, "SPRING-MVC-MAPPING"), 
	/** 2：JSON */
	JSON(2, "JSON"), 
	/** 3：JSON-MAPPING */
	JSON_MAPPING(3, "JSON-MAPPING"), 
	/** 4：DTO */
	DTO(4, "DTO"), 
	/** 5：DTO-MAPPING */
	DTO_MAPPING(5, "DTO-MAPPING"), 
	;

	private int value;
	private String desc;

	ControllerClassStyleMode(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public static ControllerClassStyleMode getByValue(int value) {
		for (ControllerClassStyleMode status : values()) {
			if (status.getValue() == value) {
				return status;
			}
		}
		return null;
	}

	public int getValue() {
		return this.value;
	}

	public String getDesc() {
		return desc;
	}
}
