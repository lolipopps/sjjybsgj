package com.sjjybsgj.source_db.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sjjybsgj.common.role.mapper.RoleMenuRelMapper;
import com.sjjybsgj.common.role.mapper.SysRoleMapper;
import com.sjjybsgj.common.role.model.RoleMenuRel;
import com.sjjybsgj.common.role.model.RoleMenuRelExample;
import com.sjjybsgj.common.role.model.RoleNode;
import com.sjjybsgj.common.role.model.SysRole;
import com.sjjybsgj.common.role.model.SysRoleExample;
import com.sjjybsgj.common.user.model.SysUser;
import com.sjjybsgj.core.annotation.MapperInject;
import com.sjjybsgj.core.controller.BaseController;
import com.sjjybsgj.core.model.JsonModel;
import com.sjjybsgj.core.model.MsgModel;
import com.sjjybsgj.core.model.PageModel;
import com.sjjybsgj.core.persistence.DelegateMapper;
import com.sjjybsgj.source_db.mapper.SourceDbMapper;
import com.sjjybsgj.source_db.model.SourceDb;
import com.sjjybsgj.stardard_table.mapper.StardardTableMapper;
import com.sjjybsgj.stardard_table.model.StardardTable;
import com.sjjybsgj.update_log.mapper.UpdateLogMapper;
import com.sjjybsgj.update_log.model.UpdateLog;

/**
 * 名称：RoleController<br>
 *
 * 描述：角色管理模块<br>
 *
 * @author Yanzheng 严正<br>
 * 时间：<br>
 * 2017-09-21 10:00:57<br>
 * 版权：<br>
 * Copyright 2017 <a href="https://github.com/micyo202" target="_blank">https://github.com/micyo202</a>. All rights reserved.
 */
@Controller
@RequestMapping("/common/datasource")
public class SourceDbController extends BaseController {

	private static final String NAMESPACE = "com.sjjybsgj.common.role.mapper.SysRoleCustomMapper";

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
	
	
	
	
}
