package com.modules.ut.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modules.ut.entity.UTWriteMonth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UTWriteMonthDao extends BaseMapper<UTWriteMonth> {
    List<UTWriteMonth> userManagerUtList(@Param(("param")) Map param);
    List<Map> userUtList(@Param(("param")) Map param);
}
