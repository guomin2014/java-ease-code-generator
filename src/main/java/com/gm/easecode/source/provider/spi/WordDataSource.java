package com.gm.easecode.source.provider.spi;

import com.gm.easecode.source.DataSource;
import com.gm.easecode.source.DataSourceMode;

public class WordDataSource extends DataSource{

	/** word文件路径 */
	private final String filePath;
	
	public WordDataSource(String filePath) {
		super(DataSourceMode.WORD, "1.0.0");
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

}
