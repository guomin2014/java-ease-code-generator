package com.gm.easecode;

import com.gm.easecode.common.vo.AppModuleBuilder;
import com.gm.easecode.common.vo.ControllerClassStyleMode;
import com.gm.easecode.config.AppConfigBuilder;
import com.gm.easecode.frame.FrameworkProviderFactory;
import com.gm.easecode.service.AppCodeService;
import com.gm.easecode.source.provider.spi.MysqlDataSource;

public class CodeGenerator {

	public static void main(String[] args) {
		String dbUrl = "jdbc:mysql://127.0.0.1:3306/mqtransfer?useUnicode=true&characterEncoding=utf8&useSSL=false&nullCatalogMeansCurrent=true";
		String dbUser = "root";
		String dbPwd = "123456";
		AppConfigBuilder builder = new AppConfigBuilder().setAppName("mqtransfer")// 应用名称
				.setCompanyPackage("com.gm")// 应用父级包结构，默认com.joyes，将与appName组合成完成父级包路径，比如：com.joyes.appname
				.setFramework(FrameworkProviderFactory.FrameworkProviderMode.JavaEaseFrame)// 基础框架
				.setControllerClassStyle(ControllerClassStyleMode.DTO)// Controller类风格
				.createSystemModule(true)// 是否创建系统模块，如是，则添加整个应用启动的相关配置，且可直接运行
				.createEntityFile(true)// 是否创建实体文件代码（model与xml）
				.createDaoFile(true)// 是否创建dao代码
				.createServiceFile(true)// 是否创建service代码
				.createControllerFile(true)// 是否创建controller代码(包含dto代码)
				.createSwagger(true)// 是否生成swagger注解，如是，则在dto中添加注解
				.createSqlFile(false)// 是否创建sql文件
			    .addModule(new AppModuleBuilder().forName("集群管理").forIdentify("cluster").forSubModuleEnable(true).forTables("gm_mqtransfer_cluster,gm_mqtransfer_cluster_node").build())
			    .addModule(new AppModuleBuilder().forName("任务管理").forIdentify("task").forSubModuleEnable(true).forTables("gm_mqtransfer_task").build())
			    .setDataSource(new MysqlDataSource(dbUrl, dbUser, dbPwd))// 数据来源
			    .setCodeSavePath("/Users/shenzhuyu/Desktop/mqtransfer");// 生成代码存储路径
		AppCodeService.createCode(builder.build());
	}

}
