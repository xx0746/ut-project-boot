package com.modules.ut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modules.ut.entity.ProjectApply;

import java.util.Map;

public interface ProjectApplyService extends IService<ProjectApply> {
    Map<String, Object> getProjectByMap(Map<String,Object> queryMap);

}
