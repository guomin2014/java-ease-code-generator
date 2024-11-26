package com.gm.easecode.source;

/**
 * 数据来源
 * 0:Inner, 1：Mysql，2：word
 * @author	GM
 * @date	2018年9月10日
 */
public enum DataSourceMode
{
	/** 0：Inner */
	INNER(0, "Inner"),
	/** 1：Mysql */
	MYSQL(1, "Mysql"),
	/** 1：WORD */
	WORD(2, "Word"), 
	;

	private int value;
	private String desc;

	DataSourceMode(int value, String desc)
	{
		this.value = value;
		this.desc = desc;
	}

	public static DataSourceMode getByValue(int value)
	{
		for (DataSourceMode status : values())
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
