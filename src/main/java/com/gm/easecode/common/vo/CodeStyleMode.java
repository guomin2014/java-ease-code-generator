package com.gm.easecode.common.vo;

/**
 * 编码结构风格
 * 0：普通，1：Maven
 * @author	GM
 * @date	2018年9月10日
 */
public enum CodeStyleMode
{
	/** 0：普通 */
	NORMAL(0, "普通"),
	/** 1：Maven */
	MAVEN(1, "Maven"), 
	;

	private int value;
	private String desc;

	CodeStyleMode(int value, String desc)
	{
		this.value = value;
		this.desc = desc;
	}

	public static CodeStyleMode getByValue(int value)
	{
		for (CodeStyleMode status : values())
		{
			if (status.getValue() == value)
			{
				return status;
			}
		}
		return null;
	}

	public int getValue()
	{
		return this.value;
	}

	public String getDesc()
	{
		return desc;
	}


}
