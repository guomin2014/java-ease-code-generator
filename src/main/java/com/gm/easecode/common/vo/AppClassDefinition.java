package com.gm.easecode.common.vo;

public class AppClassDefinition {

	/** 别名 */
	private String aliasName;
	/** 主键类型 */
	private String pkType;
	/** controller类代码风格 */
	private ControllerClassStyleMode controllerClassStyle;
	/** 是否是树形结构 */
	private boolean isTree;
	/** 是否分表 */
	private boolean isSubmeter;
	/** 分表策略，0：无，1：按日，2：按周，3：按月，4：按余数，5：按日分余数，6：按周分余数，7：按月分余数，9：其它规则 */
	private int submeterTableStrategy = 0;
	
	public AppClassDefinition(String aliasName) {
		this(aliasName, null, ControllerClassStyleMode.SPRING_MVC);
	}
	
	public AppClassDefinition(String aliasName, String pkType, ControllerClassStyleMode controllerClassStyle) {
		this(aliasName, pkType, controllerClassStyle, false, false, 0);
	}
	
	public AppClassDefinition(String aliasName, String pkType, ControllerClassStyleMode controllerClassStyle, boolean isTree, boolean isSubmeter, int submeterTableStrategy) {
		this.aliasName = aliasName;
		this.pkType = pkType;
		this.controllerClassStyle = controllerClassStyle;
		this.isTree = isTree;
		this.isSubmeter = isSubmeter;
		this.submeterTableStrategy = submeterTableStrategy;
	}

	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public String getPkType() {
		return pkType;
	}
	public void setPkType(String pkType) {
		this.pkType = pkType;
	}
	public boolean isTree() {
		return isTree;
	}
	public void setTree(boolean isTree) {
		this.isTree = isTree;
	}
	public boolean isSubmeter() {
		return isSubmeter;
	}
	public void setSubmeter(boolean isSubmeter) {
		this.isSubmeter = isSubmeter;
	}
	
	public ControllerClassStyleMode getControllerClassStyle() {
		return controllerClassStyle;
	}

	public void setControllerClassStyle(ControllerClassStyleMode controllerClassStyle) {
		this.controllerClassStyle = controllerClassStyle;
	}

	public int getSubmeterTableStrategy() {
		return submeterTableStrategy;
	}

	public void setSubmeterTableStrategy(int submeterTableStrategy) {
		this.submeterTableStrategy = submeterTableStrategy;
	}
	
}
