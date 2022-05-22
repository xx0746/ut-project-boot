package com.modules.ut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modules.ut.VO.ProjectUserVO;
import com.modules.ut.dao.ProjectUserDao;
import com.modules.ut.entity.ProjectUser;
import com.modules.ut.service.ProjectService;
import com.modules.ut.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("projectUserService")
public class ProjectUserServiceImpl extends ServiceImpl<ProjectUserDao, ProjectUser> implements ProjectUserService {


    @Autowired
    private ProjectService projectService;

    @Override
    public Map<String, Object> personList(Long current, Long size, Long projectId) {
        if (current == null) {
            current = 1l;
        }
        if (size == null) {
            size = Long.valueOf(Integer.MAX_VALUE);
        }
        List<ProjectUserVO> projectUserVOS = baseMapper.personList((current-1) * size, size, projectId);
        long total = baseMapper.selectCount(new QueryWrapper<ProjectUser>().eq("project_id",projectId));
        //Project project = projectService.getById(projectId);
        long usedUt = 0;
        for (ProjectUserVO projectUserVO : projectUserVOS) {
            usedUt += projectUserVO.getUt() == null ? 0 : projectUserVO.getUt();
        }
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("msg", "success");
        returnMap.put("current", current);
        returnMap.put("total", total);
        returnMap.put("code", 0);
        returnMap.put("size", projectUserVOS.size());
        returnMap.put("usedUt", usedUt);
        returnMap.put("dataList", projectUserVOS);
        return returnMap;
    }
}
