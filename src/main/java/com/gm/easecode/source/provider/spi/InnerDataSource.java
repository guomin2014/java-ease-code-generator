package com.gm.easecode.source.provider.spi;

import com.gm.easecode.source.DataSource;
import com.gm.easecode.source.DataSourceMode;

public class InnerDataSource extends DataSource{

	/** 模板路径 */
	private final String modulePath;
	private final String modules;
	private final String tableNamePrefix;
	private final String frameworkName;
	
	public InnerDataSource(String modulePath, String modules, String tableNamePrefix, String frameworkName) {
		super(DataSourceMode.INNER, "1.0.0");
		this.modulePath = modulePath;
		this.modules = modules;
		this.tableNamePrefix = tableNamePrefix;
		this.frameworkName = frameworkName;
	}

	public String getModulePath() {
		return modulePath;
	}

	public String getModules() {
		return modules;
	}

	public String getTableNamePrefix() {
		return tableNamePrefix;
	}

	public String getFrameworkName() {
		return frameworkName;
	}

}
