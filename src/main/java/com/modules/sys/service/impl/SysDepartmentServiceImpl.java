/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.modules.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.modules.sys.dao.SysDepartmentDao;
import com.modules.sys.entity.SysDepartmentEntity;
import com.modules.sys.service.SysDepartmentService;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("SysDepartmentService")
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentDao, SysDepartmentEntity> implements SysDepartmentService {


	@Override
	public Map<String, Object> getDepartmentByMap(Map<String,Object> queryMap){

		Page page = null;
		if(queryMap.get("page") !=null && queryMap.get("limit") !=null ){
			page = new Page((int)queryMap.get("page"),(int)queryMap.get("limit"));
		}else{
			page = new Page();
		}
		QueryWrapper<SysDepartmentEntity> wrapper = new QueryWrapper<>();

		if(queryMap.get("departmentName") !=null && !queryMap.get("departmentName").toString().equals("")){
			wrapper.like("department_name",queryMap.get("departmentName"));
		}

		Page resultPage = baseMapper.selectPage(page, wrapper);
		Map<String,Object> map = new HashMap<>();
		map.put("current",resultPage.getCurrent());
		map.put("size",resultPage.getSize());
		map.put("total",resultPage.getTotal());
		map.put("dataList",resultPage.getRecords());
		return map;
	}
	@Override
	public void deleteBatch(Long[] ids) {
		this.removeByIds(Arrays.asList(ids));
	}

}