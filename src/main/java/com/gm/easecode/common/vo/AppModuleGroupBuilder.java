package com.gm.easecode.common.vo;

import java.util.concurrent.atomic.AtomicInteger;

import com.gm.easecode.common.util.StringUtils;

public class AppModuleGroupBuilder {

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
	
	public AppModuleGroupBuilder forName(String name) {
		this.name = name;
		return this;
	}
	public AppModuleGroupBuilder forIdentify(String identify) {
		this.identify = identify;
		return this;
	}
	public AppModuleGroupBuilder forSubModuleEnable(boolean subModuleEnable) {
		this.subModuleEnable = subModuleEnable;
		return this;
	}
	public AppModuleGroupBuilder forSubModuleDepth(int subModuleDepth) {
		this.subModuleDepth = subModuleDepth;
		return this;
	}
	public AppModuleGroupBuilder forTables(String tables) {
		this.tables = tables;
		return this;
	}
	
	public AppModuleGroup build() {
		if (StringUtils.isEmpty(identify)) {
			identify = AppModuleGroup.DEFAULT_IDENTIFY;
			if (StringUtils.isEmpty(name)) {
				name = AppModuleGroup.DEFAULT_NAME;
			}
		}
		int num = moduleNum.incrementAndGet();
		AppModuleGroup module = new AppModuleGroup(num, name, identify, tables);
		if (subModuleEnable != null) {
			module.setSubModuleEnable(subModuleEnable);
		}
		if (subModuleDepth != null) {
			module.setSubModuleDepth(subModuleDepth);
		}
		return module;
	}
}
