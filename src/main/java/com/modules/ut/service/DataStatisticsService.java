package com.modules.ut.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface DataStatisticsService extends IService<Map> {
    Map<String, Object> userWorkTimeList(Map<String,Object> queryMap);
    Map<String, Object> projectWorkTimeList(Map<String,Object> queryMap);
    Map<String, Object> performanceList(Map<String,Object> queryMap);


}
