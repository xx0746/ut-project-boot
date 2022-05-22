package com.modules.ut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modules.ut.VO.PerformanceVO;
import com.modules.ut.dao.DataStatisticsDao;
import com.modules.ut.entity.UTWrite;
import com.modules.ut.entity.UTWriteMonth;
import com.modules.ut.service.DataStatisticsService;
import com.modules.ut.service.UTWriteMonthService;
import com.modules.ut.service.UTWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("DataStatisticsService")
public class DataStatisticsServiceImpl extends ServiceImpl<DataStatisticsDao, Map> implements DataStatisticsService {
    @Autowired
    private UTWriteService UTWriteService;

    @Autowired
    private UTWriteMonthService utWriteMonthService;
    @Override
    public Map<String, Object> userWorkTimeList(Map<String, Object> queryMap) {
        int current = (int) queryMap.get("current");
        int size = (int) queryMap.get("size");
        queryMap.put("start", (current - 1) * size);
        queryMap.put("size", size);
        List<Map> list = baseMapper.userWorkTimeList(queryMap);
        long total = baseMapper.userWorkTimeCount(queryMap);
        Map<String, Object> map = new HashMap<>();
        map.put("current", queryMap.get("current"));
        map.put("size", queryMap.get("size"));
        map.put("total", total);
        map.put("dataList", list);
        return map;
    }
    @Override
    public Map<String, Object> projectWorkTimeList(Map<String, Object> queryMap) {
        int current = (int) queryMap.get("current");
        int size = (int) queryMap.get("size");
        queryMap.put("start", (current - 1) * size);
        queryMap.put("size", size);
        List<Map> list = baseMapper.projectWorkTimeList(queryMap);
        long total = baseMapper.projectWorkTimeCount(queryMap);
        Map<String, Object> map = new HashMap<>();
        map.put("current", queryMap.get("current"));
        map.put("size", queryMap.get("size"));
        map.put("total", total);
        map.put("dataList", list);
        return map;
    }
    @Override
    public Map<String, Object> performanceList(Map<String, Object> queryMap) {
        long total = 0;
        if(queryMap.get("current") !=null && queryMap.get("size") !=null){
            int current = (int) queryMap.get("current");
            int size = (int) queryMap.get("size");
            queryMap.put("start", (current - 1) * size);
            queryMap.put("size", size);
            total = baseMapper.performanceCount(queryMap);
        }
        List<PerformanceVO> list = baseMapper.performanceList(queryMap);
        if(total ==0){
            total = list.size();
        }
        String errStr = "";
        if(list.size()==0){
            errStr = "原因：";
            QueryWrapper<UTWriteMonth> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("year_and_month", queryMap.get("yearAndMonth"))
                    .eq("status", 2);
            if( queryMap.get("departmentId")!=null){
                queryWrapper .eq("department_id",  queryMap.get("departmentId"));
            }
            int utWriteCount = utWriteMonthService.count(queryWrapper);
            if (utWriteCount == 0) {
                errStr+="【未查询到本月审批通过的工时】";
            }else {
                errStr+="【本月还未计算绩效，请先联系管理员计算绩效】";
            }

        }
        Map<String, Object> map = new HashMap<>();
        map.put("errStr",errStr);
        map.put("current", queryMap.get("current"));
        map.put("size", queryMap.get("size"));
        map.put("total", total);
        map.put("dataList", list);
        return map;
    }
}
