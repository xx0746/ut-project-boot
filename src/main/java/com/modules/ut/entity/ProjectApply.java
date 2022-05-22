package com.modules.ut.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.modules.ut.VO.ProjectApplyUserVo;
import lombok.Data;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@TableName("project_apply")
@Data
public class ProjectApply {
    private Long id;
    private String code;
    private String name;
    private Long userId;
    private String userName;
    private Long departmentId;
    private String departmentName;
    private String startTime;
    private String endTime;
    private BigDecimal ut;
    private String applyMonth;
    private String contents;
    private String expectedResult;
    private String according;
    private String level;
    private String projectType;
    private Integer projectStatus;
    private Date createTime;
    private Date modifiedTime;
    @TableField(exist = false)
    private List<Long> numberIds;
    @TableField(exist = false)
    List<ProjectApplyUserVo> numbers;
    @TableField(exist = false)
    String numberNames;
}
