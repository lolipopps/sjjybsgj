package com.sjjybsgj.common.user.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sjjybsgj.common.user.mapper.SysUserMapper;
import com.sjjybsgj.common.user.mapper.UserRoleRelMapper;
import com.sjjybsgj.common.user.model.SysUser;
import com.sjjybsgj.common.user.model.UserRoleRel;
import com.sjjybsgj.common.user.model.UserRoleRelExample;
import com.sjjybsgj.core.annotation.MapperInject;
import com.sjjybsgj.core.controller.BaseController;
import com.sjjybsgj.core.model.MsgModel;
import com.sjjybsgj.core.model.PageModel;

/**
 * 名称：UserController<br>
 *
 * 描述：用户管理模块<br>
 *
 * @author Yanzheng 严正<br>
 * 时间：<br>
 * 2017-09-07 15:43:05<br>
 * 版权：<br>
 * Copyright 2017 <a href="https://github.com/micyo202" target="_blank">https://github.com/micyo202</a>. All rights reserved.
 */
@Controller
@RequestMapping("/common/user")
public class UserController extends BaseController {

	@MapperInject(SysUserMapper.class)
	private SysUserMapper mapper;

	@RequestMapping("/manage")
	public String manage() {
		return "common/user/manage";
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public PageModel<SysUser> list(int offset, int limit, String search, String sort, String order) {
		this.offsetPage(offset, limit);
		List<SysUser> list = mapper.selectByExample(null);
		return this.resultPage(list);
	}

	/**
	 * 用户对应角色保存方法<br>
	 *
	 * @param userId 用户Id
	 * @param roleStr 角色列表字符串
	 * @return MsgModel 消息模型
	 */
	@RequestMapping(value = "/roleSave", method = RequestMethod.POST)
	@ResponseBody
	public MsgModel roleSave(String userId, String roleStr) {
		List<String> roleIds = Arrays.asList(roleStr.split(","));
		UserRoleRelMapper mapper = this.getMapper(UserRoleRelMapper.class);

		// 先清除历史数据
		UserRoleRelExample example = new UserRoleRelExample();
		example.createCriteria().andUserIdEqualTo(userId);
		mapper.deleteByExample(example);

		// 添加
		for (String roleId : roleIds) {
			if (!this.isNull(roleId.trim())) {
				UserRoleRel userRoleRel = new UserRoleRel();
				userRoleRel.setRelId(this.getUUID());
				userRoleRel.setUserId(userId);
				userRoleRel.setRoleId(roleId);
				mapper.insertSelective(userRoleRel);
			}
		}
		return this.resultMsg("保存成功");
	}

	@RequestMapping("/upload")
	@ResponseBody
	public String upload(HttpServletRequest request) {
		List<String> fileNames = this.fileUpLoad(request);
		System.out.println(fileNames);
		return "success";
	}

}
