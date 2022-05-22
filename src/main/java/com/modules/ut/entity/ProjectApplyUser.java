package com.modules.ut.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("project_apply_user")
@Data
public class ProjectApplyUser {
    private Long id;
    private Long userId;
    private Long projectId;
    private BigDecimal ut;
    private Date createTime;
    private Date modifiedTime;

}
