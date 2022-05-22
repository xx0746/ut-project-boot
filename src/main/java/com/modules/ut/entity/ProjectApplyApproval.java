package com.modules.ut.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("project_apply_approval")
@Data
public class ProjectApplyApproval {
    private Long id;
    private Long approvalUserId;
    private Long projectApplyId;
    private String approvalUserName;
    private String approvalOpinion;
    private String approvalType;
    private Date createTime;
    private Date modifiedTime;
}
