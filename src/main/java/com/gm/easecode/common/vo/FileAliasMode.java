package com.gm.easecode.common.vo;

/**
 * 文件别名
 * @author GM
 * @date 2024-04-30
 */
public enum FileAliasMode {
	Entity(1, "实体对象"),
	EntityKey(2, "实体Key对象"),
	EntityXml(3, "实体XML对象"),
	Query(4, "查询对象"),
	Form(5, "Form对象"),
	RequestDto(6, "Web查询对象"),
	RequestPageDto(7, "Web分页查询对象"),
	ResponseDto(8, "Web查询响应对象"),
	Dao(9, "Dao对象"),
	DaoImpl(10, "Dao实现对象"),
	Service(11, "Service对象"),
	ServiceImpl(12, "Service实现对象"),
	Controller(13, "Controller对象"), 
	Bootstrap(14, "Bootstrap对象"), 
	;

	private int value;
	private String desc;

	FileAliasMode(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return this.value;
	}

	public String getDesc() {
		return desc;
	}

	public static FileAliasMode getByValue(int value) {
		for (FileAliasMode status : values()) {
			if (status.getValue() == value) {
				return status;
			}
		}
		return null;
	}
	
	public static FileAliasMode getByName(String name) {
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		for (FileAliasMode status : values()) {
			if (status.name().equalsIgnoreCase(name)) {
				return status;
			}
		}
		return null;
	}

}
