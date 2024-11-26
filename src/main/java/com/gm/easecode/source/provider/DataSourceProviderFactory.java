package com.gm.easecode.source.provider;

import com.gm.easecode.source.DataSource;
import com.gm.easecode.source.provider.spi.InnerDataSource;
import com.gm.easecode.source.provider.spi.InnerDataSourceProvider;
import com.gm.easecode.source.provider.spi.MysqlDataSource;
import com.gm.easecode.source.provider.spi.MysqlDataSourceProvider;
import com.gm.easecode.source.provider.spi.WordDataSource;
import com.gm.easecode.source.provider.spi.WordDataSourceProvider;

public class DataSourceProviderFactory {
	public static DataSourceProvider createDataSourceProvider(DataSource dataSource) {
		DataSourceProvider provider = null;
		if (dataSource instanceof MysqlDataSource) {
			provider = new MysqlDataSourceProvider();
		} else if (dataSource instanceof WordDataSource) {
			provider = new WordDataSourceProvider();
		} else if (dataSource instanceof InnerDataSource) {
			provider = new InnerDataSourceProvider();
		}
		return provider;
	}
}
