package com.gm.easecode.common.vo;

import java.io.Serializable;
import java.util.List;

public class AppClassConstructor implements Serializable  {

	private static final long serialVersionUID = 9034207075392111632L;
	/** 访问修饰符，public, protected or private */
	private String modifiers;
	/** 参数 */
	private List<AppClassMethodParam> parameters;
	/** 主体 */
	private Object body;
	
	public String getModifiers() {
		return modifiers;
	}
	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}
	public List<AppClassMethodParam> getParameters() {
		return parameters;
	}
	public void setParameters(List<AppClassMethodParam> parameters) {
		this.parameters = parameters;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
}
