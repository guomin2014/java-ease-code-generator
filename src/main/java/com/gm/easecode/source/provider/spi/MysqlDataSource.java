package com.gm.easecode.source.provider.spi;

import com.gm.easecode.source.DataSource;
import com.gm.easecode.source.DataSourceMode;

public class MysqlDataSource extends DataSource{

	private final String dbUrl;
	private final String dbUser;
	private final String dbPwd;
	
	public MysqlDataSource(String dbUrl, String dbUser, String dbPwd) {
		super(DataSourceMode.MYSQL, "1.0.0");
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPwd = dbPwd;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPwd() {
		return dbPwd;
	}
}
