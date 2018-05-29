package com.sjjybsgj.source_db.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sjjybsgj.checked_log.mapper.CheckedLogMapper;
import com.sjjybsgj.checked_log.model.CheckedLog;
import com.sjjybsgj.common.login.model.LoginUser;
import com.sjjybsgj.core.annotation.MapperInject;
import com.sjjybsgj.core.controller.BaseController;
import com.sjjybsgj.core.model.JsonModel;
import com.sjjybsgj.core.model.MsgModel;
import com.sjjybsgj.core.model.PageModel;
import com.sjjybsgj.core.persistence.DelegateMapper;
import com.sjjybsgj.core.support.DBUtils;
import com.sjjybsgj.rule_table.model.RuleTable;
import com.sjjybsgj.rule_table.model.RuleTableWithBLOBs;
import com.sjjybsgj.source_db.mapper.SourceDbMapper;
import com.sjjybsgj.source_db.model.SourceDb;
import com.sjjybsgj.standard_db.mapper.StandardDbMapper;
import com.sjjybsgj.standard_db.model.StandardDb;
import com.sjjybsgj.update_log.mapper.UpdateLogMapper;
import com.sjjybsgj.update_log.model.UpdateLog;

/**
 * 名称：RoleController<br>
 *
 * 描述：角色管理模块<br>
 *
 * @author Yanzheng 严正<br>
 *         时间：<br>
 *         2017-09-21 10:00:57<br>
 *         版权：<br>
 *         Copyright 2017 <a href="https://github.com/micyo202" target=
 *         "_blank">https://github.com/micyo202</a>. All rights reserved.
 */
@Controller
@RequestMapping("/common/jiaoyan")
public class JiaoyanController extends BaseController {

	private static final String NAMESPACE = "com.sjjybsgj.common.role.mapper.SysRoleCustomMapper";

	@MapperInject
	private DelegateMapper delegateMapper;

	@MapperInject(UpdateLogMapper.class)
	private UpdateLogMapper mapper;

	@MapperInject(SourceDbMapper.class)
	private SourceDbMapper SourceDbMapper;

	@MapperInject(StandardDbMapper.class)
	private StandardDbMapper standardDbMapper;

	@MapperInject(CheckedLogMapper.class)
	private CheckedLogMapper checkLogMapper;

	@RequestMapping("/manage")
	public String manage() {
		return "common/jiaoyan/manage";
	}

	/**
	 * 
	 * 获取所有校验日志
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public PageModel<UpdateLog> logList(int offset, int limit, String search, String sort, String order) {
		this.offsetPage(offset, limit);
		List<UpdateLog> list = mapper.selectByExample(null);
		return this.resultPage(list);
	}

	/**
	 * 获取角色 tree 结构<br>
	 *
	 * @param id
	 *            父Id
	 * @return List<RoleNode> 角色节点列表集合
	 */
	@RequestMapping(value = "/dbList", method = RequestMethod.POST)
	@ResponseBody
	public JsonModel tableList() {
		List<SourceDb> list = SourceDbMapper.selectByExample(null);
		return this.resultJson(list);
	}

	@RequestMapping(value = "/jiaoyandb", method = RequestMethod.POST)
	@ResponseBody
	public MsgModel jiaoyandb(String dbName, String userId) {
		String status = "1";
		String message = "";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("dbName", dbName);
		paramMap.put("userId", userId);
		System.out.println(dbName + " " + userId);

		// 获取库的连接方式
		SourceDb sourceDb = delegateMapper
				.selectOne("com.sjjybsgj.common.jiaoyan.mapper.jiaoyanMapper.getjiaoyanSource", paramMap);
		LoginUser user = this.getSessionUser();
		CheckedLog record = new CheckedLog();
		record.setUserId(userId);
		record.setDbName(dbName);
		if (sourceDb == null) {
			status = "0";
			message = "数据库连不上";
		} else {
			String dbType = sourceDb.getDbType();
			// 得到了带校验的表
			List<Map<String, String>> ruleLists = delegateMapper
					.selectList("com.sjjybsgj.common.jiaoyan.mapper.jiaoyanMapper.getjiaoyantableRule", dbName);
			DBUtils dbutils = new DBUtils(sourceDb);
	
			LoginUser loguser = this.getSessionUser();
			for (Map<String, String> ruleList : ruleLists) { // 校验一个库里面的所有标准
				System.out.println("这个规则: " + ruleList.values() + "总规则为: " + ruleLists.size());
				String ruleId = ruleList.get("RULE_ID");
				String rangeValue = ruleList.get("RANGE_VALUE");
				String tableName = ruleList.get("TABLE_NAME");
				String complex = ruleList.get("COMPLEX");
				String cloumnName = ruleList.get("CLOUMN_NAME");

				record.setLogId(this.getUUID());
				record.setRuleId(ruleId);
				record.setTableName(tableName);
				record.setVaildDate(new Date());
				record.setCloumnName(cloumnName);
				
				String results = "";
				String mss = "";
				System.out.println(rangeValue + "----------" + complex);
				if (rangeValue != null && !rangeValue.trim().equals("")) {

					record.setRuleType(1);

					String sql = dbutils.getRangeSql(rangeValue, dbName, tableName, cloumnName, dbType);

					results = dbutils.execQuery(sql);
					System.out.println(results);
					if (results == null) {
						mss = "表: " + tableName + " 字段: " + cloumnName + " 规则: " + rangeValue + "通过";

						record.setInfo(mss);
						record.setIfPass(1);

					} else {

						mss = "表: " + tableName + " 字段:  " + cloumnName + " 规则: " + rangeValue + "这个规则不通过";
						status = "0";
						message = mss + "表 " + tableName + "规则: " + rangeValue;

						record.setInfo(mss);
						record.setIfPass(0);

					}

					checkLogMapper.insert(record);

				}
				if (complex != null && !complex.trim().equals("")) {
					results = dbutils.execQuery(complex);
					if (results == null) {

						mss = "表: " + tableName + " 字段: " + cloumnName + " 规则: " + complex + "通过";

						record.setInfo(mss);
						record.setIfPass(1);

					} else {
						mss = "表: " + tableName + " 字段:  " + cloumnName + " 规则: " + complex + "这个规则不通过";
						status = "0";
						message = mss + "表 " + tableName + "规则: " + complex;

						record.setInfo(mss);
						record.setIfPass(0);
					}

					checkLogMapper.insert(record);

				}

			}
		}

		return new MsgModel(status, message);
	}

}
