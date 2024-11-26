package com.gm.easecode.common.vo;

import java.util.List;

public class AppClassMethodList extends AppClassMethod {

	private static final long serialVersionUID = -347590755628023015L;
	/** 访问控制修饰符，private，public，protected */
	private String modifier;
	/** 方法名 */
	private String name;
	/** 方法描述 */
	private String desc;
	/** 方法返回类型 */
	private String returnType;
	/** 方法抛出异常类型 */
	private String[] throwsType;
	/** 方法请求参数列表 */
	private List<AppClassMethodParam> params;
	/** 方法体 */
	private Object body;
	
	public AppClassMethodList() {}
	
	public AppClassMethodList(String modifier, String name, String returnType, String desc) {
		this(modifier, name, returnType, desc, null);
	}
	public AppClassMethodList(String modifier, String name, String returnType, String desc, List<AppClassMethodParam> params) {
		this.modifier = modifier;
		this.name = name;
		this.returnType = returnType;
		this.desc = desc;
		this.params = params;
	}
	
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
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
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String[] getThrowsType() {
		return throwsType;
	}
	public void setThrowsType(String[] throwsType) {
		this.throwsType = throwsType;
	}
	public List<AppClassMethodParam> getParams() {
		return params;
	}
	public void setParams(List<AppClassMethodParam> params) {
		this.params = params;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	
}
