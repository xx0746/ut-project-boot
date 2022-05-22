package com.modules.ut.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modules.ut.VO.UTWriteVO;
import com.modules.ut.entity.UTWrite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Mapper
public interface UTWriteDao extends BaseMapper<UTWrite> {
    List<UTWriteVO> writeList(@Param(("param")) Map param);
    Long writeListCount(@Param(("param")) Map param);
    List<Map> writeProjectList(@Param("userId") Long userId, @Param("start") long l, @Param("size") Long size);
    Long projectCount(@Param("userId") Long userId);
    List<Map> utDataStatisticsUserDetailList(@Param(("param")) Map param);
    List<Map> utDataStatisticsProjectDetailList(@Param(("param")) Map param);

}
