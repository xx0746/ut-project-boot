package com.modules.ut.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UTWriteAddVO {

    private Long projectId;
    private String writeDate;
    private List<UTWriteVO> dataList;
}
