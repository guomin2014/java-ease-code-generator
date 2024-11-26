package com.gm.easecode.common.vo;

public class TableNameInfo {

	/** 公司名称 */
	private String company;
	/** 应用名称 */
	private String appName;
	/** 模块名 */
	private String moduleName;
	/** 模块包名连接，以.连接 */
	private String modulePackageJoin;
	/** 模块包名 */
	private String[] modulePackages;
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getModulePackageJoin() {
		return modulePackageJoin;
	}
	public void setModulePackageJoin(String modulePackageJoin) {
		this.modulePackageJoin = modulePackageJoin;
	}
	public String[] getModulePackages() {
		return modulePackages;
	}
	public void setModulePackages(String[] modulePackages) {
		this.modulePackages = modulePackages;
	}
}
