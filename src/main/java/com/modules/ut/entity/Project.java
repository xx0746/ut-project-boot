package com.modules.ut.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("project")
@Data
public class Project {
    @ExcelIgnore
    private Long id;
    @ExcelProperty(value = "项目编号", index = 1)
    private String code;
    @ExcelProperty(value = "项目名称", index = 2)
    private String name;
    @ExcelIgnore
    private String excelMonth;
    @ExcelProperty(value = "启动时间", index = 3)
    private Date startTime;
    @ExcelProperty(value = "完成时间", index = 4)
    private Date endTime;
    @ExcelIgnore
    private Long departmentId;
    @ExcelIgnore
    private Long projectApplyId;
    @ExcelProperty(value = "牵头部门", index = 5)
    private String departmentName;
    @ExcelIgnore
    private Long userId;
    @ExcelProperty(value = "项目组长", index = 6)
    private String userName;
    @ExcelProperty(value = "项目总工时", index = 7)
    private BigDecimal ut;
    @ExcelProperty(value = "立项依据", index = 8)
    private String according;
    @ExcelProperty(value = "项目级别", index = 9)
    private String level;
    @TableField(exist = false)
    @ExcelProperty(value = "项目成员", index = 10)
    private String projectUserNames;
    @ExcelIgnore
    private String projectType;
    @ExcelIgnore
    private String projectState;
    @ExcelIgnore
    private Long excelUserId;
    @ExcelIgnore
    private Date createTime;
    @ExcelIgnore
    private Date modifiedTime;

}
