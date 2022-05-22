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
import com.modules.ut.entity.UserPerformance;
import com.modules.ut.service.*;
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
@Component("salaryTask")
public class SalaryTask implements ITask {
    private Logger logger = LoggerFactory.getLogger(SalaryTask.class);

    @Autowired
    private UTWriteMonthService utWriteMonthService;
    @Autowired
    private UserPerformanceService userPerformanceService;

    @Override
    public void run(String params) {
        logger.debug("SalaryTask定时任务正在执行，参数为：{}", params);
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
            userPerformanceService.remove(
                    new QueryWrapper<UserPerformance>()
                            .eq("year_and_month", yearMonth)
                            .eq("department_id", departmentId)
            );

            Map map = new HashMap();
            map.put("yearAndMonth", yearMonth);
            map.put("departmentId", departmentId);
            List<Map> list = utWriteMonthService.userUtList(map);
            if (list.size() > 0) {
                List<UserPerformance> userPerformanceList = new ArrayList<>();
                for (Map map1 : list) {
                    UserPerformance userPerformance = new UserPerformance();
                    BigDecimal performance = new BigDecimal("0");
                    BigDecimal ut = (BigDecimal)map1.get("ut");
                    BigDecimal ratio = (BigDecimal)map1.get("ratio");
                    BigDecimal syntheticalPerformance = new BigDecimal("2000");
                    BigDecimal utPrice = new BigDecimal("340");
                    if(ut!=null && ratio!=null){
                        BigDecimal utPerformance = ut.multiply(utPrice).multiply(ratio);
                        performance = syntheticalPerformance.add(utPerformance);
                    }
                    userPerformance.setUserId((Long)map1.get("userId"));
                    userPerformance.setDepartmentId((Long)map1.get("departmentId"));
                    userPerformance.setYearAndMonth(yearMonth);
                    userPerformance.setUt(ut);
                    userPerformance.setUserRatio(ratio);
                    userPerformance.setPerformance(performance);
                    userPerformance.setSyntheticalPerformance(syntheticalPerformance);
                    userPerformance.setUtPrice(utPrice);
                    userPerformance.setCreateTime(new Date());
                    userPerformance.setModifiedTime(new Date());
                    userPerformanceList.add(userPerformance);
                }
                userPerformanceService.saveBatch(userPerformanceList);
                logger.info("该部门【" + departmentId + "】本月【" + yearMonth + "】计算绩效成功");
            } else {
                String errStr = "【员工工时还没审批】；";
                logger.info("该部门【" + departmentId + "】本月【" + yearMonth + "】暂时无法计算绩效：" + errStr);
            }
        }

        logger.debug("SalaryTask定时任务正在执行，参数为：{}", params);
    }
}
