package com.gm.easecode.source;

public class DataSource {
	
	private final DataSourceMode type;
	
	private final String version;
	
	public DataSource(DataSourceMode type, String version) {
		this.type = type;
		this.version = version;
	}

	public DataSourceMode getType() {
		return type;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return "type=" + type.name() + ", version=" + version;
	}
	
	
}
