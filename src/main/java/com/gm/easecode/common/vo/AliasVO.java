package com.gm.easecode.common.vo;

import java.io.Serializable;

/**
 * 别名对象
 * @author GM
 * @date 2024-04-30
 */
public class AliasVO implements Serializable {

	private static final long serialVersionUID = -164847705659638131L;
	/** 别名 */
	private String aliasName;
	
	public AliasVO(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
}
