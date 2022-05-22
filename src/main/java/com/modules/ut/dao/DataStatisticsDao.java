package com.modules.ut.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modules.ut.VO.PerformanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataStatisticsDao extends BaseMapper<Map> {
    List<Map> userWorkTimeList(@Param(("param")) Map param);
    Long userWorkTimeCount(@Param(("param")) Map param);
    List<Map> projectWorkTimeList(@Param(("param")) Map param);
    Long projectWorkTimeCount(@Param(("param")) Map param);
    List<PerformanceVO> performanceList(@Param(("param")) Map param);
    Long performanceCount(@Param(("param")) Map param);

}
