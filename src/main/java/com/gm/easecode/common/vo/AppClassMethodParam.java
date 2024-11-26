package com.gm.easecode.common.vo;

import java.io.Serializable;

public class AppClassMethodParam implements Serializable {

	private static final long serialVersionUID = 5088733336930884997L;
	/** 参数名 */
	private String name;
	/** 参数描述 */
	private String desc;
	/** 参数类型 */
	private String type;
	
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
}
