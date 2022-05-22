package com.modules.ut.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modules.ut.VO.ProjectApplyUserVo;
import com.modules.ut.entity.ProjectApplyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProjectApplyUserDao extends BaseMapper<ProjectApplyUser> {
    List<ProjectApplyUserVo> queryProjectApplyUserList(@Param(("param")) Map param);
    Long queryProjectApplyUserCount(@Param(("param")) Map param);
}
