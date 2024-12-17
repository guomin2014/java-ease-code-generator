<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"mybatis-3-mapper.dtd">
<mapper namespace="${daoImplQualifiedName}">
    
	<!-- 字段和属性映射 -->
	<resultMap type="${entityName}" id="${entityNameMap}">${resultMapCols}
	</resultMap>
	
	${createTableSql}
	
	<!-- 表所有列 -->
	<sql id="_columns">
		<trim suffixOverrides="," suffix="">${tableColumns}
		</trim>
	</sql>
	<!-- 新增 -->
	<insert id="insert" parameterType="${entityName}" ${generatedKey}>
		insert into ${entityTableName} ${insertBody}
	</insert>
	<!-- 批量新增 -->
	<insert id="insertBatch" parameterType="paramDto" useGeneratedKeys="true" keyProperty="id">
		insert into ${entityTableName} ${insertBodyField}
		VALUES
		<foreach collection="data.dataList" item="item" index="index" separator="," >  
		(${insertBodyFieldValue})
		</foreach>
	</insert>
	<!-- 根据ParamDto更新 -->
	<update id="update" parameterType="paramDto">
		update ${entityTableName} as a
		   set
		   <trim suffixOverrides="," suffix="">${updateColumns}
		    </trim>
		   <trim suffixOverrides="where" suffix="">
			    where
			    <trim prefixOverrides="and" prefix="">
					<include refid="_condition_"/>
			   	</trim>
			</trim>
	</update>
	<!-- 批量更新 -->
	<update id="updateBatch" parameterType="paramDto">
		update ${entityTableName} as a
		<trim prefix="set" suffixOverrides=",">${updateBatchColumns}
        </trim>
        where ${keyColumn} in
		<foreach collection="data.dataList" item="item" index="index" open="(" separator="," close=")">
			#{item.id}
		</foreach>
	</update>
	<!-- 根据主健查询 -->
	<select id="getByKey" parameterType="paramDto" resultMap="${entityNameMap}">
		select <include refid="_columns"/>
		from ${entityTableName} as a
	   where ${keyWhere}
	</select>
	<!-- 根据主健删除 -->
	<delete id="deleteByKey" parameterType="paramDto">
		delete a.* from ${entityTableName} as a where ${keyWhere}
	</delete>
	<!-- 根据主健删除一批，针对单一主健有效 -->
	<delete id="deleteByKeys">
		delete from ${entityTableName} where ${keyColumn} in
		<foreach collection="array" item="item" index="index" open="(" separator="," close=")">
			#{item}
		</foreach>
	</delete>
	<!-- 根据paramDto删除一批 -->
	<delete id="deleteByMap" parameterType="paramDto">
		delete a.* from ${entityTableName} as a
		<trim suffixOverrides="where" suffix="">
			 where 
			<trim prefixOverrides="and" prefix="">
				<include refid="_condition_"/>
			</trim>
		</trim>
	</delete>
	<!-- 获取列表 -->
	<select id="getList" parameterType="paramDto" resultMap="${entityNameMap}">
		select <include refid="_columns"/>
		  from ${entityTableName} as a
		 <trim suffixOverrides="where" suffix="">
			 where 
			 <trim prefixOverrides="and" prefix="">
			 	<include refid="_condition_"/>
			 </trim>
		 </trim>
		 <include refid="_orderCols_"/>
	</select>
	<!-- 获取 -->
	<select id="getListCount" parameterType="paramDto" resultType="int">
		select count(1)
		  from ${entityTableName} as a
		 <trim suffixOverrides="where" suffix="">
			 where 
			 <trim prefixOverrides="and" prefix="">
				<include refid="_condition_"/>
			</trim>
		</trim>
	</select>
	
	<!-- 获取分组列表 -->
	<select id="getStatList" parameterType="paramDto" resultMap="${entityNameMap}">
		SELECT
		<include refid="_group_columns_"/>
		FROM ${entityTableName} AS a
		 <trim suffixOverrides="where" suffix="">
			 WHERE 
			 <trim prefixOverrides="and" prefix="">
			 	<include refid="_condition_"/>
			 </trim>
		 </trim>
		 <include refid="_group_by_"/>
		 <include refid="_orderCols_"/>
	</select>
	<!-- 获取分组列表 -->
	<select id="getStatListCount" parameterType="paramDto" resultType="int">
		SELECT count(1)
		FROM ${entityTableName} AS a
		 <trim suffixOverrides="where" suffix="">
			 WHERE 
			 <trim prefixOverrides="and" prefix="">
			 	<include refid="_condition_"/>
			 </trim>
		 </trim>
		 <include refid="_group_by_"/>
	</select>
	
	<!-- 条件映射 -->
	<sql id="_condition_">
		<if test="condition != null and !condition.isEmpty()">
			<!-- 条件映射-普通条件 -->
			${condition}
			<!-- 条件映射-集合之间使用AND，集合中元素使用OR-(list[0].1 or list[0].2) and (list[1].3 or list[1].4) -->
			<if test="condition.containsKey('andConditionList') and !condition.andConditionList.isEmpty()">
				and
				<foreach collection="condition.andConditionList" open="(" close=")" index="index" item="andCondition" separator=" and ">
					<trim prefixOverrides="or" prefix="(" suffix=")">
						${conditionAndOr}
					</trim>
				</foreach>
			</if>
			<!-- 条件映射-集合之间使用OR，集合中元素使用AND-(list[0].1 and list[0].2) or (list[1].3 and list[1].4) -->
			<if test="condition.containsKey('orConditionList') and !condition.orConditionList.isEmpty()">
				and
				<foreach collection="condition.orConditionList" open="(" close=")" index="index" item="orCondition" separator=" or ">
					<trim prefixOverrides="and" prefix="(" suffix=")">
						${conditionOrAnd}
					</trim>
				</foreach>
			</if>
		</if>
	</sql>
	
	<sql id="_orderCols_">
		<if test="orderColList != null and !orderColList.isEmpty()">
			order by
			<trim suffixOverrides=","  suffix="">
				<foreach collection="orderColList" open="" close="" index="index" item="item" separator=",">
					${itemOrderCols}
				</foreach>
			</trim>
		</if>
		<if test="(orderColList == null or orderColList.isEmpty()) and orderCol != null and !orderCol.isEmpty()">
			order by
			<trim suffixOverrides=","  suffix="">${orderCols}
			</trim>
		</if>
	</sql>
	<sql id="_group_columns_">
		<if test="groupList != null and !groupList.isEmpty()">
			<trim suffixOverrides=","  suffix="">
				<foreach collection="groupList" open="" close="" index="index" item="item" separator=",">
					${itemGroup}
				</foreach>
			</trim>
		</if>
	</sql>
	<sql id="_group_by_">
		<if test="groupList != null and !groupList.isEmpty()">
			GROUP BY
			<include refid="_group_columns_"/>
		</if>
	</sql>
</mapper>
