package com.modules.ut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modules.sys.entity.SysUserEntity;
import com.modules.ut.entity.Project;

import java.util.Map;

public interface ProjectService extends IService<Project> {
    Map<String, Object> getProjectByMap(Map<String,Object> queryMap);

}
