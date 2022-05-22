package com.modules.ut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modules.sys.service.SysUserService;
import com.modules.ut.dao.ProjectDao;
import com.modules.ut.entity.Project;
import com.modules.ut.service.ProjectService;
import com.modules.ut.service.UTWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("projectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectDao, Project> implements ProjectService {
    @Autowired
    private UTWriteService UTWriteService;
    @Autowired
    private SysUserService sysUserService;
    @Override
    public Map<String, Object> getProjectByMap(Map<String,Object> queryMap) {
        Page page = null;
        if(queryMap.get("current") !=null && queryMap.get("size") !=null){
            page = new Page((int)queryMap.get("current"),(int)queryMap.get("size"));
        }else{
            page = new Page();
        }
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("excel_month");
        if(queryMap.get("excelMonth") !=null && !queryMap.get("excelMonth").toString().equals("")){
            wrapper.eq("excel_month",queryMap.get("excelMonth"));
        }
        if(queryMap.get("userId") !=null ){
            wrapper.eq("user_id",queryMap.get("userId"));
        }
        if(queryMap.get("departmentId") !=null){
            wrapper.eq("department_id",queryMap.get("departmentId"));
        }
        if(queryMap.get("userName") !=null ){
            wrapper.like("user_name",queryMap.get("userName"));
        }
        if(queryMap.get("departmentName") !=null){
            wrapper.like("department_name",queryMap.get("departmentName"));
        }
        Page resultPage = baseMapper.selectPage(page, wrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("current",resultPage.getCurrent());
        map.put("size",resultPage.getSize());
        map.put("total",resultPage.getTotal());
        map.put("dataList",resultPage.getRecords());
        map.put("roleType", queryMap.get("roleType"));
        return map;
    }
}
