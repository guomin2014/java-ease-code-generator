package com.gm.easecode.common.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.gm.easecode.common.util.TableUtil;

public class AppTable {
	/** 原始表名称 */
	private String oldTableName = null;
	/** 表名 */
	private String tableName = null;
	/** 分表名称 */
	private String submeterTableName = null;
	/** 分表策略，0：无，1：按日，2：按周，3：按月，4：按余数，5：按日分余数，6：按周分余数，7：按月分余数，9：其它规则 */
	private int submeterTableStrategy = 0;
	
	/** 表别名 */
	private String aliasName = null;
	
	/** 注释 */
	private String comment = "";
	
	/** 列信息 */
	private List<AppTableColumn> columnList = null;
	
	/** 主健列信息 */
	private List<AppTableColumn> priColList = null;
	
	/** 是否自增长主健 */
	private boolean isAutoIncrKey = false;
	
	/** 自增长列 */
	private AppTableColumn autoIncrCol = null;
	
	/** 代表类别表 */
	private List<String> codeKindList = new ArrayList<String>();
	
	/** 列表字段 */
	private List<String> listFields = new ArrayList<String>();
	/** 编辑字段 */
	private List<String> editFields = new ArrayList<String>();
	/** 查询字段 */
	private List<String> queryFields = new ArrayList<String>();
	/** 必填字段 */
	private List<String> requestFields = new ArrayList<String>();
	/** 是否是分表 */
	private boolean isSubmeterTable = false;
	/** 是否是树级表 */
	private boolean isTreeTable = false;
	/** 是否是内置表 */
	private boolean isInnerTable = false;
	/** 内置类列表,key：dao、service，value：方法列表 */
	private Map<String, AppClass> innerClassMap = new HashMap<>();
	
	public AppTable() {
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
		aliasName = TableUtil.getTableAliasName(tableName);
	}

	public String getAliasName() {
		return aliasName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<AppTableColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<AppTableColumn> columnList) {
		this.columnList = columnList;
	}

	public int getPriNum() {
		return priColList.size();
	}

	public List<AppTableColumn> getPriColList() {
		return priColList;
	}

	public void setPriColList(List<AppTableColumn> priColList) {
		this.priColList = priColList;
	}

	public boolean isAutoIncrKey() {
		return isAutoIncrKey;
	}

	public void setAutoIncrKey(boolean isAutoIncrKey) {
		this.isAutoIncrKey = isAutoIncrKey;
	}

	public AppTableColumn getAutoIncrCol() {
		return autoIncrCol;
	}

	public void setAutoIncrCol(AppTableColumn autoIncrCol) {
		this.autoIncrCol = autoIncrCol;
	}
	/**
	 * 是否列全为主健（这种情况为关联表）
	 * @return
	 */
	public boolean isColAllPri(){
		return priColList.size() == columnList.size();
	}
	public boolean isAutoIncrCol(AppTableColumn column){
		if(autoIncrCol == null){
			return false;
		}
		return autoIncrCol.getJavaPropertyName().equals(column.getJavaPropertyName());
	}
	/**
	 * 是否主健
	 * @param column
	 * @return
	 */
	public boolean isPriCol(AppTableColumn column){
		boolean bRet = false;
		for(int i = 0; i < priColList.size(); ++i){
			AppTableColumn currCol = priColList.get(i);
			if(currCol.getJavaPropertyName().equals(column.getJavaPropertyName())){
				bRet = true;
				break;
			}
		}
		return bRet;
	}
	public boolean isCodeKind(String columnName){
		boolean bRet = false;
		if(codeKindList != null&& columnName != null){
			for(String codeKindName:codeKindList){
				if(columnName.equalsIgnoreCase(codeKindName)){
					bRet = true;
					break;
				}
			}
		}
		return bRet;
	}
	public List<String> getCodeKindList() {
		return codeKindList;
	}

	public void setCodeKindList(List<String> codeKindList) {
		this.codeKindList = codeKindList;
	}

	/**
	 * @return the listFields
	 */
	public List<String> getListFields() {
		return listFields;
	}

	/**
	 * @param listFields the listFields to set
	 */
	public void setListFields(List<String> listFields) {
		this.listFields = listFields;
	}

	/**
	 * @return the editFields
	 */
	public List<String> getEditFields() {
		return editFields;
	}

	/**
	 * @param editFields the editFields to set
	 */
	public void setEditFields(List<String> editFields) {
		this.editFields = editFields;
	}

	/**
	 * @return the queryFields
	 */
	public List<String> getQueryFields() {
		return queryFields;
	}

	/**
	 * @param queryFields the queryFields to set
	 */
	public void setQueryFields(List<String> queryFields) {
		this.queryFields = queryFields;
	}

	/**
	 * @return the requestFields
	 */
	public List<String> getRequestFields() {
		return requestFields;
	}

	/**
	 * @param requestFields the requestFields to set
	 */
	public void setRequestFields(List<String> requestFields) {
		this.requestFields = requestFields;
	}

	public boolean isSubmeterTable()
	{
		return isSubmeterTable;
	}

	public void setIsSubmeterTable(boolean isSubmeterTable)
	{
		this.isSubmeterTable = isSubmeterTable;
		if(isSubmeterTable)
		{
			String oldName = oldTableName;
			if(!StringUtils.isEmpty(oldName))
			{
			    if (Pattern.compile("_[y|Y]{1,4}[m|M]{1,2}[d|D]{1,2}").matcher(oldName).find()) {
			        this.submeterTableStrategy = 1;
			    } else if (Pattern.compile("_[y|Y]{1,4}[w|W]{1,2}").matcher(oldName).find()) {
                    this.submeterTableStrategy = 2;
                } else if (Pattern.compile("_[y|Y]{1,4}[m|M]{1,2}").matcher(oldName).find()) {
                    this.submeterTableStrategy = 3;
                } 
//                else if (StringUtils.isContainChinese(oldName)) {
//                    this.submeterTableStrategy = 4;
//                }
//			    if (StringUtils.isContainChinese(oldName) && this.submeterTableStrategy != 4) {
//			        this.submeterTableStrategy += 4;
//			    }
				oldName = oldName.replaceAll("_[y|Y]{1,4}[m|M]{1,2}[d|D]{1,2}", "_\\$\\{unionTime\\}");
				oldName = oldName.replaceAll("_[y|Y]{1,4}[m|M]{1,2}", "_\\$\\{unionTime\\}");
				oldName = oldName.replaceAll("_[y|Y]{2,4}", "_\\$\\{unionTime\\}");
				StringBuffer sb = new StringBuffer();
				String[] arr = oldName.split("_");
				for(int i = 0; i < arr.length; ++i)
				{
					String name = arr[i];
//					if(StringUtils.isContainChinese(arr[i]))
//					{
//						name = "${unionId}";
//					}
					if(i == 2)
					{
						sb.append("zd_");
					}
					sb.append(name).append("_");
				}
				if(sb.length() > 0)
				{
					this.submeterTableName = sb.substring(0, sb.length() - 1);
				}
				else
				{
					this.submeterTableName = sb.toString();
				}
			}
		}
		else
		{
			this.submeterTableName = this.tableName;
		}
	}

	public String getOldTableName()
	{
		return oldTableName;
	}

	public void setOldTableName(String oldTableName)
	{
		this.oldTableName = oldTableName;
	}

	public String getSubmeterTableName()
	{
		return submeterTableName;
	}

	public void setSubmeterTableName(String submeterTableName)
	{
		this.submeterTableName = submeterTableName;
	}

    public int getSubmeterTableStrategy() {
        return submeterTableStrategy;
    }

    public void setSubmeterTableStrategy(int submeterTableStrategy) {
        this.submeterTableStrategy = submeterTableStrategy;
    }

	public boolean isTreeTable() {
		return isTreeTable;
	}

	public void setTreeTable(boolean isTreeTable) {
		this.isTreeTable = isTreeTable;
	}

	public boolean isInnerTable() {
		return isInnerTable;
	}

	public void setInnerTable(boolean isInnerTable) {
		this.isInnerTable = isInnerTable;
	}
	
	public void addInnerClass(String key, AppClass appClass) {
		this.innerClassMap.put(key, appClass);
	}
	
	public AppClass getInnerClass(String key) {
		return this.innerClassMap.get(key);
	}
}