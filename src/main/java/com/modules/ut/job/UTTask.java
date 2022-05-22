/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.modules.ut.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.modules.job.task.ITask;
import com.modules.ut.entity.UTWriteMonth;
import com.modules.ut.entity.UserPerformance;
import com.modules.ut.service.DataStatisticsService;
import com.modules.ut.service.UTWriteManagerService;
import com.modules.ut.service.UTWriteMonthService;
import com.modules.ut.service.UserPerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 计算绩效
 *
 * @author wangzhipeng
 */
@Component("UTTask")
public class UTTask implements ITask {
    private Logger logger = LoggerFactory.getLogger(UTTask.class);
    @Autowired
    private UTWriteMonthService utWriteMonthService;

    @Override
    public void run(String params) {
        logger.debug("UTTask定时任务正在执行，参数为：{}", params);
        JSONObject jsonObject = JSONObject.parseObject(params);
        String yearMonth = jsonObject.getString("yearMonth");
        if (yearMonth == null || yearMonth.equals("")) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            if (month < 10) {
                yearMonth = year + "-0" + month;
            } else {
                yearMonth = year + "-" + month;
            }
        }
        JSONArray departmentIds = jsonObject.getJSONArray("departmentIds");
        for (int i = 0; i < departmentIds.size(); i++) {
            int departmentId = departmentIds.getInteger(i);
            utWriteMonthService.remove(new QueryWrapper<UTWriteMonth>()
                    .eq("year_and_month", yearMonth)
                    .eq("department_id", departmentId));
            Map map = new HashMap();
            map.put("yearAndMonth", yearMonth);
            map.put("departmentId", departmentId);
            List<UTWriteMonth> list = utWriteMonthService.userManagerUtList(map);
            if (list.size() > 0) {
                for (UTWriteMonth utWriteMonth : list) {
                    utWriteMonth.setStatus(0);
                    utWriteMonth.setCreateTime(new Date());
                    utWriteMonth.setModifiedTime(new Date());
                }
                utWriteMonthService.saveBatch(list);
                logger.info("该部门【" + departmentId + "】本月【" + yearMonth + "】工时统计成功");
            } else {
                logger.info("该部门【" + departmentId + "】本月【" + yearMonth + "】项目负责人还没有核对工时");
            }
        }

        logger.debug("UTTask定时任务正在执行，参数为：{}", params);
    }
}
