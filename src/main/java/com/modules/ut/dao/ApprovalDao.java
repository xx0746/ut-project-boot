package com.modules.ut.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modules.ut.entity.UTWrite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApprovalDao extends BaseMapper<UTWrite> {
    List<Map> projectApprovalList(@Param(("param")) Map param);
    Long projectApprovalCount(@Param(("param")) Map param);
    List<Map> managerApprovalWorkTimeList(@Param(("param")) Map param);
    List<Map> utApprovalList(@Param(("param")) Map param);
    Long utApprovalCount(@Param(("param")) Map param);
    List<Map> utApprovalProjectList(@Param(("param")) Map param);
}
