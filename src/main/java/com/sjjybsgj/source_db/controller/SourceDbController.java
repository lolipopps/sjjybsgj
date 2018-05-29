package com.sjjybsgj.source_db.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sjjybsgj.core.annotation.MapperInject;
import com.sjjybsgj.core.controller.BaseController;

import com.sjjybsgj.core.model.PageModel;
import com.sjjybsgj.core.persistence.DelegateMapper;
import com.sjjybsgj.source_db.mapper.SourceDbMapper;
import com.sjjybsgj.source_db.model.SourceDb;
import com.sjjybsgj.standard_db.model.StandardDb;

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
@RequestMapping("/common/datasource")
public class SourceDbController extends BaseController {

	private static final String NAMESPACE = "com.sjjybsgj.common.jiaoyan.mapper.jiaoyanMapper";

	@MapperInject
	private DelegateMapper delegateMapper;

	@MapperInject(SourceDbMapper.class)
	private SourceDbMapper mapper;

	@RequestMapping("/manage")
	public String manage() {
		return "common/datasource/manage";
	}

	/**
	 * 
	 * 获取所有校验日志
	 */
	@RequestMapping(value = "/listsource", method = RequestMethod.POST)
	@ResponseBody
	public PageModel<SourceDb> logList(int offset, int limit, String search, String sort, String order) {
		this.offsetPage(offset, limit);
		List<SourceDb> list = mapper.selectByExample(null);
		return this.resultPage(list);
	}

	@RequestMapping(value = "/dbTree", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<HashMap<String, Object>> dbTree() {
		ArrayList<HashMap<String, Object>> listnode = new ArrayList<HashMap<String, Object>>();
		System.out.println(this.getSessionUser().getUserId());
		List<SourceDb> sourceDbs = delegateMapper.selectList(NAMESPACE+".getjiaoyanDbnamebyUserId",
				this.getSessionUser().getUserId());
		List<String> tableNames = delegateMapper.selectList(NAMESPACE + ".getUpdatedTabel",
				this.getSessionUser().getUserId());
		for (SourceDb sourceDb : sourceDbs) {
			HashMap<String, Object> rmap = new HashMap<String, Object>();
			rmap.put("id", sourceDb.getDbSourceId());
			rmap.put("name", sourceDb.getDbName());
			rmap.put("isParent", true);
			rmap.put("pId", sourceDb.getDbSourceId()); // pId 要大写才能有

			rmap.put("open", false);
			listnode.add(rmap);
			List<StandardDb> standardDbs = delegateMapper
					.selectList(NAMESPACE+".getStandardDb", sourceDb.getDbName());
			for (StandardDb standardDb : standardDbs) {
				HashMap<String, Object> cmap = new HashMap<String, Object>();
				cmap.put("id", sourceDb.getDbSourceId() + "-" + standardDb.getDbRuleId());
				cmap.put("name", standardDb.getTableName());
				cmap.put("isParent", false);
				cmap.put("pId", sourceDb.getDbSourceId());
				if (tableNames.contains(standardDb.getTableName())) {
					cmap.put("valid", false);
				}

				listnode.add(cmap);
			}

		}

		return listnode;
	}

}
