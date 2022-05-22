/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.modules.sys.entity.SysDepartmentEntity;
import com.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysDepartmentService extends IService<SysDepartmentEntity> {

	Map<String, Object> getDepartmentByMap(Map<String,Object> queryMap);

	/**
	 * 删除部门
	 */
	void deleteBatch(Long[] ids);
}
