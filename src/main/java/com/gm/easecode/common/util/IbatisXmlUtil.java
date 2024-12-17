package com.gm.easecode.common.util;

import com.gm.easecode.common.vo.AppTableContext;
import com.gm.easecode.common.vo.AppTable;
import com.gm.easecode.common.vo.AppTableColumn;
import com.gm.easecode.common.vo.AppTableNameSpace;


public class IbatisXmlUtil {
	
	public static String getIbatisXmlContent(AppTableContext context) throws Exception{
		String content = FileUtil.read(context.getConfig().getIbatisXmlTpl(), context.getConfig().getCharset());
		String daoImplQualifiedName = context.getNameParam().getDaoImplPkgName() + "." + context.getNameParam().getDaoImplName();
		content = content.replace("${daoImplQualifiedName}", daoImplQualifiedName);
		content = content.replace("${entityName}", context.getNameParam().getEntityName());
		content = content.replace("${entityNameMap}", context.getNameParam().getEntityName() + "-Map");
		int conditionTabIndex = 2;
		int groupTabIndex = 5;
		CustomStringBuilder sb = new CustomStringBuilder("");
		CustomStringBuilder tableColumns = new CustomStringBuilder("");
		CustomStringBuilder updateColumns = new CustomStringBuilder("");
		CustomStringBuilder updateBatchColumns = new CustomStringBuilder("");
		CustomStringBuilder condition = new CustomStringBuilder("");
		CustomStringBuilder orderCols = new CustomStringBuilder("");
		CustomStringBuilder itemOrderCols = new CustomStringBuilder("");
		CustomStringBuilder group = new CustomStringBuilder("");
		itemOrderCols.append("<choose>");
		group.append("<choose>");
		if(context.getTable().getPriColList().size() == 1){
			condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('id')\">")
			         .newLine().appendTab(conditionTabIndex+1).append("<if test=\"${__condition_ref__}.id != null\">")
			         .newLine().appendTab(conditionTabIndex+2).append("${__condition_type__} a." + context.getTable().getPriColList().get(0).getColumnName() + "=#{${__condition__}.id}")
			         .newLine().appendTab(conditionTabIndex+1).append("</if>")
			         .newLine().appendTab(conditionTabIndex).append("</if>");
		}else{
			//联合主建处理
		}
		boolean hasAuthentication = false;
		for(int i = 0; i < context.getTable().getColumnList().size(); ++i){
			AppTableColumn column = context.getTable().getColumnList().get(i);
			if(column.getJavaPropertyName().equalsIgnoreCase("customerId")){
				hasAuthentication = true;
			}
			String colComment = column.getColumnComment() != null? column.getColumnComment().trim() : "";
			sb.newLine().appendTab(2);
			sb.append("<result property=\"" + column.getJavaPropertyName() + "\" column=\"" + column.getColumnName() + "\" />");
			
			tableColumns.newLine().appendTab(3).append("<if test=\"(data == null) or (data != null and ( colPickMode == 0 and !data.containsKey('" + column.getJavaPropertyName() + "') or colPickMode == 1 and data.containsKey('" + column.getJavaPropertyName() + "')))\">");
			tableColumns.newLine().appendTab(4).append("a." + column.getColumnName() + " as " + column.getColumnName()).append(",");
			tableColumns.newLine().appendTab(3).append("</if>");
			/*if(i < context.getTable().getColumnList().size() - 1){
				tableColumns.append(",");
			}*/
			
			if((context.getTable().getAutoIncrCol() == null 
					|| !context.getTable().getAutoIncrCol().getJavaPropertyName().equalsIgnoreCase(column.getJavaPropertyName()))
					&& !column.getColumnName().equalsIgnoreCase("id") ){
				updateColumns.newLine().appendTab(4).append("<if test=\"(colPickMode==0 and data.containsKey('" + column.getJavaPropertyName() + "')) or (colPickMode==1 and !data.containsKey('" + column.getJavaPropertyName() + "'))\">")
				             .newLine().appendTab(5).append("a." + column.getColumnName() + "=#{data." + column.getJavaPropertyName() + "},")
				             .newLine().appendTab(4).append("</if>");
				
				if(column.getJavaType().equalsIgnoreCase("Integer") ||
						column.getJavaType().equalsIgnoreCase("int") ||
						column.getJavaType().equalsIgnoreCase("Long") ||
						column.getJavaType().equalsIgnoreCase("BigDecimal")){
					updateColumns.newLine().appendTab(4).append("<if test=\"(colPickMode==0 and data.containsKey('" + column.getJavaPropertyName() + "Increment')) or (colPickMode==1 and !data.containsKey('" + column.getJavaPropertyName() + "Increment'))\">")
		             .newLine().appendTab(5).append("a." + column.getColumnName() + "=ifnull(a." + column.getColumnName() + ",0) + #{data." + column.getJavaPropertyName() + "Increment},")
		             .newLine().appendTab(4).append("</if>");
					
					updateBatchColumns.newLine().appendTab(3).append("<trim prefix=\"" + column.getColumnName() + "=(case\" suffix=\"ELSE " + column.getColumnName() + " end),\">")
                    .newLine().appendTab(4).append("<foreach collection=\"data.dataList\" item=\"item\" index=\"index\" separator=\"\" >")
                    .newLine().appendTab(5).append("<choose>")
                    .newLine().appendTab(6).append("<when test=\"(colPickMode==0 and item.containsKey('" + column.getJavaPropertyName() + "')) or (colPickMode==1 and !item.containsKey('" + column.getJavaPropertyName() + "'))\">")
                    .newLine().appendTab(7).append("when a.id=#{item.id} then #{item." + column.getJavaPropertyName() + "}")
                    .newLine().appendTab(6).append("</when>")
                    .newLine().appendTab(6).append("<when test=\"(colPickMode==0 and item.containsKey('" + column.getJavaPropertyName() + "Increment')) or (colPickMode==1 and !item.containsKey('" + column.getJavaPropertyName() + "Increment'))\">")
                    .newLine().appendTab(7).append("when a.id=#{item.id} then ifnull(a." + column.getColumnName() + ",0) + #{item." + column.getJavaPropertyName() + "Increment}")
                    .newLine().appendTab(6).append("</when>")
                    .newLine().appendTab(5).append("</choose>")
                    .newLine().appendTab(4).append("</foreach>")
                    .newLine().appendTab(3).append("</trim>");
				} else {
				    updateBatchColumns.newLine().appendTab(3).append("<trim prefix=\"" + column.getColumnName() + "=(case\" suffix=\"ELSE " + column.getColumnName() + " end),\">")
                    .newLine().appendTab(4).append("<foreach collection=\"data.dataList\" item=\"item\" index=\"index\" separator=\"\" >")
                    .newLine().appendTab(5).append("<if test=\"(colPickMode==0 and item.containsKey('" + column.getJavaPropertyName() + "')) or (colPickMode==1 and !item.containsKey('" + column.getJavaPropertyName() + "'))\">")
                    .newLine().appendTab(6).append("when a.id=#{item.id} then #{item." + column.getJavaPropertyName() + "}")
                    .newLine().appendTab(5).append("</if>")
                    .newLine().appendTab(4).append("</foreach>")
                    .newLine().appendTab(3).append("</trim>");
				}
			}
			//动态查询条件
			String condTmp = "";
			if("String".equalsIgnoreCase(column.getJavaType())){
				condTmp = "and ${__condition_ref__}." + column.getJavaPropertyName() + " != ''";
			}
			//联接符
			String symbol = " = ";
			if("String".equalsIgnoreCase(column.getJavaType())){
				symbol = " like ";
			}
			
			condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "')\">")
	                 .newLine().appendTab(conditionTabIndex+1).append("<if test=\"${__condition_ref__}." + column.getJavaPropertyName() + " != null " + condTmp + "\">")
	                 .newLine().appendTab(conditionTabIndex+2).append("${__condition_type__} a." + column.getColumnName() + symbol + "#{${__condition__}." + column.getJavaPropertyName() + "}")
	                 .newLine().appendTab(conditionTabIndex+1).append("</if>")
	                 .newLine().appendTab(conditionTabIndex+1).append("<if test=\"${__condition_ref__}." + column.getJavaPropertyName() + " == null\">")
	                 .newLine().appendTab(conditionTabIndex+2).append("${__condition_type__} a." + column.getColumnName() + " is null")
	                 .newLine().appendTab(conditionTabIndex+1).append("</if>")
	                 .newLine().appendTab(conditionTabIndex).append("</if>");
			//动态查询条件
			if("integer".equalsIgnoreCase(column.getJavaType())
			    || "int".equalsIgnoreCase(column.getJavaType())
			    || "long".equalsIgnoreCase(column.getJavaType())
			    || "BigDecimal".equalsIgnoreCase(column.getJavaType())){
				condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "List')\">")
				         .newLine().appendTab(conditionTabIndex+1).append(" ${__condition_type__} a." + column.getColumnName() + " in ")
				         .newLine().appendTab(conditionTabIndex+1).append("<foreach collection=\"${__condition_ref__}." + column.getJavaPropertyName() + "List\" open=\"(\" close=\")\" index=\"index\" item=\"item\" separator=\",\">")
				         .newLine().appendTab(conditionTabIndex+2).append("#{item}")
				         .newLine().appendTab(conditionTabIndex+1).append("</foreach>")
				         .newLine().appendTab(conditionTabIndex).append("</if>");
				condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "Start') and ${__condition_ref__}." + column.getJavaPropertyName() + "Start != null\">")
                         .newLine().appendTab(conditionTabIndex+1).append("${__condition_type__} a." + column.getColumnName() + " <![CDATA[ >= ]]> #{${__condition__}." + column.getJavaPropertyName() + "Start}")
                         .newLine().appendTab(conditionTabIndex).append("</if>");
				condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "End') and ${__condition_ref__}." + column.getJavaPropertyName() + "End != null\">")
                         .newLine().appendTab(conditionTabIndex+1).append("${__condition_type__} a." + column.getColumnName() + " <![CDATA[ <= ]]> #{${__condition__}." + column.getJavaPropertyName() + "End}")
                         .newLine().appendTab(conditionTabIndex).append("</if>");
			} else if("date".equalsIgnoreCase(column.getJavaType())){
				condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "Start') and ${__condition_ref__}." + column.getJavaPropertyName() + "Start != null and ${__condition_ref__}." + column.getJavaPropertyName() + "Start!=''\">")
				         .newLine().appendTab(conditionTabIndex+1).append("${__condition_type__} a." + column.getColumnName() + " <![CDATA[ >= ]]> STR_TO_DATE(left(concat(#{${__condition__}." + column.getJavaPropertyName() + "Start},' 00:00:00'),19),'%Y-%m-%d %k:%i:%s')")
				         .newLine().appendTab(conditionTabIndex).append("</if>");
				condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "End') and ${__condition_ref__}." + column.getJavaPropertyName() + "End != null and ${__condition_ref__}." + column.getJavaPropertyName() + "End!=''\">")
		                 .newLine().appendTab(conditionTabIndex+1).append("${__condition_type__} a." + column.getColumnName() + " <![CDATA[ <= ]]> STR_TO_DATE(left(concat(#{${__condition__}." + column.getJavaPropertyName() + "End},' 23:59:59'),19),'%Y-%m-%d %k:%i:%s')")
		                 .newLine().appendTab(conditionTabIndex).append("</if>");
			} else if("string".equalsIgnoreCase(column.getJavaType())) {
			    condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "List')\">")
                        .newLine().appendTab(conditionTabIndex+1).append(" ${__condition_type__} a." + column.getColumnName() + " in ")
                        .newLine().appendTab(conditionTabIndex+1).append("<foreach collection=\"${__condition_ref__}." + column.getJavaPropertyName() + "List\" open=\"(\" close=\")\" index=\"index\" item=\"item\" separator=\",\">")
                        .newLine().appendTab(conditionTabIndex+2).append("#{item}")
                        .newLine().appendTab(conditionTabIndex+1).append("</foreach>")
                        .newLine().appendTab(conditionTabIndex).append("</if>");
			    if (colComment.indexOf("大小匹配") != -1) {
			        condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "Start') and ${__condition_ref__}." + column.getJavaPropertyName() + "Start != null and ${__condition_ref__}." + column.getJavaPropertyName() + "Start!=''\">")
                             .newLine().appendTab(conditionTabIndex+1).append("${__condition_type__} a." + column.getColumnName() + " <![CDATA[ >= ]]> #{${__condition__}." + column.getJavaPropertyName() + "Start}")
                             .newLine().appendTab(conditionTabIndex).append("</if>");
			        condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "End') and ${__condition_ref__}." + column.getJavaPropertyName() + "End != null and ${__condition_ref__}." + column.getJavaPropertyName() + "End!=''\">")
                             .newLine().appendTab(conditionTabIndex+1).append("${__condition_type__} a." + column.getColumnName() + " <![CDATA[ <= ]]> #{${__condition__}." + column.getJavaPropertyName() + "End}")
                             .newLine().appendTab(conditionTabIndex).append("</if>");
			    }
			}
			
			if (colComment.indexOf("前置匹配") != -1) {
			    condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "StartWith') and ${__condition_ref__}." + column.getJavaPropertyName() + "StartWith != null\">")
			             .newLine().appendTab(conditionTabIndex+1).append("${__condition_type__} a." + column.getColumnName() + " like CONCAT(#{${__condition__}." + column.getJavaPropertyName() + "StartWith},'%')")
			             .newLine().appendTab(conditionTabIndex).append("</if>");
			}
			if (colComment.indexOf("后置匹配") != -1) {
			    condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "EndWith') and ${__condition_ref__}." + column.getJavaPropertyName() + "EndWith != null\">")
			    .newLine().appendTab(conditionTabIndex+1).append("${__condition_type__} a." + column.getColumnName() + " like CONCAT('%',#{${__condition__}." + column.getJavaPropertyName() + "EndWith})")
			    .newLine().appendTab(conditionTabIndex).append("</if>");
			}
			if (colComment.indexOf("前后匹配") != -1) {
			    condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "MatchWith') and ${__condition_ref__}." + column.getJavaPropertyName() + "MatchWith != null\">")
			    .newLine().appendTab(conditionTabIndex+1).append("${__condition_type__} a." + column.getColumnName() + " like CONCAT('%',#{${__condition__}." + column.getJavaPropertyName() + "MatchWith},'%')")
			    .newLine().appendTab(conditionTabIndex).append("</if>");
			}
			
			if (colComment.indexOf("或条件匹配") != -1) {
			    if("string".equalsIgnoreCase(column.getJavaType())) {
			        condition.newLine().appendTab(conditionTabIndex).append("<if test=\"${__condition_ref__}.containsKey('" + column.getJavaPropertyName() + "ORList')\">")
                             .newLine().appendTab(conditionTabIndex+1).append(" ${__condition_type__} ")
                             .newLine().appendTab(conditionTabIndex+1).append("<foreach collection=\"${__condition_ref__}." + column.getJavaPropertyName() + "ORList\" open=\"(\" close=\")\" index=\"index\" item=\"item\" separator=\"OR\">")
                             .newLine().appendTab(conditionTabIndex+2).append("a." + column.getColumnName() + " like #{item}")
                             .newLine().appendTab(conditionTabIndex+1).append("</foreach>")
                             .newLine().appendTab(conditionTabIndex).append("</if>");
			    }
			}
			
			itemOrderCols.newLine().appendTab(4).append("<when test='item.colName==\"" + column.getJavaPropertyName() + "\"'>")
			             .newLine().appendTab(5).append("a." + column.getColumnName())
			             .newLine().appendTab(5).append("<if test='item.sortKind != null and \"DESC\".equalsIgnoreCase(item.sortKind)'>").append(" DESC ").append("</if>")
			             .newLine().appendTab(4).append("</when>");
		
			orderCols.newLine().appendTab(4).append("<if test=\"orderCol.containsKey('" + column.getJavaPropertyName() + "')\">")
			         .newLine().appendTab(5).append("a." + column.getColumnName())
			         .newLine().appendTab(5).append("<if test='orderCol." + column.getJavaPropertyName() + " != null and \"DESC\".equalsIgnoreCase(orderCol." + column.getJavaPropertyName() + ")'>").append(" DESC ").append("</if>")
			         .newLine().appendTab(5).append(",")
			         .newLine().appendTab(4).append("</if>");
			group.newLine().appendTab(groupTabIndex+1).append("<when test='item==\"" + column.getJavaPropertyName() + "\"'>").append(column.getColumnName()).append("</when>");
//			     .newLine().appendTab(groupTabIndex+2).append(column.getColumnName())
//                 .newLine().appendTab(groupTabIndex+1).append("</when>");
		}
		
		itemOrderCols.newLine().appendTab(3).append("</choose>");
        group.newLine().appendTab(groupTabIndex).append("</choose>");
		
		content = content.replace("${resultMapCols}",sb.toString());
		content = content.replace("${tableColumns}",tableColumns.toString());
		
		String generatedKey = "";
		if(context.getTable().getAutoIncrCol() != null){
			generatedKey = "useGeneratedKeys=\"true\" keyProperty=\"" + context.getTable().getAutoIncrCol().getColumnName() + "\"";
		}
		content = content.replace("${generatedKey}", generatedKey);
	
		String keyType = "";
		String keyWhere = "";
		
		if(context.getTable().getPriColList().size() == 1){
			keyType = context.getTable().getPriColList().get(0).getJavaType();
			String keyColumn = context.getTable().getPriColList().get(0).getColumnName();
			keyWhere = "a." + keyColumn + "=#{condition.id}";
			content = content.replace("${keyColumn}",keyColumn);
		}else{
			//TODO 联合主健的处理
		}
		
		
		content = content.replace("${keyType}",keyType);
		content = content.replace("${keyWhere}",keyWhere);
		
		String tableName = context.getTable().getTableName();
		String createTableSql = "";
		if(context.getTable().isSubmeterTable())
		{
			tableName = "${tableName}";
			createTableSql = TableUtil.generationCreateTableSql(context.getTable(), 2);
			createTableSql = "<update id=\"createTable\" parameterType=\"paramDto\">\n" + createTableSql + "\n\t</update>";
		}
		content = content.replace("${entityTableName}", tableName);
		content = content.replace("${createTableSql}", createTableSql);
		
		//insertBody
		content = content.replace("${insertBody}",getInsertBody(context.getTable(),context.getNameParam()));
		content = content.replace("${insertBodyField}",getInsertBodyField(context.getTable(),context.getNameParam()));
		content = content.replace("${insertBodyFieldValue}",getInsertBodyFieldValue(context.getTable(),context.getNameParam()));
		//updateColumns
		content = content.replace("${updateColumns}",updateColumns.toString());
		content = content.replace("${updateBatchColumns}",updateBatchColumns.toString());
		
		//AuthenticationTable
		String authenticationTable = "";
		String authenticationWhere = "";
		if(hasAuthentication){
			authenticationTable = "\n		  <include refid=\"com.mortals.Authentication.authenTable\"/>";
			authenticationWhere = "\n				<include refid=\"com.mortals.Authentication.authenCondition\"/>";
		}
		content = content.replace("${AuthenticationTable}", authenticationTable);
		content = content.replace("${AuthenticationWhere}", authenticationWhere);
		
		content = content.replace("${condition}",condition.toString().replace("\n", "\n\t").replace("${__condition__}", "condition").replace("${__condition_ref__}", "condition").replace("${__condition_type__}", "and"));
		content = content.replace("${conditionOrAnd}",condition.toString().replace("\n", "\n\t\t\t\t").replace("${__condition__}", "orCondition").replace("${__condition_ref__}", "orCondition").replace("${__condition_type__}", "and"));
		content = content.replace("${conditionAndOr}",condition.toString().replace("\n", "\n\t\t\t\t").replace("${__condition__}", "andCondition").replace("${__condition_ref__}", "andCondition").replace("${__condition_type__}", "or"));
		content = content.replace("${condition_param}",condition.toString().replace("${__condition__}", "${_conditionParam_}").replace("${__condition_ref__}", "conditionParamRef").replace("${__condition_type__}", "${_conditionType_}"));
		
		content = content.replace("${orderCols}",orderCols.toString());
		content = content.replace("${itemOrderCols}",itemOrderCols.prependTabForAll(2).toString());
		
		content = content.replace("${itemGroup}",group.toString());
		
		return content;
	}
	
	/**
	 * 获取 INSERT 语句sql结点 
	 * @param table
	 * @param nameParam
	 * @return
	 */
	private static String getInsertBody(AppTable table, AppTableNameSpace nameParam){
		CustomStringBuilder sb = new CustomStringBuilder();
		CustomStringBuilder valSb = new CustomStringBuilder("");
		sb.newLine().appendTab(2);
		sb.append("(");
		int iCol = 0;
		int lineColNum = 5;
		AppTableColumn column = null;
		
		for(int i = 0; i < table.getColumnList().size(); ++i){
			column = table.getColumnList().get(i);
			if(column.isAutoIncrement()){
				continue;
			}
			if(iCol > 0){
				sb.append(",");
				valSb.append(",");
				if(iCol%lineColNum==0){
					sb.newLine().appendTab(3);
					valSb.newLine().appendTab(3);
				}
			}
			sb.append(column.getColumnName());
			valSb.append("#{" + column.getJavaPropertyName() + "}");
			iCol ++;
		}
		sb.newLine().appendTab(2);
		sb.append(")").append("VALUES(");
		sb.append(valSb.toString()).append(")");		
		return sb.toString();
	}
	private static String getInsertBodyField(AppTable table, AppTableNameSpace nameParam){
		CustomStringBuilder sb = new CustomStringBuilder();
		sb.newLine().appendTab(2);
		sb.append("(");
		int iCol = 0;
		int lineColNum = 5;
		AppTableColumn column = null;
		
		for(int i = 0; i < table.getColumnList().size(); ++i){
			column = table.getColumnList().get(i);
			if(column.isAutoIncrement()){
				continue;
			}
			if(iCol > 0){
				sb.append(",");
				if(iCol%lineColNum==0){
					sb.newLine().appendTab(3);
				}
			}
			sb.append(column.getColumnName());
			iCol ++;
		}
		sb.newLine().appendTab(2);
		sb.append(")");		
		return sb.toString();
	}
	private static String getInsertBodyFieldValue(AppTable table, AppTableNameSpace nameParam){
		CustomStringBuilder valSb = new CustomStringBuilder("");
		int iCol = 0;
		int lineColNum = 5;
		AppTableColumn column = null;
		
		for(int i = 0; i < table.getColumnList().size(); ++i){
			column = table.getColumnList().get(i);
			if(column.isAutoIncrement()){
				continue;
			}
			if(iCol > 0){
				valSb.append(",");
				if(iCol%lineColNum==0){
					valSb.newLine().appendTab(3);
				}
			}
			valSb.append("#{item." + column.getJavaPropertyName() + "}");
			iCol ++;
		}
		return valSb.toString();
	}
	
}
