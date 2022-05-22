package com.modules.ut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modules.sys.service.SysUserService;
import com.modules.ut.VO.ProjectApplyUserVo;
import com.modules.ut.dao.ProjectApplyDao;
import com.modules.ut.entity.ProjectApply;
import com.modules.ut.service.ProjectApplyService;
import com.modules.ut.service.ProjectApplyUserService;
import com.modules.ut.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.*;

@Service("projectApplyService")
public class ProjectApplyServiceImpl extends ServiceImpl<ProjectApplyDao, ProjectApply> implements ProjectApplyService {
    @Autowired
    private ProjectApplyUserService projectApplyUserService;

    @Override
    public Map<String, Object> getProjectByMap(Map<String, Object> queryMap) {
        Page page = null;
        if (queryMap.get("current") != null && queryMap.get("size") != null) {
            page = new Page((int) queryMap.get("current"), (int) queryMap.get("size"));
        } else {
            page = new Page();
        }
        QueryWrapper<ProjectApply> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("apply_month");
        if (queryMap.get("applyMonth") != null && !queryMap.get("applyMonth").toString().equals("")) {
            wrapper.eq("apply_month", queryMap.get("applyMonth"));
        }
        if (queryMap.get("userId") != null) {
            wrapper.eq("user_id", queryMap.get("userId"));
        }
        if (queryMap.get("departmentId") != null) {
            wrapper.eq("department_id", queryMap.get("departmentId"));
        }
        if (queryMap.get("projectStatus") != null) {
            wrapper.eq("project_status", queryMap.get("projectStatus"));
        }
        if("0".equals(queryMap.get("roleType"))){
            if("1".equals(queryMap.get("status"))){
                Integer[] abc = {-1,0,1,2,3};
                wrapper.in("project_status", Arrays.asList(abc));
            }else if ("2".equals(queryMap.get("status"))){
                wrapper.eq("project_status", 4);
            }
        }
        if (queryMap.get("userName") != null) {
            wrapper.like("user_name", queryMap.get("userName"));
        }
        if (queryMap.get("departmentName") != null) {
            wrapper.like("department_name", queryMap.get("departmentName"));
        }
        Page resultPage = baseMapper.selectPage(page, wrapper);
        List<ProjectApply> list = resultPage.getRecords();
        if (list.size() > 0) {
            for (ProjectApply projectApply : list) {
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("projectId", projectApply.getId());
                List<ProjectApplyUserVo> numberList = (List<ProjectApplyUserVo>) projectApplyUserService
                        .queryProjectApplyUserList(objectMap).get("dataList");
                projectApply.setNumbers(numberList);
                List<Long> numberIds = new ArrayList<>();
                String numberNames = "";
                for (ProjectApplyUserVo projectApplyUserVo : numberList) {
                    numberIds.add(projectApplyUserVo.getUserId());
                    if("".equals(numberNames)){
                        numberNames = projectApplyUserVo.getUserName();
                    }else {
                        numberNames += "，" + projectApplyUserVo.getUserName();
                    }
                }
                projectApply.setNumberIds(numberIds);
                projectApply.setNumberNames(numberNames);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("current", resultPage.getCurrent());
        map.put("size", resultPage.getSize());
        map.put("total", resultPage.getTotal());
        map.put("dataList", resultPage.getRecords());
        map.put("roleType", queryMap.get("roleType"));
        return map;
    }
}
