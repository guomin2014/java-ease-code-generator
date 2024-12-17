package com.gm.easecode.common.vo;

public class AliasConstants {

	public static final String MODULE_DESC = "ModuleDesc";
	public static final String MODULE_TABLE_NAME = "ModuleTableName";
	public static final String MODULE_TABLE_STRATEGY = "ModuleTableStrategy";
	public static final String MODULE_ENTITY_NAME = "ModuleEntityName";
	public static final String MODULE_ENTITY_FIELD_NAME = "ModuleEntityFieldName";
	public static final String MODULE_ENTITY_FIELD_DESC = "ModuleEntityFieldDesc";
	public static final String MODULE_ENTITY_FIELD_TYPE = "ModuleEntityFieldType";
	public static final String MODULE_QUERY_NAME = "ModuleQueryName";
	public static final String DAO_NAME = "DaoName";
	public static final String DAO_ANNOTATION_NAME = "DaoAnnotationName";
	public static final String SERVICE_NAME = "ServiceName";
	public static final String SERVICE_ANNOTATION_NAME = "ServiceAnnotationName";
	public static final String CONTROLLER_NAME = "ControllerName";
	public static final String CONTROLLER_FORM_NAME = "ControllerFormName";
	public static final String CONTROLLER_DTO_REQ_NAME = "ControllerDtoReqName";
	public static final String CONTROLLER_DTO_REQ_PAGE_NAME = "ControllerDtoReqPageName";
	public static final String CONTROLLER_DTO_RSP_NAME = "ControllerDtoRspName";
	public static final String CONTROLLER_REQUEST_MAPPING_PATH = "ControllerRequestMappingPath";
	public static final String EXCEPTION = "Exception";
	
	
	public static String generalAliasVariable(String aliasName) {
		return "${" + aliasName + "}";
	}
}
