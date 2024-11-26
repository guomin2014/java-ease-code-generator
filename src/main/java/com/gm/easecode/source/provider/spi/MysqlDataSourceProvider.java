package com.gm.easecode.source.provider.spi;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gm.easecode.common.AppException;
import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.vo.AppTable;
import com.gm.easecode.common.vo.AppTableColumn;
import com.gm.easecode.common.vo.FieldVO;
import com.gm.easecode.message.MessageEntity;
import com.gm.easecode.message.MsgCallback;
import com.gm.easecode.source.DataSource;
import com.gm.easecode.source.provider.AbstractDataSourceProvider;

public class MysqlDataSourceProvider extends AbstractDataSourceProvider {

	private static final String driver = "com.mysql.cj.jdbc.Driver";
	
	@Override
	public List<AppTable> findTable(DataSource dataSource, MsgCallback callback) {
		if (!(dataSource instanceof MysqlDataSource)) {
			callback.notifyMsg(new MessageEntity("Mysql数据源不支持的配置[" + dataSource.getClass().getName() + "]"));
			return null;
		}
		callback.notifyMsg(new MessageEntity("开始从数据源[" + dataSource.getType().name() + "]获取表信息..."));
		MysqlDataSource source = (MysqlDataSource) dataSource;
		if (StringUtils.isEmpty(source.getDbUrl())) {
            throw new AppException("数据库连接地址为空");
        }
        if (StringUtils.isEmpty(source.getDbUser())) {
            throw new AppException("数据库访问用户为空");
        }
		Map<String, AppTable> map = readDB(source.getDbUrl(), source.getDbUser(), source.getDbPwd(), callback);
		List<AppTable> retList = new ArrayList<AppTable>(map.values());
		Collections.sort(retList, new Comparator<AppTable>() {
			@Override
			public int compare(AppTable o1, AppTable o2) {
				return o1.getTableName().compareTo(o2.getTableName());
			}
		});
		return retList;
	}
	
    /**
     * 获取Mysql连接
     * @param dbUrl
     * @param dbUser
     * @param dbPwd
     * @return
     */
    public Connection getConnection(String dbUrl, String dbUser, String dbPwd) {
        try {
            Class.forName(driver);// 加载驱动程序
            Connection conn = (Connection) DriverManager.getConnection(dbUrl, dbUser, dbPwd);// 连续数据库
            if(!conn.isClosed()) {
                return conn;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 从数据库读取表信息
	 * @param dbUrl
	 * @param dbUser
	 * @param dbPwd
	 * @param callback
	 * @return
	 */
	public Map<String, AppTable> readDB(String dbUrl, String dbUser, String dbPwd, MsgCallback callback)
    {
	    Map<String, AppTable> retMap = new LinkedHashMap<String, AppTable>();
	    Connection conn = null;
	    Statement stmt = null;
        try
        {
            if (StringUtils.isEmpty(dbUrl)) {
                throw new AppException("数据库连接地址为空");
            }
            if (StringUtils.isEmpty(dbUser)) {
                throw new AppException("数据库访问用户为空");
            }
            conn = this.getConnection(dbUrl, dbUser, dbPwd);
            if (conn == null) {
                throw new AppException("数据库连接信息错误，不能连接");
            }
            try {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet tableRet = meta.getTables(null, "%", "%",new String[]{"TABLE"});
                List<String> tableList = new ArrayList<>();
                Map<String, String> tableRemarkMap = new HashMap<>();
                while(tableRet.next()) {
                    String tableName = tableRet.getString("TABLE_NAME");//表名
                    String remark = tableRet.getString("REMARKS");//表描述，有的mysql版本取不到值
                    tableList.add(tableName);
                    tableRemarkMap.put(tableName, remark);
                }
                tableRet.close();
                stmt = conn.createStatement();
                //部份mysql版本取不到值，使用下面方式取值
                for (String table : tableList) {
                	if (table.equalsIgnoreCase("sys_config")) {
                		continue;
                	}
                    String tableRemark = tableRemarkMap.get(table);
                    if (StringUtils.isEmpty(tableRemark)) {
                        ResultSet tableRs = null;
                        try {
                            tableRs = stmt.executeQuery("SHOW CREATE TABLE " + table);
                            if (tableRs != null && tableRs.next()) {
                                String createTableSql = tableRs.getString(2);
                                tableRemark = getTableComment(createTableSql);
                            }
                        } finally {
                            if (tableRs != null) {
                                tableRs.close();
                            }
                        }
                    }
                    if(callback != null)
                    {
                        callback.notifyMsg(new MessageEntity("开始解析表格：" + tableRemark + "-->" + table));
                    }
                    List<AppTableColumn> columnList = new ArrayList<AppTableColumn>();
                 // 获取表的主键名字
                    ResultSet pkInfo = meta.getPrimaryKeys(null, "%", table);
                    Set<String> primaryKeys = new HashSet<>();
                    while (pkInfo.next()){
//                        System.out.print("数据库名称:"+pkInfo.getString("TABLE_CAT")+"                  ");
//                        System.out.print("表名称:"+pkInfo.getString("TABLE_NAME")+"                  ");
//                        System.out.print("主键列的名称:"+pkInfo.getString("COLUMN_NAME")+"                  ");
//                        System.out.print("类型:"+pkInfo.getString("PK_NAME")+"                  ");
//                        System.out.println("");
                        primaryKeys.add(pkInfo.getString("COLUMN_NAME"));
                        break;
                    }
                    pkInfo.close();
                    ResultSet colRet = meta.getColumns(null, "%", table, "%");
                    while(colRet.next()) { 
//                        String tableName = colRet.getString("TABLE_NAME"); //表名
                        String columnName = colRet.getString("COLUMN_NAME"); //列名
                        String columnDefault = colRet.getString("COLUMN_DEF"); //默认值
                        String columnType = colRet.getString("TYPE_NAME"); //字段的类型
                        int nullable = colRet.getInt("NULLABLE"); //是否可以为空,0:不能为空，1：为空
//                        String isNull = colRet.getString("IS_NULLABLE"); //是否可以为空,YES/NO
                        String isAutoIncr = colRet.getString("IS_AUTOINCREMENT"); //是否为自增,YES/NO
                        String remark = colRet.getString("REMARKS"); //字段说明
                        int datasize = colRet.getInt("COLUMN_SIZE"); //长度
                        int digits = colRet.getInt("DECIMAL_DIGITS"); //小数长度
//                        boolean isRequestField = nullable == 0;
                        String columnRequired = nullable == 0 ? "是" : "否";
                        String dbColumnType = columnType;
                        if ("double".equalsIgnoreCase(columnType) || "float".equalsIgnoreCase(columnType) || "decimal".equalsIgnoreCase(columnType)) {
                            dbColumnType = columnType + "(" + datasize + "," + digits + ")";
                        } else if ("datetime".equalsIgnoreCase(columnType) || "text".equalsIgnoreCase(columnType) || "longtext".equalsIgnoreCase(columnType)) {
                            dbColumnType = columnType;
                        } else {
                            if ("bigint".equalsIgnoreCase(columnType) || "tinyint".equalsIgnoreCase(columnType) || "smallint".equalsIgnoreCase(columnType) || "int".equalsIgnoreCase(columnType)) {
                                datasize++;
                            }
                            dbColumnType = columnType + "(" + datasize + ")";
                        }
                        String columnDesc = remark;
                        String columnComment = null;
                        if (StringUtils.isNotEmpty(remark)) {
                        	int index = remark.indexOf("，");
                        	if (index == -1) {
                        		index = remark.indexOf(",");
                        	}
                        	if (index > 0) {
                        		columnDesc = remark.substring(0, index);
                        		columnComment = remark.substring(index + 1);
                        	}
                        }
                        FieldVO fieldInfo = new FieldVO(columnName, columnDesc, dbColumnType, columnComment, columnDefault, isAutoIncr, columnRequired);
                        AppTableColumn column = convertTableColumn(fieldInfo);
                        columnList.add(column);
                    }
                    colRet.close();
                    AppTable realTable = convertTable(table, tableRemark, false, false, columnList);
                    retMap.put(table, realTable);
                }
            } catch (Exception e) {
//                log.error("获取表结构信息异常", e);
                e.printStackTrace();
                if(callback != null)
                {
                     callback.notifyMsg(new MessageEntity("获取表结构信息异常"));
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            if(callback != null)
            {
                 callback.notifyMsg(new MessageEntity(e.getMessage()));
            }
        }
        finally
        {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {}
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {}
            }
        }
        return retMap;
    }
	
	public String getTableComment(String createTableSql) {
        String comment = null;
        int index = createTableSql.indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = createTableSql.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1);
        try {
            comment = new String(comment.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comment;
    }
}
