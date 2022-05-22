package com.modules.ut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modules.ut.entity.ProjectApplyUser;

import java.util.Map;

public interface ProjectApplyUserService extends IService<ProjectApplyUser> {
    Map<String, Object> queryProjectApplyUserList(Map<String, Object> queryMap);
}
