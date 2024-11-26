package com.gm.easecode.common.vo;

import java.util.Set;

import com.gm.easecode.common.util.StringUtils;

public class AppModule {
	
	public static final String DEFAULT_IDENTIFY = "default";
	public static final String DEFAULT_NAME = "默认分组";
    /** 模块编号 */
    private String num;
	/** 模块名称，如：客户 */
	private String name;
	/** 模块标识，如：customer */
    private String identify;
	/** 是分区分子模块 */
	private boolean subModuleEnable = false;
	/** 分子模块深度 */
	private int subModuleDepth = 1;
	/** 表集合，逗号分隔 */
	private String tables;
	
	private Set<String> tableSet;
	
	public AppModule(int num, String name, String identify, String tables) {
		this(num, name, identify, false, 1, tables);
	}
	
	public AppModule(int num, String name, String identify, boolean subModuleEnable, int subModuleDepth, String tables)
	{
	    this.num = StringUtils.lpad(num, 2);
	    this.name = name;
		this.identify = identify;
		this.subModuleEnable = subModuleEnable;
		this.subModuleDepth = subModuleDepth;
		this.tables = tables;
		this.tableSet = StringUtils.converStr2Set(tables);
	}
	
	public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getIdentify()
	{
		return identify;
	}
	public void setIdentify(String identify)
	{
		this.identify = identify;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean isSubModuleEnable() {
		return subModuleEnable;
	}
	public void setSubModuleEnable(boolean subModuleEnable) {
		this.subModuleEnable = subModuleEnable;
	}
	public int getSubModuleDepth() {
        return subModuleDepth;
    }

    public void setSubModuleDepth(int childDepth) {
        this.subModuleDepth = childDepth;
    }

    public String getTables()
	{
		return tables;
	}
	public void setTables(String tables)
	{
		this.tables = tables;
		this.tableSet = StringUtils.converStr2Set(tables);
	}
	/**
	 * 判断是否有同级的表，比如：wisdom_agriculture_point，wisdom_agriculture_point_machine
	 * @param tableName
	 * @return
	 */
	public boolean hasSameLevel(String tableName) {
	    if (this.tableSet != null) {
	        for (String table : tableSet) {
	            if (table.startsWith(tableName + "_")) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
}
