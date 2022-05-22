package com.modules.ut.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("ut_write_manager")
public class UTWriteManager {
    private Long id;
    private String yearAndMonth;
    private String writeDate;
    private Long projectId;
    private Long userId;
    private Long departmentId;
    private BigDecimal ut;
    private Integer status;
    private Date createTime;
    private Date modifiedTime;
}
