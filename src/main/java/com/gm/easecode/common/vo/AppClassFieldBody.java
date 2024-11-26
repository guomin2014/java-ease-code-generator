package com.gm.easecode.common.vo;

/**
 * 类字段（主体模式，即多个字段以body方式存储在一起）
 * @author GM
 * @date 2024-04-28
 */
public class AppClassFieldBody extends AppClassField {

	private static final long serialVersionUID = 9074354342945521261L;
	/** 主体 */
	private String body;
	
	public AppClassFieldBody() {}
	
	public AppClassFieldBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
}
