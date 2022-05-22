package com.modules.ut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modules.ut.entity.ProjectUser;

import java.util.Map;

public interface ProjectUserService extends IService<ProjectUser> {
    Map<String, Object> personList(Long current, Long size, Long projectId);
}
