/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.modules.sys.controller;


import com.common.annotation.SysLog;
import com.common.utils.R;

import com.common.validator.ValidatorUtils;
import com.common.validator.group.AddGroup;
import com.common.validator.group.UpdateGroup;
import com.modules.sys.entity.SysDepartmentEntity;
import com.modules.sys.entity.SysUserEntity;
import com.modules.sys.service.impl.SysDepartmentServiceImpl;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/department")
public class SysDepartmentController extends AbstractController {
	@Autowired
	private SysDepartmentServiceImpl sysDepartmentService;


	/**
	 * 所有部门列表
	 */
	@PostMapping("/list")
	public R list(@RequestBody Map<String, Object> params){
		Map<String,Object> result = sysDepartmentService.getDepartmentByMap(params);
		return R.ok(result);

	}
	/**
	 * 保存部门
	 */
	@PostMapping("/save")
	public R save(@RequestBody SysDepartmentEntity departmentEntity){
		departmentEntity.setCreateTime(new Date());
		departmentEntity.setModifiedTime(new Date());
		sysDepartmentService.save(departmentEntity);
		return R.ok();
	}
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PostMapping("/update")
	public R update(@RequestBody SysDepartmentEntity departmentEntity){
		departmentEntity.setModifiedTime(new Date());
		sysDepartmentService.updateById(departmentEntity);
		return R.ok();
	}
	/**
	 * 删除部门
	 */
	@PostMapping("/delete")
	public R delete(@RequestBody Long[] ids){

		sysDepartmentService.deleteBatch(ids);

		return R.ok();
	}
}
