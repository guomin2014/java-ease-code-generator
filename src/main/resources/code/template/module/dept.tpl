--Entity-info#start--------
{name:"dept", "desc":"部门管理", "treeEnable": true, "fields":[
{"name": "id", "desc":"主键ID", "type": "bigint(20)", "required": false, "comment":"主键，自增长"},
{"name": "name", "desc":"部门名称", "type": "varchar(100)"},
{"name": "childSize", "desc":"子部门数量", "type": "int(11)"},
{"name": "level", "desc":"部门层级", "type": "int(11)"},
{"name": "remark", "desc":"部门描述", "type": "text"},
{"name": "type", "desc":"部门类型", "type": "tinyint(4)", "comment":"0：区域，1：部门，默认1"},
{"name": "orderId", "desc":"排序编号", "type": "int(11)"},
{"name": "maxChildId", "desc":"子部门的最大ID", "type": "int(11)", "required": false, "comment":""},
{"name": "status", "desc":"部门状态", "type": "tinyint(4)", "required": false, "comment":"0：停用，1：正常，2：删除，默认1"},
{"name": "createTime", "desc":"创建时间", "type": "datetime"},
{"name": "createUserId", "desc":"创建用户ID", "type": "bigint(20)"},
{"name": "createUserName", "desc":"创建用户", "type": "varchar(100)"},
]}
--Entity-info#end--------
--Service-methods#AgricultureFrame#start--------
[{"name":"createDept", "desc":"为客户创建部门", "params": [{"name":"user","desc":"当前用户","type":"IUser"},{"name":"customerId","desc":"客户ID","type":"Long"},{"name":"customerName","desc":"客户名称","type":"String"}], "returnType":"DeptEntity", "throwsType":["AppException"]}]
--Service-methods#AgricultureFrame#end--------
--Service-methods#JavaEaseFrame#start--------
[{"name":"createDept", "desc":"为客户创建部门", "params": [{"name":"user","desc":"当前用户","type":"IUser"},{"name":"customerId","desc":"客户ID","type":"Long"},{"name":"customerName","desc":"客户名称","type":"String"}], "returnType":"DeptEntity", "throwsType":["BusinessException"]}]
--Service-methods#JavaEaseFrame#end--------
--ServiceImpl-imports#AgricultureFrame#start--------
["AppException","Context","IUser"]
--ServiceImpl-imports#AgricultureFrame#end--------
--ServiceImpl-imports#JavaEaseFrame#start--------
["List","ArrayList","BusinessException","Context","IUser","PageInfo","com.gm.javaeaseframe.common.util.DataUtil","com.gm.javaeaseframe.common.util.StringUtils","org.springframework.beans.BeanUtils","${ModuleQueryName}"]
--ServiceImpl-imports#JavaEaseFrame#end--------
--ServiceImpl-fields#start--------
	/**
     * 系统平台组织架构以10开头
     */
    private final String IDFIX_SYSTEM = "10";
    /**
     * 企业客户组织架构以20开头
     */
    private final String IDFIX_COMPANY = "20";
    /**
     * 企业客户的根级部门ID
     */
    private final Long COMPANY_ROOT_ID = 20001L;
    
    public static enum DataSatusEnum {
    	DISENABLE(0, "禁用"), ENABLE(1, "启用"), DELETE(9, "已删除");
    	
    	private int value;
    	private String desc;
    	DataSatusEnum(int value, String desc) {
    		this.value = value;
    		this.desc = desc;
    	}
		public int getValue() {
			return value;
		}
		public String getDesc() {
			return desc;
		}
    }
--ServiceImpl-fields#end--------
--ServiceImpl-methods#start--------
	@Override
	protected DeptEntity findBefore(DeptEntity params, Context context) throws BusinessException {
		if (params == null) {
			return params;
		}
		DeptQuery query = null;
		if (params instanceof DeptQuery) {
			query = (DeptQuery)params;
		} else {
			query = new DeptQuery();
			BeanUtils.copyProperties(params, query);
		}
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(DataSatusEnum.ENABLE.getValue());
		statusList.add(DataSatusEnum.DISENABLE.getValue());
		query.setStatusList(statusList);
		return query;
	}

	@Override
	protected DeptEntity findBefore(DeptEntity params, PageInfo pageInfo, Context context) throws BusinessException {
		return this.findBefore(params, context);
	}
	protected void saveBefore(DeptEntity entity, Context context) throws ${Exception} {
        DeptEntity parentDept = dao.get(entity.getParentId());
        if (parentDept == null) {
            throw new ${Exception}("未选择上级部门");
        }
        if(entity.getStatus() != null && entity.getStatus()==DataSatusEnum.DELETE.getValue()) {
        	 throw new ${Exception}("已删除部门不能编辑");
        }
        int currLevel = DataUtil.conver2Int(parentDept.getLevel()) + 1;
        String idFix = parentDept.getId().toString();
        int maxChildId = DataUtil.conver2Int(parentDept.getMaxChildId()) + 1;
        String idStr = StringUtils.lpad(maxChildId, 3);
        Long realId = null;
        if (parentDept.getId().longValue() == 1)//公司内部组织
        {
            realId = Long.parseLong(IDFIX_SYSTEM + idStr);
        } else if (parentDept.getId().longValue() == COMPANY_ROOT_ID.longValue())//企业客户组织，标识+客户ID
        {
            realId = Long.parseLong(idFix + entity.getId().toString());
        } else {
            realId = Long.parseLong(idFix + idStr);
        }
        entity.setId(realId);
        entity.setLevel(currLevel);
        //同级部门内名称不能重复
        DeptEntity checkCondition = new DeptEntity();
		checkCondition.setParentId(entity.getParentId());
		checkCondition.setName(entity.getName());
		List<DeptEntity> dbentitys = this.find(checkCondition, context);
		for(DeptEntity dept : dbentitys)
		{
			if(null== dept.getId() || !dept.getId().equals(entity.getId())) {
				throw new ${Exception}("部门名称已存在");
			}
		}
		
        parentDept.setMaxChildId(maxChildId);
        parentDept.setChildSize(DataUtil.conver2Int(parentDept.getChildSize()) + 1);
        super.update(parentDept, context);
        super.saveBefore(entity, context);
    }
    
    protected void updateAfter(DeptEntity entity, Context context) throws ${Exception} {
    	//更新子节点状态
    	if(entity.getStatus() != null && entity.getStatus()==DataSatusEnum.DISENABLE.getValue()) {
    		updateDepStatus(entity.getId(),DataSatusEnum.DISENABLE.getValue(), context);
    	}
    	super.updateAfter(entity, context);
    }
    
    public int remove(Long[] ids, Context context) throws ${Exception} {
    	int count = 0;
    	for(Long id : ids){
    		count+= updateDepStatus(id,DataSatusEnum.DELETE.getValue(), context);
    	}
    	return count;
    }
    /**
     * 递归修改部门状态
     * @param parentId
     * @param context
     * @return
     */
    private int updateDepStatus(Long parentId,Integer status, Context context){
    	//先更新自己
    	int size = 0;
    	DeptEntity parentEntity = this.get(parentId, context);
    	if(null!=parentEntity) {
    		parentEntity.setStatus(status);
			this.dao.update(parentEntity);
			size++;
			//修改子节点
			DeptEntity params = new DeptEntity();
	    	params.setParentId(parentId);
	    	List<DeptEntity> depList = this.find(params, context);
			if(null!=depList && depList.size()>0) {
				for(DeptEntity entity :depList) {
					entity.setStatus(status);
					this.dao.update(entity);
					//递归子节点
					size += updateDepStatus(entity.getId(),status, context);
				}
			}
    	}
		return size;
    }
    
    public DeptEntity createDept(IUser currUser, Long customerId, String customerName) throws ${Exception} {
        if (customerId == null || customerId.longValue() == 0) {
            return null;
        }
        Long deptId = Long.parseLong(COMPANY_ROOT_ID + customerId.toString());
        DeptEntity dept = dao.get(deptId);
        if (dept != null) {
            return dept;
        }
        DeptEntity entity = new DeptEntity();
        entity.initAttrValue();
        entity.setId(customerId);
        entity.setParentId(COMPANY_ROOT_ID);
        entity.setName(customerName);
        entity.setCreateTime(System.currentTimeMillis());
        if (currUser != null) {
            entity.setCreateUserId(currUser.getId());
            entity.setCreateUserName(currUser.getRealName());
        }
        return super.save(entity, null);
    }
--ServiceImpl-methods#end--------
--Controller-imports#JavaEaseFrame#start--------
--Controller-imports#JavaEaseFrame#end--------
--Controller-methods#AgricultureFrame#start--------
protected void doListBefore(HttpServletRequest request, HttpServletResponse response, ${ControllerFormName} form,
    		Map<String, Object> model, Context context) throws ${Exception} {
    	List<Integer> statusList = new ArrayList<Integer>();
    	statusList.add(DataSatusEnum.ENABLE.getValue());
    	statusList.add(DataSatusEnum.DISENABLE.getValue());
    	form.getQuery().setStatusList(statusList);
    	super.doListBefore(request, response, form, model, context);
    }
    
    protected void init(HttpServletRequest request, HttpServletResponse response, ${ControllerFormName} form,
    		Map<String, Object> model, Context context) {
    	Map<String,Object> statsus = new HashMap<String,Object>();
    	statsus.put("status", DataSatusEnum.getEnumMap(DataSatusEnum.DELETE.getValue()));
    	model.put(KEY_RESULT_DICT,statsus);
    	super.init(request, response, form, model, context);
    }
  
    @Override
    protected void saveBefore(HttpServletRequest request, HttpServletResponse response, ${ControllerFormName} form,
    		Map<String, Object> model, Context context) throws ${Exception} {
    	if(null==form.getEntity().getName()) {
    		throw new BusinessException("部门名称不能为空");
    	}
    	super.saveBefore(request, response, form, model, context);
    }
   
    /**
     * 获取部门下所有员工
     * @param request
     * @param response
     * @param form
     * @return
     */
    @RequestMapping(value="user/list")
    public String userList(HttpServletRequest request, HttpServletResponse response, ${ControllerFormName} form) {
    	JSONObject ret = new JSONObject();
        try {
        	Map<String, Object> model = new HashMap<String, Object>();
            Context context = getContext();
            String busiDesc = "查询部门用户";
            try {
            	if(null==form.getQuery().getId() || form.getQuery().getId()==0) {
            		throw new AppException("查询参数【query.id】不能为空！");
            	}
            	UserQuery query = new UserQuery();
//            	query.setDeptIds(form.getQuery().getId()+"");
                Result<UserEntity> result = userservice.find(query, form.getPageInfo(), context);
                model.put(SysConstains.RESULT_KEY, result.getList());
                model.put(SysConstains.PAGEINFO_KEY, result.getPageInfo());
                model.putAll(form.getModel());
                int valueRet = doListAfter(request, response, form, model, context);
                recordSysLog(request, busiDesc + " 【成功】");
                if(valueRet == VALUE_RESULT_SUCCESS)
                {
                	ret.put(KEY_RESULT_CODE, VALUE_RESULT_SUCCESS);
                }
                else
                {
                	ret.put(KEY_RESULT_CODE, VALUE_RESULT_FAILURE);
                }
            } catch (Exception e) {
                doException(request, busiDesc, model, e);
            }
            form.getModel().clear();
            init(request, response, form, model, context);
            ret.put(KEY_RESULT_MSG, model.remove(SysConstains.MESSAGE_INFO));
            ret.put(KEY_RESULT_QUERY, form.getQuery());
            ret.put(KEY_RESULT_DATA, model);
            return ret.toJSONString();
        } catch (Exception e) {
        	ret.put(KEY_RESULT_CODE, VALUE_RESULT_FAILURE);
        	ret.put(KEY_RESULT_MSG, super.convertException(e));
        }
        return ret.toJSONString();
    }
    /**
     * 向部门添加人员
     * @param request
     * @param response
     * @param userId
     * @param depId
     * @return
     */
    @RequestMapping(value="user/add")
    public String userAdd(HttpServletRequest request, HttpServletResponse response,String data) {
    	JSONObject ret = new JSONObject();
        try {
        	JSONObject requestData = JSONObject.parseObject(data);
        	Long depId = requestData.getLong("deptId");
        	Long userId = requestData.getLong("userId");
        	DeptEntity deptEntity = this.service.get(depId, getContext());
        	if(null==deptEntity) {
        		throw new ${Exception}("部门不存在！");
        	}
        	UserEntity user = userservice.get(userId, getContext());
        	if(null==user) {
        		throw new ${Exception}("用户不存在！");
        	}
        	Set<String> deps =  new HashSet<String>();
//        	if(StringUtils.isEmpty(user.getDeptIds()));
//        	{
//        		deps.addAll(Arrays.asList(user.getDeptIds().split(",")));
//        	}
//        	deps.add(depId+"");
//        	user.setDeptIds(deps.toString());
        	userservice.update(user, getContext());
        	ret.put(KEY_RESULT_CODE, VALUE_RESULT_SUCCESS);
            ret.put(KEY_RESULT_MSG, "添加成功！");
        } catch (Exception e) {
        	ret.put(KEY_RESULT_CODE, VALUE_RESULT_FAILURE);
        	ret.put(KEY_RESULT_MSG, super.convertException(e));
        }
        return ret.toJSONString();
    }
    /**
     * 删除部门中已存在的用户
     * @param request
     * @param response
     * @param form
     * @return
     */
    @RequestMapping(value="user/delete")
    public String userEdit(HttpServletRequest request, HttpServletResponse response, String data) {
    	JSONObject ret = new JSONObject();
        try {
        	JSONObject requestData = JSONObject.parseObject(data);
        	Long depId = requestData.getLong("deptId");
        	Long userId = requestData.getLong("userId");
        	DeptEntity deptEntity = this.service.get(depId, getContext());
        	if(null==deptEntity) {
        		throw new ${Exception}("部门不存在！");
        	}
        	UserEntity user = userservice.get(userId, getContext());
        	if(null==user) {
        		throw new ${Exception}("用户不存在！");
        	}
//        	Set<String> deps =  new HashSet<String>();
//        	if(StringUtils.isEmpty(user.getDeptIds()));
//        	{
//        		deps.addAll(Arrays.asList(user.getDeptIds().split(",")));
//        	}
//        	deps.remove(depId+"");
//        	user.setDeptIds(deps.toString());
        	userservice.update(user, getContext());
        	ret.put(KEY_RESULT_CODE, VALUE_RESULT_SUCCESS);
            ret.put(KEY_RESULT_MSG, "删除成功！");
        } catch (Exception e) {
        	ret.put(KEY_RESULT_CODE, VALUE_RESULT_FAILURE);
        	ret.put(KEY_RESULT_MSG, super.convertException(e));
        }
        return ret.toJSONString();
    }
--Controller-methods#AgricultureFrame#end--------

--Controller-methods#JavaEaseFrame#start--------
--Controller-methods#JavaEaseFrame#end--------