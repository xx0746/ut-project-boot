package com.modules.ut.VO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkTypeVO {
    private Long id;
    private Long parentId;
    private String workType;
    private List<WorkTypeVO> children = new ArrayList<>();

    public WorkTypeVO() {}

    public WorkTypeVO(Long id, Long parentId, String workType) {
        this.id = id;
        this.parentId = parentId;
        this.workType = workType;
    }

    public void addChildren(WorkTypeVO workTypeVO) {
        children.add(workTypeVO);
    }
}
