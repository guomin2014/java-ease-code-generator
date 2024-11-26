package com.gm.easecode.source.provider;

import java.util.List;

import com.gm.easecode.common.vo.AppTable;
import com.gm.easecode.message.MsgCallback;
import com.gm.easecode.source.DataSource;

public interface DataSourceProvider {

	/**
	 * 获取表信息列表
	 * @param dataSource
	 * @param callback
	 * @return
	 */
	List<AppTable> findTable(DataSource dataSource, MsgCallback callback);
}
