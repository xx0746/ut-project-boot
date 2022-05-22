package com.modules.ut.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modules.ut.VO.ProjectUserVO;
import com.modules.ut.entity.ProjectUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectUserDao extends BaseMapper<ProjectUser> {
    List<ProjectUserVO> personList(@Param(("start")) long start, @Param(("size")) Long size, @Param("projectId") Long projectId);
}
