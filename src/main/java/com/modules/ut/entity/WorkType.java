package com.modules.ut.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("work_type")
public class WorkType {
    private Long id;
    private Long parentId;
    private String name;
}
