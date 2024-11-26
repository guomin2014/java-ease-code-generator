package com.gm.easecode.source.provider.spi;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.gm.easecode.common.util.StringUtils;
import com.gm.easecode.common.vo.AppTable;
import com.gm.easecode.common.vo.AppTableColumn;
import com.gm.easecode.common.vo.FieldVO;
import com.gm.easecode.message.MessageEntity;
import com.gm.easecode.message.MsgCallback;
import com.gm.easecode.source.DataSource;
import com.gm.easecode.source.provider.AbstractDataSourceProvider;

public class WordDataSourceProvider extends AbstractDataSourceProvider {

	@Override
	public List<AppTable> findTable(DataSource dataSource, MsgCallback callback) {
		if (!(dataSource instanceof WordDataSource)) {
			callback.notifyMsg(new MessageEntity("Word数据源不支持的配置[" + dataSource.getClass().getName() + "]"));
			return null;
		}
		callback.notifyMsg(new MessageEntity("开始从数据源[" + dataSource.getType().name() + "]获取表信息..."));
		WordDataSource source = (WordDataSource) dataSource;
		if (StringUtils.isEmpty(source.getFilePath())) {
			callback.notifyMsg(new MessageEntity("Word数据源未配置文件路径"));
			return null;
		}
		Map<String, AppTable> retMap = this.readWord(source.getFilePath(), callback);
		List<AppTable> retList = new ArrayList<AppTable>(retMap.values());
		Collections.sort(retList, new Comparator<AppTable>(){
			@Override
			public int compare(AppTable o1, AppTable o2)
			{
				return o1.getTableName().compareTo(o2.getTableName());
			}
		});
		return retList;
	}
	
	/**
	 * 从Word读取表信息
	 * @param filePath
	 * @param callback
	 * @return
	 */
	public Map<String, AppTable> readWord(String filePath, MsgCallback callback) {
		Map<String, AppTable> retMap = new LinkedHashMap<String, AppTable>();
		String titleFieldName = "字段";
		String titleFieldDesc = "字段名称";
		String titleFieldFill = "必填";
		String titleFieldType = "字段类型";
		String titleFieldComment = "备注";
		String titleFieldForList = "列表";
		String titleFieldForEdit = "编辑";
		String titleFieldForQuery = "查询";
		InputStream is = null;
		try {
			if (callback != null) {
				callback.notifyMsg(new MessageEntity("开始解析文档-->" + filePath));
			}
			is = new FileInputStream(filePath);
			XWPFDocument document = new XWPFDocument(is);
			List<XWPFTable> tables = document.getTables();
			List<XWPFTableRow> rows = null;
			List<XWPFTableCell> cells = null;
			for (XWPFTable table : tables) {
				int indexFieldName = -1;
				int indexFieldDesc = -1;
				int indexFieldRequired = -1;
				int indexFieldType = -1;
				int indexFieldComment = -1;
				int indexFieldForList = -1;
				int indexFieldForEdit = -1;
				int indexFieldForQuery = -1;
				// 获取表格对应的行
				rows = table.getRows();
				if (rows == null || rows.isEmpty() || rows.size() < 2) {
					continue;
				}
				String tableName = "";
				String tableDesc = "";

				// 解析表格的头信息【数据库表的名称与说明】
				XWPFTableRow tableTitleRow = rows.get(0);
				XWPFTableCell tableTitleCell = tableTitleRow.getCell(0);
				String tableTitle = tableTitleCell.getText();
				if (isDelCell(tableTitleCell)) {
					if (callback != null) {
						callback.notifyMsg(new MessageEntity("已删除的表格：" + tableTitle));
					}
					continue;
				}
				String[] tab = analysisTable(tableTitle);
				tableName = tab[0];
				tableDesc = tab[1];
				if (StringUtils.isEmpty(tableName)) {
					if (callback != null) {
						callback.notifyMsg(new MessageEntity("不符合规则的表格：" + tableTitle));
					}
					continue;
				} else {
					tableName = tableName.replaceAll(" ", "");
				}
				if (callback != null) {
					callback.notifyMsg(new MessageEntity("开始解析表格：" + tableDesc + "-->" + tableName));
				}
				// 解析表格中数据与创建数据库表的字段对应关系
				cells = rows.get(1).getTableCells();
				for (int i = 0; i < cells.size(); i++) {
					XWPFTableCell cell = cells.get(i);
					String text = StringUtils.trim(cell.getText());
					if (text.equalsIgnoreCase(titleFieldName)) {
						indexFieldName = i;
					} else if (text.equalsIgnoreCase(titleFieldDesc)) {
						indexFieldDesc = i;
					} else if (text.equalsIgnoreCase(titleFieldFill)) {
						indexFieldRequired = i;
					} else if (text.equalsIgnoreCase(titleFieldType)) {
						indexFieldType = i;
					} else if (text.equalsIgnoreCase(titleFieldComment)) {
						indexFieldComment = i;
					} else if (text.equalsIgnoreCase(titleFieldForList)) {
						indexFieldForList = i;
					} else if (text.equalsIgnoreCase(titleFieldForEdit)) {
						indexFieldForEdit = i;
					} else if (text.equalsIgnoreCase(titleFieldForQuery)) {
						indexFieldForQuery = i;
					}
				}
				if (indexFieldName == -1 || indexFieldType == -1) {
					if (callback != null) {
						callback.notifyMsg(new MessageEntity("解析表格出错，未找到表格对应列信息：" + tableName));
					}
					continue;
				}
				boolean needCreate = true;
				List<AppTableColumn> columnList = new ArrayList<>();
				for (int i = 2; i < rows.size(); i++) {
					XWPFTableRow row = rows.get(i);
					// 获取行对应的单元格
					cells = row.getTableCells();
					if (isDelCell(cells.get(indexFieldName))) {
						continue;
					}
					String fieldName = indexFieldName >= 0 ? StringUtils.trim(cells.get(indexFieldName).getText()) : "";
					String fieldDesc = indexFieldDesc >= 0 ? StringUtils.trim(cells.get(indexFieldDesc).getText()) : "";
					String fieldType = indexFieldType >= 0 ? StringUtils.trim(cells.get(indexFieldType).getText()) : "";
					String fieldRequired = indexFieldRequired >= 0 ? StringUtils.trim(cells.get(indexFieldRequired).getText()) : "";
					String fieldComment = indexFieldComment >= 0 ? StringUtils.trim(cells.get(indexFieldComment).getText()) : "";
					String fieldForList = indexFieldForList >= 0 ? StringUtils.trim(cells.get(indexFieldForList).getText()) : "";
					String fieldForEdit = indexFieldForEdit >= 0 ? StringUtils.trim(cells.get(indexFieldForEdit).getText()) : "";
					String fieldForQuery = indexFieldForQuery >= 0 ? StringUtils.trim(cells.get(indexFieldForQuery).getText()) : "";
					if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(fieldType)) {
						if (callback != null) {
							callback.notifyMsg(new MessageEntity("表格解析失败，字段或字段类型值为空。-->" + tableDesc + "-->" + tableName));
						}
						needCreate = false;
						break;
					}
					fieldType = fieldType.replaceAll(" ", "");
					fieldComment = fieldComment.replace("\n", " ");
					try {
						FieldVO fieldInfo = new FieldVO(fieldName, fieldDesc, fieldType, fieldComment, null, fieldForList, fieldForEdit, fieldForQuery, null, fieldRequired);
						AppTableColumn column = convertTableColumn(fieldInfo);
						columnList.add(column);
					} catch (Exception e) {
						if (callback != null) {
							callback.notifyMsg(new MessageEntity("表格解析失败[" + tableDesc + "][" + tableName + "]-->" + e.getMessage()));
						}
						needCreate = false;
						break;
					}
				}
				if (!StringUtils.isEmpty(tableName) && needCreate) {
					AppTable realTable = convertTable(tableName, tableDesc, false, false, columnList);
					retMap.put(realTable.getTableName(), realTable);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		return retMap;
	}
	
	public String[] analysisTable(String str)
	{
		String[] ret = new String[2];
		String regex = "(.*)[,|，]表名[:|：](.*)";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
        if( matcher.find()) {
        	String tableDesc = matcher.group(1);
            String tableName = matcher.group(2);
            ret[0] = tableName;
            ret[1] = tableDesc;
        }
        return ret;
	}
	/**
	 * 是否标识删除
	 * @param cell
	 * @return
	 */
	private boolean isDelCell(XWPFTableCell cell) {
	    if (cell == null) {
	        return true;
	    }
	    List<XWPFParagraph> paragraphs = cell.getParagraphs();
	    if (paragraphs == null || paragraphs.isEmpty()) {
	        return false;
	    }
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            if (runs != null && runs.size() > 0) {
                for (XWPFRun run : runs) {
                    if (run.isStrike()) {
                        return true;
                    }
                }
            }
        }
        return false;
	}
	
}
