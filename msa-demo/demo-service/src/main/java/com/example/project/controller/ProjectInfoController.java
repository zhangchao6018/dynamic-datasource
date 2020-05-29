package com.example.project.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.demo.dao.domain.TbTest;
import com.example.demo.service.ChangeDatasourceService;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.System.out;
/**
 * @Description:
 * @Author: zhangchao22
 **/
@RestController
@RequestMapping("/api")
public class ProjectInfoController {

	@Autowired
	private ChangeDatasourceService changeDatasourceService;


	@ApolloConfig
	private Config config;

	@Autowired
	DruidDataSource dataSource;

	@Value("${datasource.dynamic.enabled:false}")
	private boolean dynamicDatasourceEnabled;


	/**
	 * 获取应用项目信息
	 * @return
	 */
	@RequestMapping(value="/changeDatasource",method = RequestMethod.GET)
	public String changeDatasource() {
		TbTest tbTest = changeDatasourceService.selectByPrimaryKey("1");
		out.println(tbTest);
		return "ok";
	}

	@RequestMapping(value="/test",method = RequestMethod.GET)
	public Object test() {
		TbTest tbTest = changeDatasourceService.selectByPrimaryKey("1");
		//out.println(currentThread().getName() + "\t "+dynamicDatasourceEnabled);
		out.println(tbTest);
		//out.println("maxWait:"+dataSource.getMaxWait());
		return tbTest;
	}
	
}