package com.gm.easecode.common.vo;

import java.util.concurrent.atomic.AtomicInteger;

import com.gm.easecode.common.util.StringUtils;

public class AppModuleBuilder {

	/** 模块编号 */
    private static final AtomicInteger moduleNum = new AtomicInteger();
	/** 模块名称，如：客户 */
	private String name;
	/** 模块标识，如：customer */
    private String identify;
	/** 是分区分子模块 */
	private Boolean subModuleEnable;
	/** 分子模块深度 */
	private Integer subModuleDepth;
	/** 表集合，逗号分隔 */
	private String tables;
	
	public AppModuleBuilder forName(String name) {
		this.name = name;
		return this;
	}
	public AppModuleBuilder forIdentify(String identify) {
		this.identify = identify;
		return this;
	}
	public AppModuleBuilder forSubModuleEnable(boolean subModuleEnable) {
		this.subModuleEnable = subModuleEnable;
		return this;
	}
	public AppModuleBuilder forSubModuleDepth(int subModuleDepth) {
		this.subModuleDepth = subModuleDepth;
		return this;
	}
	public AppModuleBuilder forTables(String tables) {
		this.tables = tables;
		return this;
	}
	
	public AppModule build() {
		if (StringUtils.isEmpty(identify)) {
			identify = AppModule.DEFAULT_IDENTIFY;
			if (StringUtils.isEmpty(name)) {
				name = AppModule.DEFAULT_NAME;
			}
		}
		int num = moduleNum.incrementAndGet();
		AppModule module = new AppModule(num, name, identify, tables);
		if (subModuleEnable != null) {
			module.setSubModuleEnable(subModuleEnable);
		}
		if (subModuleDepth != null) {
			module.setSubModuleDepth(subModuleDepth);
		}
		return module;
	}
}
