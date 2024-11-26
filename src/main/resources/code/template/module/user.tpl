--Entity-info#start--------
{name:"user", "desc":"用户管理", "fields":[
{"name": "id", "desc":"主键ID", "type": "bigint(20)", "required": false, "comment":"主键，自增长"},
{"name": "loginName", "desc":"登录名", "type": "varchar(100)", "required": true},
{"name": "loginPwd", "desc":"登录密码", "type": "varchar(200)", "required": true},
{"name": "loginLimitAddress", "desc":"登录限制地址", "type": "varchar(2000)","comment":"多个IP地址用逗号分隔，可以使用IP段匹配，如：172.17.*非空：则只能该值内的IP可以登录"},
{"name": "name", "desc":"用户名", "type": "varchar(100)", "required": true},
{"name": "headImg", "desc":"头像", "type": "varchar(200)", "comment":"图片路径地址"},
{"name": "mobile", "desc":"联系电话", "type": "varchar(20)"},
{"name": "email", "desc":"联系邮箱", "type": "varchar(100)"},
{"name": "userType", "desc":"用户类型", "type": "tinyint(4)", "comment":"0：系统用户，1：普通用户，默认0"},
{"name": "status", "desc":"用户状态", "type": "tinyint(4)", "comment":"0：停用，1：正常，2：冻结，3：销户，默认1"},
{"name": "createTime", "desc":"创建时间", "type": "datetime"},
{"name": "createUserId", "desc":"创建用户ID", "type": "bigint(20)"},
{"name": "createUserName", "desc":"创建用户", "type": "varchar(100)"},
]}
--Entity-info#end--------