package com.modules.ut.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("user_performance")
public class UserPerformance {
    private Long id;
    private Long userId;
    private Long departmentId;
    private String yearAndMonth;
    private BigDecimal ut;
    private BigDecimal syntheticalPerformance;
    private BigDecimal utPrice;
    private BigDecimal userRatio;
    private BigDecimal performance;
    private Date createTime;
    private Date modifiedTime;

}
