package com.gm.easecode.common.vo;

/**
 * 构建配置风格
 * 0：XML，1：Annotation
 * @author	GM
 * @date	2018年9月10日
 */
public enum ConfigStyleMode
{
	/** 0：XML */
	XML(0, "XML"),
	/** 1：Annotation */
	Annotation(1, "Annotation"), 
	;

	private int value;
	private String desc;

	ConfigStyleMode(int value, String desc)
	{
		this.value = value;
		this.desc = desc;
	}

	public static ConfigStyleMode getByValue(int value)
	{
		for (ConfigStyleMode status : values())
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
