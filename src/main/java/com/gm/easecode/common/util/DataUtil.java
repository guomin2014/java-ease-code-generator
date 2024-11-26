package com.gm.easecode.common.util;

import java.math.BigDecimal;

public class DataUtil
{
	/**
	 * 数据转换
	 * @param value
	 * @return
	 */
	public static double conver2Double(BigDecimal value)
	{
		if(value == null)
		{
			return 0;
		}
		return value.doubleValue();
	}
	public static double conver2Double(Double value)
	{
		if(value == null)
		{
			return 0;
		}
		return value.doubleValue();
	}
	
	public static int conver2Int(Integer value)
	{
		if(value == null)
		{
			return 0;
		}
		return value.intValue();
	}
	public static int conver2Int(Long value)
	{
		if(value == null)
		{
			return 0;
		}
		return value.intValue();
	}
	public static int conver2Int(BigDecimal value)
	{
		if(value == null)
		{
			return 0;
		}
		return value.intValue();
	}
	
	public static int conver2Int(Object value)
	{
		if(value == null || value.toString().trim().length() == 0)
		{
			return 0;
		}
		return Integer.parseInt(value.toString());
	}
	
	public static long conver2Long(Long value)
	{
		if(value == null)
		{
			return 0;
		}
		return value.longValue();
	}
	public static String conver2String(Object value)
	{
		if(value == null)
		{
			return "";
		}
		return value.toString();
	}
	public static Long converObj2Long(Object value)
	{
		if(value == null || value.toString().trim().length() == 0)
		{
			return 0L;
		}
		return Long.parseLong(value.toString());
	}
	public static Double quotient(BigDecimal a,BigDecimal b)
	{
		if(a==null){
			a=new BigDecimal("0");
		}
		if(b==null){
			b=new BigDecimal("1");
		}
		return a.divide(b, 2, BigDecimal.ROUND_HALF_UP).doubleValue()*100;
	}
	
	public static int converStr2Int(String value, int defaultValue)
	{
		if(StringUtils.isEmpty(value))
		{
			return defaultValue;
		}
		try
		{
			return Integer.parseInt(value.trim());
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	public static long converStr2Long(String value, long defaultValue)
	{
		if(StringUtils.isEmpty(value))
		{
			return defaultValue;
		}
		try
		{
			return Long.parseLong(value.trim());
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	public static double converStr2Double(String value, double defaultValue)
	{
		if(StringUtils.isEmpty(value))
		{
			return defaultValue;
		}
		try
		{
			return Double.parseDouble(value.trim());
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	public static boolean converStr2Boolean(String value, boolean defaultValue)
	{
		if(StringUtils.isEmpty(value))
		{
			return defaultValue;
		}
		try
		{
			return Boolean.parseBoolean(value);
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
}
